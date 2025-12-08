/**
 * Copyright (c) 2007-2013 Ariel Flesler - aflesler<a>gmail<d>com | http://flesler.blogspot.com
 * Dual licensed under MIT and GPL.
 * @author Ariel Flesler
 * @version 1.4.6
 */
(function(f) {
  "use strict";
  "function" === typeof define && define.amd
    ? define(["jquery"], f)
    : "undefined" !== typeof module && module.exports
      ? (module.exports = f(require("jquery")))
      : f(jQuery);
})(function($) {
  "use strict";
  function n(a) {
    return (
      !a.nodeName ||
      -1 !==
        $.inArray(a.nodeName.toLowerCase(), [
          "iframe",
          "#document",
          "html",
          "body"
        ])
    );
  }
  function h(a) {
    return $.isFunction(a) || $.isPlainObject(a) ? a : { top: a, left: a };
  }
  var p = ($.scrollTo = function(a, d, b) {
    return $(window).scrollTo(a, d, b);
  });
  p.defaults = { axis: "xy", duration: 0, limit: !0 };
  $.fn.scrollTo = function(a, d, b) {
    "object" === typeof d && ((b = d), (d = 0));
    "function" === typeof b && (b = { onAfter: b });
    "max" === a && (a = 9e9);
    b = $.extend({}, p.defaults, b);
    d = d || b.duration;
    var u = b.queue && 1 < b.axis.length;
    u && (d /= 2);
    b.offset = h(b.offset);
    b.over = h(b.over);
    return this.each(function() {
      function k(a) {
        var k = $.extend({}, b, {
          queue: !0,
          duration: d,
          complete:
            a &&
            function() {
              a.call(q, e, b);
            }
        });
        r.animate(f, k);
      }
      if (null !== a) {
        var l = n(this),
          q = l ? this.contentWindow || window : this,
          r = $(q),
          e = a,
          f = {},
          t;
        switch (typeof e) {
          case "number":
          case "string":
            if (/^([+-]=?)?\d+(\.\d+)?(px|%)?$/.test(e)) {
              e = h(e);
              break;
            }
            e = l ? $(e) : $(e, q);
          case "object":
            if (e.length === 0) return;
            if (e.is || e.style) t = (e = $(e)).offset();
        }
        var v = ($.isFunction(b.offset) && b.offset(q, e)) || b.offset;
        $.each(b.axis.split(""), function(a, c) {
          var d = "x" === c ? "Left" : "Top",
            m = d.toLowerCase(),
            g = "scroll" + d,
            h = r[g](),
            n = p.max(q, c);
          t
            ? ((f[g] = t[m] + (l ? 0 : h - r.offset()[m])),
              b.margin &&
                ((f[g] -= parseInt(e.css("margin" + d), 10) || 0),
                (f[g] -= parseInt(e.css("border" + d + "Width"), 10) || 0)),
              (f[g] += v[m] || 0),
              b.over[m] &&
                (f[g] += e["x" === c ? "width" : "height"]() * b.over[m]))
            : ((d = e[m]),
              (f[g] =
                d.slice && "%" === d.slice(-1)
                  ? (parseFloat(d) / 100) * n
                  : d));
          b.limit &&
            /^\d+$/.test(f[g]) &&
            (f[g] = 0 >= f[g] ? 0 : Math.min(f[g], n));
          !a &&
            1 < b.axis.length &&
            (h === f[g] ? (f = {}) : u && (k(b.onAfterFirst), (f = {})));
        });
        k(b.onAfter);
      }
    });
  };
  p.max = function(a, d) {
    var b = "x" === d ? "Width" : "Height",
      h = "scroll" + b;
    if (!n(a)) return a[h] - $(a)[b.toLowerCase()]();
    var b = "client" + b,
      k = a.ownerDocument || a.document,
      l = k.documentElement,
      k = k.body;
    return Math.max(l[h], k[h]) - Math.min(l[b], k[b]);
  };
  $.Tween.propHooks.scrollLeft = $.Tween.propHooks.scrollTop = {
    get: function(a) {
      return $(a.elem)[a.prop]();
    },
    set: function(a) {
      var d = this.get(a);
      if (a.options.interrupt && a._last && a._last !== d)
        return $(a.elem).stop();
      var b = Math.round(a.now);
      d !== b && ($(a.elem)[a.prop](b), (a._last = this.get(a)));
    }
  };
  return p;
});

