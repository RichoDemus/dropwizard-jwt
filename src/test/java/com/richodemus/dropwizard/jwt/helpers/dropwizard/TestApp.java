package com.richodemus.dropwizard.jwt.helpers.dropwizard;

import com.richodemus.dropwizard.jwt.AuthenticationManager;
import com.richodemus.dropwizard.jwt.AuthenticationRequestFilter;
import com.richodemus.dropwizard.jwt.helpers.UserServiceImpl;
import com.richodemus.dropwizard.jwt.helpers.resources.AccessControlledResource;
import com.richodemus.dropwizard.jwt.helpers.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class TestApp extends Application<TestConfiguration>
{
	@Override
	public void run(TestConfiguration configuration, Environment environment) throws Exception
	{
		final UserServiceImpl userService = new UserServiceImpl();
		final AuthenticationManager authenticationManager = new AuthenticationManager(userService);
		environment.jersey().register(new UserResource(authenticationManager, userService));
		environment.jersey().register(AccessControlledResource.class);
		environment.jersey().register(AuthenticationRequestFilter.class);
		environment.jersey().register(RolesAllowedDynamicFeature.class);
	}
}
