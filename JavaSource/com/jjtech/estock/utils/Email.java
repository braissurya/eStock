package com.jjtech.estock.utils;



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;


import org.apache.commons.mail.EmailException;
import org.apache.ibatis.io.Resources;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jjtech.estock.model.DropDown;
import com.mchange.v1.util.ArrayUtils;






/**
 * <p>Class yang digunakan untuk fungsi2 berhubungan dengan pengiriman e-mail. Harus digunakan sebagai spring bean.
 * <p>Class ini mempunyai dependency terhadap beberapa libraries, yaitu:
 * <ul>
 * 	<li>activation.jar</li>
 * 	<li>commons-logging-1.0.4.jar</li>
 * 	<li>mail.jar</li>
 * 	<li>spring.jar</li>
 * </ul>
 * 
 * @author Yusuf
 * @since Feb 7, 2011 (4:51:10 PM)
 */
@Component //@Component berarti otomatis register sebagai bean, tanpa perlu didefinisikan di spring xml
public class Email {
	

	@Autowired
	protected Properties props;	
	



	public void setProps(Properties props) {
		this.props = props;
	}

	/**
	 * Fungsi main untuk testing, sekalian bisa lihat cara penggunaannya
	 * 
	 * @author Yusuf
	 * @since Jun 20, 2008 (10:32:57 AM)
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		//siapin object mail sender
		Email email=new Email();
		email.setProps(Resources.getResourceAsProperties("app.properties"));	
		
		//apakah ada attachment?
		List<File> attachments = new ArrayList<File>();
		attachments.add(new File("G:\\WebKerjasama.xls"));
		
		List<DropDown> listImageEmbeded=new ArrayList<DropDown>();
		listImageEmbeded.add(new DropDown("promo","\\\\ebserver\\pdfind\\simascard\\logos\\100.jpg"));
		email.send(
				false,
				"ajsjava@sinarmasmsiglife.co.id", 
				new String[]{"berto@sinarmasmsiglife.co.id"}, 
				null, 
				null,
				"Testing with non html simple mail", 
				"MOHON DI ABAIKAN SEDANG TESTING", 
				null);		
	}
	
	/**
	 * Fungsi untuk testing saja, email diprint ke console, bukan di kirim beneran
	 * 
	 * @author Yusuf
	 * @since Mar 11, 2011 (7:39:58 PM)
	 *
	 */
	private void printEmailToConsole(String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments){
		System.out.println("FROM		: " + from);
		System.out.println("TO		: " + ArrayUtils.toString(to));
		if(cc!=null)
		System.out.println("CC		: " + ArrayUtils.toString(cc));
		if(bcc!=null)
		System.out.println("BCC		: " + ArrayUtils.toString(bcc));
		System.out.println("SUBJECT		: " + subject);
		System.out.println("ATTACHMENTS	:");
		if(attachments!=null)
		for(File f : attachments) System.out.println("- " + f.getName());
		System.out.println("MESSAGE		:");
		System.out.println(message);
	}
	
	/**
	 * Fungsi untuk mengirim e-mail
	 * 
	 * @author Yusuf
	 * @since Jun 20, 2008 (10:16:41 AM)
	 * 
	 * @param isHtml flag untuk mengirim email dalam format plain text atau html
	 * @param from alamat email pengirim
	 * @param to alamat-alamat email tujuan
	 * @param cc alamat-alamat email CC
	 * @param bcc alamat-alamat email BCC
	 * @param subject subyek emailnya
	 * @param message pesan yang didalam email
	 * 
	 * @throws MailException
	 * @throws MessagingException
	 * @throws EmailException 
	 */
	public void send(boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments)
			{
		if(new Boolean(props.getProperty("email.enabled"))){
			EmailSender emailSender = new EmailSender(props);
			
			List<String> lsattachment = null;
			if(attachments!=null){
				lsattachment=new ArrayList<String>();
				for(File attach:attachments){
					if(attach!=null)
					lsattachment.add(attach.getPath());
				}
			}
			try {
				if(isHtml){
					emailSender.sendHtml(from, to, cc, bcc, 
					subject, message, lsattachment, null);
				}else{
					emailSender.send(from, to, cc, bcc, 
							subject, message, lsattachment);
				
				}
			} catch (EmailException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}else{
			printEmailToConsole(from, to, cc, bcc, subject, message, attachments);
		}
	}
	
	/**
	 * Fungsi kirim email dengan tambahan image yang bisa embeded
	 * @author Bertho
	 * @since 23 Sept 2010 14:18
	 * @param isHtml flag untuk mengirim email dalam format plain text atau html
	 * @param from alamat email pengirim
	 * @param to alamat-alamat email tujuan
	 * @param cc alamat-alamat email CC
	 * @param bcc alamat-alamat email BCC
	 * @param subject subyek emailnya
	 * @param message pesan yang didalam email
	 * @param attachments file yang di attach
	 * @param listImageEmbeded 
	 *        dalam list dropdown key = nama image, value = lokasi image
	 *        di html emailnya gambar di tulis dengan format cid:<nama_image> 
	 *        contoh : 
	 *        htmlnya ==> <img src=\"cid:promo\" width=\"100px\"/>
	 *        di dropdown ==>	 listImageEmbeded.add(new DropDown("promo","\\\\ebserver\\pdfind\\simascard\\logos\\2012.jpg"));
	 * @throws MailException
	 * @throws MessagingException
	 * @throws EmailException 
	 */
	public void sendImageEmbeded(boolean isHtml, String from, String[] to, String[] cc, String[] bcc, String subject, String message, List<File> attachments,List<DropDown> listImageEmbeded) 
	throws MailException, MessagingException, EmailException{
		if(new Boolean(props.getProperty("email.enabled"))){
			EmailSender emailSender = new EmailSender(props);
			
			List<String> lsattachment = null;
			if(attachments!=null){
				lsattachment=new ArrayList<String>();
				for(File attach:attachments){
					if(attach!=null)
					lsattachment.add(attach.getPath());
				}
			}
		
				if(isHtml){
					emailSender.sendHtml(from, to, cc, bcc, 
					subject, message, lsattachment, listImageEmbeded);

				}else{
					
						emailSender.send(from, to, cc, bcc, 
								subject, message, lsattachment);
				
				}
		

		}else{
			printEmailToConsole(from, to, cc, bcc, subject, message, attachments);
		}
	}
	
	/**
	 * Fungsi untuk merapihkan string array daftar email, misalnya : "a@b.com, null, b@c.d, , null" menjadi "a@b.com, b@c.d"
	 *  
	 * @author Yusuf
	 * @since Jun 20, 2008 (10:14:37 AM)
	 * 
	 * @param emails alamat-alamat email yang ingin dirapihkan
	 * @return alamat-alamat email yang sudah dirapihkan
	 */
	private String[] trim(String[] emails) {
		StringBuffer trimmed = new StringBuffer();
		for(String email : emails) {
			if(email != null) {
				if(!email.trim().equals("")) {
					if(!trimmed.toString().trim().equals("")) trimmed.append(",");
					trimmed.append(email);
				}
			}
		}
		return StringUtils.commaDelimitedListToStringArray(trimmed.toString());
	}

}