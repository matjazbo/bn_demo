package com.demo.movies.api.counter;

abstract class AbstractCounter implements Counter, Identifiable<String> {

	String counterId;
	
	@Override
	public int hashCode() {
		return counterId.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s [%s]", this.getClass().getName(), counterId);
	}
	
	@Override
	public String getId() {
		return counterId;
	}

	@Override
	public void setId(String counterId) {
		this.counterId = counterId;
	}
	
}
