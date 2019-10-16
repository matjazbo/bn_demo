package com.demo.images.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.demo.movies.data.model.Image;
import com.demo.movies.data.model.validation.ImdbId;

@RequestScoped
public class ImageService {

	@PersistenceContext
	private EntityManager em;	
	
	public List<Image> getImagesForMovieId(@ImdbId String movieId) {
		TypedQuery<Image> query = em.createQuery("SELECT * FROM image i WHERE i.movie_id = ?1", Image.class);
		query.setParameter(1, movieId);
		List<Image> result = query.getResultList();
		return result;
	}
	
	@Transactional
	public void newImage(Image image) {
		em.persist(image);
	}	
	
}
