package com.itranswarp.recurring.api;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

public class CorsFilterTest {

	CorsFilter filter = new CorsFilter();

	@Test
	public void testPreflightRequest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("OPTIONS");
		request.setRequestURI("/api/test");
		MockHttpServletResponse response = new MockHttpServletResponse();
		FilterChain chain = new FilterChain() {
			@Override
			public void doFilter(ServletRequest request, ServletResponse response)
					throws IOException, ServletException {
			}
		};
		filter.doFilter(request, response, chain);
		assertEquals(200, response.getStatus());
		assertEquals("*", response.getHeaderValue("Access-Control-Allow-Origin"));
		assertEquals("GET, POST, PUT, DELETE, OPTIONS", response.getHeaderValue("Access-Control-Allow-Methods"));
	}

}
