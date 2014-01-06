package com.jjtech.estock.utils;





import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.ibatis.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jjtech.estock.model.DropDown;





/**
 * Class Email baru, menggunakan Apache Commons Email
 * Karena menggunakan javamail bermasalah dgn Exchange 2010
 * 
 * <p>Class yang digunakan untuk fungsi2 berhubungan dengan pengiriman e-mail. Harus digunakan sebagai spring bean.
 * <p>Class ini mempunyai dependency terhadap beberapa libraries, yaitu:
 * <ul>
 * 	<li>commons-email-1.2.jar</li>
 *  <li>mail.jar (versi 1.4.4)</li>
 * </ul>
 * 
 * Bahan Bacaan (Bila tidak bisa akses ke Exchange 2010 karena masalah Certificate SSL):
 * http://www.mkyong.com/webservices/jax-ws/suncertpathbuilderexception-unable-to-find-valid-certification-path-to-requested-target/
 * http://code.google.com/p/java-use-examples/source/browse/trunk/src/com/aw/ad/util/InstallCert.java
 * 
 * Cara penggunaan Apache Commons Email -> http://commons.apache.org/email/userguide.html
 * 
 * Cara validasi email -> http://stackoverflow.com/questions/624581/what-is-the-best-java-email-address-validation-method
 * 
 * @author Yusuf
 * @since Jan 18, 2012 (11:01:07 AM)
 *
 */
public class EmailSender {

	private static Logger logger = LoggerFactory.getLogger(EmailSender.class);
	Properties props;
	
	/**
	 * Main Class, untuk testing saja
	 * 
	 * @param args
	 * @throws EmailException
	 * @throws IOException 
	 */
	public static void main(String[] args) throws EmailException, IOException {
		
		//to
		String[] to = new String[]{"berto@sinarmasmsiglife.co.id"};
		
		//attachments
		List<String> ls = new ArrayList<String>();
		ls.add("D:\\Projects\\2007.ppt");
		ls.add("D:\\Projects\\fields.xls");
		
		//embedded images
		List<DropDown> ld = new ArrayList<DropDown>();
		ld.add(new DropDown("cid1", "C:\\Java\\Workspace\\E-Lions\\WebContent\\include\\image\\sinarmas.gif"));
		ld.add(new DropDown("cid2", "C:\\Java\\Workspace\\E-Lions\\WebContent\\include\\image\\SIMASLIFE-700x259.gif"));
		
		//emails
		Properties props = Resources.getResourceAsProperties("app.properties");	
		EmailSender emailSender = new EmailSender(props);

		logger.debug("--- Kirim email biasa ---");
		emailSender.send("berto@sinarmasmsiglife.co.id", to, null, null, 
				"Test kirim email biasa", "Pesan... Mohon di abaikan", null);

//		logger.debug("--- Kirim email biasa + attachment ---");
//		emailSender.send(null, to, null, null, 
//				"Test kirim email + attachment", "Pesan...", ls);
//
//		logger.debug("--- Kirim email html ---");
//		emailSender.sendHtml(null, to, null, null, 
//				"Test kirim email HTML", "Hi <strong style='color:red'>Yusuf</strong>. Apa kabar?", null, null);
//
//		logger.debug("--- Kirim email html + attachment ---");
//		emailSender.sendHtml(null, to, null, null, 
//				"Test kirim email HTML + attachment", "Hi <strong style='color:red'>Yusuf</strong>. Apa <span style='font-size:3em'>kabar</span>?", ls, null);
//
//		logger.debug("--- Kirim email html + attachment + embedded images ---");
//		emailSender.sendHtml(null, to, null, null, 
//				"Test kirim email HTML + attachment + embedded", "Hi <strong style='color:red'>Yusuf</strong>. Apa <span style='font-size:3em'>kabar</span>? <img src='cid:cid1'/><img src='cid:cid2'/>", ls, ld);
	}
	
