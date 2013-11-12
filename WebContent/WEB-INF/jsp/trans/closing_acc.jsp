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
			}
			
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
	<h1>Closing &gt; Accounting</h1>
	<c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning' or messageType eq 'done' }">
   			<p class="msg ${messageType}">${message}</p>
   		</c:when>
   		<c:otherwise>
   			
    			<p class="msg info">Silahkan klik "Process Closing" untuk memproses Closing Accounting periode <fmt:formatDate pattern="MMMMM yyyy" value="${periode }"/> </p>
			    
   		</c:otherwise>
   	</c:choose>
   	<form method="post" id="formpost" name="formpost">
   		<div class="columns wat-cf">
   			<div class="group">
	            <div class="fieldWithErrors">
		            <label  class="label" >Periode</label>
	            </div>
	        	<input type="text" class="text_field read" value="<fmt:formatDate pattern="MMMMM yyyy" value="${periode }"/>" readonly="readonly" size="18" />
	        	
            </div>
           
			<br/><br/>
        	<div class="group navform wat-cf">        		
				<button class="button" name="process" type="submit" onclick="if(!confirm('Are you sure want to Closing?'))return false;">
                    <img src="${path }/static/images/icons/process_accept.png" alt="Process Closing" /> Process Closing
            	</button>
        	</div>
   		</div>
   	</form>
</body>