package api;

import java.sql.*;
import java.util.*;

import database.DBUtils;


public class DPDatabaseMysql implements DPDatabaseAPI {


    // For demonstration purposes. Better would be a constructor that takes a file path
    // and loads parameters dynamically.
    DBUtils dbu;

    public void authenticate(String url, String user, String password) {

        dbu = new DBUtils(url, user, password);
    }


    /**
     * Close the connection when application finishes
     */
    public void closeConnection() { dbu.closeConnection(); }


    @Override
    public void postTweet(Tweet t) {
        String sql = "INSERT INTO tweets (user_id, tweet_ts, tweet_text) VALUES" +
                "('"+t.getUserID()+"','"+t.getTweetTimeStamp()+"','"+t.getTweetText()+"')";
        dbu.insertOneRecord(sql);
    }


    @Override
    public void postTweets(List<Tweet> twlist) {
        String sql = "INSERT INTO tweets (user_id, tweet_text) VALUES (?,?)";
        try {
            Connection con = dbu.getConnection();
            PreparedStatement pstmt = con.prepareStatement(sql);

            for (Tweet t : twlist) {
                pstmt.setInt(1, t.getUserID());
                pstmt.setString(2, t.getTweetText());
                pstmt.execute();
            }
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }  
    }

    @Override
    public List<Tweet> getTimeline(Integer userID) {

        String sql = String.format("SELECT tweets.tweet_id as tweet_id, follows.follows_id as user_id, tweets.tweet_ts as tweet_ts, tweets.tweet_text as tweet_text " +
                     "FROM tweets JOIN follows on tweets.user_id = follows.follows_id " +
                     "WHERE follows.user_id = %s LIMIT 10;", userID);

        List<Tweet> timeline = new ArrayList<Tweet>();

        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                timeline.add(new Tweet(rs.getInt("tweet_id"), rs.getInt("user_id"), rs.getTimestamp("tweet_ts"), rs.getString("tweet_text")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return timeline;

    }


    @Override
    public Set<Integer> getFollowers(Integer userID) {
        String sql = "SELECT follows_id"+
                     "FROM follows" +
                     "where user_id = " + userID + ";";

        List<Integer> followers = new ArrayList<Integer>();

        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                followers.add(rs.getInt("follows_id"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        // utlize set to eliminate duplicates
        return new HashSet<>(followers);    
    }


    @Override
    public Set<Integer> getFollowees(Integer userID) {
        String sql = "SELECT user_id"+
                     "FROM follows" +
                     "where follows_id = " + userID + ";";

        List<Integer> followees = new ArrayList<Integer>();

        try {
        // get connection and initialize statement
            Connection con = dbu.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                followees.add(rs.getInt("user_id"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        // utlize set to eliminate duplicates
        return new HashSet<>(followees);   
    }


    @Override
    public List<Tweet> getTweets(Integer userID) {
        String sql = "SELECT *"+
                    "FROM tweets" +
                    "where follows_id = " + userID + ";";

        List<Tweet> tweets = new ArrayList<Tweet>();

        try {
        // get connection and initialize statement
            Connection con = dbu.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                tweets.add(new Tweet(rs.getInt("tweet_id"), rs.getInt("user_id"), rs.getTimestamp("tweet_ts"), rs.getString("tweet_text")));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }

        return tweets;
    }

    @Override
    public List<Integer> getAllUsers() {
        String sql = "SELECT user_id FROM tweets UNION SELECT user_id FROM follows UNION select follows_id FROM follows";

        List<Integer> users = new ArrayList<Integer>();

        try {
        // get connection and initialize statement
            Connection con = dbu.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                users.add(rs.getInt("user_id"));
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public void postFollow(User user){
        String sql = "INSERT INTO follows (user_id, follows_id) VALUES" +
        "('"+user.getUserID()+"','"+user.getFollowsID()+"')";
        dbu.insertOneRecord(sql);
    }


}
