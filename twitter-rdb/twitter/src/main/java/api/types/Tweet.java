package api.types;

import java.sql.*;


/*
 * Type Class for a Tweet object
 */
public class Tweet {

    private int tweet_ID; // unique identifier for each tweet
    private int user_ID; // id of the poster of the tweet
    private Timestamp tweet_ts; // timestamp of the tweet
    private String tweet_text; // text of the tweet


    public Tweet(int user_ID, Timestamp tweet_ts, String tweet_text) {
        this.tweet_ID = -1; // set id to -1 if not specified
        this.user_ID = user_ID;
        this.tweet_ts = tweet_ts;
        this.tweet_text = tweet_text;
    }

    public Tweet(int tweet_ID, int user_ID, Timestamp tweet_ts, String tweet_text) {
        this.tweet_ID = tweet_ID;
        this.user_ID = user_ID;
        this.tweet_ts = tweet_ts;
        this.tweet_text = tweet_text;
    }

    public Tweet(int user_ID, String tweet_text) {
        this.user_ID = user_ID;
        this.tweet_text = tweet_text;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "tweet_ID=" + tweet_ID + '\'' +
                ", user_ID='" + user_ID + '\'' +
                ", tweet_ts='" + tweet_ts +  '\'' +
                ", tweet_text=" + tweet_text + '\'' +
                '}';
    }

    public int getTweetID() {
        return tweet_ID;
    }

    public void setTweetID(int tweet_ID) {
        this.tweet_ID = tweet_ID;
    }

    public int getUserID() {
        return user_ID;
    }

    public void setUserID(int user_ID){
        this.user_ID = user_ID;
    }

    public Timestamp getTweetTimeStamp() {
        return tweet_ts;
    }

    public void setTweetTimeStamp(Timestamp tweet_ts) {
        this.tweet_ts = tweet_ts;
    }    
    
    public String getTweetText() {
        return tweet_text;
    }

    public void setTweetText(String tweet_text) {
        this.tweet_text = tweet_text;
    }
}
