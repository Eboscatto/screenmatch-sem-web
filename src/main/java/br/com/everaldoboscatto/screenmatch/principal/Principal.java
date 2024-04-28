package br.com.everaldoboscatto.screenmatch.principal;
import br.com.everaldoboscatto.screenmatch.model.DadosSerie;
import br.com.everaldoboscatto.screenmatch.model.DadosTemporada;
import br.com.everaldoboscatto.screenmatch.service.ConsumoAPI;
import br.com.everaldoboscatto.screenmatch.service.ConverterDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI(); // Instanciar o ConsumoAPI
    private ConverterDados conversor = new ConverterDados(); // Instanciar o conversor
    private final String ENDERECO = "https://www.omdbapi.com/?t="; // Constante
    private  final String API_KEY = "&apikey=2f196da8"; // Constante
    public void exibeMenu() {
        System.out.println("Digite o nome da série que de desja buscar:");
        var nomeSerie = leitura.nextLine();
        // Armazenar dados da série no formato Json
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + API_KEY);
        // Converter dados da série de Json para a classe Java
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        // Criar lista de temporadas da série
        List<DadosTemporada> listaTemporadas = new ArrayList<>();

        // Percorrer total temporadas
        for (int i = 1; i <= dados.totalTemporadas(); i++) {

            // Receber dados no formato Json
            json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + "&season=" + i + API_KEY);

            // Converter dados Json para a classe classe java
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

            // Adicionar temporada na lista
            listaTemporadas.add(dadosTemporada);
        }
        // Imprimir lsita de temporadas
        listaTemporadas.forEach(System.out::println);
    }
}
