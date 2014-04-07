<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Cabang &gt; ${cabang.mode } </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${cabang.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="cabang" name="formpost" method="POST" action="${path}/master/cabang/save" cssClass="form" >
   		<div class="columns wat-cf">
   			<div class="group">
	            <div class="fieldWithErrors">
		            <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID Cabang</form:label>
		            <form:errors path="id" cssClass="error" />
	            </div>
	        	<form:hidden path="id" /><input type="text" class="text_field read" value="${cabang.id }"  cssErrorClass="text_field inputError"  readonly="readonly" />
            	<span class="description"></span>
            </div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="kode" cssClass="label" cssErrorClass="label labelError">Kode</form:label>
	                <form:errors path="kode" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${cabang.mode eq 'VIEW'}"><form:hidden path="kode" /><input type="text" class="text_field read" value="${cabang.kode }" readonly="readonly" size="2"/></c:when>
					<c:otherwise><form:input path="kode"  id="target" cssClass="text_field target" cssErrorClass="text_field inputError target" size="2" maxlength="2"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama</form:label>
	                <form:errors path="nama" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${cabang.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${cabang.nama }" readonly="readonly" size="60"/></c:when>
					<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="60" maxlength="60"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="group">
        		<div class="fieldWithErrors">
	            	<form:label path="jenis" cssClass="label" cssErrorClass="label labelError">Jenis</form:label>
	                <form:errors path="jenis" cssClass="error" />
	        	</div>
	        	<c:choose>
					<c:when test="${cabang.mode eq 'VIEW'}"><form:hidden path="jenis" /><input type="text" class="text_field read" value="${cabang.jeniscabang }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:select path="jenis">
							<form:option value="">Silahkan Pilih Jenis Cabang</form:option>
							<form:options items="${reff.JenisCabang}" itemValue="key" itemLabel="value"/>
						</form:select>
					</c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${cabang.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/master/cabang'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/cabang'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>