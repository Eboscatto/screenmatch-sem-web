package br.com.everaldoboscatto.screenmatch.model;


public enum Categoria {
    ACAO("Action", "Ação"),// Nome de  categoria vinda do Omdb
    ROMANCE("Romance", "Romance"),
    COMEDIA("Comedy", "Comédia"),

    DRAMA("Drama", "Drama"),

    CRIME("Crime", "Crime");

    private String categoriaOmdb;
    private String categoriaPortugues;

    Categoria(String categoriaOmdb, String categoriaPortugues) {
        this.categoriaOmdb = categoriaOmdb;
        this.categoriaPortugues = categoriaPortugues;
    }

    // Interpretar os valores recebidos do Omdb. Ex.: se vem o nome "Action" considerar "ACAO", "Comedy" - "COMEDIA"
    public static Categoria fromString(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
    public static Categoria fromStringPortugues(String text) {
        for (Categoria categoria : Categoria.values()) {
            if (categoria.categoriaPortugues.equalsIgnoreCase(text)) {
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenhuma categoria encontrada para a string fornecida: " + text);
    }
}
