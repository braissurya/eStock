<%@ include file="/taglibs.jsp"%>
<c:set var="path" value="${pageContext.request.contextPath}" />
<html>
<head>
	    <%-- <script type="text/javascript" src="${path}/static/js/trans.js" >
	</script> --%>
	
	<script type="text/javascript">

		var path="${path}";
		function kostumer(kode){
			$.getJSON(path+'/json/completeCustomer?kode='+kode, function(data) {
				if(data!=""){
					$('#customer\\.nama').val(data[0].nama);
					$('#customer\\.contact').val(data[0].contact);
					$('#customer\\.alamat').val(data[0].alamat);
					$('#customer\\.kota').val(data[0].kota);
					$('#customer\\.no_telp').val(data[0].no_telp);
					$('#customer\\.no_hp').val(data[0].no_hp);
					$('input[name=customer_id]').val(data[0].id);
					$("input[name=flag_ecer][value=" + data[0].flag_ecer + "]").attr('checked', 'checked');
					$("input[name=pay_mode][value=" + data[0].pay_mode + "]").attr('checked', 'checked');
					$("input[name=flag_pajak][value=" + data[0].pkp + "]").attr('checked', 'checked');
					
					if($('input[name=flag_pajak]:checked').val()==1){
						$('#persen_ppn').val(formatCurrency(10));
						$('#ppn').val(formatCurrency(data[0].persen_ppn*(data[0].total_harga-data[0].total_disc)));
						alert(data[0].total_harga);
					}else {
						$('#persen_ppn').val(formatCurrency(0));
						$('#ppn').val(formatCurrency(0));
					}
					
				}else{
					$('#customer\\.nama').val("");
					$('#customer\\.contact').val("");
					$('#customer\\.alamat').val("");
					$('#customer\\.kota').val("");
					$('#customer\\.no_telp').val("");
					$('#customer\\.no_hp').val("");
					$('input[name=customer_id]').val("");
					$("input[name=flag_ecer][value=1]").attr('checked', 'checked');
					$("input[name=pay_mode][value=1]").attr('checked', 'checked');
					$("input[name=flag_pajak][value=0]").attr('checked', 'checked');
					$('#persen_ppn').val(formatCurrency(0));
					$('#ppn').val(formatCurrency(0));
				}
			});
			return false;
		}
		
		function item(kode){
			$.getJSON(path+'/json/autocompleteItemPrice?barcode_ext='+kode, function(data) {
				if(data!=""){
					$('#nama_produk').val(data[0].nama);

					if('${trans.jenistrans}'=='Penjualan'){
						if($('input[name=flag_ecer]:checked').val()==1){
							$('#harga').val(formatCurrency(data[0].harga_ecer));
							$('#diskon').val(formatCurrency(data[0].diskon_ecer));
							$('#diskon_persen').val(formatCurrency((data[0].diskon_ecer/data[0].harga_ecer)*100));
						}else {
							$('#harga').val(formatCurrency(data[0].harga));
							$('#diskon').val(formatCurrency(data[0].diskon));
							$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga)*100));
						}
					}
					$('#stock').val(data[0].stock_jual);
					$('#satuan').val(data[0].satuan_idNama);
					$(".kode").focus();
				}else{
					$('#nama_produk').val("");
					$('#harga').val("");
					$('#diskon_persen').val("");
					$('#diskon').val("");
					$('#stock').val("");
					$('#satuan').val("");
				}
			});
			return false;
		}
		
		function sales(nik){
			$.getJSON(path+'/json/completeKaryawan?nik='+nik, function(data) {
				if(data!=""){
					$('#karyawan\\.nama').val(data[0].nama);
					$('input[name=sales_id]').val(data[0].id);

				}else{
					$('#karyawan\\.nama').val("");
					$('input[name=sales_id]').val("");
				}
			});
			return false;
		}
		
		
		
		function supplier(kode){
			$.getJSON(path+'/json/completeSupplier?kode='+kode, function(data) {
				if(data!=""){
					$('#supplier\\.nama').val(data[0].nama);
					$('#supplier\\.contact').val(data[0].contact);
					$('#supplier\\.alamat').val(data[0].alamat);
					$('#supplier\\.kota').val(data[0].kota);
					$('#supplier\\.no_telp').val(data[0].no_telp);
					$('#supplier\\.no_hp').val(data[0].no_hp);
					$('#supplier\\.no_fax').val(data[0].no_fax);
					$('#supplier\\.email').val(data[0].email);
					$('input[name=supplier_id]').val(data[0].id);
					
					$(".kode").focus();
					
				}else{
					$('#supplier\\.nama').val("");
					$('#supplier\\.contact').val("");
					$('#supplier\\.alamat').val("");
					$('#supplier\\.kota').val("");
					$('#supplier\\.no_telp').val("");
					$('#supplier\\.no_hp').val("");
					$('#supplier\\.no_fax').val("");
					$('#supplier\\.email').val("");
					$('input[name=supplier_id]').val("");
				}
			});
			
			return false;
		}
	</script>
	<script type="text/javascript">
		$(document).ready(function(){
			var path="${path}";
			//pesan error diletakkan di alert juga
			var message = "${message}".replace(/<br\s*[\/]?>/gi, "\n");
			if(message != ''){
			 	alert(message);
			}

			//set onclick pada checkbox bagian input polis
			$("#kirimCheck").click( function(){
				togel($(this), "#contentkirim");
			});

			function replaceComma(nilai){
				nilai=nilai.replace(/[^0-9\.]+/g,"");
				if(isNaN(nilai)){
					result=0;
				}else{
					var result=nilai;
					if(result=="")result=nilai;
				}
				//alert(result);
				return result;
			}

			function syncPrice(triger){
				var grandTotal=0;
		     	  $.map($('input:text'), function(e, i) {
			    	var idx=$(e).attr("title");
			    	if(idx!=null){
			    		if($(e).attr("name")=="qty_"+idx){
			       			var qty=Number(isNaN($('#qty_'+idx).val())?0:$('#qty_'+idx).val());
			       			var harga=parseFloat(replaceComma($('#harga_'+idx).val()));
			       			var diskon=0.0;
			       			var persen_diskon=0.0;
			       			if("persen_diskon_"+idx==triger){
			       				persen_diskon=parseFloat(replaceComma($('#persen_diskon_'+idx).val()));
			       				diskon=(harga*persen_diskon)/100;
			       				$('#diskon_'+idx).val(formatNumber(diskon));
			       			}else if("diskon_"+idx==triger){
			       				diskon=parseFloat(replaceComma($('#diskon_'+idx).val()));
			       				persen_diskon=(diskon/harga)*100;
			       				$('#persen_diskon_'+idx).val(formatCurrency(persen_diskon));
			       			}else{
			       				diskon=parseFloat(replaceComma($('#diskon_'+idx).val()));
			       			}

			       			var subtotal=(harga-diskon)*qty;
			       			grandTotal+=subtotal;
			       			$('#subtotal_'+idx).text(formatNumber(subtotal));
			       			//alert("SUBTOTAL "+idx+" = ("+harga+"-"+diskon+")x"+qty+"( diskon: "+$('#diskon_'+idx).val()+" "+isNaN($('#diskon_'+idx).val())+")");
			       		}
			       	}
			       	$('.Total').text(formatNumber(grandTotal));
			      });

			    var diskon=0.0;
       			var persen_diskon=0.0;
			    var nilaippn=0.0;
       			var persen_nilaippn=0.0;
       			
		       	if("persen_disc"==triger){
       				persen_diskon=parseFloat(replaceComma($('#persen_disc').val()));
       				persen_nilaippn=parseFloat(replaceComma($('#persen_ppn').val()));
       				diskon=(grandTotal*persen_diskon)/100;
		       		nilaippn=((grandTotal-diskon)*persen_nilaippn)/100;
       				$('#total_disc').val(formatNumber(diskon));
       				$('#ppn').val(formatNumber(nilaippn));
       			}else if("total_disc"==triger){
       				diskon=parseFloat(replaceComma($('#total_disc').val()));
       				persen_nilaippn=parseFloat(replaceComma($('#persen_ppn').val()));
       				persen_diskon=(diskon/grandTotal)*100;
		       		nilaippn=((grandTotal-diskon)*persen_nilaippn)/100;
       				$('#persen_disc').val(formatCurrency(persen_diskon));
       				$('#ppn').val(formatNumber(nilaippn));
       			}else if("persen_ppn"==triger){
       				diskon=parseFloat(replaceComma($('#total_disc').val()));
       				persen_nilaippn=parseFloat(replaceComma($('#persen_ppn').val()));
       				nilaippn=((grandTotal-diskon)*persen_nilaippn)/100;
       				$('#ppn').val(formatNumber(nilaippn));
       			}else if("ppn"==triger){
       				diskon=parseFloat(replaceComma($('#total_disc').val()));
       				nilaippn=parseFloat(replaceComma($('#ppn').val()));
       				persen_nilaippn=(nilaippn/(grandTotal-diskon))*100;
       				$('#persen_ppn').val(formatCurrency(persen_nilaippn));
       			}else{
       				persen_diskon=parseFloat(replaceComma($('#persen_disc').val()));
       				diskon=(grandTotal*persen_diskon)/100;
       				$('#total_disc').val(formatNumber(diskon));

       				persen_nilaippn=parseFloat(replaceComma($('#persen_ppn').val()));
       				nilaippn=((grandTotal-diskon)*persen_nilaippn)/100;
       				$('#ppn').val(formatNumber(nilaippn));
       			}
				//alert("masuk");
   				$('.grandTotal').text(formatNumber(grandTotal-diskon+nilaippn));
			}
			
			function syncPrice2(triger){
				var harga=parseFloat(replaceComma($('#harga').val()));
			    var diskon=parseFloat(replaceComma($('#diskon').val()));
       			var diskon_persen=parseFloat(replaceComma($('#diskon_persen').val()));

		       	if("harga"==triger){
       				$('#diskon').val(formatNumber(harga*diskon_persen));
       			}else if("diskon"==triger){
       				
       				$('#diskon_persen').val(formatNumber((diskon/harga)*100));
       			}else if("diskon_persen"==triger){
       				$('#diskon').val(formatNumber((harga*diskon_persen)/100));
       			}

			}
			
			$("#table_wrapper").delegate('input:text', 'keyup', function(e){
				  syncPrice($(this).attr("id"));
		    });
			
			$("#table_wrapper2").delegate('input:text', 'keyup', function(e){
				  syncPrice2($(this).attr("id"));
		    });

		    $("#table_wrapper").delegate('input:text.nominal', 'blur', function(){
				  $(this).formatCurrency({ colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: 0});
		    });

			function addRowProduct(type){
				if($('#barcode_ext').val()=='') alert('Kode Produk Belum diisi');
				else if(parseFloat($('#stock').val())<=0&&'${trans.jenistrans }'=='Penjualan'&&'${trans.pagename }'!='Order'){
					var barcode=$('#barcode_ext').val();

					 if(confirm('Maaf Stock saat ini tidak tersedia [Stock Exist : '+parseFloat($('#stock').val())+'].\nApakah Anda ingin cek stock di cabang lain?')){
					 	doAction('${path}/stockall?barcode='+barcode, 'Cek Stock '+barcode+' Cabang Lain',1000,500);
					 }
				}else if(parseFloat($('#stock').val())<parseFloat($('#qty').val())&&'${trans.jenistrans }'=='Penjualan'&&'${trans.pagename }'!='Order'){
					var barcode=$('#barcode_ext').val();
					 if(confirm('Maaf QTY melebihi Stock saat ini [Stock Exist : '+parseFloat($('#stock').val())+' ; QTY : '+parseFloat($('#qty').val())+']. \nApakah Anda ingin cek stock di cabang lain?')){
					 	doAction('${path}/stockall?barcode='+barcode, 'Cek Stock '+barcode+' Cabang Lain',1000,500);
					 }
				}else if($('#nama_produk').val()=='') alert('Nama Produk Belum diisi');
				else{

					if(parseFloat($('#stock').val())<=0&&'${trans.jenistrans }'=='Penjualan'&&'${trans.pagename }'=='Order'){
						var barcode=$('#barcode_ext').val();
						 if(confirm('Perhatian!!\n Stock saat ini tidak tersedia [Stock Exist : '+parseFloat($('#stock').val())+'].\nApakah Anda ingin melanjutkan?')==false){
						 	return false;
						 }
					}else if(parseFloat($('#stock').val())<parseFloat($('#qty').val())&&'${trans.jenistrans }'=='Penjualan'&&'${trans.pagename }'=='Order'){
						var barcode=$('#barcode_ext').val();
						 if(confirm('Perhatian!!\n QTY melebihi Stock saat ini [Stock Exist : '+parseFloat($('#stock').val())+' ; QTY : '+parseFloat($('#qty').val())+']. \nApakah Anda ingin melanjutkan?')==false){
						 	return false;
						 }
					}
					var arr = type.closest('form').serializeArray();
					var tbl = $('.tbl_repeat tr').length;
					$.getJSON(path+'/json/addRow?tbl='+tbl, arr, function(data) {
						var len = data.length;

						if(data[0].desc=="ga"){
							alert("Kode Produk : "+$('#barcode_ext').val()+" tidak ditemukan");
						}else{

							var rows = $('.tbl_repeat tr').length;
							for (var i = 0; i< len; i++) {
								$('#table_wrapper').html(
									$(data[i].value).hide().fadeIn(350)
								);
							}
							$('#barcode_ext').val('');
							$('#qty').val('1');
							$('#harga').val('');
							$('#diskon').val('');
							$('#nama_produk').val('');
							$('#diskon_persen').val('');
							$('#stock').val('');
							$('#satuan').val('');
						}
					}, 'json');

				}
				
				$(".kode").focus();
				
				return false;

			}

			$('.remove').live('click', function() {
				var trigger = $(this);
				var arr = $(this).closest('form').serializeArray();
				var id = $(this).attr('rel');
				$.getJSON(path+'/json/removeRow?id='+id, arr, function(data) {
					var len = data.length;
					var rows = $('.tbl_repeat tr').length;
					for (var i = 0; i< len; i++) {
						//if (tbl === 0) {
							$('#table_wrapper').html(
								$(data[i].value).hide().fadeIn(350)
							);
						/* //} else {
							$('.tbl_repeat tr:last').after(
								$(data[i].value).hide().fadeIn(300)
									.css('display', 'table-row')
							);
						} */
					}
				});
				
				$(".kode").focus();
				
				return false;
			});

			$('.add_new').click(function() {
				addRowProduct($(this));
				
			});

			$('#qty').val('1');

			 $('#barcode_ext').live('keyup', function(e) {
				if($(this).val().length>2){
					$.getJSON(path+'/json/completeItemPrice?barcode_ext='+$(this).val(), function(data) {
						if(data!=""){
							$('#nama_produk').val(data[0].nama);

							if('${trans.jenistrans}'=='Penjualan'){
								if($('input[name=flag_ecer]:checked').val()==1){
									$('#harga').val(formatCurrency(data[0].harga_ecer));
									$('#diskon').val(formatCurrency(data[0].diskon_ecer));
									$('#diskon_persen').val(formatCurrency((data[0].diskon_ecer/data[0].harga_ecer)*100));
								}else {
									$('#harga').val(formatCurrency(data[0].harga));
									$('#diskon').val(formatCurrency(data[0].diskon));
									$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga)*100));
								}
							}

							$('#stock').val(data[0].stock_jual);
							$('#satuan').val(data[0].satuan_idNama);
						}else{
							$('#nama_produk').val("");
							$('#harga').val("");
							$('#diskon_persen').val("");
							$('#diskon').val("");
							$('#stock').val("");
							$('#satuan').val("");
						}
					});
				}
				return false;
			});

			$('#nama_produk').live('keyup', function(e) {
				if($(this).val().length>2){

					$.getJSON(path+'/json/completeItemPrice?nama_produk='+$(this).val(), function(data) {
						if(data!=""){
							$('#barcode_ext').val(data[0].barcode_ext);
							if('${trans.jenistrans}'=='Penjualan'){
								if($('input[name=flag_ecer]:checked').val()==1){
									$('#harga').val(formatCurrency(data[0].harga_ecer));
									$('#diskon').val(formatCurrency(data[0].diskon_ecer));
									$('#diskon_persen').val(formatCurrency((data[0].diskon_ecer/data[0].harga_ecer)*100));
								}else {
									$('#harga').val(formatCurrency(data[0].harga));
									$('#diskon').val(formatCurrency(data[0].diskon));
									$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga)*100));
								}
							}
							$('#stock').val(data[0].stock_jual);
							$('#satuan').val(data[0].satuan_idNama);

						}else{
							$('#barcode_ext').val("");
							$('#harga').val("");
							$('#diskon_persen').val("");
							$('#diskon').val("");
							$('#stock').val("");
							$('#satuan').val("");
						}
					});
				}
				return false;
			});

			$('#barcode_ext,#nama_produk,#qty,#harga').keypress(function(e) {
			  if (e.keyCode == '13') {
			  		 e.preventDefault();
			  		 if($('#nama_produk').val()=='') {
			  			var add=false;
			  		 	 $.ajax({
					     async: false,
					     url: path+'/json/completeItemPrice?barcode_ext='+$(this).val(),
					     dataType: "json",
					     success: function(data) {
					            if(data.length!='0'){
					       		$('#nama_produk').val(data[0].nama);
								if('${trans.jenistrans}'=='Penjualan'){
									if($('input[name=flag_ecer]:checked').val()==1){
										$('#harga').val(formatCurrency(data[0].harga_ecer));
										$('#diskon').val(formatCurrency(data[0].diskon_ecer));
										$('#diskon_persen').val(formatCurrency((data[0].diskon_ecer/data[0].harga_ecer)*100));
									}else {
										$('#harga').val(formatCurrency(data[0].harga));
										$('#diskon').val(formatCurrency(data[0].diskon));
										$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga)*100));
									}
								}
								$('#stock').val(data[0].stock_jual);
								$('#satuan').val(data[0].satuan_idNama);

								add=true;
							}else{
								$('#nama_produk').val("");
								$('#harga').val("");
								$('#diskon_persen').val("");
								$('#diskon').val("");
								$('#stock').val("");
								$('#satuan').val("");
							}
					     }
					   });
				  	 	 if(add)addRowProduct($(this));
					}else{
						addRowProduct($(this));
					}
			   }
			});


			  $("#nama_produk").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteItemPrice",
					data: {
						nama_produk: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nama,
								value: item.nama,
								barcode_id: item.barcode_ext,
								nama : item.nama,
								harga : item.harga,
								harga_ecer : item.harga_ecer,
								diskon :item.diskon,
								diskon_ecer :item.diskon_ecer,
								stock :item.stock_jual,
								satuan : item.satuan_idNama
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#barcode_ext').val(ui.item.barcode_id);
					if('${trans.jenistrans}'=='Penjualan'){
						if($('input[name=flag_ecer]:checked').val()==1){
							$('#harga').val(formatCurrency(ui.item.harga_ecer));
							$('#diskon').val(formatCurrency(ui.item.diskon_ecer));
							$('#diskon_persen').val(formatCurrency((ui.item.diskon_ecer/ui.item.harga_ecer)*100));
						}else {
							$('#harga').val(formatCurrency(ui.item.harga));
							$('#diskon').val(formatCurrency(ui.item.diskon));
							$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga)*100));
						}
					}

					$('#stock').val(ui.item.stock);
					$('#satuan').val(ui.item.satuan);
				}
			});

			$("#barcode_ext").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteItemPrice",
					data: {
						barcode_ext: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.barcode_ext,
								value: item.barcode_ext,
								barcode_id: item.barcode_ext,
								nama : item.nama,
								harga : item.harga,
								harga_ecer : item.harga_ecer,
								diskon :item.diskon,
								diskon_ecer :item.diskon_ecer,
								stock : item.stock_jual,
								satuan : item.satuan_idNama
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#nama_produk').val(ui.item.nama);
					if('${trans.jenistrans}'=='Penjualan'){
						if($('input[name=flag_ecer]:checked').val()==1){
							$('#harga').val(formatCurrency(ui.item.harga_ecer));
							$('#diskon').val(formatCurrency(ui.item.diskon_ecer));
							$('#diskon_persen').val(formatCurrency((ui.item.diskon_ecer/ui.item.harga_ecer)*100));
						}else {
							$('#harga').val(formatCurrency(ui.item.harga));
							$('#diskon').val(formatCurrency(ui.item.diskon));
							$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga)*100));
						}
					}
					$('#stock').val(ui.item.stock);
					$('#satuan').val(ui.item.satuan);
				}
			});

			$("#karyawan\\.nik").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteKaryawan",
					data: {
						nik: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nik,
								value: item.nik,
								nama : item.nama,
								id: item.id
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#karyawan\\.nama').val(ui.item.nama);
					$('input[name=sales_id]').val(ui.item.id);

				}
			});

			 $('#karyawan\\.nik').live('keyup', function(e) {

				if($(this).val().length>2){

					$.getJSON(path+'/json/completeKaryawan?nik='+$(this).val(), function(data) {
						if(data!=""){
							$('#karyawan\\.nama').val(data[0].nama);
							$('input[name=sales_id]').val(data[0].id);

						}else{
							$('#karyawan\\.nama').val("");
							$('input[name=sales_id]').val("");
						}
					});
				}
				return false;
			});

			$('#karyawan\\.nama').live('keyup', function(e) {

				if($(this).val().length>2){

					$.getJSON(path+'/json/completeKaryawan?nama='+$(this).val(), function(data) {
						if(data!=""){
							$('#karyawan\\.nik').val(data[0].nik);
							$('input[name=sales_id]').val(data[0].id);

						}else{
							$('#karyawan\\.nik').val("");
							$('input[name=sales_id]').val("");
						}
					});
				}
				return false;
			});




			  $("#karyawan\\.nama").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteKaryawan",
					data: {
						nama: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nama,
								value: item.nama,
								nik: item.nik,
								id:item.id
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#karyawan\\.nik').val(ui.item.nik);
					$('input[name=sales_id]').val(ui.item.id);
				}
			});


			$("#customer\\.kode").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteCustomer",
					data: {
						kode: request.term
					},
					success: function( data ) {
						//alert('masuk2'); //apabila pakai cara klik 'cari'
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.kode,
								value: item.kode,
								nama : item.nama,
								contact : item.contact,
								alamat : item.alamat,
								kota : item.kota,
								telp : item.no_telp,
								no_hp : item.no_hp,
								id : item.id,
								flag_ecer : item.flag_ecer,
								pay_mode : item.pay_mode,
								flag_pajak : item.pkp
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#customer\\.nama').val(ui.item.nama);
					$('#customer\\.contact').val(ui.item.contact);
					$('#customer\\.alamat').val(ui.item.alamat);
					$('#customer\\.kota').val(ui.item.kota);
					$('#customer\\.no_telp').val(ui.item.no_telp);
					$('#customer\\.no_hp').val(ui.item.no_hp);
					$('input[name=customer_id]').val(ui.item.id);
					$("input[name=flag_ecer][value=" + ui.flag_ecer + "]").attr('checked', 'checked');
					$("input[name=pay_mode][value=" + ui.pay_mode + "]").attr('checked', 'checked');
					$("input[name=flag_pajak][value=" + ui.flag_pajak + "]").attr('checked', 'checked');
					//alert('masuk3'); //tidak pernah debug kesini apapun kondisinya
					var nasabah="\nKode : "+ui.item.value+
								"\nNama : "+ui.item.nama+
								"\nContact Person : "+ui.item.contact+
								"\nAlamat : "+ui.item.alamat+
								"\nKota : "+ui.item.kota+
								"\nNo Telpon : "+ui.item.no_telp+
								"\nNo HP : "+ui.item.no_hp;
					if(confirm("Apakah data customer ingin dicopy?\n"+nasabah)==false){
						$('#customer\\.nama').val("");
						$('#customer\\.contact').val("");
						$('#customer\\.alamat').val("");
						$('#customer\\.kota').val("");
						$('#customer\\.no_telp').val("");
						$('#customer\\.no_hp').val("");
						$('input[name=customer_id]').val("");
						$("input[name=flag_ecer][value=1]").attr('checked', 'checked');
						$("input[name=pay_mode][value=1]").attr('checked', 'checked');
						$("input[name=flag_pajak][value=0]").attr('checked', 'checked');

					}else{
						$(".kode").focus();
					}

				}
			});

			 $('#customer\\.kode').live('keyup', function(e) {

				if($(this).val().length>2){

					$.getJSON(path+'/json/completeCustomer?kode='+$(this).val(), function(data) {
						if(data!=""){
							$('#customer\\.nama').val(data[0].nama);
							$('#customer\\.contact').val(data[0].contact);
							$('#customer\\.alamat').val(data[0].alamat);
							$('#customer\\.kota').val(data[0].kota);
							$('#customer\\.no_telp').val(data[0].no_telp);
							$('#customer\\.no_hp').val(data[0].no_hp);
							$('input[name=customer_id]').val(data[0].id);
							$("input[name=flag_ecer][value=" + data[0].flag_ecer + "]").attr('checked', 'checked');
							$("input[name=pay_mode][value=" + data[0].pay_mode + "]").attr('checked', 'checked');
							$("input[name=flag_pajak][value=" + data[0].pkp + "]").attr('checked', 'checked');
							//alert('masuk4');
							var nasabah="\nKode : "+data[0].kode+
								"\nNama : "+data[0].nama+
								"\nContact Person : "+data[0].contact+
								"\nAlamat : "+data[0].alamat+
								"\nKota : "+data[0].kota+
								"\nNo Telpon : "+data[0].no_telp+
								"\nNo HP : "+data[0].no_hp;
							if(confirm("Apakah data customer ingin dicopy?\n"+nasabah)==false){
								$('#customer\\.nama').val("");
								$('#customer\\.contact').val("");
								$('#customer\\.alamat').val("");
								$('#customer\\.kota').val("");
								$('#customer\\.no_telp').val("");
								$('#customer\\.no_hp').val("");
								$('input[name=customer_id]').val("");
								$("input[name=flag_ecer][value=1]").attr('checked', 'checked');
								$("input[name=pay_mode][value=1]").attr('checked', 'checked');
								$("input[name=flag_pajak][value=0]").attr('checked', 'checked');
							}else{
								$(".kode").focus();
							}
						}else{
							$('#customer\\.nama').val("");
							$('#customer\\.contact').val("");
							$('#customer\\.alamat').val("");
							$('#customer\\.kota').val("");
							$('#customer\\.no_telp').val("");
							$('#customer\\.no_hp').val("");
							$('input[name=customer_id]').val("");
							$("input[name=flag_ecer][value=1]").attr('checked', 'checked');
							$("input[name=pay_mode][value=1]").attr('checked', 'checked');
							$("input[name=flag_pajak][value=0]").attr('checked', 'checked');
						}
					});
				}
				return false;
			});

			$('#customer\\.nama').live('keyup', function(e) {

				if($(this).val().length>2){

					$.getJSON(path+'/json/completeCustomer?nama='+$(this).val(), function(data) {
						if(data!=""){
							$('#customer\\.kode').val(data[0].kode);
							$('#customer\\.contact').val(data[0].contact);
							$('#customer\\.alamat').val(data[0].alamat);
							$('#customer\\.kota').val(data[0].kota);
							$('#customer\\.no_telp').val(data[0].no_telp);
							$('#customer\\.no_hp').val(data[0].no_hp);
							$('input[name=customer_id]').val(data[0].id);
							$("input[name=flag_ecer][value=" + data[0].flag_ecer + "]").attr('checked', 'checked');
							$("input[name=pay_mode][value=" + data[0].pay_mode + "]").attr('checked', 'checked');
							$("input[name=flag_pajak][value=" + data[0].pkp + "]").attr('checked', 'checked');
							//alert('masuk5'); //tidak pernah masuk kesini apapun kondisinya
							var nasabah="\nKode : "+data[0].kode+
								"\nNama : "+data[0].nama+
								"\nContact Person : "+data[0].contact+
								"\nAlamat : "+data[0].alamat+
								"\nKota : "+data[0].kota+
								"\nNo Telpon : "+data[0].no_telp+
								"\nNo HP : "+data[0].no_hp;
							if(confirm("Apakah data customer ingin dicopy?\n"+nasabah)==false){
								$('#customer\\.kode').val("");
								$('#customer\\.contact').val("");
								$('#customer\\.alamat').val("");
								$('#customer\\.kota').val("");
								$('#customer\\.no_telp').val("");
								$('#customer\\.no_hp').val("");
								$('input[name=customer_id]').val("");
								$("input[name=flag_ecer][value=1]").attr('checked', 'checked');
								$("input[name=pay_mode][value=1]").attr('checked', 'checked');
								$("input[name=flag_pajak][value=0]").attr('checked', 'checked');
							}else{
								$(".kode").focus();
							}
						}else{
							$('#customer\\.kode').val("");
							$('#customer\\.contact').val("");
							$('#customer\\.alamat').val("");
							$('#customer\\.kota').val("");
							$('#customer\\.no_telp').val("");
							$('#customer\\.no_hp').val("");
							$('input[name=customer_id]').val("");
							$("input[name=flag_ecer][value=1]").attr('checked', 'checked');
							$("input[name=pay_mode][value=1]").attr('checked', 'checked');
							$("input[name=flag_pajak][value=0]").attr('checked', 'checked');
						}
					});
				}
				return false;
			});
			
			
			  $("#customer\\.nama").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteCustomer",
					data: {
						nama: request.term
					},
					success: function( data ) {
						//alert('masuk6'); //tidak pernah masuk kesini apapun kondisinya
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nama,
								value: item.nama,
								kode: item.kode,
								contact : item.contact,
								alamat : item.alamat,
								kota : item.kota,
								telp : item.no_telp,
								no_hp : item.no_hp,
								id : item.id,
								flag_ecer : item.flag_ecer,
								pay_mode : item.pay_mode,
								flag_pajak : item.pkp
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#customer\\.kode').val(ui.item.kode);
					$('#customer\\.contact').val(ui.item.contact);
					$('#customer\\.alamat').val(ui.item.alamat);
					$('#customer\\.kota').val(ui.item.kota);
					$('#customer\\.no_telp').val(ui.item.no_telp);
					$('#customer\\.no_hp').val(ui.item.no_hp);
					$('input[name=customer_id]').val(ui.item.id);
					$("input[name=flag_ecer][value=" + ui.flag_ecer + "]").attr('checked', 'checked');
					$("input[name=pay_mode][value=" + ui.pay_mode + "]").attr('checked', 'checked');
					$("input[name=flag_pajak][value=" + ui.flag_pajak + "]").attr('checked', 'checked');
					//alert('masuk7'); ////tidak pernah masuk kesini apapun kondisinya
					var nasabah="\nKode : "+ui.item.kode+
								"\nNama : "+ui.item.value+
								"\nContact Person : "+ui.item.contact+
								"\nAlamat : "+ui.item.alamat+
								"\nKota : "+ui.item.kota+
								"\nNo Telpon : "+ui.item.no_telp+
								"\nNo HP : "+ui.item.no_hp;
					if(confirm("Apakah data customer ingin dicopy?\n"+nasabah)== false){
						$('#customer\\.kode').val("");
						$('#customer\\.contact').val("");
						$('#customer\\.alamat').val("");
						$('#customer\\.kota').val("");
						$('#customer\\.no_telp').val("");
						$('#customer\\.no_hp').val("");
						$('input[name=customer_id]').val("");
						$("input[name=flag_ecer][value=1]").attr('checked', 'checked');
						$("input[name=pay_mode][value=1]").attr('checked', 'checked');
						$("input[name=flag_pajak][value=0]").attr('checked', 'checked');
					}else{
						$(".kode").focus();
					}

				}
			});

			$("#supplier\\.kode").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteSupplier",
					data: {
						kode: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.kode,
								value: item.kode,
								nama : item.nama,
								contact : item.contact,
								alamat : item.alamat,
								kota : item.kota,
								no_telp : item.no_telp,
								no_hp : item.no_hp,
								no_fax : item.no_fax,
								email : item.email,
								id : item.id
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#supplier\\.nama').val(ui.item.nama);
					$('#supplier\\.contact').val(ui.item.contact);
					$('#supplier\\.alamat').val(ui.item.alamat);
					$('#supplier\\.kota').val(ui.item.kota);
					$('#supplier\\.no_telp').val(ui.item.no_telp);
					$('#supplier\\.no_hp').val(ui.item.no_hp);
					$('#supplier\\.no_fax').val(ui.item.no_fax);
					$('#supplier\\.email').val(ui.item.email);
					$('input[name=supplier_id]').val(ui.item.id);
					var nasabah="\nKode : "+ui.item.value+
								"\nNama : "+ui.item.nama+
								"\nContact Person : "+ui.item.contact+
								"\nAlamat : "+ui.item.alamat+
								"\nKota : "+ui.item.kota+
								"\nEmail : "+ui.item.email+
								"\nNo Telpon : "+ui.item.no_telp+
								"\nNo HP : "+ui.item.no_hp+
								"\nNo Fax : "+ui.item.no_fax;
					if(confirm("Apakah data supplier ingin dicopy?\n"+nasabah)==false){
						$('#supplier\\.nama').val("");
						$('#supplier\\.contact').val("");
						$('#supplier\\.alamat').val("");
						$('#supplier\\.kota').val("");
						$('#supplier\\.no_telp').val("");
						$('#supplier\\.no_hp').val("");
						$('#supplier\\.no_fax').val("");
						$('#supplier\\.email').val("");
						$('input[name=supplier_id]').val("");
					}else{
						$(".kode").focus();
					}

				}
			});

			 $('#supplier\\.kode').live('keyup', function(e) {

				if($(this).val().length>2){

					$.getJSON(path+'/json/completeSupplier?kode='+$(this).val(), function(data) {
						if(data!=""){
							$('#supplier\\.nama').val(data[0].nama);
							$('#supplier\\.contact').val(data[0].contact);
							$('#supplier\\.alamat').val(data[0].alamat);
							$('#supplier\\.kota').val(data[0].kota);
							$('#supplier\\.no_telp').val(data[0].no_telp);
							$('#supplier\\.no_hp').val(data[0].no_hp);
							$('#supplier\\.no_fax').val(data[0].no_fax);
							$('#supplier\\.email').val(data[0].email);
							$('input[name=supplier_id]').val(data[0].id);
							var nasabah="\nKode : "+data[0].kode+
								"\nNama : "+data[0].nama+
								"\nContact Person : "+data[0].contact+
								"\nAlamat : "+data[0].alamat+
								"\nKota : "+data[0].kota+
								"\nEmail : "+data[0].email+
								"\nNo Telpon : "+data[0].no_telp+
								"\nNo HP : "+data[0].no_hp+
								"\nNo Fax : "+data[0].no_fax;
							if(confirm("Apakah data supplier ingin dicopy?\n"+nasabah)==false){
								$('#supplier\\.nama').val("");
								$('#supplier\\.contact').val("");
								$('#supplier\\.alamat').val("");
								$('#supplier\\.kota').val("");
								$('#supplier\\.no_telp').val("");
								$('#supplier\\.no_hp').val("");
								$('#supplier\\.no_fax').val("");
								$('#supplier\\.email').val("");
								$('input[name=supplier_id]').val("");
							}else{
								$(".kode").focus();
							}
						}else{
							$('#supplier\\.nama').val("");
							$('#supplier\\.contact').val("");
							$('#supplier\\.alamat').val("");
							$('#supplier\\.kota').val("");
							$('#supplier\\.no_telp').val("");
							$('#supplier\\.no_hp').val("");
							$('#supplier\\.no_fax').val("");
							$('#supplier\\.email').val("");
							$('input[name=supplier_id]').val("");
						}
					});
				}
				return false;
			});

			$('#supplier\\.nama').live('keyup', function(e) {

				if($(this).val().length>2){

					$.getJSON(path+'/json/completeSupplier?nama='+$(this).val(), function(data) {
						if(data!=""){
							$('#supplier\\.kode').val(data[0].kode);
							$('#supplier\\.contact').val(data[0].contact);
							$('#supplier\\.alamat').val(data[0].alamat);
							$('#supplier\\.kota').val(data[0].kota);
							$('#supplier\\.no_telp').val(data[0].no_telp);
							$('#supplier\\.no_hp').val(data[0].no_hp);
							$('#supplier\\.no_fax').val(data[0].no_fax);
							$('#supplier\\.email').val(data[0].email);
							$('input[name=supplier_id]').val(data[0].id);
							var nasabah="\nKode : "+data[0].kode+
								"\nNama : "+data[0].nama+
								"\nContact Person : "+data[0].contact+
								"\nAlamat : "+data[0].alamat+
								"\nKota : "+data[0].kota+
								"\nEmail : "+data[0].email+
								"\nNo Telpon : "+data[0].no_telp+
								"\nNo HP : "+data[0].no_hp+
								"\nNo Fax : "+data[0].no_fax;
							if(confirm("Apakah data supplier ingin dicopy?\n"+nasabah)==false){
								$('#supplier\\.kode').val("");
								$('#supplier\\.contact').val("");
								$('#supplier\\.alamat').val("");
								$('#supplier\\.kota').val("");
								$('#supplier\\.no_telp').val("");
								$('#supplier\\.no_hp').val("");
								$('#supplier\\.email').val("");
								$('#supplier\\.no_fax').val("");
								$('input[name=supplier_id]').val("");
							}else{
								$(".kode").focus();
							}
						}else{
							$('#supplier\\.kode').val("");
							$('#supplier\\.contact').val("");
							$('#supplier\\.alamat').val("");
							$('#supplier\\.kota').val("");
							$('#supplier\\.no_telp').val("");
							$('#supplier\\.no_hp').val("");
							$('#supplier\\.email').val("");
							$('#supplier\\.no_fax').val("");
							$('input[name=supplier_id]').val("");
						}
					});
				}
				return false;
			});




			  $("#supplier\\.nama").autocomplete({
				source:function( request, response ) {
					$.ajax({
					url: "${path}/json/autocompleteSupplier",
					data: {
						nama: request.term
					},
					success: function( data ) {
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nama,
								value: item.nama,
								kode: item.kode,
								contact : item.contact,
								alamat : item.alamat,
								kota : item.kota,
								no_telp : item.no_telp,
								no_hp : item.no_hp,
								email : item.email,
								no_fax : item.no_fax,
								id : item.id
							};
						}));
					}
				});
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#supplier\\.kode').val(ui.item.kode);
					$('#supplier\\.contact').val(ui.item.contact);
					$('#supplier\\.alamat').val(ui.item.alamat);
					$('#supplier\\.kota').val(ui.item.kota);
					$('#supplier\\.no_telp').val(ui.item.no_telp);
					$('#supplier\\.no_hp').val(ui.item.no_hp);
					$('#supplier\\.no_fax').val(ui.item.no_fax);
					$('#supplier\\.email').val(ui.item.email);
					$('input[name=supplier_id]').val(ui.item.id);
					var nasabah="\nKode : "+ui.item.kode+
								"\nNama : "+ui.item.value+
								"\nContact Person : "+ui.item.contact+
								"\nAlamat : "+ui.item.alamat+
								"\nKota : "+ui.item.kota+
								"\nEmail : "+ui.item.email+
								"\nNo Telpon : "+ui.item.no_telp+
								"\nNo HP : "+ui.item.no_hp+
								"\nNo Fax : "+ui.item.no_fax;
					if(confirm("Apakah data supplier ingin dicopy?\n"+nasabah)== false){
						$('#supplier\\.kode').val("");
						$('#supplier\\.contact').val("");
						$('#supplier\\.alamat').val("");
						$('#supplier\\.kota').val("");
						$('#supplier\\.no_telp').val("");
						$('#supplier\\.no_hp').val("");
						$('#supplier\\.email').val("");
						$('#supplier\\.no_fax').val("");
						$('input[name=supplier_id]').val("");
					}else{
						$(".kode").focus();
					}

				}
			});

			$("#copyCstToKrm").click( function(){
				$("#alamat_tujuan").val($("#customer\\.alamat").val());
				$("#contact_tujuan").val($("#customer\\.contact").val());
				$("#telp_tujuan").val($("#customer\\.no_telp").val());
				return false;
			});

			//jalankan fungsi2 awal untuk set nilai2
			togel($("#kirimCheck"), "#contentkirim");
			
			
		});
		
		function saveAja(){
				if(confirm('Are you sure want to save?')){
					if("${closingWarning}"=="false"){
						if(!confirm("Stock belum di closing. Apakah transaksi ingin dimasukkan ke periode sebelumnya?")){
							
						}else{ formpost.submit();}
					}else{
						formpost.submit();
					}
				}else{
					
				}
			}

	</script>
