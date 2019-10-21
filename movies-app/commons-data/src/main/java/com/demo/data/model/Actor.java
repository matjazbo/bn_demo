package com.demo.data.model;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(indexes = { @Index(name = "act_name1_idx", columnList = "firstName", unique = false),
		@Index(name = "act_name2_idx", columnList = "lastName", unique = false),
		@Index(name = "act_name3_idx", columnList = "firstName,lastName", unique = false) })
@NamedQueries(value = { @NamedQuery(name = "Actor.fetchAll", query = "SELECT a FROM Actor a") })
public class Actor {

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Size(max = 255)
	private String firstName;

	@Size(max = 255)
	private String lastName;

	private Date dateOfBirth;

	// Unidirectional only
	@JsonIgnore
	@ManyToMany(mappedBy = "actors", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Set<Movie> movies;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public Set<Movie> getMovies() {
		return movies;
	}

	public void setMovies(Set<Movie> movies) {
		this.movies = movies;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, dateOfBirth);
	}

}
