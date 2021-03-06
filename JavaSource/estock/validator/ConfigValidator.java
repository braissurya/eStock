package com.jjtech.estock.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jjtech.estock.model.Config;

public class ConfigValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return Config.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//Validasi dilakukan pada nested objects
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "jenis",  "NotEmpty", new String[]{""},null);
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "keterangan",  "NotEmpty", new String[]{""},null);
       
	}

}
