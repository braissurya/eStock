package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 21, 2013 10:26:20 AM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class User implements Serializable {

	private static final long serialVersionUID = -2788111663192652650L;
	
	
	 public Integer id;

	 public Integer cabang_id;

	 @Size(max=20)
	 public String username;

	 @Size(max=65535)
	 public String password;

	 @NotNull
	 public Integer group_user_id;

	 public Date lastlogin;

	 public Integer active;

	 public Integer createby;

	 public Date createdate;

	 public Integer modifyby;

	 public Date modifydate;

	 public String nama;
	 public Date dob;
	 
	 @Email
	 public String email;
	 public String nik;

	 public Integer flag_approval;
	 
	 public Integer flag_akses_all;
    
    ///tambahan untuk table
    public String passwordDecrypt;
    public Date loginTime;
    public String namaGroup;
    public String namaCabang;
    
    //tambahan untuk bantuan aplikasi
    public String menuUser;
    public String siteMap;
    public List<Menu> listMenu;
    public String mode;
    
    public String newPassword;
    public String confirmPassword;
    
    
    public Trans trans;
    public Double totalHutang;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCabang_id() {
		return cabang_id;
	}

	public void setCabang_id(Integer cabang_id) {
		this.cabang_id = cabang_id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getGroup_user_id() {
		return group_user_id;
	}

	public void setGroup_user_id(Integer group_user_id) {
		this.group_user_id = group_user_id;
	}

	public Date getLastlogin() {
		return lastlogin;
	}

	public void setLastlogin(Date lastlogin) {
		this.lastlogin = lastlogin;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Integer getCreateby() {
		return createby;
	}

	public void setCreateby(Integer createby) {
		this.createby = createby;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public Integer getModifyby() {
		return modifyby;
	}

	public void setModifyby(Integer modifyby) {
		this.modifyby = modifyby;
	}

	public Date getModifydate() {
		return modifydate;
	}

	public void setModifydate(Date modifydate) {
		this.modifydate = modifydate;
	}

	public String getPasswordDecrypt() {
		return passwordDecrypt;
	}

	public void setPasswordDecrypt(String passwordDecrypt) {
		this.passwordDecrypt = passwordDecrypt;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public String getMenuUser() {
		return menuUser;
	}

	public void setMenuUser(String menuUser) {
		this.menuUser = menuUser;
	}

	public List<Menu> getListMenu() {
		return listMenu;
	}

	public void setListMenu(List<Menu> listMenu) {
		this.listMenu = listMenu;
	}

	public String getSiteMap() {
		return siteMap;
	}

	public void setSiteMap(String siteMap) {
		this.siteMap = siteMap;
	}

	public String getNamaGroup() {
		return namaGroup;
	}

	public void setNamaGroup(String namaGroup) {
		this.namaGroup = namaGroup;
	}

	public String getNamaCabang() {
		return namaCabang;
	}

	public void setNamaCabang(String namaCabang) {
		this.namaCabang = namaCabang;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}



	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public Integer getFlag_approval() {
		return flag_approval;
	}

	public void setFlag_approval(Integer flag_approval) {
		this.flag_approval = flag_approval;
	}

	public Trans getTrans() {
		return trans;
	}

	public void setTrans(Trans trans) {
		this.trans = trans;
	}

	public Double getTotalHutang() {
		return totalHutang;
	}

	public void setTotalHutang(Double totalHutang) {
		this.totalHutang = totalHutang;
	}

	public Integer getFlag_akses_all() {
		return flag_akses_all;
	}

	public void setFlag_akses_all(Integer flag_akses_all) {
		this.flag_akses_all = flag_akses_all;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
    
	
	
    
}
