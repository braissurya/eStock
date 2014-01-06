package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class Trx implements Serializable {

	private static final long serialVersionUID = -1915082595533040252L;

	public Integer trx_id;

    @Size(max=9)
    public String no_trx;

    @Size(max=20)
    public String no_voucher;

    public Integer cash_flow_id;

    public Date tgl_trx;

    public Date tgl_rk;

    public Date tgl_jurnal;

    public Integer posisi_id;

    public Integer createby;

    public Date createdate;

    public Integer cancel;

    public Integer cancelby;

    public Date canceldate;
    
    public String coa_id;
    
    public Integer account_id;
    
	//tambahan
    public String mode;
    public String bank;
	public String cabang;
	public String no_rek;
	public String namakurs;
	public String carabayar;
	public String pagename;
	public String jenispayment;
	public String jenispay;
	public String account_idKet;
	public String coa_idKet;
	public String cara_bayarKet;
	public String createbyKet;
	public String dk;
	public String keterangan;
	public Double nominal;
	public String cash_flow_idKet;
	public String nama;
	
	public TrxDet trxDet=new TrxDet();
	public List<TrxDet> lsTrxDet= new ArrayList<TrxDet>();
	
	public Integer getAccount_id() {
		return account_id;
	}

	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}
	
	public String getCash_flow_idKet() {
		return cash_flow_idKet;
	}

	public void setCash_flow_idKet(String Cash_flow_idKet) {
		this.cash_flow_idKet = cash_flow_idKet;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public List<TrxDet> getLsTrxDet() {
		return lsTrxDet;
	}

	public void setLsTrxDet(List<TrxDet> lsTrxDet) {
		this.lsTrxDet = lsTrxDet;
	}
	
	public TrxDet getTrxDet() {
		return trxDet;
	}

	public void setTrxDet(TrxDet trxDet) {
		this.trxDet = trxDet;
	}

	public String getCoa_id() {
		return coa_id;
	}

	public void setCoa_id(String coa_id) {
		this.coa_id = coa_id;
	}
	
	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

	public Double getNominal() {
		return nominal;
	}

	public void setNominal(Double nominal) {
		this.nominal = nominal;
	}

	public String getDk() {
		return dk;
	}

	public void setDk(String dk) {
		this.dk = dk;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getCabang() {
		return cabang;
	}

	public void setCabang(String cabang) {
		this.cabang = cabang;
	}

	public String getNo_rek() {
		return no_rek;
	}

	public void setNo_rek(String no_rek) {
		this.no_rek = no_rek;
	}

	public String getNamakurs() {
		return namakurs;
	}

	public void setNamakurs(String namakurs) {
		this.namakurs = namakurs;
	}

	public String getCarabayar() {
		return carabayar;
	}

	public void setCarabayar(String carabayar) {
		this.carabayar = carabayar;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public String getJenispayment() {
		return jenispayment;
	}

	public void setJenispayment(String jenispayment) {
		this.jenispayment = jenispayment;
	}

	public String getJenispay() {
		return jenispay;
	}

	public void setJenispay(String jenispay) {
		this.jenispay = jenispay;
	}

	public String getAccount_idKet() {
		return account_idKet;
	}

	public void setAccount_idKet(String account_idKet) {
		this.account_idKet = account_idKet;
	}

	public String getCara_bayarKet() {
		return cara_bayarKet;
	}

	public void setCara_bayarKet(String cara_bayarKet) {
		this.cara_bayarKet = cara_bayarKet;
	}

	public String getCreatebyKet() {
		return createbyKet;
	}

	public void setCreatebyKet(String createbyKet) {
		this.createbyKet = createbyKet;
	}
	
	public String getCoa_idKet() {
		return coa_idKet;
	}

	public void setCoa_idKet(String coa_idKet) {
		this.coa_idKet = coa_idKet;
	}

	public Integer getTrx_id() {
		return trx_id;
	}

	public void setTrx_id(Integer trx_id) {
		this.trx_id = trx_id;
	}

	public String getNo_trx() {
		return no_trx;
	}

	public void setNo_trx(String no_trx) {
		this.no_trx = no_trx;
	}

	public String getNo_voucher() {
		return no_voucher;
	}

	public void setNo_voucher(String no_voucher) {
		this.no_voucher = no_voucher;
	}

	public Integer getCash_flow_id() {
		return cash_flow_id;
	}

	public void setCash_flow_id(Integer cash_flow_id) {
		this.cash_flow_id = cash_flow_id;
	}

	public Date getTgl_trx() {
		return tgl_trx;
	}

	public void setTgl_trx(Date tgl_trx) {
		this.tgl_trx = tgl_trx;
	}

	public Date getTgl_rk() {
		return tgl_rk;
	}

	public void setTgl_rk(Date tgl_rk) {
		this.tgl_rk = tgl_rk;
	}

	public Date getTgl_jurnal() {
		return tgl_jurnal;
	}

	public void setTgl_jurnal(Date tgl_jurnal) {
		this.tgl_jurnal = tgl_jurnal;
	}

	public Integer getPosisi_id() {
		return posisi_id;
	}

	public void setPosisi_id(Integer posisi_id) {
		this.posisi_id = posisi_id;
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

	public Integer getCancel() {
		return cancel;
	}

	public void setCancel(Integer cancel) {
		this.cancel = cancel;
	}

	public Integer getCancelby() {
		return cancelby;
	}

	public void setCancelby(Integer cancelby) {
		this.cancelby = cancelby;
	}

	public Date getCanceldate() {
		return canceldate;
	}

	public void setCanceldate(Date canceldate) {
		this.canceldate = canceldate;
	}

}
