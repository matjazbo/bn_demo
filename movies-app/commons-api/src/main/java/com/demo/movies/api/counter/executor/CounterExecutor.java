package com.demo.movies.api.counter.executor;

import com.demo.movies.api.counter.Counter;

/**
 * An interface that provides api for different implementations on how to
 * execute a counter (for example an asynchronous execution).
 * 
 * @author Matjaz
 *
 */
public interface CounterExecutor {

	public void execute(Counter counter) throws Exception;

}
