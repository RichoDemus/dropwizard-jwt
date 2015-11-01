package com.richodemus.dropwizard.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@Priority(Priorities.AUTHENTICATION)
@PreMatching
public class AuthenticationRequestFilter implements ContainerRequestFilter
{
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException
	{
		logger.info("Filter called");
		requestContext.setSecurityContext(new JwtSecurityContext());
	}
}