CREATE TABLE member (
    member_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birth DATETIME,
    role ENUM('USER'),
    deleted BOOLEAN,
    created_time DATETIME NOT NULL,
    modified_time DATETIME
);
