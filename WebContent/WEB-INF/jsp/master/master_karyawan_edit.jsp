<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Karyawan &gt; ${karyawan.mode } </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${karyawan.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="karyawan" name="formpost" method="POST" action="${path}/master/karyawan/save" cssClass="form" >
   		<div class="columns wat-cf">
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="nik" cssClass="label" cssErrorClass="label labelError">NIK</form:label>
	            	<form:hidden path="id"/>
	                <form:errors path="nik" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="nik" /><input type="text" class="text_field read" value="${karyawan.nik }" readonly="readonly" size="10" /></c:when>
					<c:otherwise><form:input path="nik"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="10" maxlength="10"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="jenis" cssClass="label" cssErrorClass="label labelError">Posisi Jabatan</form:label>
	                <form:errors path="jenis" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="jenis" /><input type="text" class="text_field read" value="${karyawan.jeniskry }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:select path="jenis">
							<form:option value="">Silahkan Pilih Posisi</form:option>
							<form:options items="${reff.AllKaryawan}" itemValue="key" itemLabel="value"/>
						</form:select>
					</c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama</form:label>
	                <form:errors path="nama" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${karyawan.nama }" readonly="readonly" size="80" /></c:when>
					<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="80" maxlength="80"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	
			<div class="space"><br/></div>    
		    <div class="group navform wat-cf">
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="tgl_masuk"  cssClass="label" cssErrorClass="label labelError">Tanggal Masuk</form:label>
			               <form:errors path="tgl_masuk" cssClass="error" />
				     </div>
			     	 <c:choose>
			     	 	<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="tgl_masuk" /><input type="text" class="text_field datepicker read" value="<fmt:formatDate value="${karyawan.tgl_masuk }" pattern="dd-MM-yyyy HH:mm:ss"/>" readonly="readonly"/></c:when>
						<c:otherwise>
							<form:input path="tgl_masuk"  cssClass="text_field datepicker"  size="10"  />
						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="tgl_keluar"  cssClass="label" cssErrorClass="label labelError">Tanggal Keluar</form:label>
			               <form:errors path="tgl_keluar" cssClass="error" />
				     </div>
			     	 <c:choose>
						<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="tgl_keluar" /><input type="text" class="text_field datepicker read" value="<fmt:formatDate value="${karyawan.tgl_keluar }" pattern="dd-MM-yyyy HH:mm:ss"/>" readonly="readonly"/></c:when>
						<c:otherwise>
							<form:input path="tgl_keluar"  cssClass="text_field datepicker"  size="10" />
						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
			</div>
			<div class="space"><br/></div>    
		    <div class="group navform wat-cf">
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="gaji"  cssClass="label" cssErrorClass="label labelError">Gaji Pokok</form:label>
			               <form:errors path="gaji" cssClass="error" />
				     </div>
			     	 <c:choose>
						<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="gaji" /><input type="text" class="text_field nominal read" value="${karyawan.gaji}" readonly="readonly" size="15"/></c:when>
						<c:otherwise>
							<form:input path="gaji"  cssClass="text_field nominal"  size="15" />
						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="makan"  cssClass="label" cssErrorClass="label labelError">Uang Makan</form:label>
			               <form:errors path="makan" cssClass="error" />
				     </div>
			     	 <c:choose>
						<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="makan" /><input type="text" class="text_field nominal read" value="${karyawan.makan}" readonly="readonly" size="15"/></c:when>
						<c:otherwise>
							<form:input path="makan"  cssClass="text_field nominal"  size="15" />
						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="transport"  cssClass="label" cssErrorClass="label labelError">Uang Transport</form:label>
			               <form:errors path="transport" cssClass="error" />
				     </div>
			     	 <c:choose>
						<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="transport" /><input type="text" class="text_field nominal read" value="${karyawan.transport}" readonly="readonly" size="15"/></c:when>
						<c:otherwise>
							<form:input path="transport"  cssClass="text_field nominal"  size="15" />
						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
		    </div>
        	
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${karyawan.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/master/karyawan'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/karyawan'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>