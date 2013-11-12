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
			
			// Initialise Plugin
			var options = {
				additionalFilterTriggers: [$('#quickfind')],
				clearFiltersControls: [$('#cleanfilters')]
			};
			$('#tableSort').tableFilter(options);
			
			
			
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
	<h1>Input Berita Acara Gudang  </h1>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${opname.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="opname" name="formpost" method="POST" action="${path}/transaksi/Stockopname/save" cssClass="form" >
   	<h3 class="tit">Daftar List Berita Acara Gudang</h3>
    		<div class="search">
	    		<div>
		    		 Quick Find: <input type='text' id='quickfind'/>
		    		 <a id="cleanfilters" href="#">Clear Filters</a>
	    		</div>
    		</div>
    	<div id="table_wrapper" style="overflow: auto;max-height: 300px;width: 100%" >
    	<c:if test="${not empty opname.lsOpnameDet}">
    		
    		<div class="inner">
	    		<table width="100%" class="table" id="tableSort">	
	    			<thead>
	    				<tr>
							<th>Nama Produk</th>
							<th>Merk Produk </th>
							<th>Warna Produk </th>
							<th>Kategori Produk </th>
							<th>Qty</th>
							<th <c:if test="${opname.mode ne 'VIEW'}">filter='false'</c:if> >@Qty Fisik</th>
							<th <c:if test="${opname.mode ne 'VIEW'}">filter='false'</c:if> >Keterangan</th>
						</tr>
	    			</thead>
	    			<tbody>
	    				<c:forEach items="${opname.lsOpnameDet}" var="p" varStatus="vs">
							<tr>
							 	<td>${p.nama_item}
							 		<form:hidden path="lsOpnameDet[${vs.index}].item_id"/>
									<form:hidden path="lsOpnameDet[${vs.index}].nama_item"/>
								</td>
								<td>${p.merk_item}
									<form:hidden path="lsOpnameDet[${vs.index}].merk_item"/>
								</td>
								<td>${p.warna_item}
									<form:hidden path="lsOpnameDet[${vs.index}].warna_item"/>
								</td>
								<td>${p.kategori_item}
									<form:hidden path="lsOpnameDet[${vs.index}].kategori_item"/>
								</td>
								<td>${p.qty}
									<form:hidden path="lsOpnameDet[${vs.index}].qty"/>
								</td>
								<td>
									<c:choose>
										<c:when test="${opname.mode eq 'VIEW'}">
											${p.qty_fisik}
											<form:hidden path="lsOpnameDet[${vs.index}].qty_fisik" />
										</c:when>
										<c:otherwise>
											<form:input path="lsOpnameDet[${vs.index}].qty_fisik" cssClass="text_field" size="5" />
											<br>
											<form:errors path="lsOpnameDet[${vs.index}].qty_fisik" cssClass="error" />
										</c:otherwise>
									</c:choose>
								</td>
								<td>
									<c:choose>
										<c:when test="${opname.mode eq 'VIEW'}"><form:hidden path="lsOpnameDet[${vs.index}].jenis" />${p.keterangan}</c:when>
										<c:otherwise>
											<form:select path="lsOpnameDet[${vs.index}].jenis" cssErrorClass="inputError">
												<form:option value="">Silahkan Pilih Keterangan</form:option>
												<form:options items="${reff.listKetOpname}" itemValue="key" itemLabel="value"/>
											</form:select>
										</c:otherwise>
							  		</c:choose>
								</td>
						 	</tr>
						</c:forEach>
	    			</tbody>
				</table>
			</div>
    	</c:if>
    	</div>
    	<br/><br/>
		<div class="group navform wat-cf">
		<form:hidden path="opname_id"  />
		<form:hidden path="createby"  />
		<form:hidden path="createdate" />
		  	<c:choose>
				<c:when test="${opname.mode eq 'VIEW'}">
					<button class="button" type="button" onclick="window.location='${path}/transaksi/Stockopname'">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
	                  </button>
				</c:when>
				<c:otherwise>
					 <form:hidden path="mode"/>
					<button class="button" type="button" onclick="saveAja();">
	                	<img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
	                </button>
	                <span class="text_button_padding"></span>
	                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/transaksi/Stockopname'">
	             	   <img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
	                </button>
	                 <span class="text_button_padding"></span>
	                <button class="button" type="button" onclick="if(confirm('Are you sure want to transfer?'))window.location='${path}/transaksi/Stockopname/transfer/${opname_id}/${opname.no_trans }'">
	                	<img src="${path }/static/decorator/main/pilu/images/icons/arrow-right.gif" alt="Transfer" /> Transfer
	                </button>
				</c:otherwise>
		   </c:choose>
	               
	    </div>
   	</form:form>
</body>
</html>