package com.jjtech.estock.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jjtech.estock.model.Category;
import com.jjtech.estock.model.Opname;

public class StockOpnameValidator implements Validator {
	
	public boolean supports(Class clazz) {
		return Opname.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
		//Validasi dilakukan pada nested objects
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "opnameDet.qty_fisik",  "NotEmpty", new String[]{"Untuk proses Berita Acara Gudang,qty_fisik tidak boleh kosong."},null);
       
	}

}
