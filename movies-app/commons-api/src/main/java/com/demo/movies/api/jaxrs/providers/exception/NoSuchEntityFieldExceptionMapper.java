package com.demo.movies.api.jaxrs.providers.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import com.kumuluz.ee.rest.exceptions.NoSuchEntityFieldException;

@Provider
public class NoSuchEntityFieldExceptionMapper extends AbstractExceptionMapper<NoSuchEntityFieldException> {

	@Override
	public Response toResponse(NoSuchEntityFieldException exception) {
		String msg = String.format("Field %s does not exist on entity %s", exception.getField(), exception.getEntity());
		ErrorPresenter ep = new ErrorPresenter(msg, Response.Status.BAD_REQUEST);
		return presentError(ep).build();
	}

}
