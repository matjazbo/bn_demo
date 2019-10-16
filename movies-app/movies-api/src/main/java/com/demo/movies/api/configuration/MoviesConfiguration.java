package com.demo.movies.api.configuration;

import javax.enterprise.context.ApplicationScoped;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ApplicationScoped
@ConfigBundle("movies")
public class MoviesConfiguration {

    @ConfigValue(value = "counters.file-system.path", watch = true)
    private String countersFilesystemPath;

	public String getCountersFilesystemPath() {
		// if last character is a / or \ then remove it
		char lastChar = countersFilesystemPath.charAt(countersFilesystemPath.length()-1);
		if (lastChar=='/' || lastChar=='\\') 
			countersFilesystemPath = countersFilesystemPath.substring(0, countersFilesystemPath.length()-1); 

		return countersFilesystemPath;
	}

	public void setCountersFilesystemPath(String countersFilesystemPath) {
		this.countersFilesystemPath = countersFilesystemPath;
	}
}
