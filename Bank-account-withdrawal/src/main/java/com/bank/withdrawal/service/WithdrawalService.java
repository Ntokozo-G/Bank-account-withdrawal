package com.bank.withdrawal.service;

import com.bank.withdrawal.dto.WithdrawalRequest;
import com.bank.withdrawal.dto.WithdrawalResponse;


//this is the business contract for the withdrawal operation,
//The controller depends on this interface, not directly on the implementation
//so the implementation can be swapped or mocked independently when running some testing.

public interface WithdrawalService {
    WithdrawalResponse withdraw(WithdrawalRequest request);
}
