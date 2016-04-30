package com.richodemus.dropwizard.jwt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RawToken
{
	private final String raw;

	@JsonCreator
	public RawToken(@JsonProperty("raw") final String raw)
	{
		this.raw = raw;
	}

	@JsonProperty("raw")
	public String stringValue()
	{
		return raw;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		RawToken rawToken = (RawToken) o;
		return Objects.equals(raw, rawToken.raw);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(raw);
	}
}
