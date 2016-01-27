package com.richodemus.dropwizard.jwt;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.richodemus.dropwizard.jwt.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthenticationManager
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final UserService userService;
	private final TokenBlacklist blacklist;
	private final Duration tokenDuration;

	@Inject
	public AuthenticationManager(final UserService userService, final @Named("tokenDuration") Duration tokenDuration)
	{
		this.userService = userService;
		this.tokenDuration = tokenDuration;
		this.blacklist = new TokenBlacklist();
	}

	public Optional<Token> login(String username, String password)
	{
		return userService.login(username, password)
				.flatMap(role -> generateToken(username, role));
	}

	private Optional<Token> generateToken(String username, Role role)
	{
		try
		{
			final JWTSigner signer = new JWTSigner(SecretKeeper.SECRET);

			Map<String, Object> claims = new HashMap<>();
			claims.put("user", username);
			claims.put("role", role.stringValue());

			//todo set all the other fields such as issuer
			final JWTSigner.Options options = new JWTSigner.Options();
			options.setExpirySeconds((int)tokenDuration.getSeconds());
			return Optional.of(new Token(signer.sign(claims, options)));
		}
		catch (Exception e)
		{
			logger.error("Unable to create token for user {}", username, e);
			return Optional.empty();
		}
	}

	public Optional<Token> refreshToken(Token token)
	{
		if (blacklist.isBlacklisted(token))
		{
			logger.debug("Token {} is blacklisted", token);
			//todo maybe have a more explicit "TokenBlacklistedException"?
			return Optional.empty();
		}
		try
		{
			final Map<String, Object> claims = new JWTVerifier(SecretKeeper.SECRET).verify(token.getRaw());
		}
		catch (Exception e)
		{
			logger.error("Exception when validating token", e);
			return Optional.empty();
		}
		//todo think more about this, is this enough validation?

		final Optional<Token> maybeNewToken = generateToken(token.getUsername(), new Role(token.getRole()));
		maybeNewToken.ifPresent(newToken -> blackListIfNotEqual(token, newToken));
		return maybeNewToken;
	}

	private void blackListIfNotEqual(Token token, Token newToken)
	{
		if (!token.equals(newToken))
		{
			blacklist.blacklist(token);
		}
	}

	public void logout(Token token)
	{
		blacklist.blacklist(token);
	}

	public boolean isBlackListed(Token token)
	{
		return blacklist.isBlacklisted(token);
	}
}
