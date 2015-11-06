package com.itranswarp.recurring.base.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itranswarp.recurring.base.auth.CookieAuthenticatorHelper;
import com.itranswarp.recurring.base.auth.PasswordAuthenticatorHelper;
import com.itranswarp.recurring.base.model.PasswordAuth;
import com.itranswarp.recurring.base.model.User;

@RestController
public class PasswordAuthenticateController {

    final Log log = LogFactory.getLog(getClass());

	@Inject PasswordAuthenticatorHelper authHelper;

	@Inject CookieAuthenticatorHelper cookieHelper;

	/**
	 * Do authentication by email and password. If succeeded, session cookie is set for re-bind 
	 * current user.
	 * 
	 * @param bean
	 * @param response
	 * @return
	 */
    @RequestMapping(value="/api/authenticate", method=RequestMethod.POST)
    public User authenticateByPassword(@RequestBody PasswordAuthBean bean, HttpServletResponse response) {
    	User user = authHelper.tryFindUser(bean.email, bean.password);
    	PasswordAuth auth = authHelper.authenticateByPassword(user, bean.password);
    	cookieHelper.setSessionCookie(response, cookieHelper.encode(auth));
        return user;
    }

}
