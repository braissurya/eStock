<%@ include file="/taglibs.jsp"%><c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	</head>
	<body>
		<div id="wrapper">
			<form:form commandName="listData" name="form_products" id="form_products" method="POST" action="${path}/admin/test/save" cssClass="form" >
				<input type="text" id="name" name="name"
					class="field field_medium"
					placeholder="Product name" value="" />
				<input type="text" id="price" name="price"
					class="field field_small"
					placeholder="Price" value="" />
				<input type="text" id="qty" name="qty"
					class="field field_small"
					placeholder="Qty" value="" />
				<a href="#" class="button add_new"> Add new</a>
				<div class="group navform wat-cf">
					<button class="button" type="submit" onclick="if(!confirm('Are you sure want to save?'))return false;">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
			        </button>
			    </div>
			
			
			<div id="table_wrapper" style="overflow: auto;max-height: 300px;" >
					<p>There are currently no records available.</p>
			</div>
			</form:form>
		</div>
	</body>
</html>