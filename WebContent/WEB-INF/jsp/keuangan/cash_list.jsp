<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script>
		$().ready(function(){
			//$("form:input:visible:first").focus();
			$("#txtSearch").focus();
		});	
	</script>
</head>
<body>

	<form name="form" id="form" method="post">
	<h1> Daftar Transaksi ${pagename}</h1>

<!-- 	<div class="notrans"> -->
<!-- 		Nomor Transaksi ${pagename}: <input type="text" name="nt"  value="${no_transaksi}" size="30" class="textfield target"> -->
<!-- 	</div> -->

    <div class="total">Total Data : ${totalData }</div>
    <div class="search">
    	Quick Find: <input type="text" name="s"  value="${search}" size="30" class="textfield" id="txtSearch">
    	<input type="submit" name="btsearch"  value=" " class="btsearch" title="search">

    </div>

            <div class="inner">
      		 	<table class="table">
      		 		<c:choose>
						<c:when test="${jenispayment eq \"inListTrans\" or jenispayment eq \"outListTrans\"}">
							<tr>
								<th>
									<script>groupList('no_trans', 'form', '${page}', 'No Order', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('customer_idKet', 'form', '${page}', 'Nama Customer', '${sort}', '${sort_type}');</script>
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
						</c:when>
						<c:otherwise>
							<tr>
								<th>
									<script>groupList('no_payment', 'form', '${page}', 'No Transaksi', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('no_trans', 'form', '${page}', 'No Transaksi Penjualan/Pembelian', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('paid_date', 'form', '${page}', 'Tgl Bayar', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('cara_bayarKet', 'form', '${page}', 'Cara Bayar', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('nominal', 'form', '${page}', 'Nominal', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('createbyKet', 'form', '${page}', 'Create By', '${sort}', '${sort_type}');</script>
								</th>
								<th>
									<script>groupList('createdate', 'form', '${page}', 'Create Date', '${sort}', '${sort_type}');</script>
								</th>
								 <th class="last">Actions</th>
							</tr>
						</c:otherwise>
					</c:choose>

					<c:forEach items="${listPaging}" var="p">
					<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
						<c:choose>
							<c:when test="${jenispayment eq \"inListTrans\" or jenispayment eq \"outListTrans\"}">
								<td>${p.no_trans}</td>
								<td>${p.customer_idKet}</td>
								<td><fmt:formatNumber pattern="#,##0">${p.total_harga}</fmt:formatNumber></td>
								<td><fmt:formatNumber pattern="#,##0"> ${p.total_disc}</fmt:formatNumber></td>
								<td>${p.createbyKet}</td>
								<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
<!-- 								<td class="last"> -->
<!-- 									<a href="javascript:window.top.location.href = '${path}/transaksi/transfer/${jenistrans}/${pagename}/edit/${p.trans_id}';" title="edit"> -->
<!-- 								    	<img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Edit" /> -->
<!-- 								    </a> -->
<!-- 								</td> -->
								<td class="last">
									<a href="javascript:$('#no_trans', top.document).val('${p.no_trans}');$('.add_new', top.document).click();parent.closeAction();lewat();" title="edit">
								    	<img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Edit" />
								    </a>
								</td>
							</c:when>
							<c:otherwise>
								<td>${p.no_payment}</td>
								<td>${p.no_trans}</td>
								<td><fmt:formatDate value="${p.paid_date}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
								<td>${p.cara_bayarKet}</td>
								<td><fmt:formatNumber pattern="#,##0"> ${p.nominal}</fmt:formatNumber></td>
								<td>${p.createbyKet}</td>
								<td><fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/></td>
								<td class="last">
									<a href="javascript:window.top.location.href = '${path}/keuangan/Cash/${jenispayment}/${p.payment_id}/edit';" title="edit">
					                	<img src="${path }/static/decorator/main/pilu/images/icons/application_edit.png" alt="Edit" />
					                </a>
					                <a href="javascript:window.top.location.href = '${path}/keuangan/Cash/${jenispayment}/${p.payment_id}/view';" title="view">
					                	<img src="${path }/static/decorator/main/pilu/images/icons/view.png" alt="View" />
					                </a>
					            </td>
							</c:otherwise>
						</c:choose>
					</tr>
					</c:forEach>
				</table>

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
               						<span class="disabled prev_page">« Previous</span>
               					</c:when>
               					<c:otherwise>
               						<a class="prev_page" href="javascript:gotoPage('${page-1 }','${sort }','${sort_type}','form')">« Previous</a>
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
               						<span class="disabled next_page">Next »</span>
               					</c:when>
               					<c:otherwise>
               						<a class="next_page" href="javascript:gotoPage('${page+1 }','${sort }','${sort_type}','form')">Next »</a>
               					</c:otherwise>
               				</c:choose>
                  		</c:when>
                  		<c:when test="${totalPage gt \"9\" }">

                  			<c:choose>
               					<c:when test="${page eq 1 }">
               						<span class="disabled prev_page">« Previous</span>
               					</c:when>
               					<c:otherwise>
               						<a class="prev_page" href="javascript:gotoPage('${page-1 }','${sort }','${sort_type}','form')">« Previous</a>
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

               						<span class="disabled next_page">Next »</span>
               					</c:when>
               					<c:otherwise>
               						<a class="next_page" href="javascript:gotoPage('${page+1 }','${sort }','${sort_type}','form')">Next »</a>
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