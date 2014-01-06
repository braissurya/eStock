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
			
			function addRowDelivery(type){
				if($('#no_trans').val()=='') alert('Kode Transaksi Belum diisi');				
				else{
					var arr = type.closest('form').serializeArray();
					var tbl = $('.tbl_repeat tr').length;
					$.getJSON(path+'/json/addRowDelivery?tbl='+tbl, arr, function(data) {
						var len = data.length;
						if(data[0].desc.length>3){
							alert("Kode Transaksi : "+$('#no_trans').val()+" "+data[0].desc);
						}else if(data[0].desc=="ga"){
							alert("Kode Transaksi : "+$('#no_trans').val()+" tidak ditemukan");
						}else{
		
							var rows = $('.tbl_repeat tr').length;
							for (var i = 0; i< len; i++) {
								$('#table_wrapper').html(
									$(data[i].value).hide().fadeIn(300)
								);
							}
							$('#no_trans').val('');
							
						}
					}, 'json');
		
				}
		
				$(".no_trans").focus();
				return false;
		
			}
		
			$('.remove').live('click', function() {
				var trigger = $(this);
				var arr = $(this).closest('form').serializeArray();
				var id = $(this).attr('rel');
				$.getJSON(path+'/json/removeRowDelivery?id='+id, arr, function(data) {
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
				$(".no_trans").focus();
				return false;
			});
			
			$('.add_new').click(function() {
				addRowDelivery($(this));

			});
			
			$('#no_trans').keypress(function(e) {
			  if (e.keyCode == '13') {
			  		 e.preventDefault();
			  		
			  		addRowDelivery($(this));					

			   }
			});

		});

		
	</script>
</head>
<body>
	<h1>Delivery &gt; ${delivery.mode } </h1>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${delivery.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="delivery" name="formpost" method="POST" action="${path}/delivery/save" cssClass="form" >
   		<div class="columns wat-cf">
   			<div class="group">
	            <div class="fieldWithErrors">
		            <form:label path="kode"  cssClass="label" cssErrorClass="label labelError">Kode Delivery</form:label>
		            <form:errors path="kode" cssClass="error" />
	            </div><form:hidden path="id" />
	        	<form:hidden path="kode" /><input type="text" class="text_field read" value="${delivery.kode }"  cssErrorClass="text_field inputError"  readonly="readonly" />
            	<span class="description"></span>
            </div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="driver_id" cssClass="label" cssErrorClass="label labelError">Driver</form:label>
	                <form:errors path="driver_id" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${delivery.mode eq 'VIEW'}"><form:hidden path="driver_id" /><input type="text" class="text_field read" value="${delivery.driver_idNama }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:select path="driver_id" cssErrorClass="error">
							<form:option value="">Silahkan Pilih Driver</form:option>
							<form:options items="${reff.lsDriver }" itemValue="key" itemLabel="value"/>
						</form:select>
					</c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	 <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="tgl_kirim" cssClass="label" cssErrorClass="label labelError">Tanggal Kirim</form:label>
	                <form:errors path="tgl_kirim" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${delivery.mode eq 'VIEW'}"><form:hidden path="tgl_kirim" /><input type="text" class="text_field read" value="${delivery.tgl_kirim }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:input path="tgl_kirim"  cssClass="text_field datetimepicker" cssErrorClass="text_field datetimepicker error"/>
					</c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	 <div class="space"></div>
        	 <c:if test="${trans.mode ne 'VIEW'}">
			   <h3 class="tit">Daftar Transaksi</h3>
			   <table  class="nostyle">
				   	<tr>
				   		<td>
						    <div class="group">
						        <div class="fieldWithErrors">
						             <label>Kode Transaksi</label>
						        </div>
						       <input type="text" class="text_field kode" value="" name="no_trans" id="no_trans"   size="20" />
						       <span class="description"></span>
						   </div>
				   		</td>
				   		<td>
				   			<div class="group navform wat-cf" style="margin-top: 15px">
								<button class="button add_new" id="add_new" type="button" >
						                    <img src="${path }/static/decorator/main/pilu/images/icons/add.gif" alt="Add Transaksi" /> Add Transaksi
						        </button>
						        <a href="javascript:doAction('${path}/transaksi/Penjualan/Delivery', 'List Antrian Delivery',1000,500,false);" title="List Antrian Delivery"><img alt="list Antrian Delivery" title="List Antrian Delivery" src="${path }/static/images/icons/addlist.png"></a>
						    </div>
						   
				   		</td>
				   	</tr>
			   </table>
		   </c:if>
		   <div id="table_wrapper" style="overflow: auto;max-height: 300px;width: 100%" >
					<c:if test="${not empty delivery.lsDeliveryDets}">
						<table width="100%" class="tbl_repeat">
							<tr>
								<th>No.</th>
								<th>Kode Transaksi</th>
								<th>Nama Tujuan</th>
								<th>Alamat Tujuan</th>
								<th>Telepon Tujuan</th>
								<th>Estimasi Tgl Kirim</th>								
								<th></th>
							</tr>				
							<c:forEach items="${delivery.lsDeliveryDets}" var="t" varStatus="st">
								<tr>
									<td>${st.count}<input type="hidden" name="trans_id" id ="trans_id" value="${t.trans_id}" title="${t.trans_id}"/></td>
									<td>${t.no_trans }<input type="hidden" name="no_trans_${t.trans_id}" id="no_trans_${t.trans_id}" value="${t.no_trans }"  title="${t.trans_id}"/></td>
								 	<td>${t.contact_tujuan}<input type="hidden" name="contact_tujuan_${t.trans_id}" id="contact_tujuan_${t.trans_id}" value="${t.contact_tujuan}"  title="${t.trans_id}"/></td>
								 	<td>${t.alamat_tujuan}<input type="hidden" name="alamat_tujuan_${t.trans_id}" id="alamat_tujuan_${t.trans_id}" value="${t.alamat_tujuan}"  title="${t.trans_id}"/></td>
								 	<td>${t.telp_tujuan}<input type="hidden" name="telp_tujuan_${t.trans_id}" id="telp_tujuan_${t.trans_id}" value="${t.telp_tujuan}"  title="${t.trans_id}"/></td>
								 	<td><fmt:formatDate value="${t.tgl_kirim_est}" pattern="dd-MM-yyyy HH:mm:ss"/> <input type="hidden" name="tgl_kirim_est_${t.trans_id}" id="tgl_kirim_est_${t.trans_id}" value="<fmt:formatDate value="${t.tgl_kirim_est}" pattern="dd-MM-yyyy HH:mm:ss"/>"  title="${t.trans_id}"/></td>
								 	<td >
								 		<a href="#" class="remove" rel="${t.trans_id}">
					                      <img src="${path }/static/decorator/main/pilu/images/icons/delete.png" alt="Delete" />
					                   </a>
								 	</td>
							 	</tr>
							</c:forEach>
						</table>
					</c:if>
			</div>
			
			<br/><br/>
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${delivery.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/delivery'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/delivery'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
			                
			                 <c:if test="${ not empty delivery.id }">
			                	  <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Surat Jalan'))doAction('${path}/report/print_surat_jalan/${delivery.id }', 'Print Surat Jalan',900,500);">
				                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Surat Jalan" /> Print Surat Jalan
				                </button>
		                	</c:if>
		                	 <c:if test="${ not empty delivery.tgl_print_sj}">
			                	 <button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/delivery/${delivery.id }/transfer';">
				                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
				                </button>
		                	</c:if>
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>