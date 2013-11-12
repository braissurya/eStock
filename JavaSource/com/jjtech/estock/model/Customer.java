package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 31, 2013 10:19:43 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Customer implements Serializable {

	private static final long serialVersionUID = 1522705444273874466L;

	public Integer id;

	@Size(max=10)
	public String kode;

	@Size(max=60)
	public String nama;

	@Size(max=200)
	public String alamat;

	@Size(max=60)
	public String kota;

	@Size(max=60)
	public String contact;

	@Size(max=50)
	public String no_telp;

	@Size(max=30)
	public String no_hp;
	
	public String email;
	
	public String no_fax;

	public Double limit_hutang;

	public Double total_hutang;

	public Date due_date;

	public Integer pkp;

	public Integer flag_ecer;

	public Integer pay_mode;

	public Integer active;

	public Integer createby;

	public Date createdate;

	public Integer modifyby;

	public Date modifydate;

	//tambahan
	public String mode;
	public String createuser;
	public Boolean akses;
	public Double totalHutang;
	public Boolean outoflimit;
	public String pay_modeKet;

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

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
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

	public String getNo_fax() {
		return no_fax;
	}

	public void setNo_fax(String no_fax) {
		this.no_fax = no_fax;
	}

	public Double getLimit_hutang() {
		return limit_hutang;
	}

	public void setLimit_hutang(Double limit_hutang) {
		this.limit_hutang = limit_hutang;
	}

	public Double getTotal_hutang() {
		return total_hutang;
	}

	public void setTotal_hutang(Double total_hutang) {
		this.total_hutang = total_hutang;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

	public Integer getPkp() {
		return pkp;
	}

	public void setPkp(Integer pkp) {
		this.pkp = pkp;
	}

	public Integer getFlag_ecer() {
		return flag_ecer;
	}

	public void setFlag_ecer(Integer flag_ecer) {
		this.flag_ecer = flag_ecer;
	}

	public Integer getPay_mode() {
		return pay_mode;
	}

	public void setPay_mode(Integer pay_mode) {
		this.pay_mode = pay_mode;
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

	public Double getTotalHutang() {
		return totalHutang;
	}

	public void setTotalHutang(Double totalHutang) {
		this.totalHutang = totalHutang;
	}

	public Boolean getOutoflimit() {
		return outoflimit;
	}

	public void setOutoflimit(Boolean outoflimit) {
		this.outoflimit = outoflimit;
	}

	public String getPay_modeKet() {
		return pay_modeKet;
	}

	public void setPay_modeKet(String pay_modeKet) {
		this.pay_modeKet = pay_modeKet;
	}   
	
	
}