</head>
<body>
	<div style="float: right; padding-top: 7px;padding-right: 5px;">

	    <a href="${path}/transaksi/${trans.jenistrans }/${trans.pagename }/new" title="Add ${trans.pagename } ${trans.jenistrans }"><img title="Add ${trans.pagename } ${trans.jenistrans }" alt="add ${trans.pagename } ${trans.jenistrans }" src="${path }/static/images/icons/addlist.png"></a>
	     <c:if test="${trans.pagename eq \"Input\"}"><a href="javascript:doAction('${path}/transaksi/${trans.jenistrans }/OrderTransfer', 'Find Order ${trans.jenistrans }',1000,500,false);" title="Find Order ${trans.jenistrans }" ><img alt="Find Order ${trans.jenistrans }" title="Find Order ${trans.jenistrans}" src="${path }/static/images/icons/findlist.png"></a></c:if>
	 	<a href="javascript:doAction('${path}/transaksi/${trans.jenistrans }/${trans.pagename }', 'List ${trans.pagename } ${trans.jenistrans }',1000,500,false);" title="List ${trans.pagename } ${trans.jenistrans }"><img alt="list ${trans.pagename } ${trans.jenistrans }" title="List ${trans.pagename } ${trans.jenistrans }" src="${path }/static/images/icons/list.png"></a>
	 </div>
	<h1>Input Transaksi ${trans.pagename } ${trans.jenistrans } &gt; ${trans.mode }
	 </h1>

	
    <form:form commandName="trans" name="formpost" method="POST" action="${path}/transaksi/save" cssClass="form" >


		<input type="hidden" name="message" id="message" value="${message }">
		
 
   			<div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="no_trans"  cssClass="label" cssErrorClass="label labelError">No Transaksi</form:label>
		               <form:errors path="no_trans" cssClass="error" />
			     </div>

			    <form:input path="no_trans" cssClass="text_field read" readonly="true"  cssErrorClass="text_field error read" size="15"/>
			   <form:hidden path="trans_id"/>
		     	 <span class="description"></span>
		    </div>
		    <c:if test="${trans.pagename eq \"Input\" }">
			    <div class="group">
			    	<div class="fieldWithErrors">
		               <form:label path="no_trans_ref"  cssClass="label" cssErrorClass="label labelError">No Order</form:label>
		         		 <form:hidden path="no_trans_ref"/>
		               <form:errors path="no_trans_ref" cssClass="error" />
		        	</div>
		            <input type="text" value="${trans.no_trans_ref}" class="read" readonly="readonly">
			     </div>
   			</c:if>
   			<c:choose>
   				<c:when test="${trans.flag_akses_all eq 1 }">
   					<div class="group">
						<div class="fieldWithErrors">
							<form:label path="retail_id"  cssClass="label" cssErrorClass="label labelError">Cabang Penerima Stock</form:label>
							<form:errors path="retail_id" cssClass="error" />
						</div>
						<c:choose>
							<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="retail_id" /><input type="text" class="text_field read" value="${trans.retail_idKet}" readonly="readonly" size="50"/></c:when>
							<c:otherwise>
								<form:select path="retail_id" items="${reff.lsCabang}" itemLabel="value" itemValue="key"></form:select>
							</c:otherwise>
						</c:choose>
						<span class="description"></span>
					</div>
   				</c:when>
   				<c:otherwise>
   					<div class="group">
				         <div class="fieldWithErrors">
				               <form:label path="retail_id"  cssClass="label" cssErrorClass="label labelError">Nama Cabang</form:label>
				               <form:errors path="retail_id" cssClass="error" />
					     </div>
							<input type="text" class="text_field read" value="${trans.retail_idKet}" readonly="readonly" size="20"/>
						 <span class="description"></span>
				    </div>
   				</c:otherwise>
   			</c:choose>
		    
   			<form:hidden path="retail_id"/>
   			
 			<c:if test="${not empty trans.tgl_order }">
 			<div class="group" style="float: left;margin-right: 30px;">
		         <div class="fieldWithErrors">
		               <label class="label">Tanggal Order</label>
		               
			     </div>
					<fmt:formatDate value="${trans.tgl_order }" pattern="dd-MM-yyyy HH:mm:ss"/>
	   				<form:hidden path="tgl_order"/>
				 <span class="description"></span>
		    </div>
		    </c:if>
		    <c:if test="${not empty trans.trans_date }">
	 			<div class="group" style="float: left; ">
			         <div class="fieldWithErrors">
			               <label class="label">Tanggal Transaksi</label>
			               <form:errors path="retail_id" cssClass="error" />
				     </div>
					<fmt:formatDate value="${trans.trans_date }" pattern="dd-MM-yyyy HH:mm:ss"/>
		   			<form:hidden path="trans_date"/>
					 <span class="description"></span>
			    </div>
		    </c:if>
 		 	
	<c:choose>
		<c:when test="${trans.pagename eq \"Retur\" }">				
	   		
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="no_trans_ref"  cssClass="label" cssErrorClass="label labelError">Referensi No Transaksi</form:label>
			               <form:errors path="no_trans_ref" cssClass="error" />
				     </div>
			     	 <c:choose>
						<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="no_trans_ref" /><input type="text" class="text_field read" value="${trans.no_trans_ref}" readonly="readonly" size="10"/></c:when>
						<c:otherwise>

							<form:input path="no_trans_ref" cssClass="text_field target" cssErrorClass="text_field_error"/>
						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
	   		
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="retur_type"  cssClass="label" cssErrorClass="label labelError">Type Retur</form:label>
			               <form:errors path="retur_type" cssClass="error" />
				     </div>
			     	 <c:choose>
						<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="retur_type" /><input type="text" class="text_field read" value="${trans.retur_typeKet}" readonly="readonly" size="15"/></c:when>
						<c:otherwise>
							<c:forEach items="${reff.lsReturType }" var="lp" varStatus="ls">
								<form:radiobutton path="retur_type" value="${lp.key}"  label="${lp.value}" />
							</c:forEach>

						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
	   			
	   		
		</c:when>
		<c:otherwise>
				<c:if test="${trans.jenistrans ne \"Pembelian\" }">		
		   			<div class="group">
				         <div class="fieldWithErrors">
				               <form:label path="flag_ecer"  cssClass="label" cssErrorClass="label labelError">Jenis ${trans.jenistrans }</form:label>
				               <form:errors path="flag_ecer" cssClass="error" />
					     </div>
				     	 <c:choose>
							<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="flag_ecer" /><input type="text" class="text_field read" value="${trans.flag_ecerKet}" readonly="readonly" size="10"/></c:when>
							<c:otherwise>
	
								<form:radiobutton path="flag_ecer" value="1"  label="Eceran"  cssClass="target" cssErrorClass="target error"/>
								<form:radiobutton path="flag_ecer" value="0" label="Grosir"/>
							</c:otherwise>
						 </c:choose>
						 <span class="description"></span>
				    </div>
			    </c:if>
	   		
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="pay_mode"  cssClass="label" cssErrorClass="label labelError">Cara Bayar</form:label>
			               <form:errors path="pay_mode" cssClass="error" />
				     </div>
			     	 <c:choose>
						<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="pay_mode" /><input type="text" class="text_field read" value="${trans.pay_modeKet}" readonly="readonly" size="10"/></c:when>
						<c:otherwise>
							<c:forEach items="${reff.lsPayMode }" var="lp" varStatus="ls">
								<c:if test="${trans.jenistrans eq \"Pembelian\" }">
									<c:choose>
										<c:when test="${ls.count eq \"1\"}">
											<c:set var="target" value="target" />
										</c:when>
										<c:otherwise>
											<c:set var="target" value="" />
										</c:otherwise>
									</c:choose>
								</c:if>
								
								<form:radiobutton path="pay_mode" value="${lp.key}"  label="${lp.value}" cssClass="${target }" />
							</c:forEach>
						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
			    
	   			<div class="group">
			         <div class="fieldWithErrors">
			               <form:label path="flag_pajak"  cssClass="label" cssErrorClass="label labelError">Status</form:label>
			               <form:errors path="flag_pajak" cssClass="error" />
				     </div>
			     	 <c:choose>
						<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="flag_pajak" /><input type="text" class="text_field read" value="${trans.flag_pajak}" readonly="readonly" size="10"/></c:when>
						<c:otherwise>

							<form:radiobutton path="flag_pajak" value="1"  label="PKP"  cssClass="" cssErrorClass=" error"/>
							<form:radiobutton path="flag_pajak" value="0" label="PTKP"/>
						</c:otherwise>
					 </c:choose>
					 <span class="description"></span>
			    </div>
		</c:otherwise>
	</c:choose>

   	<c:if test="${trans.jenistrans eq \"Penjualan\" and trans.pagename ne \"Retur\"}">
	   
   		
 		<div class="group">
	         <div class="fieldWithErrors">
	               <form:label path="karyawan.nama"  cssClass="label" cssErrorClass="label labelError">Nama Sales <a class="reffSearch" href="javascript:doAction('${path}/master/karyawan/list/3', 'Cari Sales',1000,500,false);" tabindex="-1">(Cari) </a></form:label>
	               <form:errors path="karyawan.nama" cssClass="error" />
		     </div>
	     	 <c:choose>
				<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="karyawan.nama" /><input type="text" class="text_field read" value="${trans.karyawan.nama}" readonly="readonly" size="20"/></c:when>
				<c:otherwise>
					<form:input path="karyawan.nama"  cssClass="text_field" size="20" />
				</c:otherwise>
			 </c:choose>
			   <form:hidden path="sales_id" />
				<form:hidden path="karyawan.nik" />
			 <span class="description"></span>
	    </div>
   		
	</c:if>
	<c:if test="${trans.jenistrans eq \"Pembelian\" and trans.pagename ne \"Retur\"}">
   		
  
	</c:if>
	<div class="space"></div>

	<c:choose>
		<c:when test="${trans.jenistrans eq \"Penjualan\"}">
			<h3 class="tit">Data Customer  <a class="reffSearch" href="javascript:doAction('${path}/master/customer/list', 'Cari Customer',1000,500,false);" tabindex="-1">(Cari) </a></h3>
			
		    <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="customer.kode"  cssClass="label" cssErrorClass="label labelError">Kode</form:label>
		               <form:errors path="customer.kode" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="customer.kode" /><input type="text" class="text_field read" value="${trans.customer.kode}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="customer.kode"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				  <form:hidden path="customer_id"/>
				 <span class="description"></span>
		    </div>
   			<div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="customer.nama"  cssClass="label" cssErrorClass="label labelError">Nama</form:label>
		               <form:errors path="customer.nama" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="customer.nama" /><input type="text" class="text_field read" value="${trans.customer.nama}" readonly="readonly" size="15"/></c:when>
					<c:otherwise>
						<form:input path="customer.nama"  cssClass="text_field" size="15" />
					</c:otherwise>
				 </c:choose><br/>
				 <span class="description">nama customer atau nama perusahaan</span>
		    </div>
   		
   			 <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="customer.contact"  cssClass="label" cssErrorClass="label labelError">Contact Person</form:label>
		               <form:errors path="customer.contact" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="customer.contact" /><input type="text" class="text_field read" value="${trans.customer.contact}" readonly="readonly" size="15"/></c:when>
					<c:otherwise>
						<form:input path="customer.contact"  cssClass="text_field" size="15" />
					</c:otherwise>
				 </c:choose><br/>
				 <span class="description">contact person bila customer perusahaan</span>
		    </div>
   	
		    <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="customer.alamat"  cssClass="label" cssErrorClass="label labelError">Alamat</form:label>
		               <form:errors path="customer.alamat" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="customer.alamat" /> <textarea readonly="readonly">${trans.customer.alamat }</textarea></c:when>
					<c:otherwise>
						<form:textarea path="customer.alamat"/>
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
   			 <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="customer.kota"  cssClass="label" cssErrorClass="label labelError">Kota</form:label>
		               <form:errors path="customer.kota" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="customer.kota" /><input type="text" class="text_field read" value="${trans.customer.kota}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="customer.kota"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
		    <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="customer.no_telp"  cssClass="label" cssErrorClass="label labelError">No Telepon </form:label> 
		               <form:errors path="customer.no_telp" cssClass="error" /> /
		                <form:label path="customer.no_hp"  cssClass="label" cssErrorClass="label labelError">No Handphone</form:label>
		               <form:errors path="customer.no_hp" cssClass="error" />
			     </div>
		     	<c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="customer.no_telp" /><input type="text" class="text_field read" value="${trans.customer.no_telp}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="customer.no_telp"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				  <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="customer.no_hp" /><input type="text" class="text_field read" value="${trans.customer.no_hp}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="customer.no_hp"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
   			
		    
			<div class="space"></div>
			
			<h3 class="tit"><form:checkbox id="kirimCheck" path="flag_kirim" label=" Kirim Barang" value="1"/> <a class="reffSearch" href="#" id="copyCstToKrm" title="Copy data Pengiriman dari Data Customer" tabindex="-1">(Copy)</a></h3>
			<div id="contentkirim">
			 <table  class="nostyle"  >
				  <tr>
				  		<td>
				  			<div class="group">
					         <div class="fieldWithErrors">
					               <form:label path="tgl_kirim_est"  cssClass="label" cssErrorClass="label labelError">Tanggal Estimasi Kirim</form:label>
					               <form:errors path="tgl_kirim_est" cssClass="error" />
						     </div>
					     	 <c:choose>
								<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="tgl_kirim_est" /><input type="text" class="text_field read" value='<fmt:formatDate value="${trans.createdate}" pattern="dd-MM-yyyy"/>' readonly="readonly" size="10"/></c:when>
								<c:otherwise>
									<form:input path="tgl_kirim_est"  cssClass="text_field datetimepicker"  size="20" />
								</c:otherwise>
							 </c:choose>
							 <span class="description"></span>
					    </div>
				  		</td>
				   		<td>
				   			 <div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="contact_tujuan"  cssClass="label" cssErrorClass="label labelError">Contact Person</form:label>
						               <form:errors path="contact_tujuan" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="contact_tujuan" /><input type="text" class="text_field read" value="${trans.contact_tujuan}" readonly="readonly" size="50"/></c:when>
									<c:otherwise>
										<form:input path="contact_tujuan"  cssClass="text_field" size="30" />
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
				   		<td>
						    <div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="alamat_tujuan"  cssClass="label" cssErrorClass="label labelError">Alamat Pengiriman</form:label>
						               <form:errors path="alamat_tujuan" cssClass="error" />
							     </div>
						     	 <c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="alamat_tujuan" /> <textarea readonly="readonly">${trans.alamat_tujuan}</textarea></c:when>
									<c:otherwise>
										<form:textarea path="alamat_tujuan"/>
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>
			   	   		<td>
						    <div class="group">
						         <div class="fieldWithErrors">
						               <form:label path="telp_tujuan"  cssClass="label" cssErrorClass="label labelError">No Telepon</form:label>
						               <form:errors path="telp_tujuan" cssClass="error" />
							     </div>
						     	<c:choose>
									<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="telp_tujuan" /><input type="text" class="text_field read" value="${trans.telp_tujuan}" readonly="readonly" size="30"/></c:when>
									<c:otherwise>
										<form:input path="telp_tujuan"  cssClass="text_field" size="30" />
									</c:otherwise>
								 </c:choose>
								 <span class="description"></span>
						    </div>
				   		</td>

				   	</tr>
				</table>
			</div>
		</c:when>
		<c:when test="${trans.jenistrans eq \"Pembelian\"}">
			 <h3 class="tit">Data Supplier <a class="reffSearch" href="javascript:doAction('${path}/master/supplier/list', 'Cari Supplier',1000,500,false);" tabindex="-1">(Cari) </a></h3>
			  
			
		    <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.kode"  cssClass="label" cssErrorClass="label labelError">Kode</form:label>
		               <form:errors path="supplier.kode" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.kode" /><input type="text" class="text_field read" value="${trans.supplier.kode}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="supplier.kode"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				 <form:hidden path="supplier_id"/>
				 <span class="description"></span>
		    </div>
   	
   			 <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.nama"  cssClass="label" cssErrorClass="label labelError">Nama</form:label>
		               <form:errors path="supplier.nama" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.nama" /><input type="text" class="text_field read" value="${trans.supplier.nama}" readonly="readonly" size="15"/></c:when>
					<c:otherwise>
						<form:input path="supplier.nama"  cssClass="text_field" size="15" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   	
   			 <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.contact"  cssClass="label" cssErrorClass="label labelError">Contact Person</form:label>
		               <form:errors path="supplier.contact" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.contact" /><input type="text" class="text_field read" value="${trans.supplier.contact}" readonly="readonly" size="15"/></c:when>
					<c:otherwise>
						<form:input path="supplier.contact"  cssClass="text_field" size="15" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
   			<div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.email"  cssClass="label" cssErrorClass="label labelError">Alamat Email</form:label>
		               <form:errors path="supplier.email" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.email" /><input type="text" class="text_field read" value="${trans.supplier.email}" readonly="readonly" size="15"/></c:when>
					<c:otherwise>
						<form:input path="supplier.email"  cssClass="text_field" size="15" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
		    <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.alamat"  cssClass="label" cssErrorClass="label labelError">Alamat</form:label>
		               <form:errors path="supplier.alamat" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.alamat" /> <textarea readonly="readonly">${trans.supplier.alamat }</textarea></c:when>
					<c:otherwise>
						<form:textarea path="supplier.alamat" rows="2" cols="15"/>
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
   			 <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.kota"  cssClass="label" cssErrorClass="label labelError">Kota</form:label>
		               <form:errors path="supplier.kota" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.kota" /><input type="text" class="text_field read" value="${trans.supplier.kota}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="supplier.kota"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
		    <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.no_telp"  cssClass="label" cssErrorClass="label labelError">No Telepon</form:label>
		               <form:errors path="supplier.no_telp" cssClass="error" />
			     </div>
		     	<c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.no_telp" /><input type="text" class="text_field read" value="${trans.supplier.no_telp}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="supplier.no_telp"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
   			 <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.no_hp"  cssClass="label" cssErrorClass="label labelError">No Handphone</form:label>
		               <form:errors path="supplier.no_hp" cssClass="error" />
			     </div>
		     	 <c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.no_hp" /><input type="text" class="text_field read" value="${trans.supplier.no_hp}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="supplier.no_hp"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
   		
   			 <div class="group">
		         <div class="fieldWithErrors">
		               <form:label path="supplier.no_fax"  cssClass="label" cssErrorClass="label labelError">No Fax</form:label>
		               <form:errors path="supplier.no_fax" cssClass="error" />
			     </div>
		     	<c:choose>
					<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="supplier.no_fax" /><input type="text" class="text_field read" value="${trans.supplier.no_fax}" readonly="readonly" size="10"/></c:when>
					<c:otherwise>
						<form:input path="supplier.no_fax"  cssClass="text_field" size="10" />
					</c:otherwise>
				 </c:choose>
				 <span class="description"></span>
		    </div>
		    
   			<div class="space"></div>
		</c:when>
	</c:choose>
 <div id="table_wrapper2" >
  <c:if test="${trans.mode ne 'VIEW'}">
  
	   <h3 class="tit">Daftar Produk</h3>
	  
	    <div class="group">
	        <div class="fieldWithErrors">
	             <label class="label">Kode Produk <a class="reffSearch" href="javascript:doAction('${path}/master/item/list', 'Cari Produk',1000,500,false);" tabindex="-1">(Cari) </a></label>
	        </div>
	       <input type="text" class="text_field kode" value="${trans.transDet.barcode_ext }" name="barcode_ext" id="barcode_ext"   size="12" />
	       <span class="description"></span>
	   </div>
  		
  			<div class="group">
	        <div class="fieldWithErrors">
	             <label class="label">Nama Produk</label>
	        </div>
	        	<input type="text" class="text_field" value="" name="nama_produk" id="nama_produk" size="20"/>

	       <span class="description"></span>
	   </div>
  		
  			<div class="group">
	        <div class="fieldWithErrors">
	             <label class="label">@Harga</label>
	        </div>
	        	<%--  harga editable request by TB User
	        		<c:choose>
	        		<c:when test="${trans.jenistrans eq \"Penjualan\" }">
	        			<input type="text" class="text_field read" name="harga" id="harga" readonly="readonly" size="10"/>
	        		</c:when>
	        		<c:when test="${trans.jenistrans eq \"Pembelian\" }">
	        			<input type="text" class="text_field nominal" name="harga" id="harga"  size="8"/>
	        		</c:when>
	        	</c:choose> --%>
				<input type="text" class="text_field nominal" name="harga" id="harga"  size="8"/>
	       <span class="description"></span>
	   </div>
  		
  			<div class="group">
	        <div class="fieldWithErrors">
	             <label class="label">%</label>
	        </div>
	        <input type="text" class="text_field nominal" name="diskon_persen" id="diskon_persen" size="3"/>

	       <span class="description"></span>
	   </div>
  	
  			<div class="group">
	        <div class="fieldWithErrors">
	             <label class="label">@Diskon</label>
	        </div>

				<input type="text" class="text_field nominal" name="diskon" id="diskon"  size="8"/>

	       <span class="description"></span>
	   </div>
  		
  			<div class="group">
	        <div class="fieldWithErrors">
	             <label class="label">Stock</label>
	        </div>
	        <input type="text" class="text_field read" value="" name="stock" id="stock" readonly="readonly" size="5" />
	       <span class="description"></span>
	   </div>
  		
  			<div class="group">
	        <div class="fieldWithErrors">
	             <label class="label">QTY</label>
	        </div>
	        <input type="text" class="text_field number" value="" name="qty" id="qty" size="4" />
	       <span class="description"></span>
	   </div>
  		
  		<div class="group">
	        <div class="fieldWithErrors">
	             <label class="label">Satuan</label>
	        </div>
	        <input type="text" class="text_field  read" value="" name="satuan" id="satuan" readonly="readonly" size="10" />
	       <span class="description"></span>
	   </div>
  		

		<div class="group" style="margin-top: 15px">
			<button class="button add_new" type="button" >
                  <img src="${path }/static/decorator/main/pilu/images/icons/add.gif" alt="Save" /> Add
	        </button>
	    </div>
		   		
   </c:if>
   </div>
   <div class="space"><br/></div>
   <div class="space"></div>
   
   <div id="table_wrapper"  style="height:350px;overflow-x: auto;overflow-y:none;">
   		<c:choose>
   			<c:when test="${not empty trans.lsTransDet}">
				<table width="100%" class="tbl_repeat"  >
					<thead>
					<tr>
						<th>No.</th>
						<th>Kode Produk</th>
						<th>Nama Produk</th>
						<th class="right">@Harga</th>
						<th class="right" colspan="2">@Diskon</th>
						<th class="right">Qty</th>
						<th>Satuan</th>
						<th class="right">Sub Total Harga</th>
						<th></th>
					</tr>
					</thead>
					<tbody >
					<c:choose>
						<c:when test="${trans.jenistrans eq \"Penjualan\" }">
							<c:forEach items="${trans.lsTransDet }" var="t">
								<tr>

									<td>${t.urut}<input type="hidden" name="idx" id ="idx" value="${t.urut}" title="${t.urut}"/></td>
									<td>${t.barcode_ext }<input type="hidden" name="item_id_${t.urut}" id="item_id_${t.urut}" value="${t.item_id }"  title="${t.urut}"/><input type="hidden" name="barcode_ext_${t.urut}" value="${t.barcode_ext }"  title="${t.urut}"/></td>
								 	<td>${t.item_idKet}<input type="hidden" name="nama_${t.urut}" id="nama_${t.urut}" value="${t.item_idKet}"  title="${t.urut}"/></td>
								 	<%-- <td class="right"><fmt:formatNumber pattern="#,##0">${t.harga}</fmt:formatNumber><input type="hidden" name="harga_${t.urut}" id="harga_${t.urut}" value="${t.harga}"  title="${t.urut}"/></td> --%>
								 	<td class="right"><input type="text" size=10 name="harga_${t.urut}" id="harga_${t.urut}"  value="<fmt:formatNumber pattern="###0.00">${t.harga}</fmt:formatNumber>"  class="text_field nominal"  title="${t.urut}"/>
								 	</td>
								 	<td class="right" colspan="2">
								 		<input type="text" size="4" name="persen_diskon_${t.urut}" id="persen_diskon_${t.urut}" value="<fmt:formatNumber pattern="###0.00">${t.persen_diskon}</fmt:formatNumber>"  class="text_field number"  title="${t.urut}"/>%
								 		(<input type="text" size="16" name="diskon_${t.urut}" id="diskon_${t.urut}" value="<fmt:formatNumber pattern="#,##0">${t.jumlah_diskon}</fmt:formatNumber>"  class="text_field nominal"  title="${t.urut}"/>)

								 	</td>
								 	<td class="right"><input type="text"  id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td>
								 	<td>${t.satuan_idKet}<input type="hidden" name="satuan_${t.urut}" id="satuan_${t.urut}" value="${t.satuan_idKet}"  title="${t.urut}"/></td>

								 	<td class="right" ><span id="subtotal_${t.urut}" class="subTotal"><fmt:formatNumber pattern="#,##0">${t.subTotal_harga}</fmt:formatNumber></span><input type="hidden" name="subTotal_harga_${t.urut}" id="subTotal_harga_${t.urut}" value="${t.subTotal_harga}"  title="${t.urut}"/></td>
								 	<td style="width: 30px;">
								 		<a href="#" class="remove" rel="${t.urut}">
					                      <img src="${path }/static/decorator/main/pilu/images/icons/delete.png" alt="Delete" />
					                   </a>
								 	</td>
							 	</tr>
							</c:forEach>
						</c:when>
						<c:when test="${trans.jenistrans eq \"Pembelian\" }">
							<c:forEach items="${trans.lsTransDet }" var="t">
								<tr>

									<td>${t.urut}<input type="hidden" name="idx" id ="idx" value="${t.urut}" title="${t.urut}"/></td>
									<td>${t.barcode_ext }<input type="hidden" name="item_id_${t.urut}" id="item_id_${t.urut}" value="${t.item_id }"  title="${t.urut}"/><input type="hidden" name="barcode_ext_${t.urut}" value="${t.barcode_ext }"  title="${t.urut}"/></td>
								 	<td>${t.item_idKet}<input type="hidden" name="nama_${t.urut}" id="nama_${t.urut}" value="${t.item_idKet}"  title="${t.urut}"/></td>
								 	<td class="right"><input type="text" size=10 name="harga_${t.urut}" id="harga_${t.urut}"  value="<fmt:formatNumber pattern="###0.00">${t.harga}</fmt:formatNumber>"  class="text_field nominal"  title="${t.urut}"/>
								 	</td>
								 	<td class="right" colspan="2">
								 		<input type="text" size="4" name="persen_diskon_${t.urut}" id="persen_diskon_${t.urut}" value="<fmt:formatNumber pattern="###0.00">${t.persen_diskon}</fmt:formatNumber>"  class="text_field number"  title="${t.urut}"/>%
								 		(<input type="text" size="10" name="diskon_${t.urut}" id="diskon_${t.urut}" value="<fmt:formatNumber pattern="#,##0">${t.jumlah_diskon}</fmt:formatNumber>"  class="text_field nominal"  title="${t.urut}"/>)

								 	</td>
								 	<td class="right"><input type="text"  id="qty_${t.urut}" name="qty_${t.urut}" value="${t.qty}" size="6" class="text_field number"  title="${t.urut}"/></td>
								 	<td>${t.satuan_idKet}<input type="hidden" name="satuan_${t.urut}" id="satuan_${t.urut}" value="${t.satuan_idKet}"  title="${t.urut}"/></td>

								 	<td class="right" ><span id="subtotal_${t.urut}" class="subTotal"><fmt:formatNumber pattern="#,##0">${t.subTotal_harga}</fmt:formatNumber></span><input type="hidden" name="subTotal_harga_${t.urut}" id="subTotal_harga_${t.urut}" value="${t.subTotal_harga}"  title="${t.urut}"/></td>
								 	<td>
								 		<a href="#" class="remove" rel="${t.urut}">
					                      <img src="${path }/static/decorator/main/pilu/images/icons/delete.png" alt="Delete" />
					                   </a>
								 	</td>
							 	</tr>
							</c:forEach>
						</c:when>
					</c:choose>

					</tbody>
					<tfoot>
					<tr>
						<td colspan="8" class="right subTotal">Total Harga</td>
						<td class="right"><span class="Total subTotal"><fmt:formatNumber pattern="#,##0">${trans.sub_total_harga}</fmt:formatNumber></span></td>
						<td></td>
					</tr>
					<tr>
						<td colspan="8" class="right" >Diskon Tambahan</td>
						<td  class="right" >
							<input type="text" name="persen_disc" id="persen_disc" size="4" value="<fmt:formatNumber pattern="###0.00">${trans.persen_disc}</fmt:formatNumber>"  class="text_field number" />%
							(<input type="text" name="total_disc" id="total_disc" value="<fmt:formatNumber pattern="#,##0">${trans.total_disc}</fmt:formatNumber>" size="10" class="text_field nominal" style="text-align: right;" />)
						</td>
						<td ></td>
					</tr>
					<tr>
						<td colspan="8" class="right" >PPN</td>
						<td  class="right" >
							<input type="text" name="persen_ppn" id="persen_ppn" size="4" value="<fmt:formatNumber pattern="###0.00">${trans.persen_ppn}</fmt:formatNumber>"  class="text_field number" />%
							<input type="text" name="ppn" id="ppn" value="<fmt:formatNumber pattern="#,##0">${trans.ppn}</fmt:formatNumber>" size="10" class="text_field nominal" style="text-align: right;" />
						</td>
						<td ></td>
					</tr>
					<tr>
						<th colspan="8" class="grandtotal right" >Grand Total</th>
						<th  class="grandtotal right"><span class="grandTotal"><fmt:formatNumber pattern="#,##0">${trans.total_harga}</fmt:formatNumber></span></th>
						<th></th>
					</tr>
					</tfoot>
				</table>
				
			</c:when>
   			<c:otherwise>
   				<table width="100%" class="tbl_repeat"  >
					<thead>
					<tr>
						<th>No.</th>
						<th>Kode Produk</th>
						<th>Nama Produk</th>
						<th class="right">@Harga</th>
						<th class="right" colspan="2">@Diskon</th>
						<th class="right">Qty</th>
						<th>Satuan</th>
						<th class="right">Sub Total Harga</th>
						<th></th>
					</tr>
					</thead>
					<tbody >
						<tr>
							<td colspan="10" style="height:150px;text-align: center;vertical-align: middle;">&nbsp;<strong>-- NO ITEM ADDED--</strong></td>
							
						</tr>
						
					</tbody>
					<tfoot>
					<tr>
						<td colspan="8" class="right subTotal">Total Harga</td>
						<td class="right"><span class="Total subTotal"><fmt:formatNumber pattern="#,##0">0</fmt:formatNumber></span></td>
						<td></td>
					</tr>
					<tr>
						<td colspan="8" class="right" >Diskon Tambahan</td>
						<td  class="right" >
							<input type="text" name="persen_disc" id="persen_disc" size="4" value="<fmt:formatNumber pattern="###0.00">0</fmt:formatNumber>"  class="text_field number" />%
							(<input type="text" name="total_disc" id="total_disc" value="<fmt:formatNumber pattern="#,##0">0</fmt:formatNumber>" size="10" class="text_field nominal" style="text-align: right;" />)
						</td>
						<td ></td>
					</tr>
					<tr>
						<td colspan="8" class="right" >PPN</td>
						<td  class="right" >
							<input type="text" name="persen_ppn" id="persen_ppn" size="4" value="<fmt:formatNumber pattern="###0.00">0</fmt:formatNumber>"  class="text_field number" />%
							<input type="text" name="ppn" id="ppn" value="<fmt:formatNumber pattern="#,##0">0</fmt:formatNumber>" size="10" class="text_field nominal" style="text-align: right;" />
						</td>
						<td ></td>
					</tr>
					<tr>
						<th colspan="8" class="grandtotal right" >Grand Total</th>
						<th  class="grandtotal right"><span class="grandTotal"><fmt:formatNumber pattern="#,##0">0</fmt:formatNumber></span></th>
						<th></th>
					</tr>
					</tfoot>
				</table>
				
   			</c:otherwise>
   		</c:choose>
			
			
			 
	</div>
	<div class="group">
         <div class="fieldWithErrors">
               <form:label path="ket"  cssClass="label" cssErrorClass="label labelError">Keterangan</form:label>
               <form:errors path="ket" cssClass="error" />
	     </div>
     	 <c:choose>
			<c:when test="${trans.mode eq 'VIEW'}"><form:hidden path="ket" /> <textarea readonly="readonly">${trans.ket }</textarea></c:when>
			<c:otherwise>
				<form:textarea path="ket" cols="30" rows="2"/>
			</c:otherwise>
		 </c:choose>
		 <span class="description"></span>
    </div>
   
   <div class="space"></div>
	<div class="group navform wat-cf">
		 <form:hidden path="mode"/>
         <form:hidden path="jenistrans"/>
         <form:hidden path="pagename"/>
          <form:hidden path="posisi_id"/>
           <form:hidden path="jenis"/>
	  	<c:choose>
			<c:when test="${trans.mode eq 'VIEW'}">
				<%-- <button class="button" type="button" onclick="window.location='${path}/transaksi/${trans.jenistrans}/${trans.pagename}'">
                    <img src="${path }/static/decorator/main/pilu/images/icons/back.png" alt="Back" /> Back
                  </button> --%>
			</c:when>
			<c:otherwise>
				<c:if test="${empty trans.print_faktur_date}">
					<button class="button" type="button" onclick="saveAja();">
	                    <img src="${path }/static/decorator/main/pilu/images/icons/tick.png" alt="Save" /> Save
	                </button>
                </c:if>
                <span class="text_button_padding"></span>
                <button class="button" type="button" onclick="if(confirm('Are you sure want to cancel?'))window.location='${path}/transaksi/${trans.jenistrans }/${trans.pagename }/new'">
                	<img src="${path }/static/decorator/main/pilu/images/icons/cross.png" alt="Cancel" /> Cancel
                </button>
                <c:if test="${not empty trans.no_trans}">
	                <c:choose>
	                	<c:when test="${trans.jenis eq 1 or  trans.jenis eq 3}"><!-- Pembelian -->
	                		
	                			<c:if test="${trans.posisi_id gt 0}"><!-- Order -->
	                				<button class="button" type="button" onclick="if(confirm('Are you sure want to Print Purchasing Order?'))doAction('${path}/report/print_po/${trans.no_trans }', 'Print Purchasing Order',900,500,true,true);">
					                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Purchasing Order" /> Print Purchasing Order
					                </button>
	                			</c:if>
	                			<c:if test="${trans.posisi_id eq 2 and trans.jenis eq 3}"><!-- Pembelian -->
	                				<c:choose>
		                				<c:when test="${trans.pay_mode eq 2}">
			                				 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Faktur?'))doAction('${path}/report/print_faktur/${trans.no_trans }', 'Print Faktur',900,500,true,true);">
							                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Faktur" /> Print Faktur
							                </button>
							                <c:if test="${not empty trans.print_faktur_date  and trans.posisi_id eq 2}">
							                	<button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${trans.jenistrans }/${trans.pagename }/${trans.mode }/${trans.trans_id}';">
								                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
								                </button>
							                </c:if>
						                </c:when>  
						                <c:otherwise>
						                	<button class="button" type="button" onclick="if(confirm('Are you sure want to Print Faktur?'))doAction('${path}/report/print_faktur/${trans.no_trans }', 'Print Faktur',900,500,true,true);">
							                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Faktur" /> Print Faktur
							                </button>
						                	<c:if test="${not empty trans.print_faktur_date  and trans.posisi_id eq 2}">
				                				 <button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${trans.jenistrans }/${trans.pagename }/${trans.mode }/${trans.trans_id}';">
								                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
								                </button>
							                </c:if>
						                </c:otherwise>
					                </c:choose>
	                			</c:if>
	                			
	                	</c:when>
	                	<c:when test="${trans.jenis eq 5 }"><!-- Retur Pembelian-->
	                		<button class="button" type="button" onclick="if(confirm('Are you sure want to Print Surat Retur Pembelian?'))doAction('${path}/report/print_retur_po/${trans.no_trans }', 'Print Surat Retur Pembelian',900,500,true,true);">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Surat Retur Pembelian" /> Print Surat Retur Pembelian
			                </button>
			                <c:if test="${ not empty trans.print_trans_date and trans.posisi_id eq 1}">
			                	 <button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${trans.jenistrans }/${trans.pagename }/${trans.mode }/${trans.trans_id}';">
				                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
				                </button>
		                	</c:if>
	                	</c:when>
	                	<c:when test="${trans.jenis eq 6 }"><!-- Retur Penjualan-->
	                		<button class="button" type="button" onclick="if(confirm('Are you sure want to Print Surat Retur Penjualan?'))doAction('${path}/report/print_retur_do/${trans.no_trans }', 'Print Surat Retur Penjualan',900,500,true,true);">
			                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Surat Retur Penjualan" /> Print Surat Retur Penjualan
			                </button>
			                 <c:if test="${ not empty trans.print_trans_date and trans.posisi_id eq 1}">
			                	 <button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${trans.jenistrans }/${trans.pagename }/${trans.mode }/${trans.trans_id}';">
				                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
				                </button>
		                	</c:if>
	                	</c:when>
	                	<c:when test="${trans.jenis eq 2 or  trans.jenis eq 4}"><!-- Penjualan -->

	                			<c:if test="${trans.posisi_id gt 0}"><!-- Order -->
                					<button class="button" type="button" onclick="if(confirm('Are you sure want to Print Form Order?'))doAction('${path}/report/print_form_order/${trans.no_trans }', 'Print Form Order',900,500,true,true);">
					                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Form Order" /> Print Form Order
					                </button>
	                			</c:if>
                				<c:if test="${trans.posisi_id gt 1 and trans.jenis eq 4}"><!-- Penjualan -->
	                				 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Delivery Order?'))doAction('${path}/report/print_do/${trans.no_trans }', 'Print Delivery Order',900,500,true,true);">
					                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Delivery Order" /> Print Delivery Order
					                </button>
					                <c:choose>
					                	 <c:when test="${trans.pay_mode eq 2}">
							                 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Faktur?'))doAction('${path}/report/print_faktur/${trans.no_trans }', 'Print Faktur',900,500,true,true);">
							                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Faktur" /> Print Faktur
							                </button>
							                <c:if test="${not empty trans.print_faktur_date and not empty trans.print_trans_date and trans.posisi_id eq 2}">
							                	<c:if test="${trans.customer.outoflimit eq \"true\"   and empty trans.approveby }">
							                		 <button class="button" type="button" onclick="doAction('${path}/approval?no_trans=${trans.no_trans}', ' Approval Hutang',1000,500);">
									                    <img src="${path }/static/decorator/main/pilu/images/icons/filetick.gif" alt=" Approval Hutang" /> Approval Hutang
									                </button>
							                	</c:if>
							                	 <button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${trans.jenistrans }/${trans.pagename }/${trans.mode }/${trans.trans_id}';">
								                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
								                </button>
						                	</c:if>
										</c:when>
										<c:otherwise>
											 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Faktur?'))doAction('${path}/report/print_faktur/${trans.no_trans }', 'Print Faktur',900,500,true,true);">
							                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Faktur" /> Print Faktur
							                </button>
											 <c:if test="${not empty trans.print_faktur_date and  not empty trans.print_trans_date and trans.posisi_id eq 2}">
											 	 <c:if test="${trans.customer.outoflimit eq \"true\" }">
							                		 <button class="button" type="button" onclick="doAction('${path}/approval?no_trans=${trans.no_trans}', ' Approval Hutang',1000,500);">
									                    <img src="${path }/static/decorator/main/pilu/images/icons/filetick.gif" alt=" Approval Hutang" /> Approval Hutang
									                </button>
									             </c:if>
							                	 <button class="button" type="button" onclick="if(confirm('Are you sure want to Transfer?'))window.location='${path}/transaksi/transfer/${trans.jenistrans }/${trans.pagename }/${trans.mode }/${trans.trans_id}';">
								                    <img src="${path }/static/decorator/main/pilu/images/icons/expanded.png" alt="Transfer" /> Transfer
								                </button>
						                	</c:if>
										</c:otherwise>
					                </c:choose>
					               
					                 

	                			</c:if>
	                			<c:if test="${trans.posisi_id gt 2}"><!-- Payment -->
	                				 <button class="button" type="button" onclick="if(confirm('Are you sure want to Print Purchasing Order?'))doAction('${path}/report/print_po/${trans.no_trans }', 'Print Purchasing Order',900,500,true,true);">
					                    <img src="${path }/static/decorator/main/pilu/images/icons/noteprint.gif" alt="Print Purchasing Order" /> Print Purchasing Order
					                </button>

	                			</c:if>
	                	</c:when>

	                </c:choose>

				</c:if>
			</c:otherwise>
	   </c:choose>

    </div>
	
	</form:form>
	  <div class="space"></div>
	 <c:choose>
   		<c:when test="${messageType eq 'error' or messageType eq 'warning'  or messageType eq 'done' }">
   			<p class="msg ${messageType}">${message }</p>
   		</c:when>
   		<c:otherwise>
   			<c:if test="${trans.mode ne 'VIEW'}">
    			<p class="msg info"><fmt:message key="CompleteForm" /> 
    			
    			</p>

		   </c:if>
   		</c:otherwise>
   	</c:choose>




</body>
</html>