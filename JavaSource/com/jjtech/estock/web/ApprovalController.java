package com.jjtech.estock.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.User;
import com.jjtech.estock.utils.Utils;

/**
 * 
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: May 14, 2013 11:22:19 AM
 * @Description : modul untuk approval customer yang berhutang lebih dari limit
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
@Controller
@RequestMapping("/approval")
public class ApprovalController extends ParentController{

	//show form pertama kali
	@RequestMapping(method=RequestMethod.GET)
	public String show(@ModelAttribute("user") User user, Model model,HttpServletRequest request, HttpSession session) {
		logger.debug("Halaman: Approval, method: SHOW");
		String no_trans=ServletRequestUtils.getStringParameter(request, "no_trans", "");
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		user.trans = dbService.selectListTrans(null, null, null, no_trans, null).get(0);
		user.trans.customer=dbService.selectListCustomer(user.trans.customer_id).get(0);
		user.totalHutang=dbService.selectHutangCustomer(user.trans.customer_id);

		return "trans/approval";
	}
	
	//saat user menekan tombol login, process form
	@RequestMapping(method=RequestMethod.POST)
	public String processForm( @ModelAttribute("user") User user, BindingResult result, HttpServletRequest request,Model model, RedirectAttributes ra) { 
		logger.debug("Halaman: LOGIN, method: PROCESSFORM");

		//validasi dasar di model object, validasi lainnya langsung didalam controller
		
	
		BindException errors = new BindException(result);
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", new String[]{"Username"});
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty", new String[]{"Password"});
		User currentUser=(User) request.getSession().getAttribute("currentUser");
		Integer aksescabang_id=null;
		if(Utils.nvl(currentUser.flag_akses_all)!=1)aksescabang_id=currentUser.cabang_id;
		if (!result.hasErrors()) {
			User tmp = dbService.selectUser(user.username);
			user.trans=dbService.selectListTrans(null, null, null, user.trans.no_trans,aksescabang_id).get(0);
			user.trans.customer=dbService.selectListCustomer(user.trans.customer_id).get(0);
			String no_trans=user.trans.no_trans;
			
			if(tmp == null) {
				errors.rejectValue("username", "", "Username tidak terdaftar");
			}else if(!tmp.passwordDecrypt.toLowerCase().equals(user.password.toLowerCase())) {
				errors.rejectValue("password", "", "Password Salah");
			}else if(tmp.flag_approval!=1) {
				errors.rejectValue("username", "", "Username bukan user approval");
			}else if(tmp.cabang_id!=user.trans.retail_id) {
				errors.rejectValue("username", "", "Username bukan dari cabang "+user.trans.retail_idKet);
			}else if(tmp.active!=1) {
				errors.rejectValue("username", "", "Username tidak aktif");
			}else {
				//set beberapa item dari object user yg ditarik dari database ke object user yg dipakai untuk login
				user=tmp;
				user.trans=dbService.selectListTrans(null, null, null,no_trans,aksescabang_id).get(0);
				user.trans.customer=dbService.selectListCustomer(user.trans.customer_id).get(0);
//				user.setGroup_menu_id(tmp.getGroup_menu_id());FIXME: belum ada groupmenu
				
			}
		}

		
		if (result.hasErrors()) {
			return "trans/approval";
		}
		
		//bila tidak error, lanjut ke login
		String pesan =dbService.approval(user, sessionRegistry, request);
		
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("messageType", "done");
		map.put("message",pesan );
		map.put("trans_idnya",user.trans.trans_id );
		model.addAllAttributes(map);
		
		return "trans/approval";
	}	
	
}
