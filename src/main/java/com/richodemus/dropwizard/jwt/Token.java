package com.richodemus.dropwizard.jwt;

import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class Token
{
	private final String raw;

	@JsonCreator
	public Token(@JsonProperty("raw") String raw)
	{
		this.raw = raw;
	}

	public String getRaw()
	{
		return raw;
	}

	@JsonIgnore
	public String getUsername()
	{
		return get("user");
	}

	@JsonIgnore
	public String getRole()
	{
		return get("role");
	}

	private String get(String user)
	{
		try
		{
			final Map<String, Object> verify = new JWTVerifier(SecretKeeper.SECRET).verify(raw);

			final Object asd = verify.get(user);
			return (String) asd;
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}
}
