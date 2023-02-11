package api.redis;

import java.util.*;

import api.types.Tweet;
import api.utils.TweetComparator;


/*
 * Implementation of the Extra Credit method of the Twitter API.
 */
public class DPDatabaseRedisEC extends DPDatabaseRedis {

    public DPDatabaseRedisEC(){
        // initialize the superclas
        super();
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
        // list of followees for a given user
        List<Integer> follows = new ArrayList<>(this.getFollowees(userID));

        // initialize a list for all possible tweets to send as uploaded
        List<Tweet> total_tweets = new ArrayList<Tweet>();
        for (int followID : follows) {
            // utilize getRecentTweets() method to add 10 recent tweets per followID
            total_tweets.addAll(this.getRecentTweets(followID, TWEETS_PREFIX, 0, 9));
        }

        // sort the total tweets with a TweetComparator
        total_tweets.sort(new TweetComparator());

        // list of final tweets for the use timeline
        List<Tweet> timeline = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            // loop for 10 tweets and add to the timeline from the back of the allTweets list (most recent)
            if (total_tweets.size() > 0) {
                timeline.add(total_tweets.remove(total_tweets.size() - 1));
            }
        }
        return timeline;    
    }

}
