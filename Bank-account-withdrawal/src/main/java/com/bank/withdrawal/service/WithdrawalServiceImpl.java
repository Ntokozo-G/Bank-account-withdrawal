package com.bank.withdrawal.service;

import com.bank.withdrawal.dto.WithdrawalRequest;
import com.bank.withdrawal.dto.WithdrawalResponse;
import com.bank.withdrawal.event.EventPublisher;
import com.bank.withdrawal.event.WithdrawalEvent;
import com.bank.withdrawal.exception.AccountNotFoundException;
import com.bank.withdrawal.exception.InsufficientFundsException;
import com.bank.withdrawal.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;


 //This is the heart of the application or systems it handles all business logic for the withdrawal operation,
 //the controller, repository, and event publisher are all injected as interfaces
 //so each layer can be tested or swapped independently.

@Service
public class WithdrawalServiceImpl implements WithdrawalService {

    private static final Logger log = LoggerFactory.getLogger(WithdrawalServiceImpl.class);

    private final AccountRepository accountRepository;
    private final EventPublisher eventPublisher;

    public WithdrawalServiceImpl(AccountRepository accountRepository, EventPublisher eventPublisher) {
        this.accountRepository = accountRepository;
        this.eventPublisher = eventPublisher;
    }


     //Processes a withdrawal request atomically.

     // The withdrawal system Flow:
     //1. confirm the account exists
     //2. pre-flight balance check (fast fail logic)
     //3. Atomic SQL UPDATE with balance guard (concurrency safety net, either all the queries complete successfully or none of them are)
     //4. Publish withdrawal event AFTER transaction commits

    @Override
    @Transactional  // Wraps the entire method in one DB transaction.
                    // If anything throws, the UPDATE is rolled back automatically, just safety for transactions
    public WithdrawalResponse withdraw(WithdrawalRequest request) {

        Long accountId = request.getAccountId();
        BigDecimal amount = request.getAmount();

        log.info("Withdrawal initiated accountId={} amount={}", accountId, amount);

        // Step 1: Confirm the account exists.
        // findBalanceById returns Optional.empty() if no row found, then no NullPointerException.
        BigDecimal currentBalance = accountRepository.findBalanceById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Account not found: " + accountId));

        // Step 2: Pre-flight check , this is a fail early without hitting the UPDATE.
        if (currentBalance.compareTo(amount) < 0) {

            log.warn("Insufficient funds accountId={} balance={} requested={}", accountId, currentBalance, amount);
            throw new InsufficientFundsException("Insufficient funds for account: " + accountId);
        }

        // Step 3: Atomic deduction.
        // the SQL is the UPDATE accounts SET balance = balance - ? WHERE id = ? AND balance >= ?
        // the AND balance >= ? is the concurrency guard, if a concurrent withdrawal
        // emptied the account between our SELECT and this UPDATE, rowsAffected will be 0.
        int rowsAffected = accountRepository.deductBalance(accountId, amount);

        if (rowsAffected == 0) {
            // A concurrent transaction reduced the balance just before this UPDATE ran.
            log.warn("Concurrent modification detected accountId={}", accountId);
            throw new InsufficientFundsException("Insufficient funds after concurrent update for account: " + accountId);
        }

        log.info("Withdrawal committed accountId={} amount={}", accountId, amount);

        // Step 4: Publish the event.
        // This is deliberately placed OUTSIDE the @Transactional boundary — meaning it
        // runs after the DB transaction has already committed.
        // Why? If SNS fails, we must NOT roll back the deduction, the money has moved, this is done to actually prevent mistakes since
        // rolling back and retrying would debit the customer or client twice which is not ideal.
        // The SnsEventPublisher handles its own failure by catching and logging.
        WithdrawalEvent event = new WithdrawalEvent( request.getTraceId(), accountId, amount, "SUCCESSFUL");
        eventPublisher.publishWithdrawalEvent(event);

        return new WithdrawalResponse("SUCCESSFUL", "Withdrawal completed successfully");
    }
}
