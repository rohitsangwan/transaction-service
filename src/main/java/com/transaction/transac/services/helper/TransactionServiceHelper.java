package com.transaction.transac.services.helper;

import com.transaction.transac.controller.TransactionController;
import com.transaction.transac.models.TransactionModel;
import com.transaction.transac.repository.TransactionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransactionServiceHelper {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    public TransactionModel updateBalance(TransactionModel txnModel, Double depositAmount) {
        Double accountBalance = txnModel.getAccountBalance();
        Double updatedAccountBalance = accountBalance + depositAmount;
        txnModel.setAccountBalance(updatedAccountBalance);
        return txnModel;
    }

}
