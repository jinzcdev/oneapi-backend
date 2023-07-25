package top.charjin.oneapi.clientsdk.profile;


/**
 * 客户端 Profile 配置类
 * 用于配置客户端的一些参数，如签名方法、HTTP 相关参数等
 */
public class ClientProfile {

    public static final String SIGN_SHA256 = "SHA256";


    private HttpProfile httpProfile;

    private String signMethod;

    /**
     * If payload is NOT involved in signing process, true means will ignore payload, default is
     * false.
     */
    private boolean unsignedPayload;


    public ClientProfile(String signMethod, HttpProfile httpProfile) {
        if (signMethod == null || signMethod.isEmpty()) {
            signMethod = SIGN_SHA256;
        }
        this.signMethod = signMethod;
        this.httpProfile = httpProfile;
        this.unsignedPayload = false;
    }

    public ClientProfile(String signMethod) {
        this(signMethod, new HttpProfile());
    }

    public ClientProfile() {
        this(ClientProfile.SIGN_SHA256, new HttpProfile());
    }

    public String getSignMethod() {
        return this.signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public HttpProfile getHttpProfile() {
        return this.httpProfile;
    }

    public void setHttpProfile(HttpProfile httpProfile) {
        this.httpProfile = httpProfile;
    }

    /**
     * Get the flag of whether payload is ignored.
     */
    public boolean isUnsignedPayload() {
        return this.unsignedPayload;
    }

    /**
     * Set the flag of whether payload should be ignored. Only has effect when request method is POST.
     *
     * @param flag
     */
    public void setUnsignedPayload(boolean flag) {
        this.unsignedPayload = flag;
    }

}