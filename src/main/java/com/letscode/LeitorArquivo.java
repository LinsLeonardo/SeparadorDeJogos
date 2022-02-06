package com.letscode;

import com.opencsv.CSVWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;

public class LeitorArquivo {

    public static final String ARQUIVO = "santander811matchesResult.csv";

    Set<Jogo> jogos = new TreeSet<>(Comparator.comparing(Jogo::getData).thenComparing(Jogo::getTimeMandante)
            .thenComparing(Jogo::getTimeDesafiante));

    public void leitorArquivo() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(ARQUIVO, UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] detalhes = line.split(";");

                if(detalhes[0].intern() != "time_1(mandante)"){
                    Jogo jogo = new Jogo(detalhes[0], detalhes[1], Integer.parseInt(detalhes[2]),
                            Integer.parseInt(detalhes[3]), LocalDate.parse(detalhes[4]));
                    jogos.add(jogo);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, List<Jogo>> timesAgrupados = jogos.stream().collect(Collectors.groupingBy(Jogo::getTimeMandante));

        List<Time> listaResultados = new ArrayList();

        for (String time : timesAgrupados.keySet()) {

            System.out.println(time);

            Time timeAtual = new Time(time, 0, 0,0,0);

            FileWriter arq = new FileWriter(time + ".txt");
            PrintWriter gravarArq = new PrintWriter(arq);

            gravarArq.println(time);
            gravarArq.println();

            for (Jogo jogo : jogos) {
                ResultadoJogo resultado = jogo.verificarResultado(jogo);


                if (jogo.getTimeDesafiante().intern() != jogo.getTimeMandante().intern() && (jogo.getTimeMandante().intern() == time.intern() || jogo.getTimeDesafiante().intern() == time.intern())) {
                  //if (resultado != ResultadoJogo.NAO_INCLUIDO) {
                    gravarArq.println(jogo.formatarJogo());

                    if(resultado == ResultadoJogo.EMPATE) {
                        timeAtual.setEmpates(timeAtual.getEmpates() + 1);
                        timeAtual.setPontos(timeAtual.getPontos() + 1);
                    }
                    if(resultado == ResultadoJogo.VITORIA) {
                        timeAtual.setVitorias(timeAtual.getVitorias() + 1);
                        timeAtual.setPontos(timeAtual.getPontos() + 3);
                    }
                    if(resultado == ResultadoJogo.DERROTA){
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
