package api.redis;

import java.sql.Timestamp;
import java.util.*;

import api.DPDatabaseAPI;
import api.types.Tweet;
import api.types.User;
import redis.clients.jedis.*;
import database.DBUtils;

/*
 * Implementation of the required Redis method for the Twitter API.
 */
public class DPDatabaseRedis implements DPDatabaseAPI {

    // initialize the Redis connection, utilizing the DBUtils jedis 
    public final Jedis jedis_connection = new DBUtils().j;

    // user & user-related prefixes & values
    public final String USERS_SET = "users";
    public final String FOLLOWERS_PREFIX = "followers:";
    public final String FOLLOWS_PREFIX = "follows:";

    // timeline & total tweets prefixes
    public final String TIMELINE_PREFIX = "timeline:";
    public final String TWEETS_PREFIX = "tweets:";
    
    // tweet-related prefixes & values
    public final String TWEET_HASH_KEY = "tweet:";
    public final String TWEET_TXT_KEY = "tweetText";
    public final String TWEET_TS_KEY = "tweetTimeStamp";
    public final String LATEST_TWEET_ID_KEY = "latestTweetID";

    /**
     * Close the Jedis connection when application finishes
     */
    @Override
    public void closeConnection() {
        jedis_connection.close();
    }

    /**
     * Flush redis of all existing data
     */
    public void flushRedis(){
        jedis_connection.flushAll();

        // set the value of the "latestTweetID" to 0
        jedis_connection.set("latestTweetID", "0");

    }

    /**
     * Post one tweet
     * @param t The tweet
     */
    @Override
    public void postTweet(Tweet t) {
        // get the latest tweet ID
        String latestTweet = jedis_connection.get(LATEST_TWEET_ID_KEY);

        // set the latest tweet timestamp & text given the parameter values
        this.jedis_connection.hset(TWEET_HASH_KEY + latestTweet, TWEET_TS_KEY, t.getTweetTimeStamp().toString());
        this.jedis_connection.hset(TWEET_HASH_KEY + latestTweet, TWEET_TXT_KEY, t.getTweetText());

        // get followers of the tweets's user
        Set<Integer> followers = this.getFollowers(t.getUserID());

        // send the tweet to all follower's timelines
        for (Integer follower : followers) {
            this.jedis_connection.lpush(TIMELINE_PREFIX + follower, latestTweet);
        }

        // push the tweet to the user's personal tweet list
        this.jedis_connection.lpush(TWEETS_PREFIX + t.getUserID(), latestTweet);

        // increment the tweet_id key
        jedis_connection.incr(LATEST_TWEET_ID_KEY);

    }


    /**
     * Get a given user's home timeline
     * @param userId The user's unique identifier
     * @return A list of tweets
     */
    @Override
    public List<Tweet> getTimeline(Integer userID) {
        // set and return a list of 10 tweets from a given user's timeline, utilzing the getRecentTweets() method
        List<Tweet> timeline = this.getRecentTweets(userID, TIMELINE_PREFIX, 0, 9);
        return timeline;   
    }

    /**
     * Tweets posted by a given userID
     * @param userId The user's unique identifier
     * @return A list of tweets
     */
    @Override
    public List<Tweet> getTweets(Integer userID) {
        // set and return a list of all tweets posted by a given user, utilzing the getRecentTweets() method
        List<Tweet> tweet_list = this.getRecentTweets(userID, TWEETS_PREFIX,0, -1);
        return tweet_list;    
    }

    /**
     * Helper method to get a list of tweets from OR for a given user & specific Redis element
     * @param userID The user's unique identifier
     * @param redis_element The Redis element to utilize
     * @param start_idx The starting index for retrieving tweets
     * @param end_idx The ending index for retrieving tweets
     * @return A list of tweets
     */
    public List<Tweet> getRecentTweets(Integer userID, String redis_element, Integer start_idx, Integer end_idx){
        // list of tweet id strings for the range, user, and type
        List<String> tweet_IDs = jedis_connection.lrange(redis_element + userID, start_idx, end_idx);

        // For each string of tweet_IDs & the userID, convert to and create a list of Tweet objects
        List<Tweet> tweet_list = new ArrayList<Tweet>();
        for (String tweet : tweet_IDs) {
            int tweet_id = Integer.valueOf(tweet);

            // create and add tweet
            tweet_list.add(new Tweet(
                                    tweet_id, // tweet_id
                                    userID, // userID
                                    // timestamp associated with tweet_id
                                    Timestamp.valueOf(jedis_connection.hget(TWEET_HASH_KEY + tweet_id, TWEET_TS_KEY)),
                                    // text associated with tweet_id
                                    jedis_connection.hget(TWEET_HASH_KEY + tweet_id, TWEET_TXT_KEY)
                                    ));
        }
        return tweet_list;
    }

     /**
     * Who is following a given user
     * @param userId The user's unique identifier
     * @return A list of userIDs
     */
    @Override
    public Set<Integer> getFollowers(Integer userID) {
        // unique set of user IDs following a given user
        Set<String> followers = jedis_connection.smembers(FOLLOWERS_PREFIX + userID);

        // convert the string set to a set of integers
        Set<Integer> integer_set = new HashSet<Integer>();
        for (String f : followers) {
          integer_set.add(Integer.valueOf(f));
        }
        return integer_set;
    }

    /**
     * Who a given user user is following
     * @param userId The user's unique identifier
     * @return A list of userIDs
     */
    @Override
    public Set<Integer> getFollowees(Integer userID) {
        // unique set of user IDs that a given user follows
        Set<String> followees_set = jedis_connection.smembers(FOLLOWS_PREFIX + userID);

        // convert the string set to a set of integers
        Set<Integer> integer_set = new HashSet<Integer>();
        for (String f : followees_set) {
          integer_set.add(Integer.valueOf(f));
        }
        return integer_set;
    }

    /**
     * Fetch all users in the database
     * @return A list of all of the users
     */
    @Override
    public List<Integer> getAllUsers() {
        //  unique set of user_id, the memebers of the 'users' set
        Set<String> all_users = jedis_connection.smembers(USERS_SET);

        // convert the string set to a set of integers
        List<Integer> integer_list = new ArrayList<Integer>();
        for (String user : all_users) {
            integer_list.add(Integer.valueOf(user));
        }
        return integer_list;
    }

    /**
     * Post a follower/followee from a CSV stream
     * @param userId The user's unique identifiers
     */
    @Override
    public void postFollow(User user) {
        // add the userID and followerID to the users set
        jedis_connection.sadd(USERS_SET, String.valueOf(user.getUserID()));
        jedis_connection.sadd(USERS_SET, String.valueOf(user.getFollowsID()));

        // set the user follower/following strings indicies
        String usr_followers = FOLLOWERS_PREFIX + user.getFollowsID();
        String usr_following = FOLLOWS_PREFIX + user.getUserID();

        // add to to the follower/following value at the index for the given set
        jedis_connection.sadd(usr_followers, String.valueOf(user.getUserID()));
        jedis_connection.sadd(usr_following, String.valueOf(user.getFollowsID()));
    }

}
