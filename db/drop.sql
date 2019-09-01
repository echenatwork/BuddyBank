SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS user_to_role;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS `user`;
DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS `account_transaction`;
DROP TABLE IF EXISTS `interest_rate_schedule`;
DROP TABLE IF EXISTS `interest_rate_schedule_bucket`;
DROP TABLE IF EXISTS `account_to_interest_rate_schedule`;

SET FOREIGN_KEY_CHECKS = 1;

commit;