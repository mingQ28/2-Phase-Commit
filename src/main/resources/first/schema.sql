CREATE DATABASE IF NOT EXISTS firstdb;
USE firstdb;

-- 테이블 생성
CREATE TABLE account (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(255) NOT NULL,
    balance DECIMAL(19,2) NOT NULL,
    owner_name VARCHAR(255)
);
