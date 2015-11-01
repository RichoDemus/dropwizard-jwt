package com.richodemus.dropwizard.jwt.model;

public class Role
{
	final String role;

	public Role(String role)
	{
		this.role = role;
	}

	public String stringValue()
	{
		return role;
	}
}
