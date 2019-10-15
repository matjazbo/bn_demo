package com.demo.movies.api.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Annotates asynchronous quality of the resource
 * 
 * @author Matjaz
 *
 */
@Retention(RUNTIME)
@Target({ TYPE })
@Qualifier
public @interface Asynchronous {

}
