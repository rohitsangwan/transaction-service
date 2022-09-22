package com.transaction.transac.services;

import com.transaction.transac.constants.Constants;
import com.transaction.transac.controller.TransactionController;
import com.transaction.transac.dto.request.ActivateAccountDTO;
import com.transaction.transac.models.TransactionModel;
import com.transaction.transac.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionRepository transactionRepository;

    public String activateUserAccount(ActivateAccountDTO activateAccountDTO, String userId) {
        TransactionModel transactionModel = new TransactionModel();
        try{
            BeanUtils.copyProperties(activateAccountDTO, transactionModel);
            transactionModel.setAccountBalance(Constants.ZERO);
            transactionModel.setUserId(userId);
            TransactionModel model = transactionRepository.save(transactionModel);
            return Constants.SUCCESS;
        } catch (Exception e){
            logger.error("[activateUserAccount] account activation failed");
            throw new RuntimeException();
        }
    }
}
