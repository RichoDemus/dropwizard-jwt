package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.helpers.dropwizard.TestApp;
import com.richodemus.dropwizard.jwt.helpers.dropwizard.TestConfiguration;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserRequest;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;
import com.richodemus.dropwizard.jwt.model.Role;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest
{
	private static final String EXISTING_USER = "existing_user";
	private static final String EXISTING_USER_PASSWORD = "existing_user_password";
	private static final Role EXISTING_USER_ROLE = new Role("user");

	private static final String NON_EXISTING_USER = "non_existing_user";
	private static final String NON_EXISTING_USER_PASSWORD = "non_existing_user_password";


	public DropwizardTestSupport<TestConfiguration> target;

	@Before
	public void setUp() throws Exception
	{
		target = new DropwizardTestSupport<>(TestApp.class, ResourceHelpers.resourceFilePath("conf.yaml"));
		target.before();
		final CreateUserResponse result = ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/new")
				.request()
				.post(Entity.json(new CreateUserRequest(EXISTING_USER, EXISTING_USER_PASSWORD, EXISTING_USER_ROLE.stringValue())), CreateUserResponse.class);
		assertThat(result.getResult()).isEqualTo(CreateUserResponse.Result.OK);
	}

	@After
	public void tearDown() throws Exception
	{
		target.after();
	}

	@Test
	public void shouldCreateUser() throws Exception
	{
		final String expectedRole = "user";
		final CreateUserResponse result = ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/new")
				.request()
				.post(Entity.json(new CreateUserRequest(NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD, expectedRole)), CreateUserResponse.class);
		assertThat(result.getResult()).isEqualTo(CreateUserResponse.Result.OK);

		final Token result2 = ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/login")
				.request()
				.post(Entity.json(new LoginRequest(NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD)), Token.class);

		assertThat(result2.getUsername()).isEqualTo(NON_EXISTING_USER);
		assertThat(result2.getRole()).isEqualTo(expectedRole);

	}

	@Test(expected = ForbiddenException.class)
	public void shouldThrowForbiddenExceptionWhenUserAlreadyExists() throws Exception
	{
		ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/login")
				.request()
				.post(Entity.json(new LoginRequest(NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD)), Token.class);
	}

	@Test
	public void shouldReturnValidTokenOnLogin() throws Exception
	{
		//First login
		final Token firstToken = ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/login")
				.request()
				.post(Entity.json(new LoginRequest(EXISTING_USER, EXISTING_USER_PASSWORD)), Token.class);

		//if refresh doesn't throw an exception, the first token was valid
		ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/refresh-token")
				.request()
				.header("x-token-jwt", firstToken.getRaw())
				.post(Entity.json(null), Token.class);

	}

	@Test
	public void shouldReturnNewTokenWhenRefreshing() throws Exception
	{
		//First login
		final Token firstToken = ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/login")
				.request()
				.post(Entity.json(new LoginRequest(EXISTING_USER, EXISTING_USER_PASSWORD)), Token.class);

		//There is no random element to tokens, so we need a new expiration or the new one will be identical
		Thread.sleep(1100L);

		//refresh the token
		final Token newToken = ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/refresh-token")
				.request()
				.header("x-token-jwt", firstToken.getRaw())
				.post(Entity.json(null), Token.class);

		assertThat(firstToken.getRaw()).isNotEqualTo(newToken.getRaw());
	}
}
