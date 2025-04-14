package fr.ttis.npp.exception;

public class HmacVerificationException extends RuntimeException {
    public HmacVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
