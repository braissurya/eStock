package com.jjtech.estock.model;

import java.io.Serializable;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

public class OpnameDet implements Serializable {

	public Integer opname_id;
	
	@NotEmpty
	public Integer item_id;
	
	public Integer jenis;
	
	public String jenis_string;
	
	public String getJenis_string() {
		return jenis_string;
	}
	public void setJenis_string(String jenis_string) {
		this.jenis_string = jenis_string;
	}
	@Size(max=9)
	public Integer qty;
	
	@Size(max=9)
	public Integer qty_fisik;
	
	public String nama_item;
	
	public String warna_item;
	
	public String kategori_item;
	
	public String merk_item;
	
	public String satuan_item;
	
	public String keterangan;
	
	
	public String getKeterangan() {
		return keterangan;
	}
	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}
	public String getNama_item() {
		return nama_item;
	}
	public void setNama_item(String nama_item) {
		this.nama_item = nama_item;
	}
	public String getWarna_item() {
		return warna_item;
	}
	public void setWarna_item(String warna_item) {
		this.warna_item = warna_item;
	}
	public String getKategori_item() {
		return kategori_item;
	}
	public void setKategori_item(String kategori_item) {
		this.kategori_item = kategori_item;
	}
	public String getMerk_item() {
		return merk_item;
	}
	public void setMerk_item(String merk_item) {
		this.merk_item = merk_item;
	}
	public String getSatuan_item() {
		return satuan_item;
	}
	public void setSatuan_item(String satuan_item) {
		this.satuan_item = satuan_item;
	}
	
	public Integer getOpname_id() {
		return opname_id;
	}
	public void setOpname_id(Integer opname_id) {
		this.opname_id = opname_id;
	}
	public Integer getItem_id() {
		return item_id;
	}
	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}
	public Integer getJenis() {
		return jenis;
	}
	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	public Integer getQty_fisik() {
		return qty_fisik;
	}
	public void setQty_fisik(Integer qty_fisik) {
		this.qty_fisik = qty_fisik;
	}
}
