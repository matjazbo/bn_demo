package com.demo.movies.data.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates if value is an IMDB movie id
 * @author Matjaz
 * 
 */
public class ImdbIdValidator implements ConstraintValidator<ImdbId, String> {

	
    public boolean isValid(String value, ConstraintValidatorContext context) {

    	// TODO implement more proper logic, check ID online
    	if (value.startsWith("tt")) {
    		return true;
    	}
    	
    	return false;    	
    }
}