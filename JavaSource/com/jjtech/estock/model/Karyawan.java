package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 31, 2013 10:51:18 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Karyawan implements Serializable {

	private static final long serialVersionUID = 8670200031538372706L;

	public Integer id;
	
	public Integer jenis;

	@Size(max=10)
	public String nik;

	@Size(max=80)
	public String nama;

	public Date tgl_masuk;

	public Date tgl_keluar;

	public Double gaji;

	public Double makan;

	public Double transport;

	public Integer active;

	public Integer createby;

	public Date createdate;

	public Integer modifyby;

	public Date modifydate;

	//tambahan
	public String mode;
	public String createuser;
	public Boolean akses;
	public String jeniskry;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Date getTgl_masuk() {
		return tgl_masuk;
	}

	public void setTgl_masuk(Date tgl_masuk) {
		this.tgl_masuk = tgl_masuk;
	}

	public Date getTgl_keluar() {
		return tgl_keluar;
	}

	public void setTgl_keluar(Date tgl_keluar) {
		this.tgl_keluar = tgl_keluar;
	}

	public Double getGaji() {
		return gaji;
	}

	public void setGaji(Double gaji) {
		this.gaji = gaji;
	}

	public Double getMakan() {
		return makan;
	}

	public void setMakan(Double makan) {
		this.makan = makan;
	}

	public Double getTransport() {
		return transport;
	}

	public void setTransport(Double transport) {
		this.transport = transport;
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
	
	public String getJeniskry() {
		return jeniskry;
	}
	
	public void setJeniskry(String jeniskry) {
		this.jeniskry = jeniskry;
	}

}
