package com.bank.withdrawal.dto;

// not really needed but makes things work better since before we just had strings typed but here it's useful since we will actually validate if we got status code 200
// for success or 404/422 or other for failed

public class WithdrawalResponse {

    private final String status;
    private final String message;

    public WithdrawalResponse(String status, String message) {
        this.status  = status;
        this.message = message;
    }

    public String getStatus()  { return status; }
    public String getMessage() { return message; }
}
