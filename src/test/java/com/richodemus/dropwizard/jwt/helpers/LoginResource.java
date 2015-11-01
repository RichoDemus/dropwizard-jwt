package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.AuthenticationManager;
import com.richodemus.dropwizard.jwt.Token;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserRequest;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;
import com.richodemus.dropwizard.jwt.model.Role;

import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("login")
public class LoginResource
{
	private final AuthenticationManager authenticationManager;
	private final UserServiceImpl userService;

	public LoginResource(AuthenticationManager authenticationManager, UserServiceImpl userService)
	{
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Token login(LoginRequest loginRequest)
	{
		return authenticationManager.login(loginRequest.getUsername(), loginRequest.getPassword())
				.orElseThrow(ForbiddenException::new);
	}

	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public CreateUserResponse createUser(CreateUserRequest createUserRequest)
	{
		userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword(), new Role(createUserRequest.getRole()));
		return new CreateUserResponse();
	}
}
