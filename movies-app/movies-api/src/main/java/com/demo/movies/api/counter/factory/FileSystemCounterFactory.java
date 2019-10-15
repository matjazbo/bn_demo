package com.demo.movies.api.counter.factory;

import java.io.File;

import javax.enterprise.context.RequestScoped;

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
	
	@Override
	protected Counter createNewCounter(String counter) {
		FileSystemCounter c = FileSystemCounter.newInstance();
		c.setId(counter);
		c.setFile(new File("c:/temp/"+counter));	// TODO - better file creation, use configuration
		return c;
	}

}
