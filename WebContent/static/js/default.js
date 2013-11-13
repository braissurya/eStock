//function untuk paging
function gotoPage(page,sort,type,idform){
	document.getElementById("page").value=page;
	document.getElementById("sort").value=sort;
	document.getElementById("st").value=type;
	document.getElementById(idform).submit();
}

/* Popup Window, persis ditengah layar */
function popWin(href, height, width,scrollbar,stat) {
	var vWin;
	if(scrollbar!='no')scrollbar='yes';
	if(stat!='yes')stat='no';

	vWin = window.open(href,'','height='+height+',width='+width+
		',toolbar=no,directories=no,menubar=no,scrollbars='+scrollbar+',resizable=yes,modal=yes,status=yes'+
		//',toolbar=no,directories=no,status=no,menubar=no,scrollbars=yes,resizable=no,modal=yes,status=yes'+
		',left='+((screen.availWidth-width)/2)+
		',top='+((screen.availHeight-height)/2));
	vWin.opener = self;
}

//autopopulate select
function autoPopulateSelect(path, ajaxType, parentSelectId, childSelectId, optionALL, selectedId, textAll){
	//ajax autopopulate select box
	$(parentSelectId).change(function(){
		$(childSelectId).children().remove(); //remove all isi dari child

		//populate ulang isi dari child
		$.getJSON(path + '/json/' + ajaxType + '/' + $(parentSelectId).val(), {t:ajaxType, p:$(parentSelectId).val()}, function(data) {
			var html = '';
			if(textAll==null)textAll="ALL";
			if(optionALL == true) html = '<option value="">'+textAll+'</option>';
		    var len = data.length;
		    for (var i = 0; i< len; i++) {
		    	if(selectedId!=null){
		    		var select="";
		    		if(selectedId==data[i].key){
		    			select="selected=\"selected\"";
		    		}
		    		html += '<option value="' + data[i].key + '" '+select+' >' + data[i].value + '</option>';
		    	}else{
		        	html += '<option value="' + data[i].key + '">' + data[i].value + '</option>';
		        }
		    }
		    $(childSelectId).append(html);
		});
	});
}

function formatCurrency( num ){
    num = num.toString().replace(/\,/g, '');
    if( isNaN(num) )
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    cents = num % 100;
    num = Math.floor(num / 100).toString();
    if( cents < 10 )
        cents = "0" + cents;
    for( var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++ )
        num = num.substring(0, num.length - (4 * i + 3)) + ',' +
              num.substring(num.length - (4 * i + 3));
    return (((sign) ? '' : '-') + num + '.' + cents);
}

function formatNumber( num ){
    num = num.toString().replace(/\,/g, '');
    if( isNaN(num) )
        num = "0";
    sign = (num == (num = Math.abs(num)));
    num = Math.floor(num * 100 + 0.50000000001);
    cents = num % 100;
    num = Math.floor(num / 100).toString();
    if( cents < 10 )
        cents = "0" + cents;
    for( var i = 0; i < Math.floor((num.length - (1 + i)) / 3); i++ )
        num = num.substring(0, num.length - (4 * i + 3)) + ',' +
              num.substring(num.length - (4 * i + 3));
    return (((sign) ? '' : '-') + num );
}

function groupList(id, form, page, keterangan, sort, sort_type){
	if(sort==id){
		if(sort_type== 'asc'){
			document.write("<a href=\"javascript:gotoPage('"+page+"','"+id+"','desc','"+form+"')\" class=\"up\" >"+keterangan+"</a>");
		}else if(sort_type== 'desc'){
			document.write("<a href=\"javascript:gotoPage('"+page+"','"+id+"','asc','"+form+"')\" class=\"down\" >"+keterangan+"</a>");
		}else{
			document.write("<a href=\"javascript:gotoPage('"+page+"','"+id+"','asc','"+form+"')\" >"+keterangan+"</a>");
		}
	}else{
		document.write("<a href=\"javascript:gotoPage('"+page+"','"+id+"','asc','"+form+"')\" >"+keterangan+"</a>");
	}
}

function doAction(linknya, title,width,height,reload){
	/*$(".element_title").html(title);
	$("#element_to_pop_up").css("width",width+ "px");
	$("#element_to_pop_up").css("height",height+ "px");
	if(reload==false){
		$("#element_to_pop_up").bPopup({
			modalClose: false,
			opacity: 0.7,
			positionStyle: 'fixed', //'fixed' or 'absolute'
			content:'iframe', //'iframe' or 'ajax'
            contentContainer:'#frame',
            loadUrl:linknya,
           	zIndex:-1,
            scrollBar:false
		});
	}else{
		$("#element_to_pop_up").bPopup({
			modalClose: false,
			opacity: 0.7,
			positionStyle: 'fixed', //'fixed' or 'absolute'
			content:'iframe', //'iframe' or 'ajax'
            contentContainer:'#frame',
            loadUrl:linknya,
           	zIndex:-1,
            scrollBar:false,
           onClose: function() { location.reload(true);}
		});
	}*/
	if(reload==false){
		$.window({  title: title,   // title
				url:linknya,	//link url
				width: width,           // window width
				height: height, // window height
				showModal:true,
				showRoundCorner:true,
				minimizable:false,
				maximizable:false,
				bookmarkable:false,
				resizable:false
			});
	}else{
		$.window({  title: title,   // title
			url:linknya,	//link url
			width: width,           // window width
			height: height, // window height
			onClose: function(wnd) { // a callback function while user click close button
				location.reload(true);
			   },
			showModal:true,
			showRoundCorner:true,
			minimizable:false,
			maximizable:false,
			bookmarkable:false,
			resizable:false
		});
	}

}

function closeAction(){
//	$("#element_to_pop_up").bPopup().close(); 
	$.window.closeAll();
	return false;
}

function lewat(){
}

//show/hide bagian input polis
function togel(checkbox, section){
	if( checkbox.is(':checked') ) $(section).show(300);
	else $(section).hide(300);
}

function timedRefresh(timeoutPeriod) {setTimeout("location.reload(true);",timeoutPeriod);}