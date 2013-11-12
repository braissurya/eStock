package com.jjtech.estock.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.j2ee.servlets.ImageServlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jjtech.estock.model.Delivery;
import com.jjtech.estock.model.DeliveryDet;
import com.jjtech.estock.model.Payment;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.TransHist;
import com.jjtech.estock.model.User;
import com.jolbox.bonecp.BoneCPDataSource;
import com.jjtech.estock.service.DbService;
import com.jjtech.estock.utils.FormatDate;
import com.jjtech.estock.utils.JasperUtils;
import com.jjtech.estock.utils.Utils;



/**
 * Report Controller
 *
 * @author Rudy
 * @since Apr 21, 2013 (9:41:40 AM)
 *
 */
@Controller
@RequestMapping("/report")
public class ReportController extends ParentController{

	@Autowired
	private BoneCPDataSource dbDataSource;

	private Connection connection;

	private Connection getConnection() {
		if(this.connection==null)
			try { this.connection = dbDataSource.getConnection(); }
			catch (SQLException e) { e.printStackTrace(); }
		return this.connection;
	}

	//@ModelAttribute pada deklarasi method berarti:
	//bisa lebih dari satu model attribute, bisa juga digunakan sebagai reference data
	@ModelAttribute("reff")
	public Map<String, Object> reff(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("AllKategori", dbService.selectDropDown("id", "concat(inisial, ' - ', nama)", "lst_kategori", "active = 1", "nama"));
		map.put("AllMerk", dbService.selectDropDown("id", "concat(inisial, ' - ', nama)", "lst_merk", "active = 1", "nama"));
		map.put("AllWarna", dbService.selectDropDown("id", "concat(inisial, ' - ', nama)", "lst_warna", "active = 1", "nama"));
		return map;
	}

