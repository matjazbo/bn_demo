package com.demo.images.service.restclient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.demo.data.model.Movie;

public interface MoviesClient {

	@GET
	@Path("{movieId}")
	@Produces(MediaType.APPLICATION_JSON)
	Movie getMovie(@PathParam("movieId") String movieId);

}
