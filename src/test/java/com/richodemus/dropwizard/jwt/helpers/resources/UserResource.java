package com.richodemus.dropwizard.jwt.helpers.resources;

import com.richodemus.dropwizard.jwt.AuthenticationManager;
import com.richodemus.dropwizard.jwt.Token;
import com.richodemus.dropwizard.jwt.helpers.UserServiceImpl;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserRequest;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;
import com.richodemus.dropwizard.jwt.helpers.model.LogoutResponse;
import com.richodemus.dropwizard.jwt.model.Role;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

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
	public Token login(LoginRequest loginRequest)
	{
		return authenticationManager.login(loginRequest.getUsername(), loginRequest.getPassword())
				.orElseThrow(ForbiddenException::new);
	}

	@POST
	@Path("/new")
	public CreateUserResponse createUser(CreateUserRequest createUserRequest)
	{
		userService.createUser(createUserRequest.getUsername(), createUserRequest.getPassword(), new Role(createUserRequest.getRole()));
		return new CreateUserResponse(CreateUserResponse.Result.OK);
	}

	@POST
	@Path("refresh-token")
	public Token refreshToken(@Context HttpServletRequest request)
	{
		//todo there is a standard for how to add the token to the header, use it
		final String rawToken = request.getHeader("x-token-jwt");
		return authenticationManager.refreshToken(new Token(rawToken));
	}

	@POST
	@Path("logout")
	public LogoutResponse logout(@Context HttpServletRequest request)
	{
		final Token token = Optional.ofNullable(request.getHeader("x-token-jwt"))
				.map(Token::new)
				.orElseThrow(ForbiddenException::new);

		authenticationManager.logout(token);
		return new LogoutResponse(LogoutResponse.Result.OK);
	}
}
