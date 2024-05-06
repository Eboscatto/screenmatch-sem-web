package br.com.everaldoboscatto.screenmatch.repository;

import br.com.everaldoboscatto.screenmatch.model.Categoria;
import br.com.everaldoboscatto.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    // Bucar uma série através de um trecho do seu título
    // findBy -> significa encontrar por...
    // Optinal -> indica que pode ou não encontrar uma série com o nome indicado
    // Containing -> se existir, se encontrar
    // IgnoreCase -> indica que deve ignorar maiúsculas e minúsculas
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    // Buscar séries pelo nome do ator e pela avaliação informado
    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    // Buscar as 5 séries mais bem avaliadas
    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);
}

