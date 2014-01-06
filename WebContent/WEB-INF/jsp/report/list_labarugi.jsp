<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />

<head>
	<script type="text/javascript">
		$(document).ready(function(){			
			 $('.periodepicker').datepicker( {
		        changeMonth: true,
		        changeYear: true,
		        showButtonPanel: true,
		        dateFormat: 'MM yy',
		        onClose: function(dateText, inst) { 
		            var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
		            var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
		            $(this).datepicker('setDate', new Date(year, month, 1));
		        }
		      });
		 });
	</script>
	
	<style type="text/css">
		.ui-datepicker-calendar {
		    display: none;
	    }
	</style>
	
</head>
<body>
	<div id="main" class="fullwidth">
		<div class="block" id="block-forms">
			<h3 class="tit"> List Laba/Rugi </h3>
			
			<div class="content">
				<div class="inner">
					<form method="post" target="_blank">
						<div class="columns wat-cf">
							<table class="nostyle" style="width: 500px;">
<!-- 								<tr> -->
<!-- 									<th><label>Periode</label></th> -->
<!-- 									<td> -->
<!-- 										<input type="text" name="beg_date" class="text_field datepicker" value=${tgl_awal}> s/d  -->
<!-- 										<input type="text" name="end_date" class="text_field datepicker" value=${tgl_akhir}> -->
<!-- 									</td> -->
<!-- 								</tr> -->
								<tr>
									<th><label>Periode </label></th>
									<td>
										<input type="text" name="beg_date" class="text_field periodepicker" value="${tgl_awal}"> 
									</td>
								</tr>
								<tr>
									<th><label>Format Laporan</label></th>
									<td>
										<select name="format">
											<optgroup label="Office">
												<option value="xls">Excel (.xls)</option>
												<option value="xlsx">Excel 2007-2010 (.xlsx)</option>
												<!-- <option value="jxl">jExcel (.xls)</option> -->
												<option value="pptx">Powerpoint 2007-2010 (.pptx)</option>
												<option value="docx">Word 2007-2010 (.docx)</option>
											</optgroup>
											<optgroup label="Others">
												<option value="pdf">Adobe Reader (.pdf)</option>
												<option value="html">HTML (.html)</option>
												<option value="odt">OpenOffice Writer (.odt)</option>
												<option value="jxl">OpenOffice Calc (.ods)</option>
												<option value="rtf">Rich Text Format (.rtf)</option>
												<option value="txt">Text (.txt)</option>
											</optgroup>
										</select>
									</td>
								</tr>
								<tr>
									<th></th>
									<td>
										<button class="button" type="submit" name="show" value="1">
											<img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Show
										</button>
										<span class="text_button_padding"></span>
										<button class="button" type="reset">
											<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Reset
										</button>
									</td>
								</tr>
							</table>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
