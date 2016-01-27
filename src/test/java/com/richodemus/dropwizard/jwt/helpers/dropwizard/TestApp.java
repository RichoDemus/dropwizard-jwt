package com.richodemus.dropwizard.jwt.helpers.dropwizard;

import com.richodemus.dropwizard.jwt.AuthenticationManager;
import com.richodemus.dropwizard.jwt.AuthenticationRequestFilter;
import com.richodemus.dropwizard.jwt.helpers.UserServiceImpl;
import com.richodemus.dropwizard.jwt.helpers.resources.AccessControlledResource;
import com.richodemus.dropwizard.jwt.helpers.resources.UserResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class TestApp extends Application<TestConfiguration>
{
	@Override
	public void run(TestConfiguration configuration, Environment environment) throws Exception
	{
		//todo this is getting messy, need a cleaner way of doing this
		final UserServiceImpl userService = new UserServiceImpl();

		//These three lines are the actual library setup
		final AuthenticationManager authenticationManager = new AuthenticationManager(userService, Duration.of(1, ChronoUnit.HOURS));
		environment.jersey().register(new AuthenticationRequestFilter(authenticationManager));
		environment.jersey().register(RolesAllowedDynamicFeature.class);

		//This is not really library related
		environment.jersey().register(new UserResource(authenticationManager, userService));
		environment.jersey().register(AccessControlledResource.class);
	}
}
