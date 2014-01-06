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

import com.jjtech.estock.model.Category;
import com.jjtech.estock.model.Item;
import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;
import com.jjtech.estock.validator.CategoryValidator;
import com.jjtech.estock.validator.ItemValidator;
import com.jjtech.estock.web.ParentController;

/**
 * @author 		: Deddy
 * @since		: Mar 14, 2013 10:25:42 PM
 * @Description : inputan master Category
 */

@Controller
@RequestMapping("/master/item")
public class MasterItemController extends ParentController{
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(new ItemValidator());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
	}

//	@ModelAttribute pada deklarasi method berarti: 
//	bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listKategori", dbService.selectDropDown("id", "nama", "lst_kategori", "active = 1", "nama"));
		map.put("listMerk", dbService.selectDropDown("id", "nama", "lst_merk", "active = 1", "nama"));
		map.put("listWarna", dbService.selectDropDown("id", "nama", "lst_warna", "active = 1", "nama"));
		map.put("listSatuan", dbService.selectDropDown("id", "nama", "lst_satuan", "active = 1", "nama"));
		return map;
	}
		
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {	
		logger.debug("Halaman: Master Item, method: SHOW");
		Utils.generatePaging(dbService, request, model, "item", 0);
		return "master/master_item_list";
	}
	
	@RequestMapping(value="/list",method={RequestMethod.GET, RequestMethod.POST})
	public String list(Model model,HttpServletRequest request) {	
		logger.debug("Halaman: Master Item, method: SHOW");
		Utils.generatePaging(dbService, request, model, "item", 0);
		model.addAttribute("list",0);
		return "master/master_item_list";
	}
	
	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(@ModelAttribute("item") Item item) {
		logger.debug("Halaman:  Master Item, method: NEW");
		item.setMode("NEW");
		item.harga = 0.0;
		item.diskon = 0.0;
		item.harga_ecer = 0.0;
		item.diskon_ecer = 0.0;
		item.qty_min = 0.0;
		item.qty_max = 0.0;
		return "master/master_item_edit";
	}
	
	@RequestMapping(value="/edit/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(@ModelAttribute("item") Item item, @PathVariable Integer uname, Model model, HttpServletRequest request) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master Item, method: EDIT");

		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		Item tmp=dbService.selectListItem(null,null, uname, null,null,currentUser.cabang_id).get(0);
		BeanUtils.copyProperties(tmp, item);
		item.setMode("EDIT");
		return "master/master_item_edit";
	}
	
	//view data
	@RequestMapping(value="/view/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(@ModelAttribute("item") Item item, @PathVariable Integer uname, HttpServletRequest request) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman:  Master Item, method: View");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		Item tmp=dbService.selectListItem(null,null, uname, null,null,currentUser.cabang_id).get(0);
		BeanUtils.copyProperties(tmp, item);
		item.setMode("VIEW");
		return "master/master_item_edit";
	}
	
	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("item") Item item, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Master Item, method: SAVE");
		
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
			
			return "master/master_item_edit";
		}
			//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{
			pesan = dbService.saveItem(item, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Master Item",""+item.nama,""},null);
				email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Item", Utils.errorExtract(e), null);
			
			
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/master/item"; 
		
	}
	
	//user menekan tombol delete
	@RequestMapping(value="/delete/{uname}", method=RequestMethod.GET)
	public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request) throws MailException, MessagingException {
		logger.debug("Halaman: Master Item, method: DELETE");
		String pesan ="";
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Item item=dbService.selectListItem(null,null, uname, null,null,currentUser.cabang_id).get(0);
			item.setMode("DELETE");
			pesan = dbService.saveItem(item, currentUser);
			
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Master Item","","diproses"},null);
				email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Item", Utils.errorExtract(e), null);
		
		
		}
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/master/item";
	}
	
}

