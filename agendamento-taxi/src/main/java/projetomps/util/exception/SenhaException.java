package projetomps.util.exception;

public class SenhaException extends Exception {
    public SenhaException(String message) {
        super(message);
    }
    
    public SenhaException(String message, Throwable cause) {
        super(message, cause);
    }
}