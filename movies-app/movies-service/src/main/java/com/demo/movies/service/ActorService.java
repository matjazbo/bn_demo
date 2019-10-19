package com.demo.movies.service;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.demo.data.model.Actor;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import com.kumuluz.ee.rest.utils.QueryStringDefaults;

@RequestScoped
public class ActorService {

	private static final Logger logger = LogManager.getLogger(ActorService.class);

	@PersistenceContext
	private EntityManager em;

	@Context
	protected UriInfo uriInfo;	

	private static QueryStringDefaults qsd = new QueryStringDefaults()
			.maxLimit(200)
			.defaultLimit(10)
			.defaultOffset(0);	

	public List<Actor> getAllActors() {
		QueryParameters qParams = qsd.builder()
				.queryEncoded(uriInfo.getRequestUri().getRawQuery())
				.build();
		List<Actor> result = JPAUtils.queryEntities(em, Actor.class, qParams);

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
