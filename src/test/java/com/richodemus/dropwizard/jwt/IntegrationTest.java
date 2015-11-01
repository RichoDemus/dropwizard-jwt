package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.helpers.TestApp;
import com.richodemus.dropwizard.jwt.helpers.TestConfiguration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

public class IntegrationTest
{
	@ClassRule
	public static final DropwizardAppRule<TestConfiguration> RULE =
			new DropwizardAppRule<>(TestApp.class, ResourceHelpers.resourceFilePath("my-app-config.yaml"));

	@Test
	public void loginHandlerRedirectsAfterPost()
	{
		Client client = new JerseyClientBuilder(RULE.getEnvironment()).build("test client");

		Response response = client.target(
				String.format("http://localhost:%d/login", RULE.getLocalPort()))
				.request()
				.post(Entity.json(loginForm()));

		assertThat(response.getStatus()).isEqualTo(302);
	}
}
