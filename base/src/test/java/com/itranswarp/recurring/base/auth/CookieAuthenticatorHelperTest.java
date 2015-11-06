package com.itranswarp.recurring.base.auth;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.itranswarp.recurring.base.model.PasswordAuth;
import com.itranswarp.recurring.base.util.HashUtil;
import com.itranswarp.recurring.db.Database;
import com.itranswarp.recurring.db.model.BaseEntity;

public class CookieAuthenticatorHelperTest {

	CookieAuthenticatorHelper helper;
	PasswordAuth passwordAuth;

	final String EXIST_AUTH_ID = "0014f2a341cc1c05e9bd61b0142cea7de24f2a8068a3f00000";
	final String EXIST_USER_ID = "0014f2a341cc12d27b3757b8f4c329fb6af6c8807674000000";
	final String PASSWORD = HashUtil.sha1("password");

	@Before
	public void setUp() {
		helper = new CookieAuthenticatorHelper();
		helper.sessionCookieExpires = 1;
		passwordAuth = new PasswordAuth();
		passwordAuth.setId(EXIST_AUTH_ID);
		passwordAuth.setUserId(EXIST_USER_ID);
		passwordAuth.setPassword(PASSWORD);
		passwordAuth.setCreatedBy(EXIST_USER_ID);
		passwordAuth.setUpdatedBy(EXIST_USER_ID);
	
		helper.database = new Database(null) {
			@Override
			@SuppressWarnings("unchecked")
			public <T extends BaseEntity> T fetch(Class<T> clazz, String id) {
				if (EXIST_AUTH_ID.equals(id)) {
					return (T) passwordAuth;
				}
				return null;
			}
		};
	}

	@Test
	public void testEncodeAndDecode() {
		String token = helper.encode(passwordAuth);
		CookieAuth auth = helper.decode(token);
		assertNotNull(auth);
		assertEquals(passwordAuth.getUserId(), auth.userId);
		// decode for invalid auth id:
		assertNull(helper.decode(replaceCharAt(token, 'f', 0)));
		// decode for invalid expires:
		assertNull(helper.decode(replaceCharAt(token, 'x', 51)));
		// decode for invalid sha1:
		assertNull(helper.decode(replaceCharAt(token, 'x', token.length()-1)));
	}

	@Test
	public void testEncodeAndDecodeButExpires() throws Exception {
		String token = helper.encode(passwordAuth);
		CookieAuth auth = helper.decode(token);
		assertNotNull(auth);
		Thread.sleep(1200);
		// token expires:
		assertNull(helper.decode(token));
		// re-generate token:
		String refreshedToken = helper.encode(auth.passwordAuth);
		CookieAuth authAgain = helper.decode(refreshedToken);
		assertNotNull(authAgain);
		assertEquals(auth.userId, authAgain.userId);
	}

	String replaceCharAt(String str, char ch, int pos) {
		char[] cs = str.toCharArray();
		cs[pos] = ch;
		return new String(cs);
	}
}
