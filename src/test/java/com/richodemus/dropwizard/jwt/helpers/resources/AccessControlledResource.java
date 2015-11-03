package com.richodemus.dropwizard.jwt.helpers.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/controlled")
@Produces(MediaType.APPLICATION_JSON)
public class AccessControlledResource
{
	@PermitAll
	@Path("/anyone")
	@GET
	public Response allowedForAnyone()
	{
		return Response.ALLOWED_FOR_EVERYONE;
	}

	@RolesAllowed("any")
	@Path("/logged-in")
	@GET
	public Response allowedForLoggedInUsers()
	{
		return Response.ALLOWED_FOR_LOGGED_IN_USERS;
	}

	@RolesAllowed({"user", "admin"})
	@Path("/admin-and-user")
	@GET
	public Response allowedForAdminsAndUsers()
	{
		return Response.ALLOWED_FOR_ADMINS_AND_USERS;
	}

	@RolesAllowed("admin")
	@Path("/admins")
	@GET
	public Response admimnsOnly()
	{
		return Response.ADMINS_ONLY;
	}

	public enum Response
	{
		ALLOWED_FOR_LOGGED_IN_USERS, ADMINS_ONLY, ALLOWED_FOR_ADMINS_AND_USERS, ALLOWED_FOR_EVERYONE
	}
}
