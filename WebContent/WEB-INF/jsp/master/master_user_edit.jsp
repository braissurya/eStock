<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
	
</script>
</head>
<body>
	<h3 class="tit">Master User &gt; ${user.mode }  </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${user.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
    <form:form commandName="user" name="formpost" method="POST" action="${path}/admin/user/save" cssClass="form" >
              
		
	<div class="group">
        <div class="fieldWithErrors">
         	<form:label path="group_user_id"  cssClass="label" cssErrorClass="label labelError">Group User</form:label>
        </div>
   		<c:choose>
   			<c:when test="${groupuserid eq\"-1\"}">
   				<c:choose>
					<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="group_user_id" /><input type="text" class="text_field read" value="${user.namaGroup }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:select path="group_user_id">
							<form:option value="">Silahkan Pilih Group User</form:option>
							<form:options items="${reff.AllGroupUser}" itemValue="key" itemLabel="value"/>
						</form:select>
					</c:otherwise>
			   </c:choose>
   			</c:when>
   			<c:otherwise>
   				<form:hidden path="group_user_id" /><input type="text" class="text_field read" value="${groupuserName}" readonly="readonly" size="20"/>
   			</c:otherwise>
   		</c:choose>
		<span class="description"></span>
     </div>
			 
	<div class="group">
         <div class="fieldWithErrors">
               <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID User</form:label>
               <form:errors path="id" cssClass="error" />
	     </div>
     	 <form:hidden path="id" /><input type="text" class="text_field read" value="${user.id }" readonly="readonly" size="4"/>
		 <span class="description"></span>
    </div>
    <div class="group">
        <div class="fieldWithErrors">
             <form:label path="flag_approval" cssClass="label" cssErrorClass="label labelError">Type User</form:label>
              <form:errors path="flag_approval" cssClass="error" />
        </div>
        <c:choose>
			<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="flag_approval" />
				<c:choose>
					<c:when test="${user.flag_approval eq \"1\" }">
						<c:set  var="appr" value="User Approval" />
					</c:when>
					<c:otherwise>
						<c:set  var="appr" value="User Regular" />
					</c:otherwise>
				</c:choose>
				<input type="text" class="text_field read" value="${appr}" readonly="readonly" size="50"/>
			</c:when>
			<c:otherwise>
				<form:radiobutton path="flag_approval" value="1" label="User Approval"/>
				<form:radiobutton path="flag_approval" value="0" label="User Regular"/>
			</c:otherwise>
	   </c:choose>
       <span class="description"></span>
   </div>
    <div class="group">
        <div class="fieldWithErrors">
             <form:label path="username" cssClass="label" cssErrorClass="label labelError">User Name</form:label>
              <form:errors path="username" cssClass="error" />
        </div>
        <c:choose>
			<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="username" /><input type="text" class="text_field read" value="${user.username }" readonly="readonly" size="50"/></c:when>
			<c:otherwise><form:input path="username"  id="target" cssClass="text_field" size="50" /></c:otherwise>
	   </c:choose>
       <span class="description"></span>
   </div>
   <div class="group">
   	  	<div class="fieldWithErrors">
             <form:label path="password" cssClass="label" cssErrorClass="label labelError">Password</form:label>
              <form:errors path="password" cssClass="error" />
        </div>
                	
		<form:hidden path="password" />
		<form:hidden path="passwordDecrypt" />
		<c:choose>
			<c:when test="${user.passwordDecrypt eq \"123bcd\" or empty user.passwordDecrypt }">
				<input type="text" class="text_field read" value="default : 123bcd" readonly="readonly" size="20"/>
			</c:when>
			<c:otherwise>
				<input type="text" class="text_field read" value="**********" readonly="readonly" size="20"/>
			</c:otherwise>
		</c:choose>
        <span class="description"></span>
   </div>
   <div class="group">
        <div class="fieldWithErrors">
             <form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama Lengkap</form:label>
              <form:errors path="nama" cssClass="error" />
        </div>
        <c:choose>
			<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${user.nama }" readonly="readonly" size="50"/></c:when>
			<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" size="50" /></c:otherwise>
	   </c:choose>
       <span class="description"></span>
   </div>   
   <div class="group">
        <div class="fieldWithErrors">
             <form:label path="dob" cssClass="label" cssErrorClass="label labelError">Tanggal Lahir</form:label>
              <form:errors path="dob" cssClass="error" />
        </div>
        <c:choose>
			<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="dob" /><input type="text" class="text_field read" value="${user.dob }" readonly="readonly" size="50"/></c:when>
			<c:otherwise><form:input path="dob" cssClass="text_field datepicker"  /></c:otherwise>
	   </c:choose>
       <span class="description"></span>
   </div>
   <div class="group">
        <div class="fieldWithErrors">
             <form:label path="email" cssClass="label" cssErrorClass="label labelError">Email</form:label>
              <form:errors path="email" cssClass="error" />
        </div>
        <c:choose>
			<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="email" /><input type="text" class="text_field read" value="${user.email }" readonly="readonly" size="50"/></c:when>
			<c:otherwise><form:input path="email"  id="target" cssClass="text_field" size="50" /></c:otherwise>
	   </c:choose>
       <span class="description"></span>
   </div>
   <div class="group">
        <div class="fieldWithErrors">
             <form:label path="nik" cssClass="label" cssErrorClass="label labelError">NIK</form:label>
              <form:errors path="nik" cssClass="error" />
        </div>
        <c:choose>
			<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="nik" /><input type="text" class="text_field read" value="${user.nik }" readonly="readonly" size="50"/></c:when>
			<c:otherwise><form:input path="nik"  id="target" cssClass="text_field" size="50" /></c:otherwise>
	   </c:choose>
       <span class="description"></span>
   </div>          
   <div class="group">
		<div class="fieldWithErrors">
			<form:label path="cabang_id" cssClass="label" cssErrorClass="label labelError">Cabang</form:label>
			<form:errors path="cabang_id" cssClass="error" />
		</div>
		<c:choose>
			<c:when test="${user.mode eq 'VIEW'}">
				<form:hidden path="cabang_id" /><input type="text" class="text_field read" value="${user.namaCabang }" readonly="readonly" size="5"/>
			</c:when>
			<c:otherwise>
				<form:select  path="cabang_id" cssErrorClass="inputError">
					<form:option value="">Silahkan Pilih Cabang</form:option>
					<form:options items="${reff.listCabang}" itemValue="key" itemLabel="value"/>
				</form:select>
			</c:otherwise>
		</c:choose>
	</div>
	
	
                
		<br/>
                
               
                
	<div class="group navform wat-cf">
	  	<c:choose>
			<c:when test="${user.mode eq 'VIEW'}"><form:hidden path="id" />
				<button class="button" type="button" onclick="window.location='${path}/admin/user/${groupuserid }'">
                    <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
                  </button>
			</c:when>
			<c:otherwise>
				<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
                  </button>
                  <form:hidden path="mode"/>
                     <input type="hidden" value="${groupuserid}" name="groupid">
                  <span class="text_button_padding"></span>
                  <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/admin/user/${groupuserid }'">
                    <img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
                  </button>
			</c:otherwise>
	   </c:choose>
               
    </div>
		
	</form:form>		
                
               
          
	
	
</body>
</html>		