package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.helpers.dropwizard.TestApp;
import com.richodemus.dropwizard.jwt.helpers.dropwizard.TestConfiguration;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserRequest;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.model.LoginRequest;
import com.richodemus.dropwizard.jwt.helpers.model.LogoutResponse;
import com.richodemus.dropwizard.jwt.model.Role;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class IntegrationTest
{
	private static final String EXISTING_USER = "existing_user";
	private static final String EXISTING_USER_PASSWORD = "existing_user_password";
	private static final Role EXISTING_USER_ROLE = new Role("user");

	private static final String NON_EXISTING_USER = "non_existing_user";
	private static final String NON_EXISTING_USER_PASSWORD = "non_existing_user_password";


	public DropwizardTestSupport<TestConfiguration> target;

	@Before
	public void setUp() throws Exception
	{
		target = new DropwizardTestSupport<>(TestApp.class, ResourceHelpers.resourceFilePath("conf.yaml"));
		target.before();
		final CreateUserResponse result = createUser(EXISTING_USER_ROLE.stringValue(), EXISTING_USER, EXISTING_USER_PASSWORD);
		assertThat(result.getResult()).isEqualTo(CreateUserResponse.Result.OK);
	}

	@After
	public void tearDown() throws Exception
	{
		target.after();
	}

	@Test
	public void shouldCreateUser() throws Exception
	{
		final String expectedRole = "user";

		final CreateUserResponse result = createUser(expectedRole, NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD);
		assertThat(result.getResult()).isEqualTo(CreateUserResponse.Result.OK);

		final Token result2 = login(NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD);
		assertThat(result2.getUsername()).isEqualTo(NON_EXISTING_USER);
		assertThat(result2.getRole()).isEqualTo(expectedRole);

	}

	@Test(expected = ForbiddenException.class)
	public void shouldThrowForbiddenExceptionWhenUserCredentialsAreWrong() throws Exception
	{
		login(NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowBadRequestExceptionWhenRefreshingUsingAnInvalidToken() throws Exception
	{
		refreshToken(new Token("invalidino tokenirino cappuchino"));

	}

	@Test
	public void shouldReturnValidTokenOnLogin() throws Exception
	{
		final Token firstToken = login(EXISTING_USER, EXISTING_USER_PASSWORD);

		//if refresh doesn't throw an exception, the first token was valid
		refreshToken(firstToken);
	}

	@Test
	public void shouldReturnNewTokenWhenRefreshing() throws Exception
	{
		final Token firstToken = login(EXISTING_USER, EXISTING_USER_PASSWORD);

		//There is no random element to tokens, so we need a new expiration or the new one will be identical
		Thread.sleep(1100L);

		final Token newToken = refreshToken(firstToken);

		assertThat(firstToken.getRaw()).isNotEqualTo(newToken.getRaw());
	}

	@Test
	public void shouldBlackListOldTokenWhenReturningANewWhenRefreshing() throws Exception
	{
		final Token firstToken = login(EXISTING_USER, EXISTING_USER_PASSWORD);

		//There is no random element to tokens, so we need a new expiration or the new one will be identical
		Thread.sleep(1100L);

		refreshToken(firstToken);
		try
		{
			refreshToken(firstToken);
			fail("Should've thrown BadRequestException");
		}
		catch (BadRequestException ignored)
		{
		}
	}

	/*
		I know this is ugly, dont kill me
		so, we need to refresh quick enough so that we get the same token
		this is possible since there is no randomness to tokens, so if we create one in the same milisecond
		the expiration will be the same and the tokens will be identical
	 */
	@Test(timeout = 10000)
	public void shouldNotBlacklistOldTokenWhenReturningSameTokenWhenRefreshing() throws Exception
	{
		Token firstToken;
		Token sameToken;
		do
		{
			firstToken = login(EXISTING_USER, EXISTING_USER_PASSWORD);
			sameToken = refreshToken(firstToken);
		}
		while (!firstToken.equals(sameToken));

		Thread.sleep(1100L);

		final Token newToken = refreshToken(firstToken);

		assertThat(firstToken.getRaw()).isNotEqualTo(newToken.getRaw());
	}

	@Test
	public void shouldBlacklistTokenWhenLoggingOut() throws Exception
	{
		final Token token = login(EXISTING_USER, EXISTING_USER_PASSWORD);
		final LogoutResponse response = logout(token);
		assertThat(response.getResult()).isEqualTo(LogoutResponse.Result.OK);
		try
		{
			refreshToken(token);
			fail("Should've thrown BadRequestException");
		}
		catch (BadRequestException ignored)
		{
		}
	}

	private Token login(String existingUser, String existingUserPassword)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/login")
				.request()
				.post(Entity.json(new LoginRequest(existingUser, existingUserPassword)), Token.class);
	}

	private CreateUserResponse createUser(String expectedRole, String nonExistingUser, String nonExistingUserPassword)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/new")
				.request()
				.post(Entity.json(new CreateUserRequest(nonExistingUser, nonExistingUserPassword, expectedRole)), CreateUserResponse.class);
	}

	private Token refreshToken(Token token)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/refresh-token")
				.request()
				.header("x-token-jwt", token.getRaw())
				.post(Entity.json(null), Token.class);
	}

	private LogoutResponse logout(Token token)
	{
		return ClientBuilder.newClient()
				.target("http://localhost:" + target.getLocalPort())
				.path("api/users/logout")
				.request()
				.header("x-token-jwt", token.getRaw())
				.post(Entity.json(null), LogoutResponse.class);
	}
}
