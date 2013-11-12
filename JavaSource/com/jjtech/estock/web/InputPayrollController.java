package com.jjtech.estock.web;

import java.text.ParseException;
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
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jjtech.estock.model.Delivery;
import com.jjtech.estock.model.DeliveryDet;
import com.jjtech.estock.model.Payroll;
import com.jjtech.estock.model.Warna;
import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;
import com.jjtech.estock.validator.WarnaValidator;
import com.jjtech.estock.web.ParentController;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: May 12, 2013 9:28:02 PM
 * @Description : input payroll
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */

@Controller
@RequestMapping("/payroll")
public class InputPayrollController extends ParentController{

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultCF, true));
		binder.registerCustomEditor(Integer.class,new CustomNumberEditor(Integer.class, true));
	}

	//@ModelAttribute pada deklarasi method berarti:
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("lsperiode", dbService.selectDropDown("DATE_FORMAT(periode, '%m-%Y')", "DATE_FORMAT(periode, '%M %Y')", "mst_payroll", "1 = 1 group by periode", "periode desc"));
		
		return map;
	}


	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {
		logger.debug("Halaman: Transaksi, method: SHOW");

		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="periode",sort_type=null,no_trans=null,periodeStr=null;
		Date periode=null;
		List<Payroll> listPaging = null;

		periodeStr=ServletRequestUtils.getStringParameter(request, "periode", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "periode", "");
		if(periodeStr!=null){		
			periode=Utils.convertStringToDate(periodeStr, "MM-yyyy");
		}
			
		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);

		totalData=dbService.selectListPayrollPagingCount(search, periode);

		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X

		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)

		if(offset<0)offset=0;

		listPaging=dbService.selectListPayrollPaging(search, offset, rowcount, sort, sort_type, periode);
		
		model.addAttribute("listNumRows", listNumRows);
		model.addAttribute("periode", periodeStr);
		model.addAttribute("totalPage", totalPage);
		model.addAttribute("totalData", totalData);
		model.addAttribute("halfPage", (new Double(totalPage/2)).intValue());
		List<Integer>pages=new ArrayList<Integer>();
		for (int i = 0; i < totalPage; i++) {
			pages.add(i+1);
		}
		model.addAttribute("pages", pages);
		model.addAttribute("page", page);
		model.addAttribute("rowcount", rowcount);
		model.addAttribute("search", search);
		model.addAttribute("sort", sort);
		model.addAttribute("sort_type", sort_type);
		model.addAttribute("listPaging", listPaging);

		return "payroll_list";
	}
	
	
	

	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(HttpServletRequest request,@ModelAttribute("payroll") Payroll payroll,Model model) {
		logger.debug("Halaman: Transaksi, method: NEW");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		payroll.setMode("NEW");

		Date sysdate=dbService.selectSysdate();
		
		return "payroll_edit";
	}

	//edit data
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(HttpServletRequest request,@ModelAttribute("payroll") Payroll payroll,  Model model,@PathVariable Integer id) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transaksi, method: Edit");

		Payroll tmp=dbService.selectListPayroll(id, null, null, null).get(0);
		BeanUtils.copyProperties(tmp, payroll);
		payroll.setMode("EDIT");
		
		try{
			payroll.gaji_total=payroll.gapok+payroll.uang_makan+payroll.uang_transport+payroll.uang_lembur+payroll.bonus;
			payroll.pot_total=payroll.pot_asuransi+payroll.pot_pinjam+payroll.pot_lain;
			payroll.gaji_bersih=payroll.gaji_total-payroll.pot_total;
		}catch(Exception e){
			
		}
		
		return "payroll_edit";
	}

		//view data
	@RequestMapping(value="/{id}/view", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(HttpServletRequest request,@ModelAttribute("payroll") Payroll payroll,  Model model,@PathVariable Integer id) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transaksi, method: Edit");

		Payroll tmp=dbService.selectListPayroll(id, null, null, null).get(0);
		BeanUtils.copyProperties(tmp, payroll);
		payroll.setMode("EDIT");
		
		try{
			payroll.gaji_total=payroll.gapok+payroll.uang_makan+payroll.uang_transport+payroll.uang_lembur+payroll.bonus;
			payroll.pot_total=payroll.pot_asuransi+payroll.pot_pinjam+payroll.pot_lain;
			payroll.gaji_bersih=payroll.gaji_total-payroll.pot_total;
		}catch(Exception e){
			
		}
		
		return "payroll_edit";
	}




	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("payroll") Payroll payroll, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman: Delivery, method: SAVE");

		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");

		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "karyawan_id",  "NotEmpty", new String[]{""},null);
			
			
		}


		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			String message=messageSource.getMessage("ErrorForm", null,null);
			for(String err:Utils.errorBinderToList(result, messageSource)){
				if(!(err.trim().contains("diisi")||err.trim().contains("may not be null")))
				message+="<br/>"+err;
			}
			map.put("message",message );
			model.addAllAttributes(map);

			return "payroll_edit";
		}

		//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save)
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{

			pesan = dbService.savePayroll(payroll, currentUser);

			//balikin ke layar list input
		}catch (Exception e) {
			pesan=messageSource.getMessage("submitfailed", new String[]{"Save Payroll","","diproses"},null);
			e.printStackTrace();
			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock", Utils.errorExtract(e), null);

		
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message",pesan );
			model.addAllAttributes(map);

			return "payroll_edit";
		}
		ra.addFlashAttribute("messageType", "done");
		ra.addFlashAttribute("message", pesan);
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/payroll";
	}
	
	

}

