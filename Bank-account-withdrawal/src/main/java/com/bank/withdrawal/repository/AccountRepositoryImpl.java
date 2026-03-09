package com.bank.withdrawal.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

//JDBC implementation of AccountRepository.
//Spring detects this via @Repository and registers it as a bean automatically.

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    // Constructor injection, the JdbcTemplate is auto-configured by Spring Boot
    // because we have spring-boot-starter-jdbc + H2 on the classpath
    public AccountRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<BigDecimal> findBalanceById(Long accountId) {
        try {
            // queryForObject(sql, Class<T>, Object... args) — non-deprecated form
            BigDecimal balance = jdbcTemplate.queryForObject(
                    "SELECT balance FROM accounts WHERE id = ?",
                    BigDecimal.class,
                    accountId);
            return Optional.ofNullable(balance);

        } catch (EmptyResultDataAccessException ex) {
            // Spring throws this not returns null when no row matches.
            // We convert it to Optional.empty() so the caller gets a clean contract.
            return Optional.empty();
        }
    }

    @Override
    public int deductBalance(Long accountId, BigDecimal amount) {
        // The AND balance >= ? is the optimistic concurrency guard,
        // If a concurrent withdrawal already reduced the balance, this UPDATE
        // will match zero rows and return 0 , this avoids the issue o going into a negative balance so no overdraft possible.
        return jdbcTemplate.update(
                "UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?",
                amount, accountId, amount);
    }
}
