package com.demo.movies.service;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;

import com.kumuluz.ee.rest.utils.QueryStringDefaults;

public class DemoProducer {

	/**
	 * TODO - CDI can't find this producer.Don't know why. Get it to work.
	 * 	      Error: WELD-001408: Unsatisfied dependencies for type QueryStringDefaults
	 */
    @Produces
    @ApplicationScoped
    public QueryStringDefaults getQueryStringDefaults() {
        return new QueryStringDefaults()
                .maxLimit(200)
                .defaultLimit(10)
                .defaultOffset(0);
    }
    
}
