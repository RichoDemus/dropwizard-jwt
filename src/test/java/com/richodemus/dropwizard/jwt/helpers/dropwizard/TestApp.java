package com.richodemus.dropwizard.jwt.helpers.dropwizard;

import com.richodemus.dropwizard.jwt.AuthenticationManager;
import com.richodemus.dropwizard.jwt.helpers.UserServiceImpl;
import com.richodemus.dropwizard.jwt.helpers.resources.ConfidentalResource;
import com.richodemus.dropwizard.jwt.helpers.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;

public class TestApp extends Application<TestConfiguration>
{
	@Override
	public void run(TestConfiguration configuration, Environment environment) throws Exception
	{
		final UserServiceImpl userService = new UserServiceImpl();
		final AuthenticationManager authenticationManager = new AuthenticationManager(userService);
		environment.jersey().register(new UserResource(authenticationManager, userService));
		environment.jersey().register(ConfidentalResource.class);

	}
}
