package com.jjtech.estock.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.record.aggregates.PageSettingsBlock;


import com.ibatis.common.resources.Resources;
import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.util.JRLoader;



/**
 * Fungsi2 tambahan untuk jasperreports, seperti setting untuk PDF, misalnya security, password, printing, etc...
 * @author Yusuf Sutarko
 * @since May 25, 2007 (4:21:36 PM)
 */
public class JasperUtils {

		/**
		 * Exporter Parameters untuk tipe file PDF
		 * 
		 * @author Yusuf Sutarko
		 * @since May 25, 2007 (4:21:43 PM)
		 * @param permissions
		 * @param ownerPassword
		 * @param userPassword
		 * @return
		 */
	public static Map<JRExporterParameter, Object> getPdfExporterParameter(
			Integer permissions, String ownerPassword, String userPassword) {
		Map<JRExporterParameter, Object> expParams = new HashMap<JRExporterParameter, Object>();
		if (permissions != null) expParams.put(JRPdfExporterParameter.PERMISSIONS, permissions);
		expParams.put(JRPdfExporterParameter.IS_ENCRYPTED, Boolean.TRUE);
		expParams.put(JRPdfExporterParameter.IS_128_BIT_KEY, Boolean.TRUE);
		if (ownerPassword != null) expParams.put(JRPdfExporterParameter.OWNER_PASSWORD, ownerPassword);
		if (userPassword != null) expParams.put(JRPdfExporterParameter.USER_PASSWORD, userPassword);
		//exp.put(JRPdfExporterParameter.PERMISSIONS, new Integer(PdfWriter.AllowCopy));
		//exp.put(JRPdfExporterParameter.USER_PASSWORD, elionsManager.validationVerify(1).get("PASSWORD").toString().toLowerCase());
		//exp.put(JRPdfExporterParameter.OWNER_PASSWORD, props.getProperty("pdf.masterPassword"));
		//exp.put("net.sf.jasperreports.engine.export.JRPdfExporterParameter.PERMISSIONS", new Integer(PdfWriter.AllowPrinting));			
		//AllowPrinting, AllowModifyContents, AllowCopy, AllowModifyAnnotations, AllowFillIn, AllowScreenReaders, AllowAssembly and AllowDegradedPrinting 

		return expParams;
	}

	/**
	 * Exporter Parameters untuk tipe file HTML
	 * @author Yusuf Sutarko
	 * @since May 25, 2007 (4:22:03 PM)
	 * @param contextPath
	 * @return
	 */
	public static Map<JRExporterParameter, Comparable> getHtmlExporterParameter(String contextPath) {
		Map<JRExporterParameter, Comparable> expParams = new HashMap<JRExporterParameter, Comparable>();
		expParams.put(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
		expParams.put(JRHtmlExporterParameter.IMAGES_URI, contextPath + "/image?image=");
		//request.getSession().setAttribute(ImageServlet.DEFAULT_JASPER_PRINT_SESSION_ATTRIBUTE, jasperPrint);
		//exp.put(JRHtmlExporterParameter.IMAGES_URI, request.getContextPath()+"/include/image/");
		//exp.put(JRExporterParameter.PAGE_INDEX, new Integer(0));
		//exp.put(JRHtmlExporterParameter.HTML_HEADER, "");
		//exp.put(JRHtmlExporterParameter.HTML_FOOTER, "");

		return expParams;
	}

	/**
	 * Fungsi ini mengexport report dalam bentuk PDF dan menyimpan file nya ke server
	 * 
	 * @author Yusuf Sutarko
	 * @since May 25, 2007 (4:22:14 PM)
	 * @param reportSourcePath
	 * @param outputDirectory
	 * @param outputFileName
	 * @param params
	 * @param connection
	 * @param permissions
	 * @param ownerPassword
	 * @param userPassword
	 * @throws IOException
	 * @throws JRException
	 */
	public static void exportReportToPdf(
			String reportSourcePath, String outputDirectory, String outputFileName, 
			Map params, Connection connection, 
			Integer permissions, String ownerPassword, String userPassword) throws IOException, JRException{

        String op = null;
        if(outputFileName == null) {
        	op = outputDirectory;
    		File userDir = new File(outputDirectory.substring(0, outputDirectory.lastIndexOf("\\")));
            if(!userDir.exists()) userDir.mkdirs();
        } else {
        	op = outputDirectory+"\\"+outputFileName;
    		File userDir = new File(outputDirectory);
            if(!userDir.exists()) userDir.mkdirs();
        }

		File sourceFile = Resources.getResourceAsFile(reportSourcePath);
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
		JRPdfExporter jrpdfexporter = new JRPdfExporter();
		//jrpdfexporter.setParameters(JasperUtils.getPdfExporterParameter(permissions, ownerPassword, userPassword));
		jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, op);
		jrpdfexporter.exportReport();
		
	}
	
