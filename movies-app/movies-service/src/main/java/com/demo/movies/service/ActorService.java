package com.demo.movies.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.movies.data.model.Actor;

@RequestScoped
public class ActorService {

	private static final Logger logger = LogManager.getLogger(ActorService.class);
	
	@PersistenceContext
	private EntityManager em;
	
	public List<Actor> getAllActors() {
		TypedQuery<Actor> query = em.createNamedQuery("Actor.fetchAll", Actor.class);
		List<Actor> result = query.getResultList();
		return result;
	}
	
	@Transactional
	public void newActor(Actor actor) {
		em.persist(actor);
	}

	@Transactional
	public void updateActor(@Valid Actor actor) {
		em.merge(actor);
	}
	
	@Transactional
	public void deleteActor(Long actorId) {
		if (actorId==null) {
			throw new IllegalArgumentException("Actor id missing");
		}
		
		Query query = em.createQuery("DELETE Actor a WHERE a.id = ?1");
		query.setParameter(1, actorId);
		query.executeUpdate();
	}
	
}
