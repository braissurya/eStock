<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
		$(document).ready(function(){
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
		
			$("#table_wrapper").delegate('input:text', 'keyup', function(e){
					
				  syncPrice($(this).attr("id"));
		    }); 
		    
		    $("#table_wrapper").delegate('input:text.nominal', 'blur', function(){	 
				  $(this).formatCurrency({ colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: 0});
		    });
			
			
			function addRowProduct(type){
				if($('#barcode_ext').val()=='') alert('Kode Produk Belum diisi');
				else if($('#qty').val()=='') alert('QTY Belum diisi');
				else if($('#nama_produk').val()=='') alert('Nama Produk Belum diisi');
				else{
					var arr = type.closest('form').serializeArray();
					var tbl = $('.tbl_repeat tr').length;
					$.getJSON('${path}/json/addRowTransfer?tbl='+tbl, arr, function(data) {
						var len = data.length;
						
						if(data[0].desc=="ga"){
							alert("Kode Produk : "+$('#barcode_ext').val()+" tidak ditemukan");
						}else{
							
							var rows = $('.tbl_repeat tr').length;
							for (var i = 0; i< len; i++) {						
								$('#table_wrapper').html(
									$(data[i].value).hide().fadeIn(300)
								);
							}
							$('#barcode_ext').val('');
							$('#qty').val('1');
							$('#nama_produk').val('');
						}
					}, 'json');
					
				}
				
				$(".kode").focus();
				return false;
								
			}
				
			$('.remove').live('click', function() {
				var trigger = $(this);
				var arr = $(this).closest('form').serializeArray();
				var id = $(this).attr('rel');
				$.getJSON('${path}/json/removeRowTransfer?id='+id, arr, function(data) {
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
			
			$('.add_new').click(function() {
				addRowProduct($(this));
				
			});
			
			$('#qty').val('1');
			
			 $('#barcode_ext').live('keyup', function(e) {
				
				if($(this).val().length>2){
					
					$.getJSON('${path}/json/completeItemPrice?barcode_ext='+$(this).val(), function(data) {
						if(data!=""){
							$('#nama_produk').val(data[0].nama);
							if($('input[name=flag_ecer]:checked').val()==1){
								$('#harga').val(formatCurrency(data[0].harga_ecer));
								$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga_ecer)*100));
							}else {
								$('#harga').val(formatCurrency(data[0].harga));
								$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga)*100));
							}
							$('#diskon').val(formatCurrency(data[0].diskon));
							
						}else{
							$('#nama_produk').val("");
							$('#harga').val("");
							$('#diskon_persen').val("");							
							$('#diskon').val("");
						}
					});				
				}
				return false;
			}); 
			
			$('#nama_produk').live('keyup', function(e) {
				
				if($(this).val().length>2){
					
					$.getJSON('${path}/json/completeItemPrice?nama_produk='+$(this).val(), function(data) {
						if(data!=""){
							$('#barcode_ext').val(data[0].barcode_ext);
							if($('input[name=flag_ecer]:checked').val()==1){
								$('#harga').val(formatCurrency(data[0].harga_ecer));
								$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga_ecer)*100));
							}else {
								$('#harga').val(formatCurrency(data[0].harga));
								$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga)*100));
							}
							$('#diskon').val(formatCurrency(data[0].diskon));
							
						}else{
							$('#barcode_ext').val("");
							$('#harga').val("");
							$('#diskon_persen').val("");							
							$('#diskon').val("");
						}
					});				
				}
				return false;
			});  
			
			$('#barcode_ext,#nama_produk,#qty').keypress(function(e) {
			  if (e.keyCode == '13') {
			  		 e.preventDefault();
			   	  addRowProduct($(this));
			   }
			});
			
						
			  $("#nama_produk").autocomplete({
				source:function( request, response ) {					
					$.ajax({
					url: "${path}/json/autocompleteItemPrice",
					data: {
						nama_produk: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nama,
								value: item.nama,
								barcode_id: item.barcode_ext,
								nama : item.nama,
								harga : item.harga,
								harga_ecer : item.harga_ecer,
								diskon :item.diskon
							};
						})); 
					}
				});	
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#barcode_ext').val(ui.item.barcode_id);
					if($('input[name=flag_ecer]:checked').val()==1){
						$('#harga').val(formatCurrency(ui.item.harga_ecer));
						$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga_ecer)*100));
					}else {
						$('#harga').val(formatCurrency(ui.item.harga));
						$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga)*100));
					}
					$('#diskon').val(formatCurrency(ui.item.diskon));
				}
			});  
			
			$("#barcode_ext").autocomplete({
				source:function( request, response ) {					
					$.ajax({
					url: "${path}/json/autocompleteItemPrice",
					data: {
						barcode_ext: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.barcode_ext,
								value: item.barcode_ext,
								barcode_id: item.barcode_ext,
								nama : item.nama,
								harga : item.harga,
								harga_ecer : item.harga_ecer,
								diskon :item.diskon
							};
						})); 
					}
				});	
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#nama_produk').val(ui.item.nama);
					if($('input[name=flag_ecer]:checked').val()==1){
						$('#harga').val(formatCurrency(ui.item.harga_ecer));
						$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga_ecer)*100));
					}else {
						$('#harga').val(formatCurrency(ui.item.harga));
						$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga)*100));
					}
					$('#diskon').val(formatCurrency(ui.item.diskon));
				}
			});
			
			
			
		});	
		
		function saveAja(){
				if(confirm('Are you sure want to save?')){
					if("${closingWarning}"=="false"){
						if(!confirm("Stock belum di closing. Apakah transaksi ingin dimasukkan ke periode sebelumnya?")){
							
						}else{ formpost.submit();}
					}else{
						formpost.submit();
					}
				}else{
					
				}
			}
		
	</script>
