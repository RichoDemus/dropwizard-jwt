package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.AuthenticationManager;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TestApp extends Application<TestConfiguration>
{
	@Override
	public void run(TestConfiguration configuration, Environment environment) throws Exception
	{
		final AuthenticationManager authenticationManager = new AuthenticationManager(new UserServiceImpl());
		environment.jersey().register(new LoginResource(authenticationManager));
		environment.jersey().register(ConfidentalResource.class);

	}
}
