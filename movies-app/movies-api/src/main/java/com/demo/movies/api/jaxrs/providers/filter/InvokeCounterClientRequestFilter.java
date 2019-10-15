package com.demo.movies.api.jaxrs.providers.filter;


import java.io.IOException;
import java.util.Objects;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import com.demo.movies.api.counter.service.CounterService;

@Provider
public class InvokeCounterClientRequestFilter implements ContainerRequestFilter  {

	@Inject
	CounterService counterService;
	
	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Objects.nonNull(requestContext);
		
		String path = requestContext.getUriInfo().getPath();
		String method = requestContext.getMethod();
		
		if (path!=null) {
			path = path.replaceAll("/", "");
			if (method==null) method = "NOMETHOD";
			counterService.increaseCounter(path + "_" + method);		
		}
	}

}