$(function() {
	// ---------  Inscrição no evento -------------- //
	function realizarInscricao() {
		$('#modal-login').show();
	}
	
	$(document).ready(function (){
		
		// modal-login
	    $('#modal-login #cpf').focus(function(){
	        $(this).removeClass('sb-form-error');
	        $('.modal-login-erro').removeClass('active');
	    });
	    $('#modal-login #password').focus(function(){
	        $(this).removeClass('sb-form-error');
	        $('.modal-login-erro').removeClass('active');
	    });

	    // enter tela de login
	    $(document).keypress(function(e) {
	        if ($('#modal-login').hasClass('active') && (e.keycode == 13 || e.which == 13)) {
	            // alert("Enter is pressed");
	            logar();
	        }
	    });
	    // add mascara campo cpf
	    $("#cpf").attr("onkeyup","mascaraTexto(event,'999.999.999-99');validarTextoNumero(this,'num');").attr("onkeypress","BloquearLetras(event)").attr("maxlength","14");

	    // ajusta cards
	  cutTextCardPortfolio('.vt__card--main'); 
	  calcHeightCardPortfolio('.listCardsResponsive, #portfolio-cards-grid');
	});

	// ---------------- Mascara campo texto

	function mascaraTexto(assunto, mascara){
	    var campo, valor, i, tam, caracter;

	    if (document.all) // Internet Explorer
	        campo = assunto.srcElement;
	    else // Nestcape, Mozzila
	        campo= assunto.target;

	    valor = campo.value;
	    tam = valor.length;

	    for(i=0;i<mascara.length;i++){
	        caracter = mascara.charAt(i);
	        if(caracter!="9")
	            if(i<tam & caracter!=valor.charAt(i))
	                campo.value = valor.substring(0,i) + caracter + valor.substring(i,tam);

	    }

	}

	//------------------------ Bloqueia letras e caracteres especiais -----------------//

	function validarTextoNumero(dom,tipo){
	    switch(tipo){
	        case'num':var regex=/[A-Za-z]/g;break;
	        case'text':var regex=/\d/g;break;
	    }
	    dom.value=dom.value.replace(regex,'');
	}

	function BloquearLetras(e){
	    if(window.e){
	        var tecla = window.e.keyCode;
	        if (!((tecla>=48 && tecla<=57) || (tecla==8) || (tecla==0))) {
	            window.e.keyCode=0;
	        }
	    }else{
	        var tecla = e.which
	        var ctrl=e.ctrlKey;
	        if (!((tecla>=48 && tecla<=57) || (tecla==8) || (tecla==0) || (ctrl && tecla==118) || (ctrl && tecla==99)))
	        {
	            e.preventDefault();
	        }
	    }
	}

	// -------- REGRAS DE LOGIN --------

	function logar() {
	    $("#msgErro").text('').removeClass("active");
	    var erro = true;
	    if($('#modal-login #cpf').val() == '') {
	        $('#modal-login #cpf').addClass('sb-form-error');
	        erro = false;
	    }
	    if($('#modal-login #password').val() == '') {
	        $('#modal-login #password').addClass('sb-form-error');
	        erro = false;
	    }
	    if(erro) {
	    	var urlLogin = $('#urlHttps').val();
	        $.ajax({
	            url: urlLogin + 'appportal/formulario-cadastro.do',
	            dataType: 'jsonp',
	            crossDomain: true,
	            jsonp: false,
	            jsonpCallback: 'jsonRetorno',
	            timeout: 60000,
	            async: false,
	            type: 'POST',
	            cache: false,
	            data: {
	                metodo: 'entrar',
	                'pessoa.cpf': $('#cpf').val(),
	                'pessoa.senha': $('#password').val()
	            },
	            success: function (data) {
	                if (data != null && data.indexOf('LOGIN_VALIDO') != -1) {
	                	criaCookie('user', cpf, true);
	                    
	                	var url = $('#confirmarInscricaoUrl').val();
	        	        window.location = new_url;
	                	
	                } else if (data == null || data == '') {
//	                    limparCookies();
	                    $("#msgErro").text('Sistema temporariamente indisponível. Por favor, tente novamente em instantes.').addClass('active');
	                } else if (data.indexOf('CPF_INVALIDO') != -1) {
//	                    limparCookies();
	                    $("#msgErro").text('CPF inválido.').addClass('active');
	                } else {
//	                    limparCookies();
	                    $("#msgErro").text('Senha incorreta.').addClass('active');
	                }
	            },
	            complete: function() {

	            }
	        });
	    }
	}
	
	function criaCookie(chave, value, is_expira) {
	    var expira = new Date();
	    expira.setHours(expira.getHours() + 24); //expira dentro de 24h
	    var a = '';
	    if (is_expira) {
	        a = ';expires=' + expira.toUTCString();
	    }
	    document.cookie = chave + '=' + value + '; path=/' + a;
	}

	function lerCookie(chave) {
	    var ChaveValor = document.cookie.match('(^|;) ?' + chave + '=([^;]*)(;|$)');
	    return ChaveValor ? ChaveValor[2] : null;
	}
	
  // adicionando classe last Ã  Ãºltima section
  $("article.main-content section:first-of-type").addClass("first");
  $("article.main-content section:last-of-type").addClass("last");

  //
  // var _hash = window.location.hash.substring(1);
  // if($.type(_hash) !== "undefined"){
  // 		hero_nav_navigate("#"+_hash);
  // }

  // NAVEGAÃ‡ÃƒO HERO  MENU
  $(".main-content .hero-nav a").click(function(e) {
    e.preventDefault();
    var index = $(this).attr("href");
    hero_nav_navigate(index);
  });

  $("#main-content-front .hero-nav a").click(function() {
    var index = $(this).attr("href");
    hero_nav_navigate(index);
  });

  function hero_nav_navigate(index) {
    $("html, body").scrollTo($(".main-content").find(index), 400, {
      offset: { top: -80 }
    });
  }

  // NAVEGAÃ‡ÃƒO ENTRE OS TÃ“PICOS
  /* 1. pega o h3 prÃ³ximo ou anterior e mostra no label */
  $("ul.section-nav li a.next").each(function() {
    var next = $(this)
      .closest("section")
      .next()
      .find("h3 span")
      .text(); /* procura o prÃ³ximo h3 */
    $(this)
      .parent()
      .find("b")
      .html(next); /* mostra o conteÃºdo na label */
  });

  $("ul.section-nav li a.prev").each(function() {
    var prev = $(this)
      .closest("section")
      .prev()
      .find("h3 span")
      .text(); /* idem */
    $(this)
      .parent()
      .find("b")
      .html(prev); /* ibidem */
  });

  /* 2. navega entre os tÃ³picos com o scrollTo */
  $("ul.section-nav li a.next").click(function(e) {
    e.preventDefault();
    $("html, body").scrollTo($(this).attr("href"), 400, {
      offset: { top: -80 }
    });
  });

  $("ul.section-nav li a.prev").click(function(e) {
    e.preventDefault();
    $("html, body").scrollTo($(this).attr("href"), 400, {
      offset: { top: -80 }
    });
  });

  // TABS //
  $(".tabs li a").click(function(e) {
    e.preventDefault();
    var tab = $(this).attr("href");
    if ($(this).hasClass("selected")) {
      return false;
    } else {
      $(".tabs li a").removeClass("selected");
      $("body")
        .find(".tab")
        .hide();
      $("body")
        .find("." + tab)
        .fadeIn();
      $(".no-touch .form-select:visible").msDropDown();
      $(this).addClass("selected");
    }
  });

  // TABS //
  $(".tabs_new li").click(function(e) {
    e.preventDefault();
    var tab = $("a", this).attr("href");
    if ($(this).hasClass("selected")) {
      return false;
    } else {
      $(".tabs_new li").removeClass("selected");
      $("body")
        .find(".tab")
        .hide();
      $("body")
        .find("." + tab)
        .fadeIn();
      $(".no-touch .form-select:visible").msDropDown();
      $(this).addClass("selected");
    }
  });

  // FILTER //
  $(".filter li a").click(function(e) {
    e.preventDefault();
    var el = $(this).attr("href");
    if ($(this).hasClass("selected")) {
      return false;
    } else {
      $(".filter li a").removeClass("selected");
      $(".filter-content")
        .find(".filter-item")
        .hide();
      $(".filter-content")
        .find("." + el)
        .fadeIn("slow");
      $(this).addClass("selected");
    }
  });

  // ZEBRA //
  $("ul.static-list li:odd").addClass("odd");

  // ACCORDION //
  /*$('.accordion-link').click(function(e) {
		e.preventDefault();

		//se nÃ£o quiserem fechar os outros itens ao clicar, podemos remover essas duas funcoes abaixo
		$(this).closest('.accordion-wrap').siblings().find('.accordion-link').removeClass('active');
		$(this).closest('.accordion-wrap').siblings().find('.accordion-box').slideUp(200);

		$(this).toggleClass('active');
		$(this).closest('.accordion-wrap').find('.accordion-box').slideToggle(200);
	});*/

  //  Share - abrir modal
  $(".link-share").click(function(e) {
    $(".overlay").css({
      background: "url(img/transparencia2.png)"
    });
    e.preventDefault();
  });

  //  Login - abrir modal
  $(".link-bookmarks").click(function(e) {
        var element = $(this).find("a");
        getFavoritoComCallback($(element).attr('codigo'),
        function(){
            removeFavoritoComCallback($(element).attr('codigo'), function(){
                $(element).css('background-image', '');
                $(element).html('Adicionar aos <strong>Favoritos</strong>');
            })
        },
        function(){
            favoritarComCallback($(element).attr('codigo'), function(){
                $(element).css('background-image', 'url("/sebraena-templating/files/img/ico/star-favorited.png")');
                $(element).html('<strong>Favorito</strong>');
            })
        });
  });

  // conteÃºdo foi Ãºtil?
  $(".poll-feedback #voltar-pergunta").click(function(event) {
    $(this)
      .closest(".poll-feedback")
      .slideUp(200);
    $(this)
      .closest(".poll-feedback")
      .siblings(".poll")
      .slideDown(200);
    e.preventDefault();
    /* Act on the event */
  });
  // $('#conteudo-util-sim').click(function(e) {
  // 	e.preventDefault();
  // 	var parent = $(this).parents('ul');
  // 	$('li a', parent).removeClass('ativo');
  // 	$(this).addClass('ativo');
  // 	// $(this).closest('.poll').slideUp(200);
  // 	// $(this).closest('.poll').siblings('.poll-feedback').slideDown(200);
  // });
  $("#conteudo-util-nao").click(function(e) {
    e.preventDefault();
    $(this)
      .closest(".poll")
      .slideUp(200);
    $(this)
      .closest(".poll")
      .siblings(".poll-feedback")
      .slideDown(200);
  });
  $("#conteudo-util-sim").click(function(e) {
    e.preventDefault();
    $(this)
      .closest(".poll")
      .slideUp(100);
    $(this)
      .closest(".poll")
      .siblings(".poll-success")
      .slideDown(200);
  });
  $('.poll-feedback input[type="button"], .poll-feedback button').click(
    function(e) {
      e.preventDefault();
      $(this)
        .closest(".poll-feedback")
        .slideUp(200);
      $(this)
        .closest(".poll-feedback")
        .siblings(".poll-success")
        .slideDown(200);
    }
  );
  $(".ico-print-info").click(function() {
    window.print();
  });

  $("#base-zoom").click(function() {
    // arquivo pdf
    var ancora = $(".single-pdf")
      .attr("data")
      .split("#")[1];
    var link = $(".single-pdf")
      .attr("data")
      .split("#")[0];
    var valorZomm = parseInt(ancora.split("=")[1]);
    // valor do input range
    var valorscroll = $("#base-zoom").val();
    $(".single-pdf").attr("data", link + "#zoom=" + valorscroll);
  });

  function zoom_html() {
    // zoom for range
    $(".icon.zoom input.cropit-image-zoom-input").change(function() {
      var valurange = $(this).val();
      var zoom_ = $(this).val();
      $(".pack-control span.value").text(valurange);
      // o zoom deve vim do span sempre que ele for mudado
      $("#conteiner").css({
        transform: "scale(" + zoom_ + ")"
      });
    });
    $(".embedbar_html .html5 .ico-lupa-less").click(function() {
      var inputvalue = parseFloat(
        $(".icon.zoom input.cropit-image-zoom-input").val()
      );
      var lessinput = inputvalue - 0.2;
      $(".icon.zoom input.cropit-image-zoom-input")
        .val(lessinput)
        .change();
    });
    $(".embedbar_html .html5 .ico-lupa-more").click(function() {
      var inputvalue = parseFloat(
        $(".icon.zoom input.cropit-image-zoom-input").val()
      );
      var moreinput = inputvalue + 0.2;
      $(".icon.zoom input.cropit-image-zoom-input")
        .val(moreinput)
        .change();
    });
  }
  zoom_html();

  //////////////////////
  // infografico Jpg
  //////////////////////
  // zoom for input range

  $(function zoomInfografico() {
    if ($.isFunction($.fn.iviewer)) {
      // id function
      var infografico_JPG = $("#img_wrapper").iviewer({});
      var oldvalue = parseInt(
        $(".icon.zoom input.cropit-image-zoom-input").val()
      );
      // zoom for range
      $(".icon.zoom input.cropit-image-zoom-input").change(function() {
        var valueinputrange = $(this).val();
        infografico_JPG.iviewer("set_zoom", valueinputrange);
      });
      // click less zoom
      $(".ico-lupa-less").click(function() {
        infografico_JPG.iviewer("zoom_by", -1);
        oldvalue = parseInt(
          $(".icon.zoom input.cropit-image-zoom-input").val()
        );
        $(".icon.zoom input.cropit-image-zoom-input")
          .val(oldvalue - 10)
          .change();
      });
      // click less more
      $(".ico-lupa-more").click(function() {
        infografico_JPG.iviewer("zoom_by", -1);
        oldvalue = parseInt(
          $(".icon.zoom input.cropit-image-zoom-input").val()
        );
        $(".icon.zoom input.cropit-image-zoom-input")
          .val(oldvalue + 10)
          .change();
      });
    }
  });

  /////// Simples Tabs
  function simplesTabs() {
    $(".simples_content-tabs a[data-tabs]").on("click", function(event) {
      var id_tabs = $(this).attr("data-tabs");
      var parents_tabs = $(this).parents(".simples_content-tabs");
      var find_tabs = parents_tabs.find("._tabs");
      $(find_tabs).removeClass("active");
      $(".simples_content-tabs a[data-tabs]").removeClass("active");
      $(this).addClass("active");
      $("#" + id_tabs).addClass("active");
      event.preventDefault();
      /* Act on the event */
    });
  }
  simplesTabs();

  $("[data-scroll-tabs]").on("click", function() {
    var simplesTabs_id = $(this).data("scroll-tabs");
    $(".simples_content-tabs > .active").removeClass("active");
    $(".simples_content-tabs ._buttons a").removeClass("active");
    $(
      ".simples_content-tabs ._buttons [data-tabs=" + simplesTabs_id + "]"
    ).addClass("active");
    $(".simples_content-tabs ._tabs#" + simplesTabs_id + "").addClass("active");
  });

  /////// Simples Tabs box de a-z
  function tabs_az_() {
    $("._tabs .box-az button").on("click", function(event) {
      var id_button = $(this).attr("data-id");
      var _parents_tabs = $(this).parents(".box-az");
      var _find_tabs = _parents_tabs.find("._content");
      $("._tabs .box-az button").removeClass("active");
      $("._content .active").removeClass("active");
      $("._content #" + id_button).addClass("active");
      $(this).addClass("active");
      event.preventDefault();
    });
  }
  tabs_az_();

  ///////// separar colunas de acordo com a quantidade de opÃ§Ã£o A-Z
  $(".box-az .content__tabs ul").each(function() {
    var count_ = $(this).find("li").length;
    if (count_ <= 10) {
      $(this).addClass("collumn-one");
    } else if (count_ < 30) {
      $(this).addClass("collumn-two");
    } else {
      $(this).addClass("collumn-tree");
    }
  });
  /////// change result busca
  $("#_search__example_links a").on("click", function(event) {
    var value_search = $(this).text();
    var value_parents = $(this).parents("._search");
    var value_parents_find = value_parents.find("._search-input input");
    value_parents_find.val(value_search);
    var _box = $(this).parents(".component_ideia-de-negocio");
    search_article(_box);
    change_qnt_result();
  });
  $("._search ._search-input input").keyup(function() {
    var _box = $(this).parents(".component_ideia-de-negocio");
    search_article(_box);
    change_qnt_result();
  });
  $("._search ._search-input .clear").on("click", function(event) {
    var _box = $(this).parents(".component_ideia-de-negocio");
    $("._search-input input", _box).val("");
    search_article(_box);
  });

  function search_article(_box) {
    var _searchinput = $("._search-input", _box);
    var _tabs = $(".simples_content-tabs", _box);
    var _results = $(".simples_content-search-results", _box);
    var _load = $(".simples_content-load", _box);
    var _qnt_input = $("input", _searchinput).val().length;
    $(".simples_content-search-no-results", _box).hide();
    if (_qnt_input >= 1) {
      _searchinput.addClass("_find");
      _tabs.hide();
      _results.hide();
      _load.show();
      get_search_article($("input", _searchinput).val(), _results, _load);
    } else {
      _searchinput.removeClass("_find");
      _results.hide();
      _load.hide();
      _tabs.show();
    }
  }

  function get_search_article(value, _results, _load, _box) {
    var url = "_webservice/search_article.php";
    $.post(
      url,
      {
        search: value
      },
      function(data) {
        var count = 0;
        var _html = "<ul>";
        $.each(data.result, function(index, value) {
          count++;
          _html += "<li>";
          _html += '<a href="#this">' + value + "</a>";
          _html += "</li>";
        });
        _html += "</ul>";
        if (count > 0) {
          $("._content", _results).html(_html);
          _results.show();
          change_qnt_result(count, _box);
        } else {
          $(".simples_content-search-no-results", _box).show();
        }
        _load.hide();
      }
    ),
      "json";
  }
  function change_qnt_result(count, _box) {
    var _count_li = $(".simples_content-search-results ._content ul");
    var _value_input = $("._search ._search-input input", _box).val();
    $(
      ".simples_content-search-results ._content_result > strong , .simples_content-search-no-results p span"
    ).html(_value_input);
    $(".simples_content-search-results ._content_result > span").html(count);
    if (count < 10) {
      _count_li.addClass("collumn-one");
    } else if (count < 30) {
      _count_li.addClass("collumn-two");
    } else {
      _count_li.addClass("collumn-tree");
    }
  }
  // resize tamanho do box de acordo com o tamanho da tela - box_size - ideias de negocio
  function aside_box_height() {
    var height_window = $(window).outerHeight(true); // window
    var height_header = calc_height("header#header"); // header
    var height_box_list_links = calc_height("._box_list_links"); // box list
    var height_ul_box_aside = calc_height("._box-size ul");
    var height_box_aside_boxwhite = calc_height("._box_white"); // box white
    var height_box_aside_back = calc_height(".back-page-box-shadow"); // box white
    var calc_box_list = height_box_list_links - height_ul_box_aside;
    // Resize calc
    var resul_calc =
      height_window -
      (height_header +
        height_box_aside_back +
        height_box_aside_boxwhite +
        calc_box_list) -
      60;
    $("._box-size ul").css("max-height", resul_calc + "px");
  }
  aside_box_height();

  function calc_height(obj) {
    var margin = 0;
    var padding = 0;
    obj = $(obj);
    margin =
      parseInt(obj.css("margin-top")) + parseInt(obj.css("margin-bottom"));
    padding =
      parseInt(obj.css("padding-top")) + parseInt(obj.css("padding-bottom"));
    return obj.height() + margin + padding;
  }

  /*-----------------------------------------*/
  /*--- vtPortfolio__aside toggle filter ---*/
  /*----------------------------------------*/
  $(document).on('click', '#button-more-filter', function(){
	    var item = $(this);
	    var item_parent = item.parents(".vtPortfolio__aside");
	    $(item).toggleClass("active");
	    if (item.hasClass("active")) {
	      item_parent.addClass("show");
	    } else {
	      item_parent.removeClass("show");
	    }
	  });
  /*-----------------------------------------*/
  /*--- vtPortfolio vtBoxLocais ---*/
  /*----------------------------------------*/

  $("#vtBoxLocais-cidades").change(function() {
    reset_vtBoxLocais();
    var cidade_selected = $(this).val();
    if (cidade_selected !== "Selecione") {
      open_unidade(cidade_selected);
    } else {
      reset_vtBoxLocais();
    }
  });

  $("#vtBoxLocais .select-unidade .form-select").on("change", function() {
    if ($(this).val() === "Selecione") {
      close_vtContent();
      return;
    }
    open_vtContent();
  });

  function open_unidade(local) {
    var box = $("#vtBoxLocais");
    var box_descr = box.find(".select-unidade em");
    $(".vtBox__locais--middle", box).slideDown();
    $(".vtBox__locais--verificar", box).slideDown();
    setTimeout(() => {
      box_descr.html(local);
    }, 400);
  }
  function reset_vtBoxLocais() {
    var box = $("#vtBoxLocais");
    $(".vtBox__locais--middle", box).slideUp();
    $(".vtBox__locais--verificar", box).hide();
    $(".vtBox__locais--footer", box).hide();
    $(".content", box).hide();
    $(".select-unidade .form-select", box)
      .val("Selecione")
      .change();
    $(".select-unidade ._msddli_.selected")
      .removeClass("selected")
      .parent()
      .children("._msddli_:first")
      .addClass("selected");
  }
  function open_vtContent() {
    var box = $("#vtBoxLocais");
    $(".content", box).slideDown();
    $(".vtBox__locais--verificar", box).hide();
    $(".vtBox__locais--footer", box).show();
  }

  function close_vtContent() {
    var box = $("#vtBoxLocais");
    $(".content", box).slideUp();
    $(".vtBox__locais--verificar", box).show();
    $(".vtBox__locais--footer", box).hide();
  }
  
//corta a descrição dos cards
  function cutTextCardPortfolio(cards) {
    setTimeout(function(){  
      // var hgtCard = 0;
      $(cards).each(function(){
        var txtDesc = $(this).find('p').text().trim();
        if(txtDesc.length > 99) {
          txtDesc = txtDesc.substring(99, 0);
          txtDesc = txtDesc+'...';
          lastWord = txtDesc.split(' ').pop();
          txtDesc = txtDesc.replace(lastWord, '...');
          $(this).find('p').text(txtDesc);
        }
        // var newHgtCard = $(this).find('p').height();
        // if(newHgtCard > hgtCard) {
        //   hgtCard = newHgtCard;
        // }
      });
      // $(cards).find('p').height(hgtCard);
    }, 50);
  }
  
//calcula a altura dos cards para igualar em altura
  function calcHeightCardPortfolio(elmt) {
    setTimeout(function(){  
      $(elmt).each(function(){
        var hgtCard = 0;
        $(this).find('.vt__card').each(function(){
          var newHgtCard = $(this).height();
          if(newHgtCard > hgtCard) {
            hgtCard = newHgtCard;
          }
        });
        $(this).find('.vt__card').height(hgtCard);
      });
    }, 100);
  }
  
});
