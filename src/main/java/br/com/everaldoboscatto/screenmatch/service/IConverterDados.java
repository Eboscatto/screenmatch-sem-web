package br.com.everaldoboscatto.screenmatch.service;

public interface IConverterDados {
    <T> T obterDados  (String json, Class<T> classe);
}

// <T> T é um tipo genérico

// Cabeçalho do IConverteDados, recebendo um Json que é uma String,
// vai receber uma classe e lá no cpnversor de dados transformar esse J
// son na classe que for indicada.