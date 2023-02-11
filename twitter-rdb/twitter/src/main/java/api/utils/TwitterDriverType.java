package api.utils;

import api.DPDatabaseAPI;
import api.mysql.DPDatabaseMysql;
import api.redis.DPDatabaseRedis;
import api.redis.DPDatabaseRedisEC;

/**
 * Factory to create instances of different databases API classes.
 */
public class TwitterDriverType{

    // denotes whether to flush the Redis connection
    public boolean flush;

    /**
     * Enum of API types callable from the client.
     */
    public static enum DatabaseType {
        MYSQL, REDIS, REDISEC
    }

    public TwitterDriverType(String flush){
        // constructor handles the user input regarding flushing
        if(flush.equalsIgnoreCase("yes")){
            this.flush = true; 
        }
        else if(flush.equalsIgnoreCase("no")){
            this.flush = false;
        }
        else{
            this.flush = true;
            System.out.println("Your database will be flushed. You entered an invalid command");
        }
    }

    /**
     * Creates an instance of the specified API.
     * @param d_type - the database implementation the user wishes to use.
     * @return an instance of the specified API.
     */
    public DPDatabaseAPI createTwitterAPI(DatabaseType d_type) {
        // switch statement for each database implementation
        switch (d_type) {
            // MySQL case
            case MYSQL: 
                // initialize API
                DPDatabaseMysql mysql_api = new DPDatabaseMysql();

                // set connection string, username, and password
                String url =  "jdbc:mysql://localhost:3306/twitter?serverTimezone=EST5EDT";
                String user = System.getenv("TWITTER_USERNAME");
                String password = System.getenv("TWITTER_PASSWORD");

                // authenticate the API
				mysql_api.authenticate(url, user, password); // DON'T HARDCODE PASSWORDS!
                return mysql_api;
            
            case REDIS:
                // Base Redis case
                DPDatabaseRedis redis_api = new DPDatabaseRedis();

                // handle flushing if asked
                if(flush){
                    redis_api.flushRedis();
                }
                return redis_api;

            case REDISEC:
                // EC Redis case
                DPDatabaseRedisEC redis_ec_api = new DPDatabaseRedisEC();
                
                // handle flushing if asked
                if(flush){
                    redis_ec_api.flushRedis();
                }
                return redis_ec_api;

            default:
                throw new IllegalArgumentException("Cannot constuct null twitter type");
        }

    }
}