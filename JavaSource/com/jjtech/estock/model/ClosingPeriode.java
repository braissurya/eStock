package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

public class ClosingPeriode implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8047456686455514881L;

	public Integer id;  
	  
	public Integer type;

	public String desc;

	public Date periode;
	 
	public Integer cabang_id;
	
	public ClosingPeriode(Integer type,Integer cabang_id,Date periode){
		this.type=type;
		this.cabang_id=cabang_id;
		this.periode=periode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getPeriode() {
		return periode;
	}

	public void setPeriode(Date periode) {
		this.periode = periode;
	}

	public Integer getCabang_id() {
		return cabang_id;
	}

	public void setCabang_id(Integer cabang_id) {
		this.cabang_id = cabang_id;
	}
	
	
	
}
