package com.transaction.transac.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ActivateAccountDTO implements Serializable {
    private Long accountNumber;
}
