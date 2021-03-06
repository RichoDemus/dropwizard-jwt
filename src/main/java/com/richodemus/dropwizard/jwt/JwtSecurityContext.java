package com.richodemus.dropwizard.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;
import java.util.Optional;

public class JwtSecurityContext implements SecurityContext
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final AuthenticationManager authenticationManager;
	private final Optional<RawToken> maybeRawToken;
	private final Optional<Token> maybeToken;

	//Fix this.....
	public JwtSecurityContext(AuthenticationManager authenticationManager, Optional<RawToken> maybeRawToken, Optional<Token> maybeToken)
	{
		this.authenticationManager = authenticationManager;
		this.maybeRawToken = maybeRawToken;
		this.maybeToken = maybeToken;
	}

	@Override
	public Principal getUserPrincipal()
	{
		logger.trace("getUserPrincipal called");
		//todo validate token right away
		return maybeToken
				.map(Token::getUsername)
				.map(username -> (Principal) () -> username)
				.orElse(() -> "Unknown user");
	}

	@Override
	public boolean isUserInRole(String role)
	{
		logger.trace("isUserinRole {} called", role);

		if (!maybeToken.isPresent())
		{
			logger.debug("No token, user not logged in");
			return false;
		}

		if (role.equals("any"))
		{
			logger.debug("all logged in users are considered to be of role any");
			return true;
		}

		return maybeToken.map(Token::getRole)
				.filter(role::equals)
				.isPresent();
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
