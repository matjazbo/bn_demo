package com.demo.images.service;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.apache.commons.io.FileUtils;

import com.demo.data.model.Image;
import com.demo.data.model.Movie;
import com.demo.data.model.validation.ImdbId;
import com.demo.images.configuration.ImagesConfiguration;
import com.demo.images.service.restclient.MoviesClient;

@RequestScoped
public class ImageService {

	@PersistenceContext
	private EntityManager em;
	
	@Inject
	private ImagesConfiguration configuration;

	@Inject
	private MoviesClient moviesClient;
	
	public List<Image> getImagesForMovieId(@ImdbId String movieId) {
		TypedQuery<Image> query = em.createQuery("SELECT i FROM Image i WHERE i.movieId = ?1", Image.class);
		query.setParameter(1, movieId);
		List<Image> result = query.getResultList();
		return result;
	}
	
	private Optional<Movie> getMovie(String movieId) {
		if (movieId==null) return Optional.empty();
		Movie clientResponse = moviesClient.getMovie(movieId);
		return Optional.ofNullable(clientResponse);
	}
	
	@Transactional
	public void newImage(Image image) {
		em.persist(image);
	}	

	/**
	 * Upload the file to the file system, creates an Image entity
	 * for it and binds it to the Movie for the given movie id.
	 * 
	 * @param fileInputStream input strem of the file being uploaded
	 * @param fileName name of the file being uploaded without path
	 * @param movieId id of the movie to which the Image should be attached
	 * @throws IOException exception is thrown if there is a problem writing the file to the filesystem
	 * @return a new Image object that has been persisted
	 */
	@Transactional
	public Image uploadImageFile(InputStream fileInputStream, String fileName, String movieId) throws IOException {
		
		// 1. check if move exists and set movie id
		Image i = new Image();
		Movie movie = getMovie(movieId).orElseThrow();
		i.setMovieId(movie.getId());
		newImage(i);

		// 2. prepend image id to filename to have unique filenames (or generate uuid?)
		String fileNameWithId = i.getId() + "--" + fileName;
		File file = new File(configuration.getImageUploadPath(), fileNameWithId);
		FileUtils.copyInputStreamToFile(fileInputStream, file);
		
		// 3. set filename to Image and persist
		i.setName(fileNameWithId);
		em.persist(i);
		return i;
	}

	public List<Image> getImagesForMoviesIds(String moviesIds) {
		if (moviesIds==null) return new ArrayList<>();
		TypedQuery<Image> query = em.createQuery("SELECT i FROM Image i WHERE i.movieId IN ?1", Image.class);
		query.setParameter(1, Arrays.asList(moviesIds.split(",")));
		List<Image> result = query.getResultList();
		return result;		
	}
	
}
