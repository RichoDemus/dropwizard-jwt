package com.richodemus.dropwizard.jwt;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;
import com.richodemus.dropwizard.jwt.model.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthenticationManager
{
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final UserService userService;
	private final TokenBlacklist blacklist;

	public AuthenticationManager(UserService userService)
	{
		this.userService = userService;
		blacklist = new TokenBlacklist();
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
			options.setExpirySeconds(60 * 10);
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
		if(blacklist.isBlacklisted(token))
		{
			logger.debug("Token {} is blacklisted", token);
			//todo maybe have a more explicit "TokenBlacklistedException"?
			return Optional.empty();
		}
		try
		{
			final Map<String, Object> claims = new JWTVerifier(SecretKeeper.SECRET).verify(token.getRaw());
		}
		catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | IOException | JWTVerifyException e)
		{
			e.printStackTrace();
		}

		//todo think more about this, is this enough?
		return generateToken(token.getUsername(), new Role(token.getRole()));
	}

	public void logout(Token token)
	{
		blacklist.blacklist(token);
	}
}
