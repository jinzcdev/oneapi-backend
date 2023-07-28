package top.charjin.oneapi.clientsdk.exception;

public class OneAPISDKException extends Exception {
    private static final long serialVersionUID = 1L;


    /**
     * Error code, When API returns a failure, it must have an error code.
     */
    private String errorCode;

    public OneAPISDKException(String message) {
        this(message, "");
    }

    public OneAPISDKException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }


    /**
     * Get error code
     *
     * @return A string represents error code
     */
    public String getErrorCode() {
        return errorCode;
    }

    public String toString() {
        return "[OneAPISDKException]"
                + " code: "
                + this.getErrorCode()
                + " message: "
                + this.getMessage();
    }
}