	/**
	 * Fungsi untuk generate report, dipanggil oleh seluruh report
	 *
	 * @author Rudy
	 * @since Apr 21, 2013 (3:23:00 PM)
	 *
	 * @param jenis
	 * @param params
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	private String generateReport(String jenis, Map params, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException{
		ServletContext context = session.getServletContext();
		String format = (String) params.get("format");

		//Generate report
		JasperPrint jasperPrint = JasperFillManager.fillReport(
			context.getRealPath("/WEB-INF/classes/" + props.getProperty("dir.report") + "/" +
			props.getProperty("report." + jenis) + ".jasper"), //report path
			params, //report parameters
			getConnection() //connection object
			);

		//Put generated report into session
		session.setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);

		//Text File
		if(format.equalsIgnoreCase("txt")){
			JRCsvExporter exporter = new JRCsvExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());
			//tambahan header khusus file CSV
			response.setHeader("Content-Disposition","attachment; filename=\"report.txt\";");

			exporter.exportReport();
			return null;

		//csv File
		}else if(format.equalsIgnoreCase("csv")){
				JRCsvExporter exporter = new JRCsvExporter();
				exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER,",");
				exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
				exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());
				//tambahan header khusus file CSV
				response.setHeader("Content-Disposition","attachment; filename=\"report.csv\";");

				exporter.exportReport();
				return null;

			//HTML File
		}else if(format.equalsIgnoreCase("html")){
			JRHtmlExporter exporter = new JRHtmlExporter();

			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_WRITER, response.getWriter());
			//HTML Specific parameters
			exporter.setParameter(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath() + "/jasper/image?image=");
			exporter.setParameter(JRHtmlExporterParameter.IGNORE_PAGE_MARGINS, true); //biar gak terlalu banyak white space
			exporter.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, true); //biar gak terlalu banyak white space
			exporter.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, ""); //biar tidak ada paging (khusus html)

			exporter.exportReport();
			return null;

		//format selain HTML dan TXT
		}else{
			return "redirect:/jasper/" + format; //redirect ke JasperReports Servlet sesuai format
		}
	}

	

	@RequestMapping("/print_do/{no_trans}")
	public String print_do(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable String no_trans)
			throws Exception {
		
		String jenisReport = "print_do";
		logger.debug("Halaman: REPORT " + jenisReport);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		Trans trans=new Trans();
		try{
			trans=dbService.selectListTrans(null, null, null, no_trans,aksescabang_id).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException ("No Transaksi tidak ditemukan");
		}




		//currently logged in user

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		params.put("trans_id",trans.trans_id);

		Date sysdate=dbService.selectSysdate();
		if(trans.print_trans_date==null){
			Trans tmp=new Trans();
			tmp.trans_id=trans.trans_id;
			tmp.print_trans_date=sysdate;
			dbService.updateTrans(tmp);
		}
		dbService.insertTransHist(new TransHist(trans.trans_id, sysdate, trans.posisi_id, "Print Delivery Order", currentUser.id));

		return generateReport(jenisReport, params, session, request, response);

	}

	@RequestMapping("/print_po/{no_trans}")
	public String print_po(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable String no_trans)
			throws Exception {

		String jenisReport = "print_po";
		logger.debug("Halaman: REPORT " + jenisReport);

		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		Trans trans=new Trans();
		try{
			trans=dbService.selectListTrans(null, null, null, no_trans,aksescabang_id).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException ("No Transaksi tidak ditemukan");
		}





		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		params.put("trans_id",trans.trans_id);

		Date sysdate=dbService.selectSysdate();
		if(trans.print_trans_date==null){
			Trans tmp=new Trans();
			tmp.trans_id=trans.trans_id;
			tmp.print_trans_date=sysdate;
			tmp.posisi_id=2;
			dbService.updateTrans(tmp);
		}
		dbService.insertTransHist(new TransHist(trans.trans_id, sysdate, trans.posisi_id, "Print Purchasing Order", currentUser.id));



		return generateReport(jenisReport, params, session, request, response);

	}

	@RequestMapping("/print_faktur/{no_trans}")
	public String print_faktur(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable String no_trans)
			throws Exception {

		String jenisReport = "print_faktur";
		logger.debug("Halaman: REPORT " + jenisReport);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		
		Trans trans=new Trans();
		try{
			trans=dbService.selectListTrans(null, null, null, no_trans,aksescabang_id).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException ("No Transaksi tidak ditemukan");
		}





		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		params.put("trans_id",trans.trans_id);

		Date sysdate=dbService.selectSysdate();
		if(trans.print_faktur_date==null){
			Trans tmp=new Trans();
			tmp.trans_id=trans.trans_id;
			tmp.print_faktur_date=sysdate;
			dbService.updateTrans(tmp);
		}
		dbService.insertTransHist(new TransHist(trans.trans_id, sysdate, trans.posisi_id, "Print Faktur", currentUser.id));

		return generateReport(jenisReport, params, session, request, response);

	}

	@RequestMapping("/print_surat_jalan/{id}")
	public String print_surat_jalan(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable Integer id)
			throws Exception {

		String jenisReport = "print_surat_jalan";
		logger.debug("Halaman: REPORT " + jenisReport);

		Delivery delivery=new Delivery();
		try{
			delivery=dbService.selectListDelivery(id,null,null).get(0);
			delivery.lsDeliveryDets=dbService.selectListDeliveryDet(id, null);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException ("No Bacth Delivery tidak ditemukan");
		}


		

		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		
		List<String> listFile=new ArrayList<String>();
		Date sysdate=dbService.selectSysdate();
		for(DeliveryDet dd:delivery.lsDeliveryDets){
			params.put("trans_id",dd.trans_id);
			Utils.deleteFile(props.getProperty("dir.report.export") +"\\surat_jalan\\"+dd.delivery_id, "sj_"+dd.trans_id+".pdf", response);
			JasperUtils.exportReportToPdf(
					props.getProperty("dir.report")+ "/"  +props.getProperty("report." + jenisReport) + ".jasper", props.getProperty("dir.report.export") +"\\surat_jalan\\"+dd.delivery_id, "sj_"+dd.trans_id+".pdf", 
					params, getConnection(), 
					null, null, null);
			
			listFile.add(props.getProperty("dir.report.export") +"\\surat_jalan\\"+dd.delivery_id+"\\sj_"+dd.trans_id+".pdf");
			Trans trans=dbService.selectListTrans(null, null, dd.trans_id, null,aksescabang_id).get(0);
			
			
			if(trans.print_sj_date==null){
				Trans tmp=new Trans();
				tmp.trans_id=trans.trans_id;
				tmp.print_sj_date=sysdate;
				dbService.updateTrans(tmp);
			}
			dbService.insertTransHist(new TransHist(trans.trans_id, sysdate, trans.posisi_id, "Print Surat Jalan", currentUser.id));
		}

		Utils.deleteFile(props.getProperty("dir.report.export") +"\\surat_jalan\\"+delivery.id, "surat_jalan.pdf", response);
		JasperUtils.concatPdf(listFile, new FileOutputStream(props.getProperty("dir.report.export") +"\\surat_jalan\\"+delivery.id+"\\surat_jalan.pdf"), false);
		
		Utils.downloadFile(props.getProperty("dir.report.export") +"\\surat_jalan\\"+delivery.id+"\\surat_jalan.pdf", "", response, "inline");
		delivery.tgl_print_sj=sysdate;
		dbService.updateDelivery(delivery);
		return null;

	}

	@RequestMapping("/print_retur_po/{no_trans}")
	public String print_retur_po(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable String no_trans)
			throws Exception {

		String jenisReport = "print_retur_po";
		logger.debug("Halaman: REPORT " + jenisReport);

		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		
		Trans trans=new Trans();
		try{
			trans=dbService.selectListTrans(null, null, null, no_trans,aksescabang_id).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException ("No Transaksi tidak ditemukan");
		}




		//currently logged in user

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		params.put("trans_id",trans.trans_id);

		Date sysdate=dbService.selectSysdate();
		if(trans.print_trans_date==null){
			Trans tmp=new Trans();
			tmp.trans_id=trans.trans_id;
			tmp.print_trans_date=sysdate;
			dbService.updateTrans(tmp);
		}
		dbService.insertTransHist(new TransHist(trans.trans_id, sysdate, trans.posisi_id, "Print Retur Purchashing Order", currentUser.id));



		return generateReport(jenisReport, params, session, request, response);

	}

	@RequestMapping("/print_retur_do/{no_trans}")
	public String print_retur_do(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable String no_trans)
			throws Exception {

		String jenisReport = "print_retur_do";
		logger.debug("Halaman: REPORT " + jenisReport);

		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		
		Trans trans=new Trans();
		try{
			trans=dbService.selectListTrans(null, null, null, no_trans,aksescabang_id).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException ("No Transaksi tidak ditemukan");
		}





		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		params.put("trans_id",trans.trans_id);

		Date sysdate=dbService.selectSysdate();
		if(trans.print_trans_date==null){
			Trans tmp=new Trans();
			tmp.trans_id=trans.trans_id;
			tmp.print_trans_date=sysdate;
			dbService.updateTrans(tmp);
		}
		dbService.insertTransHist(new TransHist(trans.trans_id, sysdate, trans.posisi_id, "Print Retur Delivery Order", currentUser.id));



		return generateReport(jenisReport, params, session, request, response);

	}
	
	/**
	 * * 
	 * @author Deddy
	 * @since Apr 11,2013 (10:20:21 PM)
	 *
	 * @param session
	 * @param request
	 * @param response
	 * @return
	 * @throws JRException
	 * @throws IOException
	 * @throws ServletRequestBindingException
	 *//*
	 */
	@RequestMapping(value="/stockopname/{opname_id}") 
	public String StockOpname(Model model, @PathVariable Integer opname_id,HttpSession session, HttpServletRequest request, HttpServletResponse response)throws JRException, IOException, ServletRequestBindingException {	
		String jenisReport="stockopname";
		logger.debug("Halaman: REPORT " + jenisReport);		
		Date sysdate = dbService.selectSysdate();
		User currentUser = (User) session.getAttribute("currentUser");
		Integer month_sysdate = sysdate.getMonth()+1;
		Integer year_sysdate = sysdate.getYear()+1900;
		Map params = new HashMap();
		params.put("opname_id", opname_id);
		Integer countMstOpname=dbService.selectCountTable("mst_opname", "month(tgl)="+month_sysdate+" and year(tgl)="+year_sysdate);
		//jika belum ada data Berita Acara Gudang untuk bulan yg sedang berjalan, select lgsg dari mst_stock.
		if(countMstOpname==0){
			params.put("query", "select a.item_id, b.nama as nama_item, c.nama as warna_item, d.nama as kategori_item, e.nama as merk_item, f.nama as satuan_item, (saldo_awal + masuk - keluar) as qty "+
				"from mst_stock a,"+
					 "lst_item b"+
					 "left join lst_warna c 	on c.id = b.warna_id"+
					 "left join lst_kategori d on d.id = b.kategori_id"+
					 "left join lst_merk e on e.id =b.merk_id"+
					 "left join lst_satuan f on f.id = b.satuan_id"+
				"where a.item_id = b.id"+
				  "and a.cabang_id = "+currentUser.cabang_id+
				  "and a.periode in (select max(periode) from mst_stock  )"+
				"order by b.merk_id, b.nama)");
		//jika sudah ada data Berita Acara Gudang untuk bulan yg sedang berjalan, select lgsg dari mst_opname_det.	
		}else if(countMstOpname>0){
			params.put("query", "");
		}
		
		
		return generateReport(jenisReport, params, session, request, response);
	}
	
