package com.itranswarp.recurring.base.exception;

public class MissingContextException extends RuntimeException {

    private static final long serialVersionUID = 9132991972815986538L;

    public MissingContextException() {
        super();
    }

    public MissingContextException(String message) {
        super(message);
    }

    public MissingContextException(Throwable cause) {
        super(cause);
    }

    public MissingContextException(String message, Throwable cause) {
        super(message, cause);
    }

}
