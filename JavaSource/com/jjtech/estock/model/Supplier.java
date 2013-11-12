package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

public class Supplier implements Serializable{
private static final long serialVersionUID = -2788111663192652650L;
	
	
	public Integer id;
	
	@Size(max=10)
	public String kode;

	@Size(max=50)
	public String nama;

	@Size(max=100)
	public String alamat;

	@Size(max=60)
	public String kota;

	@Size(max=30)
	public String contact;

	@Size(max=50)
	public String no_telp;

	@Size(max=30)
	public String no_hp;

	@Size(max=60)
	@Email
	public String email;

	public Integer tenor;

	@Size(max=30)
	public String no_fax;

	public Double hutang;

	public Date last_order;

	public Integer pkp;

	public Integer active;

	public Date createdate;
	
	public Integer createby;

	public Integer modifyby;

	public Date modifydate;

	//tambahan
	public String mode;
	public String createuser;
	public Boolean akses;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getKode() {
		return kode;
	}

	public void setKode(String kode) {
		this.kode = kode;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getKota() {
		return kota;
	}

	public void setKota(String kota) {
		this.kota = kota;
	}

	public String getNo_telp() {
		return no_telp;
	}

	public void setNo_telp(String no_telp) {
		this.no_telp = no_telp;
	}

	public String getNo_hp() {
		return no_hp;
	}

	public void setNo_hp(String no_hp) {
		this.no_hp = no_hp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getTenor() {
		return tenor;
	}

	public void setTenor(Integer tenor) {
		this.tenor = tenor;
	}

	public String getNo_fax() {
		return no_fax;
	}

	public void setNo_fax(String no_fax) {
		this.no_fax = no_fax;
	}

	public Double getHutang() {
		return hutang;
	}

	public void setHutang(Double hutang) {
		this.hutang = hutang;
	}

	public Date getLast_order() {
		return last_order;
	}

	public void setLast_order(Date last_order) {
		this.last_order = last_order;
	}

	public Integer getPkp() {
		return pkp;
	}

	public void setPkp(Integer pkp) {
		this.pkp = pkp;
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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}
	
	public String getMode() {
		return mode;
	}
	
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	public String getCreateuser() {
		return createuser;
	}
	
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	
	public Boolean getAkses() {
		return akses;
	}
	
	public void setAkses(Boolean akses) {
		this.akses = akses;
	}    

}
