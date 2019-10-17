package com.demo.images.api;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/")
public class ImageApplication extends Application {
	
	@Override
	public Map<String, Object> getProperties() {
	    Map<String, Object> props = new HashMap<>();
	    
	    // register MultiPartFeature for file uploading
	    props.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.media.multipart.MultiPartFeature");
	    return props;
	}	
    
    
}
