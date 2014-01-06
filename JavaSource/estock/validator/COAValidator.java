package com.jjtech.estock.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jjtech.estock.model.COA;

public class COAValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return COA.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//Validasi dilakukan pada nested objects
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "id",  "NotEmpty", new String[]{""},null);
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "nama",  "NotEmpty", new String[]{""},null);
       
	}

}
