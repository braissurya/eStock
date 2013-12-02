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
 * @author : Bertho Rafitya Iwasurya
 * @since : Jan 28, 2013 9:25:42 PM
 * @Description : inputan master user
 * @Revision :
 *           #====#===========#===================#===========================#
 *           | ID | Date | User | Description |
 *           #====#===========#===================#===========================#
 *           | | | | |
 *           #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/transaksiApproval")
public class InputTransApprovalController extends ParentController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
	// binder.registerCustomEditor(Date.class, new
	// CustomDateEditor(Utils.defaultDF, true)); //bind otomatis date dgn
	// format dd-MM-yyyy
	// binder.registerCustomEditor(Date.class, new
	// CustomDateEditor(Utils.defaultDFLong, true)); //bind otomatis date
	// dgn format dd-MM-yyyy HH:mm:ss

	binder.registerCustomEditor(Double.class, new CustomNumberEditor(Double.class, Utils.defaultNF, true));
	binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    // @ModelAttribute pada deklarasi method berarti:
    // bisa lebih dari satu model attribute, bisa juga digunakan sebagai
    // reference data
    @ModelAttribute("reff")
    public Map<String, Object> reff() {
	Map<String, Object> map = new HashMap<String, Object>();

	map.put("lsCabang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1", "nama"));
	map.put("lsGudang", dbService.selectDropDown("id", "concat(nama,' [',kode,']')", "lst_cabang", "active = 1 and jenis=2", "nama"));
	map.put("lsPayMode", dbService.selectDropDown("jenis", "keterangan", "lst_config", "active = 1 and id=8", "keterangan"));
	map.put("lsReturType", dbService.selectDropDown("jenis", "keterangan", "lst_config", "active = 1 and id=13", "keterangan"));

	return map;
    }

    @RequestMapping(value = "/list", method = { RequestMethod.GET, RequestMethod.POST })
    public String show(Model model, HttpServletRequest request) {
	logger.debug("Halaman: Transaksi, method: SHOW");

	Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
	String search = null, sort = "no_trans", sort_type = null, no_trans = null;

	Integer jenis = null, posisi_id = null;

	jenis = 4;

	List<Trans> listPaging = null;

	User currentUser = (User) request.getSession().getAttribute("currentUser");
	Integer aksescabang_id = null;
	if (Utils.nvl(currentUser.flag_akses_all) != 1)
	    aksescabang_id = currentUser.cabang_id;

	// reference data utk dropdown
	int[] listNumRows = new int[] { 5, 10, 15, 20, 25, 30, 40, 50 };
	no_trans = ServletRequestUtils.getStringParameter(request, "nt", "").equals("") ? null : ServletRequestUtils.getStringParameter(request,
		"nt", "");
	search = ServletRequestUtils.getStringParameter(request, "s", "").equals("") ? null : ServletRequestUtils
		.getStringParameter(request, "s", "");
	sort = ServletRequestUtils.getStringParameter(request, "sort", "").equals("") ? sort : ServletRequestUtils.getStringParameter(request,
		"sort", "");
	sort_type = ServletRequestUtils.getStringParameter(request, "st", "asc");
	// perhitungan paging
	rowcount = ServletRequestUtils.getIntParameter(request, "rowcount", 5);

	totalData = dbService.selectListTransPagingCount(search, null, null, null, no_trans, aksescabang_id, 1);

	totalPage = new Double(Math.ceil(new Double(totalData) / new Double(rowcount))).intValue(); // jml
												    // total
												    // halaman
												    // =
												    // (jumlah
												    // data
												    // /
												    // rowcount)
												    // dibulatkan
												    // keatas
	page = ServletRequestUtils.getIntParameter(request, "page", 1); // halaman
									// ke X

	if (page < 1)
	    page = 1;
	if (page > totalPage)
	    page = totalPage;
	int offset = (page - 1) * rowcount; // start penarikan data dari row ke
					    // X (mySQL)

	if (offset < 0)
	    offset = 0;

	listPaging = dbService.selectListTransPaging(search, offset, rowcount, sort, sort_type, null, null, null, no_trans, aksescabang_id, 1);

	if (no_trans != null && !listPaging.isEmpty()) {
	    model.addAttribute("trans_idnya", listPaging.get(0).trans_id);
	}

	model.addAttribute("listNumRows", listNumRows);
	model.addAttribute("totalPage", totalPage);
	model.addAttribute("totalData", totalData);
	model.addAttribute("halfPage", (new Double(totalPage / 2)).intValue());
	List<Integer> pages = new ArrayList<Integer>();
	for (int i = 0; i < totalPage; i++) {
	    pages.add(i + 1);
	}
	model.addAttribute("pages", pages);
	model.addAttribute("page", page);
	model.addAttribute("rowcount", rowcount);
	model.addAttribute("search", search);
	model.addAttribute("sort", sort);
	model.addAttribute("sort_type", sort_type);
	model.addAttribute("listPaging", listPaging);

	return "trans/trans_applist";
    }

    // edit data
    @RequestMapping(value = "/{trans_id}/approve", method = RequestMethod.GET)
    // mapping request GET saja ke method ini, menerima parameter "uname"
    public String update(HttpServletRequest request, @ModelAttribute("trans") Trans trans, Model model, @PathVariable Integer trans_id) { // @PathVariable
																	  // berarti
																	  // parameter
																	  // "uname"
																	  // langsung
																	  // di
																	  // bind
																	  // ke
																	  // string
																	  // "uname"
	logger.debug("Halaman: Transaksi, method: Edit");

	User currentUser = (User) request.getSession().getAttribute("currentUser");
	Integer aksescabang_id = null;
	if (Utils.nvl(currentUser.flag_akses_all) != 1)
	    aksescabang_id = currentUser.cabang_id;

	Trans tmp = dbService.selectListTrans(null, null, trans_id, null, aksescabang_id).get(0);
	BeanUtils.copyProperties(tmp, trans);
	trans.setMode("EDIT");

	Date sysdate = dbService.selectSysdate();
	if (trans.jenis == 4) {
	    if (trans.customer_id != null) {
		trans.customer = dbService.selectListCustomer(trans.customer_id).get(0);
		if (trans.customer.limit_hutang == null)
		    trans.customer.limit_hutang = 0.0;
		trans.customer.totalHutang = dbService.selectHutangCustomer(trans.customer_id);
		if (trans.customer.totalHutang > trans.customer.limit_hutang) {
		    trans.customer.outoflimit = true;
		}
	    }
	} else if (trans.jenis == 3) {
	    if (trans.supplier_id != null)
		trans.supplier = dbService.selectListSupplier(trans.supplier_id).get(0);
	} else {
	    throw new RuntimeException("Page not found");
	}

	trans.lsTransDet = dbService.selectListTransDet(trans_id, null, null, null, null);
	if (trans.sales_id != null)
	    trans.karyawan = dbService.selectListKaryawan(trans.sales_id, null).get(0);

	trans.sub_total_harga = trans.total_harga + trans.total_disc - trans.ppn;
	trans.persen_disc = (trans.total_disc / trans.sub_total_harga) * 100;
	trans.persen_ppn = (trans.ppn / (trans.sub_total_harga - trans.total_disc)) * 100;

	// cek apakah transaksi masuk ke periode closing
	model.addAttribute("closingWarning", dbService.checkTglClossing(1, currentUser));

	return "trans/trans_approval";
    }

    // saat user menekan tombol save baik dari layar input maupun layar edit
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    // mapping request POST saja ke method ini
    public String save(@Valid @ModelAttribute("trans") Trans trans, BindingResult result, HttpServletRequest request, Model model,
	    RedirectAttributes ra) throws MailException, MessagingException {
	logger.debug("Halaman:  Master User, method: SAVE");

	// currently logged in user
	User currentUser = (User) request.getSession(false).getAttribute("currentUser");
	Integer aksescabang_id = null;
	if (Utils.nvl(currentUser.flag_akses_all) != 1)
	    aksescabang_id = currentUser.cabang_id;

	int jenis = trans.jenis;

	// contoh bila validasi dilakukan langsung didalam controller (validasi
	// tambahan, selain validasi standar yang sudah diset langsung di class
	// User)
	if (!result.hasErrors()) {
	    BindException errors = new BindException(result);

	    trans.total_disc = new Double(ServletRequestUtils.getStringParameter(request, "total_disc", "0").replace(",", "").replace("Rp ", ""));
	    trans.persen_disc = new Double(ServletRequestUtils.getStringParameter(request, "persen_disc", "0").replace(",", "").replace("Rp ", ""));
	    trans.ppn = new Double(ServletRequestUtils.getStringParameter(request, "ppn", "0").replace(",", "").replace("Rp ", ""));
	    trans.persen_ppn = new Double(ServletRequestUtils.getStringParameter(request, "persen_ppn", "0").replace(",", "").replace("Rp ", ""));

	    trans.total_harga = new Double(ServletRequestUtils.getStringParameter(request, "totalHarga", "0").replace(",", "").replace("Rp ", ""));
	    trans.remain = trans.total_harga;

	}

	// bila ada error, kembalikan ke halaman edit
	if (result.hasErrors()) {
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("messageType", "error");
	    String message = messageSource.getMessage("ErrorForm", null, null);
	    for (String err : Utils.errorBinderToList(result, messageSource)) {
		if (!(err.trim().contains("diisi") || err.trim().contains("may not be null") || err.trim().contains("tidak ditemukan")))
		    message += "<br/>" + err;
	    }
	    map.put("message", message);
	    model.addAllAttributes(map);
	    ra.addFlashAttribute("messageType", "error");
	    ra.addFlashAttribute("message", message);
	    return "redirect:/transaksiApproval/"+trans.trans_id+"/approve";
	}

	// simpan data disini, lalu kembalikan ke layar list input, letakkan
	// pesan di flash attribute nya spring
	// flash attribute berguna untuk mengirimkan pesan (contohnya pesan
	// sukses/error setelah save)
	// ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu
	// hilang)
	String pesan = "";
	try {

	    pesan = dbService.saveTransApproval(trans, currentUser);
	    // balikin ke layar list input
	} catch (Exception e) {
	    trans.no_trans = null;
	    pesan = messageSource.getMessage("submitfailed", new String[] { "Transaksi " + trans.jenistrans + " " + trans.pagename, "", "diproses" },
		    null);
	    e.printStackTrace();
	    email.send(true, props.getProperty("email.from"), props.getProperty("admin.email.to").split(";"), null, null, "ERROR pada eStock",
		    Utils.errorExtract(e), null);

	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put("messageType", "error");
	    map.put("message", pesan);
	    model.addAllAttributes(map);
	    ra.addFlashAttribute("messageType", "error");
	    ra.addFlashAttribute("message", pesan);
	    
	    return "redirect:/transaksiApproval/"+trans.trans_id+"/approve";
	}
	ra.addFlashAttribute("messageType", "done");
	ra.addFlashAttribute("message", pesan);

	return "redirect:/transaksiApproval/list";
    }

}
