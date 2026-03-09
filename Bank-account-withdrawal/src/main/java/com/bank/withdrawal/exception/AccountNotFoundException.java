package com.bank.withdrawal.exception;

//This exception is thrown by WithdrawalService when the requested accountId does not exist.
//this is mapped to HTTP error code 404 by GlobalExceptionHandler.

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
