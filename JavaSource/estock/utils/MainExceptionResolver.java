package com.jjtech.estock.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.jjtech.estock.model.User;

/**
 * Class yang digunakan untuk me-redirect error yang terjadi di aplikasi ke
 * halaman bersangkutan (view bernama 'exception')
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:03:46 AM)
 *
 */
public class MainExceptionResolver extends AbstractHandlerExceptionResolver {
	
	@Autowired
	private Email email;
	
	@Autowired
	private Properties props;

	private static Logger logger = Logger.getLogger(MainExceptionResolver.class);
	
	@Override
	public ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy (HH:mm:ss)");
		Date now = new Date();
		
        User currentUser = (User) request.getSession(false).getAttribute("currentUser");
		StringBuffer stackTrace = new StringBuffer();
		String exception = null;

		stackTrace.append("\n===== START ERROR [eStock][" + df.format(now) + "] ===============");
		stackTrace.append("\n\n- Class : " + handler);
		if(currentUser!=null) stackTrace.append("\n- User : " + currentUser.username + "] ");
		
		stackTrace.append("\n- Request Parameters : ");
		Enumeration<?> paramNames = request.getParameterNames();
		while(paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			stackTrace.append("\n  " + paramName + " = " + request.getParameter(paramName));
		}

		stackTrace.append("\n- Request Headers: ");
		Enumeration<?> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = (String) headerNames.nextElement();
			stackTrace.append("\n  " + headerName + " = " + request.getHeader(headerName));
		}
		
		stackTrace.append("\n- Request Method : " + request.getMethod());
		stackTrace.append("\n- Request Protocol : " + request.getProtocol());
		stackTrace.append("\n- Request QueryString : " + request.getQueryString());
		stackTrace.append("\n- Request RemoteAddr : " + request.getRemoteAddr());
		stackTrace.append("\n- Request RemoteHost : " + request.getRemoteHost());
		stackTrace.append("\n- Request RemotePort : " + request.getRemotePort());
		stackTrace.append("\n- Request RequestURI : " + request.getRequestURI());
		stackTrace.append("\n- Request Scheme : " + request.getScheme());
		stackTrace.append("\n- Request ServerName : " + request.getServerName());
		stackTrace.append("\n- Request ServerPort : " + request.getServerPort());
		stackTrace.append("\n- Request ServletPath : " + request.getServletPath());
		stackTrace.append("\n- Request RequestURL : " + request.getRequestURL());		
		stackTrace.append("\n- Session No Pre: " + request.getSession().getAttribute("no_pre"));		
		
		stackTrace.append("\n- Exception : \n");

		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		stackTrace.append(sw);
		stackTrace.append("\n===== END ERROR ===============");

		exception = sw.toString();
		
		try {
			sw.close();
			pw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		logger.error(stackTrace);
		
		
		email.send(
				true, props.getProperty("admin.email.from"),
				props.getProperty("admin.email.to").split( ";" ), null, null,
				"ERROR pada eStock", stackTrace.toString(), null);
		

		Map<String, Object> m = new HashMap<String, Object>();
		m.put("exception", exception);
		return new ModelAndView("exception", m); //exception.jsp
	}

}