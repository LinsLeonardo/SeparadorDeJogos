package com.letscode;

import lombok.*;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class Jogo implements Comparable<Jogo>{

    private String timeMandante;
    private String timeDesafiante;
    private Integer placar1;
    private Integer placar2;
    private LocalDate data;

    @Override
    public int compareTo(Jogo o) {
        return 0;
    }

    public String formatarJogo() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return data.format(formatter) + ": " + timeMandante + " " + placar1 + " x " + placar2 + " " + timeDesafiante;
    }

    public ResultadoJogo verificarResultado(Time time){
        if ((placar1 > placar2 && timeMandante == time.getNome().intern()) || (placar1 < placar2 && timeDesafiante == time.getNome().intern()))
            return ResultadoJogo.VITORIA;
        if((placar1 == placar2) && timeMandante == time.getNome().intern() || timeDesafiante == time.getNome().intern())
            return ResultadoJogo.EMPATE;
        if ((placar1 < placar2 && timeMandante == time.getNome().intern()) || (placar1 > placar2 && timeDesafiante == time.getNome().intern()))
            return ResultadoJogo.DERROTA;

        return ResultadoJogo.NAO_INCLUIDO;
    }
}
