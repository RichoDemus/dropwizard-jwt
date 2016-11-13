package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.RawToken;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserRequest;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

public class LoginPage
{
	private final int port;

	public LoginPage(int port)
	{
		this.port = port;
	}
	
	public CreateUserResponse createUser(String expectedRole, String nonExistingUser, String nonExistingUserPassword)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/users/new")
				.request()
				.post(Entity.json(new CreateUserRequest(nonExistingUser, nonExistingUserPassword, expectedRole)), CreateUserResponse.class);
	}

	public RawToken login(String existingUser, String existingUserPassword)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/users/login")
				.request()
				.post(Entity.json(new LoginRequest(existingUser, existingUserPassword)), RawToken.class);
	}

	public RawToken refreshToken(RawToken token)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/users/refresh-token")
				.request()
				.header("x-token-jwt", token.stringValue())
				.post(Entity.json(null), RawToken.class);
	}
}
