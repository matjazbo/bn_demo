package com.demo.movies.service.util;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

/**
 * Wrapper class for calling the static JPAUtils methods
 * which can not be mocked.
 * 
 * @author Matjaz
 *
 */
@RequestScoped
public class JPAUtilsCaller {

	public <T> List<T> queryEntities(EntityManager em, Class<T> type, QueryParameters qParams) {
		return JPAUtils.queryEntities(em, type, qParams);
	}
	
}
