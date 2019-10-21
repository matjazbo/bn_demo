package com.demo.movies.configuration;

import javax.enterprise.context.ApplicationScoped;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ApplicationScoped
@ConfigBundle("movies")
public class MoviesDataConfiguration {

	@ConfigValue(value = "service-images.url", watch = true)
	private String serviceImagesUrl;

	public String getServiceImagesUrl() {
		return serviceImagesUrl;
	}

	public void setServiceImagesUrl(String serviceImagesUrl) {
		this.serviceImagesUrl = serviceImagesUrl;
	}

}
