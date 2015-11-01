package com.richodemus.dropwizard.jwt.helpers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest
{
	private String role;
	private String password;
	private String username;

	@JsonCreator
	public CreateUserRequest(@JsonProperty("username") String username, @JsonProperty("password") String password, @JsonProperty("role") String role)
	{
		this.role = role;
		this.password = password;
		this.username = username;
	}

	public String getRole()
	{
		return role;
	}

	public String getPassword()
	{
		return password;
	}

	public String getUsername()
	{
		return username;
	}
}
