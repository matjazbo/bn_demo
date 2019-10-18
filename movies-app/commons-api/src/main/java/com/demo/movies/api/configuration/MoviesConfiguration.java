package com.demo.movies.api.configuration;

import javax.enterprise.context.ApplicationScoped;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

@ApplicationScoped
@ConfigBundle("movies")
public class MoviesConfiguration extends ApiCommonConfiguration {

    @ConfigValue(value = "counters.file-system.path", watch = true)
    private String countersFilesystemPath;

	@ConfigValue(value = "http-cache.max-age", watch = true)
	private Integer httpCacheMaxAge;

	
	public String getCountersFilesystemPath() {
		return countersFilesystemPath;
	}

	public void setCountersFilesystemPath(String countersFilesystemPath) {
		this.countersFilesystemPath = countersFilesystemPath;
	}

	public Integer getHttpCacheMaxAge() {
		return httpCacheMaxAge;
	}

	public void setHttpCacheMaxAge(Integer httpCacheMaxAge) {
		this.httpCacheMaxAge = httpCacheMaxAge;
	}

}
