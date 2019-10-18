package com.demo.movies.service;


import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.movies.data.model.Movie;

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

	public List<Movie> getAllMovies() {
		TypedQuery<Movie> query = em.createNamedQuery("Movie.fetchAll", Movie.class);
		List<Movie> result = query.getResultList();
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
		
		Query query = em.createQuery("DELETE Movie m WHERE m.id = ?1");
		query.setParameter(1, movieId);
		query.executeUpdate();
	}

}
