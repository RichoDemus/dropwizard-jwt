package com.richodemus.dropwizard.jwt;

import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;

import java.util.Objects;

public class Token
{
	private final String username;
	private final String role;
	private final String expiration;

	public Token(String username, String role, String expiration)
	{
		this.username = username;
		this.role = role;
		this.expiration = expiration;
	}

	@JsonIgnore
	public String getUsername()
	{
		return username;
	}

	@JsonIgnore
	public String getRole()
	{
		return role;
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
		Token token = (Token) o;
		return Objects.equals(username, token.username) &&
				Objects.equals(role, token.role) &&
				Objects.equals(expiration, token.expiration);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(username, role, expiration);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this)
				.add("username", username)
				.add("role", role)
				.add("expiration", expiration)
				.toString();
	}
}
