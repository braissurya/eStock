<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	  
	<script type="text/javascript">
		$(document).ready(function(){
			var path="${path}";
			//pesan error diletakkan di alert juga
			/* var message = "${message}".replace(/<br\s*[\/]?>/gi, "\n");
			if(message != ''){
			 	alert(message);
			} */

			//set onclick pada checkbox bagian input polis
			
			//jalankan fungsi2 awal untuk set nilai2
			togel($("#kirimCheck"), "#contentkirim");
		});

	</script>
</head>
<body>
	<div style="float: right; padding-top: 10px;padding-right: 10px;">

	    <a href="${path}/transaksi/${gudang.jenistrans }/${gudang.trans.pagename }/new" title="Add ${gudang.trans.pagename } ${gudang.jenistrans }"><img title="Add ${gudang.trans.pagename } ${gudang.jenistrans }" alt="add ${gudang.trans.pagename } ${gudang.jenistrans }" src="${path }/static/images/icons/addlist.png"></a>
	     <c:if test="${gudang.trans.pagename eq \"Input\"}"><a href="javascript:doAction('${path}/transaksi/${gudang.jenistrans }/OrderTransfer', 'Find Order ${gudang.jenistrans }',1000,500);" title="Find Order ${gudang.jenistrans }"><img alt="Find Order ${gudang.jenistrans }" title="Find Order ${gudang.jenistrans}" src="${path }/static/images/icons/findlist.png"></a></c:if>
	 	<a href="javascript:doAction('${path}/transaksi/${gudang.jenistrans }/${gudang.trans.pagename }', 'List ${gudang.trans.pagename } ${gudang.jenistrans }',1000,500);" title="List ${gudang.trans.pagename } ${gudang.jenistrans }"><img alt="list ${gudang.trans.pagename } ${gudang.jenistrans }" title="List ${gudang.trans.pagename } ${gudang.jenistrans }" src="${path }/static/images/icons/list.png"></a>
	 </div>
	<h1>Tanda Terima Pengiriman ${gudang.jenistrans } &gt; ${gudang.mode }
	 </h1>

	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning'  or messageType eq 'done' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${gudang.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> 
    			
    			</p>

		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	   <c:choose>
           	<c:when test="${empty gudang.trans.no_trans }">
       		    <form:form commandName="gudang" name="formpost" method="POST"  cssClass="form" >
          		    <div class="block" id="block-forms">
		         
			            <div class="inner">
		          		  	  <div class="group">
				               	  <div class="fieldWithErrors">
					                   <form:label path="trans_no" cssClass="label" cssErrorClass="label labelError">No Transaksi</form:label>
					                    <form:errors path="trans_no" cssClass="error" />
					              </div>
				                  <form:input path="trans_no"  id="target" cssClass="text_field" size="20" maxlength="14"/>
				                  <span class="description"></span>
			                 </div>
	               		</div>
	               		<div class="group navform wat-cf">
							 <form:hidden path="mode"/>
					         <form:hidden path="jenistrans"/>
		               		<button class="button" type="submit" name="cari" >
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Cari" /> Cari
			                </button>
			                 <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/klaim/input'">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                  </button>
			            </div>
                   	</div>
                  
                </form:form>
             </c:when>
             <c:otherwise>
			    <form:form commandName="gudang" name="formpost" method="POST" action="${path}/tandaterima/save" cssClass="form" >
			
			
					<input type="hidden" name="message" id="message" value="${message }">
					<div style="float: right;">
						<fieldset>
							<legend>History Tanggal</legend>
							 <table  class="nostyle">
						  		<tr>
						  			<th>
						  				Tanggal Order
						  			</th>
						  			<th>
						  				:
						  			</th>
							   		<td>
							   			<fmt:formatDate value="${gudang.trans.tgl_order }" pattern="dd-MM-yyyy HH:mm:ss"/>
							   			<form:hidden path="trans.tgl_order"/>
							   		</td>
						   		</tr>
						   		<tr>
						  			<th>
						  				Tanggal ${gudang.jenistrans }
						  			</th>
						  			<th>
						  				:
						  			</th>
							   		<td>
							   			<fmt:formatDate value="${gudang.trans.trans_date }" pattern="dd-MM-yyyy HH:mm:ss"/>
							   			<form:hidden path="trans.trans_date"/>
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
							               <form:label path="trans.no_trans"  cssClass="label" cssErrorClass="label labelError">No Transaksi ${gudang.jenistrans }</form:label>
							               <form:errors path="trans.no_trans" cssClass="error" />
								     </div>
			
								    <form:input path="trans.no_trans" cssClass="text_field read" readonly="true" cssErrorClass="text_field error read"/>
								   <form:hidden path="trans.trans_id"/>
							     	 <span class="description"></span>
							    </div>
					   		</td>
					   		<td>
					   			<div class="group">
			
							    </div>
					   		</td>
					   	</tr>
			
				   	<tr>
				   		<td >
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="trans.flag_ecer"  cssClass="label" cssErrorClass="label labelError">Jenis ${gudang.jenistrans }</form:label>
						               <form:errors path="trans.flag_ecer" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.flag_ecer" /><input type="text" class="text_field read" value="${gudang.trans.flag_ecerKet}" readonly="readonly" size="10"/></c:when>
									<c:otherwise>
			
										<form:radiobutton path="trans.flag_ecer" value="1"  label="Eceran"  cssClass="target" cssErrorClass="target error"/>
										<form:radiobutton path="trans.flag_ecer" value="0" label="Grosir"/>
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
				   		<td >
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="trans.pay_mode"  cssClass="label" cssErrorClass="label labelError">Cara Bayar</form:label>
						               <form:errors path="trans.pay_mode" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.pay_mode" /><input type="text" class="text_field read" value="${gudang.trans.pay_modeKet}" readonly="readonly" size="10"/></c:when>
									<c:otherwise>
										<c:forEach items="${reff.lsPayMode }" var="lp" varStatus="ls">
											<form:radiobutton path="trans.pay_mode" value="${lp.key}"  label="${lp.value}" />
										</c:forEach>
			
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
				   	</tr>
				   	<tr>
				   		<td colspan="2">
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="trans.retail_id"  cssClass="label" cssErrorClass="label labelError">Nama Cabang</form:label>
						               <form:errors path="trans.retail_id" cssClass="error" />
							     </div>
									<input type="text" class="text_field read" value="${gudang.trans.retail_idKet}" readonly="readonly" size="50"/>
									<form:hidden path="trans.retail_id"/>
								 <span class="description"></span>
						    </div>
				   			<%-- <div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="trans.gudang_id"  cssClass="label" cssErrorClass="label labelError">Nama Gudang </form:label>
						               <form:errors path="trans.gudang_id" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.gudang_id" /><input type="text" class="text_field read" value="${gudang.trans.gudang_idKet}" readonly="readonly" size="50"/></c:when>
									<c:otherwise>
										<form:select path="trans.gudang_id" items="${reff.lsGudang}" itemLabel="value" itemValue="key"></form:select>
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div> --%>
				   		</td>
			
				   	</tr>
				   	<tr>
				   		<td>
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="trans.sales_id"  cssClass="label" cssErrorClass="label labelError">Sales Id</form:label>
						               <form:errors path="trans.sales_id" cssClass="error" />
							     </div>
							     <form:hidden path="trans.sales_id" />
						     	 <c:choose>
									<c:when test="${gudang.mode eq 'VIEW'}"><input type="text" class="text_field read" value="${gudang.trans.karyawan.nik}" readonly="readonly" size="10"/></c:when>
									<c:otherwise>
										<form:input path="trans.karyawan.nik"  cssClass="text_field" size="10" />
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
				   		<td colspan="">
				   			<div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="trans.karyawan.nama"  cssClass="label" cssErrorClass="label labelError">Nama Sales </form:label>
						               <form:errors path="trans.karyawan.nama" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.karyawan.nama" /><input type="text" class="text_field read" value="${gudang.trans.karyawan.nama}" readonly="readonly" size="50"/></c:when>
									<c:otherwise>
										<form:input path="trans.karyawan.nama"  cssClass="text_field" size="50" />
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
				   	</tr>
				   </table>
			
				<c:choose>
					<c:when test="${gudang.jenistrans eq \"Penjualan\"}">
						<h3 class="tit">Data Customer</h3>
						 <table  class="nostyle">
							   	<tr>
							   		<td>
									    <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.customer.kode"  cssClass="label" cssErrorClass="label labelError">Kode</form:label>
									               <form:errors path="trans.customer.kode" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.customer.kode" /><input type="text" class="text_field read" value="${gudang.trans.customer.kode}" readonly="readonly" size="10"/></c:when>
												<c:otherwise>
													<form:input path="trans.customer.kode"  cssClass="text_field" size="10" />
												</c:otherwise>
											 </c:choose>
											  <form:hidden path="trans.customer_id"/>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.customer.nama"  cssClass="label" cssErrorClass="label labelError">Nama</form:label>
									               <form:errors path="trans.customer.nama" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.customer.nama" /><input type="text" class="text_field read" value="${gudang.trans.customer.nama}" readonly="readonly" size="50"/></c:when>
												<c:otherwise>
													<form:input path="trans.customer.nama"  cssClass="text_field" size="50" />
												</c:otherwise>
											 </c:choose><br/>
											 <span class="description">nama customer atau nama perusahaan</span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.customer.contact"  cssClass="label" cssErrorClass="label labelError">Contact Person</form:label>
									               <form:errors path="trans.customer.contact" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.customer.contact" /><input type="text" class="text_field read" value="${gudang.trans.customer.contact}" readonly="readonly" size="50"/></c:when>
												<c:otherwise>
													<form:input path="trans.customer.contact"  cssClass="text_field" size="50" />
												</c:otherwise>
											 </c:choose><br/>
											 <span class="description">contact person bila customer perusahaan</span>
									    </div>
							   		</td>
							   	</tr>
							   	<tr>
							   		<td>
									    <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.customer.alamat"  cssClass="label" cssErrorClass="label labelError">Alamat</form:label>
									               <form:errors path="trans.customer.alamat" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.customer.alamat" /> <textarea readonly="readonly">${gudang.trans.customer.alamat }</textarea></c:when>
												<c:otherwise>
													<form:textarea path="trans.customer.alamat"/>
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.customer.kota"  cssClass="label" cssErrorClass="label labelError">Kota</form:label>
									               <form:errors path="trans.customer.kota" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.customer.kota" /><input type="text" class="text_field read" value="${gudang.trans.customer.kota}" readonly="readonly" size="30"/></c:when>
												<c:otherwise>
													<form:input path="trans.customer.kota"  cssClass="text_field" size="30" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
			
							   		</td>
							   	</tr>
							   	<tr>
							   		<td>
									    <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.customer.no_telp"  cssClass="label" cssErrorClass="label labelError">No Telepon</form:label>
									               <form:errors path="trans.customer.no_telp" cssClass="error" />
										     </div>
									     	<c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.customer.no_telp" /><input type="text" class="text_field read" value="${gudang.trans.customer.no_telp}" readonly="readonly" size="30"/></c:when>
												<c:otherwise>
													<form:input path="trans.customer.no_telp"  cssClass="text_field" size="30" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.customer.no_hp"  cssClass="label" cssErrorClass="label labelError">No Handphone</form:label>
									               <form:errors path="trans.customer.no_hp" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.customer.no_hp" /><input type="text" class="text_field read" value="${gudang.trans.customer.no_hp}" readonly="readonly" size="30"/></c:when>
												<c:otherwise>
													<form:input path="trans.customer.no_hp"  cssClass="text_field" size="30" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
			
							   		</td>
							   	</tr>
							</table>
						 <h3 class="tit"><form:checkbox id="kirimCheck" path="trans.flag_kirim" label=" Kirim Barang" value="1"/> <a href="#" id="copyCstToKrm" title="Copy data Pengiriman dari Data Customer">(Copy)</a></h3>
						<div id="contentkirim">
						 <table  class="nostyle"  >
							  <tr>
							  		<td>
							  			<div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.tgl_kirim_est"  cssClass="label" cssErrorClass="label labelError">Tanggal Estimasi Kirim</form:label>
									               <form:errors path="trans.tgl_kirim_est" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.tgl_kirim_est" /><input type="text" class="text_field read" value='<fmt:formatDate value="${gudang.trans.createdate}" pattern="dd-MM-yyyy"/>' readonly="readonly" size="10"/></c:when>
												<c:otherwise>
													<form:input path="trans.tgl_kirim_est"  cssClass="text_field datetimepicker"  size="20" />
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
									               <form:label path="trans.contact_tujuan"  cssClass="label" cssErrorClass="label labelError">Contact Person</form:label>
									               <form:errors path="trans.contact_tujuan" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.contact_tujuan" /><input type="text" class="text_field read" value="${gudang.trans.contact_tujuan}" readonly="readonly" size="50"/></c:when>
												<c:otherwise>
													<form:input path="trans.contact_tujuan"  cssClass="text_field" size="50" />
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
									               <form:label path="trans.alamat_tujuan"  cssClass="label" cssErrorClass="label labelError">Alamat Pengiriman</form:label>
									               <form:errors path="trans.alamat_tujuan" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.alamat_tujuan" /> <textarea readonly="readonly">${gudang.trans.alamat_tujuan}</textarea></c:when>
												<c:otherwise>
													<form:textarea path="trans.alamat_tujuan"/>
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
									               <form:label path="trans.telp_tujuan"  cssClass="label" cssErrorClass="label labelError">No Telepon</form:label>
									               <form:errors path="trans.telp_tujuan" cssClass="error" />
										     </div>
									     	<c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.telp_tujuan" /><input type="text" class="text_field read" value="${gudang.trans.telp_tujuan}" readonly="readonly" size="30"/></c:when>
												<c:otherwise>
													<form:input path="trans.telp_tujuan"  cssClass="text_field" size="30" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
			
							   	</tr>
							</table>
							</div>
					</c:when>
					<c:when test="${gudang.jenistrans eq \"Pembelian\"}">
						  <h3 class="tit">Data Supplier</h3>
						   <table  class="nostyle">
							   	<tr>
							   		<td>
									    <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.supplier.kode"  cssClass="label" cssErrorClass="label labelError">Kode</form:label>
									               <form:errors path="trans.supplier.kode" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.kode" /><input type="text" class="text_field read" value="${gudang.trans.supplier.kode}" readonly="readonly" size="10"/></c:when>
												<c:otherwise>
													<form:input path="trans.supplier.kode"  cssClass="text_field" size="10" />
												</c:otherwise>
											 </c:choose>
											 <form:hidden path="trans.supplier_id"/>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.supplier.nama"  cssClass="label" cssErrorClass="label labelError">Nama</form:label>
									               <form:errors path="trans.supplier.nama" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.nama" /><input type="text" class="text_field read" value="${gudang.trans.supplier.nama}" readonly="readonly" size="50"/></c:when>
												<c:otherwise>
													<form:input path="trans.supplier.nama"  cssClass="text_field" size="50" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.supplier.contact"  cssClass="label" cssErrorClass="label labelError">Contact Person</form:label>
									               <form:errors path="trans.supplier.contact" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.contact" /><input type="text" class="text_field read" value="${gudang.trans.supplier.contact}" readonly="readonly" size="50"/></c:when>
												<c:otherwise>
													<form:input path="trans.supplier.contact"  cssClass="text_field" size="50" />
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
									               <form:label path="trans.supplier.alamat"  cssClass="label" cssErrorClass="label labelError">Alamat</form:label>
									               <form:errors path="trans.supplier.alamat" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.alamat" /> <textarea readonly="readonly">${gudang.trans.supplier.alamat }</textarea></c:when>
												<c:otherwise>
													<form:textarea path="trans.supplier.alamat"/>
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.supplier.kota"  cssClass="label" cssErrorClass="label labelError">Kota</form:label>
									               <form:errors path="trans.supplier.kota" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.kota" /><input type="text" class="text_field read" value="${gudang.trans.supplier.kota}" readonly="readonly" size="30"/></c:when>
												<c:otherwise>
													<form:input path="trans.supplier.kota"  cssClass="text_field" size="30" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			<div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.supplier.email"  cssClass="label" cssErrorClass="label labelError">Alamat Email</form:label>
									               <form:errors path="trans.supplier.email" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.email" /><input type="text" class="text_field read" value="${gudang.trans.supplier.email}" readonly="readonly" size="50"/></c:when>
												<c:otherwise>
													<form:input path="trans.supplier.email"  cssClass="text_field" size="50" />
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
									               <form:label path="trans.supplier.no_telp"  cssClass="label" cssErrorClass="label labelError">No Telepon</form:label>
									               <form:errors path="trans.supplier.no_telp" cssClass="error" />
										     </div>
									     	<c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.no_telp" /><input type="text" class="text_field read" value="${gudang.trans.supplier.no_telp}" readonly="readonly" size="30"/></c:when>
												<c:otherwise>
													<form:input path="trans.supplier.no_telp"  cssClass="text_field" size="30" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.supplier.no_hp"  cssClass="label" cssErrorClass="label labelError">No Handphone</form:label>
									               <form:errors path="trans.supplier.no_hp" cssClass="error" />
										     </div>
									     	 <c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.no_hp" /><input type="text" class="text_field read" value="${gudang.trans.supplier.no_hp}" readonly="readonly" size="30"/></c:when>
												<c:otherwise>
													<form:input path="trans.supplier.no_hp"  cssClass="text_field" size="30" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   		<td>
							   			 <div class="group">
									         <div class="fieldWithErrors">
									               <form:label path="trans.supplier.no_fax"  cssClass="label" cssErrorClass="label labelError">No Fax</form:label>
									               <form:errors path="trans.supplier.no_fax" cssClass="error" />
										     </div>
									     	<c:choose>
												<c:when test="${gudang.mode eq 'VIEW'}"><form:hidden path="trans.supplier.no_fax" /><input type="text" class="text_field read" value="${gudang.trans.supplier.no_fax}" readonly="readonly" size="30"/></c:when>
												<c:otherwise>
													<form:input path="trans.supplier.no_fax"  cssClass="text_field" size="30" />
												</c:otherwise>
											 </c:choose>
											 <span class="description"></span>
									    </div>
							   		</td>
							   	</tr>
							</table>
					</c:when>
				</c:choose>
			
			   <h3 class="tit">Daftar Produk</h3>
			   <div id="table_wrapper" >
						<c:if test="${not empty gudang.trans.lsTransDet}">
							<table width="100%" class="tbl_repeat">
								<tr>
									<th>No.</th>
									<th>Kode Produk</th>
									<th>Nama Produk</th>
