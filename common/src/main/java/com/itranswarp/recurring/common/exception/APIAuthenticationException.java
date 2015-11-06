package com.itranswarp.recurring.common.exception;

public class APIAuthenticationException extends APIException {

	public APIAuthenticationException(String message) {
		super("auth:failed", null, message);
	}

	public APIAuthenticationException(String field, String message) {
		super("auth:failed", field, message);
	}

}
