package com.transaction.transac.enums;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@Getter
@ToString
public enum ErrorCode {
    BANK_ACCOUNT_ACTIVATION_FAILED("BS_TXN_400053", "Could not activate the bank account", "Bank account could not be activated", HttpStatus.BAD_REQUEST);


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
