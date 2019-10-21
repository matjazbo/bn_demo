package com.demo.movies.api.jaxrs.providers.exception;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalServerErrorExceptionMapper extends AbstractExceptionMapper<InternalServerErrorException> {

	@Override
	public Response toResponse(InternalServerErrorException exception) {
		return unwrapExceptionResponseBuilder(exception).build();
	}

}
