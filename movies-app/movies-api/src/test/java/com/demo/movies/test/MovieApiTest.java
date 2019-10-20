package com.demo.movies.test;

import static io.restassured.RestAssured.DEFAULT_PORT;
import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.demo.data.model.Movie;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.EeApplication;

import io.restassured.http.ContentType;
import io.restassured.response.Response;


@RunAsClient
public class MovieApiTest {

	private static final Logger logger = LogManager.getLogger(MovieApiTest.class);
	private static EeApplication app;
	
    @BeforeClass
    public static void setUp() {
    	if (portAvailable(DEFAULT_PORT)) {	// TODO - port should be read from config.yaml
    		app = new EeApplication();
    	}
    }
    
    @AfterClass
    public static void tearDown() throws JsonParseException, JsonMappingException, IOException {
    	cleanup();
    	
    	if (app!=null && app.getServer()!=null && !portAvailable(DEFAULT_PORT)) {
    		app.getServer().stopServer();
    	}    	
    }    

    private static void cleanup() throws JsonParseException, JsonMappingException, IOException {
    	logger.info("--------- CLEANUP phase ---------");
    	Movie movie = mapToEntity("postMovieAndGetMovie.json", Movie.class);
    	delete("/movies/" + movie.getId());
    	logger.info("Deleted movie {}", movie.getId());
    	
		List<Movie> movies = mapToEntity("5movies.json", List.class, new TypeReference<List<Movie>>() {});
		movies.stream().forEach(m -> {
			delete("/movies/" + m.getId());
			logger.info("Deleted movie {}", m.getId());
		});
	}

	@Test
	public void postMovieAndGetMovie() throws IOException {
		Movie movie = mapToEntity("postMovieAndGetMovie.json", Movie.class);
		
		given()
			.contentType(ContentType.JSON)
			.body(movie)
			.post("/movies")
			.then()
			.statusCode(200);
		
		Response response = get("movies/" + movie.getId()).andReturn();
		assertEquals(response.jsonPath().get("id"), movie.getId());
		assertEquals(response.jsonPath().get("title"), movie.getTitle());
		assertEquals(response.jsonPath().get("year"), movie.getYear());
		assertEquals(response.jsonPath().get("description"), movie.getDescription());
		logger.info("OK");
	}

	@Test
	public void add5MoviesTestPagingAndSearch() throws JsonParseException, JsonMappingException, IOException {
		List<Movie> movies = mapToEntity("5movies.json", List.class, new TypeReference<List<Movie>>() {});
		movies.stream().forEach(m -> {
			logger.info("Adding movie {}", m);
			given().contentType(ContentType.JSON)
			.body(m)
			.post("/movies")
			.then()
			.statusCode(200);
			
			logger.info("Find movie {} by title {}", m.getId(), m.getTitle());
			given().
				pathParam("title", m.getTitle()).
			when().
				get("movies/findBy/title/{title}").
			then().
				assertThat().
				statusCode(200).
			and().
				contentType(ContentType.JSON).
			and().
				body("[0].id", equalTo(m.getId()));
			
		});

		// sort by ID
		movies.sort((Movie m1, Movie m2) -> {
			return m1.getId().compareTo(m2.getId());
		});
		
		// get second movie in list
		Movie secondMovieInList = movies.get(1);
		
		// find test movies, sort them by ID and display 2 movies (limit=2), starting at 2nd (offset=1)
		given().
			pathParam("title", "Test movie").
		when().
			get("movies/findBy/title/{title}?offset=1&limit=2&order=id ASC").
		then().
			assertThat().
			statusCode(200).
		and().
			contentType(ContentType.JSON).
		and().
			body("", hasSize(2)).
		and().
			body("[0].id", equalTo(secondMovieInList.getId()));
		
	}

	
	/****** utility functions below ******/
	
	protected static String getJson(String fileName) throws IOException {
		Writer writer = new StringWriter();
		IOUtils.copy(MovieApiTest.class.getResourceAsStream(fileName), writer, "utf-8");
		String json = writer.toString();
    	return json;
    }
	protected static <T> T mapToEntity(String jsonFilename, Class<T> type) throws JsonParseException, JsonMappingException, IOException {
		return mapToEntity(jsonFilename, type, null);
	}
	
	protected static <T> T mapToEntity(String jsonFilename, Class<T> type, TypeReference<?> toValueTypeRef) throws JsonParseException, JsonMappingException, IOException {
		jsonFilename = "json/" + jsonFilename;	// take files from src/test/resources/json
		ObjectMapper mapper = new ObjectMapper();
		InputStream rstream = MovieApiTest.class.getClassLoader().getResourceAsStream(jsonFilename);
		if (toValueTypeRef!=null) {
			T readValue = (T) mapper.readValue(rstream, type);
			T obj = mapper.convertValue(readValue, toValueTypeRef);
			return obj;
		} else {
			T entity = (T) mapper.readValue(rstream, type);
			return entity;
		}
	}
	
	/**
	 * Check if port is listening.
	 * @return true if port is not in LISTEN state, false is in LISTEN state
	 */
	private static boolean portAvailable(int port) {
	    try (Socket ignored = new Socket("localhost", port)) {
	        return false;
	    } catch (IOException ignored) {
	        return true;
	    }
	}	
	
}
