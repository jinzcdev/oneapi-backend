package top.charjin.oneapi.clientsdk.exception;

public class OneAPISDKException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * UUID of the request, it will be empty if request is not fulfilled.
     */
    private String requestId;

    /**
     * Error code, When API returns a failure, it must have an error code.
     */
    private String errorCode;

    public OneAPISDKException(String message) {
        this(message, "");
    }

    public OneAPISDKException(String message, String requestId) {
        this(message, requestId, "");
    }

    public OneAPISDKException(String message, String requestId, String errorCode) {
        super(message);
        this.requestId = requestId;
        this.errorCode = errorCode;
    }

    public String getRequestId() {
        return requestId;
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
        return "[TencentCloudSDKException]"
                + "code: "
                + this.getErrorCode()
                + " message:"
                + this.getMessage()
                + " requestId:"
                + this.getRequestId();
    }
}
