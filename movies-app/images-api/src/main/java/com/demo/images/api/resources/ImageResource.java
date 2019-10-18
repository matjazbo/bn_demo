package com.demo.images.api.resources;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.demo.data.model.Image;
import com.demo.images.service.ImageService;
import com.demo.movies.api.cache.RequestClientCachingBuilder;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;

@RequestScoped
@Path("/images")
@Log(LogParams.METRICS)
public class ImageResource {

	private static final Logger logger = LogManager.getLogger(ImageResource.class);

	private static final String UPLOAD_FOLDER = "c:/temp/";

	@Inject ImageService imageService;

	@Inject
	private RequestClientCachingBuilder cacheBuilder;	

	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/image/{movieId}")
	public Response getImagesForMovieId(@PathParam("movieId") String movieId, @Context Request request) {
		List<Image> images = imageService.getImagesForMovieId(movieId);

		return cacheBuilder.addCaching(request, images).build();
	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/movies/{moviesIds}")
	public Response getImagesForMoviesIds(@PathParam("moviesIds") String moviesIds, @Context Request request) {
		List<Image> images = imageService.getImagesForMoviesIds(moviesIds);
		return cacheBuilder.addCaching(request, images).build();
	}
	
	@POST
	@Path("/upload/{movieId}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response uploadImageForMovie(
			@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileData,
			@PathParam("movieId") String movieId) {
		
		if (fileInputStream == null || fileData == null || movieId == null)
			return Response.status(Response.Status.BAD_REQUEST).build();
		
		try {
			imageService.uploadImageFile(fileInputStream, fileData.getFileName(), movieId);
		} catch (IOException e) {
			logger.error("Error saving image " + fileData.getName() + " for movie id " + movieId, e);
			Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		
		return Response.noContent().build();
	}

}
