package com.transaction.transac.utils;

import com.transaction.transac.dto.MetaDTO;
import com.transaction.transac.enums.SuccessCode;
import org.slf4j.MDC;

import static com.transaction.transac.constants.Constants.REQUEST_ID;
import static com.transaction.transac.constants.Constants.RESPONSE_ID;

public class CreateMetaData {
    /**
     * common method to create meta data for response
     *
     * @return MetaDto
     */

    public static MetaDTO createSuccessMetaData(){
        return new MetaDTO(SuccessCode.SUCCESS_CODE.getCode(), SuccessCode.SUCCESS_CODE.getMessage(),
                MDC.get(REQUEST_ID), MDC.get(RESPONSE_ID));
    }
}
