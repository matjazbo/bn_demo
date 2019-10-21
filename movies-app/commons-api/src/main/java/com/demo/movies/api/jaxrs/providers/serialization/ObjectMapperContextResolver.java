package com.demo.movies.api.jaxrs.providers.serialization;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

	final ObjectMapper defaultMapper = new ObjectMapper();

	public ObjectMapperContextResolver() {
		defaultMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // disable serialization to
																						// numeric timestamp

		// set format for serializing date(time)s
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		defaultMapper.setDateFormat(dateFormat);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return defaultMapper;
	}
}
