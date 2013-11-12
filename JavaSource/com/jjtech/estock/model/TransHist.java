package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 19, 2013 11:20:14 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class TransHist implements Serializable {


	private static final long serialVersionUID = -5124084806781760554L;

	@NotNull
	public Integer trans_id;
	
	@NotNull
	public String no_trans;

	public Date tgl;

	public Integer posisi_id;

	@Size(max=30)
	public String keterangan;

	public Integer createby;

	public Date createdate;

	public TransHist(Integer trans_id,Date tgl, Integer posisi_id,String keterangan,Integer createby){
		this.trans_id=trans_id;
		this.tgl=tgl;
		this.posisi_id=posisi_id;
		this.keterangan=keterangan;
		this.createdate=tgl;
		this.createby=createby;
	}
	
	public Integer getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(Integer trans_id) {
		this.trans_id = trans_id;
	}
	
	public String getNo_trans() {
		return no_trans;
	}

	public void setNo_trans(String no_trans) {
		this.no_trans = no_trans;
	}

	public Date getTgl() {
		return tgl;
	}

	public void setTgl(Date tgl) {
		this.tgl = tgl;
	}

	public Integer getPosisi_id() {
		return posisi_id;
	}

	public void setPosisi_id(Integer posisi_id) {
		this.posisi_id = posisi_id;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
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