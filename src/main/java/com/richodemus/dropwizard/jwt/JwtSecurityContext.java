package com.richodemus.dropwizard.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

public class JwtSecurityContext implements SecurityContext
{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Context
	HttpServletRequest webRequest;

	@Override
	public Principal getUserPrincipal()
	{
		logger.trace("getUserPrincipal called");
		return null;
	}

	@Override
	public boolean isUserInRole(String role)
	{
		logger.trace("isUserinRole {} called", role);

		return true;
	}

	@Override
	public boolean isSecure()
	{
		logger.trace("isSecure called");
		return false;
	}

	@Override
	public String getAuthenticationScheme()
	{
		logger.trace("getAuthScheme called");
		return null;
	}
}
