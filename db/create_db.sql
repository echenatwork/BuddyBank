CREATE TABLE `user` (
    user_id int(11) NOT NULL AUTO_INCREMENT,
    user_name varchar(255) NOT NULL,
    salt varchar(255) NOT NULL,
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


-- create default roles
INSERT INTO role(role_id, role_code)
VALUES(1, 'ADMIN'),
	  (2, 'USER');

-- TODO testing, insert test user/admin accounts
INSERT INTO `user`
(`user_id`,`user_name`,`salt`,`password_hash`,`first_name`,`last_name`)
VALUES
(1,'admin','KJd/+OwAhhZjc5XEIgBZSn0b9ZIW32nRSRuDvt0nrG0=','oJPykmJbqNYfRj4G9Dd5a8ps/8j4GP0puTeucusaBiE=','adminfirstname','adminlastname'),
(2,'user','rvjMck3qDsERXES3QLdaaaMUL0EfiDNOQc3+yE5LHk8=','rCcbTzswO0LG7GySS0r8+CSNakk6BQk6FwyhP2MBTMo=','userfirstname','userlastname'),
(3,'user2','rvjMck3qDsERXES3QLdaaaMUL0EfiDNOQc3+yE5LHk8=','rCcbTzswO0LG7GySS0r8+CSNakk6BQk6FwyhP2MBTMo=','userfirstname','userlastname');

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