package com.demo.movies.api.initializer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.movies.api.annotation.Asynchronous;
import com.demo.movies.api.configuration.CounterConfiguration;
import com.demo.movies.api.counter.executor.CounterExecutor;
import com.demo.movies.api.counter.factory.CounterFactory;
import com.demo.movies.api.counter.service.CounterService;

@ApplicationScoped
public class DemoInitializer {
	
	private static final Logger logger = LogManager.getLogger(DemoInitializer.class);
	
	@Inject	private CounterFactory counterFactory;
	@Inject @Asynchronous private CounterExecutor counterExecutor;
	@Inject private CounterConfiguration counterConfiguration;
	
    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
    	logger.info("--- Initializaing DEMO application ---");
    	
    	logger.info("Initializaing Counter service");
    	CounterService.initialize(counterExecutor, counterFactory, counterConfiguration);
    }

}


