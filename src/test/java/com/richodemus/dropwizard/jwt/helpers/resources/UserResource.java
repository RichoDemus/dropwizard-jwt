package com.richodemus.dropwizard.jwt.helpers.resources;

import com.richodemus.dropwizard.jwt.AuthenticationManager;
import com.richodemus.dropwizard.jwt.RawToken;
import com.richodemus.dropwizard.jwt.TokenCreationCommand;
import com.richodemus.dropwizard.jwt.helpers.UserServiceImpl;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserRequest;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

@Path("users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource
{
	private final AuthenticationManager authenticationManager;
	private final UserServiceImpl userService;

	public UserResource(AuthenticationManager authenticationManager, UserServiceImpl userService)
	{
		this.authenticationManager = authenticationManager;
		this.userService = userService;
	}

	@POST
	@Path("/login")
	public RawToken login(LoginRequest loginRequest)
	{
		return authenticationManager.login(loginRequest.getUsername(), loginRequest.getPassword())
				.orElseThrow(ForbiddenException::new);
	}

	@POST
	@Path("/new")
	public CreateUserResponse createUser(CreateUserRequest createUserRequest)
	{
		final Map<String, Object> claims = new HashMap<>();
		claims.put("specific-claim", "cool-value");
		final TokenCreationCommand tokenCreationCommand = new TokenCreationCommand("richo", createUserRequest.getRole(), claims);
		userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword(), tokenCreationCommand);
		return new CreateUserResponse(CreateUserResponse.Result.OK);
	}

	@POST
	@Path("refresh-token")
	public RawToken refreshToken(@Context HttpServletRequest request)
	{
		//todo there is a standard for how to add the token to the header, use it
		final RawToken rawToken = new RawToken(request.getHeader("x-token-jwt"));
		return authenticationManager.refreshToken(rawToken)
				.orElseThrow(BadRequestException::new);
	}
}
