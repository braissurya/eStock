package com.jjtech.estock.persistence;


import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.dao.DataAccessException;

import com.jjtech.estock.model.Category;
import com.jjtech.estock.model.COA;
import com.jjtech.estock.model.ClosingPeriode;
import com.jjtech.estock.model.Customer;
import com.jjtech.estock.model.Delivery;
import com.jjtech.estock.model.DeliveryDet;
import com.jjtech.estock.model.DropDown;
import com.jjtech.estock.model.GroupUser;
import com.jjtech.estock.model.HakAkses;
import com.jjtech.estock.model.Item;
import com.jjtech.estock.model.Karyawan;
import com.jjtech.estock.model.Menu;
import com.jjtech.estock.model.Opname;
import com.jjtech.estock.model.OpnameDet;
import com.jjtech.estock.model.Payment;
import com.jjtech.estock.model.Payroll;
import com.jjtech.estock.model.RepTB;
import com.jjtech.estock.model.Stock;
import com.jjtech.estock.model.StockHist;
import com.jjtech.estock.model.Supplier;
import com.jjtech.estock.model.Trans;
import com.jjtech.estock.model.TransDet;
import com.jjtech.estock.model.TransHist;
import com.jjtech.estock.model.Trx;
import com.jjtech.estock.model.TrxDet;
import com.jjtech.estock.model.User;
import com.jjtech.estock.model.Merk;
import com.jjtech.estock.model.Satuan;
import com.jjtech.estock.model.Warna;
import com.jjtech.estock.model.Config;
import com.jjtech.estock.model.Cabang;
import com.jjtech.estock.model.Bank;
import com.jjtech.estock.model.Account;


/**
 * MyBatis Mapper, sebagai pengganti DAO. Sudah tidak perlu dibuat class implement lagi (cukup interfacenya saja)
 *
 * @author Yusuf
 * @since Jan 23, 2013 (9:27:34 AM)
 *
 */
public interface DbMapper {

	public Date selectSysdate() throws DataAccessException;

	public List<DropDown> selectDropDown(@Param("keycol")String key, @Param("valcol") String value,@Param("tablename") String table,
										@Param("where") String where, @Param("ordercol") String orderby) throws DataAccessException;

	public Integer selectCountTable(@Param("tablename")String table,@Param("where") String where) throws DataAccessException;

	public Date selectMaxDateFromTable(@Param("valcol") String value, @Param("tablename") String table, @Param("where") String where ) throws DataAccessException;

	public Date selectClosingPeriode( @Param("id")Integer id,  @Param("cabang_id")Integer cabang_id) throws DataAccessException;
	public Integer insertClosingPeriode(ClosingPeriode closingPeriode) throws DataAccessException;
	public void updateClosingPeriod(ClosingPeriode closingPeriode) throws DataAccessException;
	
	public User selectUser(String username) throws DataAccessException;

	public List<Menu> selectMenuAccess(@Param("group_user_id")Integer group_user_id,@Param("root")Integer root,@Param("path")String path) throws DataAccessException;

