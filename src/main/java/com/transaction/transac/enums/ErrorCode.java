package com.transaction.transac.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {
    BANK_ACCOUNT_ACTIVATION_FAILED("BS_TXN_400053", "Could not activate the bank account", "Bank account could not be activated", HttpStatus.OK),
    ACCOUNT_VALIDATION_FAILED("BS_TXN_400054", "Could not validate the account", "Account validation failed", HttpStatus.BAD_REQUEST),
    INVALID_ACCOUNT_NUMBER("BS_TXN_400055", "The entered account number is invalid", "Please enter a valid account number", HttpStatus.BAD_REQUEST),
    ACCOUNT_BALANCE_UPDATE_FAILED("BS_TXN_400056", "Could not update the account balance", "Account balance not updated", HttpStatus.OK);

    private final String code;
    private final String errorMessage;
    private final String displayMessage;
    private final HttpStatus httpStatusCode;

    private ErrorCode(String code, String errorMessage, String displayMessage, HttpStatus httpStatusCode) {
        this.code = code;
        this.errorMessage = errorMessage;
        this.displayMessage = displayMessage;
        this.httpStatusCode = httpStatusCode;
    }
}