	@RequestMapping("/print_kwitansi/{payment_id}")
	public String print_kwitansi(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable Integer payment_id) throws Exception{
		String jenisReport = "kwitansi";
		logger.debug("Halaman: REPORT " + jenisReport);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		
		Payment payment=new Payment();
		try{
			payment=dbService.selectListPayment(payment_id, null, null).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException ("No Transaksi tidak ditemukan");
		}
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf");
		params.put("payment_id",payment_id);
		
		return generateReport(jenisReport, params, session, request, response);
	}

	@RequestMapping("/print_form_order/{no_trans}")
	public String print_form_order(HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable String no_trans)
			throws Exception {

		String jenisReport = "print_form_order";
		logger.debug("Halaman: REPORT " + jenisReport);
		
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;

		Trans trans=new Trans();
		try{
			trans=dbService.selectListTrans(null, null, null, no_trans,aksescabang_id).get(0);
		}catch (Exception e) {
			// TODO: handle exception
			throw new RuntimeException ("No Transaksi tidak ditemukan");
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("format","pdf"); //format report
		params.put("trans_id",trans.trans_id);

		Date sysdate=dbService.selectSysdate();
		if(trans.print_order_form==null){
			Trans tmp=new Trans();
			tmp.trans_id=trans.trans_id;
			tmp.print_order_form=sysdate;
			tmp.posisi_id=2;
			dbService.updateTrans(tmp);
		}
		dbService.insertTransHist(new TransHist(trans.trans_id, sysdate, trans.posisi_id, "Print Order Form", currentUser.id));



		return generateReport(jenisReport, params, session, request, response);

	}
	
	@RequestMapping("/master/{jenisReport}")
	public String list_master(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String jenisReport) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String judul_report = "";
		String param = "";
		
		if(jenisReport.equals("list_item")){
			judul_report = "Item";
		}else if(jenisReport.equals("list_kategori")){		
			judul_report = "Kategori";	
		}else if(jenisReport.equals("list_merk")){		
			judul_report = "Merk";	
		}else if(jenisReport.equals("list_satuan")){		
			judul_report = "Satuan";	
		}else if(jenisReport.equals("list_warna")){		
			judul_report = "Warna";	
		}else if(jenisReport.equals("list_account")){		
			judul_report = "Account";	
		}else if(jenisReport.equals("list_karyawan")){		
			judul_report = "Karyawan";	
		}else if(jenisReport.equals("list_supplier")){		
			judul_report = "Supplier";	
		}else if(jenisReport.equals("list_customer")){		
			judul_report = "Customer";		
		}else if(jenisReport.equals("list_bank")){		
			judul_report = "Bank";		
		}else if(jenisReport.equals("list_coa")){		
			judul_report = "Chart Of Account";		
		}else{
			throw new RuntimeException ("Page not found");
		}
		
		logger.debug("Halaman: REPORT " + jenisReport);
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		

			model.addAttribute("judul_report", judul_report);
			return "report/list_master";
		}
	}
	
	@RequestMapping("/jurnal")
	public String list_jurnal(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String judul_report = "";
		String param = "";
		String type = "", jenisReport = "";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		Integer jenis = 0;
		
		judul_report = "Jurnal";
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report	
			params.put("beg_date", ServletRequestUtils.getRequiredStringParameter(request, "beg_date"));
			params.put("end_date", ServletRequestUtils.getRequiredStringParameter(request, "end_date"));

			jenisReport = "laporan_jurnal";
			
			logger.debug("Halaman: REPORT " + jenisReport);
			
			param = "AND DATE(a.tgl_jurnal) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
			
			params.put("judul_report", judul_report);
			params.put("param", param);
			params.put("jenis", jenis);
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		

			model.addAttribute("judul_report", judul_report);
			model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);		
			return "report/list_jurnal";
		}
	}
	
	@RequestMapping("/uang/{pageName}")
	public String list_keuangan(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String pageName) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String judul_report = "";
		String param = "";
		String jenisReport = "";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		Integer jenis = 0;
		
		if(pageName.toLowerCase().equals("in")){
			judul_report = "Penerimaan Keuangan";
		}else if(pageName.toLowerCase().equals("out")){
			judul_report = "Pengeluaran Keuangan";
		}else{
			throw new RuntimeException ("Page not found");
		}
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report	
			params.put("beg_date", ServletRequestUtils.getRequiredStringParameter(request, "beg_date"));
			params.put("end_date", ServletRequestUtils.getRequiredStringParameter(request, "end_date"));
			
			jenisReport="laporan_keuangan";
			
			logger.debug("Halaman: REPORT " + jenisReport);
			
			if(pageName.toLowerCase().equals("in")){
				param = "AND a.dk = 'K' AND DATE(a.paid_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
			}else if(pageName.toLowerCase().equals("out")){
				param = "AND a.dk = 'D' AND DATE(a.paid_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
			}else{
				throw new RuntimeException ("Page not found");
			}
			
			params.put("judul_report", judul_report);
			params.put("param", param);
			params.put("jenis", jenis);
			params.put("pageName",  pageName);
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		

			model.addAttribute("judul_report", judul_report);
			model.addAttribute("pageName", pageName);
			model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);		
			return "report/list_keuangan";
		}
	}
	
	@RequestMapping("/labarugi")
	public String list_labarugi(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
	throws JRException, IOException, ServletRequestBindingException {	
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String param = "";
		String jenisReport = "";
		String beg_date="";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "MMMMM yyyy");
		Date periode;
//		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
//		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report	
			
			beg_date=ServletRequestUtils.getRequiredStringParameter(request, "beg_date");
			periode=Utils.convertStringToDate(beg_date, "MMMMM yyyy");
			params.put("periode", periode);
			params.put("beg_date", Utils.convertDateToString(periode, "yyyy-MM-01"));
			param = "AND DATE(a.tgl_trx) between $P{beg_date} AND DATE_ADD(DATE_ADD($P{beg_date}, INTERVAL 1 MONTH),INTERVAL -1 DAY)";
			
//			params.put("beg_date", ServletRequestUtils.getRequiredStringParameter(request, "beg_date"));
//			params.put("end_date", ServletRequestUtils.getRequiredStringParameter(request, "end_date"));
//			param = "DATE(a.tgl_trx) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
			
			jenisReport="laporan_labarugi";
			
			logger.debug("Halaman: REPORT " + jenisReport);
			
			params.put("param", param);
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		
			model.addAttribute("tgl_awal", tgl_awal);
//			model.addAttribute("tgl_akhir", tgl_akhir);
			return "report/list_labarugi";
		}
	}
	
	@RequestMapping("/payroll")
	public String report_payroll(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String judul_report = "";
		String param = "", beg_date = "";
		String jenisReport = "";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "MMMMM yyyy");
		Date periode=null;
		Date periodeAkhir=null;
		
		judul_report = "Payroll";	
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			params.put("username", currentUser.username);	
			jenisReport="laporan_payroll";
			
				
			beg_date=ServletRequestUtils.getRequiredStringParameter(request, "beg_date");
			periode=Utils.convertStringToDate(beg_date, "MMMMM yyyy");
			periodeAkhir=FormatDate.add(FormatDate.add(periode, Calendar.MONTH, 1), Calendar.DATE, -1);
			params.put("beg_date", Utils.convertDateToString(periode, "yyyy-MM-01"));
			params.put("end_date", Utils.convertDateToString(periodeAkhir, "yyyy-MM-dd"));
			param = "AND DATE(a.periode) BETWEEN $P{beg_date} and $P{end_date}";
			
			logger.debug("Halaman: REPORT " + judul_report);
			
			params.put("param", param);
			params.put("username", currentUser.username);
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		
			model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("judul_report", judul_report);
			return "report/report_payroll";
		}
	}
	
	@RequestMapping("/transaksi/{jenisName}/{pageName}")
	public String list_transaksi(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String jenisName, @PathVariable String pageName) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String judul_report = "";
		String param = "";
		String type = "", jenisReport = "";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		Integer jenis = 0;
		
		//filter jenis report
		if(jenisName.equals("penjualan")){
			if(pageName.equals("order")){
				judul_report = "Order Penjualan";
			}else if(pageName.equals("input")){
				judul_report = "Input Penjualan";
			}else if(pageName.equals("retur")){
				judul_report = "Retur Penjualan";
			}else{
				throw new RuntimeException ("Page not found");
			}
		}else if(jenisName.equals("pembelian")){
			if(pageName.equals("order")){
				judul_report = "Order Pembelian";
			}else if(pageName.equals("input")){
				judul_report = "Input Pembelian";
			}else if(pageName.equals("retur")){
				judul_report = "Retur Pembelian";
			}else{
				throw new RuntimeException ("Page not found");
			}
		}else{
			throw new RuntimeException ("Page not found");
		}	
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report	
			params.put("beg_date", ServletRequestUtils.getRequiredStringParameter(request, "beg_date"));
			params.put("end_date", ServletRequestUtils.getRequiredStringParameter(request, "end_date"));
			type = ServletRequestUtils.getRequiredStringParameter(request, "jenis_report");

			//summary
			if(type.equals("1")){ 
				jenisReport = "laporan_transaksi";
			//detail
			}else if(type.equals("2")){ 
				jenisReport = "laporan_transaksi_det";
			}
			
			logger.debug("Halaman: REPORT " + jenisReport);
			
			//filter jenis report
			if(jenisName.equals("penjualan")){
				if(pageName.equals("order")){
					jenis = 2;
					param = "AND DATE(t.tgl_order) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
				}else if(pageName.equals("input")){
					jenis = 4;
					param = "AND DATE(t.trans_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
				}else if(pageName.equals("retur")){
					jenis = 6;
					param = "AND DATE(t.trans_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
				}else{
					throw new RuntimeException ("Page not found");
				}
			}else if(jenisName.equals("pembelian")){
				if(pageName.equals("order")){
					jenis = 1;
					param = "AND DATE(t.tgl_order) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
				}else if(pageName.equals("input")){
					jenis = 3;
					param = "AND DATE(t.trans_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
				}else if(pageName.equals("retur")){
					jenis = 5;
					param = "AND DATE(t.trans_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
				}else{
					throw new RuntimeException ("Page not found");
				}
			}else{
				throw new RuntimeException ("Page not found");
			}		
			
			params.put("judul_report", judul_report);
			params.put("param", param);
			params.put("jenis", jenis);
			params.put("jenisName", jenisName);
			params.put("pageName",  pageName);
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		

			model.addAttribute("judul_report", judul_report);
			model.addAttribute("pageName", pageName);
			model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);		
			return "report/list_transaksi";
		}
	}
	
	@RequestMapping("/stock/{jenisReport}")
	public String report_stock(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String jenisReport) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String judul_report = "";
		String param = "", beg_date = "";
		String kategori = "", merk = "", warna = "";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "MMMMM yyyy");
		Date periode=null;
		
		if(jenisReport.equals("stock_gudang")){
			judul_report = "Stock Gudang";
		}else if(jenisReport.equals("stock_limit")){		
			judul_report = "Stock Limit";	
		}else{
			throw new RuntimeException ("Page not found");	
		}
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			kategori = ServletRequestUtils.getRequiredStringParameter(request, "kategori");
			merk = ServletRequestUtils.getRequiredStringParameter(request, "merk");
			warna = ServletRequestUtils.getRequiredStringParameter(request, "warna");
			params.put("username", currentUser.username);	
				
			beg_date=ServletRequestUtils.getRequiredStringParameter(request, "beg_date");
			periode=Utils.convertStringToDate(beg_date, "MMMMM yyyy");
			params.put("beg_date", Utils.convertDateToString(periode, "yyyy-MM-01"));
			param = "AND DATE(s.periode) = $P{beg_date}";
			
			//filter kategori
			if(kategori != ""){
				param = param + " AND item.kategori_id = " + kategori;
			}	
			
			//filter merk
			if(merk != ""){
				param = param + " AND item.merk_id = " + merk;
			}	
			
			//filter warna
			if(warna != ""){
				param = param + " AND item.warna_id = " + warna;
			}	
			
			logger.debug("Halaman: REPORT " + jenisReport);
			
			params.put("param", param);
			params.put("username", currentUser.username);
			
			return generateReport("laporan_" + jenisReport, params, session, request, response);
		}else{		
			
			model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("judul_report", judul_report);
			return "report/report_stock";
		}
	}
	
	@RequestMapping("/limitkreditcustomer")
	public String report_limitkreditcustomer(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String jenisReport = "";
		String type = "";
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			params.put("username", currentUser.username);	
			type = ServletRequestUtils.getRequiredStringParameter(request, "jenis_report");

			//summary
			if(type.equals("1")){ 
				jenisReport = "laporan_limit_kredit";
			//detail
			}else if(type.equals("2")){ 
				jenisReport = "laporan_limit_kredit_det";
			}
			
			logger.debug("Halaman: REPORT " + jenisReport);
			
			params.put("username", currentUser.username);
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		
			return "report/report_limit_kredit";
		}
	}
	
	@RequestMapping("/berita_acara_gudang")
	public String report_berita_acara_gudang(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String jenisReport = "";
		String type = "", param = "";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report	
			params.put("beg_date", ServletRequestUtils.getRequiredStringParameter(request, "beg_date"));
			params.put("end_date", ServletRequestUtils.getRequiredStringParameter(request, "end_date"));
			params.put("username", currentUser.username);	
			type = ServletRequestUtils.getRequiredStringParameter(request, "jenis_report");

			//summary
			if(type.equals("1")){ 
				jenisReport = "laporan_berita_acara_gudang";
			//detail
			}else if(type.equals("2")){ 
				jenisReport = "laporan_berita_acara_gudang_det";
			}
			
			logger.debug("Halaman: REPORT " + jenisReport);
			
			param = "AND DATE(o.tgl) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";

			params.put("param", param);
			params.put("username", currentUser.username);
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		
			
			model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);	
			return "report/report_berita_acara_gudang";
		}
	}
	
	@RequestMapping("/keuangan/{jenisName}")
	public String list_transaksi(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response, @PathVariable String jenisName) 
	throws JRException, IOException, ServletRequestBindingException {	
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String judul_report = "";
		String param = "";
		String type = "", jenisReport = "";
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		Integer jenis = 0;
		
		//filter jenis report
		if(jenisName.equals("Hutang")){
			judul_report = "Hutang (AP)";
			jenis = 3;
		}else if(jenisName.equals("Piutang")){
			judul_report = "Piutang (AR)";
			jenis = 4;
		}else{
			throw new RuntimeException ("Page not found");
		}	
		
		if(request.getParameter("show") != null){				
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report	
			params.put("beg_date", ServletRequestUtils.getRequiredStringParameter(request, "beg_date"));
			params.put("end_date", ServletRequestUtils.getRequiredStringParameter(request, "end_date"));
			type = ServletRequestUtils.getRequiredStringParameter(request, "jenis_report");

			//summary
			if(type.equals("1")){ 
				jenisReport = "laporan_hutang_piutang";
			//detail
			}else if(type.equals("2")){ 
				jenisReport = "laporan_hutang_piutang_det";
			}
			
			logger.debug("Halaman: REPORT " + jenisReport);
			
			param = "AND DATE(t.trans_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";			
			
			params.put("judul_report", judul_report);
			params.put("param", param);
			params.put("jenis", jenis);
			params.put("jenisName", jenisName);
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response);
		}else{		

			model.addAttribute("judul_report", judul_report);
			model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);		
			return "report/list_transaksi";
		}
	}
	
	/*@RequestMapping("/transaksi/{jenisName}/{pageName}")
	public String transaksi(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,@PathVariable String jenisName,@PathVariable String pageName) 
			throws JRException, IOException, ServletRequestBindingException {
		
		//currently logged in user
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		
		String jenisReport = "laporan_transaksi";
		String param = "", param2 = "";
		
		String tgl_awal = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		String tgl_akhir = Utils.convertDateToString(dbService.selectSysdate(), "dd-MM-yyyy");
		
		logger.debug("Halaman: REPORT " + jenisReport);
			
		if(request.getParameter("show") != null){			
			Map<String, String> params = new HashMap<String, String>();
			params.put("format", ServletRequestUtils.getRequiredStringParameter(request, "format")); //format report
			params.put("beg_date", ServletRequestUtils.getRequiredStringParameter(request, "beg_date"));
			params.put("end_date", ServletRequestUtils.getRequiredStringParameter(request, "end_date"));
			
			Integer posisi_id=0,jenis=0;
			if(jenisName.toLowerCase().equals("penjualan")){
				if(pageName.toLowerCase().equals("order")){
					jenis=2;
				}else if(pageName.toLowerCase().equals("input")){
					jenis=4;
				}else if(pageName.toLowerCase().equals("retur")){
					jenis=6;
				}else{
					throw new RuntimeException ("Page not found");
				}
			}else if(jenisName.toLowerCase().equals("pembelian")){
				if(pageName.toLowerCase().equals("order")){
					jenis=1;
				}else if(pageName.toLowerCase().equals("input")){
					jenis=3;
				}else if(pageName.toLowerCase().equals("retur")){
					jenis=5;
				}else{
					throw new RuntimeException ("Page not found");
				}
			}else{
				throw new RuntimeException ("Page not found");
			}
			params.put("jenisName", jenisName);
			params.put("pageName",  pageName);
			params.put("posisi_id",  posisi_id);
			params.put("jenis",  jenis);
			
			
			Integer jenis=ServletRequestUtils.getRequiredIntParameter(request, "jenis_report");
			
			
			//filter jenis report
			if(jenis == 1){//summary
				jenisReport = "laporan_transaksi";	//BANK & CABANG
			}else if(jenis == 2){//detail
				jenisReport = "laporan_transaksi_det";	//ASURANSI JIWA
			}
						
			//filter periode
			param = "DATE(p.beg_date) BETWEEN STR_TO_DATE($P{beg_date}, '%d-%m-%Y') and STR_TO_DATE($P{end_date}, '%d-%m-%Y')";
						
			params.put("param", param);
			params.put("username", currentUser.username);	
			
			return generateReport(jenisReport, params, session, request, response);
		}else{
			model.addAttribute("tgl_awal", tgl_awal);
			model.addAttribute("tgl_akhir", tgl_akhir);
			return "report_" + jenisReport;
		}
	}*/

		

}