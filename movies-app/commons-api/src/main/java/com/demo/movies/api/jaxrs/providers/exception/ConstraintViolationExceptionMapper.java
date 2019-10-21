package com.demo.movies.api.jaxrs.providers.exception;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper extends AbstractExceptionMapper<ConstraintViolationException> {

	public Response toResponse(ConstraintViolationException exception) {
		return unwrapExceptionResponseBuilder(exception).status(Response.Status.BAD_REQUEST).build();
	}

}
