CREATE DATABASE `TWITTER`;

DROP TABLE IF EXISTS `tweets`;

CREATE TABLE `tweets`(
    `user_id` varchar(50) NOT NULL,
    `text` varchar(255) NOT NULL,
    `tweet_id` BIGINT NOT NULL, AUTO_INCREMENT, 
    `time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP

) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users`(

)