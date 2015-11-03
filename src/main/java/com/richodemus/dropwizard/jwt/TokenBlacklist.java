package com.richodemus.dropwizard.jwt;

import java.util.HashSet;
import java.util.Set;

//todo regularly check for and remove expired tokens
public class TokenBlacklist
{
	private final Set<Token> blacklist;

	public TokenBlacklist()
	{
		blacklist = new HashSet<>();
	}

	public boolean isBlacklisted(Token token)
	{
		return blacklist.contains(token);
	}

	public void blacklist(Token token)
	{
		blacklist.add(token);
	}
}
