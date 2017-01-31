package com.example.app.bale;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by 13108306 on 13/09/2016.
 */
public class Pesquisa implements Serializable {
    private final int pk_pesquisa;
    private final String nomeParticipante;
    private final String nomeExaminador;
    private final String dataRealizacao;
    // Esta matriz guardará quais perguntas foram respondidas, onde cada coluna representa uma etapa da bateria
    private ArrayList<ArrayList<Boolean>> perguntasPorEtapa;
    private ArrayList<Integer> porcentagemPorEtapa;
    private int porcentagemTotal;

    public Pesquisa(int pk_pesquisa, String dataRealizacao,
                    String nomeParticipante, String nomeExaminador) {
        this.pk_pesquisa = pk_pesquisa;
        this.nomeParticipante = nomeParticipante;
        this.nomeExaminador = nomeExaminador;
        this.dataRealizacao = dataRealizacao;
        perguntasPorEtapa = new ArrayList<>();
        porcentagemPorEtapa = new ArrayList<>();
        this.porcentagemTotal = 0;
    }

    public int
    getPk_pesquisa() {
        return pk_pesquisa;
    }

    public String getNomeParticipante() {
        return nomeParticipante;
    }

    public String getNomeExaminador() {
        return nomeExaminador;
    }

    public String getDataRealizacao() {
        return dataRealizacao;
    }

    public ArrayList<ArrayList<Boolean>> getPerguntasPorEtapa() {
        return perguntasPorEtapa;
    }

    public void setPerguntasPorEtapa(ArrayList<ArrayList<Boolean>> perguntasPorEtapa) {
        this.perguntasPorEtapa = perguntasPorEtapa;
    }

    public int getPorcentagemTotal() {
        return porcentagemTotal;
    }

    public void calculaPorcentagemParcial() {
        int perguntasTotaisPorEtapa = 0;
        int perguntasRespondidasPorEtapa = 0;
        // Para cada etapa do vetor de etapas
        for (int i = 0; i < perguntasPorEtapa.size(); i++) {
            // Para cada pergunta da etapa i
            for (int j = 0; j < perguntasPorEtapa.get(i).size(); j++) {
                perguntasTotaisPorEtapa = perguntasPorEtapa.get(i).size();
                if (perguntasPorEtapa.get(i).get(j) != null)
                    perguntasRespondidasPorEtapa++;
            }
            porcentagemPorEtapa.add(i, (perguntasRespondidasPorEtapa/perguntasTotaisPorEtapa) * 100);
        }
    }

    public void calculaPorcentagemTotal() {
        calculaPorcentagemParcial();

        int novaPorcentagemTotal = 0;
        for (int porcentagem : porcentagemPorEtapa) {
            novaPorcentagemTotal = novaPorcentagemTotal + porcentagem;
        }
        novaPorcentagemTotal = novaPorcentagemTotal/porcentagemPorEtapa.size();
        porcentagemTotal = novaPorcentagemTotal;
    }

    @Override
    public String toString() {
        return nomeParticipante + "\n" + nomeExaminador + "\n" + perguntasPorEtapa + "\n" +porcentagemTotal;
    }
}