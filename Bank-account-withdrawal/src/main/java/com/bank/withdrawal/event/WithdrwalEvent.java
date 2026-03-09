package com.bank.withdrawal.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;


//Immutable domain event, this will represent a completed (or failed) withdrawal.
// Published to SNS(this is in the context of the original codes since will be using aws ) (or logged locally just for testing purposes) after a successful database commit.
// Jackson serialises this automatically to JSON , we do not need to do manual String formatting, just less room for mistakes and making silly mistakes

public class WithdrawalEvent {

    private final String eventId;  // UUID this will let consumers deduplicate retries
    private final String traceId; // Trace ID from the original HTTP request, this will be useful in production to be able to trace events
    private final Long accountId;
    private final BigDecimal amount;
    private final String status;
    private final Instant timestamp;     // Just good practice to keep trace or track of when the event was created, helps and makes debugging easier if there are issues

    public WithdrawalEvent(String traceId, Long accountId, BigDecimal amount, String status) {
        this.eventId = UUID.randomUUID().toString();
        this.traceId = traceId;
        this.accountId = accountId;
        this.amount = amount;
        this.status = status;
        this.timestamp = Instant.now();
    }

    // Getters only — no setters, events are immutable after creation
    public String getEventId() { return eventId; }
    public String getTraceId() { return traceId; }
    public Long getAccountId() { return accountId; }
    public BigDecimal getAmount() { return amount; }
    public String getStatus() { return status; }
    public Instant getTimestamp() { return timestamp; }
}
