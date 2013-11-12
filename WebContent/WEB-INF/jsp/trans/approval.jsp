<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	<script type="text/javascript">
		$(document).ready(function(){
			var path="${path}";
			//pesan error diletakkan di alert juga
			var message = "${message}".replace(/<br\s*[\/]?>/gi, "\n");
			if(message != ''){
			 	alert(message);
			 	window.top.location.href = '${path}/transaksi/Penjualan/Input/${trans_idnya}/edit';
			}
			
			

		});

		
	</script>
</head>
<body>
	<h1>Approval Page</h1>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>   			
    			<p class="msg info"><fmt:message key="CompleteForm" /> </p>
   		</c:otherwise>
   	</c:choose>
   	<form:form commandName="user" name="formpost" method="POST" cssClass="form" >
   		<div class="columns wat-cf">
   			<fieldset>
   				<legend>Data Customer</legend>
   				<table class="nostyle">
   					<tr>
   						<th>
   							Kode Customer
   						</th>
   						<th>:
   						</th>
   						<td>
   							${user.trans.customer.kode }
   						</td>
   					</tr>
   					<tr>
   						<th>
   							Nama Customer
   						</th>
   						<th>:
   						</th>
   						<td>
   							${user.trans.customer.nama }
   						</td>
   					</tr>
   					<tr>
   						<th>
   							Limit Hutang
   						</th>
   						<th>:
   						</th>
   						<td>
   							Rp <fmt:formatNumber pattern="#,##0">${user.trans.customer.limit_hutang }</fmt:formatNumber>
   						</td>
   					</tr>
   					<tr>
   						<th>
   							Total Hutang
   						</th>
   						<th>:
   						</th>
   						<td>
   							Rp <fmt:formatNumber pattern="#,##0">${user.totalHutang	}</fmt:formatNumber>
   						</td>
   					</tr>
   				</table>
   			</fieldset>
   			<div class="group">
	            <div class="fieldWithErrors">
		            <form:label path="username"  cssClass="label" cssErrorClass="label labelError">Username Approval</form:label>
		            <form:errors path="username" cssClass="error" />
	            </div><form:hidden path="id" />
	        	<form:input path="username" cssClass="text_field target" cssErrorClass="text_field error target"/>
            	<span class="description"></span>
            </div>
            
            <div class="group">
	            <div class="fieldWithErrors">
		            <form:label path="password"  cssClass="label" cssErrorClass="label labelError">Password Approval</form:label>
		            <form:errors path="password" cssClass="error" />
	            </div><form:hidden path="id" />
	        	<form:password path="password" cssClass="text_field" cssErrorClass="text_field error"/>
            	<span class="description"></span>
            </div>
            
			
			<br/><br/>
        	<div class="group navform wat-cf">
        		
					<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Approved" /> Approved
	            	</button>
	                <form:hidden path="trans.no_trans"/>
	                <span class="text_button_padding"></span>
	                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/driver'">
	                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
	                </button>
			                
			                 
        	</div>
   		</div>
   	</form:form>
</body>