	/**
	 * Constructor
	 */
	public EmailSender(Properties props) {
		this.props = props;
	}
	
	
	
	
	private void init(org.apache.commons.mail.Email email) throws EmailException{
		logger.debug("--- initializing ---");
		
		email.setDebug(Boolean.parseBoolean(props.getProperty("email.debug")));
		email.setHostName(props.getProperty("email.host"));
		email.setSmtpPort(Integer.parseInt(props.getProperty("email.port.smtp")));
		email.setAuthentication(props.getProperty("email.user"), props.getProperty("email.pass"));
		email.setTLS(false);
		email.setFrom(props.getProperty("admin.email.from"));
//		email.addReplyTo(props.getProperty("email.from"));
	}
	
	/**
	 * Setiap email send function memanggil fungsi ini di akhir
	 *
	 * @param email
	 * @param from
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param message
	 * @throws EmailException
	 */
	private void send(org.apache.commons.mail.Email email, String from, String[] to, String[] cc, String[] bcc, String subject, String message) throws EmailException {
		logger.debug("--- sending email ---");
//		if(from!=null){
//			email.addReplyTo(from);
//			email.setFrom(props.getProperty("email.from"), from);
//		}
		if(from!=null)email.setFrom(from,from );
		for(String t : to) email.addTo(t);
		if(cc != null) for(String c : cc) email.addCc(c);
		if(bcc != null) for(String b : bcc) email.addBcc(b);
 
		email.setSubject(subject);
		email.setMsg(message);

		email.send();
	}
	
	
	/**
	 * Fungsi send dengan/tanpa attachment (bukan html)
	 * 
	 * @param from bila diset null, maka akan menggunakan EMAIL_FROM diatas sebagai default
	 * @param to harus diisi
	 * @param cc opsional
	 * @param bcc opsional
	 * @param subject subjek email
	 * @param message pesan email
	 * @param attachments list dari file attachment (langsung string full path nya saja)
	 * @throws EmailException 
	 */
	public void send(String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<String> attachments) throws EmailException {
			logger.debug("--- send simple email w/wo attachment ---");
	
			//bila tidak ada attachment, pakai simple email saja, tidak perlu multipart email
			if(attachments == null){
				SimpleEmail email = new SimpleEmail();
				init(email);
				send(email, from, to, cc, bcc, subject, message);
			}else{
				MultiPartEmail email = new MultiPartEmail();
				init(email);
				
				//attachments
				if(attachments != null){
					for(String s : attachments){
						  EmailAttachment attachment = new EmailAttachment();
						  attachment.setPath(s);
						  attachment.setDisposition(EmailAttachment.ATTACHMENT);
						  email.attach(attachment);
					}
				}
				
				send(email, from, to, cc, bcc, subject, message);		
			}
	}

	/**
	 * Fungsi send HTML dengan/tanpa attachment, dengan/tanpa embedded images
	 * 
	 * @param from bila diset null, maka akan menggunakan EMAIL_FROM diatas sebagai default
	 * @param to harus diisi
	 * @param cc opsional
	 * @param bcc opsional
	 * @param subject subjek email
	 * @param message pesan email
	 * @param attachments list dari file attachment (langsung string full path nya saja)
	 * @param embed list dari embedded image (langsung string full path nya saja)
	 *        dalam list dropdown key = nama image, value = lokasi image
	 *        di html emailnya gambar di tulis dengan format cid:<nama_image> 
	 *        contoh : 
	 *        htmlnya ==> <img src=\"cid:promo\" width=\"100px\"/>
	 *        di dropdown ==>	 listImageEmbeded.add(new DropDown("promo","\\\\ebserver\\pdfind\\simascard\\logos\\2012.jpg"));
	 * @throws EmailException 
	 */
	public void sendHtml(String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<String> attachments, List<DropDown> embed) throws EmailException {
		logger.debug("--- send html email w/wo attachment w/wo embedded ---");

		HtmlEmail email = new HtmlEmail();
		init(email);
		
		//attachments
		if(attachments != null){
			for(String s : attachments){
				  EmailAttachment attachment = new EmailAttachment();
				  attachment.setPath(s);
				  attachment.setDisposition(EmailAttachment.ATTACHMENT);
				  email.attach(attachment);
			}
		}
		
		//embedded images
		if(embed != null){
			for(DropDown e : embed){
				String cid = email.embed(new File(e.getValue()), e.getKey());
				message = message.replaceAll(e.getKey(), cid);
			}
		}
		
		send(email, from, to, cc, bcc, subject, message);		
	}
	
	
	

}