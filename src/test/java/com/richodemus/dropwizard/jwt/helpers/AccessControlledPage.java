package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.RawToken;
import com.richodemus.dropwizard.jwt.helpers.resources.AccessControlledResource;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import java.util.Optional;

public class AccessControlledPage
{
	private final int port;

	public AccessControlledPage(int port)
	{
		this.port = port;
	}

	public AccessControlledResource.Response anyone(Optional<RawToken> token)
	{
		return getClient("api/controlled/anyone", token).get(AccessControlledResource.Response.class);
	}

	public AccessControlledResource.Response loggedIn(Optional<RawToken> token)
	{
		return getClient("api/controlled/logged-in", token).get(AccessControlledResource.Response.class);
	}

	public AccessControlledResource.Response userAndAdmin(Optional<RawToken> token)
	{
		return getClient("api/controlled/admin-and-user", token).get(AccessControlledResource.Response.class);
	}

	public AccessControlledResource.Response admins(Optional<RawToken> token)
	{
		return getClient("api/controlled/admins", token).get(AccessControlledResource.Response.class);
	}

	private Invocation.Builder getClient(String path, Optional<RawToken> maybeToken)
	{
		final Invocation.Builder builder = ClientBuilder.newClient()
				.target("http://localhost:" + port)
				.path(path)
				.request();

		return maybeToken.map(RawToken::stringValue)
				.map(token -> builder.header("x-token-jwt", token))
				.orElse(builder);

	}
}
