package com.jjtech.estock.model;

import java.io.Serializable;

public class Gudang implements Serializable{


	private static final long serialVersionUID = -2138826549669923390L;

	public Integer jenis;
	
	public String trans_no;
	
	public Trans trans=new  Trans();
	
	public Delivery delivery=new Delivery();
	
	public String mode;
	
	public String jenistrans;
	
	

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public Trans getTrans() {
		return trans;
	}

	public void setTrans(Trans trans) {
		this.trans = trans;
	}

	public Delivery getDelivery() {
		return delivery;
	}

	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getTrans_no() {
		return trans_no;
	}

	public void setTrans_no(String trans_no) {
		this.trans_no = trans_no;
	}

	public String getJenistrans() {
		return jenistrans;
	}

	public void setJenistrans(String jenistrans) {
		this.jenistrans = jenistrans;
	}
	
	
	
	
}
