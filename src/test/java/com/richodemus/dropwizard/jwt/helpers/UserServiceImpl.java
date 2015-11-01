package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.UserService;
import com.richodemus.dropwizard.jwt.model.Role;

public class UserServiceImpl implements UserService
{
	@Override
	public Role login(String username, String password)
	{
		return new Role("user");
	}
}
