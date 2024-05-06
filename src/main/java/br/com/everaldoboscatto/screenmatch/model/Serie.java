package br.com.everaldoboscatto.screenmatch.model;
import br.com.everaldoboscatto.screenmatch.service.ConsultarChatGPT;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;
 @Column(unique = true)
 private String titulo; // Dizendo que Title é um titulo
 private Integer totalTemporadas;
 private Double avaliacao;
 @Enumerated(EnumType.STRING)
 private Categoria genero;
 private  String atores;
 private String poster;
 private String sinopse ;

 // Atualizar tantos séries quanto episódios e vice e verso

 // Relacionamento um para muitos, entre as entidades Série e Episódio. Onde uma série pode ter muitos episódios.
 @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
 private List<Episodio> episodios = new ArrayList<>();

 // Construtor padrão
 public Serie(){

 }

 public Serie(DadosSerie dadosSerie) {
  this.titulo = dadosSerie.titulo();
  this.totalTemporadas = dadosSerie.totalTemporadas();

  // Tentar obter o valor Double, se não conseguir atribuir o valor {0} para a avaliação
  this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
  // Pegar a primeira palvara com split(), retirar os espaços em branco com o trim()
  this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
  this.atores = dadosSerie.atores();
  this.poster = dadosSerie.poster();
  this.sinopse =  ConsultarChatGPT.obterTraducao(dadosSerie.sinopse()).trim(); //dadosSerie.sinopse();
 }

 public Long getId() {
  return id;
 }

 public void setId(Long id) {
  this.id = id;
 }

 public String getTitulo() {
  return titulo;
 }

 public void setTitulo(String titulo) {
  this.titulo = titulo;
 }

 public Integer getTotalTemporadas() {
  return totalTemporadas;
 }

 public void setTotalTemporadas(Integer totalTemporadas) {
  this.totalTemporadas = totalTemporadas;
 }

 public Double getAvaliacao() {
  return avaliacao;
 }

 public void setAvaliacao(Double avaliacao) {
  this.avaliacao = avaliacao;
 }

 public Categoria getGenero() {
  return genero;
 }

 public void setGenero(Categoria genero) {
  this.genero = genero;
 }

 public String getAtores() {
  return atores;
 }

 public void setAtores(String atores) {
  this.atores = atores;
 }

 public String getPoster() {
  return poster;
 }

 public void setPoster(String poster) {
  this.poster = poster;
 }

 public String getSinopse() {
  return sinopse;
 }

 public void setSinopse(String sinopse) {
  this.sinopse = sinopse;
 }

 public List<Episodio> getEpisodios() {
  return episodios;
 }

 public void setEpisodios(List<Episodio> episodios) {
  episodios.forEach(e -> e .setSerie(this)); // Gravar a chave estrangeira na tabela episódios

  this.episodios = episodios;
 }

 @Override
 public String toString() {
  return "Serie{" +
          "genero=" + genero +
          ",titulo='" + titulo + '\'' +
          ", totalTemporadas=" + totalTemporadas +
          ", avaliacao=" + avaliacao +
          ", atores='" + atores + '\'' +
          ", poster='" + poster + '\'' +
          ", sinopse='" + sinopse + '\'' +
          ", episodios" + episodios +'\''  +
          '}';
 }
}
