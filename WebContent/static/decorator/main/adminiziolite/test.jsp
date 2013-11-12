<%@ include file="/taglibs.jsp"%><c:set var="path" value="${pageContext.request.contextPath}" />
<!doctype html>
<html lang="en">
<head>
	<meta charset="utf-8">
	<title>jQuery UI Button - Checkboxes</title>
	<link rel="stylesheet" href="${path}/static/js/jquery/themes/base/jquery.ui.all.css">
	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.js"></script>
	
 
	<script src="${path }/static/js/jquery/ui/jquery.ui.core.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.widget.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.button.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.position.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.menu.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.autocomplete.js"></script>
	
<script>
	$(function() {
		var availableTags = [
			"ActionScript",
			"AppleScript",
			"Asp",
			"BASIC",
			"C",
			"C++",
			"Clojure",
			"COBOL",
			"ColdFusion",
			"Erlang",
			"Fortran",
			"Groovy",
			"Haskell",
			"Java",
			"JavaScript",
			"Lisp",
			"Perl",
			"PHP",
			"Python",
			"Ruby",
			"Scala",
			"Scheme"
		];
		$( "#tags" ).autocomplete({
			source: availableTags
		});
	});
	</script>
</head>
<body>

<div class="ui-widget">
	<label for="tags">Tags: </label>
	<input id="tags">
</div>
</body>
</html>
