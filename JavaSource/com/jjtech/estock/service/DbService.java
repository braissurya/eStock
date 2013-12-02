package com.jjtech.estock.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.jjtech.estock.model.Category;
import com.jjtech.estock.model.COA;
import com.jjtech.estock.model.ClosingPeriode;
import com.jjtech.estock.model.Customer;
import com.jjtech.estock.model.Delivery;
import com.jjtech.estock.model.DeliveryDet;
import com.jjtech.estock.model.DropDown;
import com.jjtech.estock.model.GroupUser;
import com.jjtech.estock.model.Gudang;
import com.jjtech.estock.model.Item;
import com.jjtech.estock.model.Karyawan;
import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.Opname;
import com.jjtech.estock.model.OpnameDet;
import com.jjtech.estock.model.Payroll;
import com.jjtech.estock.model.RepTB;
import com.jjtech.estock.model.Payment;
import com.jjtech.estock.model.Stock;
import com.jjtech.estock.model.StockHist;
import com.jjtech.estock.model.Supplier;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.TransDet;
import com.jjtech.estock.model.TransHist;
import com.jjtech.estock.model.Trx;
import com.jjtech.estock.model.TrxDet;
import com.jjtech.estock.model.User;
import com.jjtech.estock.persistence.DbMapper;
import com.jjtech.estock.utils.SessionRegistry;
import com.jjtech.estock.utils.Utils;
import com.jjtech.estock.validator.PaymentValidator;
import com.jjtech.estock.model.Merk;
import com.jjtech.estock.model.Satuan;
import com.jjtech.estock.model.Warna;
import com.jjtech.estock.model.Config;
import com.jjtech.estock.model.Cabang;
import com.jjtech.estock.model.Bank;
import com.jjtech.estock.model.Account;


/**
 * Main service object. semua business logic diletakkan disini
 * Fitur baru MyBatis, tidak perlu buat class DAO implementation lagi, cukup buat mapper interface + sql nya saja langsung bisa pakai
 *
 * @author Yusuf
 * @since Jan 23, 2013 (9:25:35 AM)
 *
 */
@Transactional
public class DbService {

	private static Logger logger = Logger.getLogger(DbService.class);

	@Autowired
	private DbMapper dbMapper;

	@Autowired
	private MessageSource messageSource;

	public Date selectSysdate() {
		logger.debug("selectSysdate");
		return dbMapper.selectSysdate();
	}

	/**
	 *
	 * @Method_name	: selectDropDown
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 3, 2013 3:51:43 PM
	 * @return_type : List<DropDown>
	 * @Description : Template Query untuk query-query berupa list dropdown
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<DropDown> selectDropDown(String key, String value, String table, String where, String orderby) {
		return dbMapper.selectDropDown(key, value, table, where, orderby);
	}



	/**
	 *
	 * @Method_name	: selectCountTable
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 3, 2013 3:52:19 PM
	 * @return_type : Integer
	 * @Description : Template Query untuk Count suatu table
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public Integer selectCountTable(String table, String where) {
		return dbMapper.selectCountTable(table, where);
	}


	/**
	 *
	 * @Method_name	: selectUser
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 3, 2013 3:59:08 PM
	 * @return_type : User
	 * @Description : query ke table user
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public User selectUser(String username){
		return dbMapper.selectUser(username);
	}



	/**
	 *
	 * @Method_name	: login
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 3, 2013 3:59:42 PM
	 * @return_type : void
	 * @Description : Modul untuk login
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public void login(User currentUser, SessionRegistry sessionRegistry, HttpServletRequest request) {
		logger.debug("login(User currentUser, SessionRegistry sessionRegistry, HttpServletRequest request)");
		//bila sudah pernah login, kick session lama
		if(sessionRegistry.contains(currentUser)) sessionRegistry.kick(currentUser, request.getSession(false));
		//create session baru
		HttpSession session = request.getSession(true);
		//set login time
		currentUser.setLoginTime(dbMapper.selectSysdate());
		//set menu sesuai hak akses FIXME: belum ada hak akses
		currentUser.setListMenu(dbMapper.selectMenuAccess(currentUser.group_user_id,null,null));

		//generate menu
		currentUser.setMenuUser(Utils.generateMenu(request.getContextPath(), currentUser,this));
		currentUser.setSiteMap(Utils.generateSiteMap(request.getContextPath(), currentUser, currentUser.getListMenu()));

		//letakkan currentUser di session
		session.setAttribute("currentUser", currentUser);
		//letakkan currentUser di daftar user
		sessionRegistry.put(currentUser);
	}
	
	public String approval(User currentUser, SessionRegistry sessionRegistry, HttpServletRequest request) {
		logger.debug("login(User currentUser, SessionRegistry sessionRegistry, HttpServletRequest request)");
		String pesan="";
		
		currentUser.trans.approveby=currentUser.id;
		currentUser.trans.approvedate=dbMapper.selectSysdate();
		updateTrans(currentUser.trans);
		
		pesan = messageSource.getMessage("submitsuccess", new String[]{"Approval",""+currentUser.trans.no_trans," di setujui"},null);
		return pesan;
	}

	/**
	 *
	 * @Method_name	: selectMenuAccess
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 8, 2013 11:21:20 AM
	 * @return_type : List<Menu>
	 * @Description : modul untuk query menu access user
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<Menu> selectMenuAccess(Integer group_user_id,Integer root,String path){
		return dbMapper.selectMenuAccess(group_user_id, root,path);
	}


	/**
	 *
	 * @Method_name	: selectAllUser
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:38:50 AM
	 * @return_type : List<User>
	 * @Description : modul untuk query user
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<User> selectAllUser(Integer group_menu_id,Integer id) {
		return dbMapper.selectAllUser(group_menu_id, id);
	}

	/**
	 *
	 * @Method_name	: selectListUserPaging
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:39:17 AM
	 * @return_type : List<User>
	 * @Description : modul untuk guery user paging
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<User> selectListUserPaging(String search,Integer offset, Integer rowcount,String sort,String sort_type, Integer group_menu_id) {
		return dbMapper.selectListUserPaging(search, offset, rowcount, sort, sort_type, group_menu_id);
	}

	/**
	 *
	 * @Method_name	: selectListUserPagingCount
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:39:53 AM
	 * @return_type : Integer
	 * @Description : modul untuk query jumlah paging user
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public Integer selectListUserPagingCount(String search,Integer group_menu_id) {
		return dbMapper.selectListUserPagingCount(search, group_menu_id);
	}

	/**
	 *
	 * @Method_name	: saveUser
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:40:34 AM
	 * @return_type : String
	 * @Description : modul proses User
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public String saveUser(User user, User currentUser){
		logger.debug("saveUser(User user, User currentUser)");

		String pesan=null;

		if("NEW".equals(user.mode)){
			user.active=1;
			user.createby = currentUser.id;
			user.createdate = dbMapper.selectSysdate();
			user.passwordDecrypt="123BCD";
			user.id=dbMapper.insertUser(user);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master User",""+user.username,"ditambahkan"},null);
		}else if("DELETE".equals(user.mode)){
			user.active=0;
			user.modifyby = currentUser.id;
			user.modifydate = dbMapper.selectSysdate();
			dbMapper.updateUser(user);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master User",""+user.username,"dihapus"},null);
		}else if("RESET".equals(user.mode)){
			user.password="123BCD";
			user.passwordDecrypt=user.password;
			user.modifyby = currentUser.id;
			user.modifydate = dbMapper.selectSysdate();
			dbMapper.updateUser(user);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master User",""+user.username,"direset"},null);
		}else if("CHANGE".equals(user.mode)){
			user.passwordDecrypt=user.password;
			user.modifyby = currentUser.id;
			user.modifydate = dbMapper.selectSysdate();
			dbMapper.updateUser(user);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master User",""+user.username,"diubah"},null);
		}else if("EDIT".equals(user.mode)){
			user.active=1;
			user.modifyby = currentUser.id;
			user.modifydate = dbMapper.selectSysdate();
			dbMapper.updateUser(user);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master User",""+user.username,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+user.mode);
		}

		return pesan;
	}

	/**
	 *
	 * @Method_name	: selectListMenu
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:42:42 AM
	 * @return_type : List<Menu>
	 * @Description : modul untuk query menu
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<Menu> selectListMenu(Integer id) {
		return dbMapper.selectListMenu(id);
	}

	/**
	 *
	 * @Method_name	: selectListMenuPaging
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:43:07 AM
	 * @return_type : List<Menu>
	 * @Description : modul untuk query menu paging
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<Menu> selectListMenuPaging(String search,Integer offset,Integer rowcount,String sort,String sort_type) {
		return dbMapper.selectListMenuPaging(search, offset, rowcount, sort, sort_type);
	}

	/**
	 *
	 * @Method_name	: selectListMenuPagingCount
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:43:31 AM
	 * @return_type : Integer
	 * @Description : modul untuk query paging menu
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public Integer selectListMenuPagingCount(String search) {
		return dbMapper.selectListMenuPagingCount(search);
	}

	/**
	 *
	 * @Method_name	: saveMenu
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:44:38 AM
	 * @return_type : String
	 * @Description : modul proses menu
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public String saveMenu(Menu menu, User currentUser){
		logger.debug("saveMenu(Menu menu, User currentUser)");

		String pesan;

		if("NEW".equals(menu.mode)){
			menu.active=1;
			menu.createby = currentUser.id;
			menu.createdate = dbMapper.selectSysdate();
			menu.level=selectListMenu(menu.parent).get(0).level+1;
			menu.id=dbMapper.insertMenu(menu);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Menu",""+menu.nama,"ditambah"},null);
		}else if("DELETE".equals(menu.mode)){
			menu.active=0;
			menu.modifyby = currentUser.id;
			menu.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMenu(menu);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Menu",""+menu.nama,"dihapus"},null);
		}else  if("EDIT".equals(menu.mode)){
			menu.modifyby = currentUser.id;
			menu.modifydate = dbMapper.selectSysdate();
			menu.level=selectListMenu(menu.parent).get(0).level+1;
			dbMapper.updateMenu(menu);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Menu",""+menu.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+menu.mode);
		}


		return pesan;
	}


	/**
	 *
	 * @Method_name	: selectListHakAkses
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 10, 2013 2:49:25 AM
	 * @return_type : List<GroupUser>
	 * @Description : modul query hak akses
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<GroupUser> selectListHakAkses(Integer id,Integer menu_id,Integer grouping,Integer aktif) {
		return dbMapper.selectListHakAkses(id,menu_id,grouping,aktif);
	}

	/**
	 *
	 * @Method_name	: selectListHakAksesPaging
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:48:19 AM
	 * @return_type : List<GroupUser>
	 * @Description : modul untuk query paging hak akses
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<GroupUser> selectListHakAksesPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type,Integer grouping) {
		return dbMapper.selectListHakAksesPaging(search, offset, rowcount, sort, sort_type,grouping);
	}

	/**
	 *
	 * @Method_name	: selectListHakAksesPagingCount
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:48:58 AM
	 * @return_type : Integer
	 * @Description : modul untuk query jumlah hak akses
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public Integer selectListHakAksesPagingCount(String search,Integer grouping) {
		return dbMapper.selectListHakAksesPagingCount(search,grouping);
	}

	/**
	 *
	 * @Method_name	: selectListGroupUser
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:47:43 AM
	 * @return_type : List<GroupUser>
	 * @Description : modul untuk query group user
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<GroupUser> selectListGroupUser(Integer id,Integer menu_id,Integer grouping,Integer aktif) {
		return dbMapper.selectListGroupUser(id,menu_id,grouping,aktif);
	}

	/**
	 *
	 * @Method_name	: selectListGroupUserPaging
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:48:19 AM
	 * @return_type : List<GroupUser>
	 * @Description : modul untuk query paging group user
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<GroupUser> selectListGroupUserPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type,Integer grouping) {
		return dbMapper.selectListGroupUserPaging(search, offset, rowcount, sort, sort_type,grouping);
	}

	/**
	 *
	 * @Method_name	: selectListGroupUserPagingCount
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:48:58 AM
	 * @return_type : Integer
	 * @Description : modul untuk query jumlah group user
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public Integer selectListGroupUserPagingCount(String search,Integer grouping) {
		return dbMapper.selectListGroupUserPagingCount(search,grouping);
	}

	/**
	 *
	 * @Method_name	: saveGroupUser
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Mar 9, 2013 6:50:07 AM
	 * @return_type : String
	 * @Description : modul untuk proses group user
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public String saveGroupUser(GroupUser groupUser, User currentUser){
		logger.debug("saveGroupUser(GroupUser groupUser, User currentUser)");

		String pesan;

		if("NEW".equals(groupUser.mode)){
			dbMapper.insertGroupUser(groupUser);
			for (Menu mn:groupUser.getMenu()) {
				groupUser.menu_id=mn.id;
				if(mn.akses){
					groupUser.active=1;
					dbMapper.insertHakAkses(groupUser);
				}
			}

			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master GroupUser",""+groupUser.nama,"ditambah"},null);
		}else if("DELETE".equals(groupUser.mode)){
			dbMapper.deleteGroupUser(groupUser.id);
			groupUser.menu_id=null;
			groupUser.active=0;
			groupUser.group_user_id=groupUser.id;
			dbMapper.deleteHakAkses(groupUser.group_user_id);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master GroupUser",""+groupUser.nama,"dihapus"},null);
		}else if("EDIT".equals(groupUser.mode)){
			for (Menu mn:groupUser.getMenu()) {
				groupUser.menu_id=mn.id;
				if(mn.akses){
					if(selectListHakAkses(groupUser.id, mn.id, null,null).isEmpty()){
						groupUser.active=1;
						dbMapper.insertHakAkses(groupUser);
					}else{
						groupUser.active=1;
						dbMapper.updateHakAkses(groupUser);
					}
				}else{
					if(!selectListGroupUser(groupUser.id, mn.id, null,null).isEmpty()){
						groupUser.active=0;
						dbMapper.updateHakAkses(groupUser);
					}
				}
			}
			dbMapper.updateGroupUser(groupUser);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master GroupUser",""+groupUser.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+groupUser.mode);
		}


		return pesan;
	}

	/**
	 *
	 * @Method_name	: selectListCategory
	 * @author 		: Deddy
	 * @since		: Mar 15, 2013 10:42:42 AM
	 * @return_type : List<Category>
	 * @Description : modul untuk query Category
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<Category> selectListCategory(Integer id) {
		return dbMapper.selectListCategory(id);
	}

	/**
	 *
	 * @Method_name	: selectListCategoryPaging
	 * @author 		: Deddy
	 * @since		: Mar 15, 2013 9:48:19 AM
	 * @return_type : List<Category>
	 * @Description : modul untuk query paging Category
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<Category> selectListCategoryPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListCategoryPaging(search, offset, rowcount, sort, sort_type);
	}

	/**
	 *
	 * @Method_name	: selectListCategoryPagingCount
	 * @author 		: Deddy
	 * @since		: Mar 15, 2013 9:48:58 AM
	 * @return_type : Integer
	 * @Description : modul untuk query jumlah category
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public Integer selectListCategoryPagingCount(String search) {
		return dbMapper.selectListCategoryPagingCount(search);
	}
	
	public Integer selectNamaCategoryCount(String search) {
		return dbMapper.selectNamaCategoryCount(search);
	}

	/**
	 *
	 * @Method_name	: saveCategory
	 * @author 		: Deddy
	 * @since		: Mar 15, 2013 10:44:38 AM
	 * @return_type : String
	 * @Description : modul proses Category
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public String saveCategory(Category category, User currentUser){
		logger.debug("saveCategory(Category category, User currentUser)");

		String pesan;

		if("NEW".equals(category.mode)){
			category.nomor=1;
			category.flagdet=0;
			category.active=1;
			category.createby = currentUser.id;
			category.createdate = dbMapper.selectSysdate();
			category.id=dbMapper.insertCategory(category);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Category",""+category.nama,"ditambah"},null);
		}else if("DELETE".equals(category.mode)){
			category.active=0;
			category.modifyby = currentUser.id;
			category.modifydate = dbMapper.selectSysdate();
			dbMapper.updateCategory(category);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Category",""+category.nama,"dihapus"},null);
		}else  if("EDIT".equals(category.mode)){
			category.modifyby = currentUser.id;
			category.modifydate = dbMapper.selectSysdate();
			dbMapper.updateCategory(category);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Category",""+category.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+category.mode);
		}

		return pesan;
	}

	public String saveOpname(Opname opname, User currentUser){
		String pesan;

		if("NEW".equals(opname.mode)){
			opname.cabang_id = currentUser.cabang_id;
			opname.tgl = dbMapper.selectSysdate();
			opname.posisi_id = 1;
			opname.createby = currentUser.id;
			opname.createdate = dbMapper.selectSysdate();
			dbMapper.insertOpname(opname);

			for(OpnameDet od:opname.lsOpnameDet){
				od.opname_id = opname.opname_id;
				dbMapper.insertOpnameDet(od);
			}
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Berita Acara Gudang"," ","disave"},null);
		}else  if("EDIT".equals(opname.mode)){
			opname.cabang_id = currentUser.cabang_id;
			dbMapper.updateOpname(opname);
			for(OpnameDet od:opname.lsOpnameDet){
				od.opname_id = opname.opname_id;
				dbMapper.updateOpnameDet(od);
			}
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Berita Acara Gudang"," ","disave"},null);
		}else if("DELETE".equals(opname.mode)){
			opname.cancel = 1;
			opname.cancelby = currentUser.id;
			opname.canceldate =  dbMapper.selectSysdate();
			opname.cabang_id = currentUser.cabang_id;
			dbMapper.updateOpname(opname);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Berita Acara Gudang"," ","dibatalkan"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+opname.mode);
		}

		return pesan;
	}

	/**
	 *
	 * @Method_name	: selectListSupplier
	 * @author 		: Deddy
	 * @since		: Mar 16, 2013 1:22:42 AM
	 * @return_type : List<Supplier>
	 * @Description : modul untuk query Supplier
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public List<Supplier> selectListSupplierAuto(String nama_auto,String nama,Integer id,String kode_auto,String kode) {
		return dbMapper.selectListSupplierAuto(nama_auto, nama, id, kode_auto, kode);
	}

	public List<Supplier> selectListSupplier(Integer id) {
		return dbMapper.selectListSupplier(id);
	}

	public List<Supplier> selectListSupplierPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListSupplierPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListSupplierPagingCount(String search) {
		return dbMapper.selectListSupplierPagingCount(search);
	}
	
	public Integer selectNamaSupplierCount(String search) {
		return dbMapper.selectNamaSupplierCount(search);
	}

	public String saveSupplier(Supplier supplier, User currentUser){
		logger.debug("saveSupplier(Supplier supplier, User currentUser)");

		String pesan = null;

		if("NEW".equals(supplier.mode)){
			supplier.active=1;
			supplier.createby = currentUser.id;
			supplier.createdate = dbMapper.selectSysdate();
			supplier.id=dbMapper.insertSupplier(supplier);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Supplier",""+supplier.nama,"ditambah"},null);
		}else if("DELETE".equals(supplier.mode)){
			supplier.active=0;
			supplier.modifyby = currentUser.id;
			supplier.modifydate = dbMapper.selectSysdate();
			//select ke data supplier existing, apabila ID supplier tersebut memiliki hutang, maka tidak bisa dihapus/dinonatifkan.
			if(selectListSupplier(supplier.id).get(0).hutang!=null){
				if(selectListSupplier(supplier.id).get(0).hutang>0){
					pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Supplier",""+supplier.nama,"tidak dapat dihapus karena Supplier masih ada hutang."},null);
				}
			}else{
				dbMapper.updateSupplier(supplier);
				pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Supplier",""+supplier.nama,"dihapus"},null);
			}
		}else  if("EDIT".equals(supplier.mode)){
			supplier.modifyby = currentUser.id;
			supplier.modifydate = dbMapper.selectSysdate();
			dbMapper.updateSupplier(supplier);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Supplier",""+supplier.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+supplier.mode);
		}

		return pesan;
	}

//	/**
//	 *
//	 * @Method_name	: selectListItem
//	 * @author 		: Deddy
//	 * @since		: Mar 21, 2013 10:48:42 AM
//	 * @return_type : List<Item>
//	 * @Description : modul untuk query Item
//	 * @Revision	:
//	 * #====#===========#===================#===========================#
//	 * | ID	|    Date	|	    User		|			Description		|
//	 * #====#===========#===================#===========================#
//	 * |	|			|					|							|
//	 * #====#===========#===================#===========================#
//	 */
//	public List<Item> selectListItem(Integer id) {
//		return dbMapper.selectListItem(id);
//	}

