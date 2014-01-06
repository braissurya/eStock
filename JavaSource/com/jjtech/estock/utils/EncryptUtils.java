package com.jjtech.estock.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;



/**
 * Fungsi2 tambahan untuk encryption
 * 
 * @author Yusuf Sutarko
 * @since Jun 27, 2007 (1:51:33 PM)
 */
public class EncryptUtils {/*
	private static String passphrase="spiderWeb200704";
	*//**
	 * Encrypt dengan SHA-1, lalu Encode dengan Base64
	 * 
	 * @author Yusuf Sutarko
	 * @since Jun 27, 2007 (1:51:18 PM)
	 * @param bytes
	 * @return
	 *//*
	public static String encode(byte[] bytes) {
		return new sun.misc.BASE64Encoder().encode(DigestUtils.sha(bytes));
	}
	
	*//**
	 * FUNSGSI ENCRYPT BARU
	 * @author Berto
	 * @since Apr 24, 2008 1:46:04 PM
	 * @param data
	 * @return
	 * @throws Exception
	 *//*
	public static String encrypt(String data)
			throws Exception {

		byte[] dataTemp;
		//		 PMLogger logger = new PMLogger(PMLoggerConstants.DEFECTO);

		try {
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passphrase.getBytes());
			DESKeySpec key = new DESKeySpec(md.digest());
			SecretKeySpec DESKey = new SecretKeySpec(key.getKey(), "DES");

			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, DESKey);
			dataTemp = cipher.doFinal(data.getBytes());
		} catch (Exception e) {

			//		 logger.error("[com.pm.util.directorio.DESEncryptor][encrypt] Se produjo una excepción durante la encriptación: "+e);

			throw e;

		}

		return new BASE64Encoder().encode(dataTemp);
	}

	*//**
	 * FUNGSI DECYPT TERBARU
	 * @author Berto
	 * @since Apr 24, 2008 1:46:31 PM
	 * @param data
	 * @return
	 * @throws Exception
	 *//*
	public static String decrypt(String data)
			throws Exception {

		byte[] dataTemp;
		
		//		 PMLogger logger = new PMLogger(PMLoggerConstants.DEFECTO);
		BASE64Decoder bas=new BASE64Decoder();
		
		try {
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passphrase.getBytes());
			DESKeySpec key = new DESKeySpec(md.digest());
			SecretKeySpec DESKey = new SecretKeySpec(key.getKey(), "DES");

			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, DESKey);

			dataTemp = cipher.doFinal(bas.decodeBuffer(data));

		} catch (Exception e) {

			//		 logger.error("[com.pm.util.directorio.DESEncryptor][decrypt] Se produjo una excepción durante la desencriptación: "+e);

			throw e;

		}

		return new String(dataTemp);

	}
	
	*//**
	 * FUNSGSI ENCRYPT BARU
	 * @author Berto
	 * @since Apr 24, 2008 1:46:04 PM
	 * @param data
	 * @return
	 * @throws Exception
	 *//*
	public static byte [] encryptByte(String data)
			throws Exception {

		byte[] dataTemp;
		//		 PMLogger logger = new PMLogger(PMLoggerConstants.DEFECTO);

		try {
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passphrase.getBytes());
			DESKeySpec key = new DESKeySpec(md.digest());
			SecretKeySpec DESKey = new SecretKeySpec(key.getKey(), "DES");

			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, DESKey);
			dataTemp = cipher.doFinal(data.getBytes());
		} catch (Exception e) {

			//		 logger.error("[com.pm.util.directorio.DESEncryptor][encrypt] Se produjo una excepción durante la encriptación: "+e);

			throw e;

		}

		return dataTemp;
	}
	
	*//**
	 * FUNGSI DECYPT menjadi String
	 * @author Berto
	 * @since Apr 24, 2008 1:46:31 PM
	 * @param data
	 * @return
	 * @throws Exception
	 *//*
	public static String decryptByte(byte[] data)
			throws Exception {

		byte[] dataTemp;
		
		//		 PMLogger logger = new PMLogger(PMLoggerConstants.DEFECTO);
		BASE64Decoder bas=new BASE64Decoder();
		
		try {
			Security.addProvider(new com.sun.crypto.provider.SunJCE());
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(passphrase.getBytes());
			DESKeySpec key = new DESKeySpec(md.digest());
			SecretKeySpec DESKey = new SecretKeySpec(key.getKey(), "DES");

			Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, DESKey);

			dataTemp = cipher.doFinal(data);

		} catch (Exception e) {

			//		 logger.error("[com.pm.util.directorio.DESEncryptor][decrypt] Se produjo una excepción durante la desencriptación: "+e);

			throw e;

		}

		return new String(dataTemp);

	}
	
	public static String encodeURL(String url) {
		try {
			try {
				url = EncryptUtils.encrypt (url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return URLEncoder.encode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String decodeURL(String url) {
		try {
			url=URLDecoder.decode(url, "UTF-8");
			 try {
				return EncryptUtils.decrypt (url);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
		

	public static void main(String[] args) throws Exception {

//		 Datos de entrada:
//		 args[0] -> cadena para generar la clave de encriptación
//		 args[1] -> datos a encriptar
		
		System.out.println(EncryptUtils.encodeURL("00"));
		
		System.out.println(EncryptUtils.encode(("yusufsutarko33061000150".trim()).getBytes()));
		
//		RpBygB/5x69qiKbM3nRNh5xy5wU=

//		String sClave = "spiderWeb200704";
//		String sDatos = "4211673322221111";
//		
//		String bd="0JV+/S4IHSO=";
//		
//		byte [] ba = sDatos.getBytes ();
//
//		System.out.println("CLAVE: " + sClave);
//		System.out.println("MENSAJE ORIGINAL: " + new String (ba));
//
////		 Encriptación
//
//		String bas = EncryptUtils.encrypt (sDatos);	
//		System.out.println("MENSAJE ENCRIPTADO: " + bas);
//
////		 Desencriptación		
//		
//		bas = EncryptUtils.decrypt(bas);
//		System.out.println("MENSAJE DESENCRIPTADO: " + bas);
//		
//		bas =EncryptUtils.encodeURL(sDatos);
//		System.out.println("MENSAJE DESENCRIPTADO: " + bas);
//		
//		bas =EncryptUtils.decodeURL(bas);
//		System.out.println("MENSAJE DESENCRIPTADO: " + bas);
//		
//		byte [] bas2 = EncryptUtils.encryptByte(sDatos);	
//		System.out.println("Encrypt jadi byte: " + bas2);
//
////		 Desencriptación		
//		
//		bas = EncryptUtils.decryptByte(bas2);
//		System.out.println("decrypt bytenya jadi...: " + bas);

		} 

*/}