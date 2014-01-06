<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>

<script type="text/javascript">
   
    $(document).ready(function() {
		var path = "${path}";
		//pesan error diletakkan di alert juga
		var message = "${message}".replace(/<br\s*[\/]?>/gi, "\n");
		if (message != '') {
		    alert(message);
		}
	
		function replaceComma(nilai) {
		    nilai = nilai.replace(/[^0-9\.]+/g, "");
		    if (isNaN(nilai)) {
			result = 0;
		    } else {
			var result = nilai;
			if (result == "")
			    result = nilai;
		    }
		    //alert(result);
		    return result;
		}
	
		function syncPrice(triger) {
			
		    var grandTotal = parseFloat(replaceComma($('#total').val()));
		    var diskon = 0.0;
		    var persen_diskon = 0.0;
		    var nilaippn = 0.0;
		    var persen_nilaippn = 0.0;
			
		    if ("persen_disc" == triger) {
				persen_diskon = parseFloat(replaceComma($('#persen_disc').val()));
				persen_nilaippn = parseFloat(replaceComma($('#persen_ppn').val()));
				diskon = (grandTotal * persen_diskon) / 100;
				nilaippn = ((grandTotal - diskon) * persen_nilaippn) / 100;
				$('#total_disc').val(formatNumber(diskon));
				$('#ppn').val(formatNumber(nilaippn));
				$('#ppn_span').text(formatNumber(nilaippn));
		    } else if ("total_disc" == triger) {
				diskon = parseFloat(replaceComma($('#total_disc').val()));
				persen_nilaippn = parseFloat(replaceComma($('#persen_ppn').val()));
				persen_diskon = (diskon / grandTotal) * 100;
				nilaippn = ((grandTotal - diskon) * persen_nilaippn) / 100;
				$('#persen_disc').val(formatCurrency(persen_diskon));
				$('#ppn').val(formatNumber(nilaippn));
				$('#ppn_span').text(formatNumber(nilaippn));
		    } 
		    $('#totalHarga').val(formatNumber(grandTotal - diskon + nilaippn));
		    $('.grandTotal').text(formatNumber(grandTotal - diskon + nilaippn));
		}
	
		
	
		$("#table_wrapper").delegate('input:text', 'keyup', function(e) {
		    syncPrice($(this).attr("id"));
		});
	
		
	
		$("#table_wrapper").delegate('input:text.nominal', 'blur', function() {
		    $(this).formatCurrency({
			colorize : true,
			negativeFormat : '-%s%n',
			roundToDecimalPlace : 0
		    });
		});

    });

    function saveAja() {
	if (confirm('Are you sure want to save?')) {
	    if ("${closingWarning}" == "false") {
		if (!confirm("Stock belum di closing. Apakah transaksi ingin dimasukkan ke periode sebelumnya?")) {

		} else {
		    formpost.submit();
		}
	    } else {
		formpost.submit();
	    }
	} else {

	}
    }
