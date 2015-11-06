package com.itranswarp.recurring.api;

import java.io.IOException;
import java.util.Arrays;

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

/**
 * Do CORS for each request.
 * 
 * @author michael
 */
@Named("corsFilter")
public class CorsFilter implements Filter {

    final Log log = LogFactory.getLog(getClass());

    final String accessControlAllowMethods = String.join(", ", Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    final String accessControlAllowHeaders = String.join(", ", Arrays.asList("X-API-Key", "X-API-Secret", "X-Client-Timestamp"));
    final String accessControlMaxAge = Integer.toString(3600 * 24 * 7);

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        String httpMethod = request.getMethod();
        if ("OPTIONS".equals(httpMethod)) {
            // handle pre-flighted request:
            log.info("handle pre-flighted request: OPTIONS " + request.getRequestURI());
        	response.setHeader("Access-Control-Allow-Origin", "*");
        	response.setHeader("Access-Control-Allow-Methods", accessControlAllowMethods);
        	response.setHeader("Access-Control-Allow-Headers", accessControlAllowHeaders);
        	response.setHeader("Access-Control-Max-Age", accessControlMaxAge);
            return;
        }
    	response.setHeader("Access-Control-Allow-Origin", "*");
        chain.doFilter(req, resp);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Init CorsFilter...");
    }

    @Override
    public void destroy() {
    }
}
