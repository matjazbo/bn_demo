/**
 * 
 */
package com.demo.movies.data.model.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * Annotation for validation of fields which represent an IMDB id
 * @author Matjaz
 *
 */
@Retention(RUNTIME)
@Target(FIELD)
@Constraint(validatedBy = ImdbIdValidator.class)
public @interface ImdbId {
	
    String message() default "Value must be a valid IMDB movie id";
    
    Class<?>[] groups() default {};
 
    Class<? extends Payload>[] payload() default {};
    
}
