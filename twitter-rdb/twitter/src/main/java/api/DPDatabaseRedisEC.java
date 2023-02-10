package api;

import java.util.*;


public class DPDatabaseRedisEC extends DPDatabaseRedis {

    public DPDatabaseRedisEC(){
        super();
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
        List<Integer> follows = new ArrayList<>(this.getFollowees(userID));

        List<Tweet> total_tweets = new ArrayList<Tweet>();
        for (int followId : follows) {
            total_tweets.addAll(this.getTimeline(followId));
        }
        total_tweets.sort(new TweetComparator());
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            if (total_tweets.size() > 0) {
                tweets.add(total_tweets.remove(total_tweets.size() - 1));
            }
        }
        return tweets;
    }
}
