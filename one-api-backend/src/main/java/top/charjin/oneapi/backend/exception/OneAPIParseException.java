package top.charjin.oneapi.backend.exception;

public class OneAPIParseException extends Exception {
    private static final long serialVersionUID = 1L;

    public OneAPIParseException(String message) {
        super(message);
    }

    public OneAPIParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
