package com.letscode;

import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.CollationElementIterator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LeitorArquivo {

    public static final String ARQUIVO = "CollectionsClubs.csv";
    public static String PATH;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    Set<Jogo> jogos = new TreeSet<>(Comparator.comparing(Jogo::getData).thenComparing(Jogo::getTimeMandante)
            .thenComparing(Jogo::getTimeDesafiante));
//    ArrayList<Jogo>  jogos = new ArrayList<>();


    public void leitorArquivo() throws IOException {
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
        } catch (IOException e) {
            e.printStackTrace();
        }

//        for (Jogo jogo : jogos){
//            if(jogo.getTimeDesafiante().intern() != jogo.getTimeMandante().intern() && (jogo.getTimeMandante().toLowerCase().intern() == "sport" || jogo.getTimeDesafiante().toLowerCase().intern() == "sport")) {
//                jogo.formatarJogo();
//            }
//        }

        Map<String, List<Jogo>> timesAgrupados = jogos.stream().collect(Collectors.groupingBy(Jogo::getTimeMandante));

//        System.out.println(timesAgrupados.values());
        List<Time> listaResultados = new ArrayList();

        for (String time : timesAgrupados.keySet()) {

            System.out.println(time);

            Time timeAtual = new Time(time, 0, 0,0,0);

            FileWriter arq = new FileWriter(time + ".txt");
            PrintWriter gravarArq = new PrintWriter(arq);

            gravarArq.println(time);
            gravarArq.println();

            for (Jogo jogo : jogos) {

                if (jogo.getTimeDesafiante().intern() != jogo.getTimeMandante().intern() && (jogo.getTimeMandante().intern() == time.intern() || jogo.getTimeDesafiante().intern() == time.intern())) {
                    gravarArq.println(jogo.formatarJogo());

                    if(jogo.getPlacar1() == jogo.getPlacar2()) {
                        timeAtual.setEmpates(timeAtual.getEmpates() + 1);
                        timeAtual.setPontos(timeAtual.getPontos() + 1);
                    } else if((jogo.getPlacar1() > jogo.getPlacar2() && jogo.getTimeMandante().intern() == time.intern()) || (jogo.getPlacar1() < jogo.getPlacar2() && jogo.getTimeDesafiante().intern() == time.intern())) {
                        timeAtual.setVitorias(timeAtual.getVitorias() + 1);
                        timeAtual.setPontos(timeAtual.getPontos() + 3);
                    } else {
                        timeAtual.setDerrotas(timeAtual.getDerrotas() + 1);
                    }
                }


            }
            arq.close();
            System.out.println("Arquivo criado do " + time + " com sucesso");
            System.out.println("-------------------------------------------");

            listaResultados.add(timeAtual);
        }

        Collections.sort(listaResultados);



        Writer tabelaCampeonato = Files.newBufferedWriter(Paths.get("tabela-do-campeonato.csv"));
        CSVWriter escreverCSV = new CSVWriter(tabelaCampeonato);
        escreverCSV.writeNext(new String[]{"Time;Vitórias;Empates;Derrotas;Pontos"});
        for (Time time : listaResultados) {
            escreverCSV.writeNext(time.formatarResultados());
        }
        escreverCSV.flush();
        tabelaCampeonato.close();
        System.out.println("Arquivo criado da Tabela do campeonato com sucesso");
        System.out.println("-------------------------------------------");
    }
}
