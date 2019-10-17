package com.demo.images.configuration;

import javax.enterprise.context.ApplicationScoped;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ApplicationScoped
@ConfigBundle("movies")
public class ImagesConfiguration {
	
    @ConfigValue(value = "image.upload.path", watch = true)
    private String imageUploadPath;

	public String getImageUploadPath() {
		return imageUploadPath;
	}

	public void setImageUploadPath(String imageUploadPath) {
		this.imageUploadPath = imageUploadPath;
	}

}
