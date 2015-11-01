package com.richodemus.dropwizard.jwt;

import com.auth0.jwt.JWTSigner;
import com.richodemus.dropwizard.jwt.model.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthenticationManager
{
	private final UserService userService;

	public AuthenticationManager(UserService userService)
	{
		this.userService = userService;
	}

	public Optional<Token> login(String username, String password)
	{
		return userService.login(username, password)
				.map(role -> generateToken(username, role));
	}

	private Token generateToken(String username, Role role)
	{
		final JWTSigner signer = new JWTSigner(SecretKeeper.SECRET);

		Map<String, Object> claims = new HashMap<>();
		claims.put("user", username);
		claims.put("role", role.stringValue());

		final JWTSigner.Options options = new JWTSigner.Options();
		options.setExpirySeconds(60 * 10);
		return new Token(signer.sign(claims, options));
	}
}
