package com.demo.movies.restclient.images;

import java.util.List;

import javax.enterprise.context.Dependent;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.demo.data.model.Image;

@RegisterRestClient
@Dependent
public interface ImagesClient {

	@GET
	@Path("movie/{movieId}")
	@Produces(MediaType.APPLICATION_JSON)
	List<Image> getImagesForMovieId(@PathParam("movieId") String movieId);
	
	@GET
	@Path("movies/{moviesIds}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Image> getImagesForMoviesIds(@PathParam("moviesIds") String moviesIds);
	
}
