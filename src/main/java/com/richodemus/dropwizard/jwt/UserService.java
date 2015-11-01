package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.model.Role;

import java.util.Optional;

public interface UserService
{
	Optional<Role> login(String username, String password);
}
