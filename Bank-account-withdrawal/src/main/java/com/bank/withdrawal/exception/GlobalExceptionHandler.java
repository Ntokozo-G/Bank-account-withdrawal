package com.bank.withdrawal.exception;

import com.bank.withdrawal.dto.WithdrawalResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//Centrally mapping the domain exceptions into relevant and clear HTTP responses.
//without this class or exception handler,
//every unhandled exception would return a raw 500 with a Java stack trace(this is not idea since it would expose our code or code structure to the outside).
//@RestControllerAdvice intercepts exceptions from ALL @RestController classes.

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // throw the error 404 when the account does not exist
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<WithdrawalResponse> handleNotFound(AccountNotFoundException ex) {
        log.warn("Account not found: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new WithdrawalResponse("FAILED", ex.getMessage()));
    }

    // throw the error 422 when we have business rules rejections (example not enough balance, concurrent depletion)
    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<WithdrawalResponse> handleInsufficientFunds(InsufficientFundsException ex) {
        log.warn("Insufficient funds: {}", ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new WithdrawalResponse("FAILED", ex.getMessage()));
    }

    // throw the error 400 when the @Valid on @RequestBody fails (this could be a missing field, negative amount, etc. this is more input validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<WithdrawalResponse> handleValidation(MethodArgumentNotValidException ex) {
        String detail = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        return ResponseEntity
                .badRequest()
                .body(new WithdrawalResponse("FAILED", detail));
    }

    // throw the error 400 when @Positive / @NotNull on method parameters (path/query params)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<WithdrawalResponse> handleConstraint(ConstraintViolationException ex) {
        return ResponseEntity
                .badRequest()
                .body(new WithdrawalResponse("FAILED", ex.getMessage()));
    }

    // throw the error 500 when anything else unexpected happens (maybe when the DB is  down, or we have network error or issues, etc.)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<WithdrawalResponse> handleGeneral(Exception ex) {
        log.error("Unexpected error during withdrawal", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new WithdrawalResponse("FAILED", "An unexpected error occurred"));
    }
}
