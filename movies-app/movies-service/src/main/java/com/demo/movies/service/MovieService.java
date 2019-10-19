package com.demo.movies.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;
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
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.RestClientDefinitionException;

import com.demo.data.model.Image;
import com.demo.data.model.Movie;
import com.demo.movies.configuration.MoviesDataConfiguration;
import com.demo.movies.restclient.images.ImagesClient;

/**
 * Business logic provider class.
 * 
 * @author Matjaz
 *
 */
@RequestScoped
public class MovieService {

	private static final Logger logger = LogManager.getLogger(MovieService.class);
	
	@PersistenceContext
	private EntityManager em;

	@Inject
	MoviesDataConfiguration configuration;
	
	private Client client;
	private WebTarget imagesWebTarget;	
	private ImagesClient imagesClient;
	
	@PostConstruct
	public void init() {
		client = ClientBuilder.newClient();
		imagesWebTarget = client.target(configuration.getServiceImagesUrl());
		
		if (imagesClient==null) {
			try {
				imagesClient = RestClientBuilder
					    .newBuilder()
					    .baseUrl(new URL(configuration.getServiceImagesUrl()))
					    .build(ImagesClient.class);
			} catch (IllegalStateException | RestClientDefinitionException | MalformedURLException e) {
				logger.error("Error initializing images rest client.", e);
			}
		}
	}

	public List<Movie> getAllMovies() {
		TypedQuery<Movie> query = em.createNamedQuery("Movie.fetchAll", Movie.class);
		List<Movie> result = query.getResultList();
		Set<String> moviesIds = result.stream().map(m -> m.getId()).collect(Collectors.toSet());
		Map<String, List<Image>> images = getImagesForMoviesIds(moviesIds);
		result.stream().forEach(m -> {
			m.setImages(images.get(m.getId()));
		});
		return result;
	}

	@Transactional
	public void newMovie(Movie movie) {
		em.persist(movie);
	}

	@Transactional
	public void updateMovie(@Valid Movie movie) {
		Movie movieInDb = em.getReference(Movie.class, movie.getId());
		movie.setActors(movieInDb.getActors());
		em.merge(movie);
	}
	
	@Transactional
	public void deleteMovie(String movieId) {
		if (movieId==null || movieId.isBlank()) {
			throw new IllegalArgumentException("Movie id missing");
		}
		
		Query query = em.createNamedQuery("Movie.deleteOne");
		query.setParameter(1, movieId);
		query.executeUpdate();
	}
	
	protected List<Image> getImagesForMovieId(String movieId) {
		if (movieId==null || imagesClient==null) return new ArrayList<>();

		try {
			List<Image> response = imagesClient.getImagesForMovieId(movieId);
			return response;
		} catch (ProcessingException e) {
			logger.error("Error calling getImagesForMovieId from the images service. Returning empty list.", e);
			return new ArrayList<>();
		} catch (Exception e) {
			logger.error("Unknown error calling getImagesForMovieId from the images service. Returning empty list.", e);
			return new ArrayList<>();
		}
		
	}
	
	/**
	 * Returns images for multiple movies at once.
	 * @param movieIds set of movie ids for which to fetch images
	 * @return a map of movieId -> list of Images
	 */
	protected Map<String, List<Image>> getImagesForMoviesIds(Set<String> movieIds) {
		if (movieIds==null) return new HashMap<>();
		String path = "movies/"+StringUtils.join(movieIds, ',');
		WebTarget getImageIdPath = imagesWebTarget.path(path);
		logger.debug("Invoking service: {}", path);
		Invocation.Builder invocationBuilder = getImageIdPath.request(MediaType.APPLICATION_JSON);
		List<Image> response = invocationBuilder.get(new GenericType<List<Image>> () {});
		
		Map<String, List<Image>> ret = new HashMap<>();
		response.stream().forEach(image -> {
			String movieId = image.getMovieId();
			if (!ret.containsKey(movieId)) ret.put(movieId, new LinkedList<>());
			ret.get(movieId).add(image);
		});
		
		return ret;
	}
	

	public Optional<Movie> getMovie(String movieId) {
		return getMovie(movieId, false);
	}
	
	/**
	 * Fetch a Movie from database.
	 * @param movieId id of the movie
	 * @param withImages if true it will fetch movie images from the images service
	 * @return
	 */
	public Optional<Movie> getMovie(String movieId, boolean withImages) {
		if (movieId==null) return Optional.empty();
		TypedQuery<Movie> query = em.createNamedQuery("Movie.fetchOne", Movie.class);
		query.setParameter(1, movieId);
		Movie movie = query.getSingleResult();
		if (withImages) {
			movie.setImages(getImagesForMovieId(movieId));
		}
		return Optional.ofNullable(movie);
	}

}
