package com.richodemus.dropwizard.jwt;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;

public class AuthenticationManager
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final UserService userService;
	private final Duration tokenDuration;
	private final String secret;

	@Inject
	public AuthenticationManager(final UserService userService,
								 final @Named("tokenDuration") Duration tokenDuration,
								 final @Named("secret") String secret)
	{
		this.userService = userService;
		this.tokenDuration = tokenDuration;
		this.secret = secret;
	}

	public Optional<RawToken> login(String username, String password)
	{
		return userService.login(username, password)
				.flatMap(this::generateToken);
	}

	public Token parseToken(final RawToken raw)
	{
		return new TokenParser(secret, raw).parse();
	}

	public Optional<RawToken> refreshToken(RawToken rawToken)
	{
		final Token token = parseToken(rawToken);
		final Map<String, Object> claims;
		try
		{
			claims = new JWTVerifier(secret).verify(rawToken.stringValue());
		}
		catch (Exception e)
		{
			logger.error("Exception when validating token", e);
			return Optional.empty();
		}
		//todo think more about this, is this enough validation?

		return generateToken(new TokenCreationCommand(claims));
	}

	private Optional<RawToken> generateToken(final TokenCreationCommand tokenCreationCommand)
	{
		try
		{
			final JWTSigner signer = new JWTSigner(secret);

			//todo set all the other fields such as issuer
			final JWTSigner.Options options = new JWTSigner.Options();
			options.setExpirySeconds((int) tokenDuration.getSeconds());
			return Optional.of(new RawToken(signer.sign(tokenCreationCommand, options)));
		}
		catch (Exception e)
		{
			logger.error("Unable to create token for claims {}", tokenCreationCommand, e);
			return Optional.empty();
		}
	}
}
