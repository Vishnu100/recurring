package com.itranswarp.recurring.base.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itranswarp.recurring.base.model.User;

/**
 * Authenticate by header: Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==
 * 
 * @author michael
 *
 */
@Named
@Priority(20)
public class BasicAuthenticator implements Authenticator {

	final Log log = LogFactory.getLog(getClass());

	@Inject PasswordAuthenticatorHelper authHelper;

	@Override
	public User authenticate(HttpServletRequest request, HttpServletResponse response) {
		String header = request.getHeader("Authorization");
		if (header == null) {
			log.info("No Authorization header found.");
			return null;
		}
		if ((header.length() < 7) || !header.substring(0, 6).equalsIgnoreCase("basic ")) {
			log.warn("Invalid Authorization header: must be format to \"Basic XXX\".");
			return null;
		}
		String auth = null;
		try {
			auth = new String(Base64.getDecoder().decode(header.substring(6)), StandardCharsets.UTF_8);
		} catch (IllegalArgumentException e) {
			log.warn("Invalid format of Authorization header: " + header);
			return null;
		}
		String[] ss = auth.split("\\:", 2);
		if (ss.length != 2) {
			log.warn("Invalid format of Authorization header: " + header);
			return null;
		}
		String email = ss[0];
		String password = ss[1];
		return authHelper.getAuthenticateUser(email, password);
	}

}
