package com.itranswarp.recurring.api;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.itranswarp.recurring.api.exception.AuthenticateException;
import com.itranswarp.recurring.base.auth.Authenticator;
import com.itranswarp.recurring.base.context.TenantContext;
import com.itranswarp.recurring.base.model.Tenant;
import com.itranswarp.recurring.base.model.User;
import com.itranswarp.recurring.db.Database;

/**
 * Do authentication for each request and set TenantContext if authenticate OK.
 * 
 * @author michael
 */
@Named("authenticationFilter")
public class AuthenticationFilter implements Filter {

    final Log log = LogFactory.getLog(getClass());

    @Inject
    Database database;

    @Inject
    Authenticator[] authenticators = new Authenticator[0];

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        User user = null;
        Tenant tenant = null;
        try {
        	for (Authenticator authenticator : this.authenticators) {
        		user = authenticator.authenticate(request, response);
        		if (user != null) {
                    log.info("Authenticate ok.");
        			break;
        		}
        	}
        }
        catch (AuthenticateException e) {
            if (! response.isCommitted()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
            return;
        }
        if (user == null) {
            chain.doFilter(request, response);
        }
        else {
            log.info("Authentication bind to: " + user.getEmail());
            tenant = database.get(Tenant.class, user.getTenantId());
            try (TenantContext context = new TenantContext(tenant, user)) {
                chain.doFilter(request, response);
            } catch (Exception e) {
                throw new ServletException(e);
            }
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Init AuthenticationFilter...");
    }

    @Override
    public void destroy() {
    }
}
