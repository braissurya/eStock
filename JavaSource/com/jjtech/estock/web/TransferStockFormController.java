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
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jjtech.estock.model.Opname;
import com.jjtech.estock.model.Stock;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.TransDet;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;

@Controller
@RequestMapping("/transaksi/Transferstock")
public class TransferStockFormController extends ParentController {
	
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
		map.put("lsCabangReq", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1 and id !="+currentUser.cabang_id, "nama"));
		map.put("lsGudang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1 and id ="+currentUser.cabang_id, "nama"));
		map.put("lsDriver", dbService.selectDropDown("id", "concat(nik,' - ',nama)", "mst_karyawan", "active = 1 and jenis=2", "concat(nik,' - ',nama)"));
		return map;
	}
	
	@RequestMapping(value="/{posisi_id}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,@PathVariable Integer posisi_id, HttpServletRequest request) {
		logger.debug("Halaman: Transfer Stock, method: SHOW");
		Utils.generatePaging(dbService, request, model, "transferstock", posisi_id);
		String pagename="Transfer Stock";
		if(posisi_id == 1){
			pagename= "Permintaan Transfer Stock";
		}else if(posisi_id ==2){
			pagename= "Gudang Transfer Stock";
		}else if(posisi_id ==3){
			pagename= "Penerimaan Transfer Stock";
		}
		model.addAttribute("pagename",pagename);
		model.addAttribute("posisi_id",posisi_id);
		return "transferstock/transfer_list";
	}
	
	//input baru
	@RequestMapping(value="/new/{posisi_id}", method=RequestMethod.GET)
	public String insert(HttpServletRequest request,@ModelAttribute("trans") Trans trans,@PathVariable Integer posisi_id, Model model) {
		logger.debug("Halaman: Transfer Stock, method: NEW");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		trans.setMode("NEW");
		trans.jenis=7;
		trans.posisi_id=posisi_id;
		trans.retail_id=currentUser.getCabang_id();
		trans.tgl_order=dbService.selectSysdate();
		trans.jenistrans="transferstock";
		//cek apakah transaksi masuk ke periode closing
		model.addAttribute("closingWarning", dbService.checkTglClossing(1,currentUser));
		return "transferstock/transfer_edit";
	}
	
	@RequestMapping(value="/edit/{trans_id}/{posisi_id}", method=RequestMethod.GET) 
	public String update(@ModelAttribute("trans") Trans trans, @PathVariable Integer trans_id, @PathVariable Integer posisi_id, Model model, HttpSession session) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transfer Stock, method: EDIT");
		User currentUser=(User) session.getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		
		Trans tmp=dbService.selectListTrans(7, posisi_id, trans_id, null,aksescabang_id).get(0);
		BeanUtils.copyProperties(tmp, trans);
		trans.setMode("EDIT");
		trans.jenistrans="transferstock";
		trans.lsTransDet=dbService.selectListTransDet(trans_id, null, null,null, null);
		//cek apakah transaksi masuk ke periode closing
		model.addAttribute("closingWarning", dbService.checkTglClossing(1,currentUser));
		return "transferstock/transfer_edit";
	}
	
	//view data
	@RequestMapping(value="/view/{trans_id}/{posisi_id}", method=RequestMethod.GET) 
	public String view(@ModelAttribute("trans") Trans trans, @PathVariable Integer trans_id, @PathVariable Integer posisi_id, Model model, HttpSession session) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transfer Stock, method: View");
		User currentUser = (User) session.getAttribute("currentUser");
		Trans tmp=dbService.selectListTrans(7, posisi_id, trans_id, null,null).get(0);
		BeanUtils.copyProperties(tmp, trans);
		trans.setMode("VIEW");
		trans.jenistrans="transferstock";
		trans.lsTransDet=dbService.selectListTransDet(trans_id, null, null,null, null);
		return "transferstock/transfer_edit";
	}
	
	@RequestMapping(value="/save/{posisi_id}", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("trans") Trans trans, @PathVariable Integer posisi_id, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Transfer Stock, method: SAVE");
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			if (!result.hasErrors()) {
				int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");
				if (idxList.length==0) {
					errors.rejectValue("lsTransDet", null, "Minimal 1 Produk diinput");
				}
				if(posisi_id==1){
				if(trans.tgl_order==null){
					errors.rejectValue("tgl_order", null, "harus diisi.");
				}
				}else if(posisi_id==2){
					if(trans.tgl_kirim==null){
						errors.rejectValue("tgl_kirim", null, "harus diisi.");
					}
					if(trans.driver_id==null){
						errors.rejectValue("driver_id", null, "harus diisi.");
					}
				}else if(posisi_id==3){
					if(trans.receiveddate==null){
						errors.rejectValue("receiveddate", null, "harus diisi.");
					}
				}
				
				List<TransDet> lsTransDets=new ArrayList<TransDet>();
				for(int idx:idxList){
					TransDet transDet=new TransDet();
					transDet.urut=idx;
					transDet.item_id=ServletRequestUtils.getIntParameter(request, "item_id_"+idx,0);
					transDet.qty=Integer.parseInt(ServletRequestUtils.getStringParameter(request, "qty_"+idx,"1").replace(",", "").replace("Rp ", ""));
					transDet.satuan_idKet=ServletRequestUtils.getStringParameter(request, "satuan_"+idx,"0");
					transDet.stock=Integer.parseInt(ServletRequestUtils.getStringParameter(request, "stock_"+idx,"0").replace(",", "").replace("Rp ", ""));
					
					transDet.item_idKet=ServletRequestUtils.getStringParameter(request, "nama_"+idx,"");
					transDet.barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode_ext_"+idx,"");
					lsTransDets.add(transDet);
					//cek apabila stock tidak ada, tidak bisa input permintaan transfer stock
					Date closingStockPeriode=dbService.selectClosingPeriode(1,currentUser.getCabang_id());
					List<Stock> lsStock=dbService.selectListStockItem(transDet.barcode_ext, currentUser.getCabang_id(), closingStockPeriode);
					if(lsStock.size()<=0){
						errors.rejectValue("item_id", null, "Item "+transDet.item_idKet+" tidak ada stock.");
					}else{
						Integer ready_stock = (lsStock.get(0).saldo_awal + lsStock.get(0).masuk - lsStock.get(0).keluar);
						if(transDet.qty > ready_stock ){
							errors.rejectValue("lsTransDet", null, "Permintaan Transfer Stock untuk item "+transDet.item_idKet+" melebihi quota yang ada.( Ready Stock :"+ready_stock+")");
						}
					}
				}
				trans.flag_ecer=1;
				trans.dk="O";
				trans.posisi_id=posisi_id;
				trans.lsTransDet=lsTransDets;
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
			
			return "transferstock/transfer_edit";
		}
		
		String pesan ="";
		try{
			pesan = dbService.saveTrans(trans, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans,"","diproses"},null);
			e.printStackTrace();
			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock", Utils.errorExtract(e), null);
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/transaksi/Transferstock"+"/"+posisi_id; 
	}
	
	//delete data
	@RequestMapping(value="delete/{trans_id}", method=RequestMethod.GET)
	public String delete(@ModelAttribute("trans") Trans trans, @PathVariable Integer trans_id, Model model, HttpSession session, RedirectAttributes ra) { 
		logger.debug("Halaman: Transfer Stock, method: DELETE");
		User currentUser = (User) session.getAttribute("currentUser");
		trans.setMode("DELETE");
		String pesan ="";
		try{
			pesan = dbService.saveTrans(trans, currentUser);
			//balikin ke layar list input
		}catch (Exception e) {
			e.printStackTrace();
			pesan=messageSource.getMessage("submitfailed", new String[]{"Transfer Stock"," ","gagal Diproses"},null);
				email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock modul Transfer Stock", Utils.errorExtract(e), null);
		}
		ra.addFlashAttribute("pesan", pesan);
		return "redirect:/transaksi/Transferstock";
	}
	
	@RequestMapping(value="/transfer/{trans_id}/{posisi_id}/{modename}", method=RequestMethod.GET)
	public String transfer( RedirectAttributes ra, HttpServletRequest request,HttpServletResponse response,@PathVariable Integer trans_id,@PathVariable Integer posisi_id, @PathVariable String modename) throws MailException, MessagingException {
		logger.debug("Halaman: Micro Transfer, method: transfer");
		String pesan ="";
		String  redirectTo=request.getContextPath()+"/transaksi/Transferstock/"+trans_id+"/"+posisi_id;
		
		if(modename.equals("show")){
			redirectTo=request.getContextPath()+"/transaksi/Transferstock/"+posisi_id;
		}else{
			redirectTo=request.getContextPath()+"/transaksi/Transferstock/"+"/new/"+posisi_id;
		}
		
		try{
			//currently logged in user
			User currentUser = (User) request.getSession(false).getAttribute("currentUser");
			Trans trans=dbService.selectListTrans(null, null, trans_id, null, null).get(0);
			trans.setMode("TRANSFER");
			trans.setPosisi_id(posisi_id);
			trans_id=trans.trans_id;
			trans.setLsTransDet(dbService.selectListTransDet(trans_id, null, null, null, null));
			
			pesan=dbService.saveTrans(trans, currentUser);

			if(pesan.contains("gagal")){
				if(modename.equals("show")){
					redirectTo=request.getContextPath()+"/transaksi/Transferstock/"+posisi_id;
				}else{
					redirectTo=request.getContextPath()+"/transaksi/Transferstock/"+"/new/"+posisi_id;
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

//Transferstock