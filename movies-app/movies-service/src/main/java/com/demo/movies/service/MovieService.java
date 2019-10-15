package com.demo.movies.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.demo.movies.data.model.Movie;

/**
 * Business logic provider class.
 * 
 * @author Matjaz
 *
 */
@RequestScoped
public class MovieService {

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

}
