package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 19, 2013 11:45:12 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Stock implements Serializable {

	private static final long serialVersionUID = -4855359164955043025L;

	@NotNull
	public Integer cabang_id;

	@NotNull
	public Integer item_id;

	public Date periode;

	public Integer saldo_awal;

	public Integer masuk;

	public Integer keluar;

	public Integer qty_order_jual;
	
	public Integer qty_order_beli; 
	
	public Double hpp;
	
	public Double hpp_awal;
	
	public String nama_item;
	
	public String warna_item;
	
	public String kategori_item;
	
	public String merk_item;
	
	public String satuan_item;
	
	public Integer qty;
	
	public Integer jenis;
	
	public String mode;
	
	public Integer qty_fisik;
	
	
	public String namaCabang;
	public String kodeCabang;
	public String namaItem;
	public String barcode_ext;
	public Double stock_jual;
	public Double stock_beli;
	
	public StockHist stockHist=new StockHist();
	
	public Integer getQty_fisik() {
		return qty_fisik;
	}

	public void setQty_fisik(Integer qty_fisik) {
		this.qty_fisik = qty_fisik;
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	
	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public Integer getCabang_id() {
		return cabang_id;
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

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public void setCabang_id(Integer cabang_id) {
		this.cabang_id = cabang_id;
	}

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public Date getPeriode() {
		return periode;
	}

	public void setPeriode(Date periode) {
		this.periode = periode;
	}

	public Integer getSaldo_awal() {
		return saldo_awal;
	}

	public void setSaldo_awal(Integer saldo_awal) {
		this.saldo_awal = saldo_awal;
	}

	public Integer getMasuk() {
		return masuk;
	}

	public void setMasuk(Integer masuk) {
		this.masuk = masuk;
	}

	public Integer getKeluar() {
		return keluar;
	}

	public void setKeluar(Integer keluar) {
		this.keluar = keluar;
	}

	
	public Double getHpp() {
		return hpp;
	}

	public void setHpp(Double hpp) {
		this.hpp = hpp;
	}

	public Integer getQty_order_jual() {
		return qty_order_jual;
	}

	public void setQty_order_jual(Integer qty_order_jual) {
		this.qty_order_jual = qty_order_jual;
	}

	public Integer getQty_order_beli() {
		return qty_order_beli;
	}

	public void setQty_order_beli(Integer qty_order_beli) {
		this.qty_order_beli = qty_order_beli;
	}

	public String getNamaCabang() {
		return namaCabang;
	}

	public void setNamaCabang(String namaCabang) {
		this.namaCabang = namaCabang;
	}

	public String getKodeCabang() {
		return kodeCabang;
	}

	public void setKodeCabang(String kodeCabang) {
		this.kodeCabang = kodeCabang;
	}

	public String getNamaItem() {
		return namaItem;
	}

	public void setNamaItem(String namaItem) {
		this.namaItem = namaItem;
	}

	public String getBarcode_ext() {
		return barcode_ext;
	}

	public void setBarcode_ext(String barcode_ext) {
		this.barcode_ext = barcode_ext;
	}

	public Double getStock_jual() {
		return stock_jual;
	}

	public void setStock_jual(Double stock_jual) {
		this.stock_jual = stock_jual;
	}

	public Double getStock_beli() {
		return stock_beli;
	}

	public void setStock_beli(Double stock_beli) {
		this.stock_beli = stock_beli;
	}

	public StockHist getStockHist() {
		return stockHist;
	}

	public void setStockHist(StockHist stockHist) {
		this.stockHist = stockHist;
	}

	public Double getHpp_awal() {
		return hpp_awal;
	}

	public void setHpp_awal(Double hpp_awal) {
		this.hpp_awal = hpp_awal;
	}
	
	

}
