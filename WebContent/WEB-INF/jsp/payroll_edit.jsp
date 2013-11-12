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
			
			 $('.periodepicker').datepicker( {
		        changeMonth: true,
		        changeYear: true,
		        showButtonPanel: true,
		        dateFormat: 'MM yy',
		        onClose: function(dateText, inst) { 
		            var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
		            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
		            $(this).datepicker('setDate', new Date(year, month, 1));
		        }
		    });
			 
			function replaceComma(nilai){
				var result=0;
				nilai=nilai.replace(/[^0-9\.]+/g,"");
				if(isNaN(nilai)){
					result=0;
				}else{
					result=nilai;
					if(result=="")result=0;
				}
				
				return result;
			}
				
		    function syncPrice(triger){
			  	var gaji_bersih=0;
			  	var gaji_total=0;
			  	var pot_total=0;
	     	 
       			var gapok=parseFloat(replaceComma($('#gapok').val()));
       			var uang_makan=parseFloat(replaceComma($('#uang_makan').val()));
       			var uang_transport=parseFloat(replaceComma($('#uang_transport').val()));
       			var uang_lembur=parseFloat(replaceComma($('#uang_lembur').val()));
       			var bonus=parseFloat(replaceComma($('#bonus').val()));
       			gaji_total=gapok+uang_makan+uang_transport+uang_lembur+bonus;
       			
       			var pot_pinjam=parseFloat(replaceComma($('#pot_pinjam').val()));
       			var pot_asuransi=parseFloat(replaceComma($('#pot_asuransi').val()));
       			var pot_lain=parseFloat(replaceComma($('#pot_lain').val()));
       			pot_total=pot_pinjam+pot_asuransi+pot_lain;
       			
       			gaji_bersih=gaji_total-pot_total;
       			
       			$('#gaji_total').val(formatNumber(gaji_total));
       			$('#pot_total').val(formatNumber(pot_total));
       			$('.gaji_bersih').text(formatNumber(gaji_bersih));		
		    
		    }
			
			$("#table_wrapper").delegate('input:text', 'keyup', function(e){
				  syncPrice($(this).attr("id"));
		    });
			 
			
			$("#nik").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteKaryawan",
					data: {
						nik: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nik,
								value: item.nik,
								nama : item.nama,
								gaji : item.gaji,
								id: item.id
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#nama').val(ui.item.nama);
					$('#gapok').val(ui.item.gaji);
					$('input[name=karyawan_id]').val(ui.item.id);

				}
			});

			 $('#nik').live('keyup', function(e) {

				if($(this).val().length>2){

					$.getJSON(path+'/json/completeKaryawan?nik='+$(this).val(), function(data) {
						if(data!=""){
							$('#nama').val(data[0].nama);
							$('#gapok').val(data[0].gaji);
							$('input[name=karyawan_id]').val(data[0].id);

						}else{
							$('nama').val("");
							$('#gapok').val("");
							$('input[name=karyawan_id]').val("");
						}
					});
				}
				return false;
			});

			$('#nama').live('keyup', function(e) {

				if($(this).val().length>2){

					$.getJSON(path+'/json/completeKaryawan?nama='+$(this).val(), function(data) {
						if(data!=""){
							$('#nik').val(data[0].nik);
							$('#gapok').val(data[0].gaji);
							$('input[name=karyawan_id]').val(data[0].id);

						}else{
							$('#nik').val("");
							$('#gapok').val("");
							$('input[name=karyawan_id]').val("");
						}
					});
				}
				return false;
			});

			  $("#nama").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteKaryawan",
					data: {
						nama: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nama,
								value: item.nama,
								nik: item.nik,
								gaji: item.gaji,
								id:item.id
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#nik').val(ui.item.nik);
					$('#gapok').val(ui.item.gaji);
					$('input[name=karyawan_id]').val(ui.item.id);
				}
			});
	
		});
	</script>
	
	<style type="text/css">
		.ui-datepicker-calendar {
		    display: none;
	    }
	</style>
