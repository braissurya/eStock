<%@ include file="/taglibs.jsp"%><c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    

  </head>
  
  <body>
 	<h4>Site Map</h4>
		${sessionScope.currentUser.siteMap }

  </body>
</html>
