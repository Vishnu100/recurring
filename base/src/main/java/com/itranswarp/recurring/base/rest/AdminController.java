package com.itranswarp.recurring.base.rest;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.itranswarp.recurring.base.context.TenantContext;
import com.itranswarp.recurring.base.model.PasswordAuth;
import com.itranswarp.recurring.base.model.Tenant;
import com.itranswarp.recurring.base.model.User;
import com.itranswarp.recurring.base.util.HashUtil;
import com.itranswarp.recurring.common.util.IdGenerator;
import com.itranswarp.recurring.db.Database;

@RestController
public class AdminController {

	static final String SUPER_TENANT_ID = "000000000000011ffffffffffffffffffffffffffffff00000";
	static final String SUPER_ADMIN_ID = "00000000000001199999999999999999999999999999900000";

	final Log log = LogFactory.getLog(getClass());

	@Inject
	Database database;

	@Transactional
	@RequestMapping(value = "/api/initialize", method = RequestMethod.POST)
	public User initialize() throws Exception {
		log.info("Try initialize super tenant...");
		Tenant superTenant = database.fetch(Tenant.class, SUPER_TENANT_ID);
		if (superTenant != null) {
			throw new RuntimeException("Super tenant already exist.");
		}
		// super tenant:
		superTenant = new Tenant();
		superTenant.setId(SUPER_TENANT_ID);
		superTenant.setTenantId(SUPER_TENANT_ID);
		superTenant.setCreatedBy(SUPER_ADMIN_ID);
		superTenant.setUpdatedBy(SUPER_ADMIN_ID);
		superTenant.setName("Super Tenant");
		superTenant.setEmail("super@itranswarp.com");
		// super user:
		User superUser = new User();
		superUser.setId(SUPER_ADMIN_ID);
		superUser.setTenantId(SUPER_TENANT_ID);
		superUser.setCreatedBy(SUPER_ADMIN_ID);
		superUser.setUpdatedBy(SUPER_ADMIN_ID);
		superUser.setEmail("super@itranswarp.com");
		superUser.setName("Super Admin");
		// default password:
		String salt = IdGenerator.next();
		PasswordAuth passwordAuth = new PasswordAuth();
		passwordAuth.setId(salt);
		passwordAuth.setTenantId(SUPER_TENANT_ID);
		passwordAuth.setUserId(SUPER_ADMIN_ID);
		passwordAuth.setCreatedBy(SUPER_ADMIN_ID);
		passwordAuth.setUpdatedBy(SUPER_ADMIN_ID);
		passwordAuth.setPassword(generatePassword(salt, "password"));
		try (TenantContext ctx = new TenantContext(superTenant, superUser)) {
			database.save(passwordAuth);
			database.save(superUser);
			database.save(superTenant);
		}
		return superUser;
	}

	String generatePassword(String id, String plainPassword) {
		String inputPassword = HashUtil.sha1(plainPassword);
		return HashUtil.sha1(id + ":" + inputPassword);
	}
}
