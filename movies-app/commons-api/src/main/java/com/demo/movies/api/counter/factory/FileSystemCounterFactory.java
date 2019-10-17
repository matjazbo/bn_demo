package com.demo.movies.api.counter.factory;

import java.io.File;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
	
	private static final Logger logger = LogManager.getLogger(FileSystemCounterFactory.class);
	
	@Inject MoviesConfiguration config;

	/**
	 * Creates a new file on the configuration path movies.counters.file-system.path
	 * Returns a null object if it cannot create Counter.
	 */
	@Override
	protected Counter createNewCounter(String counterId) {
		try {
			String countersFilesystemPath = config.getCountersFilesystemPath();
			if (countersFilesystemPath==null || countersFilesystemPath.isBlank()) {
				logger.error("Could not create counter with id {}, because countersFilesystemPath is empty", counterId);
				return null;
			}
			
			FileSystemCounter c = FileSystemCounter.newInstance();
			c.setId(counterId);
			
			c.setFile(new File(countersFilesystemPath + "/" + counterId));
			return c;
		} catch (Exception e) {
			logger.error("Error creating new counter with id " + counterId, e);
			return null;
		}
	}

}
