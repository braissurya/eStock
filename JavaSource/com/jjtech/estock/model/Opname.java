package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

public class Opname implements Serializable {
	private static final long serialVersionUID = -4855359164955043025L;
	
	public Integer opname_id;
	
	public Integer cabang_id;
	public Date tgl;
	
	public String no_trans;
	public Integer posisi_id;
	public Integer approveby;
	public Date approvedate;
	public Integer createby;
	public Date createdate;
	public Integer cancel;
	public Integer cancelby;
	public Date canceldate;
	public String keterangan;
	public String nama_cabang;
	public Integer jenis;
	public String mode;
	
	//tambahan
	public OpnameDet opnameDet = new OpnameDet();
	public List<OpnameDet> lsOpnameDet = new ArrayList<OpnameDet>();		
	public Stock stock = new Stock();
	public List<Stock> lsStock = new ArrayList<Stock>();
	public String posisi;
	
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getNama_cabang() {
		return nama_cabang;
	}
	public void setNama_cabang(String nama_cabang) {
		this.nama_cabang = nama_cabang;
	}
	
	public String getPosisi() {
		return posisi;
	}
	public void setPosisi(String posisi) {
		this.posisi = posisi;
	}
	public Stock getStock() {
		return stock;
	}
	public void setStock(Stock stock) {
		this.stock = stock;
	}
	public List<Stock> getLsStock() {
		return lsStock;
	}
	public void setLsStock(List<Stock> lsStock) {
		this.lsStock = lsStock;
	}
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	
	public OpnameDet getOpnameDet() {
		return opnameDet;
	}
	public void setOpnameDet(OpnameDet opnameDet) {
		this.opnameDet = opnameDet;
	}
	
	public Integer getOpname_id() {
		return opname_id;
	}
	public void setOpname_id(Integer opname_id) {
		this.opname_id = opname_id;
	}
	public Integer getCabang_id() {
		return cabang_id;
	}
	public void setCabang_id(Integer cabang_id) {
		this.cabang_id = cabang_id;
	}
	public Date getTgl() {
		return tgl;
	}
	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}
	public String getNo_trans() {
		return no_trans;
	}
	public void setNo_trans(String no_trans) {
		this.no_trans = no_trans;
	}
	public Integer getPosisi_id() {
		return posisi_id;
	}
	public void setPosisi_id(Integer posisi_id) {
		this.posisi_id = posisi_id;
	}
	public Integer getApproveby() {
		return approveby;
	}
	public void setApproveby(Integer approveby) {
		this.approveby = approveby;
	}
	public Date getApprovedate() {
		return approvedate;
	}
	public void setApprovedate(Date approvedate) {
		this.approvedate = approvedate;
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
	public Integer getCancel() {
		return cancel;
	}
	public void setCancel(Integer cancel) {
		this.cancel = cancel;
	}
	public Integer getCancelby() {
		return cancelby;
	}
	public void setCancelby(Integer cancelby) {
		this.cancelby = cancelby;
	}
	public Date getCanceldate() {
		return canceldate;
	}
	public void setCanceldate(Date canceldate) {
		this.canceldate = canceldate;
	}
	public List<OpnameDet> getLsOpnameDet() {
		return lsOpnameDet;
	}
	public void setLsOpnameDet(List<OpnameDet> lsOpnameDet) {
		this.lsOpnameDet = lsOpnameDet;
	}
	
}