package com.jjtech.estock.web;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jjtech.estock.model.Category;
import com.jjtech.estock.model.Opname;
import com.jjtech.estock.model.OpnameDet;
import com.jjtech.estock.model.Stock;
import com.jjtech.estock.model.StockHist;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;
import com.jjtech.estock.validator.CategoryValidator;
import com.jjtech.estock.validator.StockOpnameValidator;

@Controller
@RequestMapping("/transaksi/Stockopname")
public class StockOpnameController extends ParentController {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
//		binder.setValidator(new StockOpnameValidator());
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
		binder.registerCustomEditor(Integer.class,new CustomNumberEditor(Integer.class, true));
	}
	
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("listKetOpname", dbService.selectDropDown("jenis", "keterangan", "lst_config", "id = 3 and active = 1", " jenis "));
//		map.put("listKategori", dbService.selectDropDown("id", "nama", "lst_kategori", "active = 1", "nama"));
//		map.put("listCabang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1", "nama"));
//		map.put("AllUser", dbService.selectDropDown("id", "group_menu_id \"desc\" ,username", "user", "active = 1", "username"));
		return map;
	}
	
	@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request) {
		logger.debug("Halaman: Berita Acara Gudang, method: SHOW");
		Utils.generatePaging(dbService, request, model, "opname", 0);
		return "stockopname/stockopname_list";
//		return "stockopname/test";
	}
	
	//input baru
	@RequestMapping(value="/new", method=RequestMethod.GET)
	public String insert(@ModelAttribute("opname") Opname opname,Model model, HttpSession session, RedirectAttributes ra) {
		logger.debug("Halaman: Berita Acara Gudang , method: NEW");
		opname.setMode("NEW");
		User currentUser = (User) session.getAttribute("currentUser");
//		//validasi apabila sudah pernah input Berita Acara Gudang untuk bulan yg sedang berjalan dari sysdate,, tidak perlu add stockopname baru,,cukup memakai ID Berita Acara Gudang yg sudah pernah diproses.
		Date sysdate = dbService.selectSysdate();
		Integer month_sysdate = sysdate.getMonth()+1;
		Integer year_sysdate = sysdate.getYear()+1900;
		List<Opname> tmp=dbService.selectListOpname("cabang_id = "+currentUser.cabang_id);
		for(int i=0;i<tmp.size();i++){
			Date tgl = tmp.get(i).getTgl();
			Integer month_tgl = tgl.getMonth()+1;
			Integer year_tgl = tgl.getYear()+1900;
			if(month_sysdate==month_tgl && year_sysdate==year_tgl){
				String pesan= messageSource.getMessage("submitfailed", new String[]{"Untuk Berita Acara Gudang bulan "+month_tgl+" tahun "+(tgl.getYear()+1900)+" sudah pernah dilakukan di OPNAME_ID "+tmp.get(i).getOpname_id(),"",""},null);
				ra.addFlashAttribute("pesan", pesan);
				return "redirect:/transaksi/Stockopname";
//				throw new RuntimeException ("Untuk Berita Acara Gudang bulan "+month_tgl+" tahun "+(tgl.getYear()+1900)+" sudah pernah dilakukan di OPNAME_ID "+tmp.get(i).getOpname_id());
			}
		}
		opname.setLsOpnameDet(dbService.selectListOpnameDetFromStock(currentUser.getCabang_id()));
		dbService.saveOpname(opname, currentUser);
		//cek apakah transaksi masuk ke periode clossing
		model.addAttribute("clossingWarning", dbService.checkTglClossing(1,currentUser));
		return "stockopname/stockopname_edit";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("opname") Opname opname, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman: Berita Acara Gudang, method: SAVE");
		opname.setMode("EDIT");
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			int i=0;
			for(OpnameDet od: opname.lsOpnameDet){
				//validasi penginputan qty_fisik
				if(od.qty_fisik==null){
//					daftaRider["+k+"].plan_rider
					errors.rejectValue("lsOpnameDet["+i+"].qty_fisik", "", "Belum diinput.");
//					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lsOpnameDet["+i+"].qty_fisik",  "NotEmpty", new String[]{""},null);
				}else{
					//validasi untuk penginputan keterangan apabila qty_fisik sudah diisi.
					if(od.jenis==null && od.qty.compareTo(od.qty_fisik)!=0 ){
						errors.rejectValue("lsOpnameDet["+i+"].jenis", "", "Untuk Produk "+od.nama_item+" silakan dipilih terlebih dahulu keterangannya.");
					}else{
						if(od.qty_fisik<od.qty && od.jenis==3){
							errors.rejectValue("lsOpnameDet["+i+"].qty_fisik", "", "nilai plus.");
						}
						if(od.qty_fisik>od.qty && od.jenis!=3){
							errors.rejectValue("lsOpnameDet["+i+"].qty_fisik", "", "nilai minus.");
						}
					}
				}
				i++;
			}
			if (!result.hasErrors()) {
				
			}
		}
		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("ErrorForm", new String[]{"tess"," ", "gagal om"},null));
			model.addAllAttributes(map);
			
			return "stockopname/stockopname_edit";
		}
		String pesan ="";
		try{
			pesan = dbService.saveOpname(opname, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Berita Acara Gudang"," ","gagal Diproses"},null);
				email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Berita Acara Gudang", Utils.errorExtract(e), null);
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/transaksi/Stockopname";
	}
	
	@RequestMapping(value="/edit/{opname_id}", method=RequestMethod.GET) 
	public String update(@ModelAttribute("opname") Opname opname, @PathVariable Integer opname_id, Model model, HttpSession session) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Berita Acara Gudang, method: EDIT");
		User currentUser = (User) session.getAttribute("currentUser");
		Opname tmp=dbService.selectListOpname("opname_id ="+opname_id+" and cabang_id = "+currentUser.cabang_id).get(0);
		BeanUtils.copyProperties(tmp, opname);
		opname.setMode("EDIT");
		opname.lsOpnameDet = dbService.selectListOpnameDet(opname_id);
		//cek apakah transaksi masuk ke periode clossing
		model.addAttribute("clossingWarning", dbService.checkTglClossing(1,currentUser));
		return "stockopname/stockopname_edit";
	}
	
	//view data
	@RequestMapping(value="/view/{opname_id}", method=RequestMethod.GET) 
	public String view(@ModelAttribute("opname") Opname opname, @PathVariable Integer opname_id, HttpSession session) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Berita Acara Gudang, method: View");
		User currentUser = (User) session.getAttribute("currentUser");
		Opname tmp=dbService.selectListOpname("opname_id ="+opname_id+" and cabang_id = "+currentUser.cabang_id).get(0);
		BeanUtils.copyProperties(tmp, opname);
		opname.setMode("VIEW");
		opname.lsOpnameDet = dbService.selectListOpnameDet(opname_id);
		return "stockopname/stockopname_edit";
	}
	
	//delete data
	@RequestMapping(value="delete/{opname_id}", method=RequestMethod.GET)
	public String delete(@ModelAttribute("opname") Opname opname, @PathVariable Integer opname_id, Model model, HttpSession session, RedirectAttributes ra) { 
		logger.debug("Halaman: Berita Acara Gudang, method: DELETE");
		User currentUser = (User) session.getAttribute("currentUser");
		opname.setMode("DELETE");
		String pesan ="";
		try{
			pesan = dbService.saveOpname(opname, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Berita Acara Gudang"," ","gagal Diproses"},null);
				email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Berita Acara Gudang", Utils.errorExtract(e), null);
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/transaksi/Stockopname";
	}
	
	//transfer data
	@RequestMapping(value="transfer/{opname_id}/{no_trans}", method=RequestMethod.GET)
	public String transfer(@ModelAttribute("opname") Opname opname, @PathVariable Integer opname_id , @PathVariable String no_trans , BindingResult result , Model model, HttpSession session, RedirectAttributes ra) {
		logger.debug("Halaman: Berita Acara Gudang, method: Transfer");
		User currentUser = (User) session.getAttribute("currentUser");
		opname.no_trans=no_trans;
		//jika qty_fisik belum diisi, munculkan pesan tidak dapat transfer karena qty_fisik belum semua diisi(Berita Acara Gudang belum selesai)
		opname.lsOpnameDet = dbService.selectListOpnameDet(opname_id);
		int i=0;
		for(OpnameDet od:opname.lsOpnameDet){
			if(od.qty_fisik==null){
				//bila ada error, kembalikan ke halaman edit
				if (!result.hasErrors()) {
					BindException errors = new BindException(result);
					errors.rejectValue("lsOpnameDet["+i+"].qty_fisik", "", "Untuk Produk "+od.nama_item+" belum diinput stok fisik.");
//					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lsOpnameDet["+i+"].qty_fisik",  "NotEmpty", new String[]{""},null);
					i++;
				}
				
			}
		}
		
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message", messageSource.getMessage("Proses Transfer Gagal.", null,null));
			model.addAllAttributes(map);
			
			return "stockopname/stockopname_edit";
		}
		String pesan = "";
		try{
			pesan = dbService.prosesTransferOpname(opname, opname_id, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Berita Acara Gudang "," ","ditransfer."},null);
			email.send(
				true, props.getProperty("email.from"),
				props.getProperty("admin.email.to").split( ";" ), null, null,
				"ERROR pada eStock modul Transfer Berita Acara Gudang", Utils.errorExtract(e), null);
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/transaksi/Stockopname";
	}
	
}
