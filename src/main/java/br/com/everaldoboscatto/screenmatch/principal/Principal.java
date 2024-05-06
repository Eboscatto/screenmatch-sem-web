package br.com.everaldoboscatto.screenmatch.principal;

import br.com.everaldoboscatto.screenmatch.model.DadosSerie;
import br.com.everaldoboscatto.screenmatch.model.DadosTemporada;
import br.com.everaldoboscatto.screenmatch.model.Episodio;
import br.com.everaldoboscatto.screenmatch.model.Serie;
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

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """       
                    0 - Sair                
                    1 - Buscar séries
                    2 - Buscar episódios    
                    3 - Listar séries buscadas    
                    4 - Buscar série por título   
                    5 - Buscar séries por ator                                       
                                      
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
                case 0:
                    System.out.println("Encerrando sistema...");
                    break;
                default:
                    System.out.println("Opçao inválida!");
            }
        }
    }
    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println("\nImprimindo dados da série buscada:\n" + dados);
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
        // DadosSerie dadosSerie = getDadosSerie();

        // Criar lista de temporadas da série
        System.out.println("Escolha um série pelo nome:");
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

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha um série pelo título: ");
        var nomeSerie = leitura.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série: " + serieBuscada.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void busccarSeriesPorAtor() {
        System.out.println("Digite o nome do ator para busca: ");
        var nomeAtor = leitura.nextLine();
        System.out.println("Avaliações a partir de qual valor? ");
        var avaliacao = leitura.nextDouble();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach( s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));
    }
}