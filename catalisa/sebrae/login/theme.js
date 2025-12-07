$(document).ready(function () {
    $('.sidebarCollapse').on('click', function () {
        $('#sidebar, #content').toggleClass('active');
        $('.collapse.in').toggleClass('in');
        $('a[aria-expanded=true]').attr('aria-expanded', 'false');
    });
});
//$('.popover-infos').popover('show')

$(function () {
    $("[data-toggle = 'popover']").popover();
    $('.close-popover').popover('hide');
});


// menu superior


(function ($) {
    $(document).ready(function () {
        $('ul.dropdown-menu [data-toggle=dropdown]').on('click', function (event) {
            event.preventDefault();
            event.stopPropagation();
            $(this).parent().siblings().removeClass('open');
            $(this).parent().toggleClass('open');
        });
    });
})(jQuery);


// carrossel
$(function () {
    $('.carousel').carousel({
        interval: false
    });

});
// inicia os tooltips
$(function () {
    $('.tooltip-show').tooltip({trigger: 'manual'}).tooltip('show');
    $(".progress-bar").each(function(){
        each_bar_width = $(this).attr('aria-valuenow');
        $(this).width(each_bar_width + '%');
    });
});


// popovers
$(function () {
    $('[data-toggle="popover"]').popover()
});

// Busca: abre e fecha
if (typeof jQuery === "undefined") {
    throw new Error("jQuery required");
}

+function ($) {
    'use strict';

    // SEARCHBAR CLASS DEFINITION
    // =========================

    var backdrop = '.searchbar-backdrop';
    var toggle = '[data-toggle="searchbar"]';
    var Searchbar = function (element) {
        $(element).on('click.mr.searchbar', this.toggle);
    };

    Searchbar.VERSION = '1.0.0';

    Searchbar.prototype.toggle = function (e) {
        var $this = $(this);

        if ($this.is('.disabled, :disabled')) return;

        var $parent = getParent($this);
        var isActive = $parent.hasClass('open') || (typeof isXS == 'function' && isXS());

        if (!isActive) {

            clearMenus();
            if ('ontouchstart' in document.documentElement && !$parent.closest('.navbar-nav').length) {
                // if mobile we use a backdrop because click events don't delegate
                $('<div class="searchbar-backdrop"/>').insertAfter($(this)).on('click', clearMenus);
            }

            var relatedTarget = {relatedTarget: this};
            $parent.trigger(e = $.Event('show.mr.searchbar', relatedTarget));

            if (e.isDefaultPrevented()) return;
            e.preventDefault();

            $parent.find('input').trigger('focus');

            $parent
            .toggleClass('open')
            .trigger('shown.mr.searchbar', relatedTarget);

            return false;
        }
    };

    function clearMenus(e) {
        if (e && e.which === 3) return
        $(backdrop).remove();
        $(toggle).each(function () {
            var $parent = getParent($(this));
            var relatedTarget = {relatedTarget: this};
            if (!$parent.hasClass('open')) return;
            $parent.trigger(e = $.Event('hide.mr.searchbar', relatedTarget));
            if (e.isDefaultPrevented()) return;
            $parent.removeClass('open').trigger('hidden.mr.searchbar', relatedTarget);
        });
    }

    function getParent($this) {
        var selector = $this.attr('data-target');

        if (!selector) {
            return $this.parents('form');
        }

        var $parent = selector && $(selector);

        return $parent && $parent.length ? $parent : $this.parent();
    }


    // SEARCHBAR PLUGIN DEFINITION
    // ==========================

    function Plugin(option) {
        return this.each(function () {
            var $this = $(this);
            var data = $this.data('mr.searchbar');

            if (!data) $this.data('mr.searchbar', (data = new Searchbar(this)));
            if (typeof option == 'string') data[option].call($this);
        });
    }

    var old = $.fn.searchbar;

    $.fn.searchbar = Plugin;
    $.fn.searchbar.Constructor = Searchbar;


    // SEARCHBAR NO CONFLICT
    // ====================

    $.fn.searchbar.noConflict = function () {
        $.fn.searchbar = old;
        return this;
    };


    // APPLY TO STANDARD SEARCHBAR ELEMENTS
    // ===================================

    $(document)
    .on('click.mr.searchbar.data-api', clearMenus)
    .on('click.mr.searchbar.data-api', '.searchbar', function (e) {
        e.stopPropagation();
    })
    //.on('focus.mr.searchbar.data-api', toggle, Searchbar.prototype.toggle) // this causes the focus event to trigger twice
    .on('click.mr.searchbar.data-api', toggle, Searchbar.prototype.toggle);


}(jQuery);


$('.modal').on('shown', function () {
    $("body").css("overflow", "hidden");
});

$('.modal').on('hidden', function () {
    $("body").css("overflow", "visible");
});

$('body').on('hidden.bs.modal', '.modal', function () {
    $(this).removeData('bs.modal');
});

// Faz o back do browser fechar o modal
$(".modal").on("shown.bs.modal", function () { // any time a modal is shown
    var urlReplace = "#" + $(this).attr('id'); // make the hash the id of the modal shown
    history.pushState(null, null, urlReplace); // push state that hash into the url
});

// If a pushstate has previously happened and the back button is clicked, hide any modals.
$(window).on('popstate', function () {
    $(".modal").modal('hide');
});


$(document).on('click', '.navbar-theme .dropdown-menu', function (e) {
    e.stopPropagation()
});

$(document).ready(function () {
    if (window.matchMedia('(min-width: 767px)').matches) {
        $('.navigation-theme > li > .dropdown-toggle').click(function () {
            window.location = $(this).attr('href');
        });
    }
});

$('.barra-abre-navbar').on('click', function () {
    $('.navbar-collapse').collapse('hide');
});
$(".barra-abre-navbar").click(function () {
    $("body").toggleClass("nav-open")
});
// faz o bloqueio do body quando o menu esta aberto

$(".fechanavbr").click(function () {
    $("body").toggleClass("nav-open")
});


// toggle de senha
$(".toggle-password").click(function() {
    $(this).toggleClass("uil-eye uil-eye-slash");
    var input = $($(this).attr("toggle"));
    if (input.attr("type") == "password") {
        input.attr("type", "text");
    } else {
        input.attr("type", "password");
    }
});

$('#close-login-info').click(()=>$(".mensagem-senha").hide());