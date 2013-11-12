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
	<link rel="stylesheet" type="text/css" href="${path }/static/css/defaultTheme.css"  media="screen" />
	<link rel="stylesheet" type="text/css" href="${path }/static/css/myTheme.css"  media="screen" />
	<link rel="stylesheet" type="text/css" href="${path }/static/js/jquery/themes/base/jquery-ui-timepicker-addon.css"  media="screen" />
	
  	
	<link rel="stylesheet" href="${path }/static/decorator/main/pilu/css/base.css" type="text/css" media="screen" />
   	<link rel="stylesheet" id="current-theme" href="${path }/static/decorator/main/pilu/css/themes/default/style.css" type="text/css" media="screen" />
	
	<!--[if lte IE 6]><link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/main-ie6.css" /><![endif]--> <!-- MSIE6 -->
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/style.css" /> <!-- GRAPHIC THEME -->
	<link rel="stylesheet" media="screen,projection" type="text/css" href="${path}/static/decorator/main/adminiziolite/css/mystyle.css" /> <!-- WRITE YOUR CSS CODE HERE -->
	
	
	<script type="text/javascript" src="${path}/static/js/default.js"></script>
	
	
	<link rel="stylesheet" href="${path}/static/js/jquery/themes/base/jquery.ui.all.css">

	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.js"></script>
	<script type="text/javascript" src="${path }/static/js/jquery/jquery-ui-1.10.0.custom.min.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.hotkeys.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.scrollTo.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.formatCurrency-1.4.0.min.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/picnet.table.filter.min.js"></script>
 	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.bpopup.min.js"></script>
	<script type="text/javascript" charset="utf-8" src="${path }/static/js/jquery/jquery.fixedheadertable.js"></script>
 
	
	<script src="${path }/static/js/jquery/ui/jquery.ui.sliderAccess.js"></script>
	<script src="${path }/static/js/jquery/ui/jquery.ui.timepicker.js"></script>
	
	
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
	function scrollTable(){
		$("table.tbl_repeat").fixedHeaderTable('destroy');
		 $("table.tbl_repeat").fixedHeaderTable({ footer: true });		 
		 $("#scrollbody").animate({ scrollTop: $(document).height() }, "slow");
	}
	
	$(function() {
		$(".datepicker").datepicker({
			changeMonth: true,
			changeYear: true,
			dateFormat: "dd-mm-yy",
			yearRange: "-100:+0"
		});
		
		$(".datetimepicker").datetimepicker({
			showSecond: true,
			timeFormat: 'HH:mm:ss',
			dateFormat: "dd-mm-yy",
			yearRange: "-100:+0"
			
		});
		
	
	
		
		$("table.tbl_repeat input[type=text]").click(function(){
			$(this).select();
		});
		
		$(".read").each(function(){
		    this.tabIndex = -1;
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
		    
			$(".nominal").blur();
			$(".number").blur();	
			
			
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

	<!-- Menu -->
	<div id="menu" class="box">
		
		<!-- Switcher -->
		
		<div class="topmenu float_right">
			<strong><a href="${path}/changepass">Change Password</a></strong> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <strong><a href="${path }/logout" id="logout">Log Out</a></strong>
		</div>
		<!-- <ul class="box f-right">
			<li><a href="#"><span><strong>Visit Site &raquo;</strong></span></a></li>
		</ul> -->
	
			<span class="f-left topmenu" id="switcher" style="margin-right: 10px;">
				<a href="#" rel="1col" class="styleswitch ico-col1"   title="Display one column" ><img src="${path}/static/decorator/main/adminiziolite/design/switcher-1col.gif" alt="1 Column"  /></a>
				<a href="#" rel="2col" class="styleswitch ico-col2"  title="Display two columns"><img src="${path}/static/decorator/main/adminiziolite/design/switcher-2col.gif" alt="2 Columns" /></a>
			</span>
		${sessionScope.currentUser.menuUser}
		
	</div> <!-- /header -->

	<hr class="noscreen" />
	

	<!-- Columns -->
	<div id="cols" class="box">

		 <!-- Aside (Left Column) -->
		<div id="aside" class="box">

			<div class="padding box">
				<!-- Logo (Max. width = 200px) -->
				<p id="logo"><a href="#"><img src="${path}/static/decorator/main/adminiziolite/tmp/logo.gif" alt="Our logo" title="Visit Site" /></a></p>
				<form action="#" method="get" id="search">
				<fieldset>
					<legend>User Information</legend>
					<table>
						<tr>
							<th style="text-align: left;">Username</th>
							<th>:</th>
							<td>${sessionScope.currentUser.username }</td>
						</tr>
						<tr>
							<th  style="text-align: left;">Nama</th>
							<th>:</th>
							<td>${sessionScope.currentUser.nama }</td>
						</tr>
						<tr>
							<th  style="text-align: left;">Cabang</th>
							<th>:</th>
							<td>${sessionScope.currentUser.namaCabang }</td>
						</tr>
					</table>
				</fieldset>
				</form>
			</div> <!-- /padding -->

			
		</div> <!-- /aside --> 
		
		<hr class="noscreen" />
		
		
		
		<!-- Content (Right Column) -->
		<div id="content" class="box" style="min-height: 450px">
		
			<decorator:body></decorator:body>
		</div> <!-- /content -->
	</div> <!-- /cols -->

	<hr class="noscreen" />

	<!-- Footer -->
	<div id="footer" class="box">

		<p class="f-left">${company.copyright }</p>

		<p class="f-right" > <a href="#">${company.name}</a></p>

	</div> <!-- /footer -->

</div> <!-- /main -->

</body>
</html>