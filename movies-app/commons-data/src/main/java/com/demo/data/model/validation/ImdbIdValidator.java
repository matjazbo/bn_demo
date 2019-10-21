package com.demo.data.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validates if value is an IMDB movie id
 * 
 * @author Matjaz
 * 
 */
public class ImdbIdValidator implements ConstraintValidator<ImdbId, String> {

	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (value == null)
			return false;
		if (value.startsWith("tt")) {
			String id = value.substring(2);
			if (id.length() != 7) { // we suppose id is exactly 7 chars long
				return false;
			}
			try {
				Integer.parseInt(id);
			} catch (NumberFormatException e) {
				// id part is not numeric
				return false;
			}
			return true;
		}

		return false;
	}
}