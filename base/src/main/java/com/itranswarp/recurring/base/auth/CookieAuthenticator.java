package com.itranswarp.recurring.base.auth;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itranswarp.recurring.base.model.User;
import com.itranswarp.recurring.db.Database;

/**
 * Authenticate by cookie.
 * 
 * @author michael
 */
@Named
@Priority(30)
public class CookieAuthenticator implements Authenticator {

	final Log log = LogFactory.getLog(getClass());

	String sessionCookieName = "rcsession";

	long refreshTime = 300 * 1000L;

	@Inject Database database;
	@Inject CookieAuthenticatorHelper cookieHelper;

	@Override
	public User authenticate(HttpServletRequest request, HttpServletResponse response) {
		String token = cookieHelper.getSessionCookieValue(request, this.sessionCookieName);
		if (token == null) {
			return null;
		}
		CookieAuth auth = cookieHelper.decode(token);
		if (auth == null) {
			cookieHelper.deleteSessionCookie(response);
			return null;
		}
		if (auth.expires - System.currentTimeMillis() < refreshTime) {
			cookieHelper.setSessionCookie(response, cookieHelper.encode(auth.passwordAuth));
		}
		return database.get(User.class, auth.userId);
	}

}
