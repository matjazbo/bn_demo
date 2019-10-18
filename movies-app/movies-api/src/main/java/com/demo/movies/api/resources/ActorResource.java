package com.demo.movies.api.resources;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.data.model.Actor;
import com.demo.movies.api.cache.RequestClientCachingBuilder;
import com.demo.movies.service.ActorService;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;

@RequestScoped
@Path("/actors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log(LogParams.METRICS)
public class ActorResource {

	private static final Logger logger = LogManager.getLogger(ActorResource.class);
	
	@Inject
	private ActorService actorService;
	
	@Inject
	private RequestClientCachingBuilder cacheBuilder;	
	
	@GET
	public Response getActors(@Context Request request) {
		List<Actor> actors = actorService.getAllActors();
		return cacheBuilder.addCaching(request, actors).build();
	}

	@POST
	public Response addNewActor(Actor newActor) {
		actorService.newActor(newActor);
		return Response.ok().entity(newActor).build();
	}

	@PUT
	public Response updateActor(@Valid Actor actor) {		
		actorService.updateActor(actor);
		return Response.noContent().build();
	}
	
	@DELETE
	@Path("{id}")
	public Response deleteActor(@PathParam("id") Long actorId) {
		actorService.deleteActor(actorId);
		return Response.noContent().build();
	}
	
}
