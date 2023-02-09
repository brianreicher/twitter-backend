package api;


/**
 * Factory class to create instances of twitter APIs to use in the client programs. Currently supports RDB, HW2Strat2,
 * and HWStrat1 implementations.
 */
public class TwitterDriverType{
    /**
     * An enum of different twitter API types that can be used in client programs.
     */
    public static enum DatabaseType {
        MYSQL, REDIS
    }

    /**
     * A static factory method for creating instances of TwitterAPIs.
     * @param tt - the twitter type you would like to construct.
     * @return an instance of a twitter API to use in client programs.
     */
    public static DPDatabaseAPI createTwitterDriver(DatabaseType d_type) {
        switch (d_type) {
            case MYSQL: 
                DPDatabaseMysql mysql_api = new DPDatabaseMysql();
                String url =  "jdbc:mysql://localhost:3306/twitter?serverTimezone=EST5EDT";
                String user = System.getenv("TWITTER_USERNAME");
                String password = System.getenv("TWITTER_PASSWORD");
				mysql_api.authenticate(url, user, password); // DON'T HARDCODE PASSWORDS!
                return mysql_api;
            
            case REDIS:
                DPDatabaseRedis redis_api = new DPDatabaseRedis();
                redis_api.flushRedis();
                return redis_api;

            default:
                throw new IllegalArgumentException("Cannot constuct null twitter type");
        }

    }
}