package com.transaction.transac.controller;

import com.transaction.transac.dto.BaseResponse;
import com.transaction.transac.dto.request.ActivateAccountDTO;
import com.transaction.transac.dto.request.DepositRequestDTO;
import com.transaction.transac.exception.AccountActivationFailedException;
import com.transaction.transac.exception.InvalidAccountNumberException;
import com.transaction.transac.exception.ServiceCallException;
import com.transaction.transac.services.TransactionService;
import com.transaction.transac.utils.CreateMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("txn/v1")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/deposit/{userId}")
    public ResponseEntity<BaseResponse> deposit(@PathVariable("userId") String userId, @RequestBody DepositRequestDTO depositRequestDTO) throws ServiceCallException, InvalidAccountNumberException {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setData(transactionService.depositAmount(depositRequestDTO, userId));
        baseResponse.setMetaDTO(CreateMetaData.createSuccessMetaData());
        return new ResponseEntity<>(baseResponse, HttpStatus.OK);
    }

}
