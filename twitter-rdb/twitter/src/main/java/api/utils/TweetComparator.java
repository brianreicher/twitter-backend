package api.utils;

import java.util.Comparator;

import api.types.Tweet;

/*
 * Comparator to compare the timestamps of two tweet objects.
 */
public class TweetComparator implements Comparator<Tweet> {

    /**
     * Compares the timestamps of the two tweets
     * @return The greater of the two timestamps.
     */
    @Override
    public int compare(Tweet t1, Tweet t2) {
        if (t1.equals(t2)) {
            return 0;
        }
        return t1.getTweetTimeStamp().compareTo(t2.getTweetTimeStamp());
    }
}