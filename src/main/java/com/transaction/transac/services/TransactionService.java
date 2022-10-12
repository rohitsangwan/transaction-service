package com.transaction.transac.services;

import com.transaction.transac.client.AccountClient;
import com.transaction.transac.constants.Constants;
import com.transaction.transac.controller.TransactionController;
import com.transaction.transac.dto.account.TxnValidationDTO;
import com.transaction.transac.dto.request.DepositRequestDTO;
import com.transaction.transac.dto.response.DepositResponseDTO;
import com.transaction.transac.enums.ErrorCode;
import com.transaction.transac.exception.InvalidAccountNumberException;
import com.transaction.transac.exception.ServiceCallException;
import com.transaction.transac.models.TransactionModel;
import com.transaction.transac.repository.TransactionRepository;
import com.transaction.transac.services.helper.TransactionServiceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private TransactionServiceHelper transactionServiceHelper;

    public DepositResponseDTO depositAmount(DepositRequestDTO depositRequestDTO, String userId) throws ServiceCallException, InvalidAccountNumberException {
        logger.info("[depositAmount] depositing amount for userId : {}", userId);
        TransactionModel transactionModel = transactionRepository.findByUserId(userId);
        DepositResponseDTO depositResponseDTO = new DepositResponseDTO();
        if (transactionModel == null) {
            logger.info("[depositAmount] first transaction for userId : {}", userId);
            TxnValidationDTO txnValidationDTO = accountClient.validateAccount(depositRequestDTO, userId);
            TransactionModel txnModel = new TransactionModel();
            txnModel.setUserId(userId);
            BeanUtils.copyProperties(txnValidationDTO, txnModel);
            if (txnModel.getAccountNumber().equals(depositRequestDTO.getAccountNumber())) {
                txnModel = transactionServiceHelper.updateBalance(txnModel, depositRequestDTO.getDepositAmount());
                BeanUtils.copyProperties(txnModel, depositResponseDTO);
                try {
                    accountClient.updateAccountBalance(depositResponseDTO);
                } catch (ServiceCallException e) {
                    throw new ServiceCallException(ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED,
                            String.format(ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED.getErrorMessage(), depositResponseDTO.getAccountNumber()),
                            ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED.getDisplayMessage());
                }
                transactionRepository.save(txnModel);
                return depositResponseDTO;
            } else {
                logger.error("[depositAmount] incorrect accountNumber of userId : {}", userId);
                throw new InvalidAccountNumberException(ErrorCode.INVALID_ACCOUNT_NUMBER,
                        String.format(ErrorCode.INVALID_ACCOUNT_NUMBER.getErrorMessage(), userId),
                        ErrorCode.INVALID_ACCOUNT_NUMBER.getDisplayMessage());
            }
        } else {
            if (transactionModel.getAccountNumber().equals(depositRequestDTO.getAccountNumber())) {
                transactionModel = transactionServiceHelper.updateBalance(transactionModel, depositRequestDTO.getDepositAmount());
                BeanUtils.copyProperties(transactionModel, depositResponseDTO);
                try {
                    accountClient.updateAccountBalance(depositResponseDTO);
                } catch (ServiceCallException e) {
                    throw new ServiceCallException(ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED,
                            String.format(ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED.getErrorMessage(), depositResponseDTO.getAccountNumber()),
                            ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED.getDisplayMessage());
                }
                transactionRepository.save(transactionModel);
                return depositResponseDTO;
            } else {
                logger.error("[depositAmount] incorrect accountNumber of userId : {}", userId);
                throw new InvalidAccountNumberException(ErrorCode.INVALID_ACCOUNT_NUMBER,
                        String.format(ErrorCode.INVALID_ACCOUNT_NUMBER.getErrorMessage(), userId),
                        ErrorCode.INVALID_ACCOUNT_NUMBER.getDisplayMessage());
            }
        }
    }

}
