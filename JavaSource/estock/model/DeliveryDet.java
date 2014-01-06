package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class DeliveryDet implements Serializable {

	private static final long serialVersionUID = 161010059558998973L;

	@NotNull
    public Integer delivery_id;

    @NotNull
    public Integer trans_id;

    public Integer status;

    public Date tgl_kirim;

    public Date tgl_terima;

    @Size(max=100)
    public String nama_penerima;

    public Date tgl_kembali;

    @Size(max=200)
    public String keterangan;
    
    //tambahan
    public String statusKet;
    public String contact_tujuan;
    public String alamat_tujuan;
    public String telp_tujuan;
    public String no_trans;
    public Date tgl_kirim_est;

	public Integer getDelivery_id() {
		return delivery_id;
	}

	public void setDelivery_id(Integer delivery_id) {
		this.delivery_id = delivery_id;
	}

	public Integer getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(Integer trans_id) {
		this.trans_id = trans_id;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getTgl_kirim() {
		return tgl_kirim;
	}

	public void setTgl_kirim(Date tgl_kirim) {
		this.tgl_kirim = tgl_kirim;
	}

	public Date getTgl_terima() {
		return tgl_terima;
	}

	public void setTgl_terima(Date tgl_terima) {
		this.tgl_terima = tgl_terima;
	}

	public String getNama_penerima() {
		return nama_penerima;
	}

	public void setNama_penerima(String nama_penerima) {
		this.nama_penerima = nama_penerima;
	}

	public Date getTgl_kembali() {
		return tgl_kembali;
	}

	public void setTgl_kembali(Date tgl_kembali) {
		this.tgl_kembali = tgl_kembali;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public String getStatusKet() {
		return statusKet;
	}

	public void setStatusKet(String statusKet) {
		this.statusKet = statusKet;
	}

	public String getContact_tujuan() {
		return contact_tujuan;
	}

	public void setContact_tujuan(String contact_tujuan) {
		this.contact_tujuan = contact_tujuan;
	}

	public String getAlamat_tujuan() {
		return alamat_tujuan;
	}

	public void setAlamat_tujuan(String alamat_tujuan) {
		this.alamat_tujuan = alamat_tujuan;
	}

	public String getTelp_tujuan() {
		return telp_tujuan;
	}

	public void setTelp_tujuan(String telp_tujuan) {
		this.telp_tujuan = telp_tujuan;
	}

	public String getNo_trans() {
		return no_trans;
	}

	public void setNo_trans(String no_trans) {
		this.no_trans = no_trans;
	}

	public Date getTgl_kirim_est() {
		return tgl_kirim_est;
	}

	public void setTgl_kirim_est(Date tgl_kirim_est) {
		this.tgl_kirim_est = tgl_kirim_est;
	}
    
	
	
    

}
