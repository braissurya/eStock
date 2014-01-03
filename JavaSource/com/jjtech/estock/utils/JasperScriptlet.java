package com.jjtech.estock.utils;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import net.sf.jasperreports.engine.JRDefaultScriptlet;
import net.sf.jasperreports.engine.JRScriptletException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.io.Resources;

import com.barcode_coder.java_barcode.Barcode;
import com.barcode_coder.java_barcode.BarcodeFactory;
import com.barcode_coder.java_barcode.BarcodeType;

/**
 * Class ini berfungsi untuk disertakan dalam jasperreports sebagai scriptlet
 * utama, dimana didalamnya sudah dimasukkan fungsi2 kecil yang berguna untuk
 * reporting apabila ingin menambahkan fungsi yang spesifik untuk sebuah report
 * tertentu, lebih baik class ini di-extend saja sebagai parent.
 * 
 * @author Yusuf
 * @since 20051208
 */
public class JasperScriptlet extends JRDefaultScriptlet {

    protected final Log logger = LogFactory.getLog(getClass());

    public String rpad(String karakter, String kata, int panjang) {
	return Utils.rpad(karakter, kata, panjang);
    }
    
    public String convertEmpty(String o){
	if(Utils.isEmpty(o)){
	    return "";
	}else{
	    return o;
	}
    }

    public String formatCurrency(String currency, BigDecimal amount) {
	if (amount == null) {
	    return "-";
	} else {
	    return (currency != null ? currency : "") + new DecimalFormat("#,##0;(#,##0)").format(amount);
	}
    }

    public String formatNumber(BigDecimal amount) {
	if (amount == null)
	    return "";
	else if (amount.toString().indexOf(".") == -1)
	    return new DecimalFormat("#,##0;(#,##0)").format(amount);
	else
	    return new DecimalFormat("#,##0;(#,##0)").format(amount);
    }

    public String convertDateToString(Date tanggal, String format) {
	String result=Utils.convertDateToString(tanggal, format);
	if(result==null)result="";
	return result;
    }

    public Date convertStringToDate(String tanggal, String format) {

	return Utils.convertStringToDate(tanggal, format);
    }

    public String getProperty(String name) {

	Properties props = null;
	try {
	    props = (Properties) this.getParameterValue("props");
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	}
	try {
	    if (props == null) {
		props = new Properties();
		FileInputStream in = new FileInputStream(Resources.getResourceAsFile("app.properties"));
		props.load(in);
	    }
	    return props.getProperty(name);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	}
	return null;

    }

    public String imagePath(String fileName) {
	File file = new File(fileName);
	if (file.exists()) {
	    return fileName;
	} else {

	    return "com/eStock/utils/image/noimage.gif";
	}
    }

    public String formatDateStripes(Object tanggal) {
	if (tanggal == null)
	    return "";
	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	return df.format(tanggal);
    }

    public String formatDateIndonesian(Object tgl) throws JRScriptletException {
	if (tgl == null)
	    return "";
	DateFormat df = new SimpleDateFormat("ddMMyyyy");
	String temp = df.format(tgl);
	return Integer.valueOf(temp.substring(0, 2))
		+ (temp.substring(2, 4).equals("01") ? " Januari " : temp.substring(2, 4).equals("02") ? " Februari " : temp.substring(2, 4).equals(
			"03") ? " Maret " : temp.substring(2, 4).equals("04") ? " April " : temp.substring(2, 4).equals("05") ? " Mei " : temp
			.substring(2, 4).equals("06") ? " Juni " : temp.substring(2, 4).equals("07") ? " Juli "
			: temp.substring(2, 4).equals("08") ? " Agustus " : temp.substring(2, 4).equals("09") ? " September " : temp.substring(2, 4)
				.equals("10") ? " Oktober " : temp.substring(2, 4).equals("11") ? " November "
				: temp.substring(2, 4).equals("12") ? " Desember " : "") + temp.substring(4);
    }

    public String formatTerbilang(BigDecimal amount, String lku) {
	return AngkaTerbilang.indonesian(amount.toString(), lku);
    }

    public String generateBarcode(String value) {
	String path = getProperty("barcode.dir") + "\\" + value + "." + getProperty("barcode.format");
	BarcodeType bb = BarcodeType.Code39;

	if ("EAN8".equals(getProperty("barcode.type")))
	    bb = BarcodeType.EAN8;
	else if ("EAN13".equals(getProperty("barcode.type")))
	    bb = BarcodeType.EAN13;
	else if ("Standard2of5".equals(getProperty("barcode.type")))
	    bb = BarcodeType.Standard2of5;
	else if ("Interleaved2of5".equals(getProperty("barcode.type")))
	    bb = BarcodeType.Interleaved2of5;
	else if ("Code11".equals(getProperty("barcode.type")))
	    bb = BarcodeType.Code11;
	else if ("Code39".equals(getProperty("barcode.type")))
	    bb = BarcodeType.Code39;
	else if ("Code93".equals(getProperty("barcode.type")))
	    bb = BarcodeType.Code93;
	else if ("Code128".equals(getProperty("barcode.type")))
	    bb = BarcodeType.Code128;
	else if ("Codabar".equals(getProperty("barcode.type")))
	    bb = BarcodeType.Codabar;
	else if ("MSI".equals(getProperty("barcode.type")))
	    bb = BarcodeType.MSI;
	else if ("Datamatrix".equals(getProperty("barcode.type")))
	    bb = BarcodeType.Datamatrix;
	//
	//
	Barcode b = BarcodeFactory.createBarcode(bb, value);
	b.export(getProperty("barcode.format"), Integer.parseInt(getProperty("barcode.width")), Integer.parseInt(getProperty("barcode.height")),
		new Boolean(getProperty("barcode.title")), path);

	return path;
    }

    public static void main(String[] args) {
	JasperScriptlet js = new JasperScriptlet();
	System.out.println(js.generateBarcode("0041113TB00009"));
    }

}