</head>
<body>
	<h1>Input Transaksi Transfer Stock ${trans.mode }  </h1>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${trans.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
    <form:form commandName="trans" name="formpost" method="POST" action="${path}/transaksi/Transferstock/save/${trans.posisi_id}" cssClass="form" >
		<table  class="nostyle">
			<tr>
				<td>  	
	   				<div class="group">
				         <div class="fieldWithErrors">
				               <form:label path="no_trans"  cssClass="label" cssErrorClass="label labelError">No Order Transfer Stock</form:label>
				               <form:errors path="no_trans" cssClass="error" />
					     </div>
				     	 <form:hidden path="no_trans" /><input type="text" class="text_field read" value="${trans.no_trans }" readonly="readonly" size="20"/>
				     	 <form:hidden path="trans_id" />
						 <span class="description"></span>
			    	</div>
	   			</td>
	   		</tr>
	   		<c:choose>
		   		<c:when test="${trans.posisi_id eq 1}">
		   			<tr>
				   		<td>
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="tgl_order"  cssClass="label" cssErrorClass="label labelError">Tanggal Order</form:label>
						               <form:errors path="tgl_order" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="tgl_order" /><input type="text" class="text_field read" value="<fmt:formatDate value="${trans.tgl_order}" pattern="dd-MM-yyyy"/>" readonly="readonly" size="10"/></c:when>
									<c:otherwise>
										<form:input path="tgl_order"  cssClass="text_field datepicker"  size="10" />
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
				   		<td>
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="tgl_kirim_est"  cssClass="label" cssErrorClass="label labelError">Estimasi Tanggal Kirim</form:label>
						               <form:errors path="tgl_kirim_est" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="tgl_kirim_est" /><input type="text" class="text_field read" value="<fmt:formatDate value="${trans.tgl_kirim_est}" pattern="dd-MM-yyyy"/>" readonly="readonly" size="10"/></c:when>
									<c:otherwise>
										<form:input path="tgl_kirim_est"  cssClass="text_field datepicker" size="10" />
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
						               <form:label path="retail_id_req"  cssClass="label" cssErrorClass="label labelError">Cabang Penerima Stock</form:label>
						               <form:errors path="retail_id_req" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="retail_id_req" /><input type="text" class="text_field read" value="${trans.retail_id_reqKet}" readonly="readonly" size="50"/></c:when>
									<c:otherwise>
										<form:select path="retail_id_req" items="${reff.lsCabangReq}" itemLabel="value" itemValue="key"></form:select>
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
				   	</tr>
		   		</c:when>
	   			<c:when test="${trans.posisi_id eq 2}">
	   				<tr>
				   		<td>
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="driver_id"  cssClass="label" cssErrorClass="label labelError">Nama Driver</form:label>
						               <form:errors path="driver_id" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="driver_id" /><input type="text" class="text_field read" value="${trans.driver_idKet}" readonly="readonly" size="50"/></c:when>
									<c:otherwise>
										<form:select path="driver_id" items="${reff.lsDriver}" itemLabel="value" itemValue="key"></form:select>
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
				   		<td>
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="tgl_kirim_est"  cssClass="label" cssErrorClass="label labelError">Tanggal Kirim Estimasi</form:label>
						               <form:errors path="tgl_kirim_est" cssClass="error" />
							     </div>
						     	 <form:hidden path="tgl_kirim_est" /><input type="text" class="text_field read" value="<fmt:formatDate value="${trans.tgl_kirim_est }" pattern="dd-MM-yyyy"/>" readonly="readonly" size="15"/>
								 <span class="description"></span>
						    </div>
				   		</td>
				   		<td>
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="tgl_kirim"  cssClass="label" cssErrorClass="label labelError">Tanggal Kirim</form:label>
						               <form:errors path="tgl_kirim" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="tgl_kirim" /><input type="text" class="text_field read" value="<fmt:formatDate value="${trans.tgl_kirim }" pattern="dd-MM-yyyy"/>" readonly="readonly" size="15"/></c:when>
									<c:otherwise>
										<form:input path="tgl_kirim"  cssClass="text_field datepicker" size="15" />
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
						               <form:label path="retail_id_req"  cssClass="label" cssErrorClass="label labelError">Cabang Penerima Stock</form:label>
						               <form:errors path="retail_id_req" cssClass="error" />
							     </div>
								 <form:hidden path="retail_id_req" /><input type="text" class="text_field read" value="${trans.retail_id_reqKet}" readonly="readonly" size="50"/>
								 <span class="description"></span>
						    </div>
				   		</td>
				   		<td>
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="retail_id"  cssClass="label" cssErrorClass="label labelError">Cabang Request</form:label>
						               <form:errors path="retail_id" cssClass="error" />
							     </div>
								 <form:hidden path="retail_id" /><input type="text" class="text_field read" value="${trans.retail_idKet}" readonly="readonly" size="50"/>
								 <span class="description"></span>
						    </div>
				   		</td>
				   	</tr>
	   			</c:when>
	   			<c:when test="${trans.posisi_id eq 3}">
	   				<tr>
	   					<td>
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="receiveddate"  cssClass="label" cssErrorClass="label labelError">Tanggal Terima</form:label>
						               <form:errors path="receiveddate" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="receiveddate" /><input type="text" class="text_field read" value="<fmt:formatDate value="${trans.receiveddate }" pattern="dd-MM-yyyy"/>" readonly="readonly" size="10"/></c:when>
									<c:otherwise>
										<form:input path="receiveddate"  cssClass="text_field datepicker" size="10" />
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
	   				</tr>
	   			</c:when>
	   		</c:choose>
		</table>
		<c:if test="${trans.mode ne 'VIEW' and trans.posisi_id eq 1}">
			<h3 class="tit">Daftar Transfer Produk </h3>
			<table  class="nostyle">
				<tr>
					<td>  	
						<div class="group">
							<div class="fieldWithErrors">
						    	<label>Kode Produk</label>
							</div>
							<input type="text" class="text_field kode" value="${trans.transDet.barcode_ext }" name="barcode_ext" id="barcode_ext"   size="20" />
						    <span class="description"></span>
						</div>
				   	</td>
				   	<td>
				   		<div class="group">
							<div class="fieldWithErrors">
						    	<label>Nama Produk</label>			            
							</div>
						    <input type="text" class="text_field" value="" name="nama_produk" id="nama_produk" size="30"/>
							<span class="description"></span>
						</div>
				   	</td>
				   	<td>
				   		<div class="group">
							<div class="fieldWithErrors">
						    	<label>QTY</label>
							</div>
						    <input type="text" class="text_field number" value="" name="qty" id="qty" size="6" />
						    <span class="description"></span>
						</div>
				   	</td>
				   	<td>
				   		<div class="group navform wat-cf">
							<button class="button add_new" type="button" >
						    	<img src="${path }/static/decorator/main/pilu/images/icons/add.gif" alt="Save" /> Add Produk
							</button>
						</div>
				   	</td>
				</tr>
			</table> 
		</c:if>		 
   
		<div id="table_wrapper" style="overflow: auto;max-height: 300px;width: 100%" >
			<c:if test="${not empty trans.lsTransDet}">
				<table width="100%" class="tbl_repeat">
					<tr>
						<th>No.</th>
						<th>Kode Produk</th>
						<th>Nama Produk</th>
						<th class="right">Qty</th>
						<th></th>
					</tr>
					<c:forEach items="${trans.lsTransDet }" var="t">
						<tr>
							
							<td>${t.urut}<input type="hidden" name="idx" id ="idx" value="${t.urut}" title="${t.urut}"/></td>		
							<td>${t.barcode_ext }<input type="hidden" name="item_id_${t.urut}" id="item_id_${t.urut}" value="${t.item_id }"  title="${t.urut}"/><input type="hidden" name="barcode_ext_${t.urut}" value="${t.barcode_ext }"  title="${t.urut}"/></td>
						 	<td>${t.item_idKet}<input type="hidden" name="nama_${t.urut}" id="nama_${t.urut}" value="${t.item_idKet}"  title="${t.urut}"/></td>
						 	<td class="right"><input type="text" style="text-align: right;"  id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td>
						 	<td >
						 		<c:if test="${trans.mode ne 'VIEW'}">
							 		<c:if test="${trans.posisi_id eq 1}">
							 			<a href="#" class="remove" rel="${t.urut}">
					                      <img src="${path }/static/decorator/main/pilu/images/icons/delete.png" alt="Delete" />
					                	</a> 
						 			</c:if>	
						 		</c:if>
						 	</td>
					 	</tr>
					</c:forEach>
				</table>
			</c:if>
		</div>         
                
	    <br/><br/>
		<div class="group navform wat-cf">
		  	<c:choose>
				<c:when test="${trans.mode eq 'VIEW'}">
					<button class="button" type="button" onclick="window.location='${path}/transaksi/Transferstock/${trans.posisi_id}'">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
	                  </button>
				</c:when>
				<c:otherwise>
					<button class="button" type="button" onclick="saveAja();">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
	                  </button>
	                  <form:hidden path="mode"/>
	                  <form:hidden path="jenistrans"/>
	                  <form:hidden path="pagename"/>
	                  <form:hidden path="retail_idKet"/>
	                  <form:hidden path="retail_id_reqKet"/>
	                  <span class="text_button_padding"></span>
	                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/transaksi/Transferstock/${trans.posisi_id}'">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
	                 </button>
	                 <c:if test="${trans.posisi_id eq 1 }">
	                 	<c:if test="${not empty trans.no_trans }">
		                 	<span class="text_button_padding"></span>
				    		<button class="button" type="button" onclick="if(confirm('Are you sure want to transfer?'))window.location='${path}/transaksi/Transferstock/transfer/${trans.trans_id}/${trans.posisi_id}/${trans.mode }'">
				    			<img src="${path }/static/decorator/main/pilu/images/icons/arrow-right.gif" alt="Transfer" /> Transfer
				  			</button>
	                 	</c:if>
	                 </c:if>
	     			<c:if test="${trans.posisi_id eq 2 }">
	         			<c:if test="${not empty trans.tgl_kirim }">
	         				<span class="text_button_padding"></span>
				    		<button class="button" type="button" onclick="if(confirm('Are you sure want to transfer?'))window.location='${path}/transaksi/Transferstock/transfer/${trans.trans_id}/${trans.posisi_id}/${trans.mode }'">
				    			<img src="${path }/static/decorator/main/pilu/images/icons/arrow-right.gif" alt="Transfer" /> Transfer
				  			</button>
	         			</c:if>
	         		</c:if>
	                <c:if test="${trans.posisi_id eq 3 }">
	         			<c:if test="${not empty trans.receiveddate }">
	         				<span class="text_button_padding"></span>
				    		<button class="button" type="button" onclick="if(confirm('Are you sure want to transfer?'))window.location='${path}/transaksi/Transferstock/transfer/${trans.trans_id}/${trans.posisi_id}/${trans.mode }'">
				    			<img src="${path }/static/decorator/main/pilu/images/icons/arrow-right.gif" alt="Transfer" /> Transfer
				  			</button>
	         			</c:if>
	         		</c:if>
				</c:otherwise>
		   </c:choose>
	    </div>
	</form:form>		
</body>
</html>		