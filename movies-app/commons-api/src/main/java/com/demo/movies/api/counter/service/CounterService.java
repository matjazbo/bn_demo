package com.demo.movies.api.counter.service;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.movies.api.annotation.Asynchronous;
import com.demo.movies.api.counter.Counter;
import com.demo.movies.api.counter.executor.CounterExecutor;
import com.demo.movies.api.counter.factory.CounterFactory;

/**
 * Provides methods for executing (increasing) a counter
 * 
 * @author Matjaz
 *
 */
@RequestScoped
public class CounterService {

	private static final Logger logger = LogManager.getLogger(CounterService.class);
	
	@Inject
	private CounterFactory counterFactory;

	@Inject 
	@Asynchronous	// selecting asynchronous executor 
	CounterExecutor defaultCounterExecutor;
	
	/**
	 * Executes (increases) a counter using the default execution strategy
	 */
	public Counter increaseCounter(String counterId) {
		return increaseCounter(counterId, defaultCounterExecutor);
	}
	
	/**
	 * Executes (increases) a counter using the given execution strategy
	 * 
	 * @return returns a Counter object if increaseCounter succeeded, otherwise null
	 */
	public Counter increaseCounter(String counterId, CounterExecutor executor) {
		if (executor==null) executor = defaultCounterExecutor;
		
		Counter counter = counterFactory.getCounter(counterId);
		
		if (counter == null) {
			logger.error("Could not increase counter, because factory didn't create object.");
			return null;
		}
		
		try {
			executor.execute(counter);
			return counter;
		} catch (Exception e) {
			logger.error("Error increasing counter " + counterId, e);
			// eating up the exception and not bothering the client with it
			// rethrow if client needs to handle this problem
		}
		return null;
	}
	
}