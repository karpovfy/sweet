INSERT INTO `user` (`id`, `active`, `password`, `username`, `activation_code`, `email`) VALUES ('1', true, 'a', 'a', NULL, NULL);

INSERT INTO `user_role` (`user_id`, `roles`) VALUES ('1', 'ADMIN');

INSERT INTO `user_role` (`user_id`, `roles`) VALUES ('1', 'USER');