package com.richodemus.dropwizard.jwt.helpers.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserResponse
{
	private final Result result;
	@JsonCreator
	public CreateUserResponse(@JsonProperty("result") Result result)
	{
		this.result = result;
	}

	public CreateUserResponse()
	{
		result = Result.OK;
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
