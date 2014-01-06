package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Apr 22, 2013 11:10:50 PM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Delivery implements Serializable {

	private static final long serialVersionUID = 4484565412131404086L;

	public Integer id;

    public String kode;

    public Integer driver_id;

    public Integer posisi_id;
    
    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm:ss")
    public Date tgl_kirim;
    
    public Date tgl_print_sj;

    @DateTimeFormat(pattern="dd-MM-yyyy HH:mm:ss")
    public Date tgl_kembali;

    public String keterangan;

    public Date createdate;

    public Integer createby;
    
  //TAMBAHAN
  	public String mode;
  	public String driverNama;
  	public String posisiKet;
  	
    public List<DeliveryDet> lsDeliveryDets=new ArrayList<DeliveryDet>(); 


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getKode() {
		return kode;
	}


	public void setKode(String kode) {
		this.kode = kode;
	}


	public Integer getDriver_id() {
		return driver_id;
	}


	public void setDriver_id(Integer driver_id) {
		this.driver_id = driver_id;
	}


	
	public Integer getPosisi_id() {
		return posisi_id;
	}


	public void setPosisi_id(Integer posisi_id) {
		this.posisi_id = posisi_id;
	}


	public Date getTgl_kirim() {
		return tgl_kirim;
	}


	public void setTgl_kirim(Date tgl_kirim) {
		this.tgl_kirim = tgl_kirim;
	}


	


	public Date getTgl_kembali() {
		return tgl_kembali;
	}


	public void setTgl_kembali(Date tgl_kembali) {
		this.tgl_kembali = tgl_kembali;
	}


	public String getKeterangan() {
		return keterangan;
	}


	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}


	public Date getCreatedate() {
		return createdate;
	}


	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


	public Integer getCreateby() {
		return createby;
	}


	public void setCreateby(Integer createby) {
		this.createby = createby;
	}


	public String getDriverNama() {
		return driverNama;
	}


	public void setDriverNama(String driverNama) {
		this.driverNama = driverNama;
	}


	public String getPosisiKet() {
		return posisiKet;
	}


	public void setPosisiKet(String posisiKet) {
		this.posisiKet = posisiKet;
	}


	public List<DeliveryDet> getLsDeliveryDets() {
		return lsDeliveryDets;
	}


	public void setLsDeliveryDets(List<DeliveryDet> lsDeliveryDets) {
		this.lsDeliveryDets = lsDeliveryDets;
	}


	public String getMode() {
		return mode;
	}


	public void setMode(String mode) {
		this.mode = mode;
	}


	public Date getTgl_print_sj() {
		return tgl_print_sj;
	}


	public void setTgl_print_sj(Date tgl_print_sj) {
		this.tgl_print_sj = tgl_print_sj;
	}
    
	
    

}
