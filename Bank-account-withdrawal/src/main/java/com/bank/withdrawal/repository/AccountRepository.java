package com.bank.withdrawal.repository;

import java.math.BigDecimal;
import java.util.Optional;


 //Data access contract for the accounts table.
 //The service layer depends on this interface, not on JdbcTemplate directly,
 //so it can be mocked in unit tests with no database.

public interface AccountRepository {


     //Returns the current balance for the given account,
     //or Optional.empty() if the account does not exist.

    Optional<BigDecimal> findBalanceById(Long accountId);


     //Deducts the given amount from the account balance.
     //The SQL includes WHERE balance >= amount as a concurrency guard,
     //if a concurrent transaction emptied the account first, rowsAffected will be 0.
     //@return number of rows affected (1 = success, 0 = balance was insufficient at DB level)

    int deductBalance(Long accountId, BigDecimal amount);
}
