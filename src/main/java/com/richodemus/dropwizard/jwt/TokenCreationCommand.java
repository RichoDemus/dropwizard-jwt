package com.richodemus.dropwizard.jwt;

import java.util.HashMap;
import java.util.Map;

public class TokenCreationCommand extends HashMap<String, Object>
{
	private static final long serialVersionUID = 1L;

	public TokenCreationCommand(final String username, final String role)
	{
		this(username, role, new HashMap<>());
	}

	public TokenCreationCommand(final String username, final String role, final Map<String, Object> claims)
	{
		super(claims);
		put("username", username);
		put("role", role);
	}

	TokenCreationCommand(final Map<String, Object> initial)
	{
		super(initial);
		if (!containsKey("username"))
		{
			throw new IllegalArgumentException("Missing claim username");
		}
		if (!containsKey("role"))
		{
			throw new IllegalArgumentException("Missing claim role");
		}
	}

	public String getUsername()
	{
		return String.valueOf(get("username"));
	}

	public String getRole()
	{
		return String.valueOf(get("role"));
	}
}
