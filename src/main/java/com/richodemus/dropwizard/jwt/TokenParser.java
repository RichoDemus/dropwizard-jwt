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
		return new Token(getUsername(), getRole(), getExpiration());
	}

	private String getUsername()
	{
		return get("user");
	}

	private String getRole()
	{
		return get("role");
	}

	private String getExpiration()
	{
		return get("exp");
	}

	//todo rewrite this and getInt
	private String get(String field)
	{
		try
		{
			final Map<String, Object> verify = new JWTVerifier(secret).verify(raw.stringValue());

			final Object asd = verify.get(field);

			if(!verify.containsKey(field))
			{
				return "";
			}

			if(asd instanceof String)
			{
				return (String) asd;
			}
			return String.valueOf(asd);
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}
}
