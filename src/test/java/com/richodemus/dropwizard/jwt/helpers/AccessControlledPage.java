package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.Token;
import com.richodemus.dropwizard.jwt.helpers.resources.AccessControlledResource;

import javax.ws.rs.client.ClientBuilder;

public class AccessControlledPage
{
	private final int port;

	public AccessControlledPage(int port)
	{
		this.port = port;
	}

	public AccessControlledResource.Response anyone()
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/controlled/anyone")
				.request()
				.get(AccessControlledResource.Response.class);
	}

	public AccessControlledResource.Response anyone(Token token)
	{
		return ClientBuilder.newClient()
						.target("http://localhost:" + port)
						.path("api/controlled/anyone")
						.request()
				.header("x-token-jwt", token.getRaw())
				.get(AccessControlledResource.Response.class);
	}

	public AccessControlledResource.Response loggedIn(Token token)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/controlled/logged-in")
				.request()
				.header("x-token-jwt", token.getRaw())
				.get(AccessControlledResource.Response.class);
	}

	public AccessControlledResource.Response userAndAdmin(Token token)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/controlled/admin-and-user")
				.request()
				.header("x-token-jwt", token.getRaw())
				.get(AccessControlledResource.Response.class);
	}

	public AccessControlledResource.Response admins(Token token)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path("api/controlled/admins")
				.request()
				.header("x-token-jwt", token.getRaw())
				.get(AccessControlledResource.Response.class);
	}
}
