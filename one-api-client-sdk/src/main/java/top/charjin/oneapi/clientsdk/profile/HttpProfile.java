package top.charjin.oneapi.clientsdk.profile;


/**
 * HTTP Profile 配置类
 * 用于配置 HTTP 请求的一些参数，如超时时间、协议、请求方法等
 * 请求方法仅支持 POST 和 GET
 */
public class HttpProfile {

    public static final String REQ_HTTPS = "https://";

    public static final String REQ_HTTP = "http://";

    public static final String REQ_POST = "POST";

    public static final String REQ_GET = "GET";

    /**
     * Time unit, 1 minute, equals 60 seconds.
     */
    public static final int TM_MINUTE = 60;

    private String reqMethod;

    /**
     * Endpoint means the domain which this request is sent to, such as oenapi.charjin.top.
     */
    private String endpoint;

    /**
     * root domain means endpoint without service name, such as charjin.top.
     */
    private String rootDomain;

    /**
     * HTTPS or HTTP, currently only HTTPS is valid.
     */
    private String protocol;


    /**
     * Connect timeout in seconds
     */
    private int connTimeout;
    /**
     * Read timeout in seconds.
     */
    private int readTimeout;
    /**
     * Write timeout in seconds
     */
    private int writeTimeout;

    public HttpProfile() {
        this.reqMethod = HttpProfile.REQ_POST;
        this.endpoint = null;
        this.rootDomain = "charjin.top";
        this.protocol = HttpProfile.REQ_HTTPS;
        this.connTimeout = HttpProfile.TM_MINUTE;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public void setWriteTimeout(int writeTimeout) {
        this.writeTimeout = writeTimeout;
    }

    public String getReqMethod() {
        return this.reqMethod;
    }

    /**
     * Set request method, GET or POST.
     *
     * @param reqMethod
     */
    public void setReqMethod(String reqMethod) {
        this.reqMethod = reqMethod;
    }

    public String getEndpoint() {
        return this.endpoint;
    }

    /**
     * Set the endpoint.
     *
     * @param endpoint
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public int getConnTimeout() {
        return this.connTimeout;
    }

    /**
     * Set connect timeout value.
     *
     * @param connTimeout A integer represents time in seconds.
     */
    public void setConnTimeout(int connTimeout) {
        this.connTimeout = connTimeout;
    }

    public String getProtocol() {
        return this.protocol;
    }

    /**
     * Set request protocol.
     *
     * @param protocol https:// or http://
     */
    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }


    public String getRootDomain() {
        return rootDomain;
    }

    public void setRootDomain(String rootDomain) {
        this.rootDomain = rootDomain;
    }
}
