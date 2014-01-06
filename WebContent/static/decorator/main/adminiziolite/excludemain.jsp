<%@ include file="/taglibs.jsp"%><c:set var="path" value="${pageContext.request.contextPath}" />
<!DOCTYPE html >
<html lang="en-US">
<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta http-equiv="content-language" content="en" />
	<meta name="robots" content="noindex,nofollow" />
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/reset.css" /> <!-- RESET -->
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/main.css" /> <!-- MAIN STYLE SHEET -->
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/2col.css" title="2col" /> <!-- DEFAULT: 2 COLUMNS -->
	<link rel="alternate stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/1col.css" title="1col" /> <!-- ALTERNATE: 1 COLUMN -->
	<link rel="stylesheet" type="text/css" href="${path }/static/css/ddlevelsmenu-base.css"  media="screen" />
  	<link rel="stylesheet" type="text/css" href="${path }/static/css/ddlevelsmenu-topbar.css"  media="screen" />
	<link rel="stylesheet" type="text/css" href="${path }/static/css/style.css"  media="screen" />
  	
	<link rel="stylesheet" href="${path }/static/decorator/main/pilu/css/base.css" type="text/css" media="screen" />
   	<link rel="stylesheet" id="current-theme" href="${path }/static/decorator/main/pilu/css/themes/default/style.css" type="text/css" media="screen" />
	
	<!--[if lte IE 6]><link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/main-ie6.css" /><![endif]--> <!-- MSIE6 -->
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/style.css" /> <!-- GRAPHIC THEME -->
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/mystyle.css" /> <!-- WRITE YOUR CSS CODE HERE -->
	
	
	<script type="text/javascript" src="${path}/static/js/default.js"></script>
	
	
	<link rel="stylesheet" href="${path}/static/js/jquery/themes/base/jquery.ui.all.css">

	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.hotkeys.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.scrollTo.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.formatCurrency-1.4.0.min.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.bpopup-0.7.0.min.js"></script>
 
	<script src="${path }/static/js/jquery/ui/jquery.ui.core.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.widget.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.button.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.position.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.menu.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.autocomplete.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.tabs.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.datepicker.js"></script>
	
	
	<style>
	.ui-autocomplete-loading {
		background: white url('${path}/static/js/jquery/themes/base/images//ui-anim_basic_16x16.gif') right center no-repeat;
	}
	#city { width: 25em; }
	</style>


	 <script type="text/javascript" src="${path}/static/decorator/main/adminiziolite/js/switcher.js"></script>
	<script type="text/javascript" src="${path}/static/decorator/main/adminiziolite/js/toggle.js"></script> 
	<script type="text/javascript" src="${path }/static/js/ddlevelsmenu.js">
		/***********************************************
		* All Levels Navigational Menu- (c) Dynamic Drive DHTML code library (http://www.dynamicdrive.com)
		* This notice MUST stay intact for legal use
		* Visit Dynamic Drive at http://www.dynamicdrive.com/ for full source code
		***********************************************/	
   </script>
	
	<script>
	$(function() {
		$(".datepicker").datepicker({
			changeMonth: true,
			changeYear: true,
			dateFormat: "dd-mm-yy"
		});
		
		//untuk semua input yg mempunyai class "nominal" akan diformat 3 digit secara otomatis
		$(".nominal").blur(function(){
			$(this).formatCurrency({ symbol: '', colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: 0 });

		})
		.keyup(function(e) {
				var e = window.event || e;
				var keyUnicode = e.charCode || e.keyCode;
				if (e !== undefined) {
					switch (keyUnicode) {
						case 16: break; // Shift
						case 27: this.value = ''; break; // Esc: clear entry
						case 35: break; // End
						case 36: break; // Home
						case 37: break; // cursor left
						case 38: break; // cursor up
						case 39: break; // cursor right
						case 40: break; // cursor down
						case 78: break; // N (Opera 9.63+ maps the "." from the number key section to the "N" key too!) (See: http://unixpapa.com/js/key.html search for ". Del")
						case 110: break; // . number block (Opera 9.63+ maps the "." from the number block to the "N" key (78) !!!)
						case 190: break; // .
						default: $(this).formatCurrency({ symbol: '',colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: -1, eventOnDecimalsEntered: true });
					}
				}
			})
		;
		
		 $(".number").blur(function(){
			formatCurrency($(this).val());

		})
		.keyup(function(e) {
				var e = window.event || e;
				var keyUnicode = e.charCode || e.keyCode;
				if (e !== undefined) {
					switch (keyUnicode) {
						case 16: break; // Shift
						case 27: this.value = ''; break; // Esc: clear entry
						case 35: break; // End
						case 36: break; // Home
						case 37: break; // cursor left
						case 38: break; // cursor up
						case 39: break; // cursor right
						case 40: break; // cursor down
						case 78: break; // N (Opera 9.63+ maps the "." from the number key section to the "N" key too!) (See: http://unixpapa.com/js/key.html search for ". Del")
						case 110: break; // . number block (Opera 9.63+ maps the "." from the number block to the "N" key (78) !!!)
						case 190: break; // .
						default: formatCurrency($(this).val());
					}
				}
			})
		;  
		
		$( ".check" ).button();
		
	});
	</script>
	
	  <script type="text/javascript">
	// Jalankan semua script jquery, setelah seluruh document selesai loading
		
		$().ready(function() {
			// pesan error/sukses
			
		});
		
		$(document).ready(function(){
		  $(".target").focus();
		
		  var pesan = '${pesan}'.replace(/<br\s*[\/]?>/gi, "\n");
			if(pesan!='')alert(pesan);
		    
		  
		});
		
			 
	</script>
  
	<script type="text/javascript">
	$(document).ready(function(){
		$(".tabs > ul").tabs();	
		
		
	});
	</script>
	 <title>${company.name} - <decorator:title  default="Selamat Datang"/></title>
	 <decorator:head></decorator:head>
</head>

<body>
<!-- Element to pop up -->
	<div id="element_to_pop_up" >
	   	<div class="element_title"></div>
	   	 <a href="#" class="bClose">x</a>
	    <div id="frame">
	    	
	    </div>
	</div>

<div id="main">

	

	<hr class="noscreen" />

	

	<hr class="noscreen" />

	<!-- Columns -->
	<div id="cols" class="box">

		

		<hr class="noscreen" />

		<!-- Content (Right Column) -->
		<div id="content" class="box" style="min-height: 450px">
			
			<decorator:body></decorator:body>

		</div> <!-- /content -->
	</div> <!-- /cols -->

	<hr class="noscreen" />

	

</div> <!-- /main -->

</body>
</html>