package fr.ttis.npp.exception;


public class HmacGenerationException extends RuntimeException {
    public HmacGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}
