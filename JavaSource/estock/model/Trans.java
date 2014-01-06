package com.jjtech.estock.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;


/**
 * @author 		: Bertho Rafitya Iwasurya
 * @since		: Mar 22, 2013 12:32:53 AM
 * @Description :
 * @Revision	:
 * #====#===========#===================#===========================#
 * | ID	|    Date	|	    User		|			Description		|
 * #====#===========#===================#===========================#
 * |	|			|					|							|
 * #====#===========#===================#===========================#
 */
public class Trans implements Serializable {

	private static final long serialVersionUID = -5382690583883134368L;


	public Integer trans_id;

	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm:ss")
	public Date trans_date;

	public Integer jenis;

	@Size(max=14)
	public String no_trans;

	@Size(max=20)
	public String no_po;

	@Size(max=14)
	public String no_sj;

	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm:ss")
	public Date tgl_kirim;
	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm:ss")
	public Date tgl_order;

	@DateTimeFormat(pattern="dd-MM-yyyy HH:mm:ss")
	public Date tgl_kirim_est;
	public Date tgl_kembali;
	public Date tgl_req_trans;

	public Date tgl_gudang_trans;
	public Date tgl_terima_trans;

	public Integer flag_ecer;
	public Integer flag_kirim;
	public Integer pay_mode;
	public Integer flag_pajak;

	@Size(max=1)
	public String dk;

	public Integer retail_id;

	public Integer retail_id_req;

	public Integer gudang_id;

	public Integer supplier_id;

	public Integer customer_id;

	public Integer sales_id;

	public Integer supplier_idNull;

	public Integer customer_idNull;

	public Integer sales_idNull;

	public Integer driver_id;
	
	public Integer delivery_id;
	
	public Integer status_kirim;
	
	@Size(max=100)
	public String ket;

	public Double limit_hutang;

	public Double total_harga;

	public Double total_disc;

	public Integer posisi_id;

	public Integer print_trans;

	public Integer print_sj;

	public Date due_date;

	public Integer paid;
	
	public Double remain;
	
	public Double ppn;

	public String no_trans_ref;
	
	public Integer retur_type;

	public Integer approveby;

	public Date approvedate;

	public Integer createby;

	public Date createdate;

	public Integer cancel;
	public Integer cancelby;
	public Date canceldate;

	public Integer receivedby;
	public Date receiveddate;
	
	public String contact_tujuan;
	public String alamat_tujuan;
	public String telp_tujuan;

	public Date print_order_form;
	public Date print_trans_date;
	public Date print_faktur_date;
	public Date print_sj_date;

	


	//Tambahan table
	public String jenisKet;
	public String flag_ecerKet;
	public String dkKet;
	public String retail_idKet;
	public String gudang_idKet;
	public String supplier_idKet;
	public String customer_idKet;
	public String sales_idKet;
	public String driver_idKet;
	public String posisi_idKet;
	public String approvebyKet;
	public String createbyKet;
	public String pay_modeKet;
	public String flag_kirimKet;
	public String retail_id_reqKet;
	public String retur_typeKet;
	
	public String tgl_kirimNull;
	public String driver_idNull;	
	public String delivery_idNull;
	public String status_kirimNull;

	//TAMBAHAN
	public String mode;
	public String jenistrans;
	public String pagename;


	public Double sub_total_harga;
	public Double persen_disc;
	public Double persen_ppn;

	public int[] kode;
	public String[] nama;
	public double[] subTotal_harga;
	public int[] qty;
	public double[] harga;
	public double[] persen_diskon;
	public double[] jumlah_diskon;

	public TransDet transDet=new TransDet();
	public List<TransDet> lsTransDet=new ArrayList<TransDet>();
	public Customer customer=new Customer();
	public Supplier supplier=new Supplier();
	public Karyawan karyawan=new Karyawan();
	public Integer searchType;
	public Integer flag_akses_all;
	
