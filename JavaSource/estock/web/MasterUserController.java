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

import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;
import com.jjtech.estock.validator.UserValidator;



/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 9:25:42 PM
 * @Description : inputan master user
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/admin/user")
public class MasterUserController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//binder.setValidator(new UserValidator());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
		binder.registerCustomEditor(Integer.class,new CustomNumberEditor(Integer.class, true));
	}
	
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("AllGroupUser", dbService.selectDropDown("id", "nama", "lst_group_user", "1=1", " nama "));
		map.put("listCabang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1", "nama"));
//		map.put("AllUser", dbService.selectDropDown("id", "group_menu_id \"desc\" ,username", "user", "active = 1", "username"));
		return map;
	}
	
	
	@RequestMapping(value="/{uname}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request,@PathVariable Integer uname) {
		logger.debug("Halaman: Master User, method: SHOW");
		Utils.generatePaging(dbService, request, model, "user", uname);
		return "master/master_user_list";
	}		

	//input baru
	@RequestMapping(value="/new/{uname}", method=RequestMethod.GET)
	public String insert(@ModelAttribute("user") User user,@PathVariable Integer uname,Model model) {
		logger.debug("Halaman:  Master User, method: NEW");
		if(uname==-1)uname=null;
		user.setMode("NEW");
		user.setGroup_user_id(uname);
		user.setPassword("123bcd");
		user.flag_approval=0;
		if(uname==null)uname=-1;
		model.addAttribute("groupuserid", uname);
		model.addAttribute("groupuserName", uname==-1?"":dbService.selectListGroupUser(uname, null, 1, 1).get(0).getNama());
		
		return "master/master_user_edit";
	}
	
	//edit data
	@RequestMapping(value="/edit/{uname}/{prod}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(@ModelAttribute("user")User user, @PathVariable Integer uname, Model model,@PathVariable Integer prod) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master User, method: EDIT");
		if(prod==-1)prod=null;
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		User tmp=dbService.selectAllUser(prod, uname).get(0);
		BeanUtils.copyProperties(tmp, user);
		user.setMode("EDIT");
		if(prod==null)prod=-1;
		model.addAttribute("groupuserName", prod==-1?"":dbService.selectListGroupUser(prod, null, 1, 1).get(0).getNama());
		model.addAttribute("groupuserid", prod);
		return "master/master_user_edit";
	}
		
		//view data
	@RequestMapping(value="/view/{uname}/{prod}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(@ModelAttribute("user") User user, @PathVariable Integer uname, Model model,@PathVariable Integer prod) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master User, method: View");
		if(prod==-1)prod=null;
		//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
		
		User tmp=dbService.selectAllUser(prod, uname).get(0);
		BeanUtils.copyProperties(tmp, user);
		
		user.setMode("VIEW");
		if(prod==null)prod=-1;
		model.addAttribute("groupuserName",  prod==-1?"":dbService.selectListGroupUser(prod, null, 1, 1).get(0).getNama());
		model.addAttribute("groupuserid", prod);
		return "master/master_user_edit";
	}

	//saat user menekan tombol save baik dari layar input maupun layar edit
		@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
		public String save(@Valid @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
			logger.debug("Halaman:  Master User, method: SAVE");
			
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			
			//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
			if (!result.hasErrors()) {
				BindException errors = new BindException(result);
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username",  "NotEmpty", new String[]{""},null);
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",  "NotEmpty", new String[]{""},null);
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "cabang_id",  "NotEmpty", new String[]{""},null);
				if (!result.hasErrors()) {
					
				}
			}
			Integer groupid=ServletRequestUtils.getIntParameter(request, "groupid",-1);
			//bila ada error, kembalikan ke halaman edit
			if (result.hasErrors()) {
				model.addAttribute("groupuserName",  groupid==-1?"":dbService.selectListGroupUser(groupid, null, 1, 1).get(0).getNama());
				model.addAttribute("groupuserid", groupid);
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("messageType", "error");
				map.put("message", messageSource.getMessage("ErrorForm", null,null));
				model.addAllAttributes(map);
				
				return "master/master_user_edit";
			}

			//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
			//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
			//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
			String pesan ="";
			try{
				pesan = dbService.saveUser(user, currentUser);
				//balikin ke layar list input
			}catch (Exception e) {
				pesan=messageSource.getMessage("submitfailed", new String[]{"Master User",""+user.id,"diproses"},null);
				e.printStackTrace();
				email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada eStock", Utils.errorExtract(e), null);
				
				
			}
			ra.addFlashAttribute("pesan", pesan);
			
			return "redirect:/admin/user/"+groupid; 
			
			
		}
		
		//user menekan tombol delete
		@RequestMapping(value="/delete/{uname}/{prod}", method=RequestMethod.GET)
		public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request,@PathVariable Integer prod) throws MailException, MessagingException {
			logger.debug("Halaman: Master User, method: DELETE");
			String pesan ="";
			try{
				//currently logged in user
				User currentUser = (User) request.getSession(false).getAttribute("currentUser");
				if(prod==-1)prod=null;
				User user=dbService.selectAllUser(prod,uname).get(0);
				user.setMode("DELETE");
				pesan = dbService.saveUser(user, currentUser);
				
			}catch (Exception e) {
				e.printStackTrace();
				pesan=messageSource.getMessage("submitfailed", new String[]{"Master User","","diproses"},null);

				email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);
			
			
			}
			ra.addFlashAttribute("pesan", pesan);
			if(prod==null)prod=-1;
			return "redirect:/admin/user/"+prod;
		}
		
		//user menekan tombol delete
		@RequestMapping(value="/reset/{uname}/{prod}", method=RequestMethod.GET)
		public String reset(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request,@PathVariable Integer prod) throws MailException, MessagingException {
			logger.debug("Halaman: Master User, method: DELETE");
			String pesan ="";
			try{
				//currently logged in user
				User currentUser = (User) request.getSession(false).getAttribute("currentUser");
				if(prod==-1)prod=null;
				User user=dbService.selectAllUser(prod,uname).get(0);
				user.setMode("RESET");
				pesan = dbService.saveUser(user, currentUser);
				
			}catch (Exception e) {
				e.printStackTrace();
				pesan=messageSource.getMessage("submitfailed", new String[]{"Master User","","diproses"},null);

				email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);
			
			
			}
			ra.addFlashAttribute("pesan", pesan);
			if(prod==null)prod=-1;
			return "redirect:/admin/user/"+prod;
		}
	
}
