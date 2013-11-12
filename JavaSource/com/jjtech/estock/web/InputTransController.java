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

import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.TransDet;
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
@RequestMapping("/transaksi")
public class InputTransController extends ParentController {

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

		map.put("lsCabang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1", "nama"));
		map.put("lsGudang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1 and jenis=2", "nama"));
		map.put("lsPayMode", dbService.selectDropDown("jenis", "keterangan", "lst_config", "active = 1 and id=8", "keterangan"));
		map.put("lsReturType", dbService.selectDropDown("jenis", "keterangan", "lst_config", "active = 1 and id=13", "keterangan"));
		
		return map;
	}


	@RequestMapping(value="/{jenistrans}/{pagename}",method={RequestMethod.GET, RequestMethod.POST})
	public String show(Model model,HttpServletRequest request,@PathVariable String jenistrans,@PathVariable String pagename) {
		logger.debug("Halaman: Transaksi, method: SHOW");
		
		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="no_trans",sort_type=null,no_trans=null;
		
		Integer jenis=null,posisi_id=null;

		if(jenistrans.toLowerCase().equals("penjualan")){
			if(pagename.toLowerCase().equals("order")){
				jenis=2;
			}else if(pagename.toLowerCase().equals("ordertransfer")){
				jenis=2;
				posisi_id=2;
			}else if(pagename.toLowerCase().equals("input")){
				jenis=4;
				posisi_id=2;
			}else if(pagename.toLowerCase().equals("retur")){
				jenis=6;
				posisi_id=1;
			}else if(pagename.toLowerCase().equals("delivery")){
				//jenis=6;
				sort="tgl_kirim_est";
				posisi_id=4;
			}else if(pagename.toLowerCase().equals("gudang")){
				jenis=4;
				posisi_id=5;
			}else{
				throw new RuntimeException ("Page not found");
			}
		}else if(jenistrans.toLowerCase().equals("pembelian")){
			if(pagename.toLowerCase().equals("order")){
				jenis=1;
				posisi_id=2;
			}else if(pagename.toLowerCase().equals("ordertransfer")){
				jenis=1;
				posisi_id=2;
			}else if(pagename.toLowerCase().equals("input")){
				jenis=3;
				posisi_id=2;
			}else if(pagename.toLowerCase().equals("retur")){
				jenis=5;
				posisi_id=1;
			}else if(pagename.toLowerCase().equals("gudang")){
				jenis=3;
				posisi_id=5;
			}else{
				throw new RuntimeException ("Page not found");
			}
		}else{
			throw new RuntimeException ("Page not found");
		}

		List<Trans> listPaging = null;

		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;

		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		no_trans=ServletRequestUtils.getStringParameter(request, "nt", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "nt", "");
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);

		totalData=dbService.selectListTransPagingCount(search, jenis, posisi_id, null, no_trans,aksescabang_id);

		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X

		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)

		if(offset<0)offset=0;

		listPaging=dbService.selectListTransPaging(search, offset, rowcount, sort, sort_type, jenis, posisi_id, null, no_trans,aksescabang_id);

		if(no_trans!=null&&!listPaging.isEmpty()&&pagename.toLowerCase().equals("ordertransfer")){
			model.addAttribute("trans_id", listPaging.get(0).trans_id);
		}else if(no_trans!=null&&!listPaging.isEmpty()){
			model.addAttribute("trans_idnya", listPaging.get(0).trans_id);
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

		model.addAttribute("jenistrans",jenistrans);
		model.addAttribute("pagename",pagename );




		return "trans/trans_list";
	}
	
	
	

	//input baru
	@RequestMapping(value="/{jenistrans}/{pagename}/new", method=RequestMethod.GET)
	public String insert(HttpServletRequest request,@ModelAttribute("trans") Trans trans,Model model,@PathVariable String jenistrans,@PathVariable String pagename) {
		logger.debug("Halaman: Transaksi, method: NEW");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		trans.setMode("NEW");

		Date sysdate=dbService.selectSysdate();
		if(jenistrans.toLowerCase().equals("penjualan")){
			if(pagename.toLowerCase().equals("order")){
				trans.tgl_order=sysdate;
			}else if(pagename.toLowerCase().equals("input")){
				trans.trans_date=sysdate;
			}else if(pagename.toLowerCase().equals("retur")){
				trans.trans_date=sysdate;
			}else{
				throw new RuntimeException ("Page not found");
			}
		}else if(jenistrans.toLowerCase().equals("pembelian")){
			if(pagename.toLowerCase().equals("order")){
				trans.tgl_order=sysdate;
			}else if(pagename.toLowerCase().equals("input")){
				trans.trans_date=sysdate;
			}else if(pagename.toLowerCase().equals("retur")){
				trans.trans_date=sysdate;
			}else{
				throw new RuntimeException ("Page not found");
			}
		}else{
			throw new RuntimeException ("Page not found");
		}

		trans.retail_id=currentUser.cabang_id;
		trans.retail_idKet=currentUser.namaCabang;
		trans.pagename=pagename;
		trans.jenistrans=jenistrans;
		trans.flag_ecer=1;
		trans.pay_mode=1;
		trans.flag_pajak=0;
		trans.retail_id=currentUser.getCabang_id();
		trans.flag_akses_all=currentUser.getFlag_akses_all();
		
		//cek apakah transaksi masuk ke periode closing
		model.addAttribute("closingWarning", dbService.checkTglClossing(1,currentUser));
		
		return "trans/trans_edit";
	}

	//edit data
	@RequestMapping(value="/{jenistrans}/{pagename}/{trans_id}/edit", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String update(HttpServletRequest request,@ModelAttribute("trans") Trans trans,  Model model,@PathVariable String jenistrans,@PathVariable String pagename,@PathVariable Integer trans_id) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transaksi, method: Edit");
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		
		Trans tmp=dbService.selectListTrans(null, null, trans_id, null,aksescabang_id).get(0);
		BeanUtils.copyProperties(tmp, trans);
		trans.setMode("EDIT");

		Date sysdate=dbService.selectSysdate();
		if(jenistrans.toLowerCase().equals("penjualan")){
			if(pagename.toLowerCase().equals("order")){

			}else if(pagename.toLowerCase().equals("input")){
				if(trans.trans_date==null)trans.trans_date=sysdate;
			}else if(pagename.toLowerCase().equals("retur")){

			}else{
				throw new RuntimeException ("Page not found");
			}

			if(trans.customer_id!=null){
				trans.customer=dbService.selectListCustomer(trans.customer_id).get(0);
				if(trans.customer.limit_hutang==null)trans.customer.limit_hutang=0.0;
				if(trans.customer.limit_hutang!=0&&trans.pay_mode==2){
					trans.customer.totalHutang=dbService.selectHutangCustomer(trans.customer_id);
					if(trans.customer.totalHutang>trans.customer.limit_hutang){
						trans.customer.outoflimit=true;
					}
				}
			}
		}else if(jenistrans.toLowerCase().equals("pembelian")){
			if(pagename.toLowerCase().equals("order")){

			}else if(pagename.toLowerCase().equals("input")){
				if(trans.trans_date==null)trans.trans_date=sysdate;
			}else if(pagename.toLowerCase().equals("retur")){

			}else{
				throw new RuntimeException ("Page not found");
			}
			if(trans.supplier_id!=null)trans.supplier=dbService.selectListSupplier(trans.supplier_id).get(0);
		}else{
			throw new RuntimeException ("Page not found");
		}

		trans.pagename=pagename;
		trans.jenistrans=jenistrans;
		trans.lsTransDet=dbService.selectListTransDet(trans_id, null, null, null, null);
		if(trans.sales_id!=null)trans.karyawan=dbService.selectListKaryawan(trans.sales_id,null).get(0);



		trans.sub_total_harga=trans.total_harga+trans.total_disc-trans.ppn;
		trans.persen_disc=(trans.total_disc/trans.sub_total_harga)*100;
		trans.persen_ppn=(trans.ppn/(trans.sub_total_harga - trans.total_disc))*100;
		
		//cek apakah transaksi masuk ke periode closing
		model.addAttribute("closingWarning", dbService.checkTglClossing(1,currentUser));

		return "trans/trans_edit";
	}
	
	//edit data
	@RequestMapping(value="/{jenistrans}/{pagename}/{trans_id}/new", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String newFromOrder(HttpServletRequest request,HttpServletResponse response,@ModelAttribute("trans") Trans trans,  Model model,@PathVariable String jenistrans,@PathVariable String pagename,@PathVariable Integer trans_id) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transaksi, method: Edit");
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		
		Trans tmp=dbService.selectListTrans(null, null, trans_id, null,aksescabang_id).get(0);
		BeanUtils.copyProperties(tmp, trans);
		trans.setMode("NEW");

		Date sysdate=dbService.selectSysdate();
		if(jenistrans.toLowerCase().equals("penjualan")){
			if(pagename.toLowerCase().equals("order")){

			}else if(pagename.toLowerCase().equals("input")){
				if(trans.trans_date==null)trans.trans_date=sysdate;
			}else if(pagename.toLowerCase().equals("retur")){

			}else{
				throw new RuntimeException ("Page not found");
			}

			if(trans.customer_id!=null){
				trans.customer=dbService.selectListCustomer(trans.customer_id).get(0);
				if(trans.customer.limit_hutang==null)trans.customer.limit_hutang=0.0;
				if(trans.customer.limit_hutang!=0&&trans.pay_mode==2){
					trans.customer.totalHutang=dbService.selectHutangCustomer(trans.customer_id);
					if(trans.customer.totalHutang>trans.customer.limit_hutang){
						trans.customer.outoflimit=true;
					}
				}
			}
		}else if(jenistrans.toLowerCase().equals("pembelian")){
			if(pagename.toLowerCase().equals("order")){

			}else if(pagename.toLowerCase().equals("input")){
				if(trans.trans_date==null)trans.trans_date=sysdate;
			}else if(pagename.toLowerCase().equals("retur")){

			}else{
				throw new RuntimeException ("Page not found");
			}
			if(trans.supplier_id!=null)trans.supplier=dbService.selectListSupplier(trans.supplier_id).get(0);
		}else{
			throw new RuntimeException ("Page not found");
		}

		trans.pagename=pagename;
		trans.jenistrans=jenistrans;
		trans.lsTransDet=dbService.selectListTransDet(trans_id, null, null, null, null);
		
		if(trans.lsTransDet.isEmpty()){
			String pesan="Transaksi Order sudah tidak ada";
			String redirectTo=request.getContextPath()+"/transaksi/"+jenistrans+"/"+pagename+"/new";
			ServletOutputStream out;
			
			try {
				out = response.getOutputStream();
				out.println("<script>");
				out.println("alert('"+pesan+"');");
				out.println("window.location=\""+redirectTo+"\";</script>");
				out.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		if(trans.sales_id!=null)trans.karyawan=dbService.selectListKaryawan(trans.sales_id,null).get(0);


		trans.no_trans_ref=trans.no_trans;
		trans.no_trans=null;
		trans.sub_total_harga=trans.total_harga+trans.total_disc-trans.ppn;
		trans.persen_disc=(trans.total_disc/trans.sub_total_harga)*100;
		trans.persen_ppn=(trans.ppn/(trans.sub_total_harga - trans.total_disc))*100;
		
		//cek apakah transaksi masuk ke periode closing
		model.addAttribute("closingWarning", dbService.checkTglClossing(1,currentUser));

		return "trans/trans_edit";
	}

		//view data
	@RequestMapping(value="/{jenistrans}/{pagename}/{trans_id}/view", method=RequestMethod.GET) //mapping request GET saja ke method ini, menerima parameter "uname"
	public String view(HttpServletRequest request,@ModelAttribute("trans") Trans trans,  Model model,@PathVariable String jenistrans,@PathVariable String pagename,@PathVariable Integer trans_id) { //@PathVariable berarti parameter "uname" langsung di bind ke string "uname"
		logger.debug("Halaman: Transaksi, method: View");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		Trans tmp=dbService.selectListTrans(null, null, trans_id, null,aksescabang_id).get(0);
		BeanUtils.copyProperties(tmp, trans);
		trans.setMode("VIEW");

		if(jenistrans.toLowerCase().equals("penjualan")){
			if(pagename.toLowerCase().equals("order")){

			}else if(pagename.toLowerCase().equals("input")){

			}else if(pagename.toLowerCase().equals("retur")){

			}else{
				throw new RuntimeException ("Page not found");
			}

			if(trans.customer_id!=null)trans.customer=dbService.selectListCustomer(trans.customer_id).get(0);
		}else if(jenistrans.toLowerCase().equals("pembelian")){
			if(pagename.toLowerCase().equals("order")){

			}else if(pagename.toLowerCase().equals("input")){

			}else if(pagename.toLowerCase().equals("retur")){

			}else{
				throw new RuntimeException ("Page not found");
			}
			if(trans.supplier_id!=null)trans.supplier=dbService.selectListSupplier(trans.supplier_id).get(0);
		}else{
			throw new RuntimeException ("Page not found");
		}

		trans.pagename=pagename;
		trans.jenistrans=jenistrans;
		trans.lsTransDet=dbService.selectListTransDet(trans_id, null, null, null, null);
		if(trans.sales_id!=null)trans.karyawan=dbService.selectListKaryawan(trans.sales_id,null).get(0);



		trans.sub_total_harga=trans.total_harga+trans.total_disc-trans.ppn;
		trans.persen_disc=(trans.total_disc/trans.sub_total_harga)*100;
		trans.persen_ppn=(trans.ppn/(trans.sub_total_harga - trans.total_disc))*100;

		return "trans/trans_edit";
	}




	//saat user menekan tombol save baik dari layar input maupun layar edit
	@RequestMapping(value="/save", method=RequestMethod.POST) //mapping request POST saja ke method ini
	public String save(@Valid @ModelAttribute("trans") Trans trans, BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Master User, method: SAVE");

		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		
		int jenis=0;
		
		if(trans.jenistrans.toLowerCase().equals("penjualan")){
			jenis=4;
		}else if(trans.jenistrans.toLowerCase().equals("pembelian")){
			jenis=3;
		}

		//contoh bila validasi dilakukan langsung didalam controller (validasi tambahan, selain validasi standar yang sudah diset langsung di class User)
		if (!result.hasErrors()) {
			BindException errors = new BindException(result);
			if(trans.jenistrans.toLowerCase().equals("pembelian")&&!trans.pagename.toLowerCase().equals("retur")){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pay_mode",  "NotEmpty", new String[]{""},null);
				if(!trans.jenistrans.toLowerCase().equals("pembelian"))ValidationUtils.rejectIfEmptyOrWhitespace(errors, "flag_ecer",  "NotEmpty", new String[]{""},null);
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "supplier.nama",  "NotEmpty", new String[]{""},null);
			}else	if(trans.pagename.toLowerCase().equals("retur")){
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "no_trans_ref",  "NotEmpty", new String[]{""},null);
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "retur_type",  "NotEmpty", new String[]{""},null);
				
				if(!errors.hasErrors()){
					List<Trans> tmp=dbService.selectListTrans(jenis, null, null, trans.no_trans_ref,aksescabang_id);
					if(tmp.isEmpty()){
						errors.rejectValue("no_trans_ref", null, "tidak ditemukan");
					}
				}
			}else{
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customer.nama",  "NotEmpty", new String[]{""},null);
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "pay_mode",  "NotEmpty", new String[]{""},null);
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, "flag_ecer",  "NotEmpty", new String[]{""},null);
			}
			if(trans.flag_kirim!=null){
				if(trans.flag_kirim==1){
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "tgl_kirim_est",  "NotEmpty", new String[]{""},null);
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "contact_tujuan",  "NotEmpty", new String[]{""},null);
					ValidationUtils.rejectIfEmptyOrWhitespace(errors, "alamat_tujuan",  "NotEmpty", new String[]{""},null);
				}
			}
			
			
			int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");

			if (idxList.length==0) {
				errors.rejectValue("lsTransDet", null, "Minimal 1 Produk diinput");
			}
			trans.total_harga=0.0;
			trans.total_disc=new Double(ServletRequestUtils.getStringParameter(request, "total_disc","0").replace(",", "").replace("Rp ", ""));
			trans.persen_disc=new Double(ServletRequestUtils.getStringParameter(request, "persen_disc","0").replace(",", "").replace("Rp ", ""));
			trans.ppn=new Double(ServletRequestUtils.getStringParameter(request, "ppn","0").replace(",", "").replace("Rp ", ""));
			trans.persen_ppn=new Double(ServletRequestUtils.getStringParameter(request, "persen_ppn","0").replace(",", "").replace("Rp ", ""));
			trans.sub_total_harga=0.0;
			boolean isItemRetur=true;
			boolean isSizeRetur=true;
			String barcode="";
			boolean harga=true;
			boolean qtyIsi=true;
			List<TransDet> lsTransDets=new ArrayList<TransDet>();
			for(int idx:idxList){
				TransDet transDet=new TransDet();
				transDet.urut=idx;
				transDet.item_id=ServletRequestUtils.getIntParameter(request, "item_id_"+idx,0);
				transDet.qty=Integer.parseInt(ServletRequestUtils.getStringParameter(request, "qty_"+idx,"1").replace(",", "").replace("Rp ", ""));
				transDet.satuan_idKet=ServletRequestUtils.getStringParameter(request, "satuan_"+idx,"0");
				transDet.stock=Integer.parseInt(ServletRequestUtils.getStringParameter(request, "stock_"+idx,"0").replace(",", "").replace("Rp ", ""));
				transDet.harga= new Double(ServletRequestUtils.getStringParameter(request, "harga_"+idx,"0").replace(",", "").replace("Rp ", ""));
				transDet.jumlah_diskon= new Double(ServletRequestUtils.getStringParameter(request, "diskon_"+idx,"0").replace(",", "").replace("Rp ", ""));
				
				if(transDet.harga>0){
					transDet.persen_diskon=(transDet.jumlah_diskon/transDet.harga)*100;
				}else{
					harga=false;
					transDet.persen_diskon=0.0;
				}
				
				
				if(transDet.qty<=0){
					qtyIsi=false;
				}
				
				transDet.subTotal_harga= (transDet.harga-transDet.jumlah_diskon)*transDet.qty;
				transDet.subTotal_diskon= transDet.jumlah_diskon*transDet.qty;
				transDet.item_idKet=ServletRequestUtils.getStringParameter(request, "nama_"+idx,"");
				transDet.barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode_ext_"+idx,"");
				transDet.satuan_idKet=ServletRequestUtils.getStringParameter(request, "satuan_"+idx,"");
				lsTransDets.add(transDet);
				trans.sub_total_harga+=transDet.subTotal_harga;
				
				if(trans.pagename.toLowerCase().equals("retur")){
					Trans tmp=dbService.selectListTrans(jenis, null, null, trans.no_trans_ref,aksescabang_id).get(0);
					List<TransDet> ld=dbService.selectListTransDet(tmp.trans_id, null, null, transDet.item_id, null);
					if(ld.isEmpty()){
						isItemRetur=false;
						barcode+=" ["+transDet.barcode_ext+"] "+transDet.item_idKet;
						break;
					}
				}else if(trans.pagename.toLowerCase().equals("input")&!Utils.isEmpty(trans.no_trans_ref)){
					Trans tmp=dbService.selectListTrans(jenis-2, null, null, trans.no_trans_ref,aksescabang_id).get(0);
					List<TransDet> ld=dbService.selectListTransDet(tmp.trans_id, null, null, transDet.item_id, null);
					if(!trans.mode.equals("EDIT")){
						if(ld.isEmpty()){
							isItemRetur=false;
							barcode+=" ["+transDet.barcode_ext+"] "+transDet.item_idKet;
							break;
						}else{
							TransDet td=ld.get(0);
							if(transDet.qty>td.qty){
								isSizeRetur=false;
								barcode+=" ["+transDet.barcode_ext+"] "+transDet.item_idKet+" melebihi jumlah Order ("+td.qty+")";
								break;
							}
						}
					}
				}
			}

			trans.total_harga=trans.sub_total_harga-trans.total_disc+trans.ppn;
			trans.remain=trans.total_harga;

			trans.lsTransDet=lsTransDets;
			if(!harga){
				errors.rejectValue("lsTransDet", null, "Harga tidak boleh kurang atau sama dengan 0");
			}
			
			if(!qtyIsi){
				errors.rejectValue("lsTransDet", null, "QTY tidak boleh kurang atau sama dengan 0");
			}
			if(!isItemRetur){
				errors.rejectValue("lsTransDet", null, barcode+" tidak terdapat pada transaksi no :"+trans.no_trans_ref);
			}
			if(!isSizeRetur){
				errors.rejectValue("lsTransDet", null, barcode+" pada transaksi no :"+trans.no_trans_ref);
			}
			//
		}


		//bila ada error, kembalikan ke halaman edit
		if (result.hasErrors()) {
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			String message=messageSource.getMessage("ErrorForm", null,null);
			for(String err:Utils.errorBinderToList(result, messageSource)){
				if(!(err.trim().contains("diisi")||err.trim().contains("may not be null")||err.trim().contains("tidak ditemukan")))
				message+="<br/>"+err;
			}
			map.put("message",message );
			model.addAllAttributes(map);

			return "trans/trans_edit";
		}

		//simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
		//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save)
		//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
		String pesan ="";
		try{



			pesan = dbService.saveTrans(trans, currentUser);

			if(pesan.contains("habis")){


//						ra.addFlashAttribute("messageType", "error");
//						ra.addFlashAttribute("message", pesan);
//
//						return "redirect:/transaksi/"+trans.jenistrans+"/"+trans.pagename+"/"+trans.trans_id+"/edit";

				Map<String,Object> map=new HashMap<String, Object>();
				map.put("messageType", "error");
				map.put("message",pesan );
				model.addAllAttributes(map);

				return "trans/trans_edit";
			}
			//balikin ke layar list input
		}catch (Exception e) {
			trans.no_trans=null;
			pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,"","diproses"},null);
			e.printStackTrace();
			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock", Utils.errorExtract(e), null);

		
			Map<String,Object> map=new HashMap<String, Object>();
			map.put("messageType", "error");
			map.put("message",pesan );
			model.addAllAttributes(map);

			return "trans/trans_edit";
		}
		ra.addFlashAttribute("messageType", "done");
		ra.addFlashAttribute("message", pesan);

		return "redirect:/transaksi/"+trans.jenistrans+"/"+trans.pagename+"/"+trans.trans_id+"/edit";

		/*Map<String,Object> map=new HashMap<String, Object>();
		map.put("messageType", "done");

		map.put("message",pesan );
		model.addAllAttributes(map);

		return "trans/trans_edit";*/

	}
		
		

		@RequestMapping(value="/transfer/{jenistrans}/{pagename}/{modename}/{trans_id}", method=RequestMethod.GET)
		public String transfer( RedirectAttributes ra, HttpServletRequest request,HttpServletResponse response,@PathVariable String jenistrans,@PathVariable String pagename,@PathVariable String modename,@PathVariable Integer trans_id) throws MailException, MessagingException {
			logger.debug("Halaman: Micro Transfer, method: transfer");
			String pesan ="";
			String  redirectTo=request.getContextPath()+"/transaksi/"+jenistrans+"/"+pagename+"/"+trans_id+"/"+modename;
			User currentUser=(User) request.getSession().getAttribute("currentUser");
			Integer aksescabang_id=null;
			if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
			
			if(modename.equals("show")){
				redirectTo=request.getContextPath()+"/transaksi/"+jenistrans+"/"+pagename;
			}else if(pagename.toLowerCase().equals("ordertransfer")){
				redirectTo=request.getContextPath()+"/transaksi/"+jenistrans+"/Input/"+trans_id+"/new";
			}else{
				redirectTo=request.getContextPath()+"/transaksi/"+jenistrans+"/"+pagename+"/new";
			}

			try{
				//currently logged in user
				Trans trans=dbService.selectListTrans(null, null, trans_id, null,aksescabang_id).get(0);
				trans.setMode("TRANSFER");
				trans.setPagename(pagename);
				trans.setJenistrans(jenistrans);
				trans.setLsTransDet(dbService.selectListTransDet(trans.trans_id, null, null, null, null));
				trans_id=trans.trans_id;
				pesan=dbService.saveTrans(trans, currentUser);

				if(pesan.contains("gagal")){
					if(modename.equals("show")){
						redirectTo=request.getContextPath()+"/transaksi/"+jenistrans+"/"+pagename;
					}else{
						redirectTo=request.getContextPath()+"/transaksi/"+jenistrans+"/"+pagename+"/"+trans_id+"/"+modename.toLowerCase();
					}
				}

			}catch (Exception e) {
				e.printStackTrace();
				pesan="Polis gagal di transfer";

				/*email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada E-Accounting", Utils.errorExtract(e), null);*/


			}


			try {
				ServletOutputStream out;
				out = response.getOutputStream();
				out.println("<script>");
				if(!pesan.equals("ga ada"))	{			
				out.println("alert('"+pesan+"');");}
				out.println("window.location=\""+redirectTo+"\";</script>");
				out.flush();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

}
