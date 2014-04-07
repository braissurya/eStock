<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Menu &gt; ${menu.mode } </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${menu.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
	<form:form commandName="menu" name="formpost" method="POST" action="${path}/admin/menu/save" cssClass="form" >
			  <div class="columns wat-cf">
		  		 <div class="group">
                  <div class="fieldWithErrors">
                   <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Menu</form:label>
                   <form:errors path="id" cssClass="error" />
                   </div>
	              <form:hidden path="id" /><input type="text" class="text_field read" value="${menu.id }"  cssErrorClass="text_field inputError"  readonly="readonly" />
                  <span class="description"></span>
                </div>
                
                <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="parent" cssClass="label" cssErrorClass="label labelError">Parent Menu</form:label>
	                    <form:errors path="parent" cssClass="error" />
	              </div>
                 <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="parent" /><input type="text" class="text_field read" value="${menu.parent_nama }" readonly="readonly" /></c:when>
						<c:otherwise>
							<form:select path="parent"  cssErrorClass="inputError" >
								<c:forEach items="${reff.parentMenu}" var="p">
									<form:option value="${p.key }"><c:if test="${p.desc ne \"1\"}"><c:forEach begin="0" end="${p.desc}">&nbsp;&nbsp;&nbsp;</c:forEach></c:if>${p.value }</form:option>
								</c:forEach>
							</form:select>
						</c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama Menu</form:label>
	                    <form:errors path="nama" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${menu.nama }" readonly="readonly" size="80"/></c:when>
						<c:otherwise><form:input path="nama"   cssClass="text_field target"  cssErrorClass="text_field inputError target"  size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="link" cssClass="label" cssErrorClass="label labelError">Link</form:label>
	                    <form:errors path="link" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="link" /><input type="text" class="text_field read" value="${menu.link }" readonly="readonly" size="80"/></c:when>
						<c:otherwise><form:input path="link"  id="target" cssClass="text_field"  cssErrorClass="text_field inputError"  size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
			  </div>
                
                 <div class="group">
               	  <div class="fieldWithErrors">
	                   <form:label path="urut" cssClass="label" cssErrorClass="label labelError">Urut</form:label>
	                    <form:errors path="urut" cssClass="error" />
	              </div>
                  <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="urut" /><input type="text" class="text_field read" value="${menu.urut }" readonly="readonly" size="80"/></c:when>
						<c:otherwise><form:input path="urut"  cssClass="text_field"  cssErrorClass="text_field inputError"  size="80" maxlength="80"/></c:otherwise>
				   </c:choose>
                  <span class="description"></span>
                </div>
                
                
                
			  <div class="group navform wat-cf">
     			  <c:choose>
						<c:when test="${menu.mode eq 'VIEW'}"><form:hidden path="id" />
							<button class="button" type="button" onclick="window.location='${path}/admin/menu'">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			                  </button>
						</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			                  </button>
			                  <form:hidden path="mode"/>
			                  <span class="text_button_padding"></span>
			                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/admin/menu'">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                  </button>
						</c:otherwise>
				   </c:choose>
                  
                </div>
			
			
		
			
	</form:form>
</body>
</html>		