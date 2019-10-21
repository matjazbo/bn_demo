package com.demo.data.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.demo.data.model.validation.ImdbId;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Image {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ImdbId
	@Column(name = "movie_id")
	private String movieId;

	private String name;

	@JsonIgnore
	@Transient
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

	public String getMovieId() {
		return movieId;
	}

	public void setMovieId(String movieId) {
		this.movieId = movieId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, movieId);
	}

}
