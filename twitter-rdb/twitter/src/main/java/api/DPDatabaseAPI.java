package api;

import java.util.List;
import java.util.Set;

import api.types.Tweet;
import api.types.User;


public interface DPDatabaseAPI {

    /**
     * Post one tweet
     * @param t The tweet
     * @return
     */
    public void postTweet(Tweet t);


    /**
     * Get a given user's home timeline
     * @param userId The user's unique identifier
     * @return A list of tweets
     */
    public List<Tweet> getTimeline(Integer userID);


    /**
     * Who is following a given user
     * @param userId The user's unique identifier
     * @return A list of userIDs
     */
    public Set<Integer> getFollowers(Integer userID);


    /**
     * Who a given user user is following
     * @param userId The user's unique identifier
     * @return A list of userIDs
     */
    public Set<Integer> getFollowees(Integer userID); // alternate using a stored procedure


    /**
     * Tweets posted by a given userID
     * @param userId The user's unique identifier
     * @return A list of tweets
     */
    public List<Tweet> getTweets(Integer userID);

    public List<Integer> getAllUsers();
        

    /**
     * Close the connection when application finishes
     */
    public void closeConnection();


    public void postFollow(User user);

}
