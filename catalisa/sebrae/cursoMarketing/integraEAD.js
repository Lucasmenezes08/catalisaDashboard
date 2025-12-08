$(document).ready(function() {
	loadBannerPublicitario();
	loadUrlSubscription();
	
	$('.sb-common-section__light-gray').addClass('sb-grid');
});

function loadBannerPublicitario(){
	$.ajax({
        url: '/sebraena-templating/controller/banners-publicitarios/find-banners',
        method: 'GET',
        success: function (data) {
            $('#banner-publicitario').html(data);
			$('.sb-common-mini-banner__mobile__close').click(function() {
				$('.sb-common-mini-banner__mobile__close').parent().remove();
			  })
        },
        error: function (e, x, error) {
            console.log('erro ao carregar banner publicitário: '+error);
        }
    });
}

function loadUrlSubscription(){
	var nomeGuid = $('#nomeGuid').val();
	
	var urlSucesso = '/sites/PortalSebrae/cursosonline/sucesso?ead='+nomeGuid;
	
	verificaUsuarioLogado(function(user) {
        if(user != null){
			$('#btnSubscription').attr('href', urlSucesso);
        }else{
			$('#btnSubscription').attr('href', '/sites/PortalSebrae/login?urlAncora='+urlSucesso);
		}
	});
}