	/**
	 * Fungsi ini mengexport report dalam bentuk PDF dan menyimpan file nya ke server
	 * perbedaan dengan diatas adalah disini inputnya List bukan connection
	 * @author Samuel Baktiar
	 * @since Apr 07, 2008 (13:00:00 PM)
	 * @param reportSourcePath
	 * @param outputDirectory
	 * @param outputFileName
	 * @param params
	 * @param dataList
	 * @param permissions
	 * @param ownerPassword
	 * @param userPassword
	 * @throws IOException
	 * @throws JRException
	 */
	public static void exportReportToPdf(
			String reportSourcePath, String outputDirectory, String outputFileName,
			Map params, List dataList,
			Integer permissions, String ownerPassword, String userPassword) throws IOException, JRException{

        String op = null;
        if(outputFileName == null) {
        	op = outputDirectory;
    		File userDir = new File(outputDirectory.substring(0, outputDirectory.lastIndexOf("\\")));
            if(!userDir.exists()) userDir.mkdirs();
        } else {
        	op = outputDirectory + outputFileName;
    		File userDir = new File(outputDirectory);
            if(!userDir.exists()) userDir.mkdirs();
        }

		File sourceFile = Resources.getResourceAsFile(reportSourcePath);
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
		params.put("reportDataKey", "dataSource");
		params.put("subReportDataKeys", new String[] {"subDS"});

		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(dataList));
        JRPdfExporter jrpdfexporter = new JRPdfExporter();
		jrpdfexporter.setParameters(	JasperUtils.getPdfExporterParameter(permissions, ownerPassword, userPassword));
		jrpdfexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		jrpdfexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, op);
		jrpdfexporter.exportReport();
	}
	
	/**
	 * Fungsi ini mengexport report dalam bentuk XLS dan menyimpan file nya ke server
	 * 
	 * @author Yusup Andri
	 * @since June 24, 2008 (16:06:33 PM)
	 * @param reportSourcePath
	 * @param outputDirectory
	 * @param outputFileName
	 * @param params
	 * @throws IOException
	 * @throws JRException
	 */	
	public static void exportReportToXls(
			String reportSourcePath, String outputDirectory, String outputFileName, 
			Map params, Connection connection) throws IOException, JRException{

        String op = null;
        if(outputFileName == null) {
        	op = outputDirectory;
    		File userDir = new File(outputDirectory.substring(0, outputDirectory.lastIndexOf("\\")));
            if(!userDir.exists()) userDir.mkdirs();
        } else {
        	op = outputDirectory + outputFileName;
    		File userDir = new File(outputDirectory);
            if(!userDir.exists()) userDir.mkdirs();
        }

		File sourceFile = Resources.getResourceAsFile(reportSourcePath);
		JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, connection);
		
        // buat file xls pake OUTPUT_FILE_NAME
		JRXlsExporter jrxlsexporter = new JRXlsExporter();
		jrxlsexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		jrxlsexporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, op);
		jrxlsexporter.exportReport();
		
		// buat file xls pake OUTPUT_STREAM
/*      OutputStream ouputStream=new FileOutputStream(new File(op));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		
		JRXlsExporter jrxlsexporter = new JRXlsExporter();
		jrxlsexporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        jrxlsexporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
        ouputStream.write(byteArrayOutputStream.toByteArray()); 
        ouputStream.flush();
        ouputStream.close();*/
        
	}	
	
	/**
	 * Fungsi ini mengexport report dalam bentuk XLS dan menyimpan file nya ke server
	 * perbedaan dengan diatas adalah disini inputnya List bukan connection
	 * @author Deddy
	 * @since Mar 11, 2009 (09:01:00 AM)
	 * @param reportSourcePath
	 * @param outputDirectory
	 * @param outputFileName
	 * @param params
	 * @param dataList
	 * @throws IOException
	 * @throws JRException
	 */
	
	public static void exportReportToXls(
			String reportSourcePath, String outputDirectory, String outputFileName, 
			Map params, List dataList, String subreportSourcePath) throws IOException, JRException{
		
		String op = null;
		String jasper=null;
		String sheet_name = "";
		sheet_name = outputFileName.substring(0, outputFileName.lastIndexOf("."));
        if(outputFileName == null) {
        	op = outputDirectory;
    		File userDir = new File(outputDirectory.substring(0, outputDirectory.lastIndexOf("\\")));
            if(!userDir.exists()) userDir.mkdirs();
        } else {
        	op = outputDirectory + outputFileName;
    		File userDir = new File(outputDirectory);
            if(!userDir.exists()) userDir.mkdirs();
        }
        
        Collection beanCollection = dataList;

//        the subreport datasource parameter in this case is the same bean collection as for the main report

        params.put("datasource", new JRBeanCollectionDataSource(beanCollection));

//        the subreport is loaded from classpath and sent as a parameter to the main report
//        String tampung = subreportSourcePath;
//        InputStream productsStream =  null;
//        productsStream = getClass().getResourceAsStream(tampung);
//        //InputStream productsStream = ClassLoader.getSystemResourceAsStream(tampung);
//        JasperReport subreport = (JasperReport)JRLoader.loadObject(productsStream);
//        params.put("ProductsSubreport", subreport);
////        exporting the main report (this code is the same, even without a subreport):
//
//        JasperPrint jasperPrint = JasperFillManager.fillReport(reportSourcePath, params, new JRBeanCollectionDataSource(beanCollection));
        
        File sourceFile = Resources.getResourceAsFile(reportSourcePath);
        JasperReport jasperReport = (JasperReport)JRLoader.loadObject(sourceFile);
        if(subreportSourcePath != null){
	        File subSourceFile = Resources.getResourceAsFile(subreportSourcePath);
	        JasperReport subReport =(JasperReport)JRLoader.loadObject(subSourceFile);
	        params.put("ProductsSubreport", subReport);
        }
		
		JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JRBeanCollectionDataSource(dataList));
		jasperPrint.setName(sheet_name);
		
