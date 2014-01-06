<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Item &gt; ${item.mode } </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${item.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="item" name="formpost" method="POST" action="${path}/master/item/save" cssClass="form" >
   		<div class="columns wat-cf">
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="barcode_ext" cssClass="label" cssErrorClass="label labelError">Kode Item</form:label>
	                <form:hidden path="id" />
	                <form:errors path="barcode_ext" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="barcode_ext" /><input type="text" class="text_field read" value="${item.barcode_ext }" readonly="readonly" size="11"/></c:when>
					<c:otherwise><form:input path="barcode_ext"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="11" maxlength="11"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div> 
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama Item</form:label>
	                <form:errors path="nama" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${item.nama }" readonly="readonly" size="60"/></c:when>
					<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="60" maxlength="60"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="space"><br/></div>
   			<table class="nostyle">
   				<tr>
   					<td style="width: 200px">  
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="kategori" cssClass="label" cssErrorClass="label labelError">Kategori</form:label>
				                <form:errors path="kategori" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="kategori" /><input type="text" class="text_field read" value="${item.kategori_idNama }" readonly="readonly" /></c:when>
								<c:otherwise>
									<form:select  path="kategori_id" cssErrorClass="inputError">
										<form:option value="">Silahkan Pilih Kategori</form:option>
										<form:options items="${reff.listKategori}" itemValue="key" itemLabel="value"/>
									</form:select>
								</c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
			   		<td style="width: 200px">   
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="merk" cssClass="label" cssErrorClass="label labelError">Merk</form:label>
				                <form:errors path="merk" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="merk" /><input type="text" class="text_field read" value="${item.merk_idNama }" readonly="readonly" /></c:when>
								<c:otherwise>
									<form:select  path="merk_id" cssErrorClass="inputError">
										<form:option value="">Silahkan Pilih Merk</form:option>
										<form:options items="${reff.listMerk}" itemValue="key" itemLabel="value"/>
									</form:select>
								</c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
			   		<td style="width: 200px">   
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="warna" cssClass="label" cssErrorClass="label labelError">Warna</form:label>
				                <form:errors path="warna" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="warna" /><input type="text" class="text_field read" value="${item.warna_idNama }" readonly="readonly" /></c:when>
								<c:otherwise>
									<form:select  path="warna_id" cssErrorClass="inputError">
										<form:option value="">Silahkan Pilih Warna</form:option>
										<form:options items="${reff.listWarna}" itemValue="key" itemLabel="value"/>
									</form:select>
								</c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
			   		<td style="width: 200px">   
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="satuan" cssClass="label" cssErrorClass="label labelError">Satuan</form:label>
				                <form:errors path="satuan" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="satuan" /><input type="text" class="text_field read" value="${item.satuan_idNama }" readonly="readonly" /></c:when>
								<c:otherwise>
									<form:select  path="satuan_id" cssErrorClass="inputError">
										<form:option value="">Silahkan Pilih Satuan</form:option>
										<form:options items="${reff.listSatuan}" itemValue="key" itemLabel="value"/>
									</form:select>
								</c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
				</tr>	
			</table>  
   			<table  class="nostyle">
   				<tr>
   					<td style="width: 200px">  
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="harga" cssClass="label" cssErrorClass="label labelError">Harga Grosir (Rp)</form:label>
				                <form:errors path="harga" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="harga" /><input type="text" class="text_field nominal read" value="${item.harga }" readonly="readonly" size="14"/></c:when>
								<c:otherwise><form:input path="harga"  id="target" cssClass="text_field nominal" cssErrorClass="text_field inputError" size="14" maxlength="14" /></c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
			   		<td style="width: 200px">   
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="diskon" cssClass="label" cssErrorClass="label labelError">Diskon Grosir (Rp)</form:label>
				                <form:errors path="diskon" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="diskon" /><input type="text" class="text_field nominal read" value="${item.diskon }" readonly="readonly" size="14"/></c:when>
								<c:otherwise><form:input path="diskon"  id="target" cssClass="text_field nominal" cssErrorClass="text_field inputError" size="14" maxlength="14" /></c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
				</tr>	
			</table>  
   			<table  class="nostyle">
   				<tr>
   					<td style="width: 200px">  
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="harga_ecer" cssClass="label" cssErrorClass="label labelError">Harga Ecer (Rp)</form:label>
				                <form:errors path="harga_ecer" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="harga_ecer" /><input type="text" class="text_field nominal read" value="${item.harga_ecer }" readonly="readonly" size="14"/></c:when>
								<c:otherwise><form:input path="harga_ecer"  id="target" cssClass="text_field nominal" cssErrorClass="text_field inputError" size="14" maxlength="14"/></c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
			   		<td style="width: 200px">   
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="diskon" cssClass="label" cssErrorClass="label labelError">Diskon Ecer (Rp)</form:label>
				                <form:errors path="diskon" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="diskon_ecer" /><input type="text" class="text_field nominal read" value="${item.diskon }" readonly="readonly" size="14"/></c:when>
								<c:otherwise><form:input path="diskon_ecer"  id="target" cssClass="text_field nominal" cssErrorClass="text_field inputError" size="14" maxlength="14"/></c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
				</tr>	
			</table>  
   			<table  class="nostyle">
   				<tr>
   					<td style="width: 200px">  
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="qty_min" cssClass="label" cssErrorClass="label labelError">Qty Minimum</form:label>
				                <form:errors path="qty_min" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="qty_min" /><input type="text" class="text_field nominal read" value="${item.qty_min }" readonly="readonly" size="14"/></c:when>
								<c:otherwise><form:input path="qty_min"  id="target" cssClass="text_field nominal" cssErrorClass="text_field inputError" size="14" maxlength="9"/></c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
			   		<td style="width: 200px">   
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="qty_max" cssClass="label" cssErrorClass="label labelError">Qty Maksimum</form:label>
				                <form:errors path="qty_max" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="qty_max" /><input type="text" class="text_field nominal read" value="${item.qty_max }" readonly="readonly" size="14"/></c:when>
								<c:otherwise><form:input path="qty_max"  id="target" cssClass="text_field nominal" cssErrorClass="text_field inputError" size="14" maxlength="9"/></c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
				</tr>	
			</table>  
   			<table  class="nostyle">
   				<tr>
   					<td style="width: 200px">  
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="stock_jual" cssClass="label" cssErrorClass="label labelError">Qty Stock Fisik</form:label>
				                <form:errors path="stock_jual" cssClass="error" />
				        	</div>
							<input type="text" class="text_field nominal read" value="${item.qty_saldo }" readonly="readonly" size="14"/> &nbsp &nbsp &nbsp &nbsp &nbsp -
			                <span class="description"></span>
			        	</div>
			   		</td> 
   					<td style="width: 200px">  
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="qty_jual" cssClass="label" cssErrorClass="label labelError">Qty Order Penjualan</form:label>
				                <form:errors path="qty_jual" cssClass="error" />
				        	</div>
							<input type="text" class="text_field nominal read" value="${item.qty_jual }" readonly="readonly" size="14"/> &nbsp &nbsp &nbsp &nbsp &nbsp +
			                <span class="description"></span>
			        	</div>
			   		</td>  
   					<td style="width: 200px">  
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="qty_beli" cssClass="label" cssErrorClass="label labelError">Qty Order Pembelian</form:label>
				                <form:errors path="qty_beli" cssClass="error" />
				        	</div>
							<input type="text" class="text_field nominal read" value="${item.qty_beli }" readonly="readonly" size="14"/> &nbsp &nbsp &nbsp &nbsp &nbsp =
			                <span class="description"></span>
			        	</div>
			   		</td>  
   					<td style="width: 200px">  
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="stock_jual" cssClass="label" cssErrorClass="label labelError">Qty Avaiable</form:label>
				                <form:errors path="stock_jual" cssClass="error" />
				        	</div>
							<input type="text" class="text_field nominal read" value="${item.stock_jual }" readonly="readonly" size="14"/>
			                <span class="description"></span>
			        	</div>
			   		</td>  
				</tr>	
			</table>  
        	
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${item.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/master/item'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/item'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>