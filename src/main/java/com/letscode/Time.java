package com.letscode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Time implements Comparable<Time> {
    private String nome;
    private int vitorias;
    private int derrotas;
    private int empates;
    private int pontos;

    public String[] formatarResultados() {
        return new String[]{nome + ";" + vitorias + ";" + empates + ";" + derrotas + ";" + pontos};
    }

    @Override
    public int compareTo(Time t) {

        if (this.pontos > t.getPontos()) {
            return -1;
        } if (this.pontos < t.getPontos()) {
            return 1;
        }

        if(this.vitorias > t.getVitorias()) {
            return -1;
        } if(this.vitorias < t.getVitorias()) {
            return 1;
        }
        return 0;
    }
}


