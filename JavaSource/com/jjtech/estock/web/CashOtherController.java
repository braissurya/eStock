package com.jjtech.estock.web;

import java.io.IOException;
import java.util.ArrayList;
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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jjtech.estock.model.Payment;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.TransDet;
import com.jjtech.estock.model.Trx;
import com.jjtech.estock.model.TrxDet;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;

@Controller
@RequestMapping("/keuangan/InputTransaksi")
public class CashOtherController extends ParentController {
	
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
		map.put("lsAccount", dbService.selectDropDown("a.id", "concat(b.nama,' [',a.no_rek,']')", "lst_account a, lst_bank b", "a.id_bank = b.id and a.active = 1", "a.id, b.nama"));
		map.put("lsCaraBayar", dbService.selectDropDown("jenis", "keterangan", "lst_config", "id=7 and active =1", "jenis"));
		map.put("lsCashFlow", dbService.selectDropDown("id", "keterangan", "lst_cash_flow", null, "id"));
		return map;
	}
	
	@RequestMapping(value="/{posisi_id}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model, @PathVariable Integer posisi_id, HttpServletRequest request) {
		logger.debug("Halaman: Kas, method: SHOW");
		String pagename="";
		pagename="Input Transaksi";
		if(posisi_id==1){
			pagename="Input Transaksi";
		}else if(posisi_id==2){
			pagename="Jurnal Transaksi";
		}else if(posisi_id==9){
			pagename="Filling Transaksi";
		}else{
			throw new RuntimeException ("Page not found");
		}
		Utils.generatePaging(dbService, request, model, "paymentOther", posisi_id);
		model.addAttribute("pagename",pagename );
		model.addAttribute("posisi_id",posisi_id );
		return "keuangan/cash_other_list";
		
	}
	
	//input baru
	@RequestMapping(value="/{posisi_id}/new", method=RequestMethod.GET)
	public String insert(HttpServletRequest request,@ModelAttribute("trx") Trx trx, @PathVariable Integer posisi_id, Model model) {
		logger.debug("Halaman: Kas, method: NEW");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Date sysdate=dbService.selectSysdate();
		trx.setMode("NEW");
		trx.createdate=sysdate;
		if(posisi_id==1){
			trx.jenispayment="Input Transaksi";
			trx.jenispay="Input Transaksi";
			trx.pagename="Input Transaksi";
		}else if(posisi_id==2){
			trx.jenispayment="Jurnal Transaksi";
			trx.jenispay="Jurnal Transaksi";
			trx.pagename="Jurnal Transaksi";
		}else if(posisi_id==9){
			trx.jenispayment="Filling Transaksi";
			trx.jenispay="Filling Transaksi";
			trx.pagename="Filling Transaksi";
		}else{
			throw new RuntimeException ("Page not found");
		}
		
		return "keuangan/cash_other_edit";
	}
	
	//edit data
	@RequestMapping(value="/{trx_id}/{posisi_id}/edit", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(HttpServletRequest request,@ModelAttribute("trx") Trx trx,  Model model, @PathVariable Integer trx_id, @PathVariable Integer posisi_id) { 
		logger.debug("Halaman: Kas, method: Edit");
		
		Trx tmp=dbService.selectListTrx(trx_id,posisi_id).get(0);
		tmp.lsTrxDet=dbService.selectListTrxDet(trx_id);
		BeanUtils.copyProperties(tmp, trx);
		trx.setMode("EDIT");
		if(posisi_id==1){
			trx.jenispayment="Input Transaksi";
			trx.jenispay="Input Transaksi";
			trx.pagename="Input Transaksi";
		}else if(posisi_id==2){
			trx.jenispayment="Jurnal Transaksi";
			trx.jenispay="Jurnal Transaksi";
			trx.pagename="Jurnal Transaksi";
		}else if(posisi_id==9){
			trx.jenispayment="Filling Transaksi";
			trx.jenispay="Filling Transaksi";
			trx.pagename="Filling Transaksi";
		}else{
			throw new RuntimeException ("Page not found");
		}
		return "keuangan/cash_other_edit";
	}
	
	@RequestMapping(value="/{trx_id}/{posisi_id}/view", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(HttpServletRequest request,@ModelAttribute("trx") Trx trx,  Model model, @PathVariable Integer trx_id, @PathVariable Integer posisi_id) { 
		logger.debug("Halaman: Kas, method: Edit");
		
		Trx tmp=dbService.selectListTrx(trx_id,posisi_id).get(0);
		tmp.lsTrxDet=dbService.selectListTrxDet(trx_id);
		BeanUtils.copyProperties(tmp, trx);
		trx.setMode("VIEW");
		if(posisi_id==1){
			trx.jenispayment="Input Transaksi";
			trx.jenispay="Input Transaksi";
			trx.pagename="Input Transaksi";
		}else if(posisi_id==2){
			trx.jenispayment="Jurnal Transaksi";
			trx.jenispay="Jurnal Transaksi";
			trx.pagename="Jurnal Transaksi";
		}else if(posisi_id==9){
			trx.jenispayment="Filling Transaksi";
			trx.jenispay="Filling Transaksi";
			trx.pagename="Filling Transaksi";
		}else{
			throw new RuntimeException ("Page not found");
		}
		return "keuangan/cash_other_edit";
	}
	
	@RequestMapping(value="/{posisi_id}/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("trx") Trx trx, @PathVariable Integer posisi_id, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Transaksi, method: SAVE");
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tgl_rk",  "NotEmpty", new String[]{""},null);
			//validasi apabila jurnal tidak balance saat di posisi jurnal, harus dimasukkan coa balance nya terlebih dahulu.
			int i=0;
			Double totalpremiDebet = 0.;
			Double totalpremiKredit = 0.;
//			payment.trans_id=dbService.selectListTrans(null, null, null, payment.no_trans).get(0).trans_id;
//			if(dbService.selectListPayment(null, null, payment.no_trans).size()>0){
//				errors.rejectValue("no_trans", null, "Pembayaran untuk no Transaksi "+payment.no_trans+" sudah pernah diinput.");
//			}
			int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");
			if(posisi_id>1){
				if (idxList.length<2) {
					errors.rejectValue("lsTrxDet", null, "Minimal 2 COA diinput");
				}
			}
			List<TrxDet> lsTrxDet=new ArrayList<TrxDet>();
			for(int idx:idxList){
				TrxDet trxDet = new TrxDet();
				trxDet.urut=idx;
				trxDet.coa_id=ServletRequestUtils.getStringParameter(request, "coa_id_"+idx,"");
				trxDet.ket=ServletRequestUtils.getStringParameter(request, "ket_"+idx,"");
				trxDet.jumlahDebet=new Double(ServletRequestUtils.getStringParameter(request, "jumlahDebet_"+idx,"0").replace(",", "").replace("Rp ", ""));
				trxDet.jumlahKredit=new Double(ServletRequestUtils.getStringParameter(request, "jumlahKredit_"+idx,"0").replace(",", "").replace("Rp ", ""));
				totalpremiDebet+=trxDet.jumlahDebet;
				totalpremiKredit+=trxDet.jumlahKredit;
				lsTrxDet.add(trxDet);
			}
			trx.lsTrxDet=lsTrxDet;
			if(totalpremiDebet.compareTo(totalpremiKredit)!=0){
				errors.rejectValue("trxDet.jumlahDebet", null, "Total Debet dan Kredit tidak Balance.");
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

			return "keuangan/cash_other_edit";
		}
		
		String pesan ="";
		try{
			pesan = dbService.saveTrx(trx, currentUser);
			if(pesan.contains("habis")){
					ra.addFlashAttribute("messageType", "error");
					ra.addFlashAttribute("message", pesan);
//					return "redirect:/transaksi/"+trans.jenistrans+"/"+trans.pagename+"/"+trans.trans_id+"/edit";
					return "redirect:/keuangan/InputTransaksi/"+"/"+trx.trx_id+"/"+posisi_id+"/edit";

			}
			//balikin ke layar list input
		}catch (Exception e) {
			pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi","","diproses"},null);
			e.printStackTrace();
			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock", Utils.errorExtract(e), null);
		}
		ra.addFlashAttribute("messageType", "done");
		ra.addFlashAttribute("message", pesan);

		return "redirect:/keuangan/InputTransaksi/"+trx.trx_id+"/"+posisi_id+"/edit";
		
	}
	@RequestMapping(value="/transfer/{trx_id}/{posisi_id}/{modename}", method=RequestMethod.GET)
	public String transfer( RedirectAttributes ra, HttpServletRequest request,HttpServletResponse response,@PathVariable Integer trx_id, @PathVariable Integer posisi_id, @PathVariable String modename) throws MailException, MessagingException {
		logger.debug("Halaman: Micro Transfer, method: transfer");
		String pesan ="";
		String  redirectTo=request.getContextPath()+"/keuangan/InputTransaksi/"+trx_id+"/"+posisi_id+"/"+"edit";
		
		if(modename.equals("show")){
			redirectTo=request.getContextPath()+"/keuangan/InputTransaksi/"+posisi_id;
		}else{
			redirectTo=request.getContextPath()+"/keuangan/InputTransaksi/"+posisi_id; //+"/"+"new";
		}
		
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Trx trx=dbService.selectListTrx(trx_id, posisi_id).get(0);
			trx.lsTrxDet=dbService.selectListTrxDet(trx_id);
			trx.setMode("TRANSFER");
			if(posisi_id==1){
				trx.jenispayment="Input Transaksi";
				trx.jenispay="Input Transaksi";
				trx.pagename="Input Transaksi";
			}else if(posisi_id==2){
				trx.jenispayment="Jurnal Transaksi";
				trx.jenispay="Jurnal Transaksi";
				trx.pagename="Jurnal Transaksi";
			}else if(posisi_id==9){
				trx.jenispayment="Filling Transaksi";
				trx.jenispay="Filling Transaksi";
				trx.pagename="Filling Transaksi";
			}else{
				throw new RuntimeException ("Page not found");
			}
			
			pesan=dbService.saveTrx(trx, currentUser);
			if(pesan.contains("gagal")){
				if(modename.equals("show")){
					redirectTo=request.getContextPath()+"/keuangan/InputTransaksi"+posisi_id;
				}else{
					redirectTo=request.getContextPath()+"/keuangan/InputTransaksi/"+posisi_id+"new";
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			pesan="gagal di transfer";
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