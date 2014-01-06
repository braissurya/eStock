package com.jjtech.estock.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;
import com.jjtech.estock.validator.MenuValidator;


/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 9:25:42 PM
 * @Description : inputan master menu
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/admin/menu")
public class MasterMenuController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
//		binder.setValidator(new MenuValidator());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
	}
	
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("parentMenu", dbService.selectDropDown("child.id", "child.level \"desc\", child.nama", "lst_menu child	INNER JOIN lst_menu parent on child.parent = parent.id and parent.active=true", "child.active=true ", "child.path, child.urut"));
		map.put("AllMenu", dbService.selectDropDown("id", "nama", "lst_menu", "active = 1", "nama"));
		return map;
	}
	
	
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {
		logger.debug("Halaman: Master Menu, method: SHOW");
		Utils.generatePaging(dbService, request, model, "menu", 0);
		return "master/master_menu_list";
	}		

	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(@ModelAttribute("menu") Menu menu) {
		logger.debug("Halaman:  Master Menu, method: NEW");
		menu.setMode("NEW");
		return "master/master_menu_edit";
	}
	
	//edit data
		@RequestMapping(value="/edit/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
		public String update(@ModelAttribute("menu")Menu menu, @PathVariable Integer uname, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
			logger.debug("Halaman:  Master Menu, method: EDIT");
			
			//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
			Menu tmp=dbService.selectListMenu(uname).get(0);
			BeanUtils.copyProperties(tmp, menu);
			menu.setMode("EDIT");
			return "master/master_menu_edit";
		}
		
		//view data
		@RequestMapping(value="/view/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
		public String view(@ModelAttribute("menu") Menu menu, @PathVariable Integer uname) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
			logger.debug("Halaman:  Master Menu, method: View");
			
			//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
			
			Menu tmp=dbService.selectListMenu(uname).get(0);
			BeanUtils.copyProperties(tmp, menu);
			
			menu.setMode("VIEW");
			
			
			return "master/master_menu_edit";
		}

	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("menu") Menu menu, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Master Menu, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nama",  "NotEmpty", new String[]{""},null);
			  ValidationUtils.rejectIfEmptyOrWhitespace(errors, "parent",  "NotEmpty", new String[]{""},null);
			if (!result.hasErrors()) {
				
			}
		}

		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", null,null));
			model.addAllAttributes(map);
			
			return "master/master_menu_edit";
		}

		//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{
			pesan = dbService.saveMenu(menu, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Master Menu",""+menu.nama,""},null);

			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Menu", Utils.errorExtract(e), null);
			
			
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/admin/menu"; 
		
		
	}
	
	//user menekan tombol delete
	@RequestMapping(value="/delete/{uname}", method=RequestMethod.GET)
	public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request) throws MailException, MessagingException {
		logger.debug("Halaman: Master Menu, method: DELETE");
		String pesan ="";
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Menu menu=dbService.selectListMenu(uname).get(0);
			menu.setMode("DELETE");
			pesan = dbService.saveMenu(menu, currentUser);
			
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Master Menu","","diproses"},null);

			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Menu", Utils.errorExtract(e), null);
		
		
		}
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/admin/menu";
	}
	
}