	public Integer getFlag_akses_all() {
		return flag_akses_all;
	}

	public void setFlag_akses_all(Integer flag_akses_all) {
		this.flag_akses_all = flag_akses_all;
	}
	
	public Integer getSearchType() {
		return searchType;
	}

	public void setSearchType(Integer searchType) {
		this.searchType = searchType;
	}

	public Integer getTrans_id() {
		return trans_id;
	}

	public void setTrans_id(Integer trans_id) {
		this.trans_id = trans_id;
	}

	public Date getTrans_date() {
		return trans_date;
	}

	public void setTrans_date(Date trans_date) {
		this.trans_date = trans_date;
	}

	public Integer getJenis() {
		return jenis;
	}

	public void setJenis(Integer jenis) {
		this.jenis = jenis;
	}

	public String getNo_trans() {
		return no_trans;
	}

	public void setNo_trans(String no_trans) {
		this.no_trans = no_trans;
	}

	public String getNo_po() {
		return no_po;
	}

	public void setNo_po(String no_po) {
		this.no_po = no_po;
	}

	public String getNo_sj() {
		return no_sj;
	}

	public void setNo_sj(String no_sj) {
		this.no_sj = no_sj;
	}

	public Date getTgl_kirim() {
		return tgl_kirim;
	}

	public void setTgl_kirim(Date tgl_kirim) {
		this.tgl_kirim = tgl_kirim;
	}

	public Integer getFlag_ecer() {
		return flag_ecer;
	}

	public void setFlag_ecer(Integer flag_ecer) {
		this.flag_ecer = flag_ecer;
	}

	public String getDk() {
		return dk;
	}

	public void setDk(String dk) {
		this.dk = dk;
	}

	public Integer getRetail_id() {
		return retail_id;
	}

	public void setRetail_id(Integer retail_id) {
		this.retail_id = retail_id;
	}

	public Integer getGudang_id() {
		return gudang_id;
	}

	public void setGudang_id(Integer gudang_id) {
		this.gudang_id = gudang_id;
	}

	public Integer getSupplier_id() {
		return supplier_id;
	}

	public void setSupplier_id(Integer supplier_id) {
		this.supplier_id = supplier_id;
	}

	public Integer getCustomer_id() {
		return customer_id;
	}

	public void setCustomer_id(Integer customer_id) {
		this.customer_id = customer_id;
	}

	public Integer getSales_id() {
		return sales_id;
	}

	public void setSales_id(Integer sales_id) {
		this.sales_id = sales_id;
	}

	public Integer getDriver_id() {
		return driver_id;
	}

	public void setDriver_id(Integer driver_id) {
		this.driver_id = driver_id;
	}

	public String getKet() {
		return ket;
	}

	public void setKet(String ket) {
		this.ket = ket;
	}

	public Double getTotal_harga() {
		return total_harga;
	}

	public void setTotal_harga(Double total_harga) {
		this.total_harga = total_harga;
	}

	public Double getTotal_disc() {
		return total_disc;
	}

	public void setTotal_disc(Double total_disc) {
		this.total_disc = total_disc;
	}

	public Integer getPosisi_id() {
		return posisi_id;
	}

	public void setPosisi_id(Integer posisi_id) {
		this.posisi_id = posisi_id;
	}

	public Integer getPrint_trans() {
		return print_trans;
	}

	public void setPrint_trans(Integer print_trans) {
		this.print_trans = print_trans;
	}

	public Integer getPrint_sj() {
		return print_sj;
	}

	public void setPrint_sj(Integer print_sj) {
		this.print_sj = print_sj;
	}

	public Date getDue_date() {
		return due_date;
	}

	public void setDue_date(Date due_date) {
		this.due_date = due_date;
	}

	public Integer getPaid() {
		return paid;
	}

	public void setPaid(Integer paid) {
		this.paid = paid;
	}

	public Integer getRetur_type() {
		return retur_type;
	}

