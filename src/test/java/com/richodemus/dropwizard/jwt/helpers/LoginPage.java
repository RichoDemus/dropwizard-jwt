package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.Token;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserRequest;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;
import com.richodemus.dropwizard.jwt.helpers.model.LogoutResponse;

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

	public Token login(String existingUser, String existingUserPassword)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/users/login")
				.request()
				.post(Entity.json(new LoginRequest(existingUser, existingUserPassword)), Token.class);
	}

	public Token refreshToken(Token token)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/users/refresh-token")
				.request()
				.header("x-token-jwt", token.getRaw())
				.post(Entity.json(null), Token.class);
	}

	public LogoutResponse logout(Token token)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/users/logout")
				.request()
				.header("x-token-jwt", token.getRaw())
				.post(Entity.json(null), LogoutResponse.class);
	}
}
