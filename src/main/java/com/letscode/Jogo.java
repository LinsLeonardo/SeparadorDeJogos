package com.letscode;


import lombok.*;

import java.time.LocalDate;

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
}
