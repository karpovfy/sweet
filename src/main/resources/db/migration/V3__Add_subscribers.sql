CREATE TABLE `user_subscriptions` (
  `channel_id` bigint(20) NOT NULL references user(id) ,
  `subscriber_id` bigint(20) NOT NULL references user(id),
PRIMARY KEY (`channel_id`,`subscriber_id`)


)ENGINE=MyISAM DEFAULT CHARSET=latin1;

