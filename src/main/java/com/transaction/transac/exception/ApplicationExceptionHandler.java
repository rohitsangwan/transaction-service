package com.transaction.transac.exception;

import com.transaction.transac.dto.BaseResponse;
import com.transaction.transac.dto.MetaDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.transaction.transac.constants.Constants.REQUEST_ID;
import static com.transaction.transac.constants.Constants.RESPONSE_ID;


@ControllerAdvice
public class ApplicationExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationExceptionHandler.class);

    /**
     * Handles account activation failed exception
     *
     * @param e
     * @return response entity
     */
    @ExceptionHandler(AccountActivationFailedException.class)
    public ResponseEntity<BaseResponse> handleInsertionFailedException(AccountActivationFailedException e){
        logger.error("AccountActivationFailedException: " + e.getStackTrace());
        BaseResponse baseResponseDTO = new BaseResponse();
        baseResponseDTO.setMetaDTO(new MetaDTO(e.getErrorCode().getCode(), e.getErrorCode().getErrorMessage(), MDC.get(REQUEST_ID), MDC.get(RESPONSE_ID)));
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

}
