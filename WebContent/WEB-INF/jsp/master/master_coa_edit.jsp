<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	
</head>
<body>
	<h3 class="tit"> Master Chart Of Account &gt; ${coa.mode } </h3>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${coa.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
			    
		   </c:if>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="coa" name="formpost" method="POST" action="${path}/master/coa/save" cssClass="form" >
   		<div class="columns wat-cf">
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="parent" cssClass="label" cssErrorClass="label labelError">Parent ID</form:label>
	                <form:errors path="parent" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${coa.mode eq 'VIEW'}"><form:hidden path="parent" /><input type="text" class="text_field read" value="${coa.parent }" readonly="readonly" /></c:when>
					<c:otherwise>
						<form:select path="parent"  cssErrorClass="inputError" >
							<c:forEach items="${reff.parentCOA}" var="p">
								<form:option value="${p.key }">
									${p.key }
									&nbsp
									<c:if test="${p.desc ne \"1\"}">
										<c:forEach begin="0" end="${p.desc}">&nbsp;&nbsp;&nbsp;</c:forEach>
									</c:if>
									${p.value }
								</form:option>
							</c:forEach>
						</form:select>
					</c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
   			<div class="group">
	            <div class="fieldWithErrors">
		            <form:label path="id"  cssClass="label" cssErrorClass="label labelError">ID COA</form:label>
		            <form:errors path="id" cssClass="error" />
	            </div>
				<c:choose>
					<c:when test="${coa.mode eq 'VIEW' or coa.mode eq 'EDIT'}"><form:hidden path="id" /><input type="text" class="text_field read" value="${coa.id }" readonly="readonly" size="10" /></c:when>
					<c:otherwise><form:input path="id"  id="target" cssClass="text_field target" cssErrorClass="text_field inputError target" size="10" maxlength="10"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
            </div>
            <div class="group">
            	<div class="fieldWithErrors">
	            	<form:label path="nama" cssClass="label" cssErrorClass="label labelError">Nama</form:label>
	                <form:errors path="nama" cssClass="error" />
	        	</div>
				<c:choose>
					<c:when test="${coa.mode eq 'VIEW'}"><form:hidden path="nama" /><input type="text" class="text_field read" value="${coa.nama }" readonly="readonly" size="100"/></c:when>
					<c:otherwise><form:input path="nama"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="100" maxlength="80"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
        	<div class="group">
        		<div class="fieldWithErrors">
	            	<form:label path="level" cssClass="label" cssErrorClass="label labelError">Level</form:label>
	                <form:errors path="level" cssClass="error" />
	        	</div>
	        	<c:choose>
					<c:when test="${coa.mode eq 'VIEW'}"><form:hidden path="level" /><input type="text" class="text_field read" value="${coa.level }" readonly="readonly" size="2"/></c:when>
					<c:otherwise><form:input path="level"  id="target" cssClass="text_field" cssErrorClass="text_field inputError" size="2" maxlength="2"/></c:otherwise>
		  		</c:choose>
                  <span class="description"></span>
        	</div>
            
   			<table  class="nostyle">
   				<tr>
   					<td style="width: 100px">   
			            <div class="group">
			            	<div class="fieldWithErrors">
				            	<form:label path="post" cssClass="label" cssErrorClass="label labelError">Posting</form:label>
				                <form:errors path="post" cssClass="error" />
				        	</div>
			                <c:choose>
								<c:when test="${coa.mode eq 'VIEW'}">
									<c:choose>
										<c:when test="${coa.post eq \"1\" }">
											<c:set  var="posting" value="Ya" />
										</c:when>
										<c:otherwise>
											<c:set  var="posting" value="Tidak" />
										</c:otherwise>
									</c:choose>
									<input type="text" class="text_field read" value="${posting}" readonly="readonly" size="5" />
								</c:when>
								<c:otherwise>	
									<form:radiobutton path="post" value="1" checked="checked" label="Ya"/>
									<form:radiobutton path="post" value="0" label="Tidak"/>
								</c:otherwise>
							</c:choose>
			               	<span class="description"></span>
			            </div>
			   		</td>   
			   		<td style="width: 120px">   
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="dk" cssClass="label" cssErrorClass="label labelError">Posisi</form:label>
				                <form:errors path="dk" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${coa.mode eq 'VIEW'}"><form:hidden path="dk" />
									<c:choose>
										<c:when test="${coa.dk eq \"D\" }">
											<c:set  var="debetkredit" value="Debet" />
										</c:when>
										<c:otherwise>
											<c:set  var="debetkredit" value="Kredit" />
										</c:otherwise>
									</c:choose>
									<input type="text" class="text_field read" value="${debetkredit}" readonly="readonly" size="5" />
								</c:when>
								<c:otherwise>					
									<form:radiobutton path="dk" value="D" checked="checked" label="Debet"/>
									<form:radiobutton path="dk" value="K" label="Kredit"/>
								</c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div>
			   		</td>  
			   		<td style="width: 100px">   
			        	<div class="group">
			        		<div class="fieldWithErrors">
				            	<form:label path="pl" cssClass="label" cssErrorClass="label labelError">Profit & Loss</form:label>
				                <form:errors path="pl" cssClass="error" />
				        	</div>
				        	<c:choose>
								<c:when test="${coa.mode eq 'VIEW'}">
									<c:choose>
										<c:when test="${coa.pl eq \"1\" }">
											<c:set  var="profitloss" value="Ya" />
										</c:when>
										<c:otherwise>
											<c:set  var="profitloss" value="Tidak" />
										</c:otherwise>
									</c:choose>
									<input type="text" class="text_field read" value="${profitloss}" readonly="readonly" size="5" />
								</c:when>
								<c:otherwise>					
									<form:radiobutton path="pl" value="1" checked="checked" label="Ya"/>
									<form:radiobutton path="pl" value="0" label="Tidak"/>
								</c:otherwise>
					  		</c:choose>
			                  <span class="description"></span>
			        	</div> 
			   		</td>  
				</tr>	
			</table>   
        	
        	<div class="group navform wat-cf">
        		<c:choose>
					<c:when test="${coa.mode eq 'VIEW'}"><form:hidden path="id" />
						<button class="button" type="button" onclick="window.location='${path}/master/coa'">
			         	   <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
			            </button>
					</c:when>
						<c:otherwise>
							<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			            	</button>
			                <form:hidden path="mode"/>
			                <span class="text_button_padding"></span>
			                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/master/coa'">
			                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
			                </button>
						</c:otherwise>
				</c:choose>
        	</div>
   		</div>
   	</form:form>
</body>