package com.jjtech.estock.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 19, 2013 11:04:03 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class TransDet implements Serializable {

	private static final long serialVersionUID = 83538355148055218L;

	@NotNull
	public Integer trans_id;

	@NotNull
	public Integer urut;

	public Integer item_id;

	public Integer qty_order;

	public Integer qty;

	public Double harga;

	public Double persen_diskon;

	public Double jumlah_diskon;

	@Size(max=1)
	public String dk;

	public Integer flag;

	public Double hpp;

	@Size(max=100)
	public String ket;
	
	public Integer jenis_retur;
	
	//Tambahan
	public String item_idKet;
	public Double subTotal_harga;
	public Double subTotal_diskon;
	public String item_no;
	public String barcode_ext;
	public String barcode_int;
	
	public String satuan_idKet;
	public Integer stock;
	public String update;
	public Integer qty_before;

	public Integer getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(Integer trans_id) {
		this.trans_id = trans_id;
	}

	public Integer getUrut() {
		return urut;
	}

	public void setUrut(Integer urut) {
		this.urut = urut;
	}

	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public Integer getQty_order() {
		return qty_order;
	}

	public void setQty_order(Integer qty_order) {
		this.qty_order = qty_order;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Double getHarga() {
		return harga;
	}

	public void setHarga(Double harga) {
		this.harga = harga;
	}

	public Double getPersen_diskon() {
		return persen_diskon;
	}

	public void setPersen_diskon(Double persen_diskon) {
		this.persen_diskon = persen_diskon;
	}

	public Double getJumlah_diskon() {
		return jumlah_diskon;
	}

	public void setJumlah_diskon(Double jumlah_diskon) {
		this.jumlah_diskon = jumlah_diskon;
	}

	public String getDk() {
		return dk;
	}

	public void setDk(String dk) {
		this.dk = dk;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Double getHpp() {
		return hpp;
	}

	public void setHpp(Double hpp) {
		this.hpp = hpp;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public Integer getJenis_retur() {
		return jenis_retur;
	}

	public void setJenis_retur(Integer jenis_retur) {
		this.jenis_retur = jenis_retur;
	}

	public String getItem_idKet() {
		return item_idKet;
	}

	public void setItem_idKet(String item_idKet) {
		this.item_idKet = item_idKet;
	}

	public Double getSubTotal_harga() {
		return subTotal_harga;
	}

	public void setSubTotal_harga(Double subTotal_harga) {
		this.subTotal_harga = subTotal_harga;
	}

	public String getItem_no() {
		return item_no;
	}

	public void setItem_no(String item_no) {
		this.item_no = item_no;
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

	public Double getSubTotal_diskon() {
		return subTotal_diskon;
	}

	public void setSubTotal_diskon(Double subTotal_diskon) {
		this.subTotal_diskon = subTotal_diskon;
	}

	public String getSatuan_idKet() {
		return satuan_idKet;
	}

	public void setSatuan_idKet(String satuan_idKet) {
		this.satuan_idKet = satuan_idKet;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public String getUpdate() {
		return update;
	}

	public void setUpdate(String update) {
		this.update = update;
	}

	public Integer getQty_before() {
		return qty_before;
	}

	public void setQty_before(Integer qty_before) {
		this.qty_before = qty_before;
	}

}
