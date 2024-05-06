package br.com.everaldoboscatto.screenmatch.model;

import com.fasterxml.jackson.databind.DatabindException;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodios")
public class Episodio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer numeroTemporada;
    private String tituloEpisodio;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    // Relacionamento muitos para um, entre as entidades Episódio e Série. Onde muitos episódios pertencem a uma série.
    @ManyToOne
    private Serie serie;

    // Construtor padrão
    public Episodio() {

    }
    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.numeroTemporada = numeroTemporada;
        this.tituloEpisodio = dadosEpisodio.tituloEpisodio();
        this.numeroEpisodio = dadosEpisodio.numeroEpisodio();

        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao()); // Fazendo um parse

        }catch (NumberFormatException ex) {
            this.avaliacao = 0.0;
        }

        try {
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataDeLancamento()); // Fazendo um parse

        }catch (DateTimeParseException ex) {
            this.dataLancamento = null;

        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumeroTemporada() {
        return numeroTemporada;
    }

    public void setNumeroTemporada(Integer numeroTemporada) {
        this.numeroTemporada = numeroTemporada;
    }

    public String getTituloEpisodio() {
        return tituloEpisodio;
    }

    public void setTituloEpisodio(String tituloEpisodio) {
        this.tituloEpisodio = tituloEpisodio;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {

        this.dataLancamento = dataLancamento;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }
    @Override
    public String toString() {
        return  "numeroTemporada=" + numeroTemporada +
                ", tituloEpisodio='" + tituloEpisodio + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento;
    }
}
