package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 19, 2013 11:28:39 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Item implements Serializable {


	private static final long serialVersionUID = 1535759834321687415L;

	@NotNull
	public Integer id;

	@Size(max=60)
	public String nama;

	@Size(max=20)
	public String inisial;

	public Integer kategori_id;

	public Integer merk_id;

	public Integer warna_id;

	public Integer satuan_id;

	public Integer satuan2_id;

	public Double qty_satuan2;

	public Integer satuan3_id;

	public Double qty_satuan3;

	public Integer satuan_jual_id;

	public Integer satuan_beli_id;

	public Double harga;

	public Double harga_ecer;

	public Double diskon;
	
	public Double diskon_ecer;

	public Double qty_min;

	public Double qty_max;

	public Double hpp_awal;

	public Date last_order;

	public Double qty_saldo;

	public Double qty_beli;

	public Double qty_jual;

	public Double hpp;

	public Integer active;

	public Integer createby;

	public Date createdate;
	
	public Integer modifyby;
	public Date modifydate;
	public String barcode_ext;
	public String barcode_int;
	
	
	
	
	//tambahan
	public String mode;
	public String createuser;
	public String kategori;
	public String merk;
	public String warna;
	public String satuan;
	public String kategori_idInisial;
	public String  kategori_idNama;
	public String  merk_idInisial;
	public String merk_idNama;
	public String  warna_idInisial;
	public String warna_idNama;
	public String satuan_idInisial;
	public String satuan_idNama;
	public String no; 
	public String ket;
	public String qty;
	public Integer stock_jual;
	public Integer stock_beli;

	
	public String getKategori() {
		return kategori;
	}

	public void setKategori(String kategori) {
		this.kategori = kategori;
	}

	public String getMerk() {
		return merk;
	}

	public void setMerk(String merk) {
		this.merk = merk;
	}

	public String getWarna() {
		return warna;
	}

	public void setWarna(String warna) {
		this.warna = warna;
	}

	public String getSatuan() {
		return satuan;
	}

	public void setSatuan(String satuan) {
		this.satuan = satuan;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public String getInisial() {
		return inisial;
	}

	public void setInisial(String inisial) {
		this.inisial = inisial;
	}

	public Integer getKategori_id() {
		return kategori_id;
	}

	public void setKategori_id(Integer kategori_id) {
		this.kategori_id = kategori_id;
	}

	public Integer getMerk_id() {
		return merk_id;
	}

	public void setMerk_id(Integer merk_id) {
		this.merk_id = merk_id;
	}

	public Integer getWarna_id() {
		return warna_id;
	}

	public void setWarna_id(Integer warna_id) {
		this.warna_id = warna_id;
	}

	public Integer getSatuan_id() {
		return satuan_id;
	}

	public void setSatuan_id(Integer satuan_id) {
		this.satuan_id = satuan_id;
	}

	public Integer getSatuan2_id() {
		return satuan2_id;
	}

	public void setSatuan2_id(Integer satuan2_id) {
		this.satuan2_id = satuan2_id;
	}

	public Double getQty_satuan2() {
		return qty_satuan2;
	}

	public void setQty_satuan2(Double qty_satuan2) {
		this.qty_satuan2 = qty_satuan2;
	}

	public Integer getSatuan3_id() {
		return satuan3_id;
	}

	public void setSatuan3_id(Integer satuan3_id) {
		this.satuan3_id = satuan3_id;
	}

	public Double getQty_satuan3() {
		return qty_satuan3;
	}

	public void setQty_satuan3(Double qty_satuan3) {
		this.qty_satuan3 = qty_satuan3;
	}

	public Integer getSatuan_jual_id() {
		return satuan_jual_id;
	}

	public void setSatuan_jual_id(Integer satuan_jual_id) {
		this.satuan_jual_id = satuan_jual_id;
	}

	public Integer getSatuan_beli_id() {
		return satuan_beli_id;
	}

	public void setSatuan_beli_id(Integer satuan_beli_id) {
		this.satuan_beli_id = satuan_beli_id;
	}

	public Double getHarga() {
		return harga;
	}

	public void setHarga(Double harga) {
		this.harga = harga;
	}

	public Double getHarga_ecer() {
		return harga_ecer;
	}

	public void setHarga_ecer(Double harga_ecer) {
		this.harga_ecer = harga_ecer;
	}

	public Double getDiskon() {
		return diskon;
	}

	public void setDiskon(Double diskon) {
		this.diskon = diskon;
	}

	public Double getQty_min() {
		return qty_min;
	}

	public void setQty_min(Double qty_min) {
		this.qty_min = qty_min;
	}

	public Double getQty_max() {
		return qty_max;
	}

	public void setQty_max(Double qty_max) {
		this.qty_max = qty_max;
	}

	public Double getHpp_awal() {
		return hpp_awal;
	}

	public void setHpp_awal(Double hpp_awal) {
		this.hpp_awal = hpp_awal;
	}

	public Date getLast_order() {
		return last_order;
	}

	public void setLast_order(Date last_order) {
		this.last_order = last_order;
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

	public String getKategori_idInisial() {
		return kategori_idInisial;
	}

	public void setKategori_idInisial(String kategori_idInisial) {
		this.kategori_idInisial = kategori_idInisial;
	}

	public String getKategori_idNama() {
		return kategori_idNama;
	}

	public void setKategori_idNama(String kategori_idNama) {
		this.kategori_idNama = kategori_idNama;
	}

	public String getMerk_idInisial() {
		return merk_idInisial;
	}

	public void setMerk_idInisial(String merk_idInisial) {
		this.merk_idInisial = merk_idInisial;
	}

	public String getMerk_idNama() {
		return merk_idNama;
	}

	public void setMerk_idNama(String merk_idNama) {
		this.merk_idNama = merk_idNama;
	}

	public String getWarna_idInisial() {
		return warna_idInisial;
	}

	public void setWarna_idInisial(String warna_idInisial) {
		this.warna_idInisial = warna_idInisial;
	}

	public String getWarna_idNama() {
		return warna_idNama;
	}

	public void setWarna_idNama(String warna_idNama) {
		this.warna_idNama = warna_idNama;
	}

	public String getSatuan_idInisial() {
		return satuan_idInisial;
	}

	public void setSatuan_idInisial(String satuan_idInisial) {
		this.satuan_idInisial = satuan_idInisial;
	}

	public String getSatuan_idNama() {
		return satuan_idNama;
	}

	public void setSatuan_idNama(String satuan_idNama) {
		this.satuan_idNama = satuan_idNama;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
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

	public String getBarcode_ext() {
		return barcode_ext;
	}

	public void setBarcode_ext(String barcode_ext) {
		this.barcode_ext = barcode_ext;
	}

	public String getBarcode_int() {
		return barcode_int;
	}

	public void setBarcode_int(String barcode_int) {
		this.barcode_int = barcode_int;
	}

	public String getCreateuser() {
		return createuser;
	}

	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}

	public Integer getStock_jual() {
		return stock_jual;
	}

	public void setStock_jual(Integer stock_jual) {
		this.stock_jual = stock_jual;
	}

	public Integer getStock_beli() {
		return stock_beli;
	}

	public void setStock_beli(Integer stock_beli) {
		this.stock_beli = stock_beli;
	}

	public Double getDiskon_ecer() {
		return diskon_ecer;
	}

	public void setDiskon_ecer(Double diskon_ecer) {
		this.diskon_ecer = diskon_ecer;
	}

	public Double getQty_saldo() {
		return qty_saldo;
	}

	public void setQty_saldo(Double qty_saldo) {
		this.qty_saldo = qty_saldo;
	}

	public Double getQty_beli() {
		return qty_beli;
	}

	public void setQty_beli(Double qty_beli) {
		this.qty_beli = qty_beli;
	}

	public Double getQty_jual() {
		return qty_jual;
	}

	public void setQty_jual(Double qty_jual) {
		this.qty_jual = qty_jual;
	}

	public Double getHpp() {
		return hpp;
	}

	public void setHpp(Double hpp) {
		this.hpp = hpp;
	}
	
}
