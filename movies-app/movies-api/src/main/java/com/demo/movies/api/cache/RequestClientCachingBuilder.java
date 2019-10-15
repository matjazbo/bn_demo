package com.demo.movies.api.cache;

import java.util.Objects;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

/**
 * Builds response with client caching for the given request
 * 
 * @author Matjaz
 *
 */
@RequestScoped
public class RequestClientCachingBuilder {

	/**
	 * Wrapper to addCaching() that defaults to 
	 * calculating cacheId from object's hashCode
	 */
	public ResponseBuilder addCaching(Request request, Object entity) {
		Objects.nonNull(entity);
		return addCaching(request, entity, Integer.toString(entity.hashCode()));
	}
	
	/**
	 * Evaluates if a content identified by cacheId has changed (according to the client).
	 * If it has changed, response is built for the content with status OK.
	 * If it hasn't changed the 304 status code response is build and returned
	 * 
	 * @param request jaxrs request object
	 * @param entity content entity that is to be responded
	 * @param cacheId content identifier
	 * @return ResponseBuilder with correct status code and entity if applicable 
	 */
	public ResponseBuilder addCaching(Request request, Object entity, String cacheId) {
		EntityTag etag = new EntityTag(cacheId);
	    ResponseBuilder builder = request.evaluatePreconditions(etag);		
		
	    // cached resource did change -> serve updated content
	    if(builder == null){
	        builder = Response.ok(entity);
	        builder.tag(etag);
	    }
		
		return builder;
	}

}
