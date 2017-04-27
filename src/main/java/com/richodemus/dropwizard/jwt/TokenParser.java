package com.richodemus.dropwizard.jwt;

import com.auth0.jwt.JWTVerifier;

import java.util.Map;

public class TokenParser
{
	private final String secret;
	private final RawToken raw;

	public TokenParser(final String secret, final RawToken raw)
	{
		this.secret = secret;
		this.raw = raw;
	}

	public Token parse()
	{
		try
		{
			final Map<String, Object> claims = new JWTVerifier(secret).verify(raw.stringValue());
			return new Token(claims);
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}

	private String parseExpiration(Map<String, Object> claims)
	{
		final Object exp = claims.get("exp");
		if (exp == null)
		{
			throw new IllegalStateException("no expiration field in token");
		}
		return String.valueOf(exp);
	}
}
