<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head></head>
<body>
	
	<h1>Transaksi Approval
	 </h1>

	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning'  or messageType eq 'done' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${trans.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> 
    			
    			</p>

		   </c:if>
   		</c:otherwise>
   	</c:choose>
    <form:form commandName="trans" name="formpost" method="POST" action="${path}/transaksi/save" cssClass="form" >
	<input type="hidden" name="message" id="message" value="${message }">
	  <table  class="nostyle">
	  		<tr>
		   		<td>
		   			<div class="group">
				         <div class="fieldWithErrors">
				               <form:label path="no_trans"  cssClass="label" cssErrorClass="label labelError">No Transaksi ${trans.jenistrans }</form:label>
				               <form:errors path="no_trans" cssClass="error" />
					     </div>

					    <form:input path="no_trans" cssClass="text_field read" readonly="true" cssErrorClass="text_field error read"/>
					   <form:hidden path="trans_id"/>
				     	 <span class="description"></span>
				    </div>
		   		</td>
		   		<td>
		   			<div class="group">
						
				    </div>
		   		</td>
		   	</tr>
		 </table>
	   <div id="table_wrapper" style="overflow: auto;max-height: 300px;width: 100%" >
			<c:if test="${not empty trans.lsTransDet}">
				<table width="100%" class="tbl_repeat">
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
					<c:choose>
						<c:when test="${trans.jenistrans eq \"Penjualan\" }">
							<c:forEach items="${trans.lsTransDet }" var="t">
								<tr>

									<td>${t.urut}<input type="hidden" name="idx" id ="idx" value="${t.urut}" title="${t.urut}"/></td>
									<td>${t.barcode_ext }<input type="hidden" name="item_id_${t.urut}" id="item_id_${t.urut}" value="${t.item_id }"  title="${t.urut}"/><input type="hidden" name="barcode_ext_${t.urut}" value="${t.barcode_ext }"  title="${t.urut}"/></td>
								 	<td>${t.item_idKet}<input type="hidden" name="nama_${t.urut}" id="nama_${t.urut}" value="${t.item_idKet}"  title="${t.urut}"/></td>
								 	<td class="right"><fmt:formatNumber pattern="#,##0">${t.harga}</fmt:formatNumber><input type="hidden" name="harga_${t.urut}" id="harga_${t.urut}" value="${t.harga}"  title="${t.urut}"/></td>
								 	<td class="right" colspan="2">
								 		<input type="text" size="4" name="persen_diskon_${t.urut}" id="persen_diskon_${t.urut}" value="<fmt:formatNumber pattern="###0.00">${t.persen_diskon}</fmt:formatNumber>"  class="text_field number"  title="${t.urut}"/>%
								 		(<input type="text" size="16" name="diskon_${t.urut}" id="diskon_${t.urut}" value="<fmt:formatNumber pattern="#,##0">${t.jumlah_diskon}</fmt:formatNumber>"  class="text_field nominal"  title="${t.urut}"/>)

								 	</td>
								 	<td class="right"><input type="text"  id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td>
								 	<td>${t.satuan_idKet}<input type="hidden" name="satuan_${t.urut}" id="satuan_${t.urut}" value="${t.satuan_idKet}"  title="${t.urut}"/></td>

								 	<td class="right" ><span id="subtotal_${t.urut}" class="subTotal"><fmt:formatNumber pattern="#,##0">${t.subTotal_harga}</fmt:formatNumber></span><input type="hidden" name="subTotal_harga_${t.urut}" id="subTotal_harga_${t.urut}" value="${t.subTotal_harga}"  title="${t.urut}"/></td>
								 	<td >
								 		
								 	</td>
							 	</tr>
							</c:forEach>
						</c:when>
						<c:when test="${trans.jenistrans eq \"Pembelian\" }">
							<c:forEach items="${trans.lsTransDet }" var="t">
								<tr>

									<td>${t.urut}<input type="hidden" name="idx" id ="idx" value="${t.urut}" title="${t.urut}"/></td>
									<td>${t.barcode_ext }<input type="hidden" name="item_id_${t.urut}" id="item_id_${t.urut}" value="${t.item_id }"  title="${t.urut}"/><input type="hidden" name="barcode_ext_${t.urut}" value="${t.barcode_ext }"  title="${t.urut}"/></td>
								 	<td>${t.item_idKet}<input type="hidden" name="nama_${t.urut}" id="nama_${t.urut}" value="${t.item_idKet}"  title="${t.urut}"/></td>
								 	<td class="right"><input type="text"  name="harga_${t.urut}" id="harga_${t.urut}"  value="<fmt:formatNumber pattern="###0.00">${t.harga}</fmt:formatNumber>"  class="text_field number"  title="${t.urut}"/>
								 	</td>
								 	<td class="right" colspan="2">
								 		<input type="text" size="4" name="persen_diskon_${t.urut}" id="persen_diskon_${t.urut}" value="<fmt:formatNumber pattern="###0.00">${t.persen_diskon}</fmt:formatNumber>"  class="text_field number"  title="${t.urut}"/>%
								 		(<input type="text" size="16" name="diskon_${t.urut}" id="diskon_${t.urut}" value="<fmt:formatNumber pattern="#,##0">${t.jumlah_diskon}</fmt:formatNumber>"  class="text_field nominal"  title="${t.urut}"/>)

								 	</td>
								 	<td class="right"><input type="text"  id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td>
								 	<td>${t.satuan_idKet}<input type="hidden" name="satuan_${t.urut}" id="satuan_${t.urut}" value="${t.satuan_idKet}"  title="${t.urut}"/></td>

								 	<td class="right" ><span id="subtotal_${t.urut}" class="subTotal"><fmt:formatNumber pattern="#,##0">${t.subTotal_harga}</fmt:formatNumber></span><input type="hidden" name="subTotal_harga_${t.urut}" id="subTotal_harga_${t.urut}" value="${t.subTotal_harga}"  title="${t.urut}"/></td>
								 	<td >
								 		
								 	</td>
							 	</tr>
							</c:forEach>
						</c:when>
					</c:choose>


					<tr>
						<td colspan="8" class="right subTotal">Total Harga</td>
						<td class="right"><span class="Total subTotal"><fmt:formatNumber pattern="#,##0">${trans.sub_total_harga}</fmt:formatNumber></span></td>
						<td></td>
					</tr>
					<tr>
						<td colspan="8" class="right" >Diskon Tambahan

						</td>
						<td  class="right">
							<input type="text" name="persen_disc" id="persen_disc" size="4" value="<fmt:formatNumber pattern="###0.00">${trans.persen_disc}</fmt:formatNumber>"  class="text_field number" />%
							(<input type="text" name="total_disc" id="total_disc" value="<fmt:formatNumber pattern="#,##0">${trans.total_disc}</fmt:formatNumber>" size="16" class="text_field nominal" style="text-align: right;" />)
						</td>
						<td></td>
					</tr>
					<tr>
						<th colspan="8" class="grandtotal right" >Grand Total</th>
						<th  class="grandtotal right"><span class="grandTotal"><fmt:formatNumber pattern="#,##0">${trans.total_harga}</fmt:formatNumber></span></th>
						<th></th>
					</tr>
				</table>
			</c:if>
	</div>

    <br/><br/>
	<div class="group navform wat-cf">
		 <form:hidden path="mode"/>
         <form:hidden path="jenistrans"/>
         <form:hidden path="pagename"/>
         <form:hidden path="posisi_id"/>
         <form:hidden path="jenis"/>
	  	 <button class="button" type="button" onclick="window.location='${path}/viewer'">
            <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
          </button> 

    </div>

	</form:form>





</body>
</html>