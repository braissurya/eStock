package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

public class RepTB implements Serializable {


	private static final long serialVersionUID = -1444160338766828835L;


	public Date periode;

    
    @Size(max=18)
    public String coa_id;

    public Double saldo;

    public Double total_debet;

    public Double total_kredit;

    public Date last_update;

    public Integer createby;

    public Date createdate;

	public Date getPeriode() {
		return periode;
	}

	public void setPeriode(Date periode) {
		this.periode = periode;
	}

	public String getCoa_id() {
		return coa_id;
	}

	public void setCoa_id(String coa_id) {
		this.coa_id = coa_id;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Double getTotal_debet() {
		return total_debet;
	}

	public void setTotal_debet(Double total_debet) {
		this.total_debet = total_debet;
	}

	public Double getTotal_kredit() {
		return total_kredit;
	}

	public void setTotal_kredit(Double total_kredit) {
		this.total_kredit = total_kredit;
	}

	public Date getLast_update() {
		return last_update;
	}

	public void setLast_update(Date last_update) {
		this.last_update = last_update;
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
    
    

}