//		 buat file xls pake OUTPUT_FILE_NAME
		JRXlsExporter jrxlsexporter = new JRXlsExporter();
		jrxlsexporter.setParameter(JRXlsExporterParameter.JASPER_PRINT, jasperPrint);
		//jrxlsexporter.setParameter(JRExporterParameter.OUTPUT_STREAM, op);
		jrxlsexporter.setParameter(JRXlsExporterParameter.OUTPUT_FILE_NAME, op);
		
		//Dua bagian ini digunakan apabila menggunakan multiple sheet(>1 sheet), dan tiap sheet yg dicreate dibagi per page.
//		jrxlsexporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
//		jrxlsexporter.setParameter(JRXlsExporterParameter.SHEET_NAMES, new String[]{sheet_name});
		jrxlsexporter.exportReport();
		
	}
	
	public static Boolean concatPdf(List<String> streamOfPDFFiles, OutputStream outputStream, boolean paging){

	    Document document = new Document();
	    try {
	      List<String> pdfs = streamOfPDFFiles;
	      List<PdfReader> readers = new ArrayList<PdfReader>();
	      int totalPages = 0;
	      Iterator<String> iteratorPDFs = pdfs.iterator();

	      // Create Readers for the pdfs.
	      while (iteratorPDFs.hasNext()) {
	        String pdf = iteratorPDFs.next();
	        PdfReader pdfReader = new PdfReader(pdf);
	        readers.add(pdfReader);
	        totalPages += pdfReader.getNumberOfPages();
	      }
	      // Create a writer for the outputstream
	      PdfWriter writer = PdfWriter.getInstance(document, outputStream);
	      
	      document.open();
	      BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
	      PdfContentByte cb = writer.getDirectContent(); // Holds the PDF
	      // data

	      PdfImportedPage page;
	      int currentPageNumber = 0;
	      int pageOfCurrentReaderPDF = 0;
	      Iterator<PdfReader> iteratorPDFReader = readers.iterator();

	      // Loop through the PDF files and add to the output.
	      while (iteratorPDFReader.hasNext()) {
	        PdfReader pdfReader = iteratorPDFReader.next();

	        // Create a new page in the target for each source page.
	        while (pageOfCurrentReaderPDF < pdfReader.getNumberOfPages()) {
	        	document.setPageSize(pdfReader.getPageSize(pageOfCurrentReaderPDF+1));
	          document.newPage();
	          
	         
	          pageOfCurrentReaderPDF++;
	          currentPageNumber++;
//	          document.setPageSize(pdfReader.getPageSize(pageOfCurrentReaderPDF));
	          page = writer.getImportedPage(pdfReader, pageOfCurrentReaderPDF);
	          cb.addTemplate(page, 0, 0);

	          // Code for pagination.
	          if (paging) {
	            cb.beginText();
	            cb.setFontAndSize(bf, 9);
	            cb.showTextAligned(PdfContentByte.ALIGN_CENTER, "" + currentPageNumber + " of " + totalPages, 520, 7, 0);
	            cb.endText();
	          }
	        }
	        pageOfCurrentReaderPDF = 0;
	      }
	      outputStream.flush();
	      document.close();
	      outputStream.close();
	    } catch (Exception e) {
	      e.printStackTrace();
	    } finally {
	      if (document.isOpen())
	        document.close();
	      try {
	        if (outputStream != null)
	          outputStream.close();
	      } catch (IOException ioe) {
	        ioe.printStackTrace();
	      }
	    }
	    return true;
	  }
}
