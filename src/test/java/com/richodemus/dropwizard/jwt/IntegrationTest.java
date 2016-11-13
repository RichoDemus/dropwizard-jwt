package com.richodemus.dropwizard.jwt;

import com.richodemus.dropwizard.jwt.helpers.AccessControlledPage;
import com.richodemus.dropwizard.jwt.helpers.LoginPage;
import com.richodemus.dropwizard.jwt.helpers.TokenUtil;
import com.richodemus.dropwizard.jwt.helpers.dropwizard.TestApp;
import com.richodemus.dropwizard.jwt.helpers.dropwizard.TestConfiguration;
import com.richodemus.dropwizard.jwt.helpers.model.CreateUserResponse;
import com.richodemus.dropwizard.jwt.helpers.resources.AccessControlledResource;
import com.richodemus.dropwizard.jwt.model.Role;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class IntegrationTest
{
	private static final String EXISTING_USER = "existing_user";
	private static final String EXISTING_USER_PASSWORD = "existing_user_password";
	private static final Role EXISTING_USER_ROLE = new Role("user");

	private static final String NON_EXISTING_USER = "non_existing_user";
	private static final String NON_EXISTING_USER_PASSWORD = "non_existing_user_password";


	private DropwizardTestSupport<TestConfiguration> target;
	private LoginPage loginPage;
	private AccessControlledPage controlledPage;

	@Before
	public void setUp() throws Exception
	{
		target = new DropwizardTestSupport<>(TestApp.class, ResourceHelpers.resourceFilePath("conf.yaml"));
		target.before();
		loginPage = new LoginPage(target.getLocalPort());
		controlledPage = new AccessControlledPage(target.getLocalPort());
		final CreateUserResponse result = loginPage.createUser(EXISTING_USER_ROLE.stringValue(), EXISTING_USER, EXISTING_USER_PASSWORD);
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

		final CreateUserResponse result = loginPage.createUser(expectedRole, NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD);
		assertThat(result.getResult()).isEqualTo(CreateUserResponse.Result.OK);

		final RawToken result2 = loginPage.login(NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD);
		final Token parsedResult = new TokenParser(Secret.SECRET, result2).parse();
		assertThat(parsedResult.getUsername()).isEqualTo(NON_EXISTING_USER);
		assertThat(parsedResult.getRole()).isEqualTo(expectedRole);
	}

	@Test(expected = ForbiddenException.class)
	public void shouldThrowForbiddenExceptionWhenUserCredentialsAreWrong() throws Exception
	{
		loginPage.login(NON_EXISTING_USER, NON_EXISTING_USER_PASSWORD);
	}

	@Test(expected = BadRequestException.class)
	public void shouldThrowBadRequestExceptionWhenRefreshingUsingAnInvalidToken() throws Exception
	{
		loginPage.refreshToken(new RawToken("invalidino tokenirino cappuchino"));

	}

	@Test
	public void shouldReturnValidTokenOnLogin() throws Exception
	{
		final RawToken firstToken = loginPage.login(EXISTING_USER, EXISTING_USER_PASSWORD);

		//if refresh doesn't throw an exception, the first token was valid
		loginPage.refreshToken(firstToken);
	}

	@Test
	public void shouldReturnNewTokenWhenRefreshing() throws Exception
	{
		final RawToken firstToken = loginPage.login(EXISTING_USER, EXISTING_USER_PASSWORD);

		//There is no random element to tokens, so we need a new expiration or the new one will be identical
		Thread.sleep(1100L);

		final RawToken newToken = loginPage.refreshToken(firstToken);

		assertThat(firstToken.stringValue()).isNotEqualTo(newToken.stringValue());
	}

	@Test
	public void shouldBeAbleToAccessTheAnyoneMethodWithoutLogginIn() throws Exception
	{
		final AccessControlledResource.Response result = controlledPage.anyone(Optional.empty());
		assertThat(result).isEqualTo(AccessControlledResource.Response.ALLOWED_FOR_EVERYONE);
	}

	@Test
	public void shouldBeAbleToAccessTheAnyoneMethodWhenLoggedInAsUser() throws Exception
	{
		final AccessControlledResource.Response result = controlledPage.anyone(Optional.of(TokenUtil.VALID_USER_JWT_TOKEN));
		assertThat(result).isEqualTo(AccessControlledResource.Response.ALLOWED_FOR_EVERYONE);
	}

	@Test
	public void shouldBeAbleToAccessTheAnyoneMethodWhenLoggedInAsAdmin() throws Exception
	{
		final AccessControlledResource.Response result = controlledPage.anyone(Optional.of(TokenUtil.VALID_ADMIN_JWT_TOKEN));
		assertThat(result).isEqualTo(AccessControlledResource.Response.ALLOWED_FOR_EVERYONE);
	}

	@Test
	public void shouldBeAbleToAccessTheLoggedInMethodWhenLoggedInAsUser() throws Exception
	{
		final AccessControlledResource.Response result = controlledPage.loggedIn(Optional.of(TokenUtil.VALID_USER_JWT_TOKEN));
		assertThat(result).isEqualTo(AccessControlledResource.Response.ALLOWED_FOR_LOGGED_IN_USERS);
	}

	@Test
	public void shouldBeAbleToAccessTheLoggedInMethodWhenLoggedInAsAdmin() throws Exception
	{
		final AccessControlledResource.Response result = controlledPage.loggedIn(Optional.of(TokenUtil.VALID_ADMIN_JWT_TOKEN));
		assertThat(result).isEqualTo(AccessControlledResource.Response.ALLOWED_FOR_LOGGED_IN_USERS);
	}

	@Test(expected = ForbiddenException.class)
	public void shouldNotBeAbleToAccessTheLoggedInMethodWhenNotLoggedIn() throws Exception
	{
		controlledPage.loggedIn(Optional.empty());
	}

	@Test
	public void shouldBeAbleToAccessTheUserAndAdminMethodWhenLoggedInAsUser() throws Exception
	{
		final AccessControlledResource.Response result = controlledPage.userAndAdmin(Optional.of(TokenUtil.VALID_USER_JWT_TOKEN));
		assertThat(result).isEqualTo(AccessControlledResource.Response.ALLOWED_FOR_ADMINS_AND_USERS);
	}

	@Test
	public void shouldBeAbleToAccessTheUserAndAdminMethodWhenLoggedInAsAdmin() throws Exception
	{
		final AccessControlledResource.Response result = controlledPage.userAndAdmin(Optional.of(TokenUtil.VALID_ADMIN_JWT_TOKEN));
		assertThat(result).isEqualTo(AccessControlledResource.Response.ALLOWED_FOR_ADMINS_AND_USERS);
	}

	@Test(expected = ForbiddenException.class)
	public void shouldNotBeAbleToAccessTheUserAndAdminMethodWhenNotLoggedIn() throws Exception
	{
		controlledPage.userAndAdmin(Optional.empty());
	}

	@Test
	public void shouldBeAbleToAccessTheAdminMethodWhenLoggedInAsAdmin() throws Exception
	{
		final AccessControlledResource.Response result = controlledPage.admins(Optional.of(TokenUtil.VALID_ADMIN_JWT_TOKEN));
		assertThat(result).isEqualTo(AccessControlledResource.Response.ADMINS_ONLY);
	}

	@Test(expected = ForbiddenException.class)
	public void shouldNotBeAbleToAccessTheAdminMethodWhenNotLoggedIn() throws Exception
	{
		controlledPage.admins(Optional.empty());
	}

	@Test(expected = ForbiddenException.class)
	public void shouldNotBeAbleToAccessTheAdminMethodWhenLoggedInAsUser() throws Exception
	{
		controlledPage.admins(Optional.of(TokenUtil.VALID_USER_JWT_TOKEN));
	}
}
