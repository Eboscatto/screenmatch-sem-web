package br.com.everaldoboscatto.screenmatch.principal;
import br.com.everaldoboscatto.screenmatch.model.DadosEpisodio;
import br.com.everaldoboscatto.screenmatch.model.DadosSerie;
import br.com.everaldoboscatto.screenmatch.model.DadosTemporada;
import br.com.everaldoboscatto.screenmatch.model.Episodio;
import br.com.everaldoboscatto.screenmatch.service.ConsumoAPI;
import br.com.everaldoboscatto.screenmatch.service.ConverterDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        System.out.println("\nImprimindo dados da série:\n" + dados);

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
        System.out.println("\nImprimindo Temporadas:");
        listaTemporadas.forEach(System.out::println); // (::) = método de referência

        // Buscar somente os títulos dos espisódios

        /*
        for (int i = 0; i < dados.totalTemporadas(); i++) {
            List<DadosEpisodio> episodiosTemporda = listaTemporadas.get(i).episodios();
            for (int j = 0; j < episodiosTemporda.size(); j++) {
                System.out.println(episodiosTemporda.get(j).titulo());
            }
        }
         */

        // Refatorando o código acima utilizando funções anônimas,
        System.out.println("\n Imprimindo Titulos dos espisódios:");
        listaTemporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo()))); // t = temporada e = espisodio

        // Buscar os espisódios top 5
        List<DadosEpisodio> dadosEpisodios = listaTemporadas.stream()
                        .flatMap(t -> t.episodios().stream())
                                .collect(Collectors.toList());

         System.out.println("\nEspisódios Top 5:");
        System.out.println("Etapas do processamento:");
         dadosEpisodios.stream()
                // Buscar o episódio que tem avaliação e ignorar os não avaliados
        .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))

                // Utilizando a função peek() para visualizar as etapas do processamento

        .peek(e -> System.out.println("Primeiro filtro(N/A!)" + e))
                // Ordenar em ordem
        .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
        .peek(e -> System.out.println("Ordenação1"))
                // Limitar aos cinco primeiros
        .limit(5)
        .peek(e -> System.out.println("Limite!"))
        .map(e -> e.titulo().toUpperCase())
        .peek(e -> System.out.println("Mapemamento!"))
        .forEach(System.out::println);


        // Buscar somente os dados dos episódios
        List<Episodio> episodios = listaTemporadas.stream()
                .flatMap(t -> t.episodios().stream()
                // Para cada dadosEpisodio criar um novo Episodio
                .map(d -> new Episodio(t.numeroTemporada(), d))
                ).collect(Collectors.toList());
        episodios.forEach(System.out::println); // Fazer um toString() na classe

           /*

        System.out.println("Digite um trecho do título do episódio");
        var trechoTitulo = leitura.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTituloEpisodio().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episódio encontrado!");
            System.out.println("Temporada: " + episodioBuscado.get().getNumeroTemporada());
        } else {
            System.out.println("Episódio não encontrado!");
        }

        // Buscar episódio por data de lançamento
        // A primeira coisa a fazer é imprimir todos os episódios e adicionar a pergunta
        // "A partir de que ano você deseja ver os episódios?".

        System.out.println("A partir de que ano você deseja ver os episódios? ");
        var ano = leitura.nextInt();
        // Lembre-se que, toda vez que usamos nextInt(), em seguida devemos usar nextLine(). Caso contrário,
        // quando teclarmos "Enter", ele confundirá os valores lidos.
        leitura.nextLine();

        // Teremos que criar uma data de busca. Portanto, declarar uma variável do tipo LocalDate chamada dataBusca.
        // Nela, usaremos LocalDate.of(), que requer ano, mês e dia.
        LocalDate dataBusca = LocalDate.of(ano,  1,1 );// Ex. ano 2015 mês 01 dia 01
        // Formatar datas
        DateTimeFormatter formatarData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream()
                // Buscar pela data de lançamentos feitos, após o ano que foi informado
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento(). isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getNumeroTemporada() +
                                " Episódio: " + e.getTituloEpisodio() +
                                " Data lançamento: " + e.getDataLancamento().format(formatarData)
                ));
         */

        // Nova estrutura de dados Map
        // Busca avaliação por temporada

        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getNumeroTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println("Avaliações por temporada:");
        System.out.println(avaliacaoPorTemporada);

        // Coletando Estatísticas utilizando o DoubleSummaryStatistics

        DoubleSummaryStatistics estatistica = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Estatística:" + estatistica);
        System.out.println("Média:" + estatistica.getAverage());
        System.out.println("Melhor Episódio: " + estatistica.getMax());
        System.out.println("Pior Episódio:" + estatistica.getMin());
        System.out.println("Quantidade de episódios avalaiados: " + estatistica.getCount());



    }
}
