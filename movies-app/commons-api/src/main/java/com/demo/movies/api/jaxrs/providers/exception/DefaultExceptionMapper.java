package com.demo.movies.api.jaxrs.providers.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

@Provider
public class DefaultExceptionMapper extends AbstractExceptionMapper<Exception> {

	@Override
	public Response toResponse(Exception exception) {
		ErrorPresenter ep = new ErrorPresenter(exception.getMessage(), Response.Status.INTERNAL_SERVER_ERROR);
		return presentError(ep).build();
	}

}
