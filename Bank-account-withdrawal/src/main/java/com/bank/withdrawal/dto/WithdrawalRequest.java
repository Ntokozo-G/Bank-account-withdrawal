package com.bank.withdrawal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;


//Inbound request body for POST /bank/withdraw.
//Validation annotations reject bad input before the service layer is reached,
//this is good addition so that wee fail quick or reject quick don't have to ggo through the whole transactions before failure happens

public class WithdrawalRequest {

    @NotNull(message = "accountId is required")
    private Long accountId;

    @NotNull(message = "amount is required")
    @Positive(message = "amount must be greater than zero")
    private BigDecimal amount;

    // trace ID could be a nice addition that could be forwarded from the calling service or API gateway.
    // included in the SNS event so downstream consumers can correlate events and could make it easier to track transactions.

    private String traceId;

    public Long getAccountId() { return accountId; }
    public void setAccountId(Long value) { this.accountId = value; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal value) { this.amount = value; }

    public String getTraceId() { return traceId; }
    public void setTraceId(String value) { this.traceId = value; }
}
