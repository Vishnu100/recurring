package com.itranswarp.recurring.common.exception;

public class APIArgumentException extends APIException {

	public APIArgumentException(String field, String message) {
		super("argument:invalid", field, message);
	}

}
