package com.letscode;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LeitorArquivo {

    public static final String ARQUIVO = "CollectionsClubs.CSV";
    public static String PATH;
    DateTimeFormatter  formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    Set<Jogo> jogos = new TreeSet<>(Comparator.comparing(Jogo::getData).thenComparing(Jogo::getTimeMandante)
            .thenComparing(Jogo::getTimeDesafiante));
//    ArrayList<Jogo>  jogos = new ArrayList<>();


    public void leitorArquivo(){
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ARQUIVO, UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] detalhes = line.split(";");


                Jogo jogo = new Jogo(detalhes[0], detalhes[1], Integer.parseInt(detalhes[2]),
                        Integer.parseInt(detalhes[3]), LocalDate.parse(detalhes[4], formatter));
                jogos.add(jogo);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException  e) {
            e.printStackTrace();
        }

//        for (Jogo jogo : jogos){
//            System.out.println(jogo);
//        }

        Map<String, List<Jogo>> timesAgrupados = jogos.stream().collect(Collectors.groupingBy(Jogo::getTimeMandante));

//        System.out.println(timesAgrupados);

        for(String time : timesAgrupados.keySet()){
//            System.out.println(time);
//            System.out.println();
            System.out.println(timesAgrupados.values());

        }
//        timesAgrupados.forEach(time -> System.out.println(time), return);
//        Map<String, Set<Jogo>> agrupamentoStream = jogos.stream().collect(groupingBy(Jogo::getTimeMandante));
    }
}
