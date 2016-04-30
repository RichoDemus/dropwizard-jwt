package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.helpers.TokenUtil;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenParserTest
{
	@Test
	public void shouldParseUsername() throws Exception
	{
		final Token result = new TokenParser(Secret.SECRET, TokenUtil.VALID_ADMIN_JWT_TOKEN).parse();
		assertThat(result.getUsername()).isEqualTo("username");
	}

	@Test
	public void shouldParseRole() throws Exception
	{
		final Token result = new TokenParser(Secret.SECRET, TokenUtil.VALID_ADMIN_JWT_TOKEN).parse();
		assertThat(result.getRole()).isEqualTo("admin");
	}
}