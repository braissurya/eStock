<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Konfigurasi &gt; ${config.mode } </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${config.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="config" name="formpost" method="POST" action="${path}/admin/config/save" cssClass="form" >
   		<div class="columns wat-cf">
   			<div class="group">
	            <div class="fieldWithErrors">
		            <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID</form:label>
		            <form:errors path="id" cssClass="error" />
	            </div>
	        	<form:hidden path="id" /><input type="text" class="text_field read" value="${config.id }"  cssErrorClass="text_field inputError"  readonly="readonly" />
            	<span class="description"></span>
            </div>
        	<div class="group">
        		<div class="fieldWithErrors">
	            	<form:label path="jenis" cssClass="label" cssErrorClass="label labelError">Jenis</form:label>
	                <form:errors path="jenis" cssClass="error" />
	        	</div>
	        	<form:hidden path="jenis" /><input type="text" class="text_field read" value="${config.jenis }"  cssErrorClass="text_field inputError"  readonly="readonly" />
            	<span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="keterangan" cssClass="label" cssErrorClass="label labelError">Keterangan</form:label>
	                <form:errors path="keterangan" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${config.mode eq 'VIEW'}"><form:hidden path="keterangan" /><input type="text" class="text_field read" value="${config.keterangan }" readonly="readonly" size="60"/></c:when>
					<c:otherwise><form:input path="keterangan"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="60" maxlength="60"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${config.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/admin/config'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/admin/config'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>