package com.transaction.transac.exception;

import com.transaction.transac.enums.ErrorCode;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InvalidAccountNumberException extends Exception{
    private ErrorCode errorCode;
    private String errorMessage;
    private String displayMessage;
}
