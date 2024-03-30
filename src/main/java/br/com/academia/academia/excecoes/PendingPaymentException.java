package br.com.academia.academia.excecoes;

public class PendingPaymentException extends RuntimeException {
    public PendingPaymentException() {
        super("Entrada não autorizada por pendência de pagamento!");
    }
    public PendingPaymentException(String message) {
        super(message);
    }
}
