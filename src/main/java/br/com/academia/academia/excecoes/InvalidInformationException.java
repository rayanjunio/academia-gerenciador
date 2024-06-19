package br.com.academia.academia.excecoes;

public class InvalidInformationException extends RuntimeException {
  public InvalidInformationException() {
    super("Check the person data!");
  }

  public InvalidInformationException(String message) {
    super(message);
  }
}
