package com.demo.movies.test;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.UriInfo;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.demo.data.model.Image;
import com.demo.data.model.Movie;
import com.demo.movies.configuration.MoviesDataConfiguration;
import com.demo.movies.service.MovieService;
import com.demo.movies.service.restclient.images.ImagesClientRest;
import com.demo.movies.service.util.JPAUtilsCaller;
import com.kumuluz.ee.rest.beans.QueryParameters;

@RunWith(Arquillian.class)
public class MovieServiceTest {

	@Mock private EntityManager em;
	@Mock MoviesDataConfiguration configuration;
	@Mock JPAUtilsCaller jpaUtils;
	@Mock protected UriInfo uriInfo;
	@Mock Query query; 
	@Mock TypedQuery typedQuery;
	@Mock ImagesClientRest imagesClientRest;

	@InjectMocks MovieService service;


	@Deployment
	public static JavaArchive createDeployment() {
		JavaArchive jar = ShrinkWrap.create(JavaArchive.class)
				.addPackages(true, MovieService.class.getPackage())
				.addPackages(true, Movie.class.getPackage())
				.addPackages(true, EntityManager.class.getPackage())
				.addPackages(true, Query.class.getPackage())
				.addPackages(true, UriInfo.class.getPackage())
				.addPackages(true, MoviesDataConfiguration.class.getPackage())
				.addPackages(true, "org.powermock")
				.addPackages(true, "org.mockito")
				.addPackages(true, "org.objenesis")
				.addPackages(true, "org.eclipse.microprofile.rest")
				.addPackages(true, "com.kumuluz.ee.rest")
				.addPackages(true, "javassist")
				.addPackages(true, "org.apache.commons.lang3")
				.addAsResource("config.yaml", "config.yaml") 
				.addAsResource("log4j2.xml", "log4j2.xml") 
				.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
		return jar;
	}

	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}    

	@Test
	public void testDelete() throws URISyntaxException {
		Mockito.when(query.executeUpdate()).thenReturn(1);
		Mockito.when(em.createNamedQuery("Movie.deleteOne")).thenReturn(query);
		service.deleteMovie(getMockedMovie().getId());
	}

	@Test
	public void getMovie() {
		Movie mockMovie = getMockedMovie();
		Mockito.when(em.createNamedQuery("Movie.fetchOne", Movie.class)).thenReturn(typedQuery);
		Mockito.when(typedQuery.getSingleResult()).thenReturn(mockMovie);
		assertEquals(service.getMovie("", false).get(), mockMovie);
	}

	@Test
	public void getAllMovies() throws URISyntaxException {
		// Prepare mocks
		List<Movie> mockedMovies = new ArrayList<>();
		Movie mockMovie = getMockedMovie();
		mockedMovies.add(mockMovie);
		
		Image mockImage = new Image();
		mockImage.setId(333L);
		mockImage.setMovieId(mockMovie.getId());
		mockImage.setName("Mock image.jpg");
		List<Image> mockedImages = new ArrayList<>();
		mockedImages.add(mockImage);
		
		// Prepare Mockito mocks
		Mockito
		.when(jpaUtils.queryEntities(
				Matchers.<EntityManager>any(),
				Mockito.any(Class.class), 
				Mockito.any(QueryParameters.class)))
		.thenReturn(mockedMovies);		
		Mockito.when(uriInfo.getRequestUri()).thenReturn(new URI("http://localhost:8080/movies?order=id%20DESC"));	// TODO should test more different queries
		Mockito.when(imagesClientRest.getImagesForMoviesIds(Matchers.anyString())).thenReturn(mockedImages);
		
		// run service
		List<Movie> result = service.getAllMovies();
		
		// check results
		assertEquals(mockedMovies, result);		
		result.stream().forEach(m -> {
			assertEquals(m.getImages(), mockedImages);
		});
	}

	protected Movie getMockedMovie() {
		Movie mockMovie = new Movie();
		mockMovie.setId("tt1856101");
		mockMovie.setTitle("Mock movie");
		mockMovie.setYear(2000);
		return mockMovie;
	}	

}
