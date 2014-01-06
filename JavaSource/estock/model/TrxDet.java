package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author 		: Rudy Hermawan
 * @since		: Apr 29, 2013 9:55:00 AM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class TrxDet implements Serializable {

	private static final long serialVersionUID = 7915186456720797802L;

    public Integer trx_id;
    
    public Integer no_urut;

    @Size(max=18)
    public String coa_id;

    @Size(max=100)
    public String ket;

    @Size(max=1)
    public String dk;

    public Double jumlah;
    
    public Integer urut;
    
    public Integer account_id;
    
    //tambahan
    public String coa_idKet;
    public Double jumlahDebet;
    public Double jumlahKredit;
    
	public Double getJumlahDebet() {
		return jumlahDebet;
	}

	public void setJumlahDebet(Double jumlahDebet) {
		this.jumlahDebet = jumlahDebet;
	}

	public Double getJumlahKredit() {
		return jumlahKredit;
	}

	public void setJumlahKredit(Double jumlahKredit) {
		this.jumlahKredit = jumlahKredit;
	}

	public String getCoa_idKet() {
		return coa_idKet;
	}

	public void setCoa_idKet(String coa_idKet) {
		this.coa_idKet = coa_idKet;
	}

	public Integer getUrut() {
		return urut;
	}

	public void setUrut(Integer urut) {
		this.urut = urut;
	}

	public Integer getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(Integer trx_id) {
		this.trx_id = trx_id;
	}

	public Integer getNo_urut() {
		return no_urut;
	}

	public void setNo_urut(Integer no_urut) {
		this.no_urut = no_urut;
	}

	public String getCoa_id() {
		return coa_id;
	}

	public void setCoa_id(String coa_id) {
		this.coa_id = coa_id;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public String getDk() {
		return dk;
	}

	public void setDk(String dk) {
		this.dk = dk;
	}

	public Double getJumlah() {
		return jumlah;
	}

	public void setJumlah(Double jumlah) {
		this.jumlah = jumlah;
	}

	public Integer getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}
	
	
	
}
