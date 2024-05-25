package br.com.academia.academia.excecoes;

public class NullIdentifierException extends RuntimeException {
  public NullIdentifierException() {
    super("O identificador digitado é nulo.");
  }

  public NullIdentifierException(String message) {
    super(message);
  }
}
