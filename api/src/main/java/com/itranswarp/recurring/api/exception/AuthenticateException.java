package com.itranswarp.recurring.api.exception;

public class AuthenticateException extends RuntimeException {

    private static final long serialVersionUID = 3612189995343024164L;

    public AuthenticateException() {
    }

    public AuthenticateException(String message) {
        super(message);
    }

    public AuthenticateException(Throwable cause) {
        super(cause);
    }

    public AuthenticateException(String message, Throwable cause) {
        super(message, cause);
    }

}
