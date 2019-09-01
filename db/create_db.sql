CREATE TABLE `user` (
    user_id int(11) NOT NULL AUTO_INCREMENT,
    user_name varchar(255) NOT NULL,
    password_hash varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id),
    UNIQUE KEY (user_name)
);

CREATE TABLE role (
    role_id int(11) NOT NULL AUTO_INCREMENT,
    role_code VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (role_id),
    UNIQUE KEY (role_code)
);

CREATE TABLE user_to_role (
    user_to_role_id int(11) NOT NULL AUTO_INCREMENT,
    user_id int(11) NOT NULL,
    role_id int(11) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_to_role_id),
    UNIQUE KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES `user`(user_id),
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);

CREATE TABLE account (
    account_id int(11) NOT NULL AUTO_INCREMENT,
    account_code varchar(255) NOT NULL,
    balance numeric(15, 2) NOT NULL,
    owner_id int(11) NOT NULL,
    account_name varchar(255),
    version int(11) NOT NULL DEFAULT 0,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (account_id),
    UNIQUE KEY (account_code),
    FOREIGN KEY (owner_id) REFERENCES `user`(user_id)
);

CREATE TABLE `account_transaction` (
    account_transaction_id int(11) NOT NULL AUTO_INCREMENT,
    transaction_code varchar (64) NOT NULL,
    transaction_type varchar(255) NOT NULL,
    amount numeric(15, 2) NOT NULL,
    account_id int(11) NOT NULL,
    transaction_date DATETIME NOT NULL,
    description VARCHAR(512),
    related_account_transaction_id int(11) NULL,
    account_balance_after_transaction numeric(15, 2) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (account_transaction_id),
    UNIQUE KEY (transaction_code),
    INDEX  (account_id, transaction_date, transaction_type),
    FOREIGN KEY (account_id) REFERENCES `account`(account_id),
    FOREIGN KEY (related_account_transaction_id) REFERENCES `account_transaction`(account_transaction_id),
    INDEX (account_id, transaction_date)
);


CREATE TABLE interest_rate_schedule (
    interest_rate_schedule_id int(11) NOT NULL AUTO_INCREMENT,
    interest_rate_schedule_code VARCHAR(64) NOT NULL,
    interest_rate_schedule_name VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (interest_rate_schedule_id),
    UNIQUE KEY (interest_rate_schedule_code)
);

CREATE TABLE interest_rate_schedule_bucket (
    interest_rate_schedule_bucket_id int(11) NOT NULL AUTO_INCREMENT,
    interest_rate_schedule_id int(11) NOT NULL,
    amount_floor numeric(15, 2),
    amount_ceiling numeric(15, 2),
    interest_rate numeric(15, 2) NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (interest_rate_schedule_bucket_id),
    FOREIGN KEY (interest_rate_schedule_id) REFERENCES `interest_rate_schedule`(interest_rate_schedule_id)
);

CREATE TABLE account_to_interest_rate_schedule (
    account_to_interest_rate_schedule_id int(11) NOT NULL AUTO_INCREMENT,
    account_id INT(11) NOT NULL,
    interest_rate_schedule_id INT(11) NOT NULL,
    start_date_time TIMESTAMP NOT NULL,
    end_date_time TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL DEFAULT NOW() ON UPDATE NOW(),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY (account_to_interest_rate_schedule_id),
    UNIQUE KEY (account_id, interest_rate_schedule_id),
    FOREIGN KEY (account_id) REFERENCES `account`(account_id),
    FOREIGN KEY (interest_rate_schedule_id) REFERENCES `interest_rate_schedule`(interest_rate_schedule_id)
);


-- create default roles
INSERT INTO role(role_id, role_code)
VALUES(1, 'ADMIN'),
	  (2, 'USER');

-- The following section is for testing
INSERT INTO `user`
(`user_id`,`user_name`,`password_hash`,`first_name`,`last_name`)
VALUES
(1,'admin','$2a$10$b1aSFy1ZipJ3fttJljiyiurzq/sOjfBlugUvkNkpfxYrTjnhAwbzC','adminfirstname','adminlastname'),
(2,'user','$2a$10$oR3PiY3AW3y0xQj1vbWXAuRU1Wn25P6.bSHdOPWxA1LtSyXrVuhAK','userfirstname','userlastname'),
(3,'user2','$2a$10$DY8faVviBPUESH1yl0KrLOd9d9v370SCkFxro/7r4B0o8.lTBzzXm','userfirstname','userlastname');

INSERT INTO `buddybank`.`user_to_role`
(`user_to_role_id`, `user_id`, `role_id`)
VALUES
(1,1,1),
(2,1,2),
(3,2,2),
(4,3,2);

INSERT INTO `buddybank`.`account` (`account_code`, `balance`, `owner_id`, `account_name`) VALUES ('DEF', '100', '2', 'User 1\'s Account');
INSERT INTO `buddybank`.`account_transaction`(`transaction_type`, `amount`, `account_id`, `transaction_date`, `description`, `account_balance_after_transaction`, `transaction_code`)
VALUES ('INTEREST_ACCRUAL', 0, 1, SYSDATE(), 'Initial account creation', 0, "709fe8df-5e2f-4b1d-99b0-f67af4a05dd9");
INSERT INTO `buddybank`.`account` (`account_code`,`balance`, `owner_id`, `account_name`) VALUES ('ABC', '200', '1', 'Admin\'s Account');
INSERT INTO `buddybank`.`account_transaction`(`transaction_type`, `amount`, `account_id`, `transaction_date`, `description`, `account_balance_after_transaction`, `transaction_code`)
VALUES ('INTEREST_ACCRUAL', 0, 2, SYSDATE(), 'Initial account creation', 0, "c3937396-d19f-458a-926e-1c28c5a2b4bf");
INSERT INTO `buddybank`.`account` (`account_code`, `balance`, `owner_id`, `account_name`) VALUES ('GHI', '300', '3', 'User 2\'s Account');
INSERT INTO `buddybank`.`account_transaction`(`transaction_type`, `amount`, `account_id`, `transaction_date`, `description`, `account_balance_after_transaction`, `transaction_code`)
VALUES ('INTEREST_ACCRUAL', 0, 3, SYSDATE(), 'Initial account creation', 0, "608fe8df-5e2f-4b1d-99b0-f67af4a05ee8");

commit;