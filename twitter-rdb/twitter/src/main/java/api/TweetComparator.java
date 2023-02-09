package api;

import java.util.Comparator;


public class TweetComparator implements Comparator<Tweet> {

    @Override
    // compares the timestamps of the two tweets
    public int compare(Tweet t1, Tweet t2) {
        if (t1.equals(t2)) {
            return 0;
        }
        return t1.getTweetTimeStamp().compareTo(t2.getTweetTimeStamp());
    }
}