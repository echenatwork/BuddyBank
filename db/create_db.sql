CREATE TABLE user (
    user_id int(11) NOT NULL AUTO_INCREMENT,
    user_name varchar(255) NOT NULL,
    salt varchar(255) NOT NULL,
    password_hash varchar(255) NOT NULL,
    first_name varchar(255) NOT NULL,
	last_name varchar(255) NOT NULL,
    PRIMARY KEY (user_id),
    UNIQUE KEY (user_name)
);


CREATE TABLE role (
    role_id int(11) NOT NULL AUTO_INCREMENT,
    role_code VARCHAR(255) NOT NULL,
    PRIMARY KEY (role_id),
    UNIQUE KEY (role_code)
);

CREATE TABLE user_to_role (
    user_to_role_id int(11) NOT NULL AUTO_INCREMENT,
    user_id int(11) NOT NULL,
    role_id int(11) NOT NULL,
    PRIMARY KEY (user_to_role_id),
    UNIQUE KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES user(user_id),
    FOREIGN KEY (role_id) REFERENCES role(role_id)
);

-- create default roles
INSERT INTO role(role_code)
VALUES('ADMIN'),
	  ('USER');

commit;