package top.charjin.oneapi.clientsdk.exception;

public class IllegalUrlException extends Exception {


    public IllegalUrlException() {
        super("url is illegal");
    }

    public IllegalUrlException(String message) {
        super(message);
    }

}
