package com.richodemus.dropwizard.jwt.helpers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse
{
	private final String username;

	@JsonCreator
	public LoginResponse(@JsonProperty("username") String username)
	{
		this.username = username;
	}

	public String getUsername()
	{
		return username;
	}
}
