package com.itranswarp.recurring.base.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itranswarp.recurring.base.model.ApiAuth;
import com.itranswarp.recurring.base.model.User;
import com.itranswarp.recurring.common.exception.APIAuthenticationException;
import com.itranswarp.recurring.db.Database;

/**
 * Authenticate by header: Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
 * 
 * @author michael
 */
@Named
@Priority(10)
public class ApiKeyAuthenticator implements Authenticator {

	final Log log = LogFactory.getLog(getClass());

    static final String HEADER_API_KEY = "X-API-Key";
    static final String HEADER_API_SECRET = "X-API-Secret";

    @Inject
    Database database;

	@Override
	public User authenticate(HttpServletRequest request, HttpServletResponse response) {
		String apiKey = request.getHeader(HEADER_API_KEY);
		if (apiKey == null) {
			return null;
		}
		String apiSecret = request.getHeader(HEADER_API_SECRET);
		if (apiSecret == null) {
			throw new APIAuthenticationException(HEADER_API_SECRET, "Missing http header: " + HEADER_API_SECRET);
		}
		ApiAuth aa = database.fetch("select * from ApiAuth where apiKey=? and apiSecret=?", apiKey, apiSecret);
		if (aa == null || aa.isDisabled()) {
			throw new APIAuthenticationException("Invalid " + HEADER_API_KEY + " or " + HEADER_API_SECRET);
		}
		return database.get(User.class, aa.getUserId());
	}

}
