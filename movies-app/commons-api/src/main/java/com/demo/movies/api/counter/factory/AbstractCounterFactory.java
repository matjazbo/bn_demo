package com.demo.movies.api.counter.factory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.movies.api.configuration.CounterConfiguration;
import com.demo.movies.api.counter.Counter;

public abstract class AbstractCounterFactory implements CounterFactory {

	private static final Logger logger = LogManager.getLogger(AbstractCounterFactory.class);

	protected static final Map<String, Counter> counterCache = new ConcurrentHashMap<>(5);

	/**
	 * Multiple calls for same counterId return same object. Method is thread safe.
	 * 
	 * @param counterId     counter identifier
	 * @param configuration a configuration object for creating the counter
	 * @return new Counter object if it doesn't exist for given id, or an existing
	 *         object from cache
	 */
	@Override
	public Counter getCounter(String counterId, CounterConfiguration configuration) {

		if (counterCache.containsKey(counterId)) {
			Counter counterFromCache = counterCache.get(counterId);
			logger.trace("Counter cache hit for id {}", counterId);
			return counterFromCache;
		}
		Counter c = createNewCounter(counterId, configuration);
		if (c == null)
			return null;
		counterCache.put(counterId, c);
		logger.trace("New counter for id {} created, added to cache.");
		return c;
	}

	protected abstract Counter createNewCounter(String counter, CounterConfiguration configuration);

}
