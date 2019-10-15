package com.demo.movies.api.resources;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import com.demo.movies.api.cache.RequestClientCachingBuilder;
import com.demo.movies.data.model.Movie;
import com.demo.movies.service.MovieService;

/**
 * Class providing API for Movies and Actors business logic.
 * 
 * @author Matjaz
 *
 */
@RequestScoped
@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovieResource {

	@Inject
	private MovieService movieService;
	
	@Inject
	private RequestClientCachingBuilder cacheBuilder;	
	
	@GET
	public Response getMovies(@Context Request request) {
		List<Movie> movies = movieService.getAllMovies();
		
		return cacheBuilder.addCaching(request, movies).build();
	}

	@POST
	public Response addNewMovie(@Valid Movie newMovie) {
		movieService.newMovie(newMovie);
		return Response.noContent().build();
	}
	
}
