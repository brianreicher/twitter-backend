DROP DATABASE IF EXISTS `TWITTER`;
CREATE DATABASE `twitter`;

USE `twitter`;

DROP TABLE IF EXISTS `tweets`;

CREATE TABLE `tweets`(
    `tweet_id` SERIAL, 
    `user_id` INT NOT NULL,
    `tweet_ts` TIMESTAMP NOT NULL,
    `tweet_text` VARCHAR(140) NOT NULL,
    PRIMARY KEY (`tweet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DROP TABLE IF EXISTS `follows`;

CREATE TABLE `follows`(
    `user_id` varchar(50) NOT NULL,
    `follows_id` varchar(50) NOT NULL
    )ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE INDEX idx_user_tweet ON tweets (user_id);
CREATE INDEX idx_user_follower ON follows (user_id, follows_id);