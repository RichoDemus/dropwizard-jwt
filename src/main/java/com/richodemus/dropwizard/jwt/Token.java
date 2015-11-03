package com.richodemus.dropwizard.jwt;

import com.auth0.jwt.JWTVerifier;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;
import java.util.Objects;

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
		return getString("user");
	}

	@JsonIgnore
	public String getRole()
	{
		return getString("role");
	}

	@JsonIgnore
	public String getExpiration()
	{
		return String.valueOf(getInt("exp"));
	}

	//todo rewrite this and getInt
	private String getString(String user)
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

	private int getInt(String user)
	{
		try
		{
			final Map<String, Object> verify = new JWTVerifier(SecretKeeper.SECRET).verify(raw);

			final Object asd = verify.get(user);
			return (Integer) asd;
		}
		catch (Exception e)
		{
			throw new IllegalStateException(e);
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		final Token token = (Token) o;
		return Objects.equals(raw, token.raw);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(raw);
	}

	@Override
	public String toString()
	{
		return getUsername() + ", " + getRole() + ", " + getExpiration();
	}
}
