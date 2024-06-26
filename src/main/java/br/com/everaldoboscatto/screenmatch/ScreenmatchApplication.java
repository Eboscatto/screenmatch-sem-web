package br.com.everaldoboscatto.screenmatch;
import br.com.everaldoboscatto.screenmatch.principal.Principal;
import br.com.everaldoboscatto.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	@Autowired
	private SerieRepository repositorio;

    public static void main(String[] args) {

		try {
			SpringApplication.run(ScreenmatchApplication.class, args);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositorio);
		principal.exibeMenu();
	}
}
