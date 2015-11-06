package com.itranswarp.recurring.api;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.itranswarp.recurring.common.exception.APIErrorInfo;
import com.itranswarp.recurring.common.exception.APIException;

@ControllerAdvice
public class RestExceptionHandler {

	final Log log = LogFactory.getLog(getClass());

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	@ResponseBody APIErrorInfo handleException(HttpServletRequest request, Exception e) {
		if (e instanceof APIException) {
			return ((APIException) e).toErrorInfo();
		}
		log.error("Handle exception: " + e.getClass().getName());
		return new APIException(e).toErrorInfo();
	}

}
