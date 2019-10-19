package com.demo.data.model;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.demo.data.model.validation.ImdbId;


@Entity
@Table(indexes = {
			@Index(name = "mv_title_idx",  columnList="title", unique = false)
			})
@NamedQueries(value = {
		@NamedQuery(name = "Movie.fetchAll", query = "SELECT m FROM Movie m LEFT JOIN FETCH m.actors"),
		@NamedQuery(name = "Movie.fetchOne", query = "SELECT m FROM Movie m LEFT JOIN FETCH m.actors WHERE m.id = ?1"),
		@NamedQuery(name = "Movie.deleteOne", query = "DELETE Movie m WHERE m.id = ?1"),
}) 
public class Movie {

	// TODO define indexes
	
	@Id
	@NotNull
	@ImdbId
	private String id;	// imdb id, tt0119654

	@NotNull
	@Column(length = 255)
	@Size(max = 255)
	private String title;

	@Min(1800)
	@Max(2200)
	private Integer year;

	@Size(max = 5000)
	private String description;

	@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	@JoinTable(
			name = "movies_actors", 
			joinColumns = @JoinColumn(name = "movie_id"), 
			inverseJoinColumns = @JoinColumn(name = "actor_id"))
	@Column(updatable = false)	// this doesnt work, why?
	private Set<Actor> actors;

	//@OneToMany(mappedBy = "movie", fetch = FetchType.LAZY)
	@Transient
	private List<Image> images;

	/**
	 * Two Movies with the same id should be equal
	 */
	@Override
	public int hashCode() {
		int hashCode = Objects.hash(id, title, year, description, actors, images);
		return hashCode;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder(getId());
		s.append("] ").append(getTitle()).append(", ").append(getYear());
		return s.toString();
	}	
	
	/******* getters and setters below *******/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Set<Actor> getActors() {
		return actors;
	}

	public void setActors(Set<Actor> actors) {
		this.actors = actors;
	}


}