	public List<Item> selectListItemPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type, Integer cabang_id) {
		return dbMapper.selectListItemPaging(search, offset, rowcount, sort, sort_type,cabang_id);
	}


	/**
	 *
	 * @Method_name	: selectItemPagingCount
	 * @author 		: Deddy
	 * @since		: Mar 21, 2013 11:50:58 PM
	 * @return_type : Integer
	 * @Description : modul untuk query jumlah item
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public Integer selectItemPagingCount(String search) {
		return dbMapper.selectItemPagingCount(search);
	}
	
	public Integer selectNamaItemCount(String search) {
		return dbMapper.selectNamaItemCount(search);
	}

	/**
	 *
	 * @Method_name	: saveItem
	 * @author 		: Deddy
	 * @since		: Mar 22, 2013 00:14:38 AM
	 * @return_type : String
	 * @Description : modul proses Item
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public String saveItem(Item item, User currentUser){
		logger.debug("saveItem(Item item, User currentUser)");

		String pesan;

		if("NEW".equals(item.mode)){
			item.active=1;
			item.createby = currentUser.id;
			item.createdate = dbMapper.selectSysdate();
			item.id=dbMapper.insertItem(item);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Item",""+item.nama,"ditambah"},null);
		}else if("DELETE".equals(item.mode)){
			item.active=0;
			item.modifyby = currentUser.id;
			item.modifydate = dbMapper.selectSysdate();
			dbMapper.updateItem(item);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Item",""+item.nama,"dihapus"},null);
		}else  if("EDIT".equals(item.mode)){
			item.modifyby = currentUser.id;
			item.modifydate = dbMapper.selectSysdate();
			dbMapper.updateItem(item);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Item",""+item.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+item.mode);
		}

		return pesan;
	}

	public List<Trans> selectListTrans(Integer jenis,Integer posisi_id,Integer trans_id,String no_trans, Integer retail_id){
		return dbMapper.selectListTrans(jenis, posisi_id, trans_id, no_trans,retail_id);
	}
	public List<Trans> selectListTransPaging(String search,	Integer offset,Integer rowcount,String sort, String sort_type,Integer jenis,Integer posisi_id,Integer trans_id,String no_trans, Integer retail_id, Integer approval) {
		return dbMapper.selectListTransPaging(search, offset, rowcount, sort, sort_type, jenis, posisi_id, trans_id, no_trans,retail_id,approval);
	}
	public Integer selectListTransPagingCount(String search,Integer jenis,Integer posisi_id,Integer trans_id,String no_trans, Integer retail_id, Integer approval){
		return dbMapper.selectListTransPagingCount(search, jenis, posisi_id, trans_id, no_trans,retail_id,approval);
	}

	public List<TransDet> selectListTransDet(Integer trans_id,Integer urut,String barcode_ext,Integer item_id, List<Integer> lsitem_id) {
		return dbMapper.selectListTransDet(trans_id, urut, barcode_ext,item_id,lsitem_id);
	}

	public List<Stock> selectListStockItem(String barcode_ext, Integer cabang_id,Date periode) {
		return dbMapper.selectListStockItem(barcode_ext, cabang_id,periode);
	}
	
	public List<Payroll> selectListStockItemPaging(String search,Integer offset, Integer rowcount,String sort, String sort_type,String barcode_ext,
			Integer cabang_id,Date  periode){
		return dbMapper.selectListStockItemPaging(search, offset, rowcount, sort, sort_type, barcode_ext, cabang_id, periode);	
	}
	public Integer selectListStockItemPagingCount(String search, String barcode_ext,Integer cabang_id,Date  periode){
		return dbMapper.selectListStockItemPagingCount(search, barcode_ext, cabang_id, periode);
	}
	
	

	/**
	 *
	 * @Method_name	: saveStock
	 * @author 		: Bertho Rafitya Iwasurya
	 * @since		: Apr 17, 2013 7:43:50 PM
	 * @return_type : void
	 * @Description :
	 * 				  modul ini untuk update stock bila ada perubahan stock fisik
	 * 				  hpp akan otomatis di hitung
	 * @param no_trans: no transaksi yang mengupdate stock
	 * @param barcode_ext : no barcode item
	 * @param qty : jumlah qty yang diproses
	 * @param harga: harga per item
	 * @param dk : debet I kredit O
	 * @param cabang_id : kode cabang
	 * @param periode : periode stock
	 * @param jenisStock : 1 penjualan; 2 Pembelian; 3 Berita Acara Gudang
	 * @Revision	:
	 * #====#===========#===================#===========================#
	 * | ID	|    Date	|	    User		|			Description		|
	 * #====#===========#===================#===========================#
	 * |	|			|					|							|
	 * #====#===========#===================#===========================#
	 */
	public void saveStock(String no_trans,String barcode_ext,Integer qty, Double harga,Date periode,String dk,Integer cabang_id, Integer jenisStock){
		Date closingStockPeriode=selectClosingPeriode(1,cabang_id);
		//Stock stock=selectListStockItem(barcode_ext, cabang_id, periode).get(0);
		Stock stock=selectListStockItem(barcode_ext, cabang_id, closingStockPeriode).get(0);
		stock.stockHist=new StockHist(stock.item_id, periode, dk, cabang_id, qty, null, harga, stock.hpp, no_trans, stock.qty);
		// insert stock hist saat tarnsdfe stock berubah
		dbMapper.insertStockHist(stock.stockHist);

		

		if(jenisStock==1){//penjualan
			stock.keluar+=qty;
			stock.qty_order_jual-=qty;
		}else if(jenisStock==2){//pembelian'
			stock.masuk+=qty;
			stock.qty_order_beli-=qty;
		}

		stock.hpp=stock.stockHist.hpp_akhir;
		//penjualan
		dbMapper.updateStock(stock);
	}

	public String saveTrans(Trans trans, User currentUser){
		logger.debug("saveTrans(Trans trans, User currentUser)");

		String pesan = null;
		boolean custBaru=false;
		boolean suplBaru=false;

		if("NEW".equals(trans.mode)){
			trans.createby = currentUser.id;
			trans.createdate = dbMapper.selectSysdate();
			trans.flag_kirim=trans.flag_kirim==null?0:trans.flag_kirim;

			String keterangan="";
			if(trans.jenistrans.toLowerCase().equals("penjualan")){
				if(trans.pagename.toLowerCase().equals("order")){
					trans.jenis=2;
					trans.posisi_id=2;
					keterangan="Order Transaksi Penjualan";
				}else if(trans.pagename.toLowerCase().equals("input")){
					trans.jenis=4;
					trans.posisi_id=2;
					keterangan="Input Transaksi Penjualan";
				}else if(trans.pagename.toLowerCase().equals("retur")){
					trans.jenis=6;
					trans.posisi_id=1;
					keterangan="Retur Transaksi Penjualan";
				}else{
					throw new RuntimeException ("Page not found");
				}
				trans.dk="I";

				if(trans.customer_id==null){
					if(!Utils.isEmpty(trans.customer.nama)){
						trans.customer.active=1;
						trans.customer.createby=trans.createby;
						trans.customer.createdate=trans.createdate;
						dbMapper.insertCustomer(trans.customer);
						trans.customer_id=trans.customer.id;
						custBaru=true;
					}
				}
			}else if(trans.jenistrans.toLowerCase().equals("pembelian")){
				if(trans.pagename.toLowerCase().equals("order")){
					trans.jenis=1;
					trans.posisi_id=2;
					keterangan="Order Transaksi Pembelian";
				}else if(trans.pagename.toLowerCase().equals("input")){
					trans.jenis=3;
					trans.posisi_id=2;
					keterangan="Input Transaksi Pembelian";
				}else if(trans.pagename.toLowerCase().equals("retur")){
					trans.jenis=5;
					trans.posisi_id=1;
					keterangan="Retur Transaksi Pembelian";
				}else{
					throw new RuntimeException ("Page not found");
				}

				trans.dk="O";

				if(trans.supplier_id==null){
					if(!Utils.isEmpty(trans.supplier.nama)){
						trans.supplier.active=1;
						trans.supplier.createby=trans.createby;
						trans.supplier.createdate=trans.createdate;
						dbMapper.insertSupplier(trans.supplier);
						trans.supplier_id=trans.supplier.id;
						suplBaru=true;
					}
				}
			}else if(trans.jenistrans.toLowerCase().equals("transferstock")){
				trans.jenis=7;
				trans.posisi_id=1;
				trans.retail_id=currentUser.cabang_id;
				keterangan="Req Transfer Stock";
			}else{
				throw new RuntimeException ("Page not found");
			}

			dbMapper.insertTrans(trans);
			dbMapper.insertTransHist(new TransHist(trans.trans_id, trans.createdate, trans.posisi_id, keterangan, currentUser.id));

			trans.no_trans=dbMapper.selectListTrans(null, null, trans.trans_id, null,null).get(0).no_trans;
			int i=1;
			for(TransDet td:trans.lsTransDet){
				td.trans_id=trans.trans_id;
				td.dk=trans.dk;
				td.urut=i;
				
				i++;

				if(!trans.jenistrans.toLowerCase().equals("transferstock")){
					Date closingStockPeriode=selectClosingPeriode(1,currentUser.cabang_id);
					List<Stock> lsSTock=selectListStockItem(td.barcode_ext, currentUser.cabang_id, closingStockPeriode);
					Stock stock=new Stock();
					Double stock_jual=0.0;
					if(lsSTock.isEmpty()){// create stock klo em blom pernah ada stock
						/*TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Stock item"+td.barcode_ext+" belum ada, silahkan diinput terlebih dahulu."},null);
						return pesan;*/
						//stock jual sebelum berubah
						stock_jual=0.0;
						td.hpp=0.0;
						//
						// insert stock hist saat tarnsdfe stock berubah
						//dbMapper.insertStockHist(new StockHist(td.item_id, trans.createdate, trans.dk, currentUser.cabang_id, td.qty, stock.qty_order_jual, td.harga, null, trans.no_trans));

						if(trans.jenistrans.toLowerCase().equals("penjualan")){
							if(trans.pagename.toLowerCase().equals("retur")){
								if(trans.retur_type==1){//tuker barang
									
								}else if(trans.retur_type==2){//tuker uang
									
								}else if(trans.retur_type==3){//lebih kirim
									
								}
							}else{
								if(Utils.isEmpty(trans.no_trans_ref))
									stock.qty_order_jual=0+td.qty;//kalo penjualan qty order jual yang di tambah
								else stock.qty_order_jual=0;
								
								stock.qty_order_beli=0;
							}
						}else if(trans.jenistrans.toLowerCase().equals("pembelian")){
							if(trans.pagename.toLowerCase().equals("retur")){
								if(trans.retur_type==1){//tuker barang
									
								}else if(trans.retur_type==2){//tuker uang
									
								}else if(trans.retur_type==3){//lebih kirim
									
								}
							}else{
								if(Utils.isEmpty(trans.no_trans_ref))
									stock.qty_order_beli=0+td.qty;//kalo penjualan qty order beli yang di tambah
								else stock.qty_order_beli=0;
								
								stock.qty_order_jual=0;
							}
						}
						stock.cabang_id=currentUser.cabang_id;
						Item item=dbMapper.selectListItem(null, td.barcode_ext, null, null, null, null).get(0);
						stock.item_id=item.id;
						stock.saldo_awal=0;
						stock.masuk=0;
						stock.keluar=0;
						stock.hpp=0.0;
						stock.hpp_awal=0.0;
						
						if(closingStockPeriode==null)closingStockPeriode=Utils.convertStringToDate(Utils.convertDateToString(dbMapper.selectSysdate(), "01MMyyyy"), "ddMMyyyy");
						stock.periode=closingStockPeriode;//Utils.convertStringToDate(Utils.convertDateToString(dbMapper.selectSysdate(), "01MMyyyy"), "ddMMyyyy");
						dbMapper.insertStock(stock);
					}else if(!lsSTock.isEmpty()){
						stock=lsSTock.get(0);
						stock_jual=stock.stock_jual;//stock jual sebelum berubah
						
						td.hpp=stock.hpp;
						//
						// insert stock hist saat tarnsdfe stock berubah
						//dbMapper.insertStockHist(new StockHist(td.item_id, trans.createdate, trans.dk, currentUser.cabang_id, td.qty, stock.qty_order_jual, td.harga, null, trans.no_trans));

						if(trans.jenistrans.toLowerCase().equals("penjualan")){
							if(trans.pagename.toLowerCase().equals("retur")){
								if(trans.retur_type==1){//tuker barang
									
								}else if(trans.retur_type==2){//tuker uang
									
								}else if(trans.retur_type==3){//lebih kirim
									
								}
							}else{
								if(Utils.isEmpty(trans.no_trans_ref))
									stock.qty_order_jual=stock.qty_order_jual+td.qty;//kalo penjualan qty order jual yang di tambah
								else
									stock.qty_order_jual=stock.qty_order_jual;
							}
						}else if(trans.jenistrans.toLowerCase().equals("pembelian")){
							if(trans.pagename.toLowerCase().equals("retur")){
								if(trans.retur_type==1){//tuker barang
									
								}else if(trans.retur_type==2){//tuker uang
									
								}else if(trans.retur_type==3){//lebih kirim
									
								}
							}else{
								if(Utils.isEmpty(trans.no_trans_ref))
									stock.qty_order_beli=stock.qty_order_beli+td.qty;//kalo penjualan qty order beli yang di tambah
								else  stock.qty_order_beli=stock.qty_order_beli;
							}
						}
						dbMapper.updateStock(stock);
					}
					
					stock=selectListStockItem(td.barcode_ext, currentUser.cabang_id, closingStockPeriode).get(0);
//					stock=lsSTock.get(0);
					//cek dulu apakah stock saat in cukup

					if(trans.jenistrans.toLowerCase().equals("penjualan")&!trans.pagename.toLowerCase().equals("order")&&stock.stock_jual<0){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						trans.no_trans=null;

						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Stock habis. Stock "+td.item_idKet+" tersisa "+stock_jual+" QTY diminta "+td.qty},null);

						if(custBaru){
							trans.customer_id=null;
							trans.customer.id=null;
						}

						if(suplBaru){
							trans.supplier_id=null;
							trans.supplier.id=null;
						}
						return pesan;
					}

				}
				if(trans.pagename.toLowerCase().equals("order")){
					td.setQty_order(td.qty);
				}
				
				if(trans.pagename.toLowerCase().equals("input")&!Utils.isEmpty(trans.no_trans_ref)){
					Trans tmp=selectListTrans(trans.jenis-2, null, null, trans.no_trans_ref,null).get(0);
					TransDet tds=selectListTransDet(tmp.trans_id, null, null, td.item_id, null).get(0);
					tds.qty-=td.qty;
					dbMapper.updateTransDet(tds);
					
					if(tds.qty<=0){
						tmp.posisi_id=-1;
						dbMapper.updateTrans(tmp);
					}
				}
				
				dbMapper.insertTransDet(td);
			}


			pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"ditambah"},null);
			trans.mode="EDIT";
		}else if("DELETE".equals(trans.mode)){
			if(trans.jenistrans.toLowerCase().equals("transferstock")){

			}
			//TODO: perlu delete ga?
			pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"dihapus"},null);
		}else if("TRANSFER".equals(trans.mode)){
			Trans tmpTrans=dbMapper.selectListTrans(null, null, trans.trans_id, null,null).get(0);
			String trx="dari "+tmpTrans.posisi_idKet +" ke ";
			Date sysdate=dbMapper.selectSysdate();
			if(trans.jenis==2){//Penjualan
				if(trans.posisi_id==1){
//					if(trans.print_order_form==null){
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Form Order Penjualan"},null);
//						return pesan;
//					}else{
						trans.posisi_id=2;
						trx="";
						pesan="ga ada";
//					}
				}else if(trans.posisi_id==2){
					trx="";
					pesan="ga ada";
//					if(trans.print_order_form==null){
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Form Order Penjualan"},null);
//						return pesan;
//					}else{
						
						/*
						 * REQUEST PAK HIMMIA per 28 Sep 2013
						 * ditutup karena saat penjualan akan create transaksi lagi, jadi 1 order bisa lebih dari 1 transaksi penjualan
						 	trans.posisi_id=2;
							trans.jenis=4;
							trans.trans_date=sysdate;
							trx="Order Penjualan Ke Input Penjualan";*/
//					}
				}
			}else if(trans.jenis==4){//Penjualan
				if(trans.posisi_id==1){
//					if(trans.print_order_form==null){
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Form Order Penjualan"},null);
//						return pesan;
//					}else{
						trans.posisi_id=2;
						trx="";
						pesan="ga ada";
//					}
				}else	if(trans.posisi_id==2){//penjualan
					if(trans.customer_id!=null&trans.pay_mode==2){//cek limit hutang customer
						if(tmpTrans.limit_hutang!=0){
							Double hutang=dbMapper.selectHutangCustomer(trans.customer_id);
							if(tmpTrans.limit_hutang<hutang&&tmpTrans.approveby==null){//klo hutang lebih besar dari limit dan tidak ada  approval
								TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
								pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Hutang Customer melebih limit. Mohon approval terlebih dahulu. [Hutang : "+hutang+" , Limit : "+trans.limit_hutang+"]"},null);
								return pesan;
							}
						}
					}

					if(trans.print_trans_date==null){//klo DO & faktur blom di print ga boleh ke posisi selanjutnya
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Surat Delivery Order"},null);
						return pesan;
					}else if(trans.print_faktur_date==null){//klo kredit faktur blom di print ga boleh ke posisi selanjutnya
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu faktur"},null);
						return pesan;
//					}else if(trans.pay_mode==1){//klo cash maka masuk ke payment //ga perlu
//						trans.posisi_id=3;
//						trx+="Payment";
					}else{
						if(trans.print_trans_date==null){//klo DO blom di print ga boleh ke posisi selanjutnya
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Surat Delivery Order"},null);
							return pesan;
						}else if(trans.flag_kirim==1){//klo dikirim ke driver dulu
							trans.posisi_id=4;
							trx+="Driver";
						}else{
							trans.posisi_id=5;
							trx+="Gudang";
						}
						
						//Insert jurnal penjualan
						Trx trxJur=new Trx();
						trxJur.cash_flow_id=1;//pemasukkan
						trxJur.tgl_trx=sysdate;
						trxJur.tgl_rk=sysdate;
						trxJur.tgl_jurnal=sysdate;
						trxJur.createdate=sysdate;
						trxJur.createby=currentUser.id;
						trxJur.posisi_id=2;//finance
						//trxJur.no_voucher=??
						dbMapper.insertTrx(trxJur);
						
						Double hppTotal=0.0;
						for(TransDet td:trans.lsTransDet){
							hppTotal+=td.qty*td.hpp;
						}
						
						TrxDet trxdet=new TrxDet();
						trxdet.trx_id=trxJur.trx_id;
						trxdet.coa_id="6.01.00";
						trxdet.no_urut=1;
						trxdet.jumlah=hppTotal;
						trxdet.dk="D";
						trxdet.ket="HPP";
						dbMapper.insertTrxDet(trxdet);
						
						trxdet=new TrxDet();
						trxdet.trx_id=trxJur.trx_id;
						trxdet.coa_id="1.05.00";
						trxdet.no_urut=2;
						trxdet.jumlah=hppTotal;
						trxdet.dk="K";
						trxdet.ket="Persediaan Barang Dagangan";
						dbMapper.insertTrxDet(trxdet);
						
						trxdet=new TrxDet();
						trxdet.trx_id=trxJur.trx_id;
						trxdet.coa_id="1.04.00";
						trxdet.no_urut=3;
						trxdet.jumlah=trans.total_harga-trans.total_disc;
						trxdet.dk="D";
						trxdet.ket="Piutang Usaha";
						dbMapper.insertTrxDet(trxdet);
						
						trxdet=new TrxDet();
						trxdet.trx_id=trxJur.trx_id;
						trxdet.coa_id="4.00.01";
						trxdet.no_urut=4;
						trxdet.jumlah=trans.total_harga-trans.total_disc;
						trxdet.dk="K";
						trxdet.ket="Pendapatan Toko";
						dbMapper.insertTrxDet(trxdet);
						
					}
//				}else if(trans.posisi_id==3){//Payment
//					if(trans.print_trans_date==null){//klo DO blom di print ga boleh ke posisi selanjutnya
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Surat Delivery Order"},null);
//						return pesan;
//					}else if(trans.print_faktur_date==null){//klo faktur blom di print ga boleh ke posisi selanjutnya
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Faktur"},null);
//						return pesan;
//					}else if(trans.flag_kirim==1){//klo dikirim ke driver dulu
//						trans.posisi_id=4;
//						trx+="Driver";
//					}else{
//						trans.posisi_id=5;
//						trx+="Gudang";
//					}

				}else if(trans.posisi_id==4){//Driver
					trans.posisi_id=5;
					trx+="Gudang";
				}else if(trans.posisi_id==5){//Gudang
					if(trans.flag_kirim==1){
						if(trans.print_sj_date==null){//klo Surat Jalan blom di print ga boleh ke posisi selanjutnya
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Surat Jalan"},null);
							return pesan;
						}
						trans.posisi_id=6;
						trx+="Tanda Terima";
					}else{
						trans.posisi_id=99;
						trx+="Filling";
					}
					List<TransDet> lsTransDet=dbMapper.selectListTransDet(trans.trans_id, null, null, null, null);
					Date closingStockPeriode=selectClosingPeriode(1,currentUser.cabang_id);
					for(TransDet td: lsTransDet){
						saveStock(trans.no_trans, td.barcode_ext, td.qty, td.harga, closingStockPeriode, trans.dk, trans.retail_id, 1);
					}
				}else{
					throw new RuntimeException ("Position Transaction not found");
				}
				pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"ditransfer "+trx},null);
			}else if(trans.jenis==1){//Pembelian
				if(trans.posisi_id==1){
//					if(trans.print_trans_date==null){
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Purchasing Order"},null);
//						return pesan;
//					}else{
						trans.posisi_id=2;
						trx="";
						pesan="ga ada";
//					}
				}else if(trans.posisi_id==2){
//					if(trans.print_trans_date==null){
//						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Purchasing Order"},null);
//						return pesan;
//					}else{
						trx="";
						pesan="ga ada";
						/*trans.posisi_id=2;
						trans.jenis=3;
						trans.trans_date=sysdate;						
						trx="Order Pembelian Ke Input Pembelian";*/
//					}
				}
			}else if(trans.jenis==3){
				if(trans.posisi_id==2){//pembelian
					
					/*if(trans.print_trans_date==null){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Purchasing Order"},null);
						return pesan;
					}else */if(trans.print_faktur_date==null){//klo kredit faktur blom di print ga boleh ke posisi selanjutnya
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu faktur"},null);
						return pesan;
					}
//						if(trans.print_trans_date==null){//klo DO blom di print ga boleh ke posisi selanjutnya
//							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//							pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Purchasing Order"},null);
//							return pesan;
//						}else 
							trans.posisi_id=5;
							trx+="Gudang";
//						}
						
						//Insert jurnal penjualan
						Trx trxJur=new Trx();
						trxJur.cash_flow_id=2;//pengeluaran
						trxJur.tgl_trx=sysdate;
						trxJur.tgl_rk=sysdate;
						trxJur.tgl_jurnal=sysdate;
						trxJur.createdate=sysdate;
						trxJur.createby=currentUser.id;
						trxJur.posisi_id=2;//finance
						//trxJur.no_voucher=??
						dbMapper.insertTrx(trxJur);
						
						TrxDet trxdet=new TrxDet();
											
						trxdet=new TrxDet();
						trxdet.trx_id=trxJur.trx_id;
						trxdet.coa_id="1.05.00";
						trxdet.no_urut=1;
						trxdet.jumlah=trans.total_harga-trans.total_disc;
						trxdet.dk="D";
						trxdet.ket="Persediaan Barang Dagangan";
						dbMapper.insertTrxDet(trxdet);
						
						trxdet=new TrxDet();
						trxdet.trx_id=trxJur.trx_id;
						trxdet.coa_id="2.01.00";
						trxdet.no_urut=2;
						trxdet.jumlah=trans.total_harga-trans.total_disc;
						trxdet.dk="K";
						trxdet.ket="Hutang Dagang";
						dbMapper.insertTrxDet(trxdet);
					}
				pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"ditransfer "+trx},null);
			}else if(trans.jenis==5){
				if(trans.posisi_id==1){//retur pembelian
					
					if(trans.print_trans_date==null){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Purchasing Order"},null);
						return pesan;
					}else{
						if(trans.print_trans_date==null){//klo DO blom di print ga boleh ke posisi selanjutnya
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Purchasing Order"},null);
							return pesan;
						}else 
							trans.posisi_id=5;
							trx+="Gudang";
						}
					}
				pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"ditransfer "+trx},null);
			}else if(trans.jenis==6){
				if(trans.posisi_id==1){//retur Penjualan
					
					if(trans.print_trans_date==null){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Purchasing Order"},null);
						return pesan;
					}else{
						if(trans.print_trans_date==null){//klo DO blom di print ga boleh ke posisi selanjutnya
							TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
							pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Silahkan print terlebih dahulu Purchasing Order"},null);
							return pesan;
						}else 
							trans.posisi_id=5;
							trx+="Gudang";
						}
					}
				pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"ditransfer "+trx},null);
			}else if(trans.jenis==7){
				if(trans.posisi_id==1){
					trans.posisi_id=2;
					trans.tgl_req_trans=dbMapper.selectSysdate();
					trans.retail_id=currentUser.cabang_id;
				}else if(trans.posisi_id==2){
					trans.posisi_id=3;
					trans.tgl_gudang_trans=dbMapper.selectSysdate();
					trans.gudang_id=currentUser.id;
					Integer jumlahTransferStock=0;
					for(TransDet transDet: trans.lsTransDet){
						//tambahkan stok di cabang req
						Stock stock = new Stock();
						stock.item_id=transDet.item_id;
						stock.cabang_id=trans.retail_id_req;
						Stock stockReq= dbMapper.selectStock(stock.cabang_id, stock.item_id);
						stockReq.saldo_awal=stockReq.saldo_awal +transDet.qty;
						stockReq.masuk=stockReq.masuk+transDet.qty;
						dbMapper.updateStock(stockReq);
						//kurangi stok di cabang pengirim
						stock = new Stock();
						stock.item_id=transDet.item_id;
						stock.cabang_id=trans.retail_id;
						Stock stockRec= dbMapper.selectStock(stock.cabang_id, stock.item_id);
						stockRec.saldo_awal=stockRec.saldo_awal -transDet.qty;
						stockRec.keluar=stockRec.keluar+transDet.qty;
						dbMapper.updateStock(stockRec);
					}
				}else if(trans.posisi_id==3){
					trans.posisi_id=4;
					trans.tgl_terima_trans=dbMapper.selectSysdate();
				}
				pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"ditransfer "+trx},null);
			}

			dbMapper.updateTrans(trans);
			
			if(pesan==null)pesan="ga ada";
			
			if(pesan.equals("ga ada"))pesan="ga ada";
			else{
				pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"ditransfer "+trx},null);
			
				if(trans.jenis==7){
					pesan = messageSource.getMessage("submitsuccess", new String[]{"Proses Transfer Stock "," ","ditransfer"},null);
				}
			}
		}else  if("EDIT".equals(trans.mode)){
			Trans tmpTrans=selectListTrans(null, null, trans.trans_id, null,null).get(0);
			String keterangan="";
			boolean transfer=false;
			Date sysdate=dbMapper.selectSysdate();
			trans.createdate=sysdate;
			trans.createby=currentUser.id;

			if(trans.jenistrans.toLowerCase().equals("penjualan")){
				if(trans.pagename.toLowerCase().equals("order")){
					keterangan="Update Order Transaksi Penjualan";
				}else if(trans.pagename.toLowerCase().equals("input")){
					keterangan="Update Input Transaksi Penjualan";
					if(tmpTrans.posisi_id==1){//klo dari order ke transaksi maka update no po & no trans
						trans.no_po=trans.no_trans;
						trans.jenis=4;
						trans.posisi_id=2;
						keterangan="Input Transaksi Penjualan dari Order "+trans.no_po;
						transfer=true;
					}
				}else if(trans.pagename.toLowerCase().equals("retur")){
					keterangan="Update Retur Transaksi Penjualan";
				}else{
					throw new RuntimeException ("Page not found");
				}
				trans.dk="I";

				if(trans.customer_id==null){
					if(!Utils.isEmpty(trans.customer.nama)){
						trans.customer.active=1;
						trans.customer.createby=trans.createby;
						trans.customer.createdate=trans.createdate;
						dbMapper.insertCustomer(trans.customer);
						trans.customer_id=trans.customer.id;
						custBaru=true;
					}else if(tmpTrans.customer_id!=null){
						trans.customer_idNull=1;
					}
				}else{
					dbMapper.updateCustomer(trans.customer);
				}
				
				
				if(trans.sales_id==null){
					if(tmpTrans.sales_id!=null){
						trans.sales_idNull=1;
					}
				}
			}else if(trans.jenistrans.toLowerCase().equals("pembelian")){
				if(trans.pagename.toLowerCase().equals("order")){
					keterangan="Update Order Transaksi Pembelian";
				}else if(trans.pagename.toLowerCase().equals("input")){
					keterangan="Update Input Transaksi Pembelian";
					if(tmpTrans.posisi_id==1){//klo dari order ke transaksi maka update no po & no trans
						trans.no_po=trans.no_trans;
						trans.jenis=3;
						trans.posisi_id=2;
						keterangan="Input Transaksi Pembelian dari Order "+trans.no_po;
						transfer=true;
					}
				}else if(trans.pagename.toLowerCase().equals("retur")){
					keterangan="Update Retur Transaksi Pembelian";
				}else{
					throw new RuntimeException ("Page not found");
				}

				trans.dk="O";

				if(trans.supplier_id==null){
					if(!Utils.isEmpty(trans.supplier.nama)){
						trans.supplier.active=1;
						trans.supplier.createby=trans.createby;
						trans.supplier.createdate=trans.createdate;
						dbMapper.insertSupplier(trans.supplier);
						trans.supplier_id=trans.supplier.id;
						suplBaru=true;
					}else if(tmpTrans.supplier_id!=null){
						trans.supplier_idNull=1;
					}
				}else{
					dbMapper.updateSupplier(trans.supplier);
				}
			}else if(trans.jenistrans.toLowerCase().equals("transferstock")){
				keterangan="Update Transfer Stock";
				trans.dk="O";
			}else{
				throw new RuntimeException ("Page not found");
			}

			dbMapper.updateTrans(trans);
			dbMapper.insertTransHist(new TransHist(trans.trans_id, sysdate, trans.posisi_id, keterangan, currentUser.id));
			//tanya berto, penerapan posisi id di trans_hist jadi bikin masing2 ID ga untuk pembelian, penjualan yang dibuat.Kalau iya, utk transfer stock harus dibuat id di lst_config baru jg.

//			dibalik aja pakai list dari DB
			List<TransDet> tmpTransDet=new ArrayList<TransDet>();
			List<Integer> lsitem_id=new ArrayList<Integer>();
			for(TransDet td:trans.lsTransDet){
				List<TransDet> lsTransDet=selectListTransDet(trans.trans_id, null, null,td.item_id, null);
				TransDet tmp=new TransDet();

				if(!lsTransDet.isEmpty()){
					td.update="UPDATE";
					tmp=lsTransDet.get(0);
					td.qty_before=tmp.qty;
				}else {
					td.update="INSERT";
					td.qty_before=0;
				}


				if(transfer){//transfer dari order ke penjualan
					td.qty_order=tmp.qty;//copy qty order
				}

				lsitem_id.add(td.item_id);

				tmpTransDet.add(td);
			}

			trans.lsTransDet=tmpTransDet;

			//hapus transdet & update stock
			List<TransDet> lsTransDetHapus=dbMapper.selectListTransDet(trans.trans_id, null, null, null, lsitem_id);
			for(TransDet td:lsTransDetHapus){
				if(!trans.jenistrans.toLowerCase().equals("transferstock")){
					Date closingStockPeriode=selectClosingPeriode(1,currentUser.cabang_id);
					List<Stock> lstStock=selectListStockItem(td.barcode_ext, currentUser.cabang_id, closingStockPeriode);
					if(!lstStock.isEmpty()){
						Stock stock=lstStock.get(0);
	
						//klo periode stock dah berubah .. update qty order periode skrg di tambah aja ga perlu dikurang
						SimpleDateFormat sdf=new SimpleDateFormat("01/MM/yyyy");
						if(sdf.format(tmpTrans.createdate).compareTo(sdf.format(stock.periode))==0){//klo ga sama
							if(trans.jenistrans.toLowerCase().equals("penjualan")){
								stock.qty_order_jual=stock.qty_order_jual-td.qty;//kalo penjualan qty order jual yang di tambah
							}else if(trans.jenistrans.toLowerCase().equals("pembelian")){
								stock.qty_order_beli=stock.qty_order_beli-td.qty;//kalo penjualan qty order beli yang di tambah
							}
							dbMapper.updateStock(stock);
						}
					}

				}
				dbMapper.deleteTransDet(trans.trans_id,td.item_id);
			}


			for(TransDet td:trans.lsTransDet){
				td.trans_id=trans.trans_id;
				List<TransDet> lsTransDet=dbMapper.selectListTransDet(trans.trans_id, null, null,td.item_id,null);
				TransDet tmpTDet=new TransDet();

			//	Boolean baru=true;
	
				if(!trans.jenistrans.toLowerCase().equals("transferstock")){
					Date closingStockPeriode=selectClosingPeriode(1,currentUser.cabang_id);
					List<Stock> lsStock=selectListStockItem(td.barcode_ext, currentUser.cabang_id, closingStockPeriode);
					Stock stock=new Stock();
					Double stock_jual=0.0;
					if(selectListStockItem(td.barcode_ext, currentUser.cabang_id, null).isEmpty()){

						/*TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Stock item"+td.barcode_ext+" belum ada, silahkan diinput terlebih dahulu."},null);
						return pesan;*/
						//stock jual sebelum berubah
						 stock_jual=0.0;
						td.hpp=0.0;
						//
						// insert stock hist saat tarnsdfe stock berubah
						//dbMapper.insertStockHist(new StockHist(td.item_id, trans.createdate, trans.dk, currentUser.cabang_id, td.qty, stock.qty_order_jual, td.harga, null, trans.no_trans));

						if(trans.jenistrans.toLowerCase().equals("penjualan")){
							if(trans.pagename.toLowerCase().equals("retur")){
								if(trans.retur_type==1){//tuker barang
									
								}else if(trans.retur_type==2){//tuker uang
									
								}else if(trans.retur_type==3){//lebih kirim
									
								}
							}else{
								stock.qty_order_jual=0+td.qty;//kalo penjualan qty order jual yang di tambah
								stock.qty_order_beli=0;
							}
						}else if(trans.jenistrans.toLowerCase().equals("pembelian")){
							if(trans.pagename.toLowerCase().equals("retur")){
								if(trans.retur_type==1){//tuker barang
									
								}else if(trans.retur_type==2){//tuker uang
									
								}else if(trans.retur_type==3){//lebih kirim
									
								}
							}else{
								stock.qty_order_beli=0+td.qty;//kalo penjualan qty order beli yang di tambah
								stock.qty_order_jual=0;
							}
						}
						stock.cabang_id=currentUser.cabang_id;
						Item item=dbMapper.selectListItem(null, td.barcode_ext, null, null, null, null).get(0);
						stock.item_id=item.id;
						stock.saldo_awal=0;
						stock.masuk=0;
						stock.keluar=0;
						stock.hpp=0.0;
						stock.hpp_awal=0.0;
						
						if(closingStockPeriode==null)closingStockPeriode=Utils.convertStringToDate(Utils.convertDateToString(dbMapper.selectSysdate(), "01MMyyyy"), "ddMMyyyy");
						
						stock.periode=closingStockPeriode;
						dbMapper.insertStock(stock);
					
					}else if(!lsStock.isEmpty()){
						stock=lsStock.get(0);
						 stock_jual=stock.stock_jual;//stock jual sebelum berubah
						td.hpp=stock.hpp;
						//klo periode stock dah berubah .. update qty order periode skrg di tambah aja ga perlu dikurang
						SimpleDateFormat sdf=new SimpleDateFormat("01/MM/yyyy");
						if(sdf.format(tmpTrans.createdate).compareTo(sdf.format(stock.periode))!=0){//klo ga sama
							td.qty_before=0;
						}
	
						if(trans.jenistrans.toLowerCase().equals("penjualan")){
							stock.qty_order_jual=stock.qty_order_jual-td.qty_before+td.qty;//kalo penjualan qty order jual yang di tambah
						}else if(trans.jenistrans.toLowerCase().equals("pembelian")){
							stock.qty_order_beli=stock.qty_order_beli-td.qty_before+td.qty;//kalo penjualan qty order beli yang di tambah
						}
	
						dbMapper.updateStock(stock);
					}
					
					if(trans.pagename.toLowerCase().equals("order")){
						td.setQty_order(td.qty);
					}
					
					if(td.update.equals("UPDATE")){
						dbMapper.updateTransDet(td);
					} else {
						dbMapper.insertTransDet(td);
					}

//					stock=selectListStockItem(td.barcode_ext, currentUser.cabang_id, closingStockPeriode).get(0);
					stock=lsStock.get(0);
					//cek dulu apakah stock saat in cukup

					if(trans.jenistrans.toLowerCase().equals("penjualan")&!trans.pagename.toLowerCase().equals("order")&&stock.stock_jual<0){
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						trans.no_trans=null;

						pesan=messageSource.getMessage("submitfailed", new String[]{"Transaksi "+trans.jenistrans+" "+trans.pagename,""," Stock habis. Stock "+td.item_idKet+" tersisa "+stock_jual+" QTY diminta "+td.qty},null);

						if(custBaru){
							trans.customer_id=null;
							trans.customer.id=null;
						}

						if(suplBaru){
							trans.supplier_id=null;
							trans.supplier.id=null;
						}
						return pesan;
					}

				}
			}


			pesan = messageSource.getMessage("submitsuccess", new String[]{trans.pagename+" "+trans.jenistrans,""+trans.no_trans,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+trans.mode);
		}
		return pesan;
	}

	public List<Trans> selectListTransAuto(String no_trans_auto, String no_trans, Integer jenis, Integer posisi_id){
		return dbMapper.selectListTransAuto(no_trans_auto, no_trans, jenis, posisi_id);
	}

	public void updateTrans(Trans trans){
		dbMapper.updateTrans(trans);
	}

	public void insertTransHist(TransHist transHist){
		dbMapper.insertTransHist(transHist);
	}

	public List<Item> selectListItem(String barcode_ext_auto,String barcode_ext,Integer id,String nama,String nama_complete,Integer cabang_id){
		return dbMapper.selectListItem(barcode_ext_auto,barcode_ext, id, nama,nama_complete,cabang_id);
	}

	public List<Customer> selectListCustomerAuto(String nama_auto,String nama,Integer id,String kode_auto,String kode){
		return dbMapper.selectListCustomerAuto(nama_auto, nama, id, kode_auto, kode);
	}

	public List<Customer> selectListCustomer(Integer id) {
		return dbMapper.selectListCustomer(id);
	}

	public List<Customer> selectListCustomerPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListCustomerPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListCustomerPagingCount(String search) {
		return dbMapper.selectListCustomerPagingCount(search);
	}

	public String saveCustomer(Customer customer, User currentUser){
		logger.debug("saveCustomer(Customer customer, User currentUser)");

		String pesan = null;

		if("NEW".equals(customer.mode)){
			customer.active=1;
			customer.createby = currentUser.id;
			customer.createdate = dbMapper.selectSysdate();
			customer.id=dbMapper.insertCustomer(customer);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Customer",""+customer.nama,"ditambah"},null);
		}else if("DELETE".equals(customer.mode)){
			customer.active=0;
			customer.modifyby = currentUser.id;
			customer.modifydate = dbMapper.selectSysdate();
			dbMapper.updateCustomer(customer);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Customer",""+customer.nama,"dihapus"},null);
		}else  if("EDIT".equals(customer.mode)){
			customer.modifyby = currentUser.id;
			customer.modifydate = dbMapper.selectSysdate();
			dbMapper.updateCustomer(customer);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Customer",""+customer.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+customer.mode);
		}

		return pesan;
	}

	public List<Karyawan> selectListKaryawanAuto(String nama_auto,String nama,Integer id,String nik_auto,String nik,Integer jenis){
		return dbMapper.selectListKaryawanAuto(nama_auto, nama, id, nik_auto, nik, jenis);
	}

	public List<Karyawan> selectListKaryawan(Integer id,Integer jenis) {
		return dbMapper.selectListKaryawan(id, jenis);
	}

	public List<Karyawan> selectListKaryawanPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type,Integer jenis){
		return dbMapper.selectListKaryawanPaging(search, offset, rowcount, sort, sort_type, jenis);
	}

	public Integer selectListKaryawanPagingCount(String search,Integer jenis) {
		return dbMapper.selectListKaryawanPagingCount(search, jenis);
	}
	
	public Integer selectNamaKaryawanCount(String search) {
		return dbMapper.selectNamaKaryawanCount(search);
	}

	public String saveKaryawan(Karyawan karyawan, User currentUser){
		logger.debug("saveKaryawan(Karyawan karyawan, User currentUser)");

		String pesan = null;

		if("NEW".equals(karyawan.mode)){
			karyawan.active=1;
			karyawan.createby = currentUser.id;
			karyawan.createdate = dbMapper.selectSysdate();
			karyawan.id=dbMapper.insertKaryawan(karyawan);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Karyawan",""+karyawan.nama,"ditambah"},null);
		}else if("DELETE".equals(karyawan.mode)){
			karyawan.active=0;
			karyawan.modifyby = currentUser.id;
			karyawan.modifydate = dbMapper.selectSysdate();
			dbMapper.updateKaryawan(karyawan);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Karyawan",""+karyawan.nama,"dihapus"},null);
		}else  if("EDIT".equals(karyawan.mode)){
			karyawan.modifyby = currentUser.id;
			karyawan.modifydate = dbMapper.selectSysdate();
			dbMapper.updateKaryawan(karyawan);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Karyawan",""+karyawan.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+karyawan.mode);
		}

		return pesan;
	}

	public Integer selectOpnamePagingCount(String search, Integer cabang_id) {
		return dbMapper.selectOpnamePagingCount(search, cabang_id);
	}

	public List<Item> selectListOpnamePaging( String search, Integer offset, Integer rowcount, String sort, String sort_type, Integer cabang_id) {
		return dbMapper.selectListOpnamePaging(search, offset, rowcount, sort, sort_type, cabang_id);
	}

	public List<Opname> selectListOpname (String where){
		return dbMapper.selectListOpname(where);
	}

	public List<OpnameDet> selectListOpnameDet (Integer opname_id){
		return dbMapper.selectListOpnameDet(opname_id);
	}

	public List<OpnameDet> selectListOpnameDetFromStock(Integer cabang_id){
		return dbMapper.selectListOpnameDetFromStock(cabang_id);
	}

	public List<Merk> selectListMerk(Integer id) {
		return dbMapper.selectListMerk(id);
	}

	public List<Merk> selectListMerkPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListMerkPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListMerkPagingCount(String search) {
		return dbMapper.selectListMerkPagingCount(search);
	}
	
	public Integer selectNamaMerkCount(String search) {
		return dbMapper.selectNamaMerkCount(search);
	}

	public Integer selectListMerkNamaCount(String search) {
		return dbMapper.selectListMerkNamaCount(search);
	}

	public String saveMerk(Merk merk, User currentUser){
		logger.debug("saveMerk(Merk merk, User currentUser)");

		String pesan;

		if("NEW".equals(merk.mode)){
			merk.active=1;
			merk.createby = currentUser.id;
			merk.createdate = dbMapper.selectSysdate();
			merk.id=dbMapper.insertMerk(merk);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Merk",""+merk.nama,"ditambah"},null);
		}else if("DELETE".equals(merk.mode)){
			merk.active=0;
			merk.modifyby = currentUser.id;
			merk.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMerk(merk);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Merk",""+merk.nama,"dihapus"},null);
		}else  if("EDIT".equals(merk.mode)){
			merk.modifyby = currentUser.id;
			merk.modifydate = dbMapper.selectSysdate();
			dbMapper.updateMerk(merk);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Merk",""+merk.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+merk.mode);
		}

		return pesan;
	}

	public List<Satuan> selectListSatuan(Integer id) {
		return dbMapper.selectListSatuan(id);
	}

	public List<Satuan> selectListSatuanPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListSatuanPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListSatuanPagingCount(String search) {
		return dbMapper.selectListSatuanPagingCount(search);
	}
	
	public Integer selectNamaSatuanCount(String search) {
		return dbMapper.selectNamaSatuanCount(search);
	}

	public String saveSatuan(Satuan satuan, User currentUser){
		logger.debug("saveSatuan(Satuan satuan, User currentUser)");

		String pesan;

		if("NEW".equals(satuan.mode)){
			satuan.active=1;
			satuan.createby = currentUser.id;
			satuan.createdate = dbMapper.selectSysdate();
			satuan.id=dbMapper.insertSatuan(satuan);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Satuan",""+satuan.nama,"ditambah"},null);
		}else if("DELETE".equals(satuan.mode)){
			satuan.active=0;
			satuan.modifyby = currentUser.id;
			satuan.modifydate = dbMapper.selectSysdate();
			dbMapper.updateSatuan(satuan);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Satuan",""+satuan.nama,"dihapus"},null);
		}else  if("EDIT".equals(satuan.mode)){
			satuan.modifyby = currentUser.id;
			satuan.modifydate = dbMapper.selectSysdate();
			dbMapper.updateSatuan(satuan);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Satuan",""+satuan.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+satuan.mode);
		}

		return pesan;
	}

	public List<Warna> selectListWarna(Integer id) {
		return dbMapper.selectListWarna(id);
	}

	public List<Warna> selectListWarnaPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListWarnaPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListWarnaPagingCount(String search) {
		return dbMapper.selectListWarnaPagingCount(search);
	}
	
	public Integer selectNamaWarnaCount(String search) {
		return dbMapper.selectNamaWarnaCount(search);
	}

	public String saveWarna(Warna warna, User currentUser){
		logger.debug("saveWarna(Warna warna, User currentUser)");

		String pesan;

		if("NEW".equals(warna.mode)){
			warna.active=1;
			warna.createby = currentUser.id;
			warna.createdate = dbMapper.selectSysdate();
			warna.id=dbMapper.insertWarna(warna);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Warna",""+warna.nama,"ditambah"},null);
		}else if("DELETE".equals(warna.mode)){
			warna.active=0;
			warna.modifyby = currentUser.id;
			warna.modifydate = dbMapper.selectSysdate();
			dbMapper.updateWarna(warna);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Warna",""+warna.nama,"dihapus"},null);
		}else  if("EDIT".equals(warna.mode)){
			warna.modifyby = currentUser.id;
			warna.modifydate = dbMapper.selectSysdate();
			dbMapper.updateWarna(warna);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Warna",""+warna.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+warna.mode);
		}

		return pesan;
	}

	public List<Config> selectListConfig(Integer id, Integer jenis) {
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("id", id);
		map.put("jenis", jenis);
		return dbMapper.selectListConfig(map);
	}

	public List<Config> selectListConfigPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListConfigPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListConfigPagingCount(String search) {
		return dbMapper.selectListConfigPagingCount(search);
	}

	public String saveConfig(Config config, User currentUser){
		logger.debug("saveConfig(Config config, User currentUser)");

		String pesan;

		if("NEW".equals(config.mode)){
			config.active=1;
			config.createby = currentUser.id;
			config.createdate = dbMapper.selectSysdate();
			config.id=dbMapper.insertConfig(config);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Config",""+config.keterangan,"ditambah"},null);
		}else if("DELETE".equals(config.mode)){
			config.active=0;
			config.modifyby = currentUser.id;
			config.modifydate = dbMapper.selectSysdate();
			dbMapper.updateConfig(config);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Config",""+config.keterangan,"dihapus"},null);
		}else  if("EDIT".equals(config.mode)){
			config.modifyby = currentUser.id;
			config.modifydate = dbMapper.selectSysdate();
			dbMapper.updateConfig(config);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Config",""+config.keterangan,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+config.mode);
		}

		return pesan;
	}

	public List<Cabang> selectListCabang(Integer id) {
		return dbMapper.selectListCabang(id);
	}

	public List<Cabang> selectListCabangPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListCabangPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListCabangPagingCount(String search) {
		return dbMapper.selectListCabangPagingCount(search);
	}
	
	public Integer selectNamaCabangCount(String search) {
		return dbMapper.selectNamaCabangCount(search);
	}

	public String saveCabang(Cabang cabang, User currentUser){
		logger.debug("saveCabang(Cabang cabang, User currentUser)");

		String pesan;

		if("NEW".equals(cabang.mode)){
			cabang.active=1;
			cabang.createby = currentUser.id;
			cabang.createdate = dbMapper.selectSysdate();
			cabang.id=dbMapper.insertCabang(cabang);
			Date periode=Utils.convertStringToDate(Utils.convertDateToString(dbMapper.selectSysdate(), "01MMyyyy"), "ddMMyyyy");
			dbMapper.insertClosingPeriode(new ClosingPeriode(1, cabang.id, periode));
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master cabang",""+cabang.nama,"ditambah"},null);
		}else if("DELETE".equals(cabang.mode)){
			cabang.active=0;
			cabang.modifyby = currentUser.id;
			cabang.modifydate = dbMapper.selectSysdate();
			dbMapper.updateCabang(cabang);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master cabang",""+cabang.nama,"dihapus"},null);
		}else  if("EDIT".equals(cabang.mode)){
			cabang.modifyby = currentUser.id;
			cabang.modifydate = dbMapper.selectSysdate();
			dbMapper.updateCabang(cabang);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master cabang",""+cabang.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+cabang.mode);
		}

		return pesan;
	}

	public List<Bank> selectListBank(Integer id) {
		return dbMapper.selectListBank(id);
	}

	public List<Bank> selectListBankPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListBankPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListBankPagingCount(String search) {
		return dbMapper.selectListBankPagingCount(search);
	}
	
	public Integer selectNamaBankCount(String search) {
		return dbMapper.selectNamaBankCount(search);
	}

	public String saveBank(Bank bank, User currentUser){
		logger.debug("saveBank(Bank bank, User currentUser)");

		String pesan;

		if("NEW".equals(bank.mode)){
			bank.active=1;
			bank.createby = currentUser.id;
			bank.createdate = dbMapper.selectSysdate();
			bank.id=dbMapper.insertBank(bank);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Bank",""+bank.nama,"ditambah"},null);
		}else if("DELETE".equals(bank.mode)){
			bank.active=0;
			bank.modifyby = currentUser.id;
			bank.modifydate = dbMapper.selectSysdate();
			dbMapper.updateBank(bank);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Bank",""+bank.nama,"dihapus"},null);
		}else  if("EDIT".equals(bank.mode)){
			bank.modifyby = currentUser.id;
			bank.modifydate = dbMapper.selectSysdate();
			dbMapper.updateBank(bank);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Bank",""+bank.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+bank.mode);
		}

		return pesan;
	}

	public List<Account> selectListAccount(Integer id) {
		return dbMapper.selectListAccount(id);
	}

	public String selectAccountName(Integer id){
		return dbMapper.selectAccountName(id);
	}

	public List<Account> selectListAccountPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListAccountPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListAccountPagingCount(String search) {
		return dbMapper.selectListAccountPagingCount(search);
	}
	
	public Integer selectNamaAccountCount(String search) {
		return dbMapper.selectNamaAccountCount(search);
	}

	public String saveAccount(Account account, User currentUser){
		logger.debug("saveAccount(Account account, User currentUser)");

		String pesan;

		if("NEW".equals(account.mode)){
			account.active=1;
			account.createby = currentUser.id;
			account.createdate = dbMapper.selectSysdate();
			account.saldo=0.00;
			account.id=dbMapper.insertAccount(account);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Account",""+account.no_rek,"ditambah"},null);
		}else if("DELETE".equals(account.mode)){
			account.active=0;
			account.modifyby = currentUser.id;
			account.modifydate = dbMapper.selectSysdate();
			dbMapper.updateAccount(account);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Account",""+account.no_rek,"dihapus"},null);
		}else  if("EDIT".equals(account.mode)){
			account.modifyby = currentUser.id;
			account.modifydate = dbMapper.selectSysdate();
			dbMapper.updateAccount(account);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master Account",""+account.no_rek,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+account.mode);
		}

		return pesan;
	}

	public void updateStock(Stock stock){
		dbMapper.updateStock(stock);
	}

	public void updateOpname(Opname opname){
		dbMapper.updateOpname(opname);
	}

	public void updateOpnameDet(OpnameDet opnameDet){
		dbMapper.updateOpnameDet(opnameDet);
	}

	public Date selectMaxDateFromTable(String field_name, String table_name, String where){
		return dbMapper.selectMaxDateFromTable(field_name, table_name, where);
	}
	
	public Date selectClosingPeriode(Integer id,Integer cabang_id){
		return dbMapper.selectClosingPeriode(id, cabang_id);
	}

	public String prosesTransferOpname(Opname opname, Integer opname_id, User currentUser){
		//update posisi_id jadi 4 (filling)
		opname.posisi_id=4;
		opname.opname_id= opname_id;
		opname.cabang_id= currentUser.cabang_id;
		for(OpnameDet od:opname.lsOpnameDet){
			Stock stock = new Stock();
			stock.item_id = od.item_id;
			stock.cabang_id = currentUser.cabang_id;
			stock.periode = selectMaxDateFromTable("periode", "mst_stock","item_id="+od.item_id+" and cabang_id="+currentUser.cabang_id);
			stock = dbMapper.selectStock(stock.cabang_id, stock.item_id);
			//apabila qty_fisik>qty maka insert ke masuk di opname_det
			Integer qty = 0;
			String dk="";
			if(od.qty_fisik>od.qty){
				qty=od.qty_fisik-od.qty;
				stock.masuk+=qty;
				dk="K";
			//apabila qty_fisik<qty maka insert ke keluar di opname_det
			}else if(od.qty_fisik<od.qty){
				qty=od.qty-od.qty_fisik;
				stock.keluar+=qty;
				dk="D";
			}
			if(stock!=null){
				updateStock(stock);
			}
			if(!dk.equals("")){
//				stock.stockHist=new StockHist(stock.item_id, stock.periode, dk, stock.cabang_id, qty, null, null, stock.hpp, opname.no_trans, stock.qty);
//				dbMapper.insertStockHist(stock.stockHist);
			}
			
		}
		updateOpname(opname);
		String pesan = messageSource.getMessage("submitsuccess", new String[]{"Berita Acara Gudang "," ","ditransfer"},null);
		return pesan;
	}
	
	public List<Delivery> selectListDelivery(Integer id,Integer posisi_id,String kode){
		return dbMapper.selectListDelivery(id, posisi_id,kode);
	}
	
	public List<Delivery> selectListDeliveryPaging(String search,Integer offset, Integer rowcount,String sort,String sort_type,  Integer id,Integer posisi_id) {
		return dbMapper.selectListDeliveryPaging(search, offset, rowcount, sort, sort_type, id, posisi_id);
	}
	
	public Integer selectListDeliveryPagingCount(String search,Integer id,Integer posisi_id) {
		return dbMapper.selectListDeliveryPagingCount(search,id, posisi_id);
	}
	
	public List<DeliveryDet> selectListDeliveryDet(Integer delivery_id,Integer trans_id){
		return dbMapper.selectListDeliveryDet(delivery_id, trans_id);
	}
	
	public void updateDelivery(Delivery delivery){
		dbMapper.updateDelivery(delivery);
	}
	
	public String saveDelivery(Delivery delivery, User currentUser){
		logger.debug("saveDelivery(Delivery delivery, User currentUser)");

		String pesan="";

		if("NEW".equals(delivery.mode)){
			
			delivery.createby = currentUser.id;
			delivery.createdate = dbMapper.selectSysdate();	
			delivery.posisi_id=1;

			dbMapper.insertDelivery(delivery);
			
			for(DeliveryDet dd:delivery.lsDeliveryDets){
				dd.status=1;
				dd.delivery_id=delivery.id;
				dbMapper.insertDeliveryDet(dd);
				
				Trans trans=new Trans();
				trans.trans_id=dd.trans_id;
				trans.driver_id=delivery.driver_id;
				trans.delivery_id=delivery.id;
				trans.tgl_kirim=delivery.createdate;
				dbMapper.updateTrans(trans);
			}
			
			delivery.kode=dbMapper.selectListDelivery(delivery.id,null,null).get(0).kode;
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Delivery",""+delivery.kode,"ditambah"},null);
		}else if("DELETE".equals(delivery.mode)){
			
		}else  if("TRANSFER".equals(delivery.mode)){
			Delivery tmp=dbMapper.selectListDelivery(delivery.id,null,null).get(0);
			
			if(tmp.posisi_id==1){
				if(delivery.tgl_print_sj==null){//klo Surat Jalan blom di print ga boleh ke posisi selanjutnya
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					pesan=messageSource.getMessage("submitfailed", new String[]{"Transfer Delivery",""," Silahkan print terlebih dahulu Surat Jalan"},null);
					return pesan;
				}
				delivery.posisi_id=2;
				dbMapper.updateDelivery(delivery);	
				
				delivery.lsDeliveryDets=dbMapper.selectListDeliveryDet(tmp.id, null);
				
				for(DeliveryDet dd:delivery.lsDeliveryDets){
					dd.status=2;//on-process
					dbMapper.updateDeliveryDet(dd);
					
					Trans trans=new Trans();
					trans.trans_id=dd.trans_id;
					trans.posisi_id=5;
					dbMapper.updateTrans(trans);
				}
				
			
				
				delivery.kode=dbMapper.selectListDelivery(delivery.id,null,null).get(0).kode;
				pesan = messageSource.getMessage("submitsuccess", new String[]{"Delivery",""+delivery.kode,"diubah"},null);
			}
		}else  if("EDIT".equals(delivery.mode)){
			dbMapper.updateDelivery(delivery);
			
			List<DeliveryDet> lsdd=dbMapper.selectListDeliveryDet(delivery.id, null);
			for(DeliveryDet dd:lsdd){
				Trans trans=new Trans();
				trans.trans_id=dd.trans_id;
				trans.driver_idNull="null";
				trans.delivery_idNull="null";
				trans.tgl_kirimNull="null";
				trans.status_kirimNull="null";
				dbMapper.updateTrans(trans);
			}
			
			dbMapper.deleteDeliveryDet(delivery.id, null);
			
			for(DeliveryDet dd:delivery.lsDeliveryDets){
				dd.status=1;
				dd.delivery_id=delivery.id;
				dbMapper.insertDeliveryDet(dd);
				
				Trans trans=new Trans();
				trans.trans_id=dd.trans_id;
				trans.driver_id=delivery.driver_id;
				trans.delivery_id=delivery.id;
				trans.tgl_kirim=delivery.createdate;
				trans.status_kirim=1;
				dbMapper.updateTrans(trans);
			}
			
			delivery.kode=dbMapper.selectListDelivery(delivery.id,null,null).get(0).kode;
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Delivery",""+delivery.kode,"diubah"},null);
			
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+delivery.mode);
		}

		return pesan;
	}
	
	public String saveGudang(Gudang gudang, User currentUser){
		logger.debug("saveGudang(Gudang gudang, User currentUser)");

		String pesan="";

		if("VIEW".equals(gudang.mode)){

			Trans tmpTrans=dbMapper.selectListTrans(null, null, null, gudang.trans.no_trans,null).get(0);
			String trx=tmpTrans.posisi_idKet +" ke ";
			if(tmpTrans.jenis==4){//Penjualan				
				 if(tmpTrans.posisi_id==5){//Gudang
					
					if(tmpTrans.flag_kirim==1){
						gudang.trans.posisi_id=6;
						gudang.trans.status_kirim=4;
						trx+="Tanda Terima";
						Delivery d=dbMapper.selectListDelivery(tmpTrans.delivery_id, null, null).get(0);
						d.posisi_id=3;
						dbMapper.updateDelivery(d);
						DeliveryDet dd=dbMapper.selectListDeliveryDet(tmpTrans.delivery_id, tmpTrans.trans_id).get(0);
						dd.status=2;
						dbMapper.updateDeliveryDet(dd);
					}else{
						gudang.trans.posisi_id=99;
						trx+="filling";
					}
					
					List<TransDet> lsTransDet=dbMapper.selectListTransDet(tmpTrans.trans_id, null, null, null, null);
					for(TransDet td: lsTransDet){
						saveStock(tmpTrans.no_trans, td.barcode_ext, td.qty, td.harga, tmpTrans.trans_date,tmpTrans.dk, tmpTrans.retail_id, 1);
					}
				}else{
					throw new RuntimeException ("Position Transaction not found");
				}
			}else if(gudang.trans.jenis==3){//Pembelian
				//Penjualan				
				 if(tmpTrans.posisi_id==5){//Gudang
										
					gudang.trans.posisi_id=99;
					trx+="filling";
										
					List<TransDet> lsTransDet=dbMapper.selectListTransDet(tmpTrans.trans_id, null, null, null, null);
					for(TransDet td: lsTransDet){
						saveStock(tmpTrans.no_trans, td.barcode_ext, td.qty, td.harga, tmpTrans.trans_date,tmpTrans.dk, tmpTrans.retail_id, 2);
					}
				}else{
					throw new RuntimeException ("Position Transaction not found");
				}
			
			}

			dbMapper.updateTrans(gudang.trans);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Gudang "+gudang.jenistrans,""+gudang.trans.no_trans,"ditransfer dari "+trx},null);
		
			
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+gudang.mode);
		}

		return pesan;
	}
	
	public List<Payroll> selectListPayroll(Integer id,Integer posisi_id,Date periode,String nik) {
		return dbMapper.selectListPayroll(id, posisi_id, periode, nik);
	}
	public List<Payroll> selectListPayrollPaging(String search,Integer offset,Integer rowcount,String sort,String sort_type, Date periode) {
		return dbMapper.selectListPayrollPaging(search, offset, rowcount, sort, sort_type, periode);
	}
	public Integer selectListPayrollPagingCount(String search,  Date periode) {
		return dbMapper.selectListPayrollPagingCount(search, periode);
	}
	
	public String savePayroll(Payroll payroll, User currentUser){
		logger.debug("savePayroll(Payroll payroll, User currentUser)");

		String pesan="";
		Date sysdate=dbMapper.selectSysdate();
		if("NEW".equals(payroll.mode)){
			payroll.tgl_input=sysdate;
			dbMapper.insertPayroll(payroll);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Payroll","","ditambahkan"},null);
		}else if("EDIT".equals(payroll.mode)){
			dbMapper.updatePayroll(payroll);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Payroll","","diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+payroll.mode);
		}

		return pesan;
	}
	
	public Double selectHutangCustomer(Integer customer_id){
		return dbMapper.selectHutangCustomer(customer_id);
	}
	
	public String saveClosingStock(Date periode, User currentUser){
		logger.debug("saveClosingStock(Date periode, User currentUser)");

		String pesan="";
		Date sysdate=dbMapper.selectSysdate();
		if (periode==null) { periode=sysdate;}
		List<Stock> lsStock=dbMapper.selectListStockItem(null, currentUser.cabang_id, periode);
		
		Date nextPeriode=Utils.add(periode, Calendar.MONTH, 1);
		
		for(Stock st:lsStock){
			Stock stock=new Stock();
			stock.item_id=st.item_id;
			stock.cabang_id=currentUser.cabang_id;
			stock.periode=nextPeriode;//dari table closing +1
			stock.saldo_awal=st.qty;
			stock.masuk=0;
			stock.keluar=0;
			stock.qty_order_jual=st.qty_order_jual;
			stock.qty_order_beli=st.qty_order_beli;
			stock.hpp=st.hpp;
			stock.hpp_awal=st.hpp;
			dbMapper.insertStock(stock);
		}
		
		dbMapper.updateClosingPeriod(new ClosingPeriode(1, currentUser.cabang_id, nextPeriode));
		
		pesan = messageSource.getMessage("submitsuccess", new String[]{"Closing Stock","Cabang "+currentUser.namaCabang+" Periode "+Utils.convertDateToString(periode, "MMMMM yyyy"),"diproses"},null);
		
		return pesan;
	}
	
	public String saveClosingAccounting(Date periode, User currentUser){
		logger.debug("saveClosingAccounting(Date periode, User currentUser)");

		String pesan="";
		
		Date sysdate=dbMapper.selectSysdate();
		if (periode==null) { periode=sysdate;}
		Date nextPeriode=Utils.add(periode, Calendar.MONTH, 1);
		
		List<COA> lsCoa=dbMapper.selectListCoa(null, 1, 1, null, null, null);
		
		for(COA coa:lsCoa){
			RepTB repTB=new RepTB();
			repTB.coa_id=coa.id;
			repTB.periode=nextPeriode;//table closing +1
			//FIXME:saldo dari mana ya?
			repTB.total_debet=0.0;
			repTB.total_kredit=0.0;
			repTB.saldo=dbMapper.selectSumTrx("D", periode)-dbMapper.selectSumTrx("K", periode);
			repTB.createby=currentUser.id;
			repTB.createdate=sysdate;
			repTB.last_update=sysdate;
			dbMapper.insertRepTB(repTB);
		}
		
		dbMapper.updateClosingPeriod(new ClosingPeriode(2,null, nextPeriode));
		
		pesan = messageSource.getMessage("submitsuccess", new String[]{"Closing Accounting","Cabang "+currentUser.namaCabang+" Periode "+Utils.convertDateToString(periode, "MMMMM yyyy"),"diproses"},null);
		
		return pesan;
	}
	
	public String saveProcessPayroll(Date periode, User currentUser){
		logger.debug("saveProcessPayroll(Date periode, User currentUser)");

		String pesan="";
		
		Date sysdate=dbMapper.selectSysdate();
		if (periode==null) { periode=sysdate;}
		Date nextPeriode=Utils.add(periode, Calendar.MONTH, 1);
		
		List<Karyawan> lsKaryawan=dbMapper.selectListKaryawan(null, 1);
		
		for(Karyawan k:lsKaryawan){
			Payroll payroll=new Payroll();
			payroll.karyawan_id=k.id;
			payroll.gapok=k.gaji;
			payroll.uang_makan=k.makan;
			payroll.uang_transport=k.transport;
			payroll.periode=Utils.convertStringToDate(Utils. convertDateToString(periode, "01/MM/yyyy"),"dd/MM/yyyy");
			dbMapper.insertPayroll(payroll);
		}
		
		dbMapper.updateClosingPeriod(new ClosingPeriode(3,null, nextPeriode));
		
		pesan = messageSource.getMessage("submitsuccess", new String[]{"Process Payroll","Cabang "+currentUser.namaCabang+" Periode "+Utils.convertDateToString(periode, "MMMMM yyyy"),"diproses"},null);
		
		return pesan;
	}
	
	
	public String saveTandaTerima(Gudang gudang, User currentUser){
		logger.debug("saveTandaTerima(Gudang gudang, User currentUser)");

		String pesan="";

		if("VIEW".equals(gudang.mode)){

			Trans tmpTrans=dbMapper.selectListTrans(null, null, null, gudang.trans.no_trans,null).get(0);
			String trx=tmpTrans.posisi_idKet +" ke ";
			if(tmpTrans.jenis==4){//Penjualan				
				 if(tmpTrans.posisi_id==6){//Tanda Terima
					
					if(tmpTrans.flag_kirim==1){
						if(gudang.trans.status_kirim==3){
							gudang.trans.posisi_id=99;
							trx+="Filling";
							
							
							List<DeliveryDet> lde=dbMapper.selectListDeliveryDet(tmpTrans.delivery_id, null);
							boolean udah=true;
							for(DeliveryDet de:lde){
								if(!de.status.equals(3)&&de.trans_id!=tmpTrans.trans_id){
									udah=false;
									break;
								}
							}
							
							if(udah){
								Delivery delivery=new Delivery();
								delivery.id=tmpTrans.delivery_id;
								delivery.posisi_id=99;
								dbMapper.updateDelivery(delivery);
							}
							
							DeliveryDet dd=dbMapper.selectListDeliveryDet(tmpTrans.delivery_id, tmpTrans.trans_id).get(0);
							dd.status=3;
							dbMapper.updateDeliveryDet(dd);
						}else{
							gudang.trans.posisi_id=6;
							trx+="Tanda Terima";
							Delivery d=dbMapper.selectListDelivery(tmpTrans.delivery_id, null, null).get(0);
							d.posisi_id=4;
							dbMapper.updateDelivery(d);
							DeliveryDet dd=dbMapper.selectListDeliveryDet(tmpTrans.delivery_id, tmpTrans.trans_id).get(0);
							dd.status=gudang.trans.status_kirim;
							dbMapper.updateDeliveryDet(dd);
						}
						
						
					}else{
						throw new RuntimeException ("Transaksi tidak dikirim");
					}
					
					
				}else{
					throw new RuntimeException ("Position Transaction not found");
				}
			}

			dbMapper.updateTrans(gudang.trans);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Tanda Terima "+gudang.jenistrans,""+gudang.trans.no_trans,"diproses"},null);
		
			
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+gudang.mode);
		}

		return pesan;
	}
	
	public List<Payment> selectListPayment(Integer payment_id, String no_payment, Integer trans_id){
		return dbMapper.selectListPayment(payment_id, no_payment, trans_id);
	}
	public List<Trans> selectListPaymentPaging(String search,	Integer offset,Integer rowcount,String sort, String sort_type,Integer jenis,Integer posisi_id,Integer payment_id,String no_payment) {
		return dbMapper.selectListPaymentPaging(search, offset, rowcount, sort, sort_type, jenis, posisi_id, payment_id, no_payment);
	}
	public Integer selectListPaymentPagingCount(String search,Integer jenis,Integer posisi_id,Integer payment_id,String no_payment){
		return dbMapper.selectListPaymentPagingCount(search, jenis, posisi_id, payment_id, no_payment);
	}
	
	public String savePayment(Payment payment,User currentUser){
		logger.debug("savePayment(Payment payment,User currentUser)");
		String pesan = null;
		if("NEW".equals(payment.mode)){
			payment.createby=currentUser.id;
			payment.paid_date=dbMapper.selectSysdate();
			dbMapper.insertPayment(payment);
			dbMapper.insertTransHist(new TransHist(payment.trans_id, payment.paid_date, selectListTrans(null, null, null, payment.no_trans,null).get(0).posisi_id, "Input Transaksi " + payment.pagename , currentUser.id));
			pesan = messageSource.getMessage("submitsuccess", new String[]{payment.pagename,""+payment.no_payment,"ditambah"},null);
		}else  if("EDIT".equals(payment.mode)){
			payment.createby=currentUser.id;
			payment.paid_date=dbMapper.selectSysdate();
			dbMapper.updatePayment(payment);
			pesan = messageSource.getMessage("submitsuccess", new String[]{payment.pagename,""+payment.no_payment,"diubah"},null);
		}else if("TRANSFER".equals(payment.mode)){
			Trans trans = selectListTrans(null, null, payment.trans_id, null,null).get(0);
			
			Trx trx= new Trx();
			trx.createby=currentUser.id;
			trx.createdate=dbMapper.selectSysdate();
			trx.tgl_trx=dbMapper.selectSysdate();
			trx.tgl_rk=payment.getPaid_date();
			trx.account_id=payment.account_id;
			trx.posisi_id=2;
			dbMapper.insertTrx(trx);
			for(int i=0;i<2;i++){
				TrxDet trxDet = new TrxDet();
				trxDet.no_urut=i+1;
				trxDet.trx_id=trx.trx_id;
				if(payment.dk.equals("D")){
					if(i==0){
						trxDet.ket="Hutang Dagang";
						trxDet.dk="D";
						trxDet.coa_id="2.01.00";
					}else if(i==1){
						
						trxDet.dk="K";
						if(payment.cara_bayar==1){
							trxDet.ket="Kas";
							trxDet.coa_id="1.01.00";
						}else{
							trxDet.ket="Bank";
							List<Account> account = dbMapper.selectListAccount(payment.account_id);
							trxDet.coa_id=account.get(0).coa_id;
						}
					}
				}else if(payment.dk.equals("K")){
					if(i==0){
						trxDet.dk="D";
						if(payment.cara_bayar==1){
							trxDet.ket="Kas";
							trxDet.coa_id="1.01.00";
						}else{
							trxDet.ket="Bank";
							List<Account> account = dbMapper.selectListAccount(payment.account_id);
							trxDet.coa_id=account.get(0).coa_id;
						}
					}else if(i==1){
						trxDet.ket="Piutang Usaha";
						trxDet.dk="K";
						trxDet.coa_id="1.04.00";
					}
				}
				trxDet.jumlah=payment.nominal;
				dbMapper.insertTrxDet(trxDet);
			}
			payment.trx_id=trx.trx_id;
			dbMapper.updatePayment(payment);
			Integer paid = 0;
			trans.remain = (trans.remain==0 && trans.paid==0?trans.total_harga:trans.remain) - payment.nominal;
			if(trans.remain<=0){
				paid=1;
			}
			trans.paid=paid;
			dbMapper.updateTrans(trans);
			dbMapper.insertTransHist(new TransHist(payment.trans_id, trx.createdate, selectListTrans(null, null, null, payment.no_trans,null).get(0).posisi_id, "Transfer Transaksi " + payment.pagename , currentUser.id));
			
			pesan = messageSource.getMessage("submitsuccess", new String[]{payment.pagename,""+payment.no_payment,"ditransfer"},null);
		}else if("DELETE".equals(payment.mode)){
			pesan = messageSource.getMessage("submitsuccess", new String[]{payment.pagename,""+payment.no_payment,"dihapus"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+payment.mode);
		}
		return pesan;
	}
	
	public List<COA> selectListCoaAuto( String coa_id_auto, String coa_id, String nama_auto, String nama){
		return dbMapper.selectListCoaAuto(coa_id_auto, coa_id, nama_auto, nama);
	}
	
	public List<Trx> selectListTrx(Integer trx_id, Integer posisi_id){
		return dbMapper.selectListTrx(trx_id, posisi_id);
	}
	
	public List<TrxDet> selectListTrxDet(Integer trx_id){
		return dbMapper.selectListTrxDet(trx_id);
	}
	
	public List<Trx> selectListTrxPaging(String search,	Integer offset,Integer rowcount,String sort, String sort_type,Integer posisi_id, Integer trx_id) {
		return dbMapper.selectListTrxPaging(search, offset, rowcount, sort, sort_type, posisi_id, trx_id);
	}
	
	public Integer selectListTrxPagingCount(String search,Integer posisi_id, Integer trx_id){
		return dbMapper.selectListTrxPagingCount(search, posisi_id, trx_id);
	}
	
	public String saveTrx(Trx trx,User currentUser){
		logger.debug("savePayment(Payment payment,User currentUser)");
		String pesan = null;
		if("NEW".equals(trx.mode)){
			trx.createby=currentUser.id;
			trx.createdate=dbMapper.selectSysdate();
			trx.tgl_trx=dbMapper.selectSysdate();
			trx.posisi_id=1;
			dbMapper.insertTrx(trx);
			for(TrxDet trxDet: trx.lsTrxDet){
				trxDet.no_urut=trxDet.urut;
				trxDet.trx_id=trx.trx_id;
				if(trxDet.jumlahDebet>0){
					trxDet.dk="D";
					trxDet.jumlah=trxDet.jumlahDebet;
				}else if(trxDet.jumlahKredit>0){
					trxDet.dk="K";
					trxDet.jumlah=trxDet.jumlahKredit;
				}
				dbMapper.insertTrxDet(trxDet);
			}
			pesan = messageSource.getMessage("submitsuccess", new String[]{"No Transaksi",""+trx.no_trx,"ditambah"},null);
		}else  if("EDIT".equals(trx.mode)){
			trx.createby=currentUser.id;
			trx.createdate=dbMapper.selectSysdate();
			trx.tgl_trx=dbMapper.selectSysdate();
			dbMapper.updateTrx(trx);
			dbMapper.deleteTrxDet(trx.trx_id);
			for(TrxDet trxDet: trx.lsTrxDet){
				trxDet.no_urut=trxDet.urut;
				trxDet.trx_id=trx.trx_id;
				if(trxDet.jumlahDebet>0){
					trxDet.dk="D";
					trxDet.jumlah=trxDet.jumlahDebet;
				}else if(trxDet.jumlahKredit>0){
					trxDet.dk="K";
					trxDet.jumlah=trxDet.jumlahKredit;
				}
				dbMapper.insertTrxDet(trxDet);
			}
			pesan = messageSource.getMessage("submitsuccess", new String[]{"No Transaksi",""+trx.no_trx,"diubah"},null);
		}else if("TRANSFER".equals(trx.mode)){
			if(trx.posisi_id==1){
				trx.posisi_id=2;
				dbMapper.updateTrx(trx);
			}else if(trx.posisi_id==2){
				trx.posisi_id=9;
				trx.tgl_jurnal=dbMapper.selectSysdate();
				dbMapper.updateTrx(trx);
			}
			pesan = messageSource.getMessage("submitsuccess", new String[]{"No Transaksi",""+trx.no_trx,"ditransfer"},null);
		}else if("DELETE".equals(trx.mode)){
			pesan = messageSource.getMessage("submitsuccess", new String[]{"No Transaksi",""+trx.no_trx,"dihapus"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+trx.mode);
		}
			
		return pesan;
	}

	public List<COA> selectListCOA(String id) {
		return dbMapper.selectListCOA(id);
	}

	public List<COA> selectListCOAPaging( String search, Integer offset, Integer rowcount, String sort, String sort_type){
		return dbMapper.selectListCOAPaging(search, offset, rowcount, sort, sort_type);
	}

	public Integer selectListCOAPagingCount(String search) {
		return dbMapper.selectListCOAPagingCount(search);
	}
	
	public Integer selectNamaCOACount(String search) {
		return dbMapper.selectNamaCOACount(search);
	}

	public String saveCOA(COA coa, User currentUser){
		logger.debug("saveCOA(COA coa, User currentUser)");

		String pesan;

		if("NEW".equals(coa.mode)){
			coa.active=1;
			coa.createby = currentUser.id;
			coa.createdate = dbMapper.selectSysdate();
			dbMapper.insertCOA(coa);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master COA",""+coa.nama,"ditambah"},null);
		}else if("DELETE".equals(coa.mode)){
			coa.active=0;
			dbMapper.updateCOA(coa);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master COA",""+coa.nama,"dihapus"},null);
		}else  if("EDIT".equals(coa.mode)){
			dbMapper.updateCOA(coa);
			pesan = messageSource.getMessage("submitsuccess", new String[]{"Master COA",""+coa.nama,"diubah"},null);
		}else{
			throw new RuntimeException ("WARNING !! Metode Save tidak ditemukan untuk Mode "+coa.mode);
		}

		return pesan;
	}
	
	
	public String saveTransApproval(Trans trans, User currentUser){
		logger.debug("saveTransApproval(Trans trans, User currentUser)");

		String pesan;

		dbMapper.updateTrans(trans);
		pesan=messageSource.getMessage("submitsuccess", new String[]{"Approval Trans No",""+trans.no_trans,"diubah"},null);

		return pesan;
	}
	
	/**
	 * Cek apakah transaksi masih di periode closing date
	 * @param type : 1=closing stock; 2=closing accounting 
	 * @param currentUser
	 * @return true : periode sama
	 */
	public boolean checkTglClossing(Integer type,User currentUser){
		Date closingStockPeriode=selectClosingPeriode(type,currentUser.cabang_id);
		Date sysdate=selectSysdate();
		return Utils.convertDateToString(closingStockPeriode, "MMyyyy").equals(Utils.convertDateToString(sysdate, "MMyyyy"));
	}

		
}