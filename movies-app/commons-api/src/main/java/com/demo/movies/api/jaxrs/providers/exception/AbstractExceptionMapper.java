package com.demo.movies.api.jaxrs.providers.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * Abstract ExceptionMapper providing exception unwrapping utilites
 * and a default ResponseBuilder.
 * 
 * @author Matjaz
 *
 */
public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {

	protected ResponseBuilder unwrapExceptionResponseBuilder(Throwable exception) {
		return Response
				.status(Response.Status.INTERNAL_SERVER_ERROR)
				.entity(unwrapException(exception))
				.type(MediaType.TEXT_PLAIN);
	}
	
	protected ResponseBuilder presentError(ErrorPresenter ep) {
		return Response
				.status(ep.getStatus())
				.entity(ep)
				.type(MediaType.APPLICATION_JSON);
	}
	
	
	protected String unwrapException(Throwable t) {
		StringBuffer sb = new StringBuffer();
		doUnwrapException(sb, t);
		return sb.toString();
	}

	private void doUnwrapException(StringBuffer sb, Throwable t) {
		if (t == null) {
			return;
		}
		sb.append(t.toString());
		if (t.getCause() != null && t != t.getCause()) {
			sb.append('[');
			doUnwrapException(sb, t.getCause());
			sb.append(']');
		}
	} 	
}
