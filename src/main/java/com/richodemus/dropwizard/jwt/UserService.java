package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.model.Role;

public interface UserService
{
	Role login(String username, String password);
}
