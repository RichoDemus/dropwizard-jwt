package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.AuthenticationManager;
import com.richodemus.dropwizard.jwt.Token;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("login")
public class LoginResource
{
	private final AuthenticationManager authenticationManager;

	public LoginResource(AuthenticationManager authenticationManager)
	{
		this.authenticationManager = authenticationManager;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Token login(LoginRequest loginRequest)
	{
		return authenticationManager.login(loginRequest.getUsername(), loginRequest.getPassword());
	}
}
