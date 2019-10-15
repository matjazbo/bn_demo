package com.demo.movies.api.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.FIELD;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

/**
 * Annotates synchronous quality of the resource
 * 
 * @author Matjaz
 *
 */
@Retention(RUNTIME)
@Target({ TYPE, FIELD })
@Qualifier
public @interface Synchronous {

}
