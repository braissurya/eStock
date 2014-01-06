<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Group Menu  &gt; ${groupuser.mode }  </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${groupuser.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
     <form:form commandName="groupuser" name="formpost" method="POST" action="${path}/admin/groupuser/save" cssClass="form" >
       	 <div class="group">
             <div class="fieldWithErrors">
              	<form:label path="id"  cssClass="label">ID Group Menu</form:label>
               	<form:errors path="id" cssClass="error" />
          	</div>
          	<form:hidden path="id" /><input type="text" class="text_field read" value="${groupuser.id }" readonly="readonly" />
 			<span class="description"></span>
         </div>
            
         <div class="group">
       	  	<div class="fieldWithErrors">
           		<form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama  Group Menu</form:label>
             	<form:errors path="nama" cssClass="error" />
           </div>
           <c:choose>
				<c:when test="${groupuser.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${groupuser.nama }" readonly="readonly" /></c:when>
				<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="80" maxlength="80"/></c:otherwise>
		   </c:choose>
           <span class="description"></span>
        </div>
            
        <table class="table">
          	<tr>
          		<th>Parent </th>
				<th>Menu</th>
				<th>Link</th>
				<th>Akses</th>
			</tr>
           	<c:forEach items="${groupuser.menu}" var="p" varStatus="vs">
				<tr <c:choose><c:when test="${(vs.count % 2) eq 0}">class="odd"</c:when><c:otherwise>class="even"</c:otherwise></c:choose>>
					<td>${p.parent_nama}
						<form:hidden path="menu[${vs.index}].parent_nama"/>
					</td>
					<td>${p.nama}
						<form:hidden path="menu[${vs.index}].nama"/>
					</td>
					<td>${p.link}
						<form:hidden path="menu[${vs.index}].link"/>
					</td>
					<td>
						<form:checkbox path="menu[${vs.index}].akses"/>
						<form:hidden path="menu[${vs.index}].id"/>
					</td>
	
				</tr>
			</c:forEach>
		</table> 
                
              <br/>  
	   <div class="group navform wat-cf">
		  	<c:choose>
				<c:when test="${groupuser.mode eq 'VIEW'}"><form:hidden path="id" />
					<button class="button" type="button" onclick="window.location='${path}/admin/groupuser'">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
	                  </button>
				</c:when>
				<c:otherwise>
					<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
	                  </button>
	                  <form:hidden path="mode"/>
	                  <span class="text_button_padding"></span>
	                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/admin/groupuser'">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
	                  </button>
				</c:otherwise>
		   </c:choose>
                  
      </div>
</form:form>		
 	
</body>
</html>		