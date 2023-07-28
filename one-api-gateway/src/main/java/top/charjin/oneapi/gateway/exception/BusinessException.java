package top.charjin.oneapi.gateway.exception;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message) {
        this.code = 403;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
