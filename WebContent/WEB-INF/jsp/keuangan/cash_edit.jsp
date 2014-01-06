<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
		$(document).ready(function(){
			var path="${path}";
			//pesan error diletakkan di alert juga
			var message = "${message}".replace(/<br\s*[\/]?>/gi, "\n");
			if(message != ''){
			 	alert(message);
			}
			
			function replaceComma(nilai){
				nilai=nilai.replace(/[^0-9\.]+/g,"");
				if(isNaN(nilai)){
					result=0;
				}else{
					var result=nilai;
					if(result=="")result=nilai;
				}
				//alert(result);
				return result;
			}
			
			$('#id_cara_bayar').change(function() {
				if($('#id_cara_bayar').val()==1){
					$('#account_id').val(0);
				}else{
					$('#account_id').find('option:first').attr('selected', 'selected');
				}
				
				if($('#id_cara_bayar').val()==3){
					$('#td_no_giro').show();
				}else{
					$('#td_no_giro').hide();
					$('#no_giro').val('');
				}
			});
			
			if($('#id_cara_bayar').val()==1){
				$("#account_id").val(0);
			}
			
			if($("#no_giro").val() == ''){
				$("#id_cara_bayar").change();
			}
			
			if($("#no_payment").val() == ''){
				$('#transferdiv').hide();
			}
			
			$("#no_trans").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteTrans?dk="+$("#dk").val(),
					data: {
						no_trans: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.no_trans,
								value: item.no_trans,
								nama : item.no_trans,
								id: item.no_trans,
								total_harga: item.total_harga
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#no_trans').val(ui.item.no_trans);
					$('#nominal').val(ui.item.total_harga);
				}
			});
			
			$('#no_trans').live('keyup', function(e) {
				if($(this).val().length>2){
					$.getJSON(path+'/json/completeTrans?no_trans='+$(this).val()+'&dk='+$("#dk").val(), function(data) {
						if(data!=""){
							$('#nominal').val(data[0].total_harga);
						}else{
							$('#nominal').val("");
						}
					});
				}
				return false;
				
			});
			
			$('.add_new').click(function() {
			/* alert(path+'/json/completeTrans?no_trans=0010513TB00001&dk=D'); */
				$.getJSON(path+'/json/completeTrans?no_trans='+$("#no_trans").val()+'&dk='+$("#dk").val(), function(data) {
					if(data!=""){
						$('#nominal').val(formatNumber(data[0].total_harga));
					}else{
						$('#nominal').val("");
					}
				});
			});
		});
	</script>
