package api;

import java.util.*;

import redis.clients.jedis.*;
import database.DBUtils;


public class DPDatabaseRedis implements DPDatabaseAPI {


    // For demonstration purposes. Better would be a constructor that takes a file path
    // and loads parameters dynamically.
    private final Jedis jedis_connection = new DBUtils().j;
    public static String LATEST_TWEET_ID_KEY = "latestTweetId";
    public static String FOLLOWERS_PREFIX = "followers:";
    public static String FOLLOWS_PREFIX = "follows:";
    public static String TIMELINE_PREFIX = "timeline:";
    public static String TWEETS_PREFIX = "tweets:";
    public static String USERS_SET = "users";
    public static String TWEET_HASH_KEY = "tweet:";
    public static String TWEET_TXT_KEY = "tweetText";
    public static String TWEET_TS_KEY = "tweetTimeStamp";
    
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
        this.jedis_connection.lpush(TWEETS_PREFIX + t.getUserID(), latestTweet);
        jedis_connection.incr(LATEST_TWEET_ID_KEY);
    }


    @Override
    public List<Tweet> getTimeline(Integer userID) {
        return new ArrayList<Tweet>();
    }


    @Override
    public List<Tweet> getTweets(Integer userID) {
        return new ArrayList<Tweet>();
    }

    @Override
    public Set<Integer> getFollowers(Integer userID) {
        return new HashSet<Integer>();
    }


    @Override
    public Set<Integer> getFollowees(Integer userID) {
        return new HashSet<Integer>();
    }


    @Override
    public List<Integer> getAllUsers() {
        return new ArrayList<Integer>();
    }

    @Override
    public void postFollow(User formatUserFromCSV) {
        
    }
}
