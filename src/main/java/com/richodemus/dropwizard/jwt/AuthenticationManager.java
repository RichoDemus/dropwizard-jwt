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
	private final Duration tokenDuration;
	private final String secret;

	@Inject
	public AuthenticationManager(final UserService userService, final @Named("tokenDuration") Duration tokenDuration, final @Named("secret") String secret)
	{
		this.userService = userService;
		this.tokenDuration = tokenDuration;
		this.secret = secret;
	}

	public Optional<RawToken> login(String username, String password)
	{
		return userService.login(username, password)
				.flatMap(role -> generateToken(username, role));
	}

	private Optional<RawToken> generateToken(String username, Role role)
	{
		try
		{
			final JWTSigner signer = new JWTSigner(secret);

			Map<String, Object> claims = new HashMap<>();
			claims.put("user", username);
			claims.put("role", role.stringValue());

			//todo set all the other fields such as issuer
			final JWTSigner.Options options = new JWTSigner.Options();
			options.setExpirySeconds((int)tokenDuration.getSeconds());
			return Optional.of(new RawToken(signer.sign(claims, options)));
		}
		catch (Exception e)
		{
			logger.error("Unable to create token for user {}", username, e);
			return Optional.empty();
		}
	}

	public Token parseToken(final RawToken raw)
	{
		return new TokenParser(secret, raw).parse();
	}

	public Optional<RawToken> refreshToken(RawToken rawToken)
	{
		final Token token = parseToken(rawToken);
		try
		{
			final Map<String, Object> claims = new JWTVerifier(secret).verify(rawToken.stringValue());
		}
		catch (Exception e)
		{
			logger.error("Exception when validating token", e);
			return Optional.empty();
		}
		//todo think more about this, is this enough validation?

		final Optional<RawToken> maybeNewToken = generateToken(token.getUsername(), new Role(token.getRole()));
		return maybeNewToken;
	}
}
