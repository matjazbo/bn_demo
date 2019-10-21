package com.demo.images.service.restclient;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.demo.data.model.Movie;
import com.demo.images.configuration.ImagesConfiguration;

@RequestScoped
public class MoviesClientImpl implements MoviesClient {

	@Inject
	private ImagesConfiguration configuration;
	
	private Client client;
	private WebTarget moviesWebTarget;
	
	@PostConstruct
	public void init() {
		client = ClientBuilder.newClient();
		moviesWebTarget = client.target(configuration.getServiceMoviesUrl());
	}
	
	
	@Override
	public Movie getMovie(String movieId) {
		WebTarget getMovieIdPath = moviesWebTarget.path(movieId);
		Invocation.Builder invocationBuilder = getMovieIdPath.request(MediaType.APPLICATION_JSON);		
		Movie response = invocationBuilder.get(Movie.class);
		return response;
	}

}
