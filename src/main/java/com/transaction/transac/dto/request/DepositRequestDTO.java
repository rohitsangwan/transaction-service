package com.transaction.transac.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepositRequestDTO implements Serializable {
    private Long accountNumber;
    private Double depositAmount;
}
