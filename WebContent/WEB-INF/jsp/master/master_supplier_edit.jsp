<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Supplier &gt; ${supplier.mode } </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${supplier.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="supplier" name="formpost" method="POST" action="${path}/master/supplier/save" cssClass="form" >
   		<div class="columns wat-cf">
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="kode" cssClass="label" cssErrorClass="label labelError">Kode Supplier</form:label>
		            <form:hidden path="id" />
	                <form:errors path="kode" cssClass="error" />
	        	</div>            	
	        	<form:hidden path="kode" /><input type="text" class="text_field read" value="${supplier.kode }"  cssErrorClass="text_field inputError"  readonly="readonly" />
            	<span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama</form:label>
	                <form:errors path="nama" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${supplier.nama }" readonly="readonly" size="50"/></c:when>
					<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="50" maxlength="50"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
			<div class="space"><br/></div>    
   			<div class="group navform wat-cf"> 
				<div class="group">
					<div class="fieldWithErrors">
						<form:label path="alamat" cssClass="label" cssErrorClass="label labelError">Alamat</form:label>
					    <form:errors path="alamat" cssClass="error" />
					</div> 
					<c:choose>
						<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="alamat" /><input type="text" class="text_field read" value="${supplier.alamat }" readonly="readonly" size="100"/></c:when>
						<c:otherwise><form:textarea path="alamat"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="100" maxlength="100"/></c:otherwise>
					</c:choose>
				    <span class="description"></span>
				</div>
				<div class="group">
					<div class="fieldWithErrors">
						<form:label path="kota" cssClass="label" cssErrorClass="label labelError">Kota</form:label>
					    <form:errors path="kota" cssClass="error" />
					</div>
					<c:choose>
						<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="kota" /><input type="text" class="text_field read" value="${supplier.kota }" readonly="readonly" size="30"/></c:when>
						<c:otherwise><form:input path="kota"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="30" maxlength="60"/></c:otherwise>
					</c:choose>
				    <span class="description"></span>
				</div>
			</div>
			<div class="space"><br/></div>      
        	<div class="group">
        		<div class="fieldWithErrors">
	            	<form:label path="contact" cssClass="label" cssErrorClass="label labelError">Contact Person</form:label>
	                <form:errors path="contact" cssClass="error" />
	        	</div>
	        	<c:choose>
					<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="contact" /><input type="text" class="text_field read" value="${supplier.contact }" readonly="readonly" size="30"/></c:when>
					<c:otherwise><form:input path="contact"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="30" maxlength="30"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="group">
        		<div class="fieldWithErrors">
	            	<form:label path="no_telp" cssClass="label" cssErrorClass="label labelError">No Telepon</form:label>
	                <form:errors path="no_telp" cssClass="error" />
	        	</div>
	        	<c:choose>
					<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="no_telp" /><input type="text" class="text_field read" value="${supplier.no_telp }" readonly="readonly" size="11"/></c:when>
					<c:otherwise><form:input path="no_telp"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="11" maxlength="50"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="group">
        		<div class="fieldWithErrors">
	            	<form:label path="no_hp" cssClass="label" cssErrorClass="label labelError">No Handphone</form:label>
	                <form:errors path="no_hp" cssClass="error" />
	        	</div>
	        	<c:choose>
					<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="no_hp" /><input type="text" class="text_field read" value="${supplier.no_hp }" readonly="readonly" size="12"/></c:when>
					<c:otherwise><form:input path="no_hp"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="12" maxlength="30"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="group">
        		<div class="fieldWithErrors">
	            	<form:label path="no_fax" cssClass="label" cssErrorClass="label labelError">No Fax</form:label>
	                <form:errors path="no_fax" cssClass="error" />
	        	</div>
	        	<c:choose>
					<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="no_fax" /><input type="text" class="text_field read" value="${supplier.no_fax }" readonly="readonly" size="11"/></c:when>
					<c:otherwise><form:input path="no_fax"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="11" maxlength="30"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="group">
        		<div class="fieldWithErrors">
	            	<form:label path="email" cssClass="label" cssErrorClass="label labelError">Email</form:label>
	                <form:errors path="email" cssClass="error" />
	        	</div>
	        	<c:choose>
					<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="email" /><input type="text" class="text_field read" value="${supplier.email }" readonly="readonly" size="60"/></c:when>
					<c:otherwise><form:input path="email"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="60" maxlength="60"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="space"><br/></div>
        	<div class="group navform wat-cf">
	        	<div class="group">
	        		<div class="fieldWithErrors">
		            	<form:label path="pkp" cssClass="label" cssErrorClass="label labelError">Status</form:label>
		                <form:errors path="pkp" cssClass="error" />
		        	</div>
		        	<c:choose>
						<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="pkp" />
							<c:choose>
								<c:when test="${supplier.pkp eq \"1\" }">
									<c:set  var="appr" value="PKP" />
								</c:when>
								<c:otherwise>
									<c:set  var="appr" value="PTKP" />
								</c:otherwise>
							</c:choose>
							<input type="text" class="text_field read" value="${appr}" readonly="readonly" size="50"/>
						</c:when>
						<c:otherwise>
							<form:radiobutton path="pkp" value="1" label="PKP" cssClass="target" cssErrorClass="target error"/>
							<form:radiobutton path="pkp" value="0" label="PTKP"/>
						</c:otherwise>
			  		</c:choose>
	                  <span class="description"></span>
	        	</div>
	        </div>
        	<div class="space"><br/></div>
        	<div class="group navform wat-cf">
	        	<div class="group">
	        		<div class="fieldWithErrors">
		            	<form:label path="hutang" cssClass="label" cssErrorClass="label labelError">Total Hutang</form:label>
		                <form:errors path="hutang" cssClass="error" />
		        	</div>
			     	<input type="text" class="text_field nominal read" value="${customer.hutang}" readonly="readonly" size="15"/>
	            	<span class="description"></span>
	        	</div>
	        </div>
        	        	        	        	
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${supplier.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/master/supplier'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/supplier'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>