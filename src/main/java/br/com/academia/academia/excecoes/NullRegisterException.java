package br.com.academia.academia.excecoes;

public class NullRegisterException extends RuntimeException{
    public NullRegisterException() {
        super("Não há registros no banco de dados.");
    }
    public NullRegisterException(String message) {
        super(message);
    }
}
