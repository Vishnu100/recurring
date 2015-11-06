package com.itranswarp.recurring.base.auth;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itranswarp.recurring.base.model.PasswordAuth;
import com.itranswarp.recurring.base.util.HashUtil;
import com.itranswarp.recurring.db.Database;

@Named
public class CookieAuthenticatorHelper {

	final Log log = LogFactory.getLog(getClass());

	String salt = "RecurringSalt";
	String sessionCookieName = "rcsession";
	int sessionCookieExpires = 3600;
	int sessionCookieRefreshTime = 300;

	@Inject
	Database database;

	/**
	 * Encode value as "auth_id : expires : sha1(auth_id : expires : password : salt)"
	 * 
	 * @param auth
	 * @return
	 */
	public String encode(PasswordAuth auth) {
		String expiresTime = String.valueOf(sessionCookieExpires * 1000L + System.currentTimeMillis());
		String sha1 = HashUtil.sha1(new StringBuilder(150)
				.append(auth.getId())
				.append(':')
				.append(expiresTime)
				.append(':')
				.append(auth.getPassword())
				.append(':')
				.append(this.salt)
				.toString());
		return new StringBuilder(150)
				.append(auth.getId())
				.append(':')
				.append(expiresTime)
				.append(':')
				.append(sha1).toString();
	}

	CookieAuth decode(String str) {
		String[] ss = str.split("\\:", 3);
		if (ss.length != 3) {
			return null;
		}
		String authId = ss[0];
		String expiresTime = ss[1];
		String sha1 = ss[2];
		long expires = 0;
		if (sha1.length() != 40) {
			log.info("Invalid sha1: " + str);
			return null;
		}
		try {
			expires = Long.parseLong(expiresTime);
		} catch (NumberFormatException e) {
			log.info("Invalid expires: " + str);
			return null;
		}
		if (expires < System.currentTimeMillis()) {
			log.info("token expires: " + str);
			return null;
		}
		PasswordAuth pa = database.fetch(PasswordAuth.class, authId);
		if (pa == null) {
			log.info("Auth not found by id: " + str);
			return null;
		}
		String expectedSha1 = HashUtil.sha1(new StringBuilder(150)
				.append(authId)
				.append(':')
				.append(expiresTime)
				.append(':')
				.append(pa.getPassword())
				.append(':')
				.append(this.salt).toString());
		if (! sha1.equals(expectedSha1)) {
			log.info("Sha1 failed: " + str);
			return null;
		}
		return new CookieAuth(pa.getUserId(), expires, pa);
	}

	String getSessionCookieValue(HttpServletRequest request, String sessionCookieName) {
		Cookie[] cs = request.getCookies();
		if (cs == null) {
			return null;
		}
		for (Cookie c : cs) {
			if (sessionCookieName.equals(c.getName())) {
				try {
					return new String(Base64.getUrlDecoder().decode(c.getValue()), StandardCharsets.UTF_8);
				} catch (IllegalArgumentException e) {
					return null;
				}
			}
		}
		return null;
	}

	public void setSessionCookie(HttpServletResponse response, String sessionCookieValue) {
		String encodedValue = Base64.getUrlEncoder()
				.encodeToString(sessionCookieValue.getBytes(StandardCharsets.UTF_8));
		_setCookie(response, encodedValue, this.sessionCookieExpires);
	}

	void deleteSessionCookie(HttpServletResponse response) {
		_setCookie(response, "-deleted-", 0);
	}

	void _setCookie(HttpServletResponse response, String value, int expiry) {
		Cookie cookie = new Cookie(sessionCookieName, value);
		cookie.setMaxAge(expiry);
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		response.addCookie(cookie);
	}

	boolean needRefresh(long expiresAt) {
		return (expiresAt - System.currentTimeMillis()) > this.sessionCookieRefreshTime * 1000L;
	}
}

class CookieAuth {

	final String userId;
	final long expires;
    final PasswordAuth passwordAuth;

	public CookieAuth(String userId, long expires, PasswordAuth passwordAuth) {
		this.userId = userId;
		this.expires = expires;
		this.passwordAuth = passwordAuth;
	}

}