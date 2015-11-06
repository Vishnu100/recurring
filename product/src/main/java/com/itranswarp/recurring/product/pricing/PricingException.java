package com.itranswarp.recurring.product.pricing;

import com.itranswarp.recurring.common.exception.APIException;

public class PricingException extends APIException {

    private static final long serialVersionUID = 7669135062080950257L;

    static final String ERROR_PREFIX = "pricing:";

    public PricingException(String field, String message) {
        super(ERROR_PREFIX + "invalid", field, message);
    }

    public PricingException(String error, String field, String message) {
        super(ERROR_PREFIX + error, field, message);
    }

    public PricingException(Throwable cause) {
        super(ERROR_PREFIX + "exception", cause);
    }

}
