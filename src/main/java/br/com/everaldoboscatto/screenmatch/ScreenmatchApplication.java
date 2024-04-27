package br.com.everaldoboscatto.screenmatch;

import br.com.everaldoboscatto.screenmatch.model.DadosSerie;
import br.com.everaldoboscatto.screenmatch.service.ConsumoAPI;
import br.com.everaldoboscatto.screenmatch.service.ConverterDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {

		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoAPI = new ConsumoAPI();
		String chave = "2f196da8";
		String buscaSerie ="gilmore+girls";
		int  buscaEpisodio = 1;
		// Obter o dados da série e atribuir a variável json
		var json = consumoAPI.obterDados("https://www.omdbapi.com/?t=" + buscaSerie + "&apikey=" + chave);
		//String endereco = "https://www.omdbapi.com/?t=" + buscaTemporada.replace(" ","+") + "&apikey=" + chave;

		// Imprimir a variável jeson para confirmar que os dados vieram ok.
		System.out.println(json);

		// Instanciar o conversor
		ConverterDados conversor = new ConverterDados();
		// Transformar em DadosSerie
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		// Imprimir a classe DadosSerie
		System.out.println(dados);

		}
}
