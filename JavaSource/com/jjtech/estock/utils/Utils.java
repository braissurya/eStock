package com.jjtech.estock.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateMidnight;
import org.joda.time.Months;
import org.joda.time.Years;
import org.springframework.context.MessageSource;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.ServletRequestUtils;

import com.jjtech.estock.model.DropDown;
import com.jjtech.estock.model.GroupUser;
import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.User;
import com.jjtech.estock.service.DbService;
/**
 * Utility classes, rata2 function/vars disini static saja
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:12:00 AM)
 *
 */
public class Utils{
	
	// Formatter2 default ada disini, tidak perlu di-register satu2 di spring xml
	public static final DateFormat defaultDF = new SimpleDateFormat("dd-MM-yyyy");
	public static final DateFormat defaultDFLong = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	public static final DateFormat yearDF = new SimpleDateFormat("yyyy");
	public static final NumberFormat defaultNF =new DecimalFormat("#,##0.00;(#,##0.00)");// NumberFormat.getInstance();
	public static final NumberFormat defaultCF =new DecimalFormat("#,##0;(#,##0)");// NumberFormat.getInstance();
	
	
	/**
	 * Fungsi untuk Tambah Tanggal (contoh: FormatDate.add(tanggal, Calendar.DATE, 30) atau add(new Date(), Calendar.MONTH, 1)),
	 * bisa juga menggunakan negatif (untuk mengurangi)
	 * 
 	 * @author Yusuf
	 * @since Nov 24, 2011
	 * 
	 * @param tanggal
	 *            Tanggal yang ingin ditambahkan
	 * @param kalendar
	 *            Konstanta penambah, sesuai dengan konstanta yang ada di class Calendar
	 * @param angka
	 *            Jumlah angka yang ingin ditambahkan ke tanggal bersangkutan
	 * @return Date hasil setelah ditambahkan (atau dikurangi)
	 * @see Date, Calendar
	 */
	public static Date add(Date tanggal, int kalendar, int angka) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(tanggal);
		cal.add(kalendar, angka);
		Date result = cal.getTime(); 
		return result;
	}
	
	
	/**
	 * Tarik data tahun aplikasi. Contoh hasilnya "2006-2011" 
	 * 
	 * @param now
	 * @return
	 */
	public static String getCopyrightYears(Date now){
		int tahunAwal = 2013;
		int tahunSekarang = Integer.parseInt(yearDF.format(now));
		
		String tahun;
		if(tahunSekarang > tahunAwal) tahun = tahunAwal + "-" + tahunSekarang;
		else tahun = String.valueOf(tahunAwal);
		return tahun;
	}
	
	

	public static String formatNumber(String format,Object amount) {
		if (amount == null)return "";
		else return new DecimalFormat(format+";("+format+")").format(amount);
	}
	
	/**
	 * Fungsi untuk me-listing semua report yang ada di file properties, dimana key nya harus dimulai dengan report atau subreport 
	 * 
	 * @author Yusuf
	 * @since Jul 8, 2008 (10:56:24 AM)
	 * @param props
	 * @return
	 */
	public static List<String> listAllReports(Properties props) {
		List<String> reportList = new ArrayList<String>();
		for(Iterator it = props.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			if(key.startsWith("report") || key.startsWith("subreport")) {
				reportList.add(key);
			}
		}
		Collections.sort(reportList);
		return reportList;
	}
	
	
	
	/**
	 * 
	 * @Method_name	: errorExtract
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Jan 28, 2013 10:25:56 PM
	 * @return_type : String
	 * @Description : mengekstrak error message ke dalam String
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static String errorExtract(Exception e){
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String	exception = sw.toString();
		
		try {
			sw.close();
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return exception;
	}
	
	/**
	 * Fungsi ini untuk mengecek apakah suatu field ada isinya
	 * @author Yusuf Sutarko
	 * @since May 2, 2007 (7:40:39 AM)
	 * @param object
	 * @return
	 */
	public static boolean isEmpty(Object object) {
		if(object==null) return true;
		else	if(object instanceof String) {
			String tmp = (String) object;
			if(tmp.trim().equals("")) return true;
			else return false;
		}else if(object instanceof List) {
			List<?> tmp = (List<?>) object;
			return tmp.isEmpty();
		}else if(object instanceof Map){
			return ((Map<?, ?>) object).isEmpty();
//		}else if(object instanceof Integer || object instanceof Long|| object instanceof Double|| object instanceof Float|| 
//				object instanceof BigDecimal || object instanceof Date){
//			return false;
		}
		return true;
	}
	
	public static String convertDateToString(Date tanggal,String format){
		if(tanggal==null)return null;
		else{
			try{
				return new SimpleDateFormat(format).format(tanggal);
			}catch (Exception e) {
				
				return null;
			}
		}
	}
	
	
	public static String convertDateToString2(Date tanggal,String format)  throws Exception{
		if(tanggal==null)return null;
		else{
			return new SimpleDateFormat(format).format(tanggal);
			
		}
	}
	
	public static Date convertStringToDate(String tanggal,String format){
		if(tanggal==null)return null;
		else{
			try{
				return new SimpleDateFormat(format).parse(tanggal);
			}catch (Exception e) {
				
				return null;
			}
		}
	}
	
	public static Date convertStringToDate2(String tanggal,String format) throws ParseException{
		if(tanggal==null)return null;
		else{
			return new SimpleDateFormat(format).parse(tanggal);
		
		}
	}
	
	public static List<String> errorBinderToList(BindingResult bindingResult,MessageSource messageSource){
		List<String> errorMessage=new ArrayList<String>();
		if (bindingResult.hasErrors()) {
			for (Object object : bindingResult.getAllErrors()) {
				    if(object instanceof FieldError) {
				        FieldError fieldError = (FieldError) object;
				        /**
				          * Use null as second parameter if you do not use i18n (internationalization)
				          */
				        errorMessage.add( messageSource.getMessage(fieldError, null));
				    }
				}
		 }
		return errorMessage;
	}
	
	public static List<DropDown> lstjenisProduct(){
		List<DropDown> lstjenisProduct=new ArrayList<DropDown>();
		lstjenisProduct.add(new DropDown("1","ASURANSI JIWA KREDIT"));
		lstjenisProduct.add(new DropDown("2","ASURANSI FIRE"));
		return lstjenisProduct;
	}
	
	public static List<DropDown> lstjenisBangunan(){
		List<DropDown> lstjenisBangunan=new ArrayList<DropDown>();
		lstjenisBangunan.add(new DropDown("1","Rumah Tinggal / Apartemen"));
		lstjenisBangunan.add(new DropDown("2","Ruko / Rukan / Kios"));
		lstjenisBangunan.add(new DropDown("3","Lainnya"));
		return lstjenisBangunan;
	}
	
	public static List<DropDown> lstRelasi(){
		List<DropDown> lstRelasi=new ArrayList<DropDown>();
		lstRelasi.add(new DropDown("1","Diri Sendiri"));
		lstRelasi.add(new DropDown("2","Istri / Suami"));
		lstRelasi.add(new DropDown("3","Orang Tua"));
		lstRelasi.add(new DropDown("4","Anak Kandung"));
		return lstRelasi;	
	}
	
	public static List<DropDown> lstMaster(){
		List<DropDown> lstMaster=new ArrayList<DropDown>();
		lstMaster.add(new DropDown("1","Posisi Dokumen Polis"));
		lstMaster.add(new DropDown("2","Jenis Relasi"));
		lstMaster.add(new DropDown("3","Jenis Grup Polis"));
		lstMaster.add(new DropDown("4","Jenis Produk"));
		lstMaster.add(new DropDown("5","Jenis Bangunan"));
		lstMaster.add(new DropDown("6","Jenis Address"));
		lstMaster.add(new DropDown("7","Jenis Manfaat Life"));
		lstMaster.add(new DropDown("8","Jenis Bank"));
		return lstMaster;	
	}
	
	/**
	 * 
	 * @Method_name	: tranferPosisi
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Feb 10, 2013 9:35:17 PM
	 * @return_type : Integer
	 * @Description : utils untuk transfer posisi sesuai flow
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static Integer tranferPosisi(Integer posisiBefore){
		Integer posisi=posisiBefore;
		if(posisiBefore==1){//transfer dari input ke validasi
			posisi=2;
		}else if(posisiBefore==2){//tranfer dari validasi ke akseptasi
			posisi=3;
		}else if(posisiBefore==3){//transfer dari akseptasi ke upload dokumen
			posisi=4;
		}
		
		return posisi;
	}
	
	
	/**
	 * 
	 * @Method_name	: hitUmur
	 * @author 		: Yusuf Sutarko
	 * @since		: 11 Feb 2013
	 * @return_type : int
	 * @Description : utils untuk hitung umur sesuai rumus asuransi (diatas 6 bulan dibulatkan keatas)
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static int hitUmur(Date birthdate, Date begdate){
		DateMidnight awal = new DateMidnight(birthdate);
		DateMidnight akhir = new DateMidnight(begdate);
		
		Years thn = Years.yearsBetween(awal, akhir);
		Months bln = Months.monthsBetween(awal, akhir);
		
		//bila diatas 6 bulan, dibulatkan keatas
		int penambah = 0;
		if(bln.getMonths() % 12 >= 6) penambah = 1;
		
		return thn.getYears() + penambah;
	}
	
	public static List<DropDown> lstJenisBank(){
		List<DropDown> lstJenisBank=new ArrayList<DropDown>();
		lstJenisBank.add(new DropDown("1","BANK"));
		lstJenisBank.add(new DropDown("2","ASURANSI JIWA"));
		lstJenisBank.add(new DropDown("3","BROKER"));
		lstJenisBank.add(new DropDown("4","ASURANSI KERUGIAN"));
		return lstJenisBank;	
	}
	
	public static List<DropDown> lstJenisCabBank(){
		List<DropDown> lstJenisCabBank=new ArrayList<DropDown>();
		lstJenisCabBank.add(new DropDown("1","KC"));
		lstJenisCabBank.add(new DropDown("2","KCP"));
		return lstJenisCabBank;	
	}
	
	public static List<DropDown> lstFormClaim(){
		List<DropDown> lstFormClaim=new ArrayList<DropDown>();
		lstFormClaim.add(new DropDown("1","CERT","Sertifikat asuransi asli / fotocopy daftar peserta"));
		lstFormClaim.add(new DropDown("2","SPK","Surat pengajuan klaim kematian dari pihak Pemegang Polis / Bank dengan dilampiri print out sisa pinjaman (sampai dengan Tertanggung/Peserta meninggal dunia)"));
		lstFormClaim.add(new DropDown("3","SKM","Surat keterangan meninggal dunia dari instansi yang berwenang"));
		lstFormClaim.add(new DropDown("4","SKD","Formulir Surat Keterangan Dokter harus diisi oleh dokter / Rumah sakit /puskesmas yang memeriksa / merawat tertanggung (formulir dari AJS MSIG)"));
		lstFormClaim.add(new DropDown("5","BAP","Surat berita acara dari kepolisian dalam hal meninggal dunianya tidak wajar atau kecelakaan lalu lintas"));
		lstFormClaim.add(new DropDown("6","ID","Bukti diri dari Tertanggung"));
		return lstFormClaim;	
	}
	
	/**
	 * 
	 * @Method_name	: isFileExist
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Feb 12, 2013 10:43:11 PM
	 * @return_type : boolean
	 * @Description : cek apakah file exist
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public static boolean isFileExist(String filename){
		boolean scFile=false;
		FileInputStream in=null;
		try{
			  File l_file = new File(filename);
		     in = new FileInputStream(l_file);				      
		      in.close();
		   
		      scFile=true;
		}catch (Exception e) {
			//e.printStackTrace();
		}finally{
			if(in!=null)
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
				}
		}
		return scFile;
	}
	
	/**
	 * Method untuk menghapus suatu file dari server
	 * 
	 * @author Yusuf
	 * @since Jul 3, 2008 (1:49:03 PM)
	 * @param destDir lokasi file
	 * @param fileName nama file
	 * @param response
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static boolean deleteFile(String destDir, String fileName, HttpServletResponse response) throws FileNotFoundException,
			IOException {
		File file = new File(destDir + "/" + fileName);
		if (file.exists()) return file.delete();
		return false;
	}
	
	/**
	 * Method untuk men-download sebuah file 
	 * 
	 * @author Yusuf
	 * @since Jul 3, 2008 (1:47:27 PM)
	 * @param location lokasi file yg ingin didownload
	 * @param fileName nama file yang ingin didownload
	 * @param response object response
	 * @param inlineOrAttached "inline" apabila ingin langsung ditampilkan di browser atau "attachment" bila ingin keluar dialog "Save As"
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void downloadFile(String location, String fileName, HttpServletResponse response, String inlineOrAttached) 
			throws FileNotFoundException, IOException{
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(location);
			if (in != null) {
				out = new BufferedOutputStream(response.getOutputStream());
				in = new BufferedInputStream(in);
				//String contentType = "application/unknown";
				response.setHeader("Content-Disposition", inlineOrAttached + "; filename=\"" + fileName + "\"");
				int c;
				while ((c = in.read()) != -1) out.write(c);
			}
		} finally {
			if (in != null) try {in.close(); } catch (Exception e) {}
			if (out != null) try {out.close(); } catch (Exception e) {}
		}				
	}
	
	/**
	 * Method untuk melist file2 yang ada dalam suatu directory
	 * 
	 * @author Yusuf
	 * @since Jul 3, 2008 (1:51:15 PM)
	 * @param dir lokasi file2 yang ingin di listing
	 * @return
	 */
	public static List<DropDown> listFilesInDirectory(String dir) {
		File destDir = new File(dir);
		List<DropDown> daftar = new ArrayList<DropDown>();
		if(destDir.exists()) {
			String[] children = destDir.list();
			daftar = new ArrayList<DropDown>();
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			for(int i=0; i<children.length; i++) {
				File file = new File(destDir+"/"+children[i]);
				daftar.add(new DropDown(children[i], df.format(new Date(file.lastModified())), dir));
			}
		}
		return daftar;
	}
	
	/**
	 * Fungsi yang mengikuti fungsi RPAD di Oracle, contoh: rpad("0", "YUSUF", 10) menghasilkan "00000YUSUF"
	 * 
	 * @author Yusuf
	 * @since Feb 21, 2011 (7:41:43 PM)
	 * @param karakter untuk melengkapi sisa string
	 * @param kata (String) yang mau dipanjangkan
	 * @param panjang dari string hasilnya
	 * @return String hasil penggabungan dari karakter dan kata
	 * @see Fungsi RPAD di Oracle
	 */
	public static String rpad(String karakter, String kata, int panjang) {
		if(kata==null) return null;
		StringBuffer result = new StringBuffer();
		if (kata.length() < panjang) {
			for (int i = 0; i < panjang - kata.length(); i++) {
				result.append(karakter);
			}
			result.append(kata);
			return result.toString();
		} else {
			return kata;
		}
	}
	
	
	
	public static String generateMenu(String path,User currentUser,DbService dbService){
		StringBuffer result = new StringBuffer();
		int tingkat = 0;
		
		List<Integer> parentIdList=new ArrayList<Integer>();
		result.append("<ul class=\"box\" id=\"ddtopmenubar\">\n");
		result.append("<li><a href=\""+path+"\" ><span>Home</span></a></li>\n");
		List<Menu> daftarMenu=dbService.selectMenuAccess(currentUser.group_user_id, null,null);
		//menu tingkat 1
		for(Menu baris : daftarMenu){
			if(baris.level==1){
				if(baris.urut.intValue()==1){
					
				}

					
				if(Utils.isEmpty(baris.link)){
					result.append("<li><a href=\"#\" rel=\"ddsubmenu"+baris.urut+"\"><span>"+baris.nama+"</span></a></li>\n");
					parentIdList.add(baris.urut);
				}else {	
					if(baris.link.startsWith("http://")){ // klo linknya dari luar
						result.append("<li><a href=\""+baris.link+" \"><span>"+baris.nama+"</span></a></li>\n");
					}else{
						result.append("<li><a href=\""+path+ "/" +baris.link+" \"><span>"+baris.nama+"</span></a></li>\n");
					}
				}
			}
		}
		result.append("</ul>\n");		
		result.append("<script type=\"text/javascript\">ddlevelsmenu.setup(\"ddtopmenubar\", \"topbar\") </script>\n");
		
		for(Integer parentId:parentIdList){
			
			List<Menu>daftarMenuNoRootPath=dbService.selectMenuAccess(currentUser.group_user_id, null,"0.1."+parentId);
			if(!daftarMenuNoRootPath.isEmpty()){
				result.append("<ul id=\"ddsubmenu"+parentId+"\" class=\"ddsubmenustyle\">\n");			
				tingkat=2;		
				int count=0;
				for(Menu baris : daftarMenuNoRootPath){	
				  if(baris.level!=1){
					  if(baris.level==tingkat) {
//							result.append("<li id=\""+id+"\" style=\"margin-left: 15px;\" class=\"jstree-no-icons\">" +isi+ "\n");
							if(baris.link == null) {
								result.append("<li><a href=\"#\" >"+baris.nama+"</a>\n");
							} else if(baris.link != null) {
								if(baris.link.startsWith("http://")){
									result.append("<li><a href=\""+baris.link+" \">"+baris.nama+"</a>\n");
								}else{
									result.append("<li><a href=\""+path+ "/" +baris.link+" \">"+baris.nama+"</a>\n");
								}
							}
							
						} else if(baris.level>tingkat) {	
								if(baris.level-tingkat>1){
									for(int i=1; i <= (baris.level - tingkat); i++){
	//									result.append("<ul>\n<li class=\"jstree-no-icons\"><a href=\"#\">....</a>\n");	
										if(baris.link == null) {
											result.append("<ul>\n<li><a href=\"#\" >....</a>\n");
										} else if(baris.link != null) {
											if(baris.link.startsWith("http://")){
												result.append("<ul>\n<li><a href=\"#\">....</a>\n");
											}else{
												result.append("<ul>\n<li><a href=\"#\">....</a>\n");
											}
										}
									}
								}
//							result.append("<ul>\n<li id=\""+id+"\" style=\"margin-left: 15px;\" class=\"jstree-no-icons\">" +isi+"\n");
							if(baris.link == null) {
								result.append("<ul>\n<li><a href=\"#\" >"+baris.nama+"</a>\n");
							} else if(baris.link != null) {
								if(baris.link.startsWith("http://")){
									result.append("<ul>\n<li><a href=\""+baris.link+" \">"+baris.nama+"</a>\n");
								}else{
									result.append("<ul>\n<li><a href=\""+path+ "/" +baris.link+" \">"+baris.nama+"</a>\n");
								}
							}
							
						} else  if(baris.level < tingkat){
							for(int i=1; i <= (tingkat - baris.level); i++){
								result.append("</ul></li>\n");	
							}
						
//							result.append("<li id=\""+id+"\" style=\"margin-left: 15px;\" class=\"jstree-no-icons\">"+isi+"\n");
							if(baris.link == null) {
								result.append("\n<li><a href=\"#\" >"+baris.nama+"</a>\n");
							} else if(baris.link != null) {
								if(baris.link.startsWith("http://")){
									result.append("\n<li><a href=\""+baris.link+" \">"+baris.nama+"</a>\n");
								}else{
									result.append("\n<li><a href=\""+path+ "/" +baris.link+" \">"+baris.nama+"</a>\n");
								}
							}
							
						}
						tingkat = baris.level;
					
					count++;	
				  }
				}	
				for(int i=1; i <= (tingkat-2); i++){
					result.append("</li></ul>\n");	
				}
				result.append("</ul>\n");
			}
			
		}
		return result.toString();
	}
	
	public static String generateSiteMap(String path,User currentUser,List<Menu> lsMenu){
		StringBuffer result = new StringBuffer();
		int tingkat = 0;
		int count=0;
		result.append("<ul >\n");		
		for(Menu baris : lsMenu){	
				  if(baris.level==tingkat) {
//						result.append("<li id=\""+id+"\" style=\"margin-left: 15px;\" class=\"jstree-no-icons\">" +isi+ "\n");
						if(baris.link == null) {
							result.append("<li><a href=\"#\" >"+baris.nama+"</a>\n");
						} else if(baris.link != null) {
							if(baris.link.startsWith("http://")){
								result.append("<li><a href=\""+baris.link+" \">"+baris.nama+"</a>>\n");
							}else{
								result.append("<li><a href=\""+path+ "/" +baris.link+" \">"+baris.nama+"</a>\n");
							}
						}
						
					} else if(baris.level>tingkat) {	
							if(baris.level-tingkat>1){
								for(int i=1; i <= (baris.level - tingkat); i++){
//									result.append("<ul>\n<li class=\"jstree-no-icons\"><a href=\"#\">....</a>\n");	
									if(baris.link == null) {
										result.append("<ul>\n<li><a href=\"#\" >....</a>\n");
									} else if(baris.link != null) {
										if(baris.link.startsWith("http://")){
											result.append("<ul>\n<li><a href=\"#\">....</a>\n");
										}else{
											result.append("<ul>\n<li><a href=\"#\">....</a>\n");
										}
									}
								}
							}
//						result.append("<ul>\n<li id=\""+id+"\" style=\"margin-left: 15px;\" class=\"jstree-no-icons\">" +isi+"\n");
						if(baris.link == null) {
							result.append("<ul>\n<li><a href=\"#\" >"+baris.nama+"</a>\n");
						} else if(baris.link != null) {
							if(baris.link.startsWith("http://")){
								result.append("<ul>\n<li><a href=\""+baris.link+" \">"+baris.nama+"</a>\n");
							}else{
								result.append("<ul>\n<li><a href=\""+path+ "/" +baris.link+" \">"+baris.nama+"</a>\n");
							}
						}
						
					} else  if(baris.level < tingkat){
						for(int i=1; i <= (tingkat - baris.level); i++){
							result.append("</ul></li>\n");	
						}
					
//						result.append("<li id=\""+id+"\" style=\"margin-left: 15px;\" class=\"jstree-no-icons\">"+isi+"\n");
						if(baris.link == null) {
							result.append("\n<li><a href=\"#\" >"+baris.nama+"</a>\n");
						} else if(baris.link != null) {
							if(baris.link.startsWith("http://")){
								result.append("\n<li><a href=\""+baris.link+" \">"+baris.nama+"</a>\n");
							}else{
								result.append("\n<li><a href=\""+path+ "/" +baris.link+" \">"+baris.nama+"</a>\n");
							}
						}
						
					}
					tingkat = baris.level;
				
				count++;	
			}	
			
		result.append("</ul>\n");
			
	
		return result.toString();
	}
	
	public static Model generatePaging(DbService dbService, HttpServletRequest request,Model model, String jenisPaging, Integer uname){
		Integer rowcount = null, totalData = null, totalPage = null, page = null, flag_type = null;
		String search=null, sort="",sort_type=null, no_trans=null;
		List listPaging = null;
		if(uname==-1)uname=null;
		
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
		if(jenisPaging.equals("menu") || jenisPaging.equals("coa")){
			rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",50);
		}else if(jenisPaging.equals("item") || jenisPaging.equals("config")){
			rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",20);
		}else if(jenisPaging.equals("customer") || jenisPaging.equals("supplier")){
			rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",10);
		}else{
			rowcount = ServletRequestUtils.getIntParameter(request, "rowcount",5);
		}
		
		if(jenisPaging.equals("menu")){
			totalData=dbService.selectListMenuPagingCount(search);
		}else if(jenisPaging.equals("groupuser")){
			totalData=dbService.selectListGroupUserPagingCount(search,1);
		}else if(jenisPaging.equals("user")){
			totalData=dbService.selectListUserPagingCount(search,uname);
		}else if(jenisPaging.equals("category")){
			totalData=dbService.selectListCategoryPagingCount(search);
		}else if(jenisPaging.equals("merk")){
			totalData=dbService.selectListMerkPagingCount(search);
		}else if(jenisPaging.equals("satuan")){
			totalData=dbService.selectListSatuanPagingCount(search);
		}else if(jenisPaging.equals("warna")){
			totalData=dbService.selectListWarnaPagingCount(search);
		}else if(jenisPaging.equals("supplier")){
			totalData=dbService.selectListSupplierPagingCount(search);
		}else if(jenisPaging.equals("customer")){
			totalData=dbService.selectListCustomerPagingCount(search);
		}else if(jenisPaging.equals("karyawan")){
			if(uname==0)uname=null;
			totalData=dbService.selectListKaryawanPagingCount(search,uname);
		}else if(jenisPaging.equals("item")){
			totalData=dbService.selectItemPagingCount(search);
		}else if(jenisPaging.equals("opname")){
			totalData=dbService.selectOpnamePagingCount(search, currentUser.cabang_id);
		}else if(jenisPaging.equals("cabang")){
			totalData=dbService.selectListCabangPagingCount(search);
		}else if(jenisPaging.equals("config")){
			totalData=dbService.selectListConfigPagingCount(search);
		}else if(jenisPaging.equals("bank")){
			totalData=dbService.selectListBankPagingCount(search);
		}else if(jenisPaging.equals("account")){
			totalData=dbService.selectListAccountPagingCount(search);
		}else if(jenisPaging.equals("transferstock")){
			totalData=dbService.selectListTransPagingCount(search, 7, uname, null, null,aksescabang_id);
		}else if(jenisPaging.equals("payment")){
			if(uname==0){
				totalData=dbService.selectListPaymentPagingCount(search, 4, 2, null, null);
			}else if(uname==1){
				totalData=dbService.selectListPaymentPagingCount(search, 3, 2, null, null);
			}else if (uname==2){
				List<Trans> lsTrans =dbService.selectListTransAuto(no_trans, null, 4, 2);
				totalData=lsTrans.size();
			}else if (uname==3){
				List<Trans> lsTrans =dbService.selectListTransAuto(no_trans, null, 3, 2);
				totalData=lsTrans.size();
			}
		}else if(jenisPaging.equals("paymentOther")){
			totalData=dbService.selectListTrxPagingCount(search, uname, null);
		}else if(jenisPaging.equals("coa")){
			totalData=dbService.selectListCOAPagingCount(search);
		}
		
		totalPage = new Double(Math.ceil(new Double(totalData)/ new Double(rowcount))).intValue(); //jml total halaman = (jumlah data / rowcount) dibulatkan keatas
		page = ServletRequestUtils.getIntParameter(request, "page", 1); //halaman ke X
		
		if(page<1) page = 1;
		if(page>totalPage) page = totalPage;
		int offset = (page - 1) * rowcount; //start penarikan data dari row ke X (mySQL)
		
		if(offset<0)offset=0;
		
		if(jenisPaging.equals("menu")){
			if(sort.equals(""))sort=null;
			listPaging=dbService.selectListMenuPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("groupuser")){
			if(sort.equals(""))sort="id";
			listPaging=dbService.selectListGroupUserPaging(search, offset, rowcount, sort, sort_type,1);
		}else if(jenisPaging.equals("user")){
			if(sort.equals(""))sort="id";
			listPaging=dbService.selectListUserPaging(search, offset, rowcount, sort, sort_type, uname);
		}else if(jenisPaging.equals("category")){
			if(sort.equals(""))sort="nama";
			listPaging=dbService.selectListCategoryPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("merk")){
			if(sort.equals(""))sort="nama";
			listPaging=dbService.selectListMerkPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("satuan")){
			if(sort.equals(""))sort="nama";
			listPaging=dbService.selectListSatuanPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("warna")){
			if(sort.equals(""))sort="nama";
			listPaging=dbService.selectListWarnaPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("supplier")){
			if(sort.equals(""))sort="kode";
			listPaging=dbService.selectListSupplierPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("customer")){
			if(sort.equals(""))sort="kode";
			listPaging=dbService.selectListCustomerPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("karyawan")){
			if(sort.equals(""))sort="nik";
//			if(uname==0)uname=null;
			listPaging=dbService.selectListKaryawanPaging(search, offset, rowcount, sort, sort_type,uname);
		}else if(jenisPaging.equals("item")){
			if(sort.equals(""))sort="barcode_ext";
			listPaging=dbService.selectListItemPaging(search, offset, rowcount, sort, sort_type, currentUser.cabang_id);
		}else if(jenisPaging.equals("opname")){
			if(sort.equals(""))sort="no_trans";
			listPaging=dbService.selectListOpnamePaging(search, offset, rowcount, sort, sort_type, currentUser.cabang_id);
		}else if(jenisPaging.equals("cabang")){
			if(sort.equals(""))sort="id";
			listPaging=dbService.selectListCabangPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("config")){
			if(sort.equals(""))sort="id";
			listPaging=dbService.selectListConfigPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("bank")){
			if(sort.equals(""))sort="nama";
			listPaging=dbService.selectListBankPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("account")){
			if(sort.equals(""))sort="namabank";
			listPaging=dbService.selectListAccountPaging(search, offset, rowcount, sort, sort_type);
		}else if(jenisPaging.equals("transferstock")){
			if(sort.equals(""))sort="no_trans";
			listPaging=dbService.selectListTransPaging(search, offset, rowcount, sort, sort_type, 7, uname, null, null,aksescabang_id);
		}else if(jenisPaging.equals("payment")){
			if(sort.equals(""))sort="no_payment";
			if(uname==0){
				listPaging=dbService.selectListPaymentPaging(search, offset, rowcount, sort, sort_type, 4, 2, null, null);
			}else if(uname==1){
				listPaging=dbService.selectListPaymentPaging(search, offset, rowcount, sort, sort_type, 3, 2, null, null);
			}else if (uname==2){
				listPaging =dbService.selectListTransAuto(no_trans, null, 4, 2);
			}else if (uname==3){
				listPaging =dbService.selectListTransAuto(no_trans, null, 3, 2);
			}
		}else if(jenisPaging.equals("paymentOther")){
			if(sort.equals(""))sort="no_trx";
			listPaging=dbService.selectListTrxPaging(search, offset, rowcount, sort, sort_type, uname, null);
		}else if(jenisPaging.equals("coa")){
			if(sort.equals(""))sort="id";
			listPaging=dbService.selectListCOAPaging(search, offset, rowcount, sort, sort_type);
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
		if(jenisPaging.equals("user")){
			if(uname==null)uname=-1;
			model.addAttribute("groupmenuid", uname);		
			model.addAttribute("groupmenuName",uname==-1?"": dbService.selectListGroupUser(uname, null, 1, 1).get(0).getNama());
		}
		
		return model;
	}
	
	public static double nvl(Double value){
		if(value != null) return value;
		else return 0.;
	}
	
	public static int nvl(Integer value){
		if(value != null) return value;
		else return 0;
	}
	
	public static String nvl(String value){
		if(value != null) return value;
		else return "";
	}
	
}