package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.UserService;
import com.richodemus.dropwizard.jwt.model.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserServiceImpl implements UserService
{
	private final Map<String, User> users;

	public UserServiceImpl()
	{
		users = new HashMap<>();
	}

	@Override
	public Optional<Role> login(String username, String password)
	{
		return Optional.ofNullable(users.get(username))
				.filter(u -> u.password.equals(password))
				.map(u -> u.role);
	}

	public void createUser(String username, String password, Role role)
	{
		users.put(username, new User(username, password, role));
	}

	private class User
	{
		final String username;
		final String password;
		final Role role;

		private User(String username, String password, Role role)
		{
			this.username = username;
			this.password = password;
			this.role = role;
		}
	}
}
