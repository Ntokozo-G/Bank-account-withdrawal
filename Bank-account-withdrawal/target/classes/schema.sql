-- Runs automatically on startup via spring.sql.init.mode=always
-- Creates the accounts table in the H2 in memory database since it's easy for testing and did not set up a real db , chose H2 just for ck testing

CREATE TABLE IF NOT EXISTS accounts (
    id      BIGINT         PRIMARY KEY,
    name    VARCHAR(100)   NOT NULL,
    balance DECIMAL(19, 2) NOT NULL CHECK (balance >= 0)
);
