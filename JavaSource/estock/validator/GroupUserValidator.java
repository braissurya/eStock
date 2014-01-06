package com.jjtech.estock.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jjtech.estock.model.GroupUser;
import com.jjtech.estock.model.Menu;

/**
 * Validator Untuk Input Group Policy (gabungan life dan fire)
 * 
 * @author Yusuf
 * @since Feb 4, 2013 (11:29:07 AM)
 *
 */
public class GroupUserValidator implements Validator {



	public boolean supports(Class clazz) {
		return GroupUser.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//Validasi dilakukan pada nested objects
		  ValidationUtils.rejectIfEmptyOrWhitespace(e, "nama",  "NotEmpty", new String[]{""},null);

       
	}

}
