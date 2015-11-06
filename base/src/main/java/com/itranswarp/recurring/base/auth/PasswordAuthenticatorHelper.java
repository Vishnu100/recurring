package com.itranswarp.recurring.base.auth;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import com.itranswarp.recurring.base.model.PasswordAuth;
import com.itranswarp.recurring.base.model.User;
import com.itranswarp.recurring.base.util.HashUtil;
import com.itranswarp.recurring.common.exception.APIArgumentException;
import com.itranswarp.recurring.common.exception.APIAuthenticationException;
import com.itranswarp.recurring.db.Database;

@Named
public class PasswordAuthenticatorHelper {

	final Log log = LogFactory.getLog(getClass());

	@Inject Database database;

	public User getAuthenticateUser(String email, String password) {
		User user = tryFindUser(email, password);
        authenticateByPassword(user, password);
        return user;
    }

	public PasswordAuth authenticateByPassword(User user, String password) {
        String userId = user.getId();
        PasswordAuth auth = database.unique("select * from PasswordAuth where userId=?", userId);
        if (! HashUtil.sha1(auth.getId() + ":" + password).equals(auth.getPassword())) {
        	throw new APIAuthenticationException("password", "Invalid password.");
        }
        return auth;
	}

	public User tryFindUser(String email, String password) {
        log.info("Try authenticate user " + email + "...");
        if (StringUtils.isEmpty(email)) {
        	throw new APIArgumentException("email", "Bad email.");
        }
        if (StringUtils.isEmpty(password) || password.length() != 40) {
        	throw new APIArgumentException("password", "Bad password.");
        }
        // find user:
        User user = database.fetch("select * from User where email=?", email);
        if (user == null) {
        	throw new APIArgumentException("email", "Email not exist.");
        }
        return user;
    }
}
