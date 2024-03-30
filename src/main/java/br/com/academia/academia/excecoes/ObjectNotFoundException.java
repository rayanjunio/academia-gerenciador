package br.com.academia.academia.excecoes;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException() {
        super("Não possuem nomes de registros que contém esses caracteres!");
    }
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