</head>
<body>
	<h1>Payroll &gt; ${payroll.mode } </h1>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${payroll.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="payroll" name="formpost" method="POST" action="${path}/payroll/save" cssClass="form" >
   		<div class="columns wat-cf">
   			<div class="group">
	            <div class="fieldWithErrors">
		            <form:label path="periode"  cssClass="label" cssErrorClass="label labelError">Periode</form:label>
		            <form:errors path="periode" cssClass="error" />
	            </div><form:hidden path="id" />
	        	<c:choose>
					<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="periode" /><input type="text" class="text_field read" value="${payroll.periode }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:input path="periode" cssClass="text_field periodepicker" cssErrorClass="text_field periodepicker error"/>
					</c:otherwise>
		  		</c:choose>
            </div>
            <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="nik"  cssClass="label" cssErrorClass="label labelError">NIK Karyawan</form:label>
		               <form:errors path="nik" cssClass="error" />
			     </div>
			     <form:hidden path="karyawan_id" />
		     	 <c:choose>
					<c:when test="${payroll.mode eq 'VIEW'}"><input type="text" class="text_field read" value="${payroll.nik}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="nik"  cssClass="text_field" cssErrorClass="text_field error" size="10" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
        	<div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="nama"  cssClass="label" cssErrorClass="label labelError">Nama Karyawan </form:label>
		               <form:errors path="nama" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${payroll.nama}" readonly="readonly" size="50"/></c:when>
					<c:otherwise>
						<form:input path="nama"  cssClass="text_field" size="50" cssErrorClass="text_field error" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div> 
		     <div class="space"></div>
		    <h3 class="tit">Rincian Gaji</h3>
		   <div id="table_wrapper" style="width: 100%" > 
		   <table class="nostyle">
		   		<tr>
		   			<th>
		   				Gaji
		   			</th>
		   			<th>
		   			</th>
		   			<th>
		   				Potongan
		   			</th>
		   		</tr>
		   		<tr>
		   			<td>
		   				 <table class="nostyle">
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="gapok"  cssClass="label" cssErrorClass="label labelError">Gaji Pokok</form:label>
								               <form:errors path="gapok" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="gapok" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.gapok}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="gapok"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="uang_makan"  cssClass="label" cssErrorClass="label labelError">Uang Makan</form:label>
								               <form:errors path="uang_makan" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="uang_makan" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.uang_makan}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="uang_makan"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="uang_transport"  cssClass="label" cssErrorClass="label labelError">Uang Transport</form:label>
								               <form:errors path="uang_transport" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="uang_transport" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.uang_transport}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="uang_transport"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="uang_lembur"  cssClass="label" cssErrorClass="label labelError">Uang Lembur</form:label>
								               <form:errors path="uang_lembur" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="uang_lembur" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.uang_lembur}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="uang_lembur"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="bonus"  cssClass="label" cssErrorClass="label labelError">Bonus</form:label>
								               <form:errors path="bonus" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td >
					    			<div class="group" style="text-align: right;">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="bonus" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.bonus}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="bonus"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
									</div>
									<div class="group" style="text-align: right;width: 100%;">	
										+
										   <hr style="border:2px double black;"/>
								     </div>
								     
								   
					    		</td>
					    		<td style="vertical-align: bottom;"></td>
					    	</tr>
					    	
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="gaji_total"  cssClass="label" cssErrorClass="label labelError">Total Gaji</form:label>
								               <form:errors path="gaji_total" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="gaji_total" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.gaji_total}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="gaji_total"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
					    </table>
		   			</td>
		   			<td style="width: 10px;">
		   			</td>
		   			<td>
		   				<table class="nostyle">
		   					<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="pot_pinjam"  cssClass="label" cssErrorClass="label labelError">Potongan Pinjaman</form:label>
								               <form:errors path="pot_pinjam" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="pot_pinjam" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.pot_pinjam}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="pot_pinjam"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="pot_asuransi"  cssClass="label" cssErrorClass="label labelError">Potongan Asuransi</form:label>
								               <form:errors path="pot_asuransi" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="pot_asuransi" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.pot_asuransi}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="pot_asuransi"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="pot_lain"  cssClass="label" cssErrorClass="label labelError">Potongan Lain</form:label>
								               <form:errors path="pot_lain" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group" style="text-align: right;">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="pot_asuransi" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.pot_lain}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="pot_lain"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
									</div>
									<div class="group" style="text-align: right;width: 100%;">	
										+
										   <hr style="border:2px double black;"/>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
					    	<tr>
					    		<th>
					    			<div class="group">
								         <div class="fieldWithErrors">
								               <form:label path="pot_total"  cssClass="label" cssErrorClass="label labelError">Total Potongan</form:label>
								               <form:errors path="pot_total" cssClass="error" />
									     </div>
								     </div> 
					    		</th>
					    		<td>
					    			<div class="group">
								       <c:choose>
											<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="pot_total" /><input type="text" class="text_field nominal read" value="<fmt:formatNumber pattern="#,##0">${payroll.pot_total}</fmt:formatNumber>" readonly="readonly" size="50"/></c:when>
											<c:otherwise>
												<form:input path="pot_total"  cssClass="text_field nominal" size="50" cssErrorClass="text_field error nominal" />
											</c:otherwise>
										 </c:choose>
								     </div>
					    		</td>
					    		<td></td>
					    	</tr>
		   				</table>
		   			</td>
		   		</tr>
		   		<tr>
		    		<th colspan="3" class="grandtotal" style="font-size: 15px;">
		    			<div class="group">
					         <div class="fieldWithErrors">
					               <form:label path="gaji_bersih"  cssClass="label" cssErrorClass="label labelError">Gaji Bersih (Total Gaji - Total Potongan) : </form:label>
					               <form:errors path="gaji_bersih" cssClass="error" />
					               
					                <span class="gaji_bersih"><fmt:formatNumber pattern="#,##0">${payroll.gaji_bersih}</fmt:formatNumber></span>
						     </div>
					     
					      
					     </div>
		    		</th>
		    	</tr>
		   </table>
		   
		    	
		    	
			</div>
			<br/><br/>
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${payroll.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/payroll'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/payroll'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
			                
			                 <c:if test="${ not empty payroll.id }">
			                	 <%--  <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Slip Gaji'))doAction('${path}/report/print_slip_gaji/${payroll.id }', 'Print Slip Gaji',900,500);">
				                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Slip Gaji" /> Print  Slip Gaji
				                </button> --%>
		                	</c:if>
		                	 
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>