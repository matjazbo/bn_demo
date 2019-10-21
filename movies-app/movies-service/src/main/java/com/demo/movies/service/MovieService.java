package com.demo.movies.service;

import java.net.ConnectException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.data.model.Actor;
import com.demo.data.model.Image;
import com.demo.data.model.Movie;
import com.demo.movies.service.restclient.images.ImagesClient;
import com.demo.movies.service.restclient.images.ImagesClientRest;
import com.demo.movies.service.util.JPAUtilsCaller;
import com.kumuluz.ee.rest.beans.QueryFilter;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.enums.FilterOperation;
import com.kumuluz.ee.rest.utils.QueryStringDefaults;

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
	ImagesClientRest imagesClientRest;

	@Inject
	JPAUtilsCaller jpaUtils;

	private static QueryStringDefaults qsd = new QueryStringDefaults()
			.maxLimit(200)
			.defaultLimit(10)
			.defaultOffset(0);
	
	@Context
	protected UriInfo uriInfo;

	// KumuluzEE client
	private ImagesClient imagesClient;

	public List<Movie> getAllMovies() {
		return getAllMovies(Optional.empty());
	}

	/**
	 * Searches for movies where field (findByField) value contains the search term
	 * 
	 * @param findByField field by which to search
	 * @param searchTerm  search term
	 * @return list of Movie-s that match the query
	 */
	public List<Movie> getAllMovies(String findByField, String searchTerm) {
		QueryFilter qf = new QueryFilter();
		qf.setField(findByField);
		qf.setValue(String.format("%%%s%%", searchTerm)); // produces "%searchTerm%"
		qf.setOperation(FilterOperation.LIKE);
		List<QueryFilter> queryFilters = Collections.singletonList(qf);

		return getAllMovies(Optional.of(queryFilters));
	}

	public List<Movie> getAllMovies(Optional<List<QueryFilter>> queryFilters) {
		QueryParameters qParams = qsd.builder()
				.queryEncoded(uriInfo.getRequestUri().getRawQuery())
				.defaultFilters(queryFilters.orElse(Collections.emptyList()))
				.build();
		List<Movie> result = jpaUtils.queryEntities(em, Movie.class, qParams);

		Set<String> moviesIds = result.stream().map(m -> m.getId()).collect(Collectors.toSet());
		Map<String, List<Image>> images = getImagesForMoviesIdsMap(moviesIds);
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
		if (movieId == null || movieId.isBlank()) {
			throw new IllegalArgumentException("Movie id missing");
		}

		Query query = em.createNamedQuery("Movie.deleteOne");
		query.setParameter(1, movieId);
		query.executeUpdate();
	}

	protected List<Image> getImagesForMovieId(String movieId) {
		if (movieId == null || imagesClient == null)
			return Collections.emptyList();

		try {
			List<Image> response = imagesClient.getImagesForMovieId(movieId);
			return response;
		} catch (ProcessingException e) {
			logger.error("Error calling getImagesForMovieId from the images service. Returning empty list.", e);
			return Collections.emptyList();
		} catch (Exception e) {
			logger.error("Unknown error calling getImagesForMovieId from the images service. Returning empty list.", e);
			return Collections.emptyList();
		}

	}

	/**
	 * Returns images for multiple movies at once.
	 * 
	 * @param movieIds set of movie ids for which to fetch images
	 * @return a map of movieId -> list of Images
	 */
	public Map<String, List<Image>> getImagesForMoviesIdsMap(Set<String> movieIds) {
		if (movieIds == null)
			return new HashMap<>();
		String movieIdsCsv = StringUtils.join(movieIds, ',');

		try {
			List<Image> response = imagesClientRest.getImagesForMoviesIds(movieIdsCsv);
			Map<String, List<Image>> ret = new HashMap<>();
			response.stream().forEach(image -> {
				String movieId = image.getMovieId();
				if (!ret.containsKey(movieId))
					ret.put(movieId, new LinkedList<>());
				ret.get(movieId).add(image);
			});

			return ret;
		} catch (ProcessingException e) {
			if (e.getCause() != null && e.getCause().getCause() != null) {
				Throwable cause = e.getCause().getCause();
				if (cause instanceof ConnectException) {
					// cant connect to the other service, return empty result
					logger.debug("Images service not available, returning empty result.");
					return Collections.emptyMap();
				}
			}
			throw e;
		}
	}

	public Optional<Movie> getMovie(String movieId) {
		return getMovie(movieId, false);
	}

	/**
	 * Fetch a Movie from database.
	 * 
	 * @param movieId    id of the movie
	 * @param withImages if true it will fetch movie images from the images service
	 * @return
	 */
	public Optional<Movie> getMovie(String movieId, boolean withImages) {
		if (movieId == null)
			return Optional.empty();
		TypedQuery<Movie> query = em.createNamedQuery("Movie.fetchOne", Movie.class);
		query.setParameter(1, movieId);
		Movie movie = query.getSingleResult();
		if (withImages) {
			movie.setImages(getImagesForMovieId(movieId));
		}
		return Optional.ofNullable(movie);
	}

	@Transactional
	public void addActorToMovie(String movieId, Long actorId) {
		Movie movie = em.getReference(Movie.class, movieId);
		if (movie == null)
			return;
		Actor actor = em.getReference(Actor.class, actorId);
		if (actor == null)
			return;
		movie.getActors().add(actor);
		em.merge(movie);
	}

}
