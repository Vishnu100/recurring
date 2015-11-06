package com.itranswarp.recurring.api.ui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.itranswarp.recurring.base.model.Tenant;
import com.itranswarp.recurring.base.model.User;

@Controller
public class UIController {

	final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value = "/view/{name}", method = RequestMethod.GET)
	public ModelAndView view(@PathVariable String name) {
		// check if signin:
		// to json:
		SessionBean session = new SessionBean(new Tenant(), new User(), name);
		// set a demo name:
		session.user.setName("Super Admin");
		return new ModelAndView(name + ".html", "session", session);
	}

	@RequestMapping(value = "/signin", method = RequestMethod.GET)
	public ModelAndView signIn() {
		ModelAndView mv = new ModelAndView("signin.html");
		return mv;
	}

}

class SessionBean {

	Tenant tenant;
	User user;
	String template;
	long currentTime = System.currentTimeMillis();

	public SessionBean(Tenant tenant, User user, String template) {
		this.tenant = tenant;
		this.user = user;
		this.template = template;
	}

	public Tenant getTenant() {
		return tenant;
	}

	public User getUser() {
		return user;
	}

	public String getTemplate() {
		return template;
	}

	public long getCurrentTime() {
		return currentTime;
	}

}
