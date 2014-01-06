package com.jjtech.estock.web.master;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jjtech.estock.model.Warna;
import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;
import com.jjtech.estock.validator.WarnaValidator;
import com.jjtech.estock.web.ParentController;

/**
 * @author 		: Rudy
 * @since		: Apr 7, 2013 10:25:42 PM
 * @Description : inputan master Warna
 */

@Controller
@RequestMapping("/master/warna")
public class MasterWarnaController extends ParentController{
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new WarnaValidator());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
	}

//	@ModelAttribute pada deklarasi method berarti: 
//	bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		return map;
	}
		
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {	
		logger.debug("Halaman: Master Warna, method: SHOW");
		Utils.generatePaging(dbService, request, model, "warna", 0);
		return "master/master_warna_list";
	}
	
	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(@ModelAttribute("warna") Warna warna) {
		logger.debug("Halaman:  Master Warna, method: NEW");
		warna.setMode("NEW");
		return "master/master_warna_edit";
	}
	
	@RequestMapping(value="/edit/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(@ModelAttribute("warna") Warna warna, @PathVariable Integer uname, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master Warna, method: EDIT");
		
		Warna tmp=dbService.selectListWarna(uname).get(0);
		BeanUtils.copyProperties(tmp, warna);
		warna.setMode("EDIT");
		return "master/master_warna_edit";
	}
	
	//view data
	@RequestMapping(value="/view/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(@ModelAttribute("warna") Warna warna, @PathVariable Integer uname) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master Warna, method: View");
		
		Warna tmp=dbService.selectListWarna(uname).get(0);
		BeanUtils.copyProperties(tmp, warna);
		warna.setMode("VIEW");
		return "master/master_warna_edit";
	}
	
	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("warna") Warna warna, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Master Warna, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			if (!result.hasErrors()) {
				
			}
		}
			//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			model.addAllAttributes(map);
			
			return "master/master_warna_edit";
		}
			//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{
			pesan = dbService.saveWarna(warna, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Master Warna",""+warna.nama,""},null);
				email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Warna", Utils.errorExtract(e), null);
			
			
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/master/warna"; 
		
	}
	
	//user menekan tombol delete
	@RequestMapping(value="/delete/{uname}", method=RequestMethod.GET)
	public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request) throws MailException, MessagingException {
		logger.debug("Halaman: Master Warna, method: DELETE");
		String pesan ="";
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Warna warna=dbService.selectListWarna(uname).get(0);
			warna.setMode("DELETE");
			pesan = dbService.saveWarna(warna, currentUser);
			
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Master Warna","","diproses"},null);
				email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Warna", Utils.errorExtract(e), null);
		
		
		}
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/master/warna";
	}
	
}

