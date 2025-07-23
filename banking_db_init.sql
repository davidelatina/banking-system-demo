
-- create demo scheme
CREATE DATABASE banking_db;

USE banking_db;

-- customers
CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci UNIQUE NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    age INT UNSIGNED NOT NULL,
    salt VARBINARY(16) NOT NULL, -- Salt for password hashing 
    hashed_password VARBINARY(64) NOT NULL, -- SHA512 hash
    registered_at DATETIME NOT NULL
);

-- account types: checking, savings
CREATE TABLE account_type (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO account_type (name) VALUES 
    ("checking"),
    ("savings");

-- accounts
CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    customer_id BIGINT UNSIGNED NOT NULL, -- FK to customer
    account_type_id INT UNSIGNED NOT NULL, -- FK to account_type
    balance DECIMAL(18,9) UNSIGNED NOT NULL,
    created_at DATETIME NOT NULL,
    
    FOREIGN KEY (customer_id) REFERENCES customer(id),
    FOREIGN KEY (account_type_id) REFERENCES account_type(id)
);

-- transaction types: deposit, withdrawal, transfer
CREATE TABLE transaction_type (
    id INT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR(100) NOT NULL UNIQUE
);

INSERT INTO transaction_type (name) VALUES 
    ("deposit"),
    ("withdrawal"),
    ("transfer");

-- transactions
CREATE TABLE transaction (
    id SERIAL PRIMARY KEY,
    account_id BIGINT UNSIGNED NOT NULL, -- FK to account
    type_id INT UNSIGNED NOT NULL, -- FK to transaction_type
    amount DECIMAL(18,9) UNSIGNED NOT NULL,
    -- to be added: recipient
    executed_at DATETIME NOT NULL,
    
    FOREIGN KEY (account_id) REFERENCES account(id),
    FOREIGN KEY (type_id) REFERENCES transaction_type(id)
);


