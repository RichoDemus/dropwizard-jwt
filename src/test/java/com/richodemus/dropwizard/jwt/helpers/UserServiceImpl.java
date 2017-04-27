package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.TokenCreationCommand;
import com.richodemus.dropwizard.jwt.UserService;

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
	public Optional<TokenCreationCommand> login(String username, String password)
	{
		return Optional.ofNullable(users.get(username))
				.filter(u -> u.password.equals(password))
				.map(u -> u.tokenCreationCommand);
	}

	public void createUser(String username, String password, TokenCreationCommand tokenCreationCommand)
	{
		users.put(username, new User(username, password, tokenCreationCommand));
	}

	private class User
	{
		final String username;
		final String password;
		final TokenCreationCommand tokenCreationCommand;

		private User(String username, String password, TokenCreationCommand tokenCreationCommand)
		{
			this.username = username;
			this.password = password;
			this.tokenCreationCommand = tokenCreationCommand;
		}
	}
}
