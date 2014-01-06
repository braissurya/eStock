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
		<h1> Master Account</h1>
		<div class="total">Total Data : ${totalData }</div>
    	<div class="search">
    		Quick Find: <input type="text" name="s"  value="${search}" size="30" class="textfield" id="txtSearch">
    		<input type="submit" name="btsearch"  value=" " class="btsearch" title="search">
    	</div>	
		<div class="inner">
       		<table class="table">
       			<tr>
       				<th>
       					<script>groupList('namabank', 'form', '${page}', 'Bank', '${sort}', '${sort_type}');</script>
       				</th>
       				<th>
       					<script>groupList('cabang', 'form', '${page}', 'Cabang', '${sort}', '${sort_type}');</script>
       				</th>
       				<th>
       					<script>groupList('no_rek', 'form', '${page}', 'No Rekening', '${sort}', '${sort_type}');</script>
       				</th>
       				<th>
       					<script>groupList('namakurs', 'form', '${page}', 'Kurs', '${sort}', '${sort_type}');</script>
       				</th>
       				<th>
       					<script>groupList('atas_nama', 'form', '${page}', 'Atas Nama', '${sort}', '${sort_type}');</script>
       				</th>
       				<th>
       					<script>groupList('coa_id', 'form', '${page}', 'ID COA', '${sort}', '${sort_type}');</script>
       				</th>
					<th>
						<script>groupList('createuser', 'form', '${page}', 'Create By', '${sort}', '${sort_type}');</script>
					</th>
					<th>
						<script>groupList('createdate', 'form', '${page}', 'Create Date', '${sort}', '${sort_type}');</script>
					</th> 
					<th class="last">
						Actions
					</th>
       			</tr>
       			<c:forEach items="${listPaging}" var="p" varStatus="s">
       				<tr <c:choose><c:when test="${(s.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
       					<td>
       						${p.namabank}
       					</td>
       					<td>
       						${p.cabang}
       					</td>
       					<td>
       						${p.no_rek}
       					</td>
       					<td>
       						${p.namakurs}
       					</td>
       					<td>
       						${p.atas_nama}
       					</td>
       					<td>
       						${p.coa_id}
       					</td>
       					<td>
       						${p.createuser}
       					</td>
       					<td>
       						<fmt:formatDate value="${p.createdate}" pattern="dd-MM-yyyy (HH:mm:ss)"/>
       					</td>
       					<td class="last">
							<a href="${path}/master/account/edit/${p.id}/${groupmenuid}" title="edit">
			                      <img src="${path }/static/decorator/main/pilu/images/icons/application_edit.png" alt="Edit" />
			                </a>
			                <a href="${path}/master/account/view/${p.id}/${groupmenuid}" title="view">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/view.png" alt="View" />
			                </a>
			                <a href="javascript:if(confirm('Are you sure want to Delete \'Id : ${p.id} , Group Name : ${groupmenuName}\' ?'))window.location='${path}/master/account/delete/${p.id}/${groupmenuid}';" title="delete">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/delete.png" alt="Delete" />
			                </a>
						</td>
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
		        <div class="actions">
			        <button class="button" type="button" onclick="window.location='${path}/master/account/new';">
			       		<img src="${path }/static/decorator/main/pilu/images/icons/add.gif" alt="Add New" /> Add New
			        </button>
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
