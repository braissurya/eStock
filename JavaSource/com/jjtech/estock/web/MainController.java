package com.jjtech.estock.web;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.google.gson.Gson;
import com.jjtech.estock.model.Category;
import com.jjtech.estock.model.COA;
import com.jjtech.estock.model.Customer;
import com.jjtech.estock.model.Delivery;
import com.jjtech.estock.model.DeliveryDet;
import com.jjtech.estock.model.DropDown;
import com.jjtech.estock.model.Item;
import com.jjtech.estock.model.Karyawan;
import com.jjtech.estock.model.Stock;
import com.jjtech.estock.model.Supplier;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.TransDet;
import com.jjtech.estock.model.TrxDet;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.EncryptUtils;
import com.jjtech.estock.utils.FormatDate;
import com.jjtech.estock.utils.Utils;

/**
 * MultiActionController untuk fungsi2 dasar
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:49:33 AM)
 *
 */
@Controller
public class MainController extends ParentController{

	@RequestMapping({"/", "/home"})
	public String home() throws MessagingException {
		logger.debug("Halaman: HOME");
		
		return "home";
	}
	
	
	
	@RequestMapping("/admin/console")
	public String console(Model model) {
		logger.debug("Halaman: CONSOLE");
		
		model.addAttribute("userMap", sessionRegistry.getUserMap());
		
		return "home";
	}

	@RequestMapping({ "/logout", "/keluar" })
	public String logout(HttpServletRequest request) {
		logger.debug("Halaman: LOGOUT");
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		sessionRegistry.kick(currentUser, request.getSession(false));
		
		return "redirect:/login";
	}
	
	@RequestMapping("/openfile/{tipe}")
	public String view(HttpServletRequest request, HttpServletResponse response,@PathVariable Integer tipe) throws Exception{
		String location=ServletRequestUtils.getStringParameter(request, "loc",null);
		if(!Utils.isEmpty(location)) {
			location=EncryptUtils.decrypt(location);
			if (tipe==1) {//download formulir
				String [] split=location.split("~");
				location=props.getProperty("dir.klaim.formulir")+"\\"+split[0]+"\\"+split[1];
			}
						
			File file = new File(location);	
			Utils.downloadFile(location, file.getName(), response, "attachment");			
		}
		return null;
	}	
	
	@RequestMapping(value="/klaim/formulir", method={RequestMethod.POST,RequestMethod.GET})
	public String downloadFormulirKlaim(HttpServletRequest request, Model model) throws Exception{
		String asuransi = ServletRequestUtils.getStringParameter(request, "asuransi", null);
		if(asuransi!=null){
			List<HashMap<String,Object>> daftarfile=new ArrayList<HashMap<String,Object>>();
			for(DropDown dd:  Utils.listFilesInDirectory(props.getProperty("dir.klaim.formulir")+"\\"+asuransi)){
				HashMap<String, Object> add=new HashMap<String, Object>();
				add.put("key", dd.key);
				add.put("encrypt", EncryptUtils.encodeURL(asuransi+"~"+dd.key));
				daftarfile.add(add);
			}
			model.addAttribute("daftarFile",daftarfile);
			model.addAttribute("asuransi", asuransi);
		}
		
		model.addAttribute("lsasuransi", dbService.selectDropDown("id", "nama", "mst_bank", "jenis in (2,4) and active=1", "nama"));
		return "downloadFormulirClaim";
	}	
	
	
	
