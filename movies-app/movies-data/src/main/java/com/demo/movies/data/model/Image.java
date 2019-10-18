package com.demo.movies.data.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Image {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	private String name;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	private Movie movie;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}
	
	@Override
	public int hashCode() {
		if (getId()==null) return 0;
		return getId().hashCode();
	}
	
}
