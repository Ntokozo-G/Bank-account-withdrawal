package com.bank.withdrawal.exception;

//This exception wil be thrown by WithdrawalService when the account balance is lower than the requested amount,
//or when a concurrent(two threads rrunning) withdrawal depletes the balance between our check and update.
//this is mapped to HTTP 422 (Unprocessable Entity) by GlobalExceptionHandler.

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
