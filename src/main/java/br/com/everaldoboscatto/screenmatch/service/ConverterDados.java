package br.com.everaldoboscatto.screenmatch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConverterDados implements IConverterDados{
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}

// Criar a inteface ConverteDados antes de criar o ObjectMapper