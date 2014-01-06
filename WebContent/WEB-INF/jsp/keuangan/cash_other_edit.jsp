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
			
			$("#table_wrapper").delegate('input:text', 'keyup', function(e){
					
				  syncPrice($(this).attr("id"));
		    }); 
		    
		    $("#table_wrapper").delegate('input:text.nominal', 'blur', function(){	 
				  $(this).formatCurrency({ colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: 0});
		    });
			
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
			
			$('#coa_id,#nama').keypress(function(e) {
				if (e.keyCode == '13') {
			  		e.preventDefault();
			   	  	addRowProduct($(this));
			  	}
			});
			
			$('.add_new').click(function() {
				addRowProduct($(this));
			});
			
			function addRowProduct(type){
// 				if($('#coa_id').val()=='') alert('Kode Jurnal Belum diisi');
// 				else if($('#nominal').val()=='') alert('Jumlah Belum diisi');
// 				else if($('#nama').val()=='') alert('Nama Transaksi Belum diisi');
// 				else{
					var arr = type.closest('form').serializeArray();
					var tbl = $('.tbl_repeat tr').length;
					$.getJSON('${path}/json/addRowCashOther?tbl='+tbl, arr, function(data) {
						var len = data.length;
						
						if(data[0].desc=="ga"){
							alert("Kode Jurnal : "+$('#coa_id').val()+" tidak ditemukan");
						}else{
							
							var rows = $('.tbl_repeat tr').length;
							for (var i = 0; i< len; i++) {						
								$('#table_wrapper').html(
									$(data[i].value).hide().fadeIn(300)
								);
							}
						}
					}, 'json');
// 				}
				
				$(".kode").focus();
				return false;
			}
			
			$('.remove').live('click', function() {
				var trigger = $(this);
				var arr = $(this).closest('form').serializeArray();
				var id = $(this).attr('rel');
				$.getJSON('${path}/json/removeRowCashOther?id='+id, arr, function(data) {
					var len = data.length;
					var rows = $('.tbl_repeat tr').length;
					for (var i = 0; i< len; i++) {
						//if (tbl === 0) {
							$('#table_wrapper').html(
								$(data[i].value).hide().fadeIn(300)
							);
						/* //} else {
							$('.tbl_repeat tr:last').after(
								$(data[i].value).hide().fadeIn(300)
									.css('display', 'table-row')
							);
						} */
					}
				});
				$(".kode").focus();
				return false;
			});
			
			
			$('#coa_id').live('keyup', function(e) {
				if($(this).val().length>2){
					
					$.getJSON('${path}/json/completeCoa?coa_id='+$(this).val(), function(data) {
						if(data!=""){
							$('#nama').val(data[0].nama);
							$('#dk').val(data[0].dk);
							$('#nominal').val("");
						}else{
							$('#nama').val("");
							$('#dk').val("");
						}
					});				
				}
				return false;
			}); 
			
			$('#nama').live('keyup', function(e) {
				
				if($(this).val().length>2){
					
					$.getJSON('${path}/json/completeCoa?nama='+$(this).val(), function(data) {
						if(data!=""){
							$('#coa_id').val(data[0].coa_id);
							$('#dk').val(data[0].dk);
							$('#nominal').val("");
						}else{
							$('#coa_id').val("");
							$('#dk').val("");
						}
					});				
				}
				return false;
			}); 
			
			$("#coa_id").autocomplete({
				source:function( request, response ) {					
					$.ajax({
					url: "${path}/json/autocompleteCoa",
					data: {
						coa_id: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.id,
								value: item.id,
								coa_id: item.id,
								nama : item.nama,
								dk : item.dk
							};
						})); 
					}
				});	
				},
				minLength: 3,
				select: function( event, ui ) {
					$('#nama').val(ui.item.nama);
					$('#dk').val(ui.item.dk);
					$('#nominal').val("");
				}
			});
			
			$("#nama").autocomplete({
				source:function( request, response ) {					
					$.ajax({
					url: "${path}/json/autocompleteCoa",
					data: {
						nama: request.term
					},
					success: function( data ) {
						 response( $.map( data, function( item ) {
							return {
								label: item.nama,
								value: item.nama,
								coa_id: item.id,
								nama : item.nama,
								dk : item.dk
							};
						})); 
					}
				});	
				},
				minLength: 3,
				select: function( event, ui ) {
					$('#coa_id').val(ui.item.coa_id);
					$('#dk').val(ui.item.dk);
					$('#nominal').val("");
				}
			}); 
			
		});
	</script>
