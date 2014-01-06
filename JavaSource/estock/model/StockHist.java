package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 19, 2013 11:54:39 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class StockHist implements Serializable {


	private static final long serialVersionUID = 2240008792573280128L;


	public Integer item_id;

	public Date tgl;


	public Integer no_urut;

	@Size(max=1)
	public String dk;
	
	public Integer cabang_id;

	public Integer qty;

	public Integer qty_saldo;

	public Double harga;

	public Double hpp;
	
	public String no_trans;	
	
	public Integer stock_awal;
	public Integer stock_akhir;
	public Double total_harga;
	public Double nilai_hpp;
	
	//tambahan
	public Double hpp_akhir;
	
	public StockHist(){	
	}

	public StockHist(Integer item_id,Date tgl,
			String dk,Integer cabang_id,Integer qty,Integer qty_saldo,
			Double harga,Double hpp_awal,String no_trans, Integer stock_awal){
		this.no_trans=no_trans;
		this.item_id=item_id;
		this.tgl=tgl;
		this.dk=dk;
		this.cabang_id=cabang_id;
		this.qty=qty;
		this.qty_saldo=qty_saldo;
		this.harga=harga;

		this.stock_awal=stock_awal;
		this.total_harga=this.harga*this.qty;
		Double nilai_saldo=hpp_awal*stock_awal;
		if(dk.equals("I")){
			this.stock_akhir=stock_awal-this.qty;
			this.hpp=nilai_saldo/stock_awal;
			this.nilai_hpp=this.hpp*this.qty;
		}else if(dk.equals("O")){
			this.stock_akhir=stock_awal+this.qty;
			this.hpp=(nilai_saldo+this.total_harga)/this.stock_akhir;
			this.nilai_hpp=null;
		}		
		
		
		this.hpp_akhir=this.hpp;
		
	}
	
	public Integer getItem_id() {
		return item_id;
	}

	public void setItem_id(Integer item_id) {
		this.item_id = item_id;
	}

	public Date getTgl() {
		return tgl;
	}

	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}

	public Integer getNo_urut() {
		return no_urut;
	}

	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}

	public String getDk() {
		return dk;
	}

	public void setDk(String dk) {
		this.dk = dk;
	}

	public Integer getQty() {
		return qty;
	}

	public void setQty(Integer qty) {
		this.qty = qty;
	}

	public Integer getQty_saldo() {
		return qty_saldo;
	}

	public void setQty_saldo(Integer qty_saldo) {
		this.qty_saldo = qty_saldo;
	}

	public Double getHarga() {
		return harga;
	}

	public void setHarga(Double harga) {
		this.harga = harga;
	}

	public Double getHpp() {
		return hpp;
	}

	public void setHpp(Double hpp) {
		this.hpp = hpp;
	}

	public Integer getCabang_id() {
		return cabang_id;
	}

	public void setCabang_id(Integer cabang_id) {
		this.cabang_id = cabang_id;
	}

	public String getNo_trans() {
		return no_trans;
	}

	public void setNo_trans(String no_trans) {
		this.no_trans = no_trans;
	}

	public Integer getStock_awal() {
		return stock_awal;
	}

	public void setStock_awal(Integer stock_awal) {
		this.stock_awal = stock_awal;
	}

	public Integer getStock_akhir() {
		return stock_akhir;
	}

	public void setStock_akhir(Integer stock_akhir) {
		this.stock_akhir = stock_akhir;
	}

	public Double getTotal_harga() {
		return total_harga;
	}

	public void setTotal_harga(Double total_harga) {
		this.total_harga = total_harga;
	}

	public Double getNilai_hpp() {
		return nilai_hpp;
	}

	public void setNilai_hpp(Double nilai_hpp) {
		this.nilai_hpp = nilai_hpp;
	}

	public Double getHpp_akhir() {
		return hpp_akhir;
	}

	public void setHpp_akhir(Double hpp_akhir) {
		this.hpp_akhir = hpp_akhir;
	}
	
	

}