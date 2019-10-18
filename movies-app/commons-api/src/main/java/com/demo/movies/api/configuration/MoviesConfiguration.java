package com.demo.movies.api.configuration;

import javax.enterprise.context.ApplicationScoped;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ApplicationScoped
@ConfigBundle("movies")
public class MoviesConfiguration extends ApplicationCommonConfiguration {

	
    @ConfigValue(value = "counters.file-system.path", watch = true)
    private String countersFilesystemPath;

	public String getCountersFilesystemPath() {
		return countersFilesystemPath;
	}

	public void setCountersFilesystemPath(String countersFilesystemPath) {
		this.countersFilesystemPath = countersFilesystemPath;
	}
}
