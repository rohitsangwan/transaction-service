package com.transaction.transac.exception;

import com.transaction.transac.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountActivationFailedException extends Exception{
    private ErrorCode errorCode;
    private String errorMessage;
    private String displayMessage;
}
