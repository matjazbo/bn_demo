package com.demo.movies.api.counter;

/**
 * A simple counter interface
 *
 */
public interface Counter extends Identifiable<String> {

	public void increaseCounter() throws Exception;

}
