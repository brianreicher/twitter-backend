package api;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;


/**
 * This program exercises the DPDatabaseAPI (MySQL implementation).
 * Notice that nothing other than the instantiation of the API shows us that
 * the underlying database is Relational, or MySQL.

 */
public class TwitterDriver {

	/**
	 * Driver method to get a specific user's timeline information. 
	 * Implemented here with a MySQL API call and easily interchangeable with Redis.
	 * @param user_id The specified user_id
	 */
	public static void getHomeTimelines(DPDatabaseAPI api, Integer user_id){
		// get list of all potential users to pick from
		List<Integer> all_users = api.getAllUsers();

		// initialize the total time calculator
		double elapsed_time = 0;

		// if the specified user_id is in the user database, fetch the user's timeline
		if(all_users.contains(user_id)){
			// start the timer
			long start_time = System.nanoTime();
			// API Call to get timeline information
			api.getTimeline(user_id);
			// end the timer
			long end_time = System.nanoTime();
			// calculate the elapsed time
			elapsed_time = (end_time - start_time)/10e8;
		}
		else{
			System.out.println("USER_NOT_FOUND: " + user_id);
		}


		// RETURN METRICS FOR FETCHED TIMELINE
		System.out.println("Time Taken to Get Timeline (seconds): " + elapsed_time);
	}

	/**
	 * Driver method to fetch as many user' timeline information as possible in 60 seconds. 
	 * Implemented here with a MySQL API call and easily interchangeable with Redis.
	 */
	public static void getHomeTimelines(DPDatabaseAPI api){
		// get list of all potential users to pick from
		List<Integer> all_users = api.getAllUsers();

		// counter for amount of fetched timelines
		int total_timelines_fetched = 0;

		// start the timer
		long start_time = System.nanoTime();
		// initialize the timer end
		long end_time = 0;
		// while the total time elapsed is less than 60 seconds, fetch a random timeline
		while(((end_time - start_time)/1e9) < 60){
			// grabbing a random user_id from all total users
			int random_user_id = all_users.get((int)(Math.random() * all_users.size()));
			// API Call to get a user timeline 
			api.getTimeline(random_user_id);
			// increment the total timeline counter
			total_timelines_fetched++;
			// increment the end time counter
			end_time = System.nanoTime();
		}

		// calculate theelapsed time
		double elapsed_time = (end_time - start_time)/10e8;


		// RETURN METRICS FOR FETCHED TIMELINE
		System.out.println("Total Elapsed Time (seconds): " + elapsed_time);
        System.out.println("Total Timelines Fetched: " + total_timelines_fetched);
        System.out.println("Timelines per Second: " + total_timelines_fetched/elapsed_time);
	}