</head>
<body>
	<div style="float: right; padding-top: 10px;padding-right: 10px;">
		<a href="${path}/keuangan/Cash/${payment.jenispayment}/new" title="Add ${payment.jenispayment}"><img title="Add ${payment.jenispayment}" alt="add ${payment.jenispayment}" src="${path }/static/images/icons/addlist.png"></a>
	 	<a href="javascript:doAction('${path}/keuangan/Cash/${payment.jenispayment}', 'List ${payment.jenispayment}',1000,500);" title="List ${payment.jenispayment}"><img alt="list ${payment.jenispayment}" title="List ${payment.jenispayment}" src="${path }/static/images/icons/list.png"></a>
	</div>
	<h1>Input Transaksi ${payment.pagename } &gt; ${payment.mode }</h1>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning'  or messageType eq 'done' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${payment.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>

		   </c:if>
   		</c:otherwise>
   	</c:choose>
    <form:form commandName="payment" name="formpost" method="POST" action="${path}/keuangan/Cash/save" cssClass="form" >
    	<input type="hidden" name="message" id="message" value="${message }">
    	<div style="float: right;">
			<fieldset>
				<legend>History Tanggal</legend>
				 <table  class="nostyle">
			   		<tr>
			  			<th>
			  				Tanggal ${payment.jenispay }
			  			</th>
			  			<th>
			  				:
			  			</th>
				   		<td>
				   			<fmt:formatDate value="${payment.createdate }" pattern="dd-MM-yyyy HH:mm:ss"/>
				   			<form:hidden path="createdate"/>
				   		</td>
			   		</tr>
			   	</table>
			</fieldset>
		</div>
		<table  class="nostyle">
			<tr>
		   		<td>
				    <div class="group">
						<div class="fieldWithErrors">
							<form:label path="no_payment"  cssClass="label" cssErrorClass="label labelError">No Transaksi ${payment.pagename}</form:label>
						    <form:errors path="no_payment" cssClass="error" />
						</div>
							<form:hidden path="no_payment" /><input type="text" readonly="readonly" class="text_field read" value="${payment.no_payment}" size="20"/>
						<span class="description"></span>
				    </div>
		   		</td>
		   		<td>
		   			<div class="group">

				    </div>
		   		</td>
		   	</tr>
		   	<tr id="tr_no_trans">
		   		<td>
				   <div class="group">
						<div class="fieldWithErrors">
							<form:label path="no_trans"  cssClass="label" cssErrorClass="label labelError">No Transaksi Penjualan/Pembelian</form:label>
						    <form:errors path="no_trans" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${payment.mode eq 'VIEW'}"><form:hidden path="no_trans" /><input type="text" class="text_field read" value="${payment.no_trans}" size="20"/></c:when>
							<c:otherwise>
								<input type="text" class="text_field" name="no_trans" id="no_trans" value="${payment.no_trans}" />
								<c:if test="${payment.jenispayment eq \"In\"}">
							 		<a href="javascript:doAction('${path}/keuangan/Cash/inListTrans', 'Find Transaksi Penjualan',1000,500,false);" title="Find Transaksi Penjualan"><img alt="Find Transaksi Penjualan" title="Find Transaksi Penjualan" src="${path }/static/images/icons/findlist.png"></a>
							 	</c:if>
							 	<c:if test="${payment.jenispayment eq \"Out\"}">
							 		<a href="javascript:doAction('${path}/keuangan/Cash/outListTrans', 'Find Transaksi Pembelian',1000,500,false);" title="Find Transaksi Pembelian"><img alt="Find Transaksi Pembelian" title="Find Transaksi Pembelian" src="${path }/static/images/icons/findlist.png"></a>
							 	</c:if>
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
				    </div>
		   		</td>
		   		<td>
					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="cara_bayar"  cssClass="label" cssErrorClass="label labelError">Cara Bayar</form:label>
						    <form:errors path="cara_bayar" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${payment.mode eq 'VIEW'}"><form:hidden path="cara_bayar" id="id_cara_bayar" /><input type="text" class="text_field read" value="${payment.cara_bayarKet}" readonly="readonly" size="20"/></c:when>
							<c:otherwise>
								<form:select path="cara_bayar" id="id_cara_bayar" items="${reff.lsCaraBayar}" itemLabel="value" itemValue="key"></form:select>
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
				    </div>
				</td>
				<td id="td_no_giro">
					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="no_giro"  cssClass="label" cssErrorClass="label labelError">No Giro</form:label>
						    <form:errors path="no_giro" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${payment.mode eq 'VIEW'}"><form:hidden path="no_giro" /><input type="text" class="text_field read" value="${payment.no_giro}" size="15"/></c:when>
							<c:otherwise>
								<input type="text" class="text_field" name="no_giro" id="no_giro" value="${payment.no_giro}" />
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
				    </div>
		   		</td>
		   	</tr>
		   	<tr>
		   		<td>
					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="account_id"  cssClass="label" cssErrorClass="label labelError">Nama Bank</form:label>
						    <form:errors path="account_id" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${payment.mode eq 'VIEW'}"><form:hidden path="account_id" /><input type="text" class="text_field read" value="${payment.account_idKet}" readonly="readonly" size="50"/></c:when>
							<c:otherwise>
								<form:select path="account_id" items="${reff.lsAccount}" itemLabel="value" itemValue="key"></form:select>
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
				    </div>
				</td>
				<td>
				   <div class="group">
						<div class="fieldWithErrors">
							<form:label path="nominal"  cssClass="label" cssErrorClass="label labelError">Total ${payment.pagename}</form:label>
						    <form:errors path="nominal" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${payment.mode eq 'VIEW'}"><form:hidden path="nominal" /><input style="text-align: right;"  type="text" class="text_field nominal" value="<fmt:formatNumber pattern="#,##0">${payment.nominal}</fmt:formatNumber>" size="15"/></c:when>
							<c:otherwise>
								<input type="text" class="text_field nominal" name="nominal" id="nominal" value="<fmt:formatNumber pattern="#,##0">${payment.nominal}</fmt:formatNumber>" />
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
				    </div>
		   		</td>
		   	</tr>
		   	<tr>
		   		
		   	</tr>
		   	<tr>
		   		<td>
					<div class="group">
					    <div class="fieldWithErrors">
						    <form:label path="keterangan"  cssClass="label" cssErrorClass="label labelError">Keterangan</form:label>
							<form:errors path="keterangan" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${payment.mode eq 'VIEW'}"><form:hidden path="keterangan" /> <textarea >${payment.keterangan}</textarea></c:when>
							<c:otherwise>
								<form:textarea maxlength="200"  rows="4" path="keterangan"/>
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
					</div>
				</td>
		   	</tr>
		</table>
    	<div class="group navform wat-cf">
		  	<c:choose>
				<c:when test="${payment.mode eq 'VIEW'}">
				</c:when>
				<c:otherwise>
					<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
	                  </button>
	                  <span class="text_button_padding"></span>
	                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}'">
	                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
	                </button>
	                  <form:hidden path="mode"/>
	                  <form:hidden path="jenispayment"/>
	                  <form:hidden path="jenispay"/>
	                  <form:hidden path="payment_id"/>
	                  <form:hidden path="pagename"/>
	                  <form:hidden path="flag_jenis"/>
	                  <form:hidden path="dk"/>
	                  <span class="text_button_padding"></span>
	                 <div id="transferdiv">
	                 	 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Kwitansi?'))doAction('${path}/report/print_kwitansi/${payment.payment_id}', 'Print Kwitansi',900,500, false);">
					     	<img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Kwitansi" /> Print Kwitansi
					     </button>
		                 <button class="button" type="button" id="transfer" onclick="if(confirm('Are you sure want to transfer?'))window.location='${path}/keuangan/Cash/transfer/${payment.jenispayment}/${payment.payment_id}/${payment.mode }'">
			             	<img src="${path }/static/decorator/main/pilu/images/icons/arrow-right.gif" alt="Transfer" /> Transfer
			             </button>
	                 </div>
					<div style="visibility: hidden;" >
						<button class="button add_new" type="button" >
				    	<img src="${path }/static/decorator/main/pilu/images/icons/add.gif" alt="Save" /> Add
					</button>
					<span class="text_button_padding"></span>
	                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}'">
	                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
	                </button>
					</div>
					
		             
				</c:otherwise>
		   </c:choose>
	               
	    </div>
    </form:form>
</body>
</html>