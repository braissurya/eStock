package com.jjtech.estock.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
import com.jjtech.estock.model.Gudang;
import com.jjtech.estock.model.Trans;
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
@RequestMapping("/gudang")
public class InputGudangController extends ParentController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
		binder.registerCustomEditor(Integer.class,new CustomNumberEditor(Integer.class, true));
	}

	//@ModelAttribute pada deklarasi method berarti:
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lsDriver", dbService.selectDropDown("id", "concat(nik,' - ',nama)", "mst_karyawan", "active = 1 and jenis=2", "concat(nik,' - ',nama)"));
		map.put("lsCabang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1 and jenis=1", "nama"));
		map.put("lsGudang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1 and jenis=2", "nama"));
		map.put("lsPayMode", dbService.selectDropDown("jenis", "keterangan", "lst_config", "active = 1 and id=8", "keterangan"));
		return map;
	}

	

	//input baru
	@RequestMapping(value="/{jenistrans}", method={RequestMethod.GET, RequestMethod.POST})
	public String insert(HttpServletRequest request,@ModelAttribute("gudang") Gudang gudang,Model model, BindingResult result,@PathVariable String jenistrans) {
		logger.debug("Halaman: Transaksi, method: NEW");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		gudang.setMode("VIEW");
		
		
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		
		if (request.getParameter("cari")!=null) {
			BindException errors = new BindException(result);
			
			
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "trans_no", "", messageSource.getMessage("NotEmpty", new String[]{"No Transaksi"},null));
			
			if(!result.hasErrors()){
				/*int jenis=0;
				if(jenistrans.toLowerCase().equals("penjualan")){
					jenis=4;
				}else if(jenistrans.toLowerCase().equals("pembelian")){
					jenis=3;
				}*/
				
				List<Trans> lstrans=dbService.selectListTrans(null, null, null, gudang.trans_no,aksescabang_id);
				if(!lstrans.isEmpty()){
					gudang.trans=lstrans.get(0);
					gudang.jenis=1;//transaksi
					
					if(gudang.trans.jenis==3)gudang.setJenistrans("Pembelian");
					else if(gudang.trans.jenis==4)gudang.setJenistrans("Penjualan");
					
					if(gudang.trans.posisi_id!=5){
						gudang.trans.no_trans=null;
						errors.rejectValue("trans_no", null, "Posisi Transaksi saat ini masih di "+gudang.trans.posisi_idKet);	
					}else{
						if(gudang.trans.jenis==4){
							if(gudang.trans.paid==null)gudang.trans.paid=0;
							if(gudang.trans.pay_mode==1&gudang.trans.paid!=1){
								gudang.trans.no_trans=null;
								errors.rejectValue("trans_no", null, "Mohon lunasi terlebih dahulu pembayaran Anda");
							}
						}
					}
					
					if(!result.hasErrors()){
						if(gudang.trans.jenis==4){	
							if(gudang.trans.customer_id!=null)gudang.trans.customer=dbService.selectListCustomer(gudang.trans.customer_id).get(0);
						}else if(gudang.trans.jenis==3){
							if(gudang.trans.supplier_id!=null)gudang.trans.supplier=dbService.selectListSupplier(gudang.trans.supplier_id).get(0);
						}else{
							throw new RuntimeException ("Page not found");
						}

						gudang.trans.lsTransDet=dbService.selectListTransDet(gudang.trans.trans_id, null, null, null, null);
						if(gudang.trans.sales_id!=null)gudang.trans.karyawan=dbService.selectListKaryawan(gudang.trans.sales_id,null).get(0);

						gudang.trans.sub_total_harga=gudang.trans.total_harga+gudang.trans.total_disc;
						gudang.trans.persen_disc=(gudang.trans.total_disc/gudang.trans.sub_total_harga)*100;
					}
				}else{
					errors.rejectValue("trans_no", null, "Maaf No Transaksi tidak ditemukan");	
				}
					
				/*if(gudang.trans_no.length()>10){
				}else{//ini kayanya ga kepakai
					List<Delivery> lsDelivery=dbService.selectListDelivery(null, null,gudang.trans_no);
					if(!lsDelivery.isEmpty()){
						gudang.delivery=lsDelivery.get(0);
						gudang.delivery.lsDeliveryDets=dbService.selectListDeliveryDet(gudang.delivery.id, null);
						gudang.jenis=2;//batch
						
						if(jenistrans.toLowerCase().equals("penjualan")){	
							
						}else if(jenistrans.toLowerCase().equals("pembelian")){
							
						}else{
							throw new RuntimeException ("Page not found");
						}
					}else{
						errors.rejectValue("trans_no", null, "Maaf No Transaksi tidak ditemukan");	
					}
				}*/
			}
		
		}
		
		//cek apakah transaksi masuk ke periode clossing
		model.addAttribute("clossingWarning", dbService.checkTglClossing(1,currentUser));
		return "trans/gudang_edit";
	}

	




	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("gudang") Gudang gudang, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman: Gudang, method: SAVE");

		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		

		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			List<Trans> lstrans=dbService.selectListTrans(null, null, null, gudang.trans.no_trans,aksescabang_id);
			if(!lstrans.isEmpty()){
				Trans trans=lstrans.get(0);
				
				if(trans.posisi_id!=5){
					trans.no_trans=null;
					errors.rejectValue("trans_no", null, "Posisi Transaksi saat ini masih di "+gudang.trans.posisi_idKet);	
				}else{
					if(gudang.trans.jenis==4){
						if(trans.paid==null)trans.paid=0;
						if(trans.pay_mode==1&trans.paid!=1){
							trans.no_trans=null;
							errors.rejectValue("trans_no", null, "Mohon lunasi terlebih dahulu pembayaran Anda");
						}
					}
				}
								
			}else{
				errors.rejectValue("trans_no", null, "Maaf No Transaksi tidak ditemukan");	
			}
			
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

			return "trans/gudang_edit";
		}

		//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save)
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{
			
			pesan = dbService.saveGudang(gudang, currentUser);
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

			return "trans/gudang_edit";
		}
		ra.addFlashAttribute("messageType", "done");
		ra.addFlashAttribute("message", pesan);
		ra.addFlashAttribute("pesan", pesan);
		
		return "redirect:/gudang/"+gudang.jenistrans;

		

	}
	
	
	
	

}
