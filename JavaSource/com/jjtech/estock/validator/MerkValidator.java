package com.jjtech.estock.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.jjtech.estock.model.Merk;
import com.jjtech.estock.service.DbService;

public class MerkValidator implements Validator {

	@Autowired
	protected DbService dbService;
	
	public void setDbService(DbService dbService) {
		this.dbService = dbService;
	}
	
	public boolean supports(Class clazz) {
		return Merk.class.equals(clazz);
	}

	public void validate(Object obj, Errors e) {
        Merk merk = (Merk) obj;
		Integer total=0;
		String search="";
		
		search = merk.nama;
		
		//Validasi dilakukan pada nested objects
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "nama",  "NotEmpty", new String[]{""},null);
		ValidationUtils.rejectIfEmptyOrWhitespace(e, "inisial",  "NotEmpty", new String[]{""},null);
		/*
		if(!e.hasErrors()){
			total=dbService.selectListMerkNamaCount(search);
			if(total>0){
				e.reject("Nama tersebut sudah pernah diinput");
			}
		}
		
		if(!e.hasErrors()){
        	if(policy.getFlag_akseptasi()==1){
                if(policy.getJenis()==1|policy.getJenis()==3){
                	ValidationUtils.rejectIfEmptyOrWhitespace(e, "policy_no", "NotEmpty", new String[]{"No Polis"});
         	    }
        	}else{
        		if(policy.getFlag_akseptasi()!=2)e.rejectValue("flag_akseptasi", null, "Kolom Akseptasi harus berisi 1: aksep  atau 2: ditolak");
        	}
        	
        	Merk temp=dbService.selectPolicy(null,null,null,policy.getSpaj_no());
        	Merk temp=dbService.
        	
        	if(temp==null){
        		e.rejectValue("spaj_no", null, "No Spaj tidak ditemukan");
        	}else{
        		policy.setId(temp.getId());
        	}
        }*/
       
	}

}
