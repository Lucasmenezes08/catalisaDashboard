function getCookie(name) {
    let nameEQ = name + "=";
    let ca = document.cookie.split(";");
    for (const element of ca) {
        let c = element;
        while (c.charAt(0) === " ") c = c.substring(1, c.length);
        if (c.indexOf(nameEQ) === 0) return c.substring(nameEQ.length, c.length);
    }
    return null;
}

function setCookie(cname, cvalue, exdays) {
    let d = new Date();
    d.setTime(d.getTime() + exdays * 24 * 60 * 60 * 1000);
    let expires = "expires=" + d.toUTCString();
    document.cookie =
        cname + "=" + cvalue + ";" + expires + ";domain=.sebrae.com.br;path=/";
}

function injetarWidgetNps(data, titulo, tema, codTema, instrumento, kc) {
    return;
    let retornoCodAtendimento = data.codAtendimento;
    let userMail = kc.tokenParsed.email;
    let userName = kc.tokenParsed.given_name;
    let fullName = kc.tokenParsed.name;
    let userCpf = kc.tokenParsed.cpf;
    let dataIni = new Date().toString();
    let birthDate = kc.tokenParsed.dataNascimento.toString();
    let url = window.location.href;
    let atendente = "Auto atendimento";
    let projeto = "Atendimento Digital";
    let acao = "Portal Sebrae - Conteúdos";
    let tipoAtendimento = "Portal Sebrae - Acesso a Conteúdos";
    let canalAtendimento = "Portal Sebrae";
    let tipoRealizacao = data.tipoRealizacao;
    let phone;
    let business = "";

    if(kc.tokenParsed.telefoneCelular != null || kc.tokenParsed.telefoneCelular != undefined){
        phone = kc.tokenParsed.telefoneCelular;
    }else{
        phone = "";
    }

    if(kc.tokenParsed.empresas){
        for( const element of kc.tokenParsed.empresas){
            if (element.isPrincipal === true){
                business = element.cnpj;
            }
        }
    }else {
        business = "";
    }
    function handleWidget(config, viewsCount) {
        if(config.exibicaoACadaPessoa && viewsCount % parseInt(config.exibicaoACadaPessoa) !== 0) {
            return;
        }
        setCookie("widgetNps", kc.tokenParsed.sub, 1);
        if (!document.getElementById("widget-nps-id")) {
            $(document.body).append(`
              <div id="widget-nps-id">
                  <link href="${config.srcCss}" rel=stylesheet>
                  <div>
                      <vue-widget widget_position="${config.posicaoWidget}"
                              widget_title="${config.tituloWidget}"
                              buttons="${config.corBotao}"
                              width="${config.tamanhoWidget}"
                              text_buttons="${config.corBotaoTexto}"
                              background="${config.corFundoWidget}"
                              texts="${config.corTexto}"
                              timeout="0"
                              width_metric="px"
                              survey_id="${config.idPesquisa}"
                              from='{"name": "${fullName}", "email": "${userMail}", "phone":"${phone}"}'
                              automatic_answer_first_question="Sim"
                              is_to_post="true"
                              metadata='{
                                  "CNPJ": \"${business}\",
                                  "DataNascimento": \"${birthDate}\",
                                  "Estado":\"${kc.tokenParsed.uf}\",
                                  "CodTema": \"${codTema}\",
                                  "Instrumento": \"${instrumento}\",
                                  "DataHoraInicioRealizacao":\"${dataIni}\",
                                  "Tema": \"${tema}\",
                                  "CodRealizacao":\"${retornoCodAtendimento}\",
                                  "NomeTratamento":\"${userName}\",
                                  "Unidade":\"NA\",
                                  "TipoRealizacao":\"${tipoRealizacao}\",
                                  "CPF":\"${userCpf}\",
                                  "Atendente":\"${atendente}\",
                                  "Projeto":\"${projeto}\",
                                  "Acao":\"${acao}\",
                                  "TipoAtendimento":\"${tipoAtendimento}\",
                                  "CanalAtendimento":\"${canalAtendimento}\",
                                  "TituloConteudo":\"${addslashes(titulo)}\",
                                  "URL":\"${url}\"
                              }'/>

                  </div>
                  <script src="${config.srcJavascript}"></script>
              </div>
          `);
        }
    }

    $.post('/sebraena-templating/controller/widgetnps/check',
        function(count) {$.getJSON("/Sebrae/Portal Sebrae/resources/json/widget-binds-config.json",
            function(config) {handleWidget(config, count)}
        )}
    )

}

function addslashes(string) {
    return string.replace(/\\/g, '\\\\').
    replace(/\u0008/g, '\\b').
    replace(/\t/g, '\\t').
    replace(/\n/g, '\\n').
    replace(/\f/g, '\\f').
    replace(/\r/g, '\\r').
    replace(/'/g, '\\\'').
    replace(/"/g, '\\"');
}

function injetarWidgetNpsByCookie(data, titulo, tema, codTema, instrumento, kc) {
    var subCookie = getCookie("widgetNps")
    if (subCookie == null || subCookie!=kc.tokenParsed.sub) {
        injetarWidgetNps(data, titulo, tema, codTema, instrumento, kc);
    }
}

