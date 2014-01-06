package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 4, 2013 9:55:00 AM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Account implements Serializable {

	private static final long serialVersionUID = -9077658817970953030L;

	public Integer id;

	public Integer id_bank;
	
	public String coa_id;

	public String cabang;

    public String no_rek;

    public Integer kurs;

    public String atas_nama;

    public Double saldo;

    public Integer active;

    public Integer createby;

    public Date createdate;

    public Integer modifyby;

    public Date modifydate;

	//tambahan
	public String mode;
	public String createuser;
	public Boolean akses;
	public String namabank;
	public String namakurs;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getId_bank() {
		return id_bank;
	}
	public void setId_bank(Integer id_bank) {
		this.id_bank = id_bank;
	}
	public String getCoa_id() {
		return coa_id;
	}
	public void setCoa_id(String coa_id) {
		this.coa_id = coa_id;
	}
	public String getCabang() {
		return cabang;
	}
	public void setCabang(String cabang) {
		this.cabang = cabang;
	}
	public String getNo_rek() {
		return no_rek;
	}
	public void setNo_rek(String no_rek) {
		this.no_rek = no_rek;
	}
	public Integer getKurs() {
		return kurs;
	}
	public void setKurs(Integer kurs) {
		this.kurs = kurs;
	}
	public String getAtas_nama() {
		return atas_nama;
	}
	public void setAtas_nama(String atas_nama) {
		this.atas_nama = atas_nama;
	}
	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
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
	public String getNamabank() {
		return namabank;
	}
	public void setNamabank(String namabank) {
		this.namabank = namabank;
	}      
	public String getNamakurs() {
		return namakurs;
	}
	public void setNamakurs(String namakurs) {
		this.namakurs = namakurs;
	}    

}
