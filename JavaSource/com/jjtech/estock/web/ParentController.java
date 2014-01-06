package com.jjtech.estock.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.jjtech.estock.service.DbService;
import com.jjtech.estock.utils.Email;
import com.jjtech.estock.utils.SessionRegistry;
import com.jjtech.estock.utils.Utils;



/**
 * Abstract ParentController sebagai parent dari semua controller
 * Cuman untuk meletakkan reference data saja dan beberapa variable
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:50:34 AM)
 *
 */
public abstract class ParentController {

	protected static Logger logger = Logger.getLogger(ParentController.class);
	
	@Autowired
	protected SessionRegistry sessionRegistry;
	
	@Autowired
	protected MessageSource messageSource;
		
	@Autowired
	protected DbService dbService;
	
	@Autowired
	protected Email email;

	@Autowired
	protected Properties props;
		
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("company")
	public Map<String, String> company(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("name", messageSource.getMessage("company.name", null, null));
		map.put("address", messageSource.getMessage("company.address", null, null).replaceAll("\\n", "<br>"));
		map.put("copyright", messageSource.getMessage("company.copyright", new String[]{Utils.getCopyrightYears(dbService.selectSysdate())}, null));
		return map;
	}
	
}
