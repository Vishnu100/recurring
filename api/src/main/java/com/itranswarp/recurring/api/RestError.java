package com.itranswarp.recurring.api;

import javax.servlet.http.HttpServletRequest;

public class RestError {

	public final String error;
	public final String field;
	public final String message;
	public final String method;
	public final String url;

	public RestError(String error, String field, String message, HttpServletRequest request) {
		this.error = error;
		this.field = field;
		this.message = message;
		this.method = request.getMethod();
		this.url = request.getRequestURI();
	}

}
