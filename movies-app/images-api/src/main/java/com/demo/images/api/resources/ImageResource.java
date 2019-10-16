package com.demo.images.api.resources;


import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.images.service.ImageService;
import com.demo.movies.data.model.Image;
import com.kumuluz.ee.logs.cdi.Log;
import com.kumuluz.ee.logs.cdi.LogParams;

@RequestScoped
@Path("/images")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log(LogParams.METRICS)
public class ImageResource {

	private static final Logger logger = LogManager.getLogger(ImageResource.class);
	
	@Inject ImageService imageService;

	@GET
	@Path("/image/{id}")
	public Response getImagesForMovieId(@PathParam("id") String movieId) {
		//List<Image> images = imageService.getImagesForMovieId(movieId);
		List<Image> images = new ArrayList<>();
		
		return Response.status(Response.Status.OK).entity(images).build();
	}
	
}
