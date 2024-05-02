package br.com.everaldoboscatto.screenmatch.principal;
import br.com.everaldoboscatto.screenmatch.model.DadosSerie;
import br.com.everaldoboscatto.screenmatch.model.DadosTemporada;
import br.com.everaldoboscatto.screenmatch.service.ConsumoAPI;
import br.com.everaldoboscatto.screenmatch.service.ConverterDados;
import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.util.*;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI(); // Instanciar o ConsumoAPI
    private ConverterDados conversor = new ConverterDados(); // Instanciar o conversor
    private final String ENDERECO = "https://www.omdbapi.com/?t="; // Constante
    private final String API_KEY = "&apikey=2f196da8"; // Constante

    public void exibeMenu() {
        var menu = """       
                0 - Sair                
                1 - Buscar séries
                2 - Buscar episódios                               
                """;

        System.out.println(menu);
        System.out.println("Opção: ");
        var opcao = leitura.nextInt();
        leitura.nextLine();
        switch (opcao) {
            case 1:
                buscarSerieWeb();
                break;
            case 2:
                buscarEpisodioPorSerie();
                break;
            case 0:
                System.out.println("Encerrando sistema...");
                break;
            default:
                System.out.println("Opçao inválida!");
        }
    }
    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        System.out.println("\nImprimindo dados da série:\n" + dados);
    }
    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série que de desja buscar:");
        var nomeSerie = leitura.nextLine();
        // Armazenar dados da série no formato Json
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        // Converter dados da série de Json para a classe Java
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        DadosSerie dadosSerie = getDadosSerie();

        // Criar lista de temporadas da série
        List<DadosTemporada> listaTemporadas = new ArrayList<>();

        // Percorrer total temporadas
        for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {

            // Receber dados no formato Json
            var json = consumo.obterDados(ENDERECO + dadosSerie.titulo().replace(" ", "+") +
                    "&season=" + i + API_KEY);

            // Converter dados Json para a classe classe java
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

            // Adicionar temporada na lista
            listaTemporadas.add(dadosTemporada);
        }
        // Imprimir lsita de temporadas
        System.out.println("\nImprimindo Temporadas:");
        listaTemporadas.forEach(System.out::println); // (::) = método de referência
    }
}