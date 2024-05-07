package br.com.everaldoboscatto.screenmatch.principal;

import br.com.everaldoboscatto.screenmatch.model.*;
import br.com.everaldoboscatto.screenmatch.repository.SerieRepository;
import br.com.everaldoboscatto.screenmatch.service.ConsumoAPI;
import br.com.everaldoboscatto.screenmatch.service.ConverterDados;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI(); // Instanciar o ConsumoAPI
    private ConverterDados conversor = new ConverterDados(); // Instanciar o conversor
    private final String ENDERECO = "https://www.omdbapi.com/?t="; // Constante
    private final String API_KEY = "&apikey=2f196da8"; // Constante
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repositorio;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;
    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """       
                    00 - Sair                
                    01 - Buscar séries
                    02 - Buscar episódios    
                    03 - Listar séries buscadas    
                    04 - Buscar série por título   
                    05 - Buscar séries por ator   
                    06 - Séries Top5 
                    07 - Buscar séries por categoria    
                    08 - Buscar séries pela quantidade de temporadas    
                    09 - Buscar episódios por um trecho do nome    
                    10 - Episódios Top5 por série   
                    11 - Buscar episódios a partir de uma data                   
                                      
                    """;

            System.out.println(menu);
            System.out.println("Opção: ");
            opcao = leitura.nextInt();
            leitura.nextLine();
            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    busccarSeriesPorAtor();
                    break;
                case 6:
                    buscarSeriesTop5();
                    break;
                case 7:
                    BuscarSeriesPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrechoDoNome();
                    break;
                case 10:
                    buscarEpisodiosTop5PorSerie();
                case 11:
                    buscarEpisodioPorData();
                    break;
                case 0:
                    System.out.println("Encerrando sistema...");
                    break;
                default:
                    System.out.println("Opçao inválida!");
            }
        }
    }

    // Método buscar séries na web
    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println("\nImprimindo dados da série buscada:\n" + dados);
    }

    // Método pegar daados de série
    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série que de desja buscar:");
        var nomeSerie = leitura.nextLine();
        // Armazenar dados da série no formato Json
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        // Converter dados da série de Json para a classe Java
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    // Método buscar episódio da série
    private void buscarEpisodioPorSerie() {
        // DadosSerie dadosSerie = getDadosSerie();

        // Criar lista de temporadas da série
        System.out.println("Escolha uma série pelo nome:");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        // Verificar se a série existe
        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> listaTemporadas = new ArrayList<>();

            // Percorrer total temporadas
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") +
                        "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                listaTemporadas.add(dadosTemporada);
            }
            listaTemporadas.forEach(System.out::println);

            List<Episodio> episodios = listaTemporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroTemporada(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        } else {
            System.out.println("Série não encontrada");
        }
    }

    // Método buscar todas as séries
    private void listarSeriesBuscadas() {
        System.out.println("\nSéries buscadas:");
        dadosSeries.forEach(System.out::println);

        // Criar lista de séries
        System.out.println("\nSéries agrupadas por gênero:");

        // Buscar no banco de dados
        series = repositorio.findAll(); // temos uma lista global de séries
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero)) // Ordena séries por gênero/categoria
                .forEach(System.out::println);
    }

    // Método buscar série pelo nome/título

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o título da série que deseja buscar:");
        var nomeSerie = leitura.nextLine();
        serieBusca = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da série: " + serieBusca.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    // Método buscar séries pelo nome do ator e pela avaliação informado
    private void busccarSeriesPorAtor() {
        System.out.println("Digite o nome do ator para busca: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual valor? ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    // Método buscar as séries Top5
    private void buscarSeriesTop5() {
        List<Serie> seriesTop = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }

    // Método buscar séries pela categoria/Genero informada
    private void BuscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de qual categoria/gênero?");
        var nomeGenero = leitura.nextLine();

        Categoria categoria = Categoria.fromStringPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        seriesPorCategoria.forEach(System.out::println);
    }

    // Método buscar séries pela quantidade de temporadas e pela avaliação informada
    private void filtrarSeriesPorTemporadaEAvaliacao() {
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaEAValiacao(totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + "  - avaliação: " + s.getAvaliacao()));
    }
      /*

    private void BuscarSeriesPelaQtdeTemporadasEAvaliacao() {
        System.out.println("Você que ver séries com até quantas temporadas: ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();

        System.out.println("Com avaliação a partir de qual valor? ");
        var avaliacao = leitura.nextDouble();
        leitura.nextLine();

       // List<Serie> filtroDeSeries = repositorio.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas, avaliacao);

        List<Serie> filtroDeSeries = repositorio.seriesPorTempordaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("Séries com " + totalTemporadas + " temporadas: ");
        filtroDeSeries.forEach(s ->
                System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));
     */
    private void buscarEpisodioPorTrechoDoNome() {
        System.out.println("Digite um trecho do nome do episódio que deseja buscar");
        var trechoNomeEpisodio = leitura.nextLine();
        List<Episodio> episodiosEcontrados = repositorio.episodiosPorTrechoDoNome(trechoNomeEpisodio);
        episodiosEcontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s \n",
                        e.getSerie().getTitulo(), e.getNumeroTemporada(),
                        e.getNumeroEpisodio(), e.getTituloEpisodio()));
    }
    private void buscarEpisodiosTop5PorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(System.out::println);
            topEpisodios.forEach( e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s \n",
                            e.getSerie().getTitulo(), e.getNumeroTemporada(),
                            e.getNumeroEpisodio(), e.getTituloEpisodio(), e.getAvaliacao()));

        }
    }

    private void buscarEpisodioPorData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            System.out.println("\nDigite o ano a partir do qual quer buscar os episódios:");
            var anoLancamento = leitura.nextInt();
            leitura.nextLine();

            List<Episodio> episodiosAno = repositorio.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);

        }
    }
}

