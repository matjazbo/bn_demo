package com.demo.movies.api.counter.executor;


import javax.enterprise.context.RequestScoped;

import com.demo.movies.api.annotation.Synchronous;
import com.demo.movies.api.counter.Counter;

/**
 * A simple execution strategy for immediate execution
 * of the increaseCounter logic on the Counter object.
 * 
 * @author Matjaz
 *
 */
@RequestScoped
@Synchronous
public class ImmediateCounterExecutor extends AbstractCounterExecutor {

	@Override
	public void execute(Counter counter) throws Exception {
		counter.increaseCounter();
	}

}
