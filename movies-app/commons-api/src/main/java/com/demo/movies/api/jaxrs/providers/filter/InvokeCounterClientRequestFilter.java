package com.demo.movies.api.jaxrs.providers.filter;


import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import com.demo.movies.api.annotation.Asynchronous;
import com.demo.movies.api.configuration.CounterConfiguration;
import com.demo.movies.api.counter.executor.CounterExecutor;
import com.demo.movies.api.counter.factory.CounterFactory;
import com.demo.movies.api.counter.service.CounterService;

/**
 * Inspects the request and identifies the current REST method that is being called.
 * Then increases the counter which represents how many times a ceratin REST method
 * was invoked.
 * 
 * @author Matjaz
 *
 */
@Provider
public class InvokeCounterClientRequestFilter implements ContainerRequestFilter  {

	@Inject @Asynchronous
	private CounterExecutor counterExecutor;
	@Inject
	private CounterFactory counterFactory;
	@Inject
	private CounterConfiguration counterConfiguration;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Objects.nonNull(requestContext);
		
		UriInfo uriInfo = requestContext.getUriInfo();

		String pathId = "";	// string constructed from path segments that are not parameters, used to identify counter
		
		// scan path segments and use only those segments (for constructing counterId) that are not parameters
		Collection<List<String>> paramValues = uriInfo.getPathParameters().values();
		for (PathSegment pathSegment : uriInfo.getPathSegments()) {
			String path = pathSegment.getPath();
			boolean pathSegementIsParameter = paramValues.stream().anyMatch(list -> {
				return list.contains(path);
			});
			if (!pathSegementIsParameter) pathId += "_" + path; 
		}
		
		String method = requestContext.getMethod();
		if (method==null) method = "NOMETHOD";
		//counterService.increaseCounter(method + "_" + pathId);
		String counterId = String.format("%s_%s", method, pathId);
		CounterService counterService = CounterService.getService(counterExecutor, counterFactory, counterConfiguration);
		counterService.increaseCounter(counterId);
	}

}
