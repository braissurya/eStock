<%@ include file="/taglibs.jsp"%><c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>POS System - ${company.name} </title>

<script type="text/javascript">
$(document).ready(function(){
$(document).pngFix( );
});
</script>
<link rel="stylesheet" href="${path}/static/decorator/login/internet_dreams/css/screen.css" type="text/css" media="screen" title="default" />
<!--  jquery core -->
<script src="${path}/static/decorator/login/internet_dreams/js/jquery/jquery-1.4.1.min.js" type="text/javascript"></script>

<!-- Custom jquery scripts -->
<script src="${path}/static/decorator/login/internet_dreams/js/jquery/custom_jquery.js" type="text/javascript"></script>

<!-- MUST BE THE LAST SCRIPT IN <HEAD></HEAD></HEAD> png fix -->
<script src="${path}/static/decorator/login/internet_dreams/js/jquery/jquery.pngFix.pack.js" type="text/javascript"></script>

</head>
<body id="login-bg"> 

 
<!-- Start: login-holder -->
<div id="login-holder">

	<!-- start logo -->
	<div id="logo-login">
		
	</div>
	<!-- end logo -->
	
	<div class="clear"></div>
	<a href="index.html"><img src="${path}/static/decorator/login/internet_dreams/images/shared/logo.png"  /></a>
	<!--  start loginbox ................................................................................. -->
	<div id="loginbox">
	
	<!--  start login-inner -->
	<div id="login-inner">
		<h2>Login Page</h2>
		 <form:form commandName="user" method="POST" >
		<table border="0" cellpadding="0" cellspacing="0">
		<tr>
			<th><form:label path="username" cssClass="login-label" cssErrorClass="login-label error">Username</form:label></th>
			<td>
				 <form:input path="username" cssClass="login-inp" cssErrorClass="login-inp errorField"/>
               
               	<form:errors path="username" cssClass="error" />
               
			</td>
		</tr>
		<tr>
			<th><form:label path="password" cssClass="login-label" cssErrorClass="login-label error">Password </form:label></th>
			<td>
				 <form:password path="password" cssClass="login-inp" cssErrorClass="login-inp errorField"/>
              
                <form:errors path="password" cssClass="error" /></td>
		</tr>
		
		<tr>
			<th></th>
			<td><input type="submit" class="submit-login"  value=""/></td>
		</tr>
		
		</table>
		</form:form>
		
		
		
	</div>
 	<!--  end login-inner -->
	<div class="clear"></div>
	
	
 </div>
  <div style="font-size: 12px;color: black;text-align: center;position: relative;bottom: 20px;z-index: 9999;font-weight: bold;line-height: 12px;">
		<br/>
			${company.name}<br/>
			${company.copyright }
	</div>

 <!--  end loginbox -->
 
</div>
<!-- End: login-holder -->
</body>
</html>