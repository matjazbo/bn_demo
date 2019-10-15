package com.demo.movies.api.counter.factory;

import com.demo.movies.api.counter.Counter;

public interface CounterFactory {

	public Counter getCounter(String counterId);
	
}