	/**
	 * Driver method to post all tweets to the database. 
	 * Implemented here with a MySQL API call and easily interchangeable with Redis.
	 */
	public static void postAllTweets(DPDatabaseAPI api){
		// initialize the timer
		long start_time = 0;

		// read CSV file containing tweets
		String csv_line;
		BufferedReader buff;
		int total_tweets_posted = 0; 
		try {
			// initialize the buffer and read each line
			buff = new BufferedReader(new FileReader("db/tweet.csv"));
			// start the timer
			start_time = System.nanoTime();
			buff.readLine();
			// if the line is not empty, the call the API and post the tweet
			while((csv_line = buff.readLine()) != null) {
				api.postTweet(formatTweetFromCSV(csv_line, total_tweets_posted));
				// increment the total post counter
				total_tweets_posted++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// end the timer
		long end_time = System.nanoTime();

		// calculate the elapsed time
		double elapsed_time = (end_time-start_time)/10e8;

		// RETURN METRICS FOR ALL POSTED TWEETS
		System.out.println("Time Taken to Post Tweets (seconds): " + elapsed_time);
		System.out.println("Total Tweets Posted: " + total_tweets_posted);
		System.out.println("Tweets Posted per Second: " + total_tweets_posted/elapsed_time);
	}

	/**
	 * Helper method to populate the follows table of the database.
	 */
	public static void postAllFollowers(DPDatabaseAPI api){
		// Read the csv
		String csv_line;
		BufferedReader buff;
		try {
			buff = new BufferedReader(new FileReader("db/follows.csv"));
			buff.readLine();
			// if the line  is not empty, the call the API and post the follow relationship
			while((csv_line = buff.readLine()) != null) {
				api.postFollow(formatUserFromCSV(csv_line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Added all followers to the database");
	}

	/**
	 * Helper method to format 'follows.csv' entries to User objects for database insertion.
	 * @param userline The line of the CSV file.
	 * @return The newly created User object from the CSV data.
	 */
	public static User formatUserFromCSV(String userline){
		// list of user values
		List<String> user_values = new ArrayList<String>();

		// scan the line, seperating values via a comma
		try(Scanner uScanner = new Scanner(userline)){
			uScanner.useDelimiter(",");
			while (uScanner.hasNext()){
				// add the seperated values to the list
				user_values.add(uScanner.next());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// return the new user object containing the line's contents
		return new User(
						Integer.parseInt(user_values.get(0)),
						Integer.parseInt(user_values.get(1))
		);
	}

	/**
	 * Helper method to format 'tweet.csv' entries to Tweet objects for posting.
	 * @param tweetline The line of the CSV file.
	 * @return The newly created Tweet object from the CSV data.
	 */
	public static Tweet formatTweetFromCSV(String tweetline, int tweet_id){
		// list of tweet values
		List<String> tweet_values = new ArrayList<String>();

		// scan the line, seperating values via a comma
		try(Scanner tweetScanner = new Scanner(tweetline)){
			tweetScanner.useDelimiter(",");
			while (tweetScanner.hasNext()){
				// add the seperated values ot the list
				tweet_values.add(tweetScanner.next());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// return the new Tweet object containing the line's contents
		return new Tweet(
						tweet_id,
						Integer.parseInt(tweet_values.get(0)),
						new Timestamp(System.currentTimeMillis()), 
						tweet_values.get(1)
		);
	}

    public static void main(String[] args) throws Exception {
		
		Scanner api_scanner = new Scanner(System.in);
		System.out.println("Enter an API type to test (MySQL, Redis):");
		String api_type = api_scanner.next();

		System.out.println("Utilizing " + api_type + " driver . . .");

		switch (api_type.toLowerCase()) {
			case "mysql":
				api_scanner.close();
				TwitterDriverType mysql_driver =  new TwitterDriverType("false");
				DPDatabaseAPI mysql_api = mysql_driver.createTwitterAPI(TwitterDriverType.DatabaseType.MYSQL);

				// Post 1,000,000 tweets to the server and display metrics
				postAllTweets(mysql_api);

				// Post all follower/followee relationships
				postAllFollowers(mysql_api);

				// Fetch random user timelines of 10 tweets and display metrics
				getHomeTimelines(mysql_api);
		
				// Fetch the timeline of a specif user_id
				getHomeTimelines(mysql_api, 0);
		
				// close database connection
				mysql_api.closeConnection();
				break;

			case "redis":
				System.out.println("Would you like to flush the database connection (yes/no)?");
				String flushed = api_scanner.next();
				api_scanner.close();
	
				TwitterDriverType redis_driver =  new TwitterDriverType(flushed);
				DPDatabaseAPI redis_api = redis_driver.createTwitterAPI(TwitterDriverType.DatabaseType.REDIS);
				
				if(redis_driver.flush){
					// Post all follower/followee relationships
					System.out.println("Posting all follower/followee relationships: ");
					postAllFollowers(redis_api);

					// Post 1,000,000 tweets to the server and display metrics
					System.out.println("Posting all tweets: ");
					postAllTweets(redis_api);
				}
				

				// Fetch random user timelines of 10 tweets and display metrics
				System.out.println("Fetching all user timelines randomly: ");
				getHomeTimelines(redis_api);
		
				// Fetch the timeline of a specific user_id
				getHomeTimelines(redis_api, 0);
		
				// close database connection
				redis_api.closeConnection();
				break;

			case "redis_ec":
				System.out.println("Would you like to flush the database connection (yes/no)?");
				String ec_flushed = api_scanner.next();
				api_scanner.close();
	
				TwitterDriverType redis_ec_driver =  new TwitterDriverType(ec_flushed);
				DPDatabaseAPI redis_ec_api = redis_ec_driver.createTwitterAPI(TwitterDriverType.DatabaseType.REDISEC);
				
				if(redis_ec_driver.flush){
					// Post all follower/followee relationships
					System.out.println("Posting all follower/followee relationships: ");
					postAllFollowers(redis_ec_api);

					// Post 1,000,000 tweets to the server and display metrics
					System.out.println("Posting all tweets: ");
					postAllTweets(redis_ec_api);
				}
				

				// Fetch random user timelines of 10 tweets and display metrics
				System.out.println("Fetching all user timelines randomly: ");
				getHomeTimelines(redis_ec_api);
		
				// Fetch the timeline of a specific user_id
				getHomeTimelines(redis_ec_api, 0);
		
				// close database connection
				redis_ec_api.closeConnection();
				break;
		
			default:
				System.out.println("Not implemented");
				break;
		}
	

	}
}
