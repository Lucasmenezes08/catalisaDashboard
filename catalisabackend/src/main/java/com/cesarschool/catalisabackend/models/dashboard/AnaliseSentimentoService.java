package com.cesarschool.catalisabackend.models.dashboard;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AnaliseSentimentoService {

    private final Map<String, Integer> lexico = new HashMap<>();

    public AnaliseSentimentoService() {
        // MUITO POSITIVO (+2)
        adicionarPalavras(2,
                "excelente", "excelentes",
                "maravilhoso", "maravilhosa", "maravilhosos", "maravilhosas",
                "perfeito", "perfeita", "perfeitos", "perfeitas",
                "sensacional", "sensacionais",
                "incrível", "incrivel", "incríveis", "incriveis",
                "fantástico", "fantastica", "fantásticos", "fantasticas",
                "espetacular", "espetaculares",
                "fenomenal", "fenomenais",
                "extraordinário", "extraordinaria", "extraordinarios", "extraordinarias",
                "impecável", "impecavel", "impecáveis", "impecaveis",
                "brilhante", "brilhantes",
                "maravilha",
                "incrível demais", "incrivel demais",
                "sensacional demais",
                "perfeito demais",
                "amei", "ameiii", "amei demais",
                "amei muito",
                "show", "show de bola",
                "top", "top demais", "topzera",
                "nota 10", "nota dez",
                "de primeira",
                "de altíssima qualidade", "de altissima qualidade",
                "sem defeitos",
                "sem palavras",
                "acima das expectativas",
                "superou as expectativas",
                "funciona perfeitamente",
                "funciona perfeitamente bem",
                "atendimento incrível", "atendimento incrivel",
                "experiência incrível", "experiencia incrivel",
                "excepcional", "excepcionais",
                "magnífico", "magnifico", "magnífica", "magnifica",
                "surpreendente", "surpreendentes",
                "soberbo",
                "fora de série", "fora da curva",
                "monstruoso",
                "impressionante",
                "épico", "epico",
                "divino", "divina",
                "obra-prima", "obra prima",
                "sensacional de verdade",
                "excelente custo benefício", "excelente custo beneficio",
                "perfeito do começo ao fim",
                "maravilhoso demais",
                "brabo demais"

        );

        // POSITIVO (+1)
        adicionarPalavras(1,
                "bom", "boa", "bons", "boas",
                "gostei", "curti", "curti bastante",
                "legal", "bem legal",
                "massa", "bacana",
                "satisfeito", "satisfeita",
                "ok",
                "funciona bem",
                "funciona direitinho",
                "funcionou bem",
                "atendeu", "atendeu bem",
                "atendeu às expectativas", "atendeu as expectativas",
                "recomendo", "recomendo sim",
                "vale a pena",
                "vale o preço", "vale o preco",
                "vale o investimento",
                "bom custo benefício", "bom custo beneficio",
                "ótimo custo benefício", "otimo custo beneficio",
                "custo-benefício bom", "custo beneficio bom",
                "positivo",
                "agradável", "agradavel",
                "agradou",
                "tudo certo",
                "tudo ok",
                "funciona como esperado",
                "cumpre o que promete",
                "cumpre o prometido",
                "produto bom",
                "boa qualidade",
                "serviço bom", "servico bom",
                "atendimento bom",
                "satisfatório", "satisfatorio",
                "bacaninha",
                "legalzinho",
                "agradável demais", "agradavel demais",
                "corresponde ao anunciado",
                "compatível com a descrição", "compativel com a descricao",
                "funcionamento aceitável",
                "recomendo bastante",
                "bem satisfatório", "bem satisfatorio",
                "preço justo", "preco justo",
                "gostei do atendimento",
                "voltaria a comprar",
                "ótima experiência", "otima experiencia"

        );

        // NEUTRO (0)

        // NEGATIVO (-1)
        adicionarPalavras(-1,
                "ruim", "ruins",
                "fraco", "fraca", "fraquinho", "fraquinha",
                "decepcionado", "decepcionada",
                "decepcionante",
                "não gostei", "nao gostei",
                "não gostei muito", "nao gostei muito",
                "podia ser melhor",
                "poderia ser melhor",
                "mais ou menos",
                "regular",
                "mediano",
                "esperava mais",
                "esperava bem mais",
                "aquém do esperado", "aquem do esperado",
                "não recomendo", "nao recomendo",
                "não vale a pena", "nao vale a pena",
                "apenas ok",
                "só ok", "so ok",
                "nada demais",
                "funciona mal",
                "funcionou mal",
                "mal acabado", "mal-acabado",
                "veio com defeito",
                "com defeito",
                "atendimento ruim",
                "suporte ruim",
                "frustrado", "frustrada",
                "insatisfeito", "insatisfeita",
                "pouco satisfeito", "pouco satisfecha", "pouco satisfeita",
                "não gostei do atendimento", "nao gostei do atendimento",
                "qualidade ruim",
                "bem fraco",
                "abaixo da média", "abaixo da media",
                "não funcionou como esperado", "nao funcionou como esperado",
                "produto simples demais",
                "me decepcionou um pouco",
                "serviço fraco", "servico fraco",
                "compatibilidade ruim",
                "funciona, mas mal",
                "podia melhorar bastante",
                "não vale o preço", "nao vale o preco",
                "experiência fraca", "experiencia fraca",
                "qualidade inferior"

        );

        // MUITO NEGATIVO (-2)
        adicionarPalavras(-2,
                "péssimo", "pessimo", "péssima", "pessima",
                "horrível", "horrivel",
                "lixo", "um lixo", "lixo total",
                "odiei",
                "terrível", "terrivel",
                "pior",
                "pior compra",
                "pior experiência", "pior experiencia",
                "pior de todas", "pior de todos",
                "nunca mais",
                "detestei",
                "muito ruim",
                "muito ruim mesmo",
                "não presta", "nao presta",
                "não funciona", "nao funciona",
                "defeituoso", "defeituosa",
                "quebrado", "quebrada",
                "chegou quebrado", "chegou quebrada",
                "chegou errado", "veio errado",
                "péssimo atendimento", "pessimo atendimento",
                "péssimo produto", "pessimo produto",
                "péssima qualidade", "pessima qualidade",
                "experiência péssima", "experiencia pessima",
                "experiência horrível", "experiencia horrivel",
                "arrepedido", "arrependida",
                "me arrependi",
                "dinheiro jogado fora",
                "joguei dinheiro fora",
                "roubo",
                "enganação", "enganacao",
                "propaganda enganosa",
                "odioso", "odiosa",
                "insuportável", "insuportavel",
                "trágico", "tragico",
                "catástrofe", "catastrofe",
                "zero estrelas", "uma estrela",
                "não comprem", "nao comprem",
                "vontade de devolver",
                "quero devolver",
                "horrível atendimento", "horrivel atendimento",
                "péssimo demais", "pessimo demais",
                "horrível demais", "horrivel demais",
                "detestei muito",
                "odiei demais",
                "qualidade horrenda",
                "deplorável", "deploravel",
                "repugnante",
                "nojento", "nojenta",
                "lixo absoluto",
                "desastre total",
                "fracasso total",
                "completamente inútil", "completamente inutil",
                "dinheiro perdido",
                "produto ridículo", "produto ridiculo",
                "experiência traumatizante", "experiencia traumatizante",
                "imprestável", "imprestavel",
                "não funciona de jeito nenhum", "nao funciona de jeito nenhum",
                "chegou destruído", "chegou destruido"
        );
    }

    private void adicionarPalavras(int peso, String... palavras) {
        for (String p : palavras) {
            lexico.put(p.toLowerCase(), peso);
        }
    }

    public Sentimento classificar(String texto) {
        if (texto == null || texto.isBlank()) {
            return Sentimento.NEUTRO;
        }

        String lower = texto.toLowerCase();
        lower = lower.replaceAll("[.,;:!?()\"']", " ");

        String[] tokens = lower.split("\\s+");

        int scoreTotal = 0;
        int qtdPalavrasComPeso = 0;

        for (String token : tokens) {
            if (token.isBlank()) continue;

            Integer peso = lexico.get(token);
            if (peso != null) {
                scoreTotal += peso;
                qtdPalavrasComPeso++;
            }
        }

        if (qtdPalavrasComPeso == 0) {
            return Sentimento.NEUTRO;
        }

        double media = (double) scoreTotal / qtdPalavrasComPeso;
        return mapearMediaParaEnum(media);
    }

    private Sentimento mapearMediaParaEnum(double media) {
        if (media >= 1.5) {
            return Sentimento.MUITO_POSITIVO;
        } else if (media >= 0.3) {
            return Sentimento.POSITIVO;
        } else if (media > -0.3) {
            return Sentimento.NEUTRO;
        } else if (media > -1.5) {
            return Sentimento.NEGATIVO;
        } else {
            return Sentimento.MUITO_NEGATIVO;
        }
    }
}
