package com.demo.movies.api.counter;

public interface Identifiable<T> {

	public void setId(T id);

	public T getId();

}
