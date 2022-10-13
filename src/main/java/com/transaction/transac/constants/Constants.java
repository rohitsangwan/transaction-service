package com.transaction.transac.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public final class Constants {

    private Constants() {
    }

    public static final String REQUEST_ID = "request-id";
    public static final String RESPONSE_ID = "response-id";
    public static final String SUCCESS = "Success";
    public static final Double ZERO = 0.0;
    public static final Set<String> URI_TO_IGNORE = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList("/account/v1/health")));
}