<!-- 									<th class="right">@Harga</th> -->
<!-- 									<th class="right" colspan="2">@Diskon</th> -->
									<th class="right">Qty</th>
									<th>Satuan</th>
<!-- 									<th class="right">Sub Total Harga</th> -->
									<th></th>
								</tr>
								<c:choose>
									<c:when test="${gudang.jenistrans eq \"Penjualan\" }">
										<c:forEach items="${gudang.trans.lsTransDet }" var="t">
											<tr>
												<td>${t.urut}<input type="hidden" name="idx" id ="idx" value="${t.urut}" title="${t.urut}"/></td>
												<td>${t.barcode_ext }<input type="hidden" name="item_id_${t.urut}" id="item_id_${t.urut}" value="${t.item_id }"  title="${t.urut}"/><input type="hidden" name="barcode_ext_${t.urut}" value="${t.barcode_ext }"  title="${t.urut}"/></td>
											 	<td>${t.item_idKet}<input type="hidden" name="nama_${t.urut}" id="nama_${t.urut}" value="${t.item_idKet}"  title="${t.urut}"/></td>
<!-- 											 	<td class="right"><fmt:formatNumber pattern="#,##0">${t.harga}</fmt:formatNumber><input type="hidden" name="harga_${t.urut}" id="harga_${t.urut}" value="${t.harga}"  title="${t.urut}"/></td> -->
<!-- 											 	<td class="right" colspan="2"> -->
<!-- 											 		<input type="text" size="4" name="persen_diskon_${t.urut}" disabled="disabled" id="persen_diskon_${t.urut}" value="<fmt:formatNumber pattern="###0.00">${t.persen_diskon}</fmt:formatNumber>"  class="text_field number"  title="${t.urut}"/>% -->
<!-- 											 		(<input type="text" size="16" name="diskon_${t.urut}" disabled="disabled" id="diskon_${t.urut}" value="<fmt:formatNumber pattern="#,##0">${t.jumlah_diskon}</fmt:formatNumber>"  class="text_field nominal"  title="${t.urut}"/>)			 -->
<!-- 											 	</td> -->
<!-- 											 	<td class="right"><input type="text" disabled="disabled"  id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td> -->
											 	<td class="right">${t.qty}<input type="hidden" id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td>
											 	<td>${t.satuan_idKet}<input type="hidden" name="satuan_${t.urut}" id="satuan_${t.urut}" value="${t.satuan_idKet}"  title="${t.urut}"/></td>
			
<!-- 											 	<td class="right" ><span id="subtotal_${t.urut}" class="subTotal"><fmt:formatNumber pattern="#,##0">${t.subTotal_harga}</fmt:formatNumber></span><input type="hidden" name="subTotal_harga_${t.urut}" id="subTotal_harga_${t.urut}" value="${t.subTotal_harga}"  title="${t.urut}"/></td> -->
											 	<td >
											 		
											 	</td>
										 	</tr>
										</c:forEach>
									</c:when>
									<c:when test="${gudang.jenistrans eq \"Pembelian\" }">
										<c:forEach items="${gudang.trans.lsTransDet }" var="t">
											<tr>
												<td>${t.urut}<input type="hidden" name="idx" id ="idx" value="${t.urut}" title="${t.urut}"/></td>
												<td>${t.barcode_ext }<input type="hidden" name="item_id_${t.urut}" id="item_id_${t.urut}" value="${t.item_id }"  title="${t.urut}"/><input type="hidden" name="barcode_ext_${t.urut}" value="${t.barcode_ext }"  title="${t.urut}"/></td>
											 	<td>${t.item_idKet}<input type="hidden" name="nama_${t.urut}" id="nama_${t.urut}" value="${t.item_idKet}"  title="${t.urut}"/></td>
<!-- 											 	<td class="right"><input type="text" disabled="disabled"  name="harga_${t.urut}" id="harga_${t.urut}"  value="<fmt:formatNumber pattern="###0.00">${t.harga}</fmt:formatNumber>"  class="text_field number"  title="${t.urut}"/></td> -->
<!-- 											 	<td class="right" colspan="2"> -->
<!-- 											 		<input type="text" size="4" disabled="disabled" name="persen_diskon_${t.urut}" id="persen_diskon_${t.urut}" value="<fmt:formatNumber pattern="###0.00">${t.persen_diskon}</fmt:formatNumber>"  class="text_field number"  title="${t.urut}"/>% -->
<!-- 											 		(<input type="text" size="16" disabled="disabled" name="diskon_${t.urut}" id="diskon_${t.urut}" value="<fmt:formatNumber pattern="#,##0">${t.jumlah_diskon}</fmt:formatNumber>"  class="text_field nominal"  title="${t.urut}"/>)	 -->
<!-- 											 	</td> -->
<!-- 											 	<td class="right"><input type="text" disabled="disabled"  id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td> -->
											 	<td class="right">${t.qty}<input type="hidden" id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td>
											 	<td>${t.satuan_idKet}<input type="hidden" name="satuan_${t.urut}" id="satuan_${t.urut}" value="${t.satuan_idKet}"  title="${t.urut}"/></td>
			
<!-- 											 	<td class="right" ><span id="subtotal_${t.urut}" class="subTotal"><fmt:formatNumber pattern="#,##0">${t.subTotal_harga}</fmt:formatNumber></span><input type="hidden" name="subTotal_harga_${t.urut}" id="subTotal_harga_${t.urut}" value="${t.subTotal_harga}"  title="${t.urut}"/></td> -->
											 	<td >
											 		
											 	</td>
										 	</tr>
										</c:forEach>
									</c:when>
								</c:choose>
			
			
<!-- 								<tr> -->
<!-- 									<td colspan="8" class="right subTotal">Total Harga</td> -->
<!-- 									<td class="right"><span class="Total subTotal"><fmt:formatNumber pattern="#,##0">${gudang.trans.sub_total_harga}</fmt:formatNumber></span></td> -->
<!-- 									<td></td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td colspan="8" class="right" >Diskon Tambahan -->
			
<!-- 									</td> -->
<!-- 									<td  class="right"> -->
<!-- 										<input type="text" name="persen_disc" disabled="disabled"   id="persen_disc" size="4" value="<fmt:formatNumber pattern="###0.00">${gudang.trans.persen_disc}</fmt:formatNumber>"  class="text_field number" />% -->
<!-- 										(<input type="text" name="total_disc" disabled="disabled"   id="total_disc" value="<fmt:formatNumber pattern="#,##0">${gudang.trans.total_disc}</fmt:formatNumber>" size="16" class="text_field nominal" style="text-align: right;" />) -->
<!-- 									</td> -->
<!-- 									<td></td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<th colspan="8" class="grandtotal right" >Grand Total</th> -->
<!-- 									<th  class="grandtotal right"><span class="grandTotal"><fmt:formatNumber pattern="#,##0">${gudang.trans.total_harga}</fmt:formatNumber></span></th> -->
<!-- 									<th></th> -->
<!-- 								</tr> -->
							</table>
						</c:if>
				</div>
			
			    <br/><br/>
			    <div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="trans.status_kirim"  cssClass="label" cssErrorClass="label labelError">Status Kirim</form:label>
			               <form:errors path="trans.status_kirim" cssClass="error" />
				     </div>
						
					<form:select path="trans.status_kirim">
						<form:option value="">Pilih Status Pengiriman</form:option>
						<form:options items="${reff.lsstatus_krm }" itemLabel="value" itemValue="key"/>
					</form:select>
					 <span class="description"></span>
			    </div>
			    <br/>
				<div class="group navform wat-cf">
					 <form:hidden path="mode"/>
			         <form:hidden path="jenistrans"/>
			          <form:hidden path="trans.posisi_id"/>
			           <form:hidden path="trans.jenis"/>
				  	<c:choose>
						<c:when test="${gudang.mode eq 'VIEW'}">
							 <button class="button" type="submit" onclick="if(!confirm('Are you sure want to Save'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			                </button>
			                   <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/klaim/input'">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			                </button>
			                <c:if test="${not empty gudang.trans.no_trans}">
				                <c:choose>
				                	<c:when test="${gudang.trans.jenis eq 1 or  gudang.trans.jenis eq 3}"><!-- Pembelian -->
				                		<c:choose>
				                			<c:when test="${gudang.trans.posisi_id eq 1}"><!-- Order -->
				                			</c:when>
				                			<c:when test="${gudang.trans.posisi_id eq 2}"><!-- Pembelian -->
				                			</c:when>
				                			<c:when test="${gudang.trans.posisi_id eq 3}"><!-- Payment -->
				                			</c:when>
				                		</c:choose>
				                	</c:when>
				                	<c:when test="${gudang.trans.jenis eq 2 or  gudang.trans.jenis eq 4}"><!-- Penjualan -->
			
				                			<c:if test="${gudang.trans.posisi_id gt 0}"><!-- Order -->
			                					<button class="button" type="button" onclick="if(confirm('Are you sure want to Print Form Order?'))doAction('${path}/report/print_form_order/${gudang.trans.no_trans }', 'Print Form Order',900,500);">
								                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Form Order" /> Print Form Order
								                </button>
			
			
				                			</c:if>
			                				<c:if test="${gudang.trans.posisi_id gt 1 and gudang.trans.jenis eq 4}"><!-- Penjualan -->
				                				 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Delivery Order?'))doAction('${path}/report/print_do/${gudang.trans.no_trans }', 'Print Delivery Order',900,500);">
								                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Delivery Order" /> Print Delivery Order
								                </button>
								                <c:choose>
								                	 <c:when test="${gudang.trans.pay_mode eq 2}">
										                 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Faktur?'))doAction('${path}/report/print_faktur/${gudang.trans.no_trans }', 'Print Faktur',900,500);">
										                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Faktur" /> Print Faktur
										                </button>
										                <c:if test="${not empty gudang.trans.print_faktur_date and not empty gudang.trans.print_trans_date and gudang.trans.posisi_id eq 2}">
										                	 <button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${gudang.jenistrans }/${gudang.trans.pagename }/${gudang.mode }/${gudang.trans.trans_id}';">
											                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
											                </button>
									                	</c:if>
													</c:when>
													<c:otherwise>
														 <c:if test="${ not empty gudang.trans.print_trans_date and gudang.trans.posisi_id eq 2}">
										                	 <button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${gudang.jenistrans }/${gudang.trans.pagename }/${gudang.mode }/${gudang.trans.trans_id}';">
											                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
											                </button>
									                	</c:if>
													</c:otherwise>
								                </c:choose>
								               
								                 
			
				                			</c:if>
				                			<c:if test="${gudang.trans.posisi_id gt 2}"><!-- Payment -->
				                				 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Purchasing Order?'))doAction('${path}/report/print_po/${gudang.trans.no_trans }', 'Print Purchasing Order',900,500);">
								                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Purchasing Order" /> Print Purchasing Order
								                </button>
			
				                			</c:if>
				                	</c:when>
			
				                </c:choose>
			
							</c:if>
						</c:otherwise>
				   </c:choose>
			
			    </div>
			
				</form:form>
		</c:otherwise>
	</c:choose>



</body>
</html>