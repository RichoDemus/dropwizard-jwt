package com.richodemus.dropwizard.jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashMap;
import java.util.Map;

public class Token extends HashMap<String, Object>
{
	private static final long serialVersionUID = 1L;

	public Token(final Map<String, Object> claims)
	{
		super(claims);
	}

	@JsonIgnore
	public String getUsername()
	{
		return String.valueOf(get("username"));
	}

	@JsonIgnore
	public String getRole()
	{
		return String.valueOf(get("role"));
	}
}
