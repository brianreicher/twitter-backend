package api;

import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
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
        String sql = "INSERT INTO tweets (user_id, tweet_text) VALUES" +
                "('"+t.getUserID()+"','"+t.getTweetText()+"')";

        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection(); // get the active connection
            Statement stmt = con.createStatement();
            stmt.executeUpdate(sql);

            // Cleanup
            stmt.close();

        } catch (SQLException e) {
            System.err.println("ERROR: Could not insert record: "+sql);
            System.err.println(e.getMessage());
            e.printStackTrace();
        }        
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

        // TODO: update query
        String sql = "select tweet_id, user_id, tweet_ts, tweet_text"+
                     "from tweets t join follows f on (t.user_id = f.follows_id) " +
                     "where user_id = " + userID + ";";


        List<Tweet> timeline = new ArrayList<Tweet>();

        try {
            // get connection and initialize statement
            Connection con = dbu.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next() != false)
                timeline.add(new Tweet(rs.getInt("tweet_id"), rs.getInt("user_id"), rs.getString("tweet_ts"), rs.getString("tweet_text")));
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
        return timeline;

    }


    @Override
    public List<Integer> getFollowers(Integer userID) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Integer> getFollowees(Integer userID) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public List<Tweet> getTweets(Integer userID) {
        // TODO Auto-generated method stub
        return null;
    }
}