	//interface untuk master user
	public Integer insertUser(User user) throws DataAccessException;
	public void updateUser(User user) throws DataAccessException;
	public List<User> selectAllUser(
			@Param("group_user_id") Integer group_user_id,
			@Param("id") Integer id) throws DataAccessException;
	public List<User> selectListUserPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,
			@Param("group_user_id") Integer group_user_id) throws DataAccessException;
	public Integer selectListUserPagingCount(
			@Param("search") String search,
			@Param("group_user_id") Integer group_user_id) throws DataAccessException;

	//interface untuk master menu
	public Integer insertMenu(Menu menu) throws DataAccessException;
	public void updateMenu(Menu menu) throws DataAccessException;
	public List<Menu> selectListMenu(Integer id) throws DataAccessException;
	public List<Menu> selectListMenuPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListMenuPagingCount(String search) throws DataAccessException;


	//interface untuk master group user
	public void deleteHakAkses(Integer id)throws DataAccessException;
	public void insertHakAkses(GroupUser groupUser) throws DataAccessException;
	public void updateHakAkses(GroupUser groupUser) throws DataAccessException;
	public List<GroupUser> selectListHakAkses(@Param("id") Integer id,
			@Param("menu_id") Integer menu_id,@Param("grouping") Integer grouping,@Param("aktif")Integer aktif) throws DataAccessException;
	public List<GroupUser> selectListHakAksesPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,@Param("grouping")Integer grouping) throws DataAccessException;
	public Integer selectListHakAksesPagingCount(@Param("search")String search,@Param("grouping")Integer grouping) throws DataAccessException;

	public void deleteGroupUser(Integer id)throws DataAccessException;
	public void insertGroupUser(GroupUser groupUser) throws DataAccessException;
	public void updateGroupUser(GroupUser groupUser) throws DataAccessException;
	public List<GroupUser> selectListGroupUser(@Param("id") Integer id,
			@Param("menu_id") Integer menu_id,@Param("grouping") Integer grouping,@Param("aktif")Integer aktif) throws DataAccessException;
	public List<GroupUser> selectListGroupUserPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,@Param("grouping")Integer grouping) throws DataAccessException;
	public Integer selectListGroupUserPagingCount(@Param("search")String search,@Param("grouping")Integer grouping) throws DataAccessException;

	//interface untuk master category
	public Integer insertCategory(Category category) throws DataAccessException;
	public void updateCategory(Category category) throws DataAccessException;
	public List<Category> selectListCategory(Integer id) throws DataAccessException;
	public List<Category> selectListCategoryPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListCategoryPagingCount(@Param("search") String search) throws DataAccessException;
	public Integer selectNamaCategoryCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master supplier
	public Integer insertSupplier(Supplier supplier) throws DataAccessException;
	public void updateSupplier(Supplier supplier) throws DataAccessException;
	public List<Supplier> selectListSupplierAuto(@Param("nama_auto") String nama_auto,@Param("nama") String nama,
			@Param("id") Integer id,@Param("kode_auto")String kode_auto,@Param("kode")String kode)throws DataAccessException;
	public List<Supplier> selectListSupplier(Integer id) throws DataAccessException;
	public List<Supplier> selectListSupplierPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListSupplierPagingCount(@Param("search") String search) throws DataAccessException;
	public Integer selectNamaSupplierCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master item
	public Integer insertItem(Item item) throws DataAccessException;
	public void updateItem(Item item) throws DataAccessException;
	public List<Item> selectListItemPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type, @Param("cabang_id") Integer cabang_id) throws DataAccessException;
	public Integer selectItemPagingCount(@Param("search") String search) throws DataAccessException;
	public List<Item> selectListItem(@Param("barcode_ext_auto") String barcode_ext_auto,@Param("barcode_ext") String barcode_ext,
			@Param("id") Integer id,@Param("nama")String nama,@Param("nama_complete")String nama_complete,@Param("cabang_id")Integer cabang_id) throws DataAccessException;
	public Integer selectNamaItemCount(@Param("search") String search) throws DataAccessException;
	
	//interface untuk trans
	public Integer insertTrans(Trans trans) throws DataAccessException;
	public void updateTrans(Trans trans) throws DataAccessException;
	public List<Trans> selectListTransAuto(@Param("no_trans_auto") String no_trans_auto, @Param("no_trans") String no_trans, @Param("jenis") Integer jenis, @Param("posisi_id") Integer posisi_id )throws DataAccessException;
	public Integer insertTransDet(TransDet transDet) throws DataAccessException;
	public void updateTransDet(TransDet transDet) throws DataAccessException;
	public void deleteTransDet(@Param("trans_id")Integer trans_id,@Param("item_id") Integer item_id) throws DataAccessException;
	public Integer insertTransHist(TransHist transHist) throws DataAccessException;
	public void updateTransHist(TransHist transHist) throws DataAccessException;
	public List<Trans> selectListTrans(@Param("jenis") Integer jenis,
			@Param("posisi_id") Integer posisi_id,@Param("trans_id") Integer trans_id,@Param("no_trans")String no_trans,@Param("retail_id") Integer retail_id) throws DataAccessException;
	public List<Trans> selectListTransPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,@Param("jenis") Integer jenis,
			@Param("posisi_id") Integer posisi_id,@Param("trans_id") Integer trans_id,@Param("no_trans")String no_trans,@Param("retail_id") Integer retail_id,@Param("approval") Integer approval) throws DataAccessException;
	public Integer selectListTransPagingCount(@Param("search")String search,@Param("jenis") Integer jenis,
			@Param("posisi_id") Integer posisi_id,@Param("trans_id") Integer trans_id,@Param("no_trans")String no_trans,@Param("retail_id") Integer retail_id,@Param("approval") Integer approval) throws DataAccessException;

	public List<TransDet> selectListTransDet(@Param("trans_id") Integer trans_id,
			@Param("urut") Integer urut,@Param("barcode_ext") String barcode_ext,@Param("item_id") Integer item_id,@Param("lsitem_id") List<Integer> lsitem_id) throws DataAccessException;

	//interface untuk stock
	public Integer insertStock(Stock stock) throws DataAccessException;
	public void updateStock(Stock stock) throws DataAccessException;
	public Integer insertStockHist(StockHist stockHist) throws DataAccessException;
	public void updateStockHist(StockHist stockHist) throws DataAccessException;

	//interface untuk master customer
	public Integer insertCustomer(Customer customer) throws DataAccessException;
	public void updateCustomer(Customer customer) throws DataAccessException;
	public List<Customer> selectListCustomerAuto(@Param("nama_auto") String nama_auto,@Param("nama") String nama,
			@Param("id") Integer id,@Param("kode_auto")String kode_auto,@Param("kode")String kode)throws DataAccessException;
	public List<Customer> selectListCustomer(Integer id) throws DataAccessException;
	public List<Customer> selectListCustomerPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListCustomerPagingCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master Karyawan
	public Integer insertKaryawan(Karyawan karyawan) throws DataAccessException;
	public void updateKaryawan(Karyawan karyawan) throws DataAccessException;
	public List<Karyawan> selectListKaryawanAuto(@Param("nama_auto") String nama_auto,@Param("nama") String nama,
			@Param("id") Integer id,@Param("nik_auto")String nik_auto,@Param("nik")String nik,@Param("jenis")Integer jenis)throws DataAccessException;
	public List<Karyawan> selectListKaryawan(@Param("id") Integer id,@Param("jenis")Integer jenis) throws DataAccessException;
	public List<Karyawan> selectListKaryawanPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,@Param("jenis")Integer jenis) throws DataAccessException;
	public Integer selectListKaryawanPagingCount(@Param("search") String search,@Param("jenis")Integer jenis) throws DataAccessException;
	public Integer selectNamaKaryawanCount(@Param("search") String search) throws DataAccessException;

	//interface untuk Berita Acara Gudang
	public Integer insertOpname(Opname opname) throws DataAccessException;
	public Integer insertOpnameDet(OpnameDet opnameDet) throws DataAccessException;
	public void updateOpname(Opname opname) throws DataAccessException;
	public void updateOpnameDet(OpnameDet opnamedet) throws DataAccessException;
	public List<Item> selectListOpnamePaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,
			@Param("cabang_id") Integer cabang_id) throws DataAccessException;
	public Integer selectOpnamePagingCount(@Param("search") String search, @Param("cabang_id") Integer cabang_id) throws DataAccessException;
	public List<Opname> selectListOpname(@Param("where") String where )throws DataAccessException;
	public List<OpnameDet> selectListOpnameDet(@Param("opname_id") Integer opname_id)throws DataAccessException;
	public List<OpnameDet> selectListOpnameDetFromStock(@Param("cabang_id") Integer cabang_id) throws DataAccessException;

	//interface untuk master merk
	public Integer insertMerk(Merk merk) throws DataAccessException;
	public void updateMerk(Merk merk) throws DataAccessException;
	public List<Merk> selectListMerk(Integer id) throws DataAccessException;
	public List<Merk> selectListMerkPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListMerkPagingCount(@Param("search") String search) throws DataAccessException;
	public Integer selectListMerkNamaCount(@Param("search") String search) throws DataAccessException;
	public Integer selectNamaMerkCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master satuan
	public Integer insertSatuan(Satuan satuan) throws DataAccessException;
	public void updateSatuan(Satuan satuan) throws DataAccessException;
	public List<Satuan> selectListSatuan(Integer id) throws DataAccessException;
	public List<Satuan> selectListSatuanPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListSatuanPagingCount(@Param("search") String search) throws DataAccessException;
	public Integer selectNamaSatuanCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master warna
	public Integer insertWarna(Warna warna) throws DataAccessException;
	public void updateWarna(Warna warna) throws DataAccessException;
	public List<Warna> selectListWarna(Integer id) throws DataAccessException;
	public List<Warna> selectListWarnaPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListWarnaPagingCount(@Param("search") String search) throws DataAccessException;
	public Integer selectNamaWarnaCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master config
	public Integer insertConfig(Config config) throws DataAccessException;
	public void updateConfig(Config config) throws DataAccessException;
	public List<Config> selectListConfig(HashMap<String, Object> map) throws DataAccessException;
	public List<Config> selectListConfigPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListConfigPagingCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master cabang
	public Integer insertCabang(Cabang cabang) throws DataAccessException;
	public void updateCabang(Cabang cabang) throws DataAccessException;
	public List<Cabang> selectListCabang(Integer id) throws DataAccessException;
	public List<Cabang> selectListCabangPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListCabangPagingCount(@Param("search") String search) throws DataAccessException;
	public Integer selectNamaCabangCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master bank
	public Integer insertBank(Bank bank) throws DataAccessException;
	public void updateBank(Bank bank) throws DataAccessException;
	public List<Bank> selectListBank(Integer id) throws DataAccessException;
	public List<Bank> selectListBankPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListBankPagingCount(@Param("search") String search) throws DataAccessException;
	public Integer selectNamaBankCount(@Param("search") String search) throws DataAccessException;

	//interface untuk master account
	public Integer insertAccount(Account account) throws DataAccessException;
	public void updateAccount(Account account) throws DataAccessException;
	public List<Account> selectListAccount(Integer id) throws DataAccessException;
	public String selectAccountName(Integer id) throws DataAccessException;
	public List<Account> selectListAccountPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListAccountPagingCount(@Param("search")String search) throws DataAccessException;
	public Integer selectNamaAccountCount(@Param("search") String search) throws DataAccessException;

	public Stock selectStock(@Param("cabang_id") Integer cabang_id, @Param("item_id") Integer  item_id) throws DataAccessException;
	public List<Stock> selectListStockItem(
			@Param("barcode_ext") String barcode_ext,
			@Param("cabang_id") Integer cabang_id,@Param("periode") Date  periode) throws DataAccessException;
	
	public List<Payroll> selectListStockItemPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,
			@Param("barcode_ext") String barcode_ext,
			@Param("cabang_id") Integer cabang_id,@Param("periode") Date  periode) throws DataAccessException;
	public Integer selectListStockItemPagingCount(@Param("search") String search,
			@Param("barcode_ext") String barcode_ext,
			@Param("cabang_id") Integer cabang_id,@Param("periode") Date  periode) throws DataAccessException;
	
	
	public Double selectHutangCustomer(Integer customer_id)throws DataAccessException;
	
	
	//interface untuk delivery
	public Integer insertDelivery(Delivery delivery) throws DataAccessException;
	public void updateDelivery(Delivery delivery) throws DataAccessException;
	public List<Delivery> selectListDelivery(@Param("id")Integer id,@Param("posisi_id")Integer posisi_id,@Param("kode")String kode) throws DataAccessException;
	public List<Delivery> selectListDeliveryPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type, @Param("id") Integer id,@Param("posisi_id")Integer posisi_id) throws DataAccessException;
	public Integer selectListDeliveryPagingCount(@Param("search") String search, @Param("id") Integer id,@Param("posisi_id")Integer posisi_id) throws DataAccessException;
	
	public Integer insertDeliveryDet(DeliveryDet deliveryDet) throws DataAccessException;
	public void updateDeliveryDet(DeliveryDet deliveryDet) throws DataAccessException;
	public void deleteDeliveryDet(@Param("delivery_id") Integer delivery_id,@Param("trans_id") Integer trans_id) throws DataAccessException;
	public List<DeliveryDet> selectListDeliveryDet(@Param("delivery_id") Integer delivery_id,@Param("trans_id") Integer trans_id) throws DataAccessException;

	//interface untuk payroll
	public Integer insertPayroll(Payroll payroll) throws DataAccessException;
	public void updatePayroll(Payroll payroll) throws DataAccessException;
	public List<Payroll> selectListPayroll(@Param("id")Integer id,@Param("karyawan_id")Integer posisi_id,@Param("periode")Date periode,@Param("nik")String nik) throws DataAccessException;
	public List<Payroll> selectListPayrollPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type, @Param("periode") Date periode) throws DataAccessException;
	public Integer selectListPayrollPagingCount(@Param("search") String search, @Param("periode") Date periode) throws DataAccessException;
	
	public List<COA> selectListCoa(@Param("id") Integer id,@Param("pl") Integer pl,@Param("active") Integer active,@Param("level") Integer level,
			@Param("post") Integer post,@Param("parent") String parent)throws DataAccessException;

	public Double selectSumTrx(@Param("dk") String dk, @Param("periode") Date periode)throws DataAccessException;
	
	public void updateRepTB(RepTB repTB)throws DataAccessException;
	public void insertRepTB(RepTB repTB)throws DataAccessException;

	
	//interface untuk master payment
	public Integer insertPayment(Payment payment) throws DataAccessException;
	public void updatePayment(Payment payment) throws DataAccessException;
	public List<Payment> selectListPayment(@Param("payment_id") Integer payment_id, @Param("no_payment") String no_payment, @Param("trans_id") Integer trans_id) throws DataAccessException;
	public List<Trans> selectListPaymentPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,@Param("jenis") Integer jenis,
			@Param("posisi_id") Integer posisi_id,@Param("payment_id") Integer payment_id,@Param("no_payment")String no_payment) throws DataAccessException;
	public Integer selectListPaymentPagingCount(@Param("search")String search,@Param("jenis") Integer jenis,
			@Param("posisi_id") Integer posisi_id,@Param("payment_id") Integer payment_id,@Param("no_payment")String no_payment) throws DataAccessException;
	
	//interface untuk master trx
	public Integer insertTrx(Trx trx) throws DataAccessException;
	public void updateTrx(Trx trx) throws DataAccessException;
	public List<Trx> selectListTrx(@Param("trx_id") Integer trx_id, @Param("posisi_id") Integer posisi_id )throws DataAccessException;
	public List<Trx> selectListTrxPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type,
			@Param("posisi_id") Integer posisi_id, @Param("trx_id") Integer trx_id) throws DataAccessException;
	public Integer selectListTrxPagingCount(@Param("search")String search,
			@Param("posisi_id") Integer posisi_id, @Param("trx_id") Integer trx_id) throws DataAccessException;
	public Integer insertTrxDet(TrxDet trxDet) throws DataAccessException;
	public void deleteTrxDet(Integer trx_id)throws DataAccessException;
	public List<TrxDet> selectListTrxDet(@Param("trx_id") Integer trx_id )throws DataAccessException;
	
	//interface untuk master COA
	public void insertCOA(COA coa) throws DataAccessException;
	public void updateCOA(COA coa) throws DataAccessException;
	public List<COA> selectListCOA(String id) throws DataAccessException;
	public List<COA> selectListCOAPaging(
			@Param("search") String search,
			@Param("offset") Integer offset, @Param("rowcount") Integer rowcount,
			@Param("sort") String sort, @Param("sort_type") String sort_type) throws DataAccessException;
	public Integer selectListCOAPagingCount(@Param("search") String search) throws DataAccessException;
	public Integer selectNamaCOACount(@Param("search") String search) throws DataAccessException;
		
	//interface untuk master coa
	public List<COA> selectListCoaAuto( @Param("coa_id_auto") String coa_id_auto, @Param("coa_id") String coa_id, @Param("nama_auto") String nama_auto, @Param("nama") String nama )throws DataAccessException;
}
