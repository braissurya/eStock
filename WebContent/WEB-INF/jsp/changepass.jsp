<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>

</head>
<body>
<h1>Change Password</h1>           
          
<form:form commandName="user" name="formpost" method="POST" action="${path}/changepass" cssClass="form">
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${payroll.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
	<div class="columns wat-cf">
		
		<table class="nostyle">
			<tr>
				<td>
					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="password" cssClass="label" cssErrorClass="label labelError">Old Password</form:label>
							<form:errors path="password" cssClass="error" />
						</div>
						<form:password path="password"/>
						<span class="description"></span>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="newPassword" cssClass="label" cssErrorClass="label labelError">New Password</form:label>
							<form:errors path="newPassword" cssClass="error" />
						</div>
						<form:password path="newPassword"/>
						<span class="description"></span>
					</div>
				</td>
			</tr>
			<tr>
				<td>
					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="confirmPassword" cssClass="label" cssErrorClass="label labelError">Confirm Password</form:label>
							<form:errors path="confirmPassword" cssClass="error" />
						</div>
						<form:password path="confirmPassword"/>
						<span class="description"></span>
					</div>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="group navform wat-cf">

		<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
               <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Change" /> Change
       	</button>
		 <form:hidden path="mode"/>
		

		</div>
	
</form:form>
			
		
</body>
</html>