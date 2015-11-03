package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.helpers.TokenUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenTest
{
	@Test
	public void shouldParseUsername() throws Exception
	{
		final Token target = TokenUtil.VALID_ADMIN_JWT_TOKEN;
		assertThat(target.getUsername()).isEqualTo("username");
	}

	@Test
	public void shouldParseRole() throws Exception
	{
		final Token target = TokenUtil.VALID_ADMIN_JWT_TOKEN;
		assertThat(target.getRole()).isEqualTo("admin");
	}
}