</head>
<body>
	<div style="float: right; padding-top: 10px;padding-right: 10px;">
		<c:if test="${trx.posisi_id eq 1}">
			<a href="${path}/keuangan/InputTransaksi/${trx.posisi_id}/new" title="Add ${trx.jenispayment}"><img title="Add ${trx.jenispayment}" alt="add ${trx.jenispayment}" src="${path }/static/images/icons/addlist.png"></a>
		 	<a href="javascript:doAction('${path}/keuangan/InputTransaksi/${trx.posisi_id}', 'List ${trx.jenispayment}',1000,500);" title="List ${trx.jenispayment}"><img alt="list ${trx.jenispayment}" title="List ${trx.jenispayment}" src="${path }/static/images/icons/list.png"></a>
		</c:if>
		
	</div>
	<h1>${trx.pagename } &gt; ${trx.mode }</h1>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning'  or messageType eq 'done' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${trx.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>

		   </c:if>
   		</c:otherwise>
   	</c:choose>
    <form:form commandName="trx" name="formpost" method="POST" action="${path}/keuangan/InputTransaksi/${trx.posisi_id}/save" cssClass="form" >
    	<input type="hidden" name="message" id="message" value="${message }">
    	<div style="float: right;">
			<fieldset>
				<legend>History Tanggal</legend>
				 <table  class="nostyle">
			   		<tr>
			  			<th>
			  				Tanggal ${trx.jenispay }
			  			</th>
			  			<th>
			  				:
			  			</th>
				   		<td>
				   			<fmt:formatDate value="${trx.createdate }" pattern="dd-MM-yyyy HH:mm:ss"/>
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
							<form:label path="no_trx"  cssClass="label" cssErrorClass="label labelError">No Transaksi ${trx.pagename}</form:label>
						    <form:errors path="no_trx" cssClass="error" />
						</div>
							<form:hidden path="no_trx" /><input type="text" readonly="readonly" class="text_field read" value="${trx.no_trx}" size="20"/>
						<span class="description"></span>
				    </div>
		   		</td>
		   		<td>
		   			<div class="group">

				    </div>
		   		</td>
		   	</tr>
		   	<tr>
		   		<td>
		   			<div class="group">
						<div class="fieldWithErrors">
							<form:label path="tgl_rk"  cssClass="label" cssErrorClass="label labelError">Tanggal RK</form:label>
						    <form:errors path="tgl_rk" cssClass="error" />
					    </div>
						<c:choose>
							<c:when test="${trx.mode eq 'VIEW'}"><form:hidden path="tgl_rk" /><input type="text" class="text_field read" value="<fmt:formatDate value="${trx.tgl_rk }" pattern="dd-MM-yyyy HH:mm:ss"/>" readonly="readonly" size="10"/></c:when>
							<c:otherwise>
								<form:input path="tgl_rk"  cssClass="text_field datepicker" size="10" />
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
					</div>
		   		</td>
		   		<td >
					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="cash_flow_id"  cssClass="label" cssErrorClass="label labelError">Cash Flow</form:label>
						    <form:errors path="cash_flow_id" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${payment.mode eq 'VIEW'}"><form:hidden path="cash_flow_id" id="cash_flow_id" /><input type="text" class="text_field read" value="${trx.cash_flow_idKet}" readonly="readonly" size="20"/></c:when>
							<c:otherwise>
								<form:select path="cash_flow_id" id="cash_flow_id" items="${reff.lsCashFlow}" itemLabel="value" itemValue="key"></form:select>
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
				    </div>
				</td>
				<td >
					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="account_id"  cssClass="label" cssErrorClass="label labelError">Nama Bank</form:label>
						    <form:errors path="account_id" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${payment.mode eq 'VIEW'}"><form:hidden path="account_id" /><input type="text" class="text_field read" value="${trx.account_idKet}" readonly="readonly" size="50"/></c:when>
							<c:otherwise>
								<form:select path="account_id" items="${reff.lsAccount}" itemLabel="value" itemValue="key"></form:select>
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
				    </div>
				</td>
		   	</tr>
		</table>
		<c:if test="${trx.mode ne 'VIEW'}">
			<h3 class="tit">Daftar Chart Of Account </h3>
			<table  class="nostyle">
				<tr>
					<td>  	
						<div class="group">
							<div class="fieldWithErrors">
								<label>Chart Of Account</label>
							</div>
							<input type="text" class="text_field" value="" name="coa_id" id="coa_id"   size="15" />
							<span class="description"></span>
						</div>
					</td>
					<td>
						<div class="group">
							<div class="fieldWithErrors">
								<label>Keterangan</label>			            
							</div>
							<input type="text" class="text_field" value="" name="nama" id="nama" size="25"/>
							<span class="description"></span>
						</div>
				   	</td>
				   	<td>
				   		<div class="group">
							<div class="fieldWithErrors">
								<label>Jumlah</label>
							</div>
							<input type="text" class="text_field number" value="" name="nominal" id="nominal" size="12" />
							<span class="description"></span>
						</div>
				   	</td>
				   	<td>
				   		<div class="group">
							<div class="fieldWithErrors">
								<label>DK</label>
							</div>
							<select id="dk" name="dk" >
								<option value="D">D</option>
								<option value="K">K</option>
							</select>
							<span class="description"></span>
						</div>
				   	</td>
				   	<c:if test="${trx.posisi_id ne 9}">
					   	<td>
				   			<div class="group navform wat-cf" style="margin-top: 15px">
								<button class="button add_new" type="button" >
				                    <img src="${path }/static/decorator/main/pilu/images/icons/add.gif" alt="Save" /> Add
						        </button>
						    </div>
				   		</td>
			   		</c:if>
			   	</tr>
			</table> 
		</c:if>
		<div id="table_wrapper" style="overflow: auto;max-height: 300px;width: 100%" >
			<c:if test="${not empty trx.lsTrxDet}">
				<table width="100%" class="tbl_repeat">
					<tr>
						<th>No.</th>
						<th>Chart Of Account</th>
						<th>Keterangan</th>
						<th class="right">Debet</th>
						<th class="right">Kredit</th>
						<th></th>
					</tr>
					<c:forEach items="${trx.lsTrxDet }" var="t">
						<tr>
							<td>${t.urut}<input type="hidden" name="idx" id ="idx" value="${t.urut}" title="${t.urut}"/></td>
							<td>${t.coa_id }<input type="hidden" name="coa_id_${t.urut}" id="coa_id_${t.urut}" value="${t.coa_id}"  title="${t.urut}"/><input type="hidden" name="coa_id_${t.urut}" value="${t.coa_id }"  title="${t.urut}"/></td>
						 	<td><input class="" type="text" style="text-align: left;" name="ket_${t.urut}" id="ket_${t.urut}" value="${t.ket}"  title="${t.urut}"  size="35"/></td>
							<td class="right"><fmt:formatNumber pattern="#,##0">${t.jumlahDebet}</fmt:formatNumber><input type="hidden" name="jumlahDebet_${t.urut}" id="jumlahDebet_${t.urut}" value="${t.jumlahDebet}"  title="${t.urut}"/></td>
							<td class="right"><fmt:formatNumber pattern="#,##0">${t.jumlahKredit}</fmt:formatNumber><input type="hidden" name="jumlahKredit_${t.urut}" id="jumlahKredit_${t.urut}" value="${t.jumlahKredit}"  title="${t.urut}"/></td>
							<td >
						 		<a href="#" class="remove" rel="${t.urut}">
			                      <img src="${path }/static/decorator/main/pilu/images/icons/delete.png" alt="Delete" />
			                   </a> 
						 	</td>
						</tr>	
					</c:forEach>
				</table>	
			</c:if>
		</div>
    	<div class="group navform wat-cf">
		  	<c:choose>
				<c:when test="${trx.mode eq 'VIEW'}">
					<button class="button" type="button" onclick="window.location='${path}/keuangan/InputTransaksi/${trx.posisi_id}'">
						<img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
					</button>
				</c:when>
				<c:otherwise>
					<form:hidden path="mode"/>
					<form:hidden path="jenispayment"/>
					<form:hidden path="jenispay"/>
					<form:hidden path="pagename"/>
					<form:hidden path="posisi_id"/>
					<form:hidden path="trx_id" />
					<form:hidden path="dk"/>
					<c:if test="${trx.posisi_id ne 9}">
						<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
							<img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
						</button>
						<span class="text_button_padding"></span>
						<button class="button" type="button" onclick="if(confirm('Are you sure want to transfer?'))window.location='${path}/keuangan/InputTransaksi/transfer/${trx.trx_id}/${trx.posisi_id}/${trx.mode }'">
							<img src="${path }/static/decorator/main/pilu/images/icons/arrow-right.gif" alt="Transfer" /> Transfer
						</button>
						<span class="text_button_padding"></span>
						<button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}'">
							<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
						</button>
					</c:if>
					<c:if test="${trx.posisi_id eq 9}">
						<button class="button" type="button" onclick="window.location='${path}/keuangan/InputTransaksi/${trx.posisi_id}'">
							<img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
						</button>
					</c:if>
				</c:otherwise>
		   </c:choose>
	    </div>
    </form:form>
</body>
</html>