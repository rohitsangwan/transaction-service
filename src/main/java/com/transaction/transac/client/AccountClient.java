package com.transaction.transac.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.transaction.transac.constants.ApiEndpoints;
import com.transaction.transac.constants.Constants;
import com.transaction.transac.dto.BaseResponse;
import com.transaction.transac.dto.account.TxnValidationDTO;
import com.transaction.transac.dto.request.DepositRequestDTO;
import com.transaction.transac.dto.response.DepositResponseDTO;
import com.transaction.transac.enums.ErrorCode;
import com.transaction.transac.exception.ServiceCallException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Component
@Slf4j
public class AccountClient {
    private static final Logger logger = LoggerFactory.getLogger(AccountClient.class);

    @Value("${account.api.base.url}")
    private String accountBaseUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public TxnValidationDTO validateAccount(DepositRequestDTO depositRequestDTO, String userId) throws ServiceCallException {
        StringBuilder urlBuilder = new StringBuilder(accountBaseUrl);
        urlBuilder.append(ApiEndpoints.ACCOUNT_VALIDATE_ACCOUNT_ENDPOINT);
        urlBuilder.append(userId);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity(headers);
        try {
            ResponseEntity<BaseResponse> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.GET, requestEntity, BaseResponse.class);
            logger.info("[validateAccount] response : {}", response);

            if (!response.getStatusCode().is2xxSuccessful() || response == null || response.getBody() == null) {
                logger.error("[validateAccount] Exception occurred while validating the account: {}", userId);
                throw new ServiceCallException(ErrorCode.ACCOUNT_VALIDATION_FAILED,
                        String.format(ErrorCode.ACCOUNT_VALIDATION_FAILED.getErrorMessage(), userId),
                        ErrorCode.ACCOUNT_VALIDATION_FAILED.getDisplayMessage());
            }
            return objectMapper.convertValue(response.getBody().getData(), TxnValidationDTO.class);
        } catch (Exception e) {
            logger.error("[validateAccount] API failed for userId: {}", userId);
            throw new ServiceCallException(ErrorCode.ACCOUNT_VALIDATION_FAILED,
                    String.format(ErrorCode.ACCOUNT_VALIDATION_FAILED.getErrorMessage(), userId),
                    ErrorCode.ACCOUNT_VALIDATION_FAILED.getDisplayMessage());
        }
    }

    public void updateAccountBalance(DepositResponseDTO depositResponseDTO) throws ServiceCallException {
        StringBuilder urlBuilder = new StringBuilder(accountBaseUrl);
        urlBuilder.append(ApiEndpoints.UPDATE_ACCOUNT_BALANCE_ENDPOINT);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<DepositResponseDTO> requestEntity = new HttpEntity(depositResponseDTO, headers);
        try {
            ResponseEntity<BaseResponse> response = restTemplate.exchange(urlBuilder.toString(), HttpMethod.PUT, requestEntity, BaseResponse.class);
            logger.info("[updateAccountBalance] response : {}", response);

            if (!response.getStatusCode().is2xxSuccessful()) {
                logger.error("[updateAccountBalance] Exception occurred while updating the accountBalance for account: {}", depositResponseDTO.getAccountNumber());
                throw new ServiceCallException(ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED,
                        String.format(ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED.getErrorMessage(), depositResponseDTO.getAccountBalance()),
                        ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED.getDisplayMessage());
            }
        } catch (Exception e) {
            logger.error("[updateAccountBalance] API failed for accountNumber: {}", depositResponseDTO.getAccountNumber());
            throw new ServiceCallException(ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED,
                    String.format(ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED.getErrorMessage(), depositResponseDTO.getAccountNumber()),
                    ErrorCode.ACCOUNT_BALANCE_UPDATE_FAILED.getDisplayMessage());
        }
    }
}
