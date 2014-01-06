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

import com.jjtech.estock.model.GroupUser;
import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;
import com.jjtech.estock.validator.GroupUserValidator;
import com.jjtech.estock.validator.MenuValidator;



/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Jan 28, 2013 9:25:42 PM
 * @Description : inputan master group user
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/admin/groupuser")
public class MasterGroupUserController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
//		binder.setValidator(new GroupUserValidator());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));

	}
	
	//@ModelAttribute pada deklarasi method berarti: 
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
//		map.put("listMenu", dbService.selectDropDown("id", "nama", "menu", "active = 1", "nama"));
//		map.put("AllGroupUser", dbService.selectDropDown("id", "nama", "group_menu", "active=1 group by nama", " nama "));
		return map;
	}
	
	
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {
		logger.debug("Halaman: Master Group User, method: SHOW");
		
//		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
//		String search=null, sort="id",sort_type=null;
//		
//		//reference data utk dropdown
//		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
//		
//		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
//		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
//		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
//		//perhitungan paging
//		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
//		totalData=dbService.selectListGroupUserPagingCount(search,1);
//		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
//		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
//		
//		
//		if(page<1) page = 1;
//		if(page>totalPage) page = totalPage;
//		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
//		
//		if(offset<0)offset=0;
//		
//		List<GroupUser> listGroupUser=dbService.selectListGroupUserPaging(search, offset, rowcount, sort, sort_type,1);
//		
//		
//		model.addAttribute("listNumRows", listNumRows);
//		model.addAttribute("totalPage", totalPage);
//		model.addAttribute("totalData", totalData);
//		model.addAttribute("halfPage", (new Double(totalPage/2)).intValue());
//		List<Integer>pages=new ArrayList<Integer>();
//		for (int i = 0; i < totalPage; i++) {
//			pages.add(i+1);
//		}
//		model.addAttribute("pages", pages);
//		model.addAttribute("page", page);
//		model.addAttribute("rowcount", rowcount);
//		model.addAttribute("search", search);
//		model.addAttribute("sort", sort);
//		model.addAttribute("sort_type", sort_type);
//		model.addAttribute("listPaging", listGroupUser);
		Utils.generatePaging(dbService, request, model, "groupuser", 0);
		

		
		
		return "master/master_groupuser_list";
	}		

	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(@ModelAttribute("groupuser") GroupUser groupuser) {
		logger.debug("Halaman:  Master Group User, method: NEW");
		groupuser.setMode("NEW");
		List<Menu> lsMenu=new ArrayList<Menu>();			
		for(Menu mn:dbService.selectListMenu(null)){
			
			lsMenu.add(mn);
		}
		groupuser.setMenu(lsMenu);
		return "master/master_groupuser_edit";
	}
	
	//edit data
		@RequestMapping(value="/edit/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
		public String update(@ModelAttribute("groupuser")GroupUser groupuser, @PathVariable Integer uname, Model model) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
			logger.debug("Halaman:  Master Group User, method: EDIT");
			
			//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
			GroupUser tmp=dbService.selectListGroupUser(uname,null,1,1).get(0);
			BeanUtils.copyProperties(tmp, groupuser);
			groupuser.setMode("EDIT");
			
			List<Menu> lsMenu=new ArrayList<Menu>();			
			for(Menu mn:dbService.selectListMenu(null)){
				if(!dbService.selectListHakAkses(uname, mn.id, null,1).isEmpty()){
					mn.akses=true;
				}
				lsMenu.add(mn);
			}
			groupuser.setMenu(lsMenu);
			
			return "master/master_groupuser_edit";
		}
		
		//view data
		@RequestMapping(value="/view/{uname}", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
		public String view(@ModelAttribute("groupuser") GroupUser groupuser, @PathVariable Integer uname) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
			logger.debug("Halaman:  Master Group User, method: View");
			
			//entah kenapa, user = oracleService.selectUserByUsername(uname) tidak bisa jalan. 
			
			GroupUser tmp=dbService.selectListGroupUser(uname,null,1,1).get(0);
			BeanUtils.copyProperties(tmp, groupuser);
			
			groupuser.setMode("VIEW");
			List<Menu> lsMenu=new ArrayList<Menu>();			
			for(Menu mn:dbService.selectListMenu(null)){
				if(!dbService.selectListHakAkses(uname, mn.id, null,1).isEmpty()){
					mn.akses=true;
				}
				lsMenu.add(mn);
			}
			groupuser.setMenu(lsMenu);
			return "master/master_groupuser_edit";
		}

	//saat user menekan tombol save baik dari layar input maupun layar edit
		@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
		public String save(@Valid @ModelAttribute("groupuser") GroupUser groupuser, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
			logger.debug("Halaman:  Master Group User, method: SAVE");
			
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			
			//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
			if (!result.hasErrors()) {
				BindException errors = new BindException(result);
				 ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nama",  "NotEmpty", new String[]{""},null);
				if (!result.hasErrors()) {
					
				}
			}

			//bila ada error, kembalikan ke halaman edit
			if (result.hasErrors()) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("messageType", "error");
				map.put("message", messageSource.getMessage("ErrorForm", null,null));
				model.addAllAttributes(map);
				
				return "master/master_groupuser_edit";
			}

			//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
			//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
			//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
			String pesan ="";
			try{
				pesan = dbService.saveGroupUser(groupuser, currentUser);
				//balikin ke layar list input
			}catch (Exception e) {
				pesan=messageSource.getMessage("submitfailed", new String[]{"Master Group User",""+groupuser.nama,"diproses"},null);

				email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);
				
				
			}
			ra.addFlashAttribute("pesan", pesan);
			return "redirect:/admin/groupuser"; 
			
			
		}
		
		//user menekan tombol delete
		@RequestMapping(value="/delete/{uname}", method=RequestMethod.GET)
		public String delete(@PathVariable Integer uname, RedirectAttributes ra, HttpServletRequest request) throws MailException, MessagingException {
			logger.debug("Halaman: Master Group User, method: DELETE");
			String pesan ="";
			try{
				//currently logged in user
				User currentUser = (User) request.getSession(false).getAttribute("currentUser");
				GroupUser groupuser=dbService.selectListGroupUser(uname,null,1,1).get(0);
				groupuser.setMode("DELETE");
				pesan = dbService.saveGroupUser(groupuser, currentUser);
				
			}catch (Exception e) {
				e.printStackTrace();
				pesan=messageSource.getMessage("submitfailed", new String[]{"Master Group User","","diproses"},null);

				email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);
			
			
			}
			ra.addFlashAttribute("pesan", pesan);
			
			return "redirect:/admin/groupuser";
		}
	
}
