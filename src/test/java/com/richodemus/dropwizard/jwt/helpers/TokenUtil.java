package com.richodemus.dropwizard.jwt.helpers;

import com.richodemus.dropwizard.jwt.Token;

public class TokenUtil
{
	/**
	 * Normal token except the secret is the_wrong_secret and it has no expiration date
	 */
	public static final Token JWT_TOKEN_WITH_THE_WRONG_SECRET = new Token("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoidXNlcm5hbWUiLCJyb2xlIjoiYWRtaW4ifQ.FR8YuIxjfqE5_s_6cHYGlRl2CE7lnt7s8WSQSbeMlC4");

	/**
	 * Normal token except it has no expiration date, the secret is secret_used_for_testing
	 */
	public static final Token VALID_JWT_TOKEN = new Token("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjoidXNlcm5hbWUiLCJyb2xlIjoiYWRtaW4ifQ.4A96ZeAOjDVeDjgJ5jPBYz9uZkPKymXCEalU-fy1naI");

	/**
	 * Normal token exect it has expired (unless your system clock is bonkers, the secret is secret_used_for_testing
	 */
	public static final Token VALID_BUT_EXPIRED_JWT_TOKEN = new Token("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0MjE0MTEzMjUsInVzZXIiOiJ1c2VybmFtZSIsInJvbGUiOiJhZG1pbiJ9.1hoz_bQB5dh4axtStwXLPR6UDLU_dzxsrzcVJuEQ05c");
}
