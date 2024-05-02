package br.com.everaldoboscatto.screenmatch.model;


public enum Categoria {
    ACAO("Action"),// Nome de  categoria vinda do Omdb
    ROMANCE("Romance"),
    COMEDIA("Comedy"),

    DRAMA("Drama"),

    CRIME("Crime");

    private String categoriaOmdb;

    Categoria(String categoriaOmdb) {
        this.categoriaOmdb = categoriaOmdb;

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
}
