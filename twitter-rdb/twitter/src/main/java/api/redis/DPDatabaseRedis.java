package api.redis;

import java.sql.Timestamp;
import java.util.*;

import api.DPDatabaseAPI;
import api.types.Tweet;
import api.types.User;
import redis.clients.jedis.*;
import database.DBUtils;


public class DPDatabaseRedis implements DPDatabaseAPI {

    public final Jedis jedis_connection = new DBUtils().j;

    // user & user-related prefixes
    public final String USERS_SET = "users";
    public final String FOLLOWERS_PREFIX = "followers:";
    public final String FOLLOWS_PREFIX = "follows:";

    // timeline & total tweets prefixes
    public final String TIMELINE_PREFIX = "timeline:";
    public final String TWEETS_PREFIX = "tweets:";
    
    // tweet-related prefixes
    public final String TWEET_HASH_KEY = "tweet:";
    public final String TWEET_TXT_KEY = "tweetText";
    public final String TWEET_TS_KEY = "tweetTimeStamp";
    public final String LATEST_TWEET_ID_KEY = "latestTweetID";

    
    @Override
    public void closeConnection() {
        jedis_connection.close();
    }

    public void flushRedis(){
        jedis_connection.flushAll();
        jedis_connection.set("latestTweetId", "0");

    }

    @Override
    public void postTweet(Tweet t) {

        String latestTweet = jedis_connection.get(LATEST_TWEET_ID_KEY);
        this.jedis_connection.hset(TWEET_HASH_KEY + latestTweet, TWEET_TS_KEY, t.getTweetTimeStamp().toString());
        this.jedis_connection.hset(TWEET_HASH_KEY + latestTweet, TWEET_TXT_KEY, t.getTweetText());

        Set<Integer> followers = this.getFollowers(t.getUserID());

        for (Integer follower : followers) {
            this.jedis_connection.lpush(TIMELINE_PREFIX + follower, latestTweet);
        }
        this.jedis_connection.lpush(TWEETS_PREFIX + t.getUserID(), latestTweet);
        jedis_connection.incr(LATEST_TWEET_ID_KEY);

    }

    @Override
    public List<Tweet> getTimeline(Integer userID) {
        List<Tweet> tweet_list = this.getRecentTweets(userID, TIMELINE_PREFIX, 0, 9);
        return tweet_list;   
    }

    @Override
    public List<Tweet> getTweets(Integer userID) {
        List<Tweet> tweet_list = this.getRecentTweets(userID, TWEETS_PREFIX,0, -1);
        System.out.println("Number of user tweets: " + tweet_list.size());
        return tweet_list;    
    }

    public List<Tweet> getRecentTweets(Integer userID, String redis_element, Integer start_idx, Integer end_idx){
        List<String> tweet_IDs = jedis_connection.lrange(redis_element + userID, start_idx, end_idx);

        List<Tweet> tweet_list = new ArrayList<Tweet>();
        for (String tweet : tweet_IDs) {
            int tweet_id = Integer.valueOf(tweet);

            tweet_list.add(new Tweet(
                                    tweet_id,
                                    userID,
                                    Timestamp.valueOf(jedis_connection.hget(TWEET_HASH_KEY + tweet_id, TWEET_TS_KEY)),
                                    jedis_connection.hget(TWEET_HASH_KEY + tweet_id, TWEET_TXT_KEY)
                                    ));
        }
        return tweet_list;
    }

    @Override
    public Set<Integer> getFollowers(Integer userID) {
        Set<String> followers = jedis_connection.smembers(FOLLOWERS_PREFIX + userID);

        // convert set of strings to ints
        Set<Integer> integer_set = new HashSet<Integer>();
        for (String f : followers) {
          integer_set.add(Integer.valueOf(f));
        }
        return integer_set;
    }


    @Override
    public Set<Integer> getFollowees(Integer userID) {
        Set<String> followees_set = jedis_connection.smembers(FOLLOWS_PREFIX + userID);

        // convert set of strings to ints
        Set<Integer> integer_set = new HashSet<Integer>();
        for (String f : followees_set) {
          integer_set.add(Integer.valueOf(f));
        }
        return integer_set;
    }


    @Override
    public List<Integer> getAllUsers() {
        Set<String> all_users = jedis_connection.smembers(USERS_SET);

        List<Integer> integer_list = new ArrayList<Integer>();
        for (String user : all_users) {
            integer_list.add(Integer.valueOf(user));
        }
        return integer_list;
    }

    @Override
    public void postFollow(User user) {
 
        jedis_connection.sadd(USERS_SET, String.valueOf(user.getUserID()));
        jedis_connection.sadd(USERS_SET, String.valueOf(user.getFollowsID()));

        String usr_followers = FOLLOWERS_PREFIX + user.getFollowsID();
        String usr_following = FOLLOWS_PREFIX + user.getUserID();
        jedis_connection.sadd(usr_followers, String.valueOf(user.getUserID()));
        jedis_connection.sadd(usr_following, String.valueOf(user.getFollowsID()));

    }

}