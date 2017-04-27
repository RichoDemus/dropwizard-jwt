package com.richodemus.dropwizard.jwt;

import java.util.Optional;

public interface UserService
{
	Optional<TokenCreationCommand> login(String username, String password);
}