	public void setRetur_type(Integer retur_type) {
		this.retur_type = retur_type;
	}

	public Integer getApproveby() {
		return approveby;
	}

	public void setApproveby(Integer approveby) {
		this.approveby = approveby;
	}

	public Date getApprovedate() {
		return approvedate;
	}

	public void setApprovedate(Date approvedate) {
		this.approvedate = approvedate;
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

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public List<TransDet> getLsTransDet() {
		return lsTransDet;
	}

	public void setLsTransDet(List<TransDet> lsTransDet) {
		this.lsTransDet = lsTransDet;
	}

	public String getJenisKet() {
		return jenisKet;
	}

	public void setJenisKet(String jenisKet) {
		this.jenisKet = jenisKet;
	}

	public String getFlag_ecerKet() {
		return flag_ecerKet;
	}

	public void setFlag_ecerKet(String flag_ecerKet) {
		this.flag_ecerKet = flag_ecerKet;
	}

	public String getDkKet() {
		return dkKet;
	}

	public void setDkKet(String dkKet) {
		this.dkKet = dkKet;
	}

	public String getRetail_idKet() {
		return retail_idKet;
	}

	public void setRetail_idKet(String retail_idKet) {
		this.retail_idKet = retail_idKet;
	}

	public String getGudang_idKet() {
		return gudang_idKet;
	}

	public void setGudang_idKet(String gudang_idKet) {
		this.gudang_idKet = gudang_idKet;
	}

	public String getSupplier_idKet() {
		return supplier_idKet;
	}

	public void setSupplier_idKet(String supplier_idKet) {
		this.supplier_idKet = supplier_idKet;
	}

	public String getCustomer_idKet() {
		return customer_idKet;
	}

	public void setCustomer_idKet(String customer_idKet) {
		this.customer_idKet = customer_idKet;
	}

	public String getSales_idKet() {
		return sales_idKet;
	}

	public void setSales_idKet(String sales_idKet) {
		this.sales_idKet = sales_idKet;
	}

	public String getDriver_idKet() {
		return driver_idKet;
	}

	public void setDriver_idKet(String driver_idKet) {
		this.driver_idKet = driver_idKet;
	}

	public String getPosisi_idKet() {
		return posisi_idKet;
	}

	public void setPosisi_idKet(String posisi_idKet) {
		this.posisi_idKet = posisi_idKet;
	}

	public String getApprovebyKet() {
		return approvebyKet;
	}

	public void setApprovebyKet(String approvebyKet) {
		this.approvebyKet = approvebyKet;
	}

	public String getCreatebyKet() {
		return createbyKet;
	}

	public void setCreatebyKet(String createbyKet) {
		this.createbyKet = createbyKet;
	}

	public String getJenistrans() {
		return jenistrans;
	}

	public void setJenistrans(String jenistrans) {
		this.jenistrans = jenistrans;
	}

	public String getPagename() {
		return pagename;
	}

	public void setPagename(String pagename) {
		this.pagename = pagename;
	}

	public TransDet getTransDet() {
		return transDet;
	}

	public void setTransDet(TransDet transDet) {
		this.transDet = transDet;
	}

	public int[] getKode() {
		return kode;
	}

	public void setKode(int[] kode) {
		this.kode = kode;
	}

	public String[] getNama() {
		return nama;
	}

	public void setNama(String[] nama) {
		this.nama = nama;
	}

	public double[] getSubTotal_harga() {
		return subTotal_harga;
	}

	public void setSubTotal_harga(double[] subTotal_harga) {
		this.subTotal_harga = subTotal_harga;
	}

	public int[] getQty() {
		return qty;
	}

	public void setQty(int[] qty) {
		this.qty = qty;
	}

	public double[] getHarga() {
		return harga;
	}

	public void setHarga(double[] harga) {
		this.harga = harga;
	}

	public double[] getPersen_diskon() {
		return persen_diskon;
	}

	public void setPersen_diskon(double[] persen_diskon) {
		this.persen_diskon = persen_diskon;
	}

	public double[] getJumlah_diskon() {
		return jumlah_diskon;
	}

	public void setJumlah_diskon(double[] jumlah_diskon) {
		this.jumlah_diskon = jumlah_diskon;
	}

	public Double getPersen_disc() {
		return persen_disc;
	}

	public void setPersen_disc(Double persen_disc) {
		this.persen_disc = persen_disc;
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

	public Double getSub_total_harga() {
		return sub_total_harga;
	}

	public void setSub_total_harga(Double sub_total_harga) {
		this.sub_total_harga = sub_total_harga;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public Karyawan getKaryawan() {
		return karyawan;
	}

	public void setKaryawan(Karyawan karyawan) {
		this.karyawan = karyawan;
	}

	public String getContact_tujuan() {
		return contact_tujuan;
	}

	public void setContact_tujuan(String contact_tujuan) {
		this.contact_tujuan = contact_tujuan;
	}

	public String getAlamat_tujuan() {
		return alamat_tujuan;
	}

	public void setAlamat_tujuan(String alamat_tujuan) {
		this.alamat_tujuan = alamat_tujuan;
	}

	public String getTelp_tujuan() {
		return telp_tujuan;
	}

	public void setTelp_tujuan(String telp_tujuan) {
		this.telp_tujuan = telp_tujuan;
	}

	public Date getTgl_order() {
		return tgl_order;
	}

	public void setTgl_order(Date tgl_order) {
		this.tgl_order = tgl_order;
	}

	public Date getTgl_kirim_est() {
		return tgl_kirim_est;
	}

	public void setTgl_kirim_est(Date tgl_kirim_est) {
		this.tgl_kirim_est = tgl_kirim_est;
	}

	public Date getTgl_kembali() {
		return tgl_kembali;
	}

	public void setTgl_kembali(Date tgl_kembali) {
		this.tgl_kembali = tgl_kembali;
	}

	public Integer getPay_mode() {
		return pay_mode;
	}

	public void setPay_mode(Integer pay_mode) {
		this.pay_mode = pay_mode;
	}

	public String getPay_modeKet() {
		return pay_modeKet;
	}

	public void setPay_modeKet(String pay_modeKet) {
		this.pay_modeKet = pay_modeKet;
	}

	public Integer getFlag_kirim() {
		return flag_kirim;
	}

	public void setFlag_kirim(Integer flag_kirim) {
		this.flag_kirim = flag_kirim;
	}

	public String getFlag_kirimKet() {
		return flag_kirimKet;
	}

	public void setFlag_kirimKet(String flag_kirimKet) {
		this.flag_kirimKet = flag_kirimKet;
	}
	
	public Date getPrint_order_form() {
		return print_order_form;
	}

	public void setPrint_order_form(Date print_order_form) {
		this.print_order_form = print_order_form;
	}

	public Date getPrint_trans_date() {
		return print_trans_date;
	}

	public void setPrint_trans_date(Date print_trans_date) {
		this.print_trans_date = print_trans_date;
	}

	public Date getPrint_faktur_date() {
		return print_faktur_date;
	}

	public void setPrint_faktur_date(Date print_faktur_date) {
		this.print_faktur_date = print_faktur_date;
	}

	public Date getPrint_sj_date() {
		return print_sj_date;
	}

	public void setPrint_sj_date(Date print_sj_date) {
		this.print_sj_date = print_sj_date;
	}

	public Integer getRetail_id_req() {
		return retail_id_req;
	}

	public void setRetail_id_req(Integer retail_id_req) {
		this.retail_id_req = retail_id_req;
	}

	public Double getLimit_hutang() {
		return limit_hutang;
	}

	public void setLimit_hutang(Double limit_hutang) {
		this.limit_hutang = limit_hutang;
	}

	public Integer getSupplier_idNull() {
		return supplier_idNull;
	}

	public void setSupplier_idNull(Integer supplier_idNull) {
		this.supplier_idNull = supplier_idNull;
	}

	public Integer getCustomer_idNull() {
		return customer_idNull;
	}

	public void setCustomer_idNull(Integer customer_idNull) {
		this.customer_idNull = customer_idNull;
	}

	public Integer getSales_idNull() {
		return sales_idNull;
	}

	public void setSales_idNull(Integer sales_idNull) {
		this.sales_idNull = sales_idNull;
	}

	public Integer getReceivedby() {
		return receivedby;
	}

	public void setReceivedby(Integer receivedby) {
		this.receivedby = receivedby;
	}

	public Date getReceiveddate() {
		return receiveddate;
	}

	public void setReceiveddate(Date receiveddate) {
		this.receiveddate = receiveddate;
	}

	public String getRetail_id_reqKet() {
		return retail_id_reqKet;
	}

	public void setRetail_id_reqKet(String retail_id_reqKet) {
		this.retail_id_reqKet = retail_id_reqKet;
	}
	
	public Double getRemain() {
		return remain;
	}

	public void setRemain(Double remain) {
		this.remain = remain;
	}
	
	public Integer getDelivery_id() {
		return delivery_id;
	}

	public void setDelivery_id(Integer delivery_id) {
		this.delivery_id = delivery_id;
	}

	public String getTgl_kirimNull() {
		return tgl_kirimNull;
	}

	public void setTgl_kirimNull(String tgl_kirimNull) {
		this.tgl_kirimNull = tgl_kirimNull;
	}

	public String getDriver_idNull() {
		return driver_idNull;
	}

	public void setDriver_idNull(String driver_idNull) {
		this.driver_idNull = driver_idNull;
	}

	public String getDelivery_idNull() {
		return delivery_idNull;
	}

	public void setDelivery_idNull(String delivery_idNull) {
		this.delivery_idNull = delivery_idNull;
	}

	public Integer getStatus_kirim() {
		return status_kirim;
	}

	public void setStatus_kirim(Integer status_kirim) {
		this.status_kirim = status_kirim;
	}

	public String getStatus_kirimNull() {
		return status_kirimNull;
	}

	public void setStatus_kirimNull(String status_kirimNull) {
		this.status_kirimNull = status_kirimNull;
	}
	public String getNo_trans_ref() {
		return no_trans_ref;
	}

	public void setNo_trans_ref(String no_trans_ref) {
		this.no_trans_ref = no_trans_ref;
	}

	public String getRetur_typeKet() {
		return retur_typeKet;
	}

	public void setRetur_typeKet(String retur_typeKet) {
		this.retur_typeKet = retur_typeKet;
	}

	public Date getTgl_gudang_trans() {
		return tgl_gudang_trans;
	}

	public void setTgl_gudang_trans(Date tgl_gudang_trans) {
		this.tgl_gudang_trans = tgl_gudang_trans;
	}

	public Date getTgl_terima_trans() {
		return tgl_terima_trans;
	}

	public void setTgl_terima_trans(Date tgl_terima_trans) {
		this.tgl_terima_trans = tgl_terima_trans;
	}
	
	public Date getTgl_req_trans() {
		return tgl_req_trans;
	}

	public void setTgl_req_trans(Date tgl_req_trans) {
		this.tgl_req_trans = tgl_req_trans;
	}

	public Double getPersen_ppn() {
		return persen_ppn;
	}

	public void setPersen_ppn(Double persen_ppn) {
		this.persen_ppn = persen_ppn;
	}

	public Double getPpn() {
		return ppn;
	}

	public void setPpn(Double ppn) {
		this.ppn = ppn;
	}

	public Integer getFlag_pajak() {
		return flag_pajak;
	}

	public void setFlag_pajak(Integer flag_pajak) {
		this.flag_pajak = flag_pajak;
	}
	
	
}
