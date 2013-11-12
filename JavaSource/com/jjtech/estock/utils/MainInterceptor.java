package com.jjtech.estock.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.jjtech.estock.model.User;

/**
 * Interceptor untuk meng-intercept request misalnya user yg belum login, etc
 * 
 * @author Yusuf
 * @since Jan 23, 2013 (9:11:10 AM)
 *
 */
public class MainInterceptor extends HandlerInterceptorAdapter {

	private static Logger logger = Logger.getLogger(MainInterceptor.class);
	
	private final String[] boleh = {"/login", "/static"}; //login dan static resources, boleh lewat
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		logger.debug("preHandle: " + handler);
		String uri = request.getRequestURI();
		logger.debug("Request URI = " + uri);
		
		//khusus halaman tertentu, boleh lewat
		for(String b : boleh){
			if(uri.toLowerCase().contains(b)){
				return true;
			}
		}

		//cek apakah user sudah ada session atau belum
		HttpSession session = request.getSession(false); //kenapa false? karena kalau tidak, dia akan selalu create session baru, makan memory		
        if (session != null) {
        	//cek user logged in
        	User currentUser = (User) session.getAttribute("currentUser");
        	if(currentUser != null) {
        		return true;
        	}
        }
        
        //bila tidak ada session / user, maka arahkan ke login
        response.sendRedirect(request.getContextPath()+"/login");
        return false;
	}
	
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		logger.debug("postHandle: " + handler);

		super.postHandle(request, response, handler, modelAndView);
	}
	
}