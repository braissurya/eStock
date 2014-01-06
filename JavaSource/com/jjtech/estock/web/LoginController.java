package com.jjtech.estock.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jjtech.estock.model.User;

/**
 * Contoh FormController
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:50:34 AM)
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController extends ParentController{

	//show form pertama kali
	@RequestMapping(method=RequestMethod.GET)
	public String show(@ModelAttribute("user") User user, HttpServletRequest request, HttpSession session) {
		logger.debug("Halaman: LOGIN, method: SHOW");

		//bila masih logged in, langsung ke home saja
        if (session != null) {
        	User currentUser = (User) session.getAttribute("currentUser");
        	if(currentUser != null) {
            	return "redirect:/home";
        	}
        }

		return "login";
	}
	
	//saat user menekan tombol login, process form
	@RequestMapping(method=RequestMethod.POST)
	public String processForm( @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request) { 
		logger.debug("Halaman: LOGIN, method: PROCESSFORM");

		//validasi dasar di model object, validasi lainnya langsung didalam controller
	
		BindException errors = new BindException(result);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", new String[]{"Username"});
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", new String[]{"Password"});
			if (!result.hasErrors()) {
				User tmp = dbService.selectUser(user.username);
				
				
				if(tmp == null) {
					errors.rejectValue("username", "", "Username tidak terdaftar");
				}else if(!tmp.passwordDecrypt.toLowerCase().equals(user.password.toLowerCase())) {
					errors.rejectValue("password", "", "Password Salah");
				}else if(tmp.active!=1) {
					errors.rejectValue("username", "", "Username tidak aktif");
				}else {
					//set beberapa item dari object user yg ditarik dari database ke object user yg dipakai untuk login
					user=tmp;
	//				user.setGroup_menu_id(tmp.getGroup_menu_id());FIXME: belum ada groupmenu
					
				}
			}

		//bila ada error, kembalikan ke halaman login
		if (result.hasErrors()) {
			return "login";
		}

		//bila tidak error, lanjut ke login
		dbService.login(user, sessionRegistry, request);
		
		return "redirect:/home";
	}	
	
}
