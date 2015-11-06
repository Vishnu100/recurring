package com.itranswarp.recurring.base.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itranswarp.recurring.base.model.User;
import com.itranswarp.recurring.common.exception.APIAuthenticationException;

public interface Authenticator {

	/**
	 * Try to get authenticated User object from current request.
	 * 
	 * @param request Current HTTP request.
	 * @return An authenticated user, or null if cannot find authentication information.
	 * @throws APIAuthenticationException
	 */
    User authenticate(HttpServletRequest request, HttpServletResponse response);

}
