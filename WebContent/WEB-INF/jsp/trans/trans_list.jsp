<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
		if('${trans_id}'!='')
			window.top.location.href = '${path}/transaksi/transfer/${jenistrans}/${pagename}/edit/${trans_id}';
		else if('${trans_idnya}'!=''){
			window.top.location.href = '${path}/transaksi/${jenistrans}/${pagename}/${trans_idnya}/edit';
		}
	</script>
</head>
<body>

	<form name="form" id="form" method="post">
	<h1> Daftar Transaksi ${pagename} ${jenistrans }</h1>

	<c:if test="${pagename ne \"Delivery\"}">
		<div class="notrans">
			Nomor Transaksi : <input type="text" name="nt"  value="${no_transaksi}" size="30" class="textfield target">
		</div>
	</c:if>
    <div class="total">Total Data : ${totalData }</div>
    <div class="search">
    	Quick Find: <input type="text" name="s"  value="${search}" size="30" class="textfield">
    	<input type="submit" name="btsearch"  value=" " class="btsearch" title="search">

    </div>

            <div class="inner">
            	<c:choose>
            		<c:when test="${pagename eq \"Delivery\"}">
            			<table class="table">
							<tr>
								<th>
									<script>groupList('no_trans', 'form', '${page}', 'Kode Transaksi', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('contact_tujuan', 'form', '${page}', 'Nama Tujuan', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('alamat_tujuan', 'form', '${page}', 'Alamat Tujuan', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('telp_tujuan', 'form', '${page}', 'Telepon Tujuan', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('tgl_kirim_est', 'form', '${page}', 'Estimasi Tgl Kirim', '${sort}', '${sort_type}');</script>
								</th>
								
								 <th class="last">Actions</th>
							</tr>
		
							<c:forEach items="${listPaging}" var="p">
							<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
								<td>${p.no_trans}</td>
								<td>${p.contact_tujuan}</td>
								<td>${p.alamat_tujuan}</td>
								<td>${p.telp_tujuan}</td>
								<td><fmt:formatDate value="${p.tgl_kirim_est}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
								<td class="last">
									<a href="javascript:$('#no_trans', top.document).val('${p.no_trans}');$('#add_new', top.document).click();parent.closeAction();lewat();" title="edit">
				                      <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Edit" />
				                    </a>
								</td>
							</tr>
							</c:forEach>
						</table>
            		</c:when>
            		<c:when test="${pagename eq \"Viewer\"}">
            			<table class="table">
							<tr>
								<th>
									<script>groupList('no_trans', 'form', '${page}', 'No Order', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('posisi_idKet', 'form', '${page}', 'Posisi', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<c:choose>
            							<c:when test="${jenistrans eq \"Pembelian\"}">
            								<script>groupList('supplier_idKet', 'form', '${page}', 'Nama Supplier', '${sort}', '${sort_type}');</script>
            							</c:when>
            							<c:when test="${jenistrans eq \"Penjualan\"}">
            								<script>groupList('customer_idKet', 'form', '${page}', 'Nama Customer', '${sort}', '${sort_type}');</script>
            							</c:when>
            							<c:otherwise>
            								Nama Customer / Supplier
            							</c:otherwise>
            						</c:choose>
									
								</th>
								<th>
									<script>groupList('total_harga', 'form', '${page}', 'Total Harga', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('total_disc', 'form', '${page}', 'Total Diskon', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('createbyKet', 'form', '${page}', 'Create By', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('createdate', 'form', '${page}', 'Create Date', '${sort}', '${sort_type}');</script>
								</th>
								 <th class="last">Actions</th>
							</tr>
		
							<c:forEach items="${listPaging}" var="p">
							<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
								<td>${p.no_trans}</td>
								<td>${p.posisi_idKet}</td>
								<td>
									<c:choose>
            							<c:when test="${jenistrans eq \"Pembelian\"}">
            								${p.supplier_idKet}
            							</c:when>
            							<c:when test="${jenistrans eq \"Penjualan\"}">
            								${p.customer_idKet}
            							</c:when>
            							<c:otherwise>
            								<c:if test="${not empty  p.supplier_idKet }">
            									${p.supplier_idKet}
            								</c:if>
            								<c:if test="${not empty  p.customer_idKet }">
            									 ${p.customer_idKet}
           									</c:if>
            							</c:otherwise>
            						</c:choose>
								</td>								
								<td><fmt:formatNumber pattern="#,##0">${p.total_harga}</fmt:formatNumber></td>
								<td><fmt:formatNumber pattern="#,##0"> ${p.total_disc}</fmt:formatNumber></td>
								<td>${p.createbyKet}</td>
								<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
								<td class="last">										
				                    <a href="javascript:window.top.location.href = '${path}/viewer/${p.trans_id}/view';" title="view">
				                      <img src="${path }/static/decorator/main/pilu/images/icons/view.png" alt="View" />
				                    </a>				                  
								</td>
							</tr>
							</c:forEach>
						</table>
            		</c:when>
            		<c:otherwise>
            			<table class="table">
							<tr>
								<c:if test="${pagename eq \"Input\" }">
									<th>
										<script>groupList('no_trans', 'form', '${page}', 'No Transaksi', '${sort}', '${sort_type}');</script>
									</th>
									<th>
										<script>groupList('no_trans_ref', 'form', '${page}', 'No Order', '${sort}', '${sort_type}');</script>
									</th>
								</c:if>
								<c:if test="${pagename eq \"OrderTransfer\" }">
									<th>
										<script>groupList('no_trans', 'form', '${page}', 'No Order', '${sort}', '${sort_type}');</script>
									</th>
								</c:if>
								
								<th>
									<c:choose>
            							<c:when test="${jenistrans eq \"Pembelian\"}">
            								<script>groupList('supplier_idKet', 'form', '${page}', 'Nama Supplier', '${sort}', '${sort_type}');</script>
            							</c:when>
            							<c:when test="${jenistrans eq \"Penjualan\"}">
            								<script>groupList('customer_idKet', 'form', '${page}', 'Nama Customer', '${sort}', '${sort_type}');</script>
            							</c:when>
            							<c:otherwise>
            								Nama Customer / Supplier
            							</c:otherwise>
            						</c:choose>
            						
								</th>
								<th>
									<script>groupList('total_harga', 'form', '${page}', 'Total Harga', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('total_disc', 'form', '${page}', 'Total Diskon', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('createbyKet', 'form', '${page}', 'Create By', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('createdate', 'form', '${page}', 'Create Date', '${sort}', '${sort_type}');</script>
								</th>
								 <th class="last">Actions</th>
							</tr>
			
							<c:forEach items="${listPaging}" var="p">
							<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
								<c:if test="${pagename eq \"Input\" }">
									<td>${p.no_trans}</td>
									<td>${p.no_trans_ref}</td>
								</c:if>
								<c:if test="${pagename eq \"OrderTransfer\" }">
									<td>${p.no_trans}</td>
								</c:if>
								<td>
									<c:choose>
            							<c:when test="${jenistrans eq \"Pembelian\"}">
            								${p.supplier_idKet}
            							</c:when>
            							<c:when test="${jenistrans eq \"Penjualan\"}">
            								${p.customer_idKet}
            							</c:when>
            							<c:otherwise>
            								<c:if test="${not empty  p.supplier_idKet }">
            									${p.supplier_idKet}
            								</c:if>
            								<c:if test="${not empty  p.customer_idKet }">
            									 ${p.customer_idKet}
           									</c:if>
            							</c:otherwise>
            						</c:choose>
								</td>
								<td><fmt:formatNumber pattern="#,##0">${p.total_harga}</fmt:formatNumber></td>
								<td><fmt:formatNumber pattern="#,##0"> ${p.total_disc}</fmt:formatNumber></td>
								<td>${p.createbyKet}</td>
								<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
								<td class="last">
										<c:choose>
											<c:when test="${pagename eq \"OrderTransfer\" }">
												<%-- <c:if test="${not empty p.print_order_form or not empty p.print_trans_date }"> --%>
													<a href="javascript:window.top.location.href = '${path}/transaksi/transfer/${jenistrans}/${pagename}/edit/${p.trans_id}';" title="Pilih Order">
								                      <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Pilih Order" />
								                    </a>
							                  <%--   </c:if> --%>
											</c:when>
											<c:when test="${pagename eq \"Gudang\" }">												
													<a href="javascript:if(confirm('Apakah ingin buka data transaksi no ${p.no_trans}?')){$('#trans_no', top.document).val('${p.no_trans}');$('#cari', top.document).click();parent.closeAction();lewat();}" title="Buka data transaksi no ${p.no_trans}">
								                      <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Buka data transaksi no ${p.no_trans}" />
								                    </a>							                
											</c:when>
		
											<c:otherwise>
												<a href="javascript:window.top.location.href = '${path}/transaksi/${jenistrans}/${pagename}/${p.trans_id}/edit';" title="edit">
							                      <img src="${path }/static/decorator/main/pilu/images/icons/application_edit.png" alt="Edit" />
							                    </a>
							                    <a href="javascript:window.top.location.href = '${path}/transaksi/${jenistrans}/${pagename}/${p.trans_id}/view';" title="view">
							                      <img src="${path }/static/decorator/main/pilu/images/icons/view.png" alt="View" />
							                    </a>
							                   <c:if test="${jenistrans eq \"Penjualan\" and pagename eq \"Order\" }">
							                   		 <a href="javascript:if(confirm('Are you sure want to Print Form Order?'))doAction('${path}/report/print_form_order/${p.no_trans}', 'Print Form Order',700,500,false,true);" title="Print Form Order">
								                      <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Form Order" />
								                    </a>
							                   		<%--  <c:if test="${not empty p.print_order_form }">
														<a href="javascript:if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${jenistrans}/${pagename}/show/${p.trans_id}';" title="transfer">
									                      <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Edit" />
									                    </a>
									                 </c:if> --%>
												</c:if>
												 <c:if test="${jenistrans eq \"Pembelian\" and pagename eq \"Order\" }">
							                   		 <a href="javascript:if(confirm('Are you sure want to Print Purchasing Order?'))doAction('${path}/report/print_po/${p.no_trans}', 'Print Form Order',700,500);" title="Print Purchasing Order">
								                      <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Purchasing Order" />
								                    </a>
							                   		<%--  <c:if test="${not empty p.print_order_form }">
														<a href="javascript:if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${jenistrans}/${pagename}/show/${p.trans_id}';" title="transfer">
									                      <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Edit" />
									                    </a>
									                 </c:if> --%>
												</c:if>
											</c:otherwise>
										</c:choose>
								</td>
							</tr>
							</c:forEach>
						</table>
            		</c:otherwise>
            	</c:choose>
	      		 	

                <div class="datacount">
	          	  Row Per Page
	            	<select name="rowcount" onchange="form.submit();">
	            		<c:forEach items="${listNumRows }" var="ln">
	            			<option value="${ln}" <c:if test="${rowcount eq ln}">selected="true"</c:if>>${ln}</option>
	            		</c:forEach>
	            	</select>
	            </div>
	            <div class="page">Page : ${page } / ${totalPage }</div>
                <div class="innerButtom">
                  <div class="actions">
                   <%--  <button class="button" type="button" onclick="window.location='${path}/transaksi/${jenistrans}/${pagename}/new';">
                      <img src="${path }/static/decorator/main/pilu/images/icons/add.gif" alt="Add New" /> Add New
                    </button> --%>
                  </div>
                  <div class="pagination">
                  	<input type="hidden" name="page" id="page" value="${page }">
                  	<input type="hidden" name="sort" id="sort" value="${sort }">
                  	<input type="hidden" name="st" id="st" value="${sort_type}">
                  	<c:choose>
                  		<c:when test="${totalPage eq \"1\" }">
                  			<span class="current">1</span>
                  		</c:when>
                  		<c:when test="${totalPage lt \"10\" }">
                  			<c:choose>
               					<c:when test="${page eq 1 }">
               						<span class="disabled prev_page">� Previous</span>
               					</c:when>
               					<c:otherwise>
               						<a class="prev_page" href="javascript:gotoPage('${page-1 }','${sort }','${sort_type}','form')">� Previous</a>
               					</c:otherwise>
               				</c:choose>

                  			<c:forEach items="${pages}" var="p" varStatus="s">
                  				<c:choose>
                  					<c:when test="${p eq page }">
                  						<span  class="current">${p}</span>
                  					</c:when>
                  					<c:otherwise>
                  						<a href="javascript:gotoPage('${p}','${sort }','${sort_type}','form');">${p}</a>
                  					</c:otherwise>
                  				</c:choose>

                  			</c:forEach>
                  			<c:choose>
               					<c:when test="${page eq totalPage }">
               						<span class="disabled next_page">Next �</span>
               					</c:when>
               					<c:otherwise>
               						<a class="next_page" href="javascript:gotoPage('${page+1 }','${sort }','${sort_type}','form')">Next �</a>
               					</c:otherwise>
               				</c:choose>
                  		</c:when>
                  		<c:when test="${totalPage gt \"9\" }">

                  			<c:choose>
               					<c:when test="${page eq 1 }">
               						<span class="disabled prev_page">� Previous</span>
               					</c:when>
               					<c:otherwise>
               						<a class="prev_page" href="javascript:gotoPage('${page-1 }','${sort }','${sort_type}','form')">� Previous</a>
               					</c:otherwise>
               				</c:choose>
                  			<c:forEach items="${pages}" var="p" varStatus="s">
                  				<c:choose>
                  					<c:when test="${p eq 1 or p eq 2 or p eq totalPage or p eq totalPage-1}">
               							<c:choose>
		                  					<c:when test="${p eq page }">
		                  						<span  class="current">${p}</span>
		                  					</c:when>
		                  					<c:otherwise>
		                  						<a href="javascript:gotoPage('${p}','${sort }','${sort_type}','form');">${p}</a>
		                  					</c:otherwise>
		                  				</c:choose>
                  					</c:when>
                  					<c:when test="${p eq halfPage}">
                  						<div class="middle">...</div>
                  						<c:choose>
                  							<c:when test="${page gt 2 and page lt totalPage-1  }">
                  								<input type="text" value="${page}" name="p" class="middle" onchange="gotoPage(this.value,'${sort }','${sort_type}','form');">
                  							</c:when>
                  							<c:otherwise>
                  								<input type="text" value="${p}" name="p" class="middle_current" onchange="gotoPage(this.value,'${sort }','${sort_type}','form');">
                  							</c:otherwise>
                  						</c:choose>
                  						<div class="middle">...</div>

                  					</c:when>

                  				</c:choose>

                  			</c:forEach>

                  			<c:choose>
               					<c:when test="${page eq totalPage }">

               						<span class="disabled next_page">Next �</span>
               					</c:when>
               					<c:otherwise>
               						<a class="next_page" href="javascript:gotoPage('${page+1 }','${sort }','${sort_type}','form')">Next �</a>
               					</c:otherwise>
               				</c:choose>
                  		</c:when>
                  	</c:choose>

                  </div>
                </div>
            </div>






</form>







</body>
</html>