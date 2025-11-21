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
                "excelente", "maravilhoso", "maravilhosa", "perfeito", "perfeita",
                "sensacional", "incrível", "incrivel", "fantástico", "fantastica",
                "amei", "ameiii", "show de bola", "impressionante"
        );

        // POSITIVO (+1)
        adicionarPalavras(1,
                "bom", "boa", "gostei", "curti", "legal",
                "satisfeito", "satisfeita", "ok", "bacana", "funciona bem",
                "atendeu", "recomendo", "positivo"
        );

        // NEUTRO (0)

        // NEGATIVO (-1)
        adicionarPalavras(-1,
                "ruim", "fraco", "decepcionado", "decepcionada",
                "não gostei", "nao gostei", "podia ser melhor",
                "mais ou menos", "regular", "mediano"
        );

        // MUITO NEGATIVO (-2)
        adicionarPalavras(-2,
                "péssimo", "pessimo", "horrível", "horrivel",
                "lixo", "odiei", "terrível", "terrivel",
                "pior", "nunca mais", "detestei"
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
