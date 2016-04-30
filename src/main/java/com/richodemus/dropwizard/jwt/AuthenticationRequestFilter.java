package com.richodemus.dropwizard.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Optional;

@Provider
@Priority(Priorities.AUTHENTICATION)
@PreMatching
public class AuthenticationRequestFilter implements ContainerRequestFilter
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final AuthenticationManager authenticationManager;

	@Inject
	public AuthenticationRequestFilter(AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException
	{
		logger.info("Filter called");
		try
		{
			final Optional<RawToken> rawToken = Optional.of("x-token-jwt")
					.map(requestContext::getHeaderString)
					.map(RawToken::new);
			final Optional<Token> token = rawToken
					.map(authenticationManager::parseToken);

			requestContext.setSecurityContext(new JwtSecurityContext(
					authenticationManager,
					rawToken,
					token));
		}
		catch (Exception e)
		{
			throw new BadRequestException(e);
		}
	}
}
