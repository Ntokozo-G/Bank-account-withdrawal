package com.bank.withdrawal.controller;

import com.bank.withdrawal.dto.WithdrawalRequest;
import com.bank.withdrawal.dto.WithdrawalResponse;
import com.bank.withdrawal.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


 // this is the HTTP entry point for the withdrawal process.
 // the responsibility is to receive the request then delegate to the service(WithdrawalService) and then return the response.
 //Nothing else — no business logic, no SQL, no AWS, this follows the SOLID principle of single responsibility
 //All error responses are handled by GlobalExceptionHandler,
 //so this class stays at just a few lines.

@RestController
@RequestMapping("/bank")
@Validated  // Enables constraint validation on method parameters
public class BankAccountController {

    private final WithdrawalService withdrawalService;

    // @Autowired is not needed since Spring auto-wires single-constructor beans
    public BankAccountController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    // POST /bank/withdraw
    // Request body (JSON):
    //{
    // "accountId": 1,
    // "amount": "150.00",
    // "correlationId": "optional-trace-id"
    //}
    // @Valid triggers validation of @NotNull and @Positive on WithdrawalRequest
    //before this method body runs. Invalid requests return 400 automatically.



    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawalResponse> withdraw(
            @RequestBody @Valid WithdrawalRequest request) {

        WithdrawalResponse response = withdrawalService.withdraw(request);
        return ResponseEntity.ok(response);
    }
}
