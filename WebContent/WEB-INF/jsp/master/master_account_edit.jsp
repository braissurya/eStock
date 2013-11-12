<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Account &gt; ${account.mode } </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${account.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="account" name="formpost" method="POST" action="${path}/master/account/save" cssClass="form" >
   		<div class="columns wat-cf">
   			<div class="group">
	            <div class="fieldWithErrors">
		            <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID</form:label>
		            <form:errors path="id" cssClass="error" />
	            </div>
	        	<form:hidden path="id" /><input type="text" class="text_field read" value="${account.id }"  cssErrorClass="text_field inputError"  readonly="readonly" />
            	<span class="description"></span>
            </div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="id_bank" cssClass="label" cssErrorClass="label labelError">Bank</form:label>
	                <form:errors path="id_bank" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${account.mode eq 'VIEW'}"><form:hidden path="id_bank" /><input type="text" class="text_field read" value="${account.namabank }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:select path="id_bank">
							<form:option value="">Silahkan Pilih Bank</form:option>
							<form:options items="${reff.AllBank}" itemValue="key" itemLabel="value"/>
						</form:select>
					</c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="cabang" cssClass="label" cssErrorClass="label labelError">Cabang</form:label>
	                <form:errors path="cabang" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${account.mode eq 'VIEW'}"><form:hidden path="cabang" /><input type="text" class="text_field read" value="${account.cabang }" readonly="readonly" size="80"/></c:when>
					<c:otherwise><form:input path="cabang"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="80" maxlength="80"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="space"><br/></div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="no_rek" cssClass="label" cssErrorClass="label labelError">No Rekening</form:label>
	                <form:errors path="no_rek" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${account.mode eq 'VIEW'}"><form:hidden path="no_rek" /><input type="text" class="text_field read" value="${account.no_rek }" readonly="readonly" size="20"/></c:when>
					<c:otherwise><form:input path="no_rek"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="20" maxlength="20"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="kurs" cssClass="label" cssErrorClass="label labelError">Kurs</form:label>
	                <form:errors path="kurs" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${account.mode eq 'VIEW'}"><form:hidden path="kurs" /><input type="text" class="text_field read" value="${account.namakurs }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:select path="kurs">
							<form:option value="">Silahkan Pilih Kurs</form:option>
							<form:options items="${reff.AllKurs}" itemValue="key" itemLabel="value"/>
						</form:select>
					</c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="atas_nama" cssClass="label" cssErrorClass="label labelError">Atas Nama</form:label>
	                <form:errors path="atas_nama" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${account.mode eq 'VIEW'}"><form:hidden path="atas_nama" /><input type="text" class="text_field read" value="${account.atas_nama }" readonly="readonly" size="80" /></c:when>
					<c:otherwise><form:input path="atas_nama"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="80" maxlength="80"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="coa_id" cssClass="label" cssErrorClass="label labelError">ID COA</form:label>
	                <form:errors path="coa_id" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${account.mode eq 'VIEW'}"><form:hidden path="coa_id" /><input type="text" class="text_field read" value="${account.coa_id }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:select path="coa_id">
							<form:option value="">Silahkan Pilih ID COA</form:option>
							<form:options items="${reff.AllCOA}" itemValue="key" itemLabel="value"/>
						</form:select>
					</c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="space"></div>
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${account.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/master/account'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/account'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>