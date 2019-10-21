package com.demo.movies.api.counter.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.enterprise.context.RequestScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.movies.api.annotation.Asynchronous;
import com.demo.movies.api.counter.Counter;

@RequestScoped
@Asynchronous
public class AsyncCounterExecutor extends AbstractCounterExecutor {

	private static final Logger logger = LogManager.getLogger(AsyncCounterExecutor.class);

	private static final ExecutorService executorService = Executors.newFixedThreadPool(2);

	@Override
	public void execute(Counter counter) throws Exception {
		// preveri ce vse stima, da ni deadlocka ali kaka pizdarija

		executorService.submit(() -> {
			try {
				counter.increaseCounter();
			} catch (Exception e) {
				// exception in the thread, just log
				logger.error("Error in threaded counter increase " + counter, e);
			}
		});
	}

}
