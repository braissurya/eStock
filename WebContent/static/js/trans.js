$(document).ready(function(){
			//pesan error diletakkan di alert juga
			var message = $("#message").val().replace(/<br\s*[\/]?>/gi, "\n");
			var path = $("#path").val();
			if(message != ''){
			 	alert(message);
			}
			
			
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
       			
		       	if("persen_disc"==triger){
		       	 
       				persen_diskon=parseFloat(replaceComma($('#persen_disc').val()));			       				
       				diskon=(grandTotal*persen_diskon)/100;       				
       				$('#total_disc').val(formatNumber(diskon));
       			}else if("total_disc"==triger){
       				diskon=parseFloat(replaceComma($('#total_disc').val()));
       				persen_diskon=(diskon/grandTotal)*100;
       				$('#persen_disc').val(formatCurrency(persen_diskon));
       			}else{
       				persen_diskon=parseFloat(replaceComma($('#persen_disc').val()));			       				
       				diskon=(grandTotal*persen_diskon)/100;       				
       				$('#total_disc').val(formatNumber(diskon));
       			}
       			
   				$('.grandTotal').text(formatNumber(grandTotal-diskon));
			}
			$("#table_wrapper").delegate('input:text', 'keyup', function(e){
					
				  syncPrice($(this).attr("id"));
		    }); 
		    
		    
		    $("#table_wrapper").delegate('input:text.nominal', 'blur', function(){	 
				  $(this).formatCurrency({ colorize: true, negativeFormat: '-%s%n', roundToDecimalPlace: 0});
		    });
			
			
			function addRowProduct(type){
				if($('#barcode_ext').val()=='') alert('Kode Produk Belum diisi');
				else if($('#qty').val()=='') alert('QTY Belum diisi');
				else if($('#nama_produk').val()=='') alert('Nama Produk Belum diisi');
				else{
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
									$(data[i].value).hide().fadeIn(300)
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
								$(data[i].value).hide().fadeIn(300)
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
					alert(path);
					$.getJSON(path+'/json/completeItemPrice?barcode_ext='+$(this).val(), function(data) {
						if(data!=""){
							$('#nama_produk').val(data[0].nama);
							if($('input[name=flag_ecer]:checked').val()==1){
								$('#harga').val(formatCurrency(data[0].harga_ecer));
								$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga_ecer)*100));
							}else {
								$('#harga').val(formatCurrency(data[0].harga));
								$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga)*100));
							}
							$('#diskon').val(formatCurrency(data[0].diskon));
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
							if($('input[name=flag_ecer]:checked').val()==1){
								$('#harga').val(formatCurrency(data[0].harga_ecer));
								$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga_ecer)*100));
							}else {
								$('#harga').val(formatCurrency(data[0].harga));
								$('#diskon_persen').val(formatCurrency((data[0].diskon/data[0].harga)*100));
							}
							$('#diskon').val(formatCurrency(data[0].diskon));
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
			
			$('#barcode_ext,#nama_produk,#qty').keypress(function(e) {
			  if (e.keyCode == '13') {
			  		 e.preventDefault();
			   	  addRowProduct($(this));
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
					if($('input[name=flag_ecer]:checked').val()==1){
						$('#harga').val(formatCurrency(ui.item.harga_ecer));
						$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga_ecer)*100));
					}else {
						$('#harga').val(formatCurrency(ui.item.harga));
						$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga)*100));
					}
					$('#diskon').val(formatCurrency(ui.item.diskon));
					$('#stock').val(ui.item.stock_jual);
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
					if($('input[name=flag_ecer]:checked').val()==1){
						$('#harga').val(formatCurrency(ui.item.harga_ecer));
						$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga_ecer)*100));
					}else {
						$('#harga').val(formatCurrency(ui.item.harga));
						$('#diskon_persen').val(formatCurrency((ui.item.diskon/ui.item.harga)*100));
					}
					$('#diskon').val(formatCurrency(ui.item.diskon));
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
								nama : item.nama
							};
						})); 
					}
				});	
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#karyawan\\.nama').val(ui.item.nama);
					
				}
			});
			
			 $('#karyawan\\.nik').live('keyup', function(e) {
				
				if($(this).val().length>2){
					
					$.getJSON(path+'/json/completeKaryawan?nik='+$(this).val(), function(data) {
						if(data!=""){
							$('#karyawan\\.nama').val(data[0].nama);
							
						}else{
							$('#karyawan\\.nama').val("");
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
							
							
						}else{
							$('#karyawan\\.nik').val("");
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
								nik: item.nik								
							};
						})); 
					}
				});	
				},
				minLength: 2,
				select: function( event, ui ) {
					$('#karyawan\\.nik').val(ui.item.nik);
					
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
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.kode,
								value: item.kode,
								nama : item.nama,
								contact : item.contact,
								alamat : item.alamat,
								kota : item.kota,
								telp : item.telp,
								no_hp : item.no_hp
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
					$('#customer\\.telp').val(ui.item.telp);
					$('#customer\\.no_hp').val(ui.item.no_hp);
					var nasabah="\nKode : "+ui.item.value+
								"\nNama : "+ui.item.nama+
								"\nContact Person : "+ui.item.contact+
								"\nAlamat : "+ui.item.alamat+
								"\nKota : "+ui.item.kota+
								"\nNo Telpon : "+ui.item.telp+
								"\nNo HP : "+ui.item.no_hp;
					if(confirm("Apakah data customer ingin dicopy?\n"+nasabah)==false){						
						$('#customer\\.nama').val("");
						$('#customer\\.contact').val("");
						$('#customer\\.alamat').val("");
						$('#customer\\.kota').val("");
						$('#customer\\.telp').val("");
						$('#customer\\.no_hp').val("");
						
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
							$('#customer\\.telp').val(data[0].telp);
							$('#customer\\.no_hp').val(data[0].no_hp);
							var nasabah="\nKode : "+data[0].kode+
								"\nNama : "+data[0].nama+
								"\nContact Person : "+data[0].contact+
								"\nAlamat : "+data[0].alamat+
								"\nKota : "+data[0].kota+
								"\nNo Telpon : "+data[0].telp+
								"\nNo HP : "+data[0].no_hp;
							if(confirm("Apakah data customer ingin dicopy?\n"+nasabah)==false){								
								$('#customer\\.nama').val("");
								$('#customer\\.contact').val("");
								$('#customer\\.alamat').val("");
								$('#customer\\.kota').val("");
								$('#customer\\.telp').val("");
								$('#customer\\.no_hp').val("");
							}else{
								$(".kode").focus();
							}
						}else{
							$('#customer\\.nama').val("");
							$('#customer\\.contact').val("");
							$('#customer\\.alamat').val("");
							$('#customer\\.kota').val("");
							$('#customer\\.telp').val("");
							$('#customer\\.no_hp').val("");
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
							$('#customer\\.telp').val(data[0].telp);
							$('#customer\\.no_hp').val(data[0].no_hp);
							var nasabah="\nKode : "+data[0].kode+
								"\nNama : "+data[0].nama+
								"\nContact Person : "+data[0].contact+
								"\nAlamat : "+data[0].alamat+
								"\nKota : "+data[0].kota+
								"\nNo Telpon : "+data[0].telp+
								"\nNo HP : "+data[0].no_hp;
							if(confirm("Apakah data customer ingin dicopy?\n"+nasabah)==false){
								$('#customer\\.kode').val("");
								$('#customer\\.contact').val("");
								$('#customer\\.alamat').val("");
								$('#customer\\.kota').val("");
								$('#customer\\.telp').val("");
								$('#customer\\.no_hp').val("");
							}else{
								$(".kode").focus();
							}	
						}else{
							$('#customer\\.kode').val("");
							$('#customer\\.contact').val("");
							$('#customer\\.alamat').val("");
							$('#customer\\.kota').val("");
							$('#customer\\.telp').val("");
							$('#customer\\.no_hp').val("");
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
						//alert(data[0].nama);
						 response( $.map( data, function( item ) {
							return {
								label: item.nama,
								value: item.nama,
								kode: item.kode,
								contact : item.contact,
								alamat : item.alamat,
								kota : item.kota,
								telp : item.telp,
								no_hp : item.no_hp
								
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
					$('#customer\\.telp').val(ui.item.telp);
					$('#customer\\.no_hp').val(ui.item.no_hp);
					var nasabah="\nKode : "+ui.item.kode+
								"\nNama : "+ui.item.value+
								"\nContact Person : "+ui.item.contact+
								"\nAlamat : "+ui.item.alamat+
								"\nKota : "+ui.item.kota+
								"\nNo Telpon : "+ui.item.telp+
								"\nNo HP : "+ui.item.no_hp;
					if(confirm("Apakah data customer ingin dicopy?\n"+nasabah)== false){
						$('#customer\\.kode').val("");
						$('#customer\\.contact').val("");
						$('#customer\\.alamat').val("");
						$('#customer\\.kota').val("");
						$('#customer\\.telp').val("");
						$('#customer\\.no_hp').val("");
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
								email : item.email
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
								no_fax : item.no_fax
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
					}else{
						$(".kode").focus();
					}
			
				}
			}); 
			
			$("#copyCstToKrm").click( function(){
				$("#alamat_tujuan").val($("#customer\\.alamat").val());
				$("#contact_tujuan").val($("#customer\\.contact").val());
				$("#telp_tujuan").val($("#customer\\.telp").val());
				return false;
			});
		});	