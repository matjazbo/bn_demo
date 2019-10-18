package com.demo.movies.api.counter.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.movies.api.configuration.CounterConfiguration;
import com.demo.movies.api.counter.Counter;
import com.demo.movies.api.counter.executor.CounterExecutor;
import com.demo.movies.api.counter.factory.CounterFactory;

/**
 * Provides methods for executing (increasing) a counter
 * 
 * @author Matjaz
 *
 */
public class CounterService {

	private static final Logger logger = LogManager.getLogger(CounterService.class);
	
	private CounterFactory counterFactory;
	private CounterExecutor counterExecutor;
	private CounterConfiguration counterConfiguration;

	private CounterService() {};
	
	public static CounterService getService(CounterExecutor counterExecutor, CounterFactory counterFactory, CounterConfiguration counterConfiguration) {
		CounterService service = new CounterService();
		service.counterExecutor = counterExecutor;
		service.counterFactory = counterFactory;
		service.counterConfiguration = counterConfiguration;
		return service;
	}
	
	/**
	 * Executes (increases) a counter using the given execution strategy
	 * 
	 * @return returns a Counter object if increaseCounter succeeded, otherwise null
	 */
	public Counter increaseCounter(String counterId) {
		
		Counter counter = counterFactory.getCounter(counterId, counterConfiguration);
		
		if (counter == null) {
			logger.error("Could not increase counter, because factory didn't create object.");
			return null;
		}
		
		try {
			counterExecutor.execute(counter);
			return counter;
		} catch (Exception e) {
			logger.error("Error increasing counter " + counterId, e);
			// eating up the exception and not bothering the client with it
			// rethrow if client needs to handle this problem
		}
		return null;
	}
	
}
