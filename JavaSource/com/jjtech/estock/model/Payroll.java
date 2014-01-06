package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

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
public class Payroll implements Serializable {

	private static final long serialVersionUID = -1106982690568367117L;

	public Integer id;
   
	public Integer karyawan_id;
	
	 @DateTimeFormat(pattern="MMMMM yyyy")
    public Date periode;

    public Date tgl_bayar;
    
   
    public Double gapok;

    public Double uang_makan;

    public Double uang_transport;

    public Double uang_lembur;

    public Double bonus;

    public Double pot_pinjam;

    public Double pot_asuransi;

    public Double pot_lain;

    public Date tgl_input;
    
    //TAMBAHAN table
    public Integer jenis;
    public String nik;
    public String nama;
    public Date tgl_masuk;
    public Date tgl_keluar;
    public Double gaji;
    
    public Double gaji_total;
    public Double pot_total;
    public Double gaji_bersih;
    
    public String mode;
    
	public Integer getKaryawan_id() {
		return karyawan_id;
	}

	public void setKaryawan_id(Integer karyawan_id) {
		this.karyawan_id = karyawan_id;
	}

	public Date getPeriode() {
		return periode;
	}

	public void setPeriode(Date periode) {
		this.periode = periode;
	}

	public Date getTgl_bayar() {
		return tgl_bayar;
	}

	public void setTgl_bayar(Date tgl_bayar) {
		this.tgl_bayar = tgl_bayar;
	}

	public Double getGapok() {
		return gapok;
	}

	public void setGapok(Double gapok) {
		this.gapok = gapok;
	}

	public Double getUang_makan() {
		return uang_makan;
	}

	public void setUang_makan(Double uang_makan) {
		this.uang_makan = uang_makan;
	}

	public Double getUang_transport() {
		return uang_transport;
	}

	public void setUang_transport(Double uang_transport) {
		this.uang_transport = uang_transport;
	}

	public Double getUang_lembur() {
		return uang_lembur;
	}

	public void setUang_lembur(Double uang_lembur) {
		this.uang_lembur = uang_lembur;
	}

	public Double getBonus() {
		return bonus;
	}

	public void setBonus(Double bonus) {
		this.bonus = bonus;
	}

	public Double getPot_pinjam() {
		return pot_pinjam;
	}

	public void setPot_pinjam(Double pot_pinjam) {
		this.pot_pinjam = pot_pinjam;
	}

	public Double getPot_asuransi() {
		return pot_asuransi;
	}

	public void setPot_asuransi(Double pot_asuransi) {
		this.pot_asuransi = pot_asuransi;
	}

	public Double getPot_lain() {
		return pot_lain;
	}

	public void setPot_lain(Double pot_lain) {
		this.pot_lain = pot_lain;
	}

	public Double getGaji_total() {
		return gaji_total;
	}

	public void setGaji_total(Double gaji_total) {
		this.gaji_total = gaji_total;
	}

	public Double getPot_total() {
		return pot_total;
	}

	public void setPot_total(Double pot_total) {
		this.pot_total = pot_total;
	}

	public Double getGaji_bersih() {
		return gaji_bersih;
	}

	public void setGaji_bersih(Double gaji_bersih) {
		this.gaji_bersih = gaji_bersih;
	}

	public Date getTgl_input() {
		return tgl_input;
	}

	public void setTgl_input(Date tgl_input) {
		this.tgl_input = tgl_input;
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public String getNik() {
		return nik;
	}

	public void setNik(String nik) {
		this.nik = nik;
	}

	public String getNama() {
		return nama;
	}

	public void setNama(String nama) {
		this.nama = nama;
	}

	public Date getTgl_masuk() {
		return tgl_masuk;
	}

	public void setTgl_masuk(Date tgl_masuk) {
		this.tgl_masuk = tgl_masuk;
	}

	public Date getTgl_keluar() {
		return tgl_keluar;
	}

	public void setTgl_keluar(Date tgl_keluar) {
		this.tgl_keluar = tgl_keluar;
	}

	public Double getGaji() {
		return gaji;
	}

	public void setGaji(Double gaji) {
		this.gaji = gaji;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
	
	
}
