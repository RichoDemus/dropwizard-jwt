package com.richodemus.dropwizard.jwt.helpers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class LogoutResponse
{
	private final Result result;

	@JsonCreator
	public LogoutResponse(@JsonProperty("result") Result result)
	{
		this.result = result;
	}

	public Result getResult()
	{
		return result;
	}

	public enum Result
	{
		OK
	}
}
