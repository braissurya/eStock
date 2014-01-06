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
import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;



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
@RequestMapping("/delivery")
public class InputDriverController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		//binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		//binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDFLong, true)); //bind otomatis date dgn format dd-MM-yyyy HH:mm:ss

		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
		binder.registerCustomEditor(Integer.class,new CustomNumberEditor(Integer.class, true));
	}

	//@ModelAttribute pada deklarasi method berarti:
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();

		map.put("lsDriver", dbService.selectDropDown("id", "concat(nik,' - ',nama)", "mst_karyawan", "active = 1 and jenis=2", "concat(nik,' - ',nama)"));
		return map;
	}


	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {
		logger.debug("Halaman: Transaksi, method: SHOW");

		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="kode",sort_type=null,no_trans=null;
		List<Delivery> listPaging = null;



		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);

		totalData=dbService.selectListDeliveryPagingCount(search,null,1);

		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X

		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)

		if(offset<0)offset=0;

		listPaging=dbService.selectListDeliveryPaging(search, offset, rowcount, sort, sort_type, null,1);
		
		if(listPaging!=null){
			List<Delivery> tmp=new ArrayList<Delivery>();
			for(Delivery d:listPaging){
				d.lsDeliveryDets=dbService.selectListDeliveryDet(d.id, null);
				tmp.add(d);
			}
			listPaging=tmp;
		}
		

		model.addAttribute("listNumRows", listNumRows);
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




		return "trans/delivery_list";
	}
	
	
	

	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(HttpServletRequest request,@ModelAttribute("delivery") Delivery delivery,Model model) {
		logger.debug("Halaman: Transaksi, method: NEW");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		delivery.setMode("NEW");

		Date sysdate=dbService.selectSysdate();
		
		return "trans/delivery_edit";
	}

	//edit data
	@RequestMapping(value="/{id}/edit", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(HttpServletRequest request,@ModelAttribute("delivery") Delivery delivery,  Model model,@PathVariable Integer id) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transaksi, method: Edit");

		Delivery tmp=dbService.selectListDelivery(id, null,null).get(0);
		BeanUtils.copyProperties(tmp, delivery);
		delivery.setMode("EDIT");

		Date sysdate=dbService.selectSysdate();
		

		delivery.lsDeliveryDets=dbService.selectListDeliveryDet(id, null);

		return "trans/delivery_edit";
	}

		//view data
	@RequestMapping(value="/{id}/view", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(HttpServletRequest request,@ModelAttribute("delivery") Delivery delivery,  Model model,@PathVariable Integer id) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transaksi, method: Edit");

		Delivery tmp=dbService.selectListDelivery(id, null,null).get(0);
		BeanUtils.copyProperties(tmp, delivery);
		delivery.setMode("EDIT");

		Date sysdate=dbService.selectSysdate();
		

		delivery.lsDeliveryDets=dbService.selectListDeliveryDet(id, null);

		return "trans/delivery_edit";
	}




	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("delivery") Delivery delivery, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman: Delivery, method: SAVE");

		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");

		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "driver_id",  "NotEmpty", new String[]{""},null);
			
			
			
			int[]idxList=ServletRequestUtils.getIntParameters(request, "trans_id");

			if (idxList.length==0) {
				errors.rejectValue("lsDeliveryDets", null, "Minimal 1 No Transaksi diinput");
			}
		
			List<DeliveryDet> lsDeliveryDet=new ArrayList<DeliveryDet>();
			for(int trans_id:idxList){
				DeliveryDet dd=new DeliveryDet();
				dd.trans_id=trans_id;
				dd.alamat_tujuan=ServletRequestUtils.getStringParameter(request, "alamat_tujuan_"+trans_id,"0");
				dd.contact_tujuan=ServletRequestUtils.getStringParameter(request, "contact_tujuan_"+trans_id,"0");
				dd.telp_tujuan=ServletRequestUtils.getStringParameter(request, "telp_tujuan_"+trans_id,"0");
				dd.no_trans=ServletRequestUtils.getStringParameter(request, "no_trans_"+trans_id,"0");
				try {
					dd.tgl_kirim_est=ServletRequestUtils.getStringParameter(request, "tgl_kirim_est_"+trans_id,"0").equals("0")?null:Utils.defaultDFLong.parse(ServletRequestUtils.getStringParameter(request, "tgl_kirim_est_"+trans_id));
				} catch (ServletRequestBindingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				lsDeliveryDet.add(dd);
			}

			delivery.lsDeliveryDets=lsDeliveryDet;
			
			
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

			return "trans/delivery_edit";
		}

		//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save)
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{

			pesan = dbService.saveDelivery(delivery, currentUser);
			
			//balikin ke layar list input
		}catch (Exception e) {
			pesan=messageSource.getMessage("submitfailed", new String[]{"Save Delivery","","diproses"},null);
			e.printStackTrace();
			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock", Utils.errorExtract(e), null);

		
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message",pesan );
			model.addAllAttributes(map);

			return "trans/delivery_edit";
		}
		ra.addFlashAttribute("messageType", "done");
		ra.addFlashAttribute("message", pesan);
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/delivery";

		

	}
	
	//user menekan tombol delete
	@RequestMapping(value="/{id}/transfer", method=RequestMethod.GET)
	public String transfer(@PathVariable Integer id, RedirectAttributes ra, HttpServletRequest request) throws MailException, MessagingException {
		logger.debug("Halaman: Delivery Transfer, method: Transfer");
		String pesan ="";
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Delivery delivery=dbService.selectListDelivery(id, null,null).get(0);
			delivery.setMode("TRANSFER");
			pesan = dbService.saveDelivery(delivery, currentUser);
			
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Delivery Transfer","","diproses"},null);

			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Menu", Utils.errorExtract(e), null);
		
		
		}
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/delivery";
	}
	
	

}
