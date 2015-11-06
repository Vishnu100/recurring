package com.itranswarp.recurring.common.exception;

public class APIException extends RuntimeException {

	String error;
	String field;

	public APIException(String error) {
		super();
		this.error = error;
	}

	public APIException(String error, String field, String message) {
		super(message);
		this.error = error;
		this.field = field;
	}

	public APIException(Throwable t) {
		super(t);
		this.error = "unknown:server_error";
	}

	public APIException(String error, Throwable t) {
		super(t);
		this.error = error;
	}

	public APIErrorInfo toErrorInfo() {
		return new APIErrorInfo(error, field, getMessage());
	}
}