</script>
</head>
<body>
	<h1>Approval Transaksi</h1>
	<c:choose>
		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
			<p class="msg ${messageType}">${message }</p>
		</c:when>
		<c:otherwise>
			<p class="msg info">
				<fmt:message key="CompleteForm" />
			</p>
		</c:otherwise>
	</c:choose>
	<form:form commandName="trans" name="formpost" method="POST" cssClass="form" action="${path }/transaksiApproval/save">

		<div class="space"></div>
		<h3 class="tit">Data Customer</h3>
		<table class="nostyle">
			<tr>
				<th>Kode Customer</th>
				<th>:</th>
				<td>${trans.customer.kode }</td>
			</tr>
			<tr>
				<th>Nama Customer</th>
				<th>:</th>
				<td>${trans.customer.nama }</td>
			</tr>
			<tr>
				<th>Limit Hutang</th>
				<th>:</th>
				<td>
					Rp
					<fmt:formatNumber pattern="#,##0">${trans.customer.limit_hutang}</fmt:formatNumber>
				</td>
			</tr>
			<tr>
				<th>Total Hutang</th>
				<th>:</th>
				<td>
					Rp
					<fmt:formatNumber pattern="#,##0">${trans.customer.totalHutang}</fmt:formatNumber>
				</td>
			</tr>
		</table>
		<div class="space"></div>
		<h3 class="tit">Data Transaksi</h3>
		<div id="table_wrapper" style="height: 350px; overflow-x: auto; overflow-y: none;">

			<table width="100%" class="tbl_repeat">
				<thead>
					<tr>
						<th>No.</th>
						<th>Kode Produk</th>
						<th>Nama Produk</th>
						<th class="right">@Harga</th>
						<th class="right" colspan="2">@Diskon</th>
						<th class="right">Qty</th>
						<th>Satuan</th>
						<th class="right">Sub Total Harga</th>
						<th></th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${trans.lsTransDet }" var="t">
						<tr>

							<td>${t.urut}<input type="hidden" name="idx" id="idx" value="${t.urut}" title="${t.urut}" />
							</td>
							<td>${t.barcode_ext }<input type="hidden" name="item_id_${t.urut}" id="item_id_${t.urut}" value="${t.item_id }" title="${t.urut}" />
								<input type="hidden" name="barcode_ext_${t.urut}" value="${t.barcode_ext }" title="${t.urut}" />
							</td>
							<td>${t.item_idKet}<input type="hidden" name="nama_${t.urut}" id="nama_${t.urut}" value="${t.item_idKet}" title="${t.urut}" />
							</td>
							<td class="right">
								<fmt:formatNumber pattern="#,##0">${t.harga}</fmt:formatNumber>
								<input type="hidden" name="harga_${t.urut}" id="harga_${t.urut}" value="${t.harga}" title="${t.urut}" />
							</td>
							<td class="right" colspan="2">
								<fmt:formatNumber pattern="###0.00">${t.persen_diskon}</fmt:formatNumber>
								% (
								<fmt:formatNumber pattern="#,##0">${t.jumlah_diskon}</fmt:formatNumber>
								)
								<input type="hidden" name="persen_diskon_${t.urut}" id="persen_diskon_${t.urut}" value="<fmt:formatNumber pattern="###0.00">${t.persen_diskon}</fmt:formatNumber>"    title="${t.urut}"/>%
								<input type="hidden"  name="diskon_${t.urut}" id="diskon_${t.urut}" value="<fmt:formatNumber pattern="#,##0">${t.jumlah_diskon}</fmt:formatNumber>"   title="${t.urut}"/>)
								
							</td>
							<td class="right">${t.qty}
								<input type="hidden"  id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}"   title="${t.urut}"/>
							</td>
							<td>${t.satuan_idKet}<input type="hidden" name="satuan_${t.urut}" id="satuan_${t.urut}" value="${t.satuan_idKet}" title="${t.urut}" />
							</td>

							<td class="right">
								<span id="subtotal_${t.urut}" class="subTotal">
									<fmt:formatNumber pattern="#,##0">${t.subTotal_harga}</fmt:formatNumber>
								</span>
								<input type="hidden" name="subTotal_harga_${t.urut}" id="subTotal_harga_${t.urut}" value="${t.subTotal_harga}" title="${t.urut}" />
							</td>
							<td style="width: 30px;">
								<a href="#" class="remove" rel="${t.urut}"> <img src="${path }/static/decorator/main/pilu/images/icons/delete.png" alt="Delete" />
								</a>
							</td>
						</tr>
					</c:forEach>


				</tbody>
				<tfoot>
					<tr>
						<td colspan="8" class="right subTotal">Total Harga</td>
						<td class="right">
							<span class="Total subTotal">
								<fmt:formatNumber pattern="#,##0">${trans.sub_total_harga}</fmt:formatNumber>
							</span>
							<input type="hidden" name="total" id="total" value="<fmt:formatNumber pattern="#,##0">${trans.sub_total_harga}</fmt:formatNumber>" />
							
						</td>
						<td></td>
					</tr>
					<tr>
						<td colspan="8" class="right">Diskon Tambahan</td>
						<td class="right">
							<input type="text" name="persen_disc" id="persen_disc" size="4" value="<fmt:formatNumber pattern="###0.00">${trans.persen_disc}</fmt:formatNumber>" class="text_field number" />
							% (
							<input type="text" name="total_disc" id="total_disc" value="<fmt:formatNumber pattern="#,##0">${trans.total_disc}</fmt:formatNumber>" size="10" class="text_field nominal" style="text-align: right;" />
							)
						</td>
						<td></td>
					</tr>
					<tr>
						<td colspan="8" class="right">PPN</td>
						<td class="right">
							<span id="persen_ppn_span" class="Total subTotal" style="padding-right: 70px;">
								<fmt:formatNumber pattern="###0.00">${trans.persen_ppn}</fmt:formatNumber>
								%
							</span>
							<span id="ppn_span" class="Total subTotal">
								<fmt:formatNumber pattern="#,##0">${trans.ppn}</fmt:formatNumber>
							</span>
							<input type="hidden" name="persen_ppn" id="persen_ppn" value="<fmt:formatNumber pattern="###0.00">${trans.persen_ppn}</fmt:formatNumber>" />
							<input type="hidden" name="ppn" id="ppn" value="<fmt:formatNumber pattern="#,##0">${trans.ppn}</fmt:formatNumber>" />
						</td>
						<td></td>
					</tr>
					<tr>
						<th colspan="8" class="grandtotal right">Grand Total</th>
						<th class="grandtotal right">
							<span class="grandTotal">
								<fmt:formatNumber pattern="#,##0">${trans.total_harga}</fmt:formatNumber>
								
							</span>
							<input type="hidden" name="totalHarga" id="totalHarga" value="<fmt:formatNumber pattern="#,##0">${trans.total_harga}</fmt:formatNumber>" />
						</th>
						<th></th>
					</tr>
				</tfoot>
			</table>





		</div>

		<br />
		<br />
		<div class="group navform wat-cf">

			<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
				<img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Approved" /> Approved
			</button>
			<form:hidden path="no_trans" />
			<form:hidden path="jenis"/>
			<form:hidden path="trans_id"/>
			<span class="text_button_padding"></span>
			<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/transaksiApproval/list'">
				<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			</button>


		</div>

	</form:form>
</body>