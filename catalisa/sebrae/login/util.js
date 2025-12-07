const buscarCEP = async (event) => {
    limpaEndereco();
    let cep = $("#cep").val();
    if (cep !== null) {
        if (cep.length === 10) {
            cep = cep.replace(/\D/g, "");
            let url = "https://viacep.com.br/ws/" + cep + "/json/";
            try{
                let resposta = await axios.get(url);
                    if(!resposta.data.erro){
                        $('#cidade').val(resposta.data.localidade !== undefined ? resposta.data.localidade : null);
                        $('#uf').val(resposta.data.uf !== undefined ? resposta.data.uf : null);
                        $('#bairro').val(resposta.data.bairro !== undefined ? resposta.data.bairro : null);
                        $('#logradouro').val(resposta.data.logradouro !== undefined ? resposta.data.logradouro : null);
                        $('html,body').animate({scrollTop: $('#kc-form-buttons').offset().top},'slow');
                    }else{
                        // alert('CEP não encontrado!');
                        // $("#cep").val(null);
                    }
            }catch(error){
                console.log(error)
            }
        }else{
            limpaEndereco();
        }
    }
}

const limpaEndereco = () => {
    $("#cidade").val(null);
    $("#uf").val(null);
    $("#bairro").val(null);
    $("#logradouro").val(null);
}

isCPFValido = async (cpf, dataNascimento, token) => {
    if (cpf == null){
        return { valido: false, mensagem: 'Informe o CPF'};
    }

    let data;
    let message = '';
    const url = window.gestaoAmei;
    if (!url) {
        return { valido: false, mensagem: 'Serviço de verificação do CPF não informado'};
    }
    try{
        const retorno = await axios.get(url + '/public/v1/users/check', {
            headers: {
                'x-recaptcha-token': token
            },
            params: {
                cpf: cpf.replace(".", "").replace(".", "").replace("-", ""),
                dataNascimento,
                nomeMae: 'null'
            }
        });
        data = retorno.data;
    } catch(e) {
        data = e.response.data;
        message = data?.message;
        if (Array.isArray(data)) {
            message = data
            .map((datum) => datum?.message)
            .join('\n');
        }
    }
    if (message === 'CPF não encontrado!') {
        message = 'Seu CPF não foi encontrado em nossas bases, favor abrir chamado no <a href="https://sacnacional.sebrae.com.br/">Suporte ao Usuário</a> do nosso portal.';
    }
    return {valido: data?.isValido ?? false, 'mensagem': message ?? 'Um erro inexperado aconteceu ao validar o CPF com a data de nascimento.'}
}