	/**
	 * Method khusus untuk return data dalam bentuk JSON
	 * Biasanya perlu untuk ajax (misalnya autocomplete, atau dynamic dropdown)
	 * 
	 * @author Yusuf
	 * @since Jan 30, 2013 (6:08:30 PM)
	 *
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws ParseException 
	 */
	@RequestMapping("/json/{tipe}")
	public String json(HttpServletRequest request, HttpServletResponse response, @PathVariable String tipe) throws IOException, ParseException{
		response.setContentType("application/json");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		List<DropDown> result = new ArrayList<DropDown>();
		if(tipe.equals("addRow")){
			Integer tbl = ServletRequestUtils.getIntParameter(request, "tbl", 0);
			String nama= ServletRequestUtils.getStringParameter(request, "nama_produk", "");
			String kode = ServletRequestUtils.getStringParameter(request, "barcode_ext","");
			Integer flag_ecer = ServletRequestUtils.getIntParameter(request, "flag_ecer",0);
			String jenistrans = ServletRequestUtils.getStringParameter(request, "jenistrans","");
			Integer qty = Integer.parseInt(ServletRequestUtils.getStringParameter(request, "qty","1").replace(",", ""));
			int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");
			Double persen_disc=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "persen_disc","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "persen_disc","0").replace(",", "")));
			Double total_disc=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "total_disc","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "total_disc","0").replace(",", "")));
			Double harga=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "harga","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "harga","0").replace(",", "")));
			Double diskon_persen=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "diskon_persen","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "diskon_persen","0").replace(",", "")));
			Double diskon=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "diskon","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "diskon","0").replace(",", "")));
			Double persen_ppn=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "persen_ppn","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "persen_ppn","0").replace(",", "")));
			Double ppn=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "ppn","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "ppn","0").replace(",", "")));
			
			String out="";
			String row="	";
			Integer index = 1;
			boolean isSame=false;
			int rowNum=1;
			Double grandTotal=0.0;
			row+="<tbody>";
			if (idxList.length!=0) {
				
				for(int idx:idxList){
					TransDet transDet=new TransDet();
					transDet.urut=idx;
					transDet.item_id=ServletRequestUtils.getIntParameter(request, "item_id_"+idx,0);
					transDet.qty=Integer.parseInt(ServletRequestUtils.getStringParameter(request, "qty_"+idx,"1").replace(",", ""));
					transDet.barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode_ext_"+idx,"");
					transDet.jumlah_diskon=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "diskon_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "diskon_"+idx,"0").replace(",", "")));
					transDet.harga=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "harga_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "harga_"+idx,"0").replace(",", "")));
					transDet.persen_diskon=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "persen_diskon_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "persen_diskon_"+idx,"0").replace(",", "")));
					
					List<Item> lsItem=dbService.selectListItem(null,transDet.barcode_ext, null, null,null,currentUser.cabang_id);
					
					if(kode.equals(transDet.barcode_ext)){
						isSame=true;
						transDet.qty+=qty;
					}
					
					if(jenistrans.equals("Penjualan")){
						if(flag_ecer==1){
							transDet.harga=lsItem.get(0).harga_ecer;
							transDet.jumlah_diskon=lsItem.get(0).diskon_ecer;
						}
						else{
							transDet.harga=lsItem.get(0).harga;
							transDet.jumlah_diskon=lsItem.get(0).diskon;
						}
					}
					
					transDet.persen_diskon=(transDet.jumlah_diskon/transDet.harga)*100;
					if(transDet.harga==0)transDet.persen_diskon=0.0;
					transDet.subTotal_harga=(transDet.harga.doubleValue()-transDet.jumlah_diskon.doubleValue())*new Double(transDet.qty);
					transDet.item_idKet=ServletRequestUtils.getStringParameter(request, "nama_"+idx,"");
					
					grandTotal+=transDet.subTotal_harga;
					
					if(jenistrans.equals("Penjualan")){					
						row +="<tr>"+
								"<td>"+rowNum+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+rowNum+"\" title=\""+rowNum+"\"/></td>"+			 	
								"<td>"+transDet.barcode_ext+"<input type=\"hidden\" name=\"item_id_"+rowNum+"\" id=\"item_id_"+rowNum+"\" value=\""+transDet.item_id+"\" title=\""+rowNum+"\"/><input type=\"hidden\" name=\"barcode_ext_"+rowNum+"\" value=\""+transDet.barcode_ext+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+transDet.item_idKet+"<input type=\"hidden\" name=\"nama_"+rowNum+"\" id=\"nama_"+rowNum+"\" value=\""+transDet.item_idKet+"\" title=\""+rowNum+"\"/></td>"+
//							 	"<td class=\"right\">"+Utils.formatNumber("#,##0",transDet.harga)+"<input type=\"hidden\" name=\"harga_"+rowNum+"\" id=\"harga_"+rowNum+"\" value=\""+transDet.harga+"\" title=\""+rowNum+"\"/></td>"+
								"<td class=\"right\"><input type=\"text\" name=\"harga_"+rowNum+"\" id=\"harga_"+rowNum+"\" value=\""+Utils.formatNumber("#,##0",transDet.harga)+"\" size=\"8\"   class=\"text_field number\" title=\""+rowNum+"\"/></td>"+
								"<td class=\"right\" colspan=\"2\"><input type=\"text\" name=\"persen_diskon_"+rowNum+"\" id=\"persen_diskon_"+rowNum+"\" size=\"4\" value=\""+Utils.formatNumber("###0.00",transDet.persen_diskon)+"\"  class=\"text_field number\"  title=\""+rowNum+"\"/>"+
							 	"% (<input type=\"text\" name=\"diskon_"+rowNum+"\" id=\"diskon_"+rowNum+"\" value=\""+Utils.formatNumber("#,##0",transDet.jumlah_diskon)+"\" size=\"15\" class=\"text_field nominal\" title=\""+rowNum+"\"/>)</td>"+
							 	"<td class=\"right\"><input type=\"text\" id=\"qty_"+rowNum+"\" name=\"qty_"+rowNum+"\" value=\""+transDet.qty+"\" size=\"6\"  class=\"text_field number\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><span id=\"subtotal_"+rowNum+"\">"+Utils.formatNumber("#,##0",transDet.subTotal_harga)+"</span><input type=\"hidden\" name=\"subTotal_harga_"+rowNum+"\" id=\"subTotal_harga_"+rowNum+"\" value=\""+transDet.subTotal_harga+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+rowNum+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
							"</tr>";
					}else if(jenistrans.equals("Pembelian")){
						row +="<tr>"+
								"<td>"+rowNum+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+rowNum+"\" title=\""+rowNum+"\"/></td>"+			 	
								"<td>"+transDet.barcode_ext+"<input type=\"hidden\" name=\"item_id_"+rowNum+"\" id=\"item_id_"+rowNum+"\" value=\""+transDet.item_id+"\" title=\""+rowNum+"\"/><input type=\"hidden\" name=\"barcode_ext_"+rowNum+"\" value=\""+transDet.barcode_ext+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+transDet.item_idKet+"<input type=\"hidden\" name=\"nama_"+rowNum+"\" id=\"nama_"+rowNum+"\" value=\""+transDet.item_idKet+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\"><input type=\"text\" name=\"harga_"+rowNum+"\" id=\"harga_"+rowNum+"\" value=\""+Utils.formatNumber("#,##0",transDet.harga)+"\" size=\"8\"   class=\"text_field number\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\" colspan=\"2\"><input type=\"text\" name=\"persen_diskon_"+rowNum+"\" id=\"persen_diskon_"+rowNum+"\" size=\"3\" value=\""+Utils.formatNumber("###0.00",transDet.persen_diskon)+"\"  class=\"text_field number\"  title=\""+rowNum+"\"/>"+
							 	"% (<input type=\"text\" name=\"diskon_"+rowNum+"\" id=\"diskon_"+rowNum+"\" value=\""+Utils.formatNumber("#,##0",transDet.jumlah_diskon)+"\" size=\"8\" class=\"text_field nominal\" title=\""+rowNum+"\"/>)</td>"+
							 	"<td class=\"right\"><input type=\"text\" id=\"qty_"+rowNum+"\" name=\"qty_"+rowNum+"\" value=\""+transDet.qty+"\" size=\"3\"  class=\"text_field number\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><span id=\"subtotal_"+rowNum+"\">"+Utils.formatNumber("#,##0",transDet.subTotal_harga)+"</span><input type=\"hidden\" name=\"subTotal_harga_"+rowNum+"\" id=\"subTotal_harga_"+rowNum+"\" value=\""+transDet.subTotal_harga+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+rowNum+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
							"</tr>";
					}
					rowNum++;
				}
				
			}
			
			
			boolean adaGa=true;
			if(!isSame){
				List<Item> lsItem=dbService.selectListItem(null,kode, null, null,null,currentUser.cabang_id);
				
				if(!lsItem.isEmpty()){
					
					Double persen_diskon=diskon_persen;
					/*//harga , diskon di ambil dari inputan
					if(jenistrans.equals("Penjualan")){
						 diskon=lsItem.get(0).diskon;
						 persen_diskon=0.0;
						if(flag_ecer==1){
							harga=lsItem.get(0).harga_ecer;
							diskon=lsItem.get(0).diskon_ecer;
						}
						else{
							harga=lsItem.get(0).harga;
							diskon=lsItem.get(0).diskon;
						}
					}
					
					persen_diskon=(diskon/harga)*100;
					if(harga==0)persen_diskon=0.0;		*/
					
					Double subtotal=(harga.doubleValue()-diskon.doubleValue())*new Double(qty);
					grandTotal+=subtotal;
					Integer idx=rowNum;
					
					if(jenistrans.equals("Penjualan")){
						row +="<tr>"+
								"<td>"+idx+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+idx+"\" title=\""+idx+"\"/></td>"+			 	
								"<td>"+lsItem.get(0).barcode_ext+"<input type=\"hidden\" name=\"item_id_"+idx+"\" id=\"item_id_"+idx+"\" value=\""+lsItem.get(0).id+"\" title=\""+idx+"\"/><input type=\"hidden\" name=\"barcode_ext_"+idx+"\" value=\""+kode+"\" title=\""+idx+"\"/></td>"+
							 	"<td>"+lsItem.get(0).nama+"<input type=\"hidden\" name=\"nama_"+idx+"\" id=\"nama_"+idx+"\" value=\""+lsItem.get(0).nama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><input type=\"text\"   name=\"harga_"+idx+"\" id=\"harga_"+idx+"\" value=\""+Utils.formatNumber("#,##0",harga)+"\" size=\"8\"  class=\"text_field nominal\" title=\""+idx+"\"/></td>"+
//							 	"<td class=\"right\">"+Utils.formatNumber("#,##0",harga)+"<input type=\"hidden\" name=\"harga_"+idx+"\" id=\"harga_"+idx+"\" value=\""+harga+"\" title=\""+idx+"\"/></td>"+
								"<td class=\"right\" colspan=\"2\"><input type=\"text\" name=\"persen_diskon_"+idx+"\" id=\"persen_diskon_"+idx+"\"  size=\"4\" value=\""+Utils.formatNumber("###0.00",persen_diskon)+"\"  class=\"text_field number\"  title=\""+idx+"\"/>"+
							 	"% (<input type=\"text\" name=\"diskon_"+idx+"\" id=\"diskon_"+idx+"\" value=\""+Utils.formatNumber("#,##0",diskon)+"\" size=\"15\"  class=\"text_field nominal\" title=\""+idx+"\"/>)</td>"+
							 	"<td class=\"right\"><input type=\"text\"   id=\"qty_"+idx+"\" name=\"qty_"+idx+"\" value=\""+qty+"\" size=\"6\"  class=\"text_field number\" title=\""+idx+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><span id=\"subtotal_"+idx+"\">"+Utils.formatNumber("#,##0",subtotal)+"</span><input type=\"hidden\" name=\"subTotal_harga_"+idx+"\" id=\"subTotal_harga_"+idx+"\" value=\""+subtotal+"\" title=\""+idx+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+idx+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
						 	"</tr>";
					} else if(jenistrans.equals("Pembelian")){
						row +="<tr>"+
								"<td>"+idx+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+idx+"\" title=\""+idx+"\"/></td>"+			 	
								"<td>"+lsItem.get(0).barcode_ext+"<input type=\"hidden\" name=\"item_id_"+idx+"\" id=\"item_id_"+idx+"\" value=\""+lsItem.get(0).id+"\" title=\""+idx+"\"/><input type=\"hidden\" name=\"barcode_ext_"+idx+"\" value=\""+kode+"\" title=\""+idx+"\"/></td>"+
							 	"<td>"+lsItem.get(0).nama+"<input type=\"hidden\" name=\"nama_"+idx+"\" id=\"nama_"+idx+"\" value=\""+lsItem.get(0).nama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><input type=\"text\"   name=\"harga_"+idx+"\" id=\"harga_"+idx+"\" value=\""+Utils.formatNumber("#,##0",harga)+"\" size=\"8\"  class=\"text_field nominal\" title=\""+idx+"\"/></td>"+
								"<td class=\"right\" colspan=\"2\"><input type=\"text\" name=\"persen_diskon_"+idx+"\" id=\"persen_diskon_"+idx+"\"  size=\"3\" value=\""+Utils.formatNumber("###0.00",persen_diskon)+"\"  class=\"text_field number\"  title=\""+idx+"\"/>"+
							 	"% (<input type=\"text\" name=\"diskon_"+idx+"\" id=\"diskon_"+idx+"\" value=\""+Utils.formatNumber("#,##0",diskon)+"\" size=\"8\"  class=\"text_field nominal\" title=\""+idx+"\"/>)</td>"+
							 	"<td class=\"right\"><input type=\"text\"   id=\"qty_"+idx+"\" name=\"qty_"+idx+"\" value=\""+qty+"\" size=\"3\"  class=\"text_field number\" title=\""+idx+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><span id=\"subtotal_"+idx+"\">"+Utils.formatNumber("#,##0",subtotal)+"</span><input type=\"hidden\" name=\"subTotal_harga_"+idx+"\" id=\"subTotal_harga_"+idx+"\" value=\""+subtotal+"\" title=\""+idx+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+idx+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
						 	"</tr>";
					}
					
				}else{
					adaGa=false;
				}
			}
			row+="</tbody>";
			total_disc=(grandTotal*persen_disc)/100;
			ppn=((grandTotal-total_disc)*persen_ppn)/100;
			row +="<tfoot>" +
					"<tr>"+
						"<td colspan=\"8\"  class=\"right subTotal\">Total</td>"+
						"<td class=\"right\"><span class=\"Total subTotal\">"+Utils.formatNumber("#,##0",grandTotal)+"</span></td>"+
						"<td></td>"+
				 "</tr>";
			row +="<tr>"+
						"<td colspan=\"8\" class=\"right\" >Diskon Tambahan "+
						"</td>"+
						"<td class=\"right\">							"+
						"	<input type=\"text\" name=\"persen_disc\" id=\"persen_disc\" size=\"3\" value=\""+Utils.formatNumber("###0.00",persen_disc)+"\"  class=\"text_field number\" />% "+
						"	(<input type=\"text\" name=\"total_disc\" id=\"total_disc\" value=\""+Utils.formatNumber("#,##0",total_disc)+"\" size=\"8\" class=\"text_field  nominal\" style=\"text-align: right;\" />)"+
						"</td>"+
						"<td></td>"+
				  "</tr>";
			row +="<tr>"+
					"<td colspan=\"8\" class=\"right\" >PPN "+
					"</td>"+
					"<td class=\"right\">							"+
					"	<input type=\"text\" name=\"persen_ppn\" id=\"persen_ppn\" size=\"3\" value=\""+Utils.formatNumber("###0.00",persen_ppn)+"\"  class=\"text_field number\" />% "+
					"	<input type=\"text\" name=\"ppn\" id=\"ppn\" value=\""+Utils.formatNumber("#,##0",ppn)+"\" size=\"8\" class=\"text_field  nominal\" style=\"text-align: right;\" />"+
					"</td>"+
					"<td></td>"+
			  "</tr>";
			row +="<tr>"+
					  "<th colspan=\"8\" class=\"grandtotal right\" >Grand Total</th>"+
				  	  "<th class=\"grandtotal right\"><span class=\"grandTotal\">"+Utils.formatNumber("#,##0",(grandTotal-total_disc+ppn))+"</span></th>"+
				  	  "<th></th>"+
				  "</tr>" +
				  "</tfoot>";
				
			out = "<table width=\"100%\" class=\"tbl_repeat\">"+
					"<thead>" +
						"<tr>"+
							"<th>No.</th>"+
							"<th>Kode Produk</th>"+
							"<th>Nama Produk</th>"+
							"<th class=\"right\">@Harga</th>"+
							"<th class=\"right\" colspan=\"2\"> @Diskon</th>" +
							"<th class=\"right\">Qty</th>"+
							"<th >Satuan</th>"+
							"<th class=\"right\">Sub Total Harga</th>"+
							"<th ></th>"+
						"</tr>" +
					"</thead>";
			out +=row;
			out += "</table>" +
					"<script type=\"text/javascript\">scrollTable();</script>" +
					"";
			row = out;
				
			
			DropDown dd=new DropDown();
			dd.setKey(tbl.toString());
			dd.setValue(row);
			dd.setDesc(adaGa?"ada":"ga");
			result.add(dd);
			
		}else if(tipe.equals("removeRow")){
			Integer tbl = ServletRequestUtils.getIntParameter(request, "tbl", 0);
			String kode = ServletRequestUtils.getStringParameter(request, "barcode_ext","");
			Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
			Integer flag_ecer = ServletRequestUtils.getIntParameter(request, "flag_ecer",0);
			String jenistrans = ServletRequestUtils.getStringParameter(request, "jenistrans","");
			Double persen_disc=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "persen_disc","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "persen_disc","0").replace(",", "")));
			Double total_disc=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "total_disc","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "total_disc","0").replace(",", "")));
			Double persen_ppn=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "persen_ppn","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "persen_ppn","0").replace(",", "")));
			Double ppn=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "ppn","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "ppn","0").replace(",", "")));
			
			int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");
			
			String out="";
			String row="";
			int rowNum=1;
			row+="<tbody>";
			Double grandTotal=0.0;
			if (idxList.length!=0) {
				for(int idx:idxList){
					if(idx==id)continue;

					TransDet transDet=new TransDet();
					transDet.urut=idx;
					transDet.item_id=ServletRequestUtils.getIntParameter(request, "item_id_"+idx,0);
					transDet.qty=Integer.parseInt(ServletRequestUtils.getStringParameter(request, "qty_"+idx,"1").replace(",", ""));
					transDet.barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode_ext_"+idx,"");
					transDet.jumlah_diskon= (Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "diskon_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request,"diskon_"+idx,"0").replace(",", "")));
					transDet.harga=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "harga_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request,"harga_"+idx,"0").replace(",", "")));
					transDet.persen_diskon=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "persen_diskon_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request,"persen_diskon_"+idx,"0").replace(",", "")));
					
					List<Item> lsItem=dbService.selectListItem(null,transDet.barcode_ext, null, null,null,currentUser.cabang_id);
					if(jenistrans.equals("Penjualan")){
						if(flag_ecer==1){
							transDet.harga=lsItem.get(0).harga_ecer;
							transDet.jumlah_diskon=lsItem.get(0).diskon_ecer;
							transDet.persen_diskon=(transDet.jumlah_diskon/transDet.harga)*100;
						}
						else {
							transDet.harga=lsItem.get(0).harga;
							transDet.jumlah_diskon=lsItem.get(0).diskon;
							transDet.persen_diskon=(transDet.jumlah_diskon/transDet.harga)*100;
						}
					}
					
					if(transDet.harga==0)transDet.persen_diskon=0.0;
					transDet.subTotal_harga=(transDet.harga.doubleValue()-transDet.jumlah_diskon.doubleValue())*new Double(transDet.qty);
					transDet.item_idKet=ServletRequestUtils.getStringParameter(request, "nama_"+idx,"");
					
					grandTotal+=transDet.subTotal_harga;
					
					row+="<tbody>";					
					if(jenistrans.equals("Penjualan")){
						row +="<tr>"+
								"<td>"+rowNum+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+rowNum+"\"  title=\""+rowNum+"\"/></td>"+			 	
								"<td>"+transDet.barcode_ext+"<input type=\"hidden\" name=\"item_id_"+rowNum+"\" id=\"item_id_"+rowNum+"\" value=\""+transDet.item_id+"\" title=\""+rowNum+"\"/><input type=\"hidden\" name=\"barcode_ext_"+rowNum+"\" value=\""+transDet.barcode_ext+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+transDet.item_idKet+"<input type=\"hidden\" name=\"nama_"+rowNum+"\" id=\"nama_"+rowNum+"\" value=\""+transDet.item_idKet+"\" title=\""+rowNum+"\"/></td>"+
//							 	"<td class=\"right\">"+Utils.formatNumber("#,##0",transDet.harga)+"<input type=\"hidden\" name=\"harga_"+rowNum+"\" id=\"harga_"+rowNum+"\" value=\""+transDet.harga+"\" title=\""+rowNum+"\"/></td>"+
								"<td class=\"right\"><input type=\"text\" name=\"harga_"+rowNum+"\" id=\"harga_"+rowNum+"\" value=\""+Utils.formatNumber("#,##0",transDet.harga)+"\" size=\"8\"   class=\"text_field nominal\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\" colspan=\"2\" ><input type=\"text\" name=\"persen_diskon_"+rowNum+"\" id=\"persen_diskon_"+rowNum+"\"  size=\"3\" value=\""+Utils.formatNumber("###0.00",transDet.persen_diskon)+"\"  class=\"text_field number\"  title=\""+rowNum+"\"/>"+
							 	"% (<input type=\"text\" name=\"diskon_"+rowNum+"\" id=\"diskon_"+rowNum+"\" value=\""+Utils.formatNumber("#,##0",transDet.jumlah_diskon)+"\" size=\"8\"  class=\"text_field nominal\" title=\""+rowNum+"\"/>)</td>" +
							 	"<td class=\"right\"><input type=\"text\"  id=\"qty_"+rowNum+"\" name=\"qty_"+rowNum+"\" value=\""+transDet.qty+"\" size=\"3\"  class=\"text_field number\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><span id=\"subtotal_"+rowNum+"\">"+Utils.formatNumber("#,##0",transDet.subTotal_harga)+"</span><input type=\"hidden\" name=\"subTotal_harga_"+rowNum+"\" id=\"subTotal_harga_"+rowNum+"\" value=\""+transDet.subTotal_harga+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+rowNum+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
						 	"</tr>";
					}else if(jenistrans.equals("Pembelian")){
						row +="<tr>"+
								"<td>"+rowNum+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+rowNum+"\" title=\""+rowNum+"\"/></td>"+			 	
								"<td>"+transDet.barcode_ext+"<input type=\"hidden\" name=\"item_id_"+rowNum+"\" id=\"item_id_"+rowNum+"\" value=\""+transDet.item_id+"\" title=\""+rowNum+"\"/><input type=\"hidden\" name=\"barcode_ext_"+rowNum+"\" value=\""+transDet.barcode_ext+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+transDet.item_idKet+"<input type=\"hidden\" name=\"nama_"+rowNum+"\" id=\"nama_"+rowNum+"\" value=\""+transDet.item_idKet+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\"><input type=\"text\" name=\"harga_"+rowNum+"\" id=\"harga_"+rowNum+"\" value=\""+Utils.formatNumber("#,##0",transDet.harga)+"\" size=\"8\"   class=\"text_field nominal\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\" colspan=\"2\"><input type=\"text\" name=\"persen_diskon_"+rowNum+"\" id=\"persen_diskon_"+rowNum+"\" size=\"3\" value=\""+Utils.formatNumber("###0.00",transDet.persen_diskon)+"\"  class=\"text_field number\"  title=\""+rowNum+"\"/>"+
							 	"% (<input type=\"text\" name=\"diskon_"+rowNum+"\" id=\"diskon_"+rowNum+"\" value=\""+Utils.formatNumber("#,##0",transDet.jumlah_diskon)+"\" size=\"8\" class=\"text_field nominal\" title=\""+rowNum+"\"/>)</td>"+
							 	"<td class=\"right\"><input type=\"text\" id=\"qty_"+rowNum+"\" name=\"qty_"+rowNum+"\" value=\""+transDet.qty+"\" size=\"3\"  class=\"text_field number\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><span id=\"subtotal_"+rowNum+"\">"+Utils.formatNumber("#,##0",transDet.subTotal_harga)+"</span><input type=\"hidden\" name=\"subTotal_harga_"+rowNum+"\" id=\"subTotal_harga_"+rowNum+"\" value=\""+transDet.subTotal_harga+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+rowNum+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
							"</tr>";
					}
					rowNum++;
					row+="<tbody>";
				}
				
			}
			row+="</tbody>";
			
			total_disc=(grandTotal*persen_disc)/100;
			ppn=((grandTotal-total_disc)*persen_ppn)/100;
			row +="<tfoot>" +
					"<tr>"+
					"<td colspan=\"8\"  class=\"right subTotal\">Total</td>"+
					"<td class=\"right\"><span class=\"Total subTotal\">"+Utils.formatNumber("#,##0",grandTotal)+"</span></td>"+
					"<td></td>"+
					"</tr>" ;
			row +="<tr>"+
						"<td colspan=\"8\" class=\"right\" >Diskon Tambahan "+
						"</td>"+
						"<td class=\"right\">							"+
						"	<input type=\"text\" name=\"persen_disc\" id=\"persen_disc\" size=\"3\" value=\""+Utils.formatNumber("###0.00",persen_disc)+"\"  class=\"text_field number\" />% "+
						"	(<input type=\"text\" name=\"total_disc\" id=\"total_disc\" value=\""+Utils.formatNumber("#,##0",total_disc)+"\" size=\"9\" class=\"text_field nominal\" style=\"text-align: right;\" />)"+
						"</td>"+
						"<td></td>"+
				  "</tr>";
			row +="<tr>"+
					"<td colspan=\"8\" class=\"right\" >PPN "+
					"</td>"+
					"<td class=\"right\">							"+
					"	<input type=\"text\" name=\"persen_ppn\" id=\"persen_ppn\" size=\"3\" value=\""+Utils.formatNumber("###0.00",persen_ppn)+"\"  class=\"text_field number\" />% "+
					"	<input type=\"text\" name=\"ppn\" id=\"ppn\" value=\""+Utils.formatNumber("#,##0",ppn)+"\" size=\"8\" class=\"text_field  nominal\" style=\"text-align: right;\" />"+
					"</td>"+
					"<td></td>"+
			  "</tr>";
			row +="<tr>"+
					  "<th colspan=\"8\" class=\"grandtotal right\" >Grand Total</th>"+
				  	  "<th class=\"grandtotal right\"><span class=\"grandTotal\">"+Utils.formatNumber("#,##0",(grandTotal-total_disc+ppn))+"</span></th>"+
				  	  "<th></th>"+
				  "</tr>" +
				  "</tfoot>";
			
		out = "<table width=\"100%\" class=\"tbl_repeat\">"+
				"<thead>" +
					"<tr>"+
						"<th>No.</th>"+
						"<th>Kode Produk</th>"+
						"<th>Nama Produk</th>"+
						"<th  class=\"right\">@Harga</th>"+
						"<th class=\"right\" colspan=\"2\"> @Diskon</th>" +
						"<th class=\"right\">Qty</th>"+
						"<th >Satuan</th>"+
						"<th class=\"right\">Sub Total Harga</th>"+
						"<th ></th>"+
					"</tr>" +
				"</thead>";
			out +=row;
			out += "</table>" +
					"<script type=\"text/javascript\">scrollTable();</script>";
			row = out;
				
			
			DropDown dd=new DropDown();
			dd.setKey(tbl.toString());
			dd.setValue(row);
			result.add(dd);
			
		}else if(tipe.equals("addRowTransfer")){
			Integer tbl = ServletRequestUtils.getIntParameter(request, "tbl", 0);
			String nama= ServletRequestUtils.getStringParameter(request, "nama_produk", "");
			String kode = ServletRequestUtils.getStringParameter(request, "barcode_ext","");
			Integer qty = Integer.parseInt(ServletRequestUtils.getStringParameter(request, "qty","1").replace(",", ""));
			int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");
			
			String out="";
			String row="";
			Integer index = 1;
			boolean isSame=false;
			int rowNum=1;
			if (idxList.length!=0) {
				
				for(int idx:idxList){
					TransDet transDet=new TransDet();
					transDet.urut=idx;
					transDet.item_id=ServletRequestUtils.getIntParameter(request, "item_id_"+idx,0);
					transDet.qty=Integer.parseInt(ServletRequestUtils.getStringParameter(request, "qty_"+idx,"1").replace(",", ""));
					transDet.barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode_ext_"+idx,"");
					
					List<Item> lsItem=dbService.selectListItem(null,transDet.barcode_ext, null, null,null,currentUser.cabang_id);
					if(kode.equals(transDet.barcode_ext)){
						isSame=true;
						transDet.qty+=qty;
					}
					
					transDet.item_idKet=ServletRequestUtils.getStringParameter(request, "nama_"+idx,"");
					
					
					row +="<tr>"+
								"<td>"+rowNum+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+rowNum+"\" title=\""+rowNum+"\"/></td>"+			 	
								"<td>"+transDet.barcode_ext+"<input type=\"hidden\" name=\"item_id_"+rowNum+"\" id=\"item_id_"+rowNum+"\" value=\""+transDet.item_id+"\" title=\""+rowNum+"\"/><input type=\"hidden\" name=\"barcode_ext_"+rowNum+"\" value=\""+transDet.barcode_ext+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+transDet.item_idKet+"<input type=\"hidden\" name=\"nama_"+rowNum+"\" id=\"nama_"+rowNum+"\" value=\""+transDet.item_idKet+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\"><input type=\"text\" id=\"qty_"+rowNum+"\" name=\"qty_"+rowNum+"\" value=\""+transDet.qty+"\" size=\"6\"  class=\"text_field number\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+rowNum+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
							"</tr>";
					rowNum++;
				}
				
			}
			
			
			boolean adaGa=true;
			if(!isSame){
				List<Item> lsItem=dbService.selectListItem(null,kode, null, null,null,currentUser.cabang_id);
				
				if(!lsItem.isEmpty()){
					
					Integer idx=rowNum;
					row +="<tr>"+
								"<td>"+idx+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+idx+"\" title=\""+idx+"\"/></td>"+			 	
								"<td>"+lsItem.get(0).barcode_ext+"<input type=\"hidden\" name=\"item_id_"+idx+"\" id=\"item_id_"+idx+"\" value=\""+lsItem.get(0).id+"\" title=\""+idx+"\"/><input type=\"hidden\" name=\"barcode_ext_"+idx+"\" value=\""+kode+"\" title=\""+idx+"\"/></td>"+
							 	"<td>"+lsItem.get(0).nama+"<input type=\"hidden\" name=\"nama_"+idx+"\" id=\"nama_"+idx+"\" value=\""+lsItem.get(0).nama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\"><input type=\"text\"   id=\"qty_"+idx+"\" name=\"qty_"+idx+"\" value=\""+qty+"\" size=\"6\"  class=\"text_field number\" title=\""+idx+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+idx+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
						 	"</tr>";
				}else{
					adaGa=false;
				}
			}
			
			out = "<table width=\"100%\" class=\"tbl_repeat\">"+
					"<thead>" +
						"<tr>"+
							"<th>No.</th>"+
							"<th>Kode Produk</th>"+
							"<th>Nama Produk</th>"+
							"<th class=\"right\">Qty</th>"+
							"<th >Satuan</th>"+
							"<th ></th>"+
						"</tr>" +
					"</thead>" +
					"<tbody>";
			out +=row;
			out += "</tbody>" +
				"</table>";
			row = out;
				
			
			DropDown dd=new DropDown();
			dd.setKey(tbl.toString());
			dd.setValue(row);
			dd.setDesc(adaGa?"ada":"ga");
			result.add(dd);
			
		}else if(tipe.equals("removeRowTransfer")){
			Integer tbl = ServletRequestUtils.getIntParameter(request, "tbl", 0);
			String kode = ServletRequestUtils.getStringParameter(request, "barcode_ext","");
			Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
			int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");
			
			String out="";
			String row="";
			int rowNum=1;

			if (idxList.length!=0) {
				for(int idx:idxList){
					if(idx==id)continue;
					
					TransDet transDet=new TransDet();
					transDet.urut=idx;
					transDet.item_id=ServletRequestUtils.getIntParameter(request, "item_id_"+idx,0);
					transDet.qty=Integer.parseInt(ServletRequestUtils.getStringParameter(request, "qty_"+idx,"1").replace(",", ""));
					transDet.barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode_ext_"+idx,"");
					
					List<Item> lsItem=dbService.selectListItem(null,transDet.barcode_ext, null, null,null,currentUser.cabang_id);
					transDet.item_idKet=ServletRequestUtils.getStringParameter(request, "nama_"+idx,"");
					
					row +="<tr>"+
								"<td>"+rowNum+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+rowNum+"\"  title=\""+rowNum+"\"/></td>"+			 	
								"<td>"+transDet.barcode_ext+"<input type=\"hidden\" name=\"item_id_"+rowNum+"\" id=\"item_id_"+rowNum+"\" value=\""+transDet.item_id+"\" title=\""+rowNum+"\"/><input type=\"hidden\" name=\"barcode_ext_"+rowNum+"\" value=\""+transDet.barcode_ext+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+transDet.item_idKet+"<input type=\"hidden\" name=\"nama_"+rowNum+"\" id=\"nama_"+rowNum+"\" value=\""+transDet.item_idKet+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\"><input type=\"text\"  id=\"qty_"+rowNum+"\" name=\"qty_"+rowNum+"\" value=\""+transDet.qty+"\" size=\"6\"  class=\"text_field number\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+lsItem.get(0).satuan_idNama+"<input type=\"hidden\" name=\"satuan_"+idx+"\" id=\"satuan_"+idx+"\" value=\""+lsItem.get(0).satuan_idNama+"\" title=\""+idx+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+rowNum+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
						 	"</tr>";
					
					
					rowNum++;
				
				}
			}
			
			out = "<table width=\"100%\" class=\"tbl_repeat\">"+
				"<thead>" +
				"<tr>"+
					"<th>No.</th>"+
					"<th>Kode Produk</th>"+
					"<th>Nama Produk</th>"+
					"<th class=\"right\">Qty</th>"+
					"<th >Satuan</th>"+
					"<th ></th>"+
				"</tr>" +
				"</thead>" +
				"<tbody>";
			out +=row;
			out += "</tbody>" +
					"</table>";
			row = out;
			
			DropDown dd=new DropDown();
			dd.setKey(tbl.toString());
			dd.setValue(row);
			result.add(dd);
			
		}else if(tipe.equals("addRowCashOther")){
			Integer tbl = ServletRequestUtils.getIntParameter(request, "tbl", 0);
			String nama= ServletRequestUtils.getStringParameter(request, "nama", "");
			String coa_id = ServletRequestUtils.getStringParameter(request, "coa_id","");
			Double nominal=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "nominal","0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "nominal","0").replace(",", "")));
			String dk = ServletRequestUtils.getStringParameter(request, "dk","");
			int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");
			Double jumlahDebet = 0.;
			Double jumlahKredit = 0.;
			if(dk.equals("D")){
				jumlahDebet=nominal;
			}else{
				jumlahKredit=nominal;
			}
			
			String out="";
			String row="";
			Integer index = 1;
			boolean isSame=false;
			int rowNum=1;
			if (idxList.length!=0) {
				
				for(int idx:idxList){
					TrxDet trxDet= new TrxDet();
					trxDet.urut=idx;
					trxDet.coa_id=ServletRequestUtils.getStringParameter(request, "coa_id_"+idx,"");
					trxDet.ket=ServletRequestUtils.getStringParameter(request, "ket_"+idx,"");
					trxDet.jumlahDebet=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "jumlahDebet_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "jumlahDebet_"+idx,"0").replace(",", "")));
					trxDet.jumlahKredit=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "jumlahKredit_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "jumlahKredit_"+idx,"0").replace(",", "")));
					if(coa_id.equals(trxDet.coa_id)){
						isSame=true;
						trxDet.jumlahDebet+=jumlahDebet;
						trxDet.jumlahKredit+=jumlahKredit;
					}
					row +="<tr>"+
							"<td>"+rowNum+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+rowNum+"\"  title=\""+rowNum+"\"/></td>"+			 	
							"<td>"+trxDet.coa_id+"<input type=\"hidden\" name=\"coa_id_"+rowNum+"\" id=\"coa_id_"+rowNum+"\" value=\""+trxDet.coa_id+"\" title=\""+rowNum+"\"/><input type=\"hidden\" name=\"coa_id_"+rowNum+"\" value=\""+trxDet.coa_id+"\" title=\""+rowNum+"\"/></td>"+
						 	"<td><input style=\"text-align: left;\" type=\"text\" name=\"ket_"+rowNum+"\" id=\"ket_"+rowNum+"\" value=\""+trxDet.ket+"\" title=\""+rowNum+"\"/></td>"+
						 	"<td class=\"right\">"+Utils.formatNumber("#,##0",trxDet.jumlahDebet)+"<input type=\"hidden\" name=\"jumlahDebet_"+rowNum+"\" id=\"jumlahDebet_"+rowNum+"\" value=\""+trxDet.jumlahDebet+"\" title=\""+rowNum+"\"/></td>"+
						 	"<td class=\"right\">"+Utils.formatNumber("#,##0",trxDet.jumlahKredit)+"<input type=\"hidden\" name=\"jumlahKredit_"+rowNum+"\" id=\"jumlahKredit_"+rowNum+"\" value=\""+trxDet.jumlahKredit+"\" title=\""+rowNum+"\"/></td>"+
						 	"<td ><a href=\"#\" class=\"remove\" rel=\""+rowNum+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
					 	"</tr>";
					rowNum++;
				}
				
			}
			
			
			boolean adaGa=true;
			if(!isSame){
				List<COA> lsCoa=dbService.selectListCoaAuto(null, coa_id, null, null);
				Integer idx=rowNum;
				if(!lsCoa.isEmpty()){
					row +="<tr>"+
								"<td>"+idx+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+idx+"\" title=\""+idx+"\"/></td>"+			 	
								"<td>"+lsCoa.get(0).id+"<input type=\"hidden\" name=\"coa_id_"+idx+"\" id=\"coa_id_"+idx+"\" value=\""+lsCoa.get(0).id+"\" title=\""+idx+"\"/><input type=\"hidden\" name=\"coa_id_"+idx+"\" value=\""+coa_id+"\" title=\""+idx+"\"/></td>"+
							 	"<td> <input type=\"text\" style=\"text-align: left;\" name=\"ket_"+idx+"\" id=\"ket_"+idx+"\" value=\""+lsCoa.get(0).nama+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\">"+Utils.formatNumber("#,##0",jumlahDebet)+"<input type=\"hidden\" name=\"jumlahDebet_"+idx+"\" id=\"jumlahDebet_"+idx+"\" value=\""+jumlahDebet+"\" title=\""+idx+"\"/></td>"+
							 	"<td class=\"right\">"+Utils.formatNumber("#,##0",jumlahKredit)+"<input type=\"hidden\" name=\"jumlahKredit_"+idx+"\" id=\"jumlahKredit_"+idx+"\" value=\""+jumlahKredit+"\" title=\""+idx+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+idx+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
						 	"</tr>";
				}else{
					row +="<tr>"+
							"<td>"+idx+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+idx+"\" title=\""+idx+"\"/></td>"+			 	
							"<td><input type=\"hidden\" name=\"coa_id_"+idx+"\" id=\"coa_id_"+idx+"\" value=\"\" title=\""+idx+"\"/><input type=\"hidden\" name=\"coa_id_"+idx+"\" value=\""+coa_id+"\" title=\""+idx+"\"/></td>"+
						 	"<td> <input type=\"text\" style=\"text-align: left;\" name=\"ket_"+idx+"\" id=\"ket_"+idx+"\" value=\"\" title=\""+idx+"\"/></td>"+
						 	"<td class=\"right\">"+Utils.formatNumber("#,##0",jumlahDebet)+"<input type=\"hidden\" name=\"jumlahDebet_"+idx+"\" id=\"jumlahDebet_"+idx+"\" value=\""+jumlahDebet+"\" title=\""+idx+"\"/></td>"+
						 	"<td class=\"right\">"+Utils.formatNumber("#,##0",jumlahKredit)+"<input type=\"hidden\" name=\"jumlahKredit_"+idx+"\" id=\"jumlahKredit_"+idx+"\" value=\""+jumlahKredit+"\" title=\""+idx+"\"/></td>"+
						 	"<td ><a href=\"#\" class=\"remove\" rel=\""+idx+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
					 	"</tr>";
				}
			}
			
			out = "<table width=\"100%\" class=\"tbl_repeat\">"+
					"<thead>" +
						"<tr>"+
							"<th>No.</th>"+
							"<th>Chart Of Account</th>"+
							"<th>Keterangan</th>"+
							"<th class=\"right\">Debet</th>"+
							"<th class=\"right\">Kredit</th>"+
							"<th></th>"+
						"</tr>" +
					"</thead>" +
					"<tbody>";
			out +=row;
			out += "</tbody>" +
					"</table>";
			row = out;
				
			
			DropDown dd=new DropDown();
			dd.setKey(tbl.toString());
			dd.setValue(row);
			dd.setDesc(adaGa?"ada":"ga");
			result.add(dd);
			
		}else if(tipe.equals("removeRowCashOther")){
			Integer tbl = ServletRequestUtils.getIntParameter(request, "tbl", 0);
			String kode = ServletRequestUtils.getStringParameter(request, "barcode_ext","");
			Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
			String dk = ServletRequestUtils.getStringParameter(request, "dk","");
			int[]idxList=ServletRequestUtils.getIntParameters(request, "idx");
			Double jumlahDebet = 0.;
			Double jumlahKredit = 0.;
			
			String out="";
			String row="";
			int rowNum=1;

			Double grandTotal=0.0;
			if (idxList.length!=0) {
				for(int idx:idxList){
					if(idx==id)continue;

					TrxDet trxDet = new TrxDet();
					trxDet.urut=idx;
					trxDet.coa_id=ServletRequestUtils.getStringParameter(request, "coa_id_"+idx,"");
					trxDet.ket=ServletRequestUtils.getStringParameter(request, "ket_"+idx,"");
					trxDet.jumlahDebet=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "jumlahDebet_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "jumlahDebet_"+idx,"0").replace(",", "")));
					trxDet.jumlahKredit=(Double) (Utils.isEmpty(ServletRequestUtils.getStringParameter(request, "jumlahKredit_"+idx,"0"))?0.0:new Double(ServletRequestUtils.getStringParameter(request, "jumlahKredit_"+idx,"0").replace(",", "")));
					trxDet.dk=ServletRequestUtils.getStringParameter(request, "dk_"+idx,"");
					row +="<tr>"+
								"<td>"+rowNum+"<input type=\"hidden\" name=\"idx\" id=\"idx\" value=\""+rowNum+"\"  title=\""+rowNum+"\"/></td>"+			 	
								"<td>"+trxDet.coa_id+"<input type=\"hidden\" name=\"coa_id_"+rowNum+"\" id=\"coa_id_"+rowNum+"\" value=\""+trxDet.coa_id+"\" title=\""+rowNum+"\"/><input type=\"hidden\" name=\"coa_id_"+rowNum+"\" value=\""+trxDet.coa_id+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td>"+trxDet.ket+"<input type=\"hidden\" style=\"text-align: left;\" name=\"ket_"+rowNum+"\" id=\"ket_"+rowNum+"\" value=\""+trxDet.ket+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\">"+Utils.formatNumber("#,##0",trxDet.jumlahDebet)+"<input type=\"hidden\" name=\"jumlahDebet_"+rowNum+"\" id=\"jumlahDebet_"+rowNum+"\" value=\""+trxDet.jumlahDebet+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td class=\"right\">"+Utils.formatNumber("#,##0",trxDet.jumlahKredit)+"<input type=\"hidden\" name=\"jumlahKredit_"+rowNum+"\" id=\"jumlahKredit_"+rowNum+"\" value=\""+trxDet.jumlahKredit+"\" title=\""+rowNum+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+rowNum+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /> </a> </td>"+
						 	"</tr>";
					rowNum++;
				}
			}
			
			out = "<table width=\"100%\" class=\"tbl_repeat\">"+
				"<thead>" +
					"<tr>"+
						"<th>No.</th>"+
						"<th>Chart Of Account</th>"+
						"<th>Keterangan</th>"+
						"<th class=\"right\">Debet</th>"+
						"<th class=\"right\">Kredit</th>"+
						"<th></th>"+
					"</tr>" +
				"</thead>" +
				"<tbody>";
			
			out +=row;
			out += "<tbody>" +
					"</table>";
			row = out;
			
			DropDown dd=new DropDown();
			dd.setKey(tbl.toString());
			dd.setValue(row);
			result.add(dd);
			
		}else if(tipe.equals("addRowDelivery")){
			Integer tbl = ServletRequestUtils.getIntParameter(request, "tbl", 0);
			String no_trans = ServletRequestUtils.getStringParameter(request, "no_trans","");
			int[]trans_idList=ServletRequestUtils.getIntParameters(request, "trans_id");
			
			String out="";
			String row="";
			Integer index = 1;
			boolean isSame=false;
			int rowNum=1;
			if (trans_idList.length!=0) {
				
				for(int trans_id:trans_idList){
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
					}
					
					if(no_trans.equals(dd.no_trans))isSame=true;
					
					row +="<tr>"+
							"<td>"+rowNum+"<input type=\"hidden\" name=\"trans_id\" id =\"trans_id\" value=\""+dd.trans_id+"\" title=\""+dd.trans_id+"\"/></td>"+
							"<td>"+dd.no_trans+"<input type=\"hidden\" name=\"no_trans_"+dd.trans_id+"\" id=\"no_trans_"+dd.trans_id+"\" value=\""+dd.no_trans+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td>"+dd.contact_tujuan+"<input type=\"hidden\" name=\"contact_tujuan_"+dd.trans_id+"\" id=\"contact_tujuan_"+dd.trans_id+"\" value=\""+dd.contact_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td>"+dd.alamat_tujuan+"<input type=\"hidden\" name=\"alamat_tujuan_"+dd.trans_id+"\" id=\"alamat_tujuan_"+dd.trans_id+"\" value=\""+dd.alamat_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td>"+dd.telp_tujuan+"<input type=\"hidden\" name=\"telp_tujuan_"+dd.trans_id+"\" id=\"telp_tujuan_"+dd.trans_id+"\" value=\""+dd.telp_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td>"+Utils.defaultDFLong.format(dd.tgl_kirim_est)+"<input type=\"hidden\" name=\"tgl_kirim_est_"+dd.trans_id+"\" id=\"tgl_kirim_est_"+dd.trans_id+"\" value=\""+Utils.defaultDFLong.format(dd.tgl_kirim_est)+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td ><a href=\"#\" class=\"remove\" rel=\""+dd.trans_id+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /></a></td>"+
							"</tr>";
					rowNum++;
				}
				
			}
			
			
			Integer aksescabang_id=null;
			if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
			
			boolean adaGa=true;
			String note="";
			if(!isSame){
				List<Trans> lsTrans=dbService.selectListTrans(null, null, null, no_trans,aksescabang_id);
				
				
				if(!lsTrans.isEmpty()){
					Trans t=lsTrans.get(0);
					
					if(t.delivery_id!=null&"1,2".contains(""+t.status_kirim)){// klo sudah di set pengiriman dan statusnya masih dalam proses tidak boleh
						adaGa=false;
						Delivery d=dbService.selectListDelivery(t.delivery_id, null,null).get(0);
						note="sudah terdaftar di kode delivery "+d.kode;
					}else{
					
						DeliveryDet dd=new DeliveryDet();
						dd.trans_id=t.trans_id;
						dd.alamat_tujuan=t.alamat_tujuan;
						dd.contact_tujuan=t.contact_tujuan;
						dd.telp_tujuan=t.telp_tujuan;
						dd.no_trans=t.no_trans;
						dd.tgl_kirim_est=t.tgl_kirim_est==null?dbService.selectSysdate():t.tgl_kirim_est;
	
						Integer idx=rowNum;
						row +="<tr>"+
								"<td>"+rowNum+"<input type=\"hidden\" name=\"trans_id\" id =\"trans_id\" value=\""+dd.trans_id+"\" title=\""+dd.trans_id+"\"/></td>"+
								"<td>"+dd.no_trans+"<input type=\"hidden\" name=\"no_trans_"+dd.trans_id+"\" id=\"no_trans_"+dd.trans_id+"\" value=\""+dd.no_trans+"\"  title=\""+dd.trans_id+"\"/></td>"+
							 	"<td>"+dd.contact_tujuan+"<input type=\"hidden\" name=\"contact_tujuan_"+dd.trans_id+"\" id=\"contact_tujuan_"+dd.trans_id+"\" value=\""+dd.contact_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
							 	"<td>"+dd.alamat_tujuan+"<input type=\"hidden\" name=\"alamat_tujuan_"+dd.trans_id+"\" id=\"alamat_tujuan_"+dd.trans_id+"\" value=\""+dd.alamat_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
							 	"<td>"+dd.telp_tujuan+"<input type=\"hidden\" name=\"telp_tujuan_"+dd.trans_id+"\" id=\"telp_tujuan_"+dd.trans_id+"\" value=\""+dd.telp_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
							 	"<td>"+Utils.defaultDFLong.format(dd.tgl_kirim_est)+"<input type=\"hidden\" name=\"tgl_kirim_est_"+dd.trans_id+"\" id=\"tgl_kirim_est_"+dd.trans_id+"\" value=\""+Utils.defaultDFLong.format(dd.tgl_kirim_est)+"\"  title=\""+dd.trans_id+"\"/></td>"+
							 	"<td ><a href=\"#\" class=\"remove\" rel=\""+dd.trans_id+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /></a></td>"+
								"</tr>";
					}
				}else{
					adaGa=false;
				}
			}
			
			out = "<table width=\"100%\" class=\"tbl_repeat\">"+
					"<thead>" +
					"<tr>"+
						"<th>No.</th>"+
						"<th>Kode Transaksi</th>"+
						"<th>Nama Tujuan</th>"+
						"<th>Alamat Tujuan</th>"+
						"<th>Telepon Tujuan</th>"+
						"<th>Estimasi Tgl Kirim</th>"+							
						"<th></th>"+
					"</tr>" +
					"</thead>" +
					"<tbody>";
			out +=row;
			out += "<tbody>" +
					"</table>";
			row = out;
				
			
			DropDown dd=new DropDown();
			dd.setKey(tbl.toString());
			dd.setValue(row);
			if(!note.equals(""))dd.setDesc(note);
			else dd.setDesc(adaGa?"ada":"ga");
			result.add(dd);
			
		}else if(tipe.equals("removeRowDelivery")){
			Integer tbl = ServletRequestUtils.getIntParameter(request, "tbl", 0);
			//String no_trans = ServletRequestUtils.getStringParameter(request, "no_trans","");
			Integer id = ServletRequestUtils.getIntParameter(request, "id", 0);
			int[]trans_idList=ServletRequestUtils.getIntParameters(request, "trans_id");
			
			String out="";
			String row="";
			int rowNum=1;

			if (trans_idList.length!=0) {
				for(int trans_id:trans_idList){
					if(trans_id==id)continue;

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
					}
					
					
					row +="<tr>"+
							"<td>"+rowNum+"<input type=\"hidden\" name=\"trans_id\" id =\"trans_id\" value=\""+dd.trans_id+"\" title=\""+dd.trans_id+"\"/></td>"+
							"<td>"+dd.no_trans+"<input type=\"hidden\" name=\"no_trans_"+dd.trans_id+"\" id=\"no_trans_"+dd.trans_id+"\" value=\""+dd.no_trans+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td>"+dd.contact_tujuan+"<input type=\"hidden\" name=\"contact_tujuan_"+dd.trans_id+"\" id=\"contact_tujuan_"+dd.trans_id+"\" value=\""+dd.contact_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td>"+dd.alamat_tujuan+"<input type=\"hidden\" name=\"alamat_tujuan_"+dd.trans_id+"\" id=\"alamat_tujuan_"+dd.trans_id+"\" value=\""+dd.alamat_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td>"+dd.telp_tujuan+"<input type=\"hidden\" name=\"telp_tujuan_"+dd.trans_id+"\" id=\"telp_tujuan_"+dd.trans_id+"\" value=\""+dd.telp_tujuan+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td>"+Utils.defaultDFLong.format(dd.tgl_kirim_est)+"<input type=\"hidden\" name=\"tgl_kirim_est_"+dd.trans_id+"\" id=\"tgl_kirim_est_"+dd.trans_id+"\" value=\""+Utils.defaultDFLong.format(dd.tgl_kirim_est)+"\"  title=\""+dd.trans_id+"\"/></td>"+
						 	"<td ><a href=\"#\" class=\"remove\" rel=\""+dd.trans_id+"\"><img src=\""+request.getContextPath()+"/static/decorator/main/pilu/images/icons/delete.png\" alt=\"Delete\" /></a></td>"+
							"</tr>";
					rowNum++;
				
				}
			}
			
			out = "<table width=\"100%\" class=\"tbl_repeat\">"+
					"<thead>" +
						"<tr>"+
							"<th>No.</th>"+
							"<th>Kode Transaksi</th>"+
							"<th>Nama Tujuan</th>"+
							"<th>Alamat Tujuan</th>"+
							"<th>Telepon Tujuan</th>"+
							"<th>Estimasi Tgl Kirim</th>"+							
							"<th></th>"+
						"</tr>" +
					"</thead>" +
					"<tbody>";
			out +=row;
			out += "</tbody>" +
					"</table>";
			row = out;
			
			DropDown dd=new DropDown();
			dd.setKey(tbl.toString());
			dd.setValue(row);
			result.add(dd);
			
		}else if(tipe.equals("completeItemPrice")){
			String nama_produk=ServletRequestUtils.getStringParameter(request, "nama_produk", null);
			String barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode_ext", null);
//			List<Item> lsItem=dbService.selectListItem(null,barcode_ext, null, null, nama_produk,currentUser.cabang_id);
			List<Item> lsItem=dbService.selectListItem(barcode_ext,null, null, nama_produk, null,currentUser.cabang_id);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsItem));
			out.close();
			return null;
		}else if(tipe.equals("autocompleteItemPrice")){
			String nama_produk=ServletRequestUtils.getStringParameter(request, "nama_produk", null);
			String barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode_ext", null);
			List<Item> lsItem=dbService.selectListItem(barcode_ext,null, null, nama_produk,null,currentUser.cabang_id);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsItem));
			out.close();
			return null;
		}else if(tipe.equals("completeItem")){
			String nama_produk=ServletRequestUtils.getStringParameter(request, "nama_produk", null);
			result=dbService.selectDropDown("barcode_ext", "nama", "lst_item", "nama like '%"+nama_produk+"%'", "nama");
			
			
		}else if(tipe.equals("completeKaryawan")){
			String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
			String nik=ServletRequestUtils.getStringParameter(request, "nik", null);
			List<Karyawan> lsKaryawan=dbService.selectListKaryawanAuto(null, nama, null, null, nik,3);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsKaryawan));
			out.close();
			return null;
		}else if(tipe.equals("autocompleteKaryawan")){
			String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
			String nik=ServletRequestUtils.getStringParameter(request, "nik", null); 
			List<Karyawan> lsKaryawan=dbService.selectListKaryawanAuto(nama,null , null, nik,null,3);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsKaryawan));
			out.close();
			return null;
		}else if(tipe.equals("completeSupplier")){
			String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
			String kode=ServletRequestUtils.getStringParameter(request, "kode", null);
			List<Supplier> lsSupplier=dbService.selectListSupplierAuto(null, nama, null, null, kode);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsSupplier));
			out.close();
			return null;
		}else if(tipe.equals("autocompleteSupplier")){
			String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
			String kode=ServletRequestUtils.getStringParameter(request, "kode", null);
			List<Supplier> lsSupplier=dbService.selectListSupplierAuto(nama,null , null, kode,null);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsSupplier));
			out.close();
			return null;
		}else if(tipe.equals("completeCustomer")){
			String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
			String kode=ServletRequestUtils.getStringParameter(request, "kode", null);
			List<Customer> lsCustomer=dbService.selectListCustomerAuto(null, nama, null, null, kode);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsCustomer));
			out.close();
			return null;
		}else if(tipe.equals("autocompleteCustomer")){
			String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
			String kode=ServletRequestUtils.getStringParameter(request, "kode", null);
			List<Customer> lsCustomer=dbService.selectListCustomerAuto(nama,null , null, kode,null);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsCustomer));
			out.close();
			return null;
		}else if(tipe.equals("autocompleteTrans")){
			String dk=ServletRequestUtils.getStringParameter(request, "dk", "");
			String no_trans=ServletRequestUtils.getStringParameter(request, "no_trans", null);
			Integer jenis=4;
			if(dk.equals("D")){
				jenis=3;
			}
			List<Trans> lsTrans=dbService.selectListTransAuto(no_trans, null, jenis, 2);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsTrans));
			out.close();
			return null;
		}else if(tipe.equals("completeTrans")){
			String dk=ServletRequestUtils.getStringParameter(request, "dk", "");
			String no_trans=ServletRequestUtils.getStringParameter(request, "no_trans", null);
			Integer jenis=4;
			if(dk.equals("D")){
				jenis=3;
			}
			List<Trans> lsTrans=dbService.selectListTransAuto(null, no_trans, jenis, 2);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsTrans));
			out.close();
			return null;
		}else if(tipe.equals("autocompleteCoa")){
			String coa_id=ServletRequestUtils.getStringParameter(request, "coa_id", null);
			String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
			List<COA> lsCoa=dbService.selectListCoaAuto( coa_id, null, nama, null);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsCoa));
			out.close();
			return null;
		}else if(tipe.equals("completeCoa")){
			String coa_id=ServletRequestUtils.getStringParameter(request, "coa_id", null);
			String nama=ServletRequestUtils.getStringParameter(request, "nama", null);
			List<COA> lsCoa=dbService.selectListCoaAuto( null, coa_id, null, nama);
			
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			Gson gson = new Gson();
			out.print(gson.toJson(lsCoa));
			out.close();
			return null;
		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		Gson gson = new Gson();
		out.print(gson.toJson(result));
		out.close();
		
		return null;
	}
	
	
	
	@RequestMapping("/admin/test")
	public static String test()throws MessagingException {
		logger.debug("Halaman: HOME");
		
		return "/master/test";
	}
	
	//input baru
	@RequestMapping(value="/admin/test/save", method=RequestMethod.POST)
	public String save( BindingResult result, HttpServletRequest request, Model model, RedirectAttributes ra) throws MailException, MessagingException {
		logger.debug("Halaman:  Master Test Save, method: SAVE");
		User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		return "master/master_category_edit";
	}
	
	@RequestMapping(value="/stockall",method={RequestMethod.GET, RequestMethod.POST})
	public String stockall(Model model,HttpServletRequest request) {
		logger.debug("Halaman: Stock Cabang");

		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="barcode_ext",sort_type=null,no_trans=null, barcode_ext=null;
		List<Stock> listPaging = null;

		Date periode=dbService.selectSysdate();

		//reference data utk dropdown
		int[] listNumRows = new int[]{5,10,15, 20,25, 30, 40, 50};
		search=ServletRequestUtils.getStringParameter(request, "s", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "s", "");
		sort=ServletRequestUtils.getStringParameter(request, "sort", "").equals("")?sort:ServletRequestUtils.getStringParameter(request, "sort", "");
		sort_type=ServletRequestUtils.getStringParameter(request, "st", "asc");
		barcode_ext=ServletRequestUtils.getStringParameter(request, "barcode", "").equals("")?null :ServletRequestUtils.getStringParameter(request, "barcode", "");
		
		//perhitungan paging
		rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);

		totalData=dbService.selectListStockItemPagingCount(search, barcode_ext, null, periode);

		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X

		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)

		if(offset<0)offset=0;

		listPaging=dbService.selectListStockItem(barcode_ext, null, periode);
		
		
		

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
		model.addAttribute("barcode", barcode_ext);




		return "trans/stock_list";
	}
	
	@RequestMapping(value="/transaksi/prosesclosingstock",method={RequestMethod.GET, RequestMethod.POST})
	public String closingStock(Model model,HttpServletRequest request,  HttpServletResponse response, RedirectAttributes ra) {
		logger.debug("Halaman: Closing Stock");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Date periode=dbService.selectClosingPeriode(1, currentUser.getCabang_id());
		Date now=dbService.selectSysdate();
		String pesan = ServletRequestUtils.getStringParameter(request, "message", "");
		
		if (request.getParameter("process")!=null) {
			if(now.before(FormatDate.add(FormatDate.add(periode, Calendar.MONTH, 1), Calendar.DATE, -1)) ){
				model.addAttribute("messageType", "error");
				pesan=messageSource.getMessage("submitfailed", new String[]{"Closing Stock",null," Tidak dapat dilakukan karena belum masuk masa Closing"},null,null);
				model.addAttribute("message","Closing Stock Tidak dapat dilakukan karena belum masuk masa Closing" );
			}else{
				try{
					pesan=dbService.saveClosingStock(periode, currentUser);
					
//					model.addAttribute("messageType", "done");
//					model.addAttribute("message",pesan );
					periode=dbService.selectClosingPeriode(1, currentUser.getCabang_id());
//					response.sendRedirect(request.getContextPath()+"/transaksi/prosesclosingstock?message="+pesan);
					ra.addFlashAttribute("messageType", "done");
					ra.addFlashAttribute("message", pesan);
					return "redirect:/transaksi/prosesclosingstock";
				}catch (Exception e) {
					pesan=messageSource.getMessage("submitfailed", new String[]{"Closing Stock","Cabang "+currentUser.namaCabang+" Periode "+Utils.convertDateToString(periode, "MMMMM yyyy"),"diproses (sudah pernah diproses sebelumnya)"},null);
					e.printStackTrace();
					email.send(
							true, props.getProperty("email.from"),
							props.getProperty("admin.email.to").split( ";" ), null, null,
							"ERROR pada eStock", Utils.errorExtract(e), null);
					model.addAttribute("messageType", "error");
					model.addAttribute("message",pesan );
						
				}
			}
			
		}
		model.addAttribute("periode", periode);
		model.addAttribute("user", currentUser);
		return "trans/closing";
	}
	
	@RequestMapping(value="/keuangan/prosesclosingaccounting",method={RequestMethod.GET, RequestMethod.POST})
	public String closingAccounting(Model model,HttpServletRequest request, RedirectAttributes ra) {
		logger.debug("Halaman: Closing");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Date periode=dbService.selectClosingPeriode(2, null);
	
		
		if (request.getParameter("process")!=null) {
			try{
				String pesan=dbService.saveClosingAccounting(periode, currentUser);
				
//				model.addAttribute("messageType", "done");
//				model.addAttribute("message",pesan );
				periode=dbService.selectClosingPeriode(2, null);
				ra.addFlashAttribute("messageType", "done");
				ra.addFlashAttribute("message", pesan);
				return "redirect:/keuangan/prosesclosingaccounting";
			}catch (Exception e) {
				String pesan=messageSource.getMessage("submitfailed", new String[]{"Closing Accounting","Cabang "+currentUser.namaCabang+" Periode "+Utils.convertDateToString(periode, "MMMMM yyyy"),"diproses (sudah pernah diproses sebelumnya)"},null);
				e.printStackTrace();
				email.send(
						true, props.getProperty("email.from"),
						props.getProperty("admin.email.to").split( ";" ), null, null,
						"ERROR pada eStock", Utils.errorExtract(e), null);

				model.addAttribute("messageType", "error");
				model.addAttribute("message",pesan );

				
			}
		}
		
		model.addAttribute("periode", periode);
		model.addAttribute("user", currentUser);
		return "trans/closing_acc";
	}
	
	
	@RequestMapping(value="/keuangan/prosespayroll",method={RequestMethod.GET, RequestMethod.POST})
	public String processPayroll(Model model,HttpServletRequest request, RedirectAttributes ra) {
		logger.debug("Halaman: Closing");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Date periode=dbService.selectClosingPeriode(3, null);
	
		String pesan="";
		String messageType="";
		try{
			pesan=dbService.saveProcessPayroll(periode, currentUser);
			periode=dbService.selectClosingPeriode(3, null);
			messageType="done";
		}catch (Exception e) {
			pesan=messageSource.getMessage("submitfailed", new String[]{"Process Payroll","Cabang "+currentUser.namaCabang+" Periode "+Utils.convertDateToString(periode, "MMMMM yyyy"),"diproses (sudah pernah diproses sebelumnya)"},null);
			e.printStackTrace();
			email.send(
					true, props.getProperty("email.from"),
					props.getProperty("admin.email.to").split( ";" ), null, null,
					"ERROR pada eStock", Utils.errorExtract(e), null);

			messageType="error";
		}
		
		ra.addFlashAttribute("periode", periode);
		ra.addFlashAttribute("messageType", messageType);
		ra.addFlashAttribute("message", pesan);
		return "redirect:/payroll";
	}
	
	@RequestMapping("/changepass")
	public String changepass(HttpServletRequest request, @ModelAttribute("user") User user, BindingResult result, Model model, RedirectAttributes ra) {
		logger.debug("Halaman: CHANGE PASSWORD");
		
		User currentUser = (User) request.getSession().getAttribute("currentUser");
		user.setId(currentUser.getId());
		user.setUsername(currentUser.getUsername());
		
		if(request.getParameter("newPassword") != null){
			ValidationUtils.rejectIfEmpty(result, "newPassword", "NotEmpty", new String[]{""});
			ValidationUtils.rejectIfEmpty(result, "confirmPassword", "NotEmpty", new String[]{""});
			if (!result.hasErrors()) {
				User tmp = currentUser;
				if(!tmp.getPasswordDecrypt().equals(user.password)) {
					result.rejectValue("password", "", "salah");
				}else if(!user.getNewPassword().equals(user.getConfirmPassword())){
					result.rejectValue("newPassword", "", "tidak sama.");
				}else if(user.getNewPassword().length() < 6 || user.getNewPassword().length() > 20){
					result.rejectValue("newPassword", "", "harus diantara 6-20 karakter.");
				}
			}
			
			//bila ada error, kembalikan ke halaman edit
			if (result.hasErrors()) {
				Map<String,Object> map=new HashMap<String, Object>();
				map.put("messageType", "error");
				String message=messageSource.getMessage("ErrorForm", null,null);
				/*for(String err:Utils.errorBinderToList(result, messageSource)){
					if(!err.trim().equals("harus diisi"))
					message+="<br/>"+err;
				}*/
				map.put("message",message );
				model.addAllAttributes(map);
				return "changepass";
			}

			//bila tidak ada error simpan data disini, lalu kembalikan ke layar list input, letakkan pesan di flash attribute nya spring
			//flash attribute berguna untuk mengirimkan pesan (contohnya pesan sukses/error setelah save) 
			//ke layar berikutnya (hanya sampai di layar berikutnya, setelah itu hilang)
			user.mode="CHANGE";
			user.password=user.newPassword;
			currentUser.passwordDecrypt=user.password;
			String pesan = dbService.saveUser(user, currentUser);
			ra.addFlashAttribute("pesan", pesan);
			return "redirect:/changepass";
		}

		return "changepass";
	}
	
	
}