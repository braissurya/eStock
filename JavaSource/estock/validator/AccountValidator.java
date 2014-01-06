package com.jjtech.estock.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jjtech.estock.model.Account;

public class AccountValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return Account.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//Validasi dilakukan pada nested objects
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "cabang",  "NotEmpty", new String[]{""},null);
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "no_rek",  "NotEmpty", new String[]{""},null);
       
	}

}
