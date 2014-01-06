package com.jjtech.estock.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jjtech.estock.model.Supplier;

public class SupplierValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return Supplier.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//Validasi dilakukan pada nested objects
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "nama",  "NotEmpty", new String[]{""},null);
	}

}
