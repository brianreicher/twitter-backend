package api.utils;

import api.DPDatabaseAPI;
import api.mysql.DPDatabaseMysql;
import api.redis.DPDatabaseRedis;
import api.redis.DPDatabaseRedisEC;

/**
 * Factory class to create instances of twitter APIs to use in the client programs. Currently supports RDB, HW2Strat2,
 * and HWStrat1 implementations.
 */
public class TwitterDriverType{

    public boolean flush;

    /**
     * An enum of different twitter API types that can be used in client programs.
     */
    public static enum DatabaseType {
        MYSQL, REDIS, REDISEC
    }
    public TwitterDriverType(String flush){
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
     * A static factory method for creating instances of TwitterAPIs.
     * @param tt - the twitter type you would like to construct.
     * @return an instance of a twitter API to use in client programs.
     */
    public DPDatabaseAPI createTwitterAPI(DatabaseType d_type) {
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
                if(flush){
                    redis_api.flushRedis();
                }
                return redis_api;

            case REDISEC:
                DPDatabaseRedisEC redis_ec_api = new DPDatabaseRedisEC();
                if(flush){
                    redis_ec_api.flushRedis();
                }
                return redis_ec_api;

            default:
                throw new IllegalArgumentException("Cannot constuct null twitter type");
        }

    }
}