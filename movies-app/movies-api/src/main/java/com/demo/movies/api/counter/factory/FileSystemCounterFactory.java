package com.demo.movies.api.counter.factory;

import java.io.File;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import com.demo.movies.api.configuration.MoviesConfiguration;
import com.demo.movies.api.counter.Counter;
import com.demo.movies.api.counter.FileSystemCounter;

/**
 * Factory for Counters that are stored in the local file system.
 * 
 * @author Matjaz
 *
 */
@RequestScoped
public class FileSystemCounterFactory extends AbstractCounterFactory {
	
	@Inject MoviesConfiguration config;
	
	@Override
	protected Counter createNewCounter(String counter) {
		FileSystemCounter c = FileSystemCounter.newInstance();
		c.setId(counter);
		String countersFilesystemPath = config.getCountersFilesystemPath();
		// TODO - check if property is missing
		
		c.setFile(new File(countersFilesystemPath + "/" + counter));
		return c;
	}

}
