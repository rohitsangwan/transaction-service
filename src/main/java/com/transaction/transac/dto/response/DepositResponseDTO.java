package com.transaction.transac.dto.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepositResponseDTO implements Serializable {
    private Long accountNumber;
    private Double accountBalance;
}
