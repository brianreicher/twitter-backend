package API;

import java.util.Date;


public class Tweet {
    private int id;
    private int tweetId;
    private int userId;
    private Date tweetTS;
    private String tweetText;


    public Tweet(int tweetId, int userId, Date tweetTS, String tweetText){
        this.tweetId = tweetId;
        this.userId = userId;
        this.tweetTS = tweetTS;
        this.tweetText = tweetText;
    }



    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public int getTweetId(){return tweetId;}
    public void setTweetId(int tweetId){this.tweetId = tweetId;}

    public int getUserId(){return userId;}
    public void setUserId(int userId){this.userId = userId;}

    public Date getTweetTS(){return tweetTS;}
    public void setTweetTS(Date tweetTS){this.tweetId = tweetId;}

    public String getTweetText(){return tweetText;}
    public void setTweetText(String tweetText){this.tweetText = tweetText;}

}