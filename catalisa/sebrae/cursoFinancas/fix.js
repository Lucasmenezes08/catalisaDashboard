// $('.ctd-artigo__main__content__text img').each(function(index, item){
// 	var originalPath = $(item).attr('src');
// 	var decodedPath = decodeURIComponent($(item).attr('src'));
// 	var actualURL = window.location.host;
// 	var httpsURL = $('#urlHttps').val();
// 	var itemSrcInit;
// 
// 	while(originalPath != decodedPath){
// 		console.log('decoding ' + originalPath + ' to  ' + decodedPath);
// 		$(item).attr('src', decodedPath);
// 		originalPath = decodedPath;
// 		decodedPath = decodeURIComponent($(item).attr('src'));
// 	}
// 
// 	itemSrcInit = $(item).attr('src');
// 
// 	if(actualURL.startsWith("m.")){
// 		$(item).attr('src', httpsURL + itemSrcInit);
// 	}
// })
// 
// $('.ctd-artigo__main__content__text a').each(function(index, item){
// 	var href = $(item).attr('href');
// 	if(href != null && href.indexOf('vgn_ext_templ_rewrite') !== -1){
// 		var index = href.indexOf("vgnextoid=") + 10;
// 		var vcmId = href.substring(index, index + 40);
// 		$.ajax({
//   		url: '/sebraena-templating/controller/util/linkBuilder/' + vcmId,
//   		crossDomain: true,
//   		success: function (data) {
//   			var json = JSON.parse(data);
// 				$(item).attr('href', json.link);
//   		}
//   	});
// 	}
// })

$('.sb-social-media__container .lnk').click(function (e) {
	e.preventDefault();

	var $temp = $("<form id='sbCopyTextLink'><input></form>");
	$($temp).insertBefore('.sb-social-media__container .lnk');
	$('#sbCopyTextLink input').val(document.URL).select();
	document.execCommand("copy");
	$('#sbCopyTextLink').remove();
	$(this).addClass('active');
	setTimeout(function () {
		$('.sb-social-media__container .lnk').removeClass('active');
	}, 2000);
});


$(".sb-home-conteudos__carrossel__item__img").each(function(){
    var item = jQuery(this);
    var x = item.css('background-image').replace(/^url\(['"](.+)['"]\)/, '$1');
      var tester=new Image();
      tester.onerror=imageNotFound;
      tester.src=x;

    function imageNotFound() {
      item.removeAttr("style");
    }
});

//$(".sb-components-button").hide();


if(location.href.indexOf("conteudo.") == -1 && location.href.endsWith("/ufs/ap?codUf=3")) {
   
}