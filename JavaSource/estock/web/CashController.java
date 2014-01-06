package com.jjtech.estock.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jjtech.estock.model.Payment;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;

@Controller
@RequestMapping("/keuangan/Cash")
public class CashController extends ParentController {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn format dd-MM-yyyy
		binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
		binder.registerCustomEditor(Integer.class,new CustomNumberEditor(Integer.class, true));
	}
	
	@ModelAttribute("reff")
	public Map<String, Object> reff(HttpSession session){
		Map<String, Object> map = new HashMap<String, Object>();
		User currentUser = (User) session.getAttribute("currentUser");
//		map.put("lsBank", dbService.selectDropDown("id", "nama", "lst_bank", "active =1", "nama"));
		map.put("lsAccount", dbService.selectDropDown("a.id", "concat(b.nama,' [',a.no_rek,']')", "lst_account a, lst_bank b", "a.id_bank = b.id and a.active = 1", "b.nama"));
		map.put("lsCaraBayar", dbService.selectDropDown("jenis", "keterangan", "lst_config", "id=7 and active =1", "jenis"));
		return map;
	}
	
	@RequestMapping(value="/{jenispayment}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,@PathVariable String jenispayment, HttpServletRequest request) {
		logger.debug("Halaman: Kas, method: SHOW");
		Integer uname=0;
		String pagename="";
		if(jenispayment.toLowerCase().equals("in")){
			pagename="Penerimaan Keuangan";
			uname=0;
		}else if(jenispayment.toLowerCase().equals("out")){
			pagename="Pengeluaran Keuangan";
			uname=1;
		}else if(jenispayment.toLowerCase().equals("inlisttrans")){
			pagename="Penjualan";
			uname=2;
		}else if(jenispayment.toLowerCase().equals("outlisttrans")){
			pagename="Pembelian";
			uname=3;
		}else {
			throw new RuntimeException ("Page not found");
		}
		Utils.generatePaging(dbService, request, model, "payment", uname);
		model.addAttribute("pagename",pagename );
		return "keuangan/cash_list";
		
	}
	
	//input baru
	@RequestMapping(value="/{jenispayment}/new", method=RequestMethod.GET)
	public String insert(HttpServletRequest request,@ModelAttribute("payment") Payment payment,Model model,@PathVariable String jenispayment) {
		logger.debug("Halaman: Kas, method: NEW");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		payment.setMode("NEW");

		Date sysdate=dbService.selectSysdate();
		payment.createdate=sysdate;
		payment.jenispayment=jenispayment;
		if(jenispayment.toLowerCase().equals("in")){
			payment.jenispay="Penerimaan";
			payment.pagename="Penerimaan Keuangan";
			payment.dk="K";
		}else if(jenispayment.toLowerCase().equals("out")){
			payment.jenispay="Pengeluaran";
			payment.pagename="Pengeluaran Keuangan";
			payment.dk="D";
		}else{
			throw new RuntimeException ("Page not found");
		}
		//flag_jenis untuk menentukan apakah penerimaan/pengeluaran kas dari transaksi atau lain-lain.(bila trans_id ada, 1 transaksi. bila trans_id null, 0 lain-lain)
		payment.flag_jenis=1;
		return "keuangan/cash_edit";
	}
	
	//edit data
	@RequestMapping(value="/{jenispayment}/{payment_id}/edit", method=RequestMethod.GET) 
	public String update(HttpServletRequest request,@ModelAttribute("payment") Payment payment,  Model model,@PathVariable String jenispayment,@PathVariable Integer payment_id) { 
		logger.debug("Halaman: Kas, method: Edit");
		
		Payment tmp=dbService.selectListPayment(payment_id, null, null).get(0);
		tmp.no_trans=dbService.selectListTrans(null, null, tmp.trans_id, null,null).get(0).getNo_trans();
		BeanUtils.copyProperties(tmp, payment);
		payment.setMode("EDIT");
		payment.jenispayment=jenispayment;
		if(jenispayment.toLowerCase().equals("in")){
			payment.jenispay="Penerimaan";
			payment.pagename="Penerimaan Keuangan";
			payment.dk="K";
		}else if(jenispayment.toLowerCase().equals("out")){
			payment.jenispay="Pengeluaran";
			payment.pagename="Pengeluaran Keuangan";
			payment.dk="D";
		}else{
			throw new RuntimeException ("Page not found");
		}
		return "keuangan/cash_edit";
	}
	
	@RequestMapping(value="/{jenispayment}/{payment_id}/view", method=RequestMethod.GET)
	public String view(HttpServletRequest request,@ModelAttribute("payment") Payment payment,  Model model,@PathVariable String jenispayment, @PathVariable Integer payment_id) {
		logger.debug("Halaman: Kas, method: View");
		
		Payment tmp=dbService.selectListPayment(payment_id, null, null).get(0);
		tmp.no_trans=dbService.selectListTrans(null, null, tmp.trans_id, null,null).get(0).getNo_trans();
		BeanUtils.copyProperties(tmp, payment);
		payment.setMode("VIEW");
		payment.jenispayment=jenispayment;
		if(jenispayment.toLowerCase().equals("in")){
			payment.jenispay="Penerimaan";
			payment.pagename="Penerimaan Keuangan";
			payment.dk="K";
		}else if(jenispayment.toLowerCase().equals("out")){
			payment.jenispay="Pengeluaran";
			payment.pagename="Pengeluaran Keuangan";
			payment.dk="D";
		}else{
			throw new RuntimeException ("Page not found");
		}

		return "keuangan/cash_edit";
	}
	
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("payment") Payment payment, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Kas, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nominal",  "NotEmpty", new String[]{""},null);
//			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "keterangan",  "NotEmpty", new String[]{""},null);
			if(payment.flag_jenis==1){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "no_trans",  "NotEmpty", new String[]{""},null);
			}
			if(payment.cara_bayar==3){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "no_giro",  "NotEmpty", new String[]{""},null);
			}
			if(payment.cara_bayar==1 && payment.account_id!=0){
				errors.rejectValue("account_id",null , "Silakan memilih Non Bank apabila Cara Bayarnya Tunai.");
			}
			if(payment.cara_bayar!=1 && payment.account_id==0){
				errors.rejectValue("cara_bayar",null , "Silakan memilih Bank Terlebih dahulu apabila Cara Bayarnya Non Tunai.");
			}
			
			List<Trans> Trans = dbService.selectListTransAuto(null, payment.no_trans, payment.jenispayment.toLowerCase().equals("in")?4:3, 2);
			if(Trans.size()==0){
				errors.rejectValue("no_trans",null , "No Transaksi yang dimasukkan tidak ada.");
			}else{
				//jika nilai sum yg dibayar untuk 1 no_transaksi sudah melebihi total_harga, tidak bisa diinput kembali (munculkan sisa berapa yg bisa diinput untuk pembayarannya)
				Double total_harga = Trans.get(0).getTotal_harga();
				List<Payment> listPayment = dbService.selectListPayment(null, null, dbService.selectListTrans(null, null, null, payment.no_trans,null).get(0).trans_id);
				Double sum_bayar = 0.;
				for(Payment pay : listPayment){
					sum_bayar += pay.nominal;
				}
				Double sisa_bayar = total_harga - sum_bayar;
				if( sum_bayar+payment.nominal > total_harga ){
					errors.rejectValue("nominal",null , "Jumlah nominal > Total Harga Barang yg dibeli. Sisa sebesar : Rp."+Utils.formatNumber("#,##0",sisa_bayar) );
				}
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

			return "keuangan/cash_edit";
		}
		payment.trans_id=dbService.selectListTrans(null, null, null, payment.no_trans,null).get(0).trans_id;		
		String pesan ="";
		try{
			pesan = dbService.savePayment(payment, currentUser);
			if(pesan.contains("habis")){
					ra.addFlashAttribute("messageType", "error");
					ra.addFlashAttribute("message", pesan);
//					return "redirect:/transaksi/"+trans.jenistrans+"/"+trans.pagename+"/"+trans.trans_id+"/edit";
					return "redirect:/keuangan/Cash/"+payment.jenispayment+"/"+payment.payment_id+"/edit";

			}
			//balikin ke layar list input
		}catch (Exception e) {
			pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+payment.pagename,"","diproses"},null);
			e.printStackTrace();
			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock", Utils.errorExtract(e), null);
		}
		ra.addFlashAttribute("messageType", "done");
		ra.addFlashAttribute("message", pesan);

		return "redirect:/keuangan/Cash/"+payment.jenispayment+"/"+payment.payment_id+"/edit";
		
	}
	@RequestMapping(value="/transfer/{jenispayment}/{payment_id}/{modename}", method=RequestMethod.GET)
	public String transfer( RedirectAttributes ra, HttpServletRequest request,HttpServletResponse response,@PathVariable String jenispayment,@PathVariable Integer payment_id, @PathVariable String modename) throws MailException, MessagingException {
		logger.debug("Halaman: Micro Transfer, method: transfer");
		String pesan ="";
		String  redirectTo=request.getContextPath()+"/keuangan/Cash/"+jenispayment+"/"+payment_id+"/"+"edit";
		
		if(modename.equals("show")){
			redirectTo=request.getContextPath()+"/keuangan/Cash/"+jenispayment;
		}else{
			redirectTo=request.getContextPath()+"/keuangan/Cash/"+jenispayment+"/new";
		}
		
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Payment payment = dbService.selectListPayment(payment_id, null, null).get(0);
			payment.setMode("TRANSFER");
			if(jenispayment.toLowerCase().equals("in")){
				payment.jenispay="Penerimaan";
				payment.pagename="Penerimaan Keuangan";
				payment.dk="K";
			}else if(jenispayment.toLowerCase().equals("out")){
				payment.jenispay="Pengeluaran";
				payment.pagename="Pengeluaran Keuangan";
				payment.dk="D";
			}else{
				throw new RuntimeException ("Page not found");
			}
			
			payment_id=payment.payment_id;
			pesan=dbService.savePayment(payment, currentUser);

			if(pesan.contains("gagal")){
				if(modename.equals("show")){
					redirectTo=request.getContextPath()+"/keuangan/Cash/"+jenispayment;
				}else{
					redirectTo=request.getContextPath()+"/keuangan/Cash/"+jenispayment+"/new";
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
			pesan="Polis gagal di transfer";
		}

		try {
			ServletOutputStream out;
			out = response.getOutputStream();
			out.println("<script>alert('"+pesan+"');");
			out.println("window.location=\""+redirectTo+"\";</script>");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	
	}
	
}