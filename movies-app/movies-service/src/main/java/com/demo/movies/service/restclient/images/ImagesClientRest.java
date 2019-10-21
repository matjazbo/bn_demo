package com.demo.movies.service.restclient.images;

import java.net.ConnectException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.data.model.Image;
import com.demo.movies.configuration.MoviesDataConfiguration;

/**
 * Using JAX RS client to invoke another service, because KumuluzEE did not
 * deserialize response correctly. Instead of creating List of Image-s it
 * created List of LinkedHashMap-s.
 */
@RequestScoped
public class ImagesClientRest implements ImagesClient {

	private static final Logger logger = LogManager.getLogger(ImagesClientRest.class);

	// JAX-RS client
	private Client client;
	private WebTarget imagesWebTarget;

	@Inject
	MoviesDataConfiguration configuration;

	@PostConstruct
	public void init() {
		// initialize JAX-RS client
		if (client == null || imagesWebTarget == null) {
			client = ClientBuilder.newClient();
			imagesWebTarget = client.target(configuration.getServiceImagesUrl());
		}
	}

	@Override
	public List<Image> getImagesForMovieId(String movieId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Image> getImagesForMoviesIds(String movieIdsCsv) {
		String path = "movies/" + movieIdsCsv;
		WebTarget getImageIdPath = imagesWebTarget.path(path);
		logger.debug("Invoking service: {}", path);
		Invocation.Builder invocationBuilder = getImageIdPath.request(MediaType.APPLICATION_JSON);

		List<Image> response = invocationBuilder.get(new GenericType<List<Image>>() {
		});
		return response;
	}

}
