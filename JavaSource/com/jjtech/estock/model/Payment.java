package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 19, 2013 11:37:43 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Payment implements Serializable {

	private static final long serialVersionUID = 4157822206629163607L;

    public Integer payment_id;

    @Size(max=18)
    public String no_payment;

    public Integer trans_id;

    public Integer no_urut;

    public Integer account_id;
    
    public Integer trx_id;

	public Date paid_date;

    @Size(max=1)
    public String dk;

    public Integer cara_bayar;
    
    public Double nominal;

    @Size(max=20)
    public String no_giro;

    public Date due_date;

    @Size(max=200)
    public String keterangan;

    public Integer createby;

    public Date createdate;

    public Integer cancel;

    public Integer cancelby;

    public Date canceldate;

	//tambahan
	public String mode;
	public String createuser;
	public Boolean akses;
	public String no_trans;
	public String bank;
	public String cabang;
	public String no_rek;
	public String namakurs;
	public String carabayar;
	public String pagename;
	public String jenispayment;
	public String jenispay;
	public Integer flag_jenis;
	public String account_idKet;
	public String cara_bayarKet;
	public String createbyKet;
	
	public String getJenispayment() {
		return jenispayment;
	}
	public void setJenispayment(String jenispayment) {
		this.jenispayment = jenispayment;
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
	public String getAccount_idKet() {
		return account_idKet;
	}
	public void setAccount_idKet(String account_idKet) {
		this.account_idKet = account_idKet;
	}
	public Integer getFlag_jenis() {
		return flag_jenis;
	}
	public void setFlag_jenis(Integer flag_jenis) {
		this.flag_jenis = flag_jenis;
	}
	public String getJenispay() {
		return jenispay;
	}
	public void setJenispay(String jenispay) {
		this.jenispay = jenispay;
	}
	
	public String getPagename() {
		return pagename;
	}
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}
	
	public Integer getPayment_id() {
		return payment_id;
	}
	public void setPayment_id(Integer payment_id) {
		this.payment_id = payment_id;
	}
	public String getNo_payment() {
		return no_payment;
	}
	public void setNo_payment(String no_payment) {
		this.no_payment = no_payment;
	}
	public Integer getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(Integer trans_id) {
		this.trans_id = trans_id;
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
	public Integer getAccount_id() {
		return account_id;
	}
	public void setAccount_id(Integer account_id) {
		this.account_id = account_id;
	}
	public Date getPaid_date() {
		return paid_date;
	}
	public void setPaid_date(Date paid_date) {
		this.paid_date = paid_date;
	}
	public String getDk() {
		return dk;
	}
	public void setDk(String dk) {
		this.dk = dk;
	}
	public Integer getCara_bayar() {
		return cara_bayar;
	}
	public void setCara_bayar(Integer cara_bayar) {
		this.cara_bayar = cara_bayar;
	}
	public Double getNominal() {
		return nominal;
	}
	public void setNominal(Double nominal) {
		this.nominal = nominal;
	}
	public String getNo_giro() {
		return no_giro;
	}
	public void setNo_giro(String no_giro) {
		this.no_giro = no_giro;
	}
	public Date getDue_date() {
		return due_date;
	}
	public void setDue_date(Date due_date) {
		this.due_date = due_date;
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
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Boolean getAkses() {
		return akses;
	}
	public void setAkses(Boolean akses) {
		this.akses = akses;
	}
	public String getNo_trans() {
		return no_trans;
	}
	public void setNo_trans(String no_trans) {
		this.no_trans = no_trans;
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
	
}
