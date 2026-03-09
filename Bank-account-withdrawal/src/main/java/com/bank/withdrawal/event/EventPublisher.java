package com.bank.withdrawal.event;


 //The service layer depends only on this interface — not on AWS, SNS, or any
 //specific messaging technology. This means:
 //Locally:  LocalEventPublisher is wired in (just logs, no AWS needed)
 //on AWS:   SnsEventPublisher is wired in (publishes to real SNS topic)
 //in tests: A mock can be injected (no infrastructure needed)

public interface EventPublisher {
    void publishWithdrawalEvent(WithdrawalEvent event);
}
