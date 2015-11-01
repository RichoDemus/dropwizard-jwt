package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.helpers.TestApp;
import com.richodemus.dropwizard.jwt.helpers.TestConfiguration;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserRequest;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;
import com.richodemus.dropwizard.jwt.model.Role;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.After;
import org.junit.ClassRule;
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


	@ClassRule
	public static final DropwizardAppRule<TestConfiguration> RULE =
			new DropwizardAppRule<>(TestApp.class, ResourceHelpers.resourceFilePath("conf.yaml"));

	@After
	public void tearDown() throws Exception
	{
		

	}

	@Test
	public void shouldCreateUser() throws Exception
	{
		final CreateUserResponse result = ClientBuilder.newClient()
				.target("http://localhost:" + RULE.getLocalPort())
				.path("api/login/new")
				.request()
				.post(Entity.json(new CreateUserRequest(EXISTING_USER, EXISTING_USER_PASSWORD, EXISTING_USER_ROLE.stringValue())), CreateUserResponse.class);
		assertThat(result.getResult()).isEqualTo(CreateUserResponse.Result.OK);

		final Token result2 = ClientBuilder.newClient()
				.target("http://localhost:" + RULE.getLocalPort())
				.path("api/login")
				.request()
				.post(Entity.json(new LoginRequest(EXISTING_USER, EXISTING_USER_PASSWORD)), Token.class);

		assertThat(result2.getUsername()).isEqualTo(EXISTING_USER);
		assertThat(result2.getRole()).isEqualTo(EXISTING_USER_ROLE.stringValue());

	}

	@Test(expected = ForbiddenException.class)
	public void shouldLoginUser() throws Exception
	{
		ClientBuilder.newClient()
				.target("http://localhost:" + RULE.getLocalPort())
				.path("api/login")
				.request()
				.post(Entity.json(new LoginRequest(EXISTING_USER, EXISTING_USER_PASSWORD)), Token.class);
	}
}
