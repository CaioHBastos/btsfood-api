package br.com.btstech.btsfoodapi.infrastructure.service.email;

public class EmailException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmailException(String message) {
        super(message);
    }

    public EmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
