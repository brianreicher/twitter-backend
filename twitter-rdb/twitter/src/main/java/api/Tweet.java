package api;

public class Tweet {

    private int tweet_ID;
    private int user_ID;
    private String tweet_ts;
    private String tweet_text;


    public Tweet(int user_ID, String tweet_ts, String tweet_text) {
        this.tweet_ID = -1;
        this.user_ID = user_ID;
        this.tweet_ts = tweet_ts;
        this.tweet_text = tweet_text;
    }

    public Tweet(int tweet_ID, int user_ID, String tweet_ts, String tweet_text) {
        this.tweet_ID = tweet_ID;
        this.user_ID = user_ID;
        this.tweet_ts = tweet_ts;
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

    public String getTweetTimeStamp() {
        return tweet_ts;
    }

    public void setTweetTimeStamp(String tweet_ts) {
        this.tweet_ts = tweet_ts;
    }    
    
    public String getTweetText() {
        return tweet_text;
    }

    public void setTweetText(String tweet_text) {
        this.tweet_text = tweet_text;
    }


}
