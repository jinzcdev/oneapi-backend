package top.charjin.oneapi.clientsdk;


import cn.hutool.crypto.SignUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import okhttp3.Headers;
import okhttp3.Response;
import top.charjin.oneapi.clientsdk.exception.OneAPISDKException;
import top.charjin.oneapi.clientsdk.http.HttpConnection;
import top.charjin.oneapi.clientsdk.profile.ClientProfile;
import top.charjin.oneapi.clientsdk.profile.HttpProfile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;


public abstract class AbstractClient {
    public static final int HTTP_RSP_OK = 200;
    public static final String SDK_VERSION = "SDk_JAVA_0.0.1";
    public Gson gson;
    private Credential credential;
    private ClientProfile profile;
    private String endpoint;
    private String service;
    private String path;
    private HttpConnection httpConnection;


    public AbstractClient(String endpoint, Credential credential) {
        this(endpoint, credential, new ClientProfile());
    }

    public AbstractClient(
            String endpoint,
            Credential credential,
            ClientProfile profile) {
        this.credential = credential;
        this.profile = profile;
        this.endpoint = endpoint;
        this.service = endpoint.split("\\.")[0];
        this.path = "/";
        this.gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        this.httpConnection = new HttpConnection(
                this.profile.getHttpProfile().getConnTimeout(),
                this.profile.getHttpProfile().getReadTimeout(),
                this.profile.getHttpProfile().getWriteTimeout()
        );
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public ClientProfile getProfile() {
        return profile;
    }

    public void setProfile(ClientProfile profile) {
        this.profile = profile;
    }

    public String getEndpoint() {
        if (null != this.profile.getHttpProfile().getEndpoint()) {
            return this.profile.getHttpProfile().getEndpoint();
        }
        return this.getService() + "." + this.profile.getHttpProfile().getRootDomain();
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public HttpConnection getHttpConnection() {
        return httpConnection;
    }

    public void setHttpConnection(HttpConnection httpConnection) {
        this.httpConnection = httpConnection;
    }


    /**
     * @param request 请求参数模型
     * @param path    请求路径，以 / 开头
     * @return
     * @throws OneAPISDKException
     */
    protected String internalRequest(AbstractModel request, String path) throws OneAPISDKException {
        // 1. 构建访问地址
        String endpoint = this.getEndpoint();
        // 2. 校验参数
        Response okRsp = null;
        String sm = this.profile.getSignMethod();

        // 3. 发起请求
        try {
            // 暂时只使用 SIGN_SHA256 一种签名方式
            if (sm.equals(ClientProfile.SIGN_SHA256)) {
                okRsp = doRequest(endpoint + path, request);
            } else {
                throw new OneAPISDKException("Signature method " + sm + " is invalid or not supported yet.");
            }
        } catch (IOException e) {
            throw new OneAPISDKException(e.getClass().getName() + "-" + e.getMessage());
        }
        // 处理 HTTP 请求响应
        if (okRsp.code() != AbstractClient.HTTP_RSP_OK) {
            throw new OneAPISDKException(okRsp.message(), String.valueOf(okRsp.code()));
        }
        String strResp = "";

        try {
            if (okRsp.body() != null) {
                strResp = okRsp.body().string();
            }
        } catch (IOException e) {
            throw new OneAPISDKException("Cannot transfer response body to string, because Content-Length is too large, or Content-Length and stream length disagree.",
                    endpoint.getClass().getName());
        }


        // 处理响应结果
        JsonResponseModel<Object> respObj = null;
        try {
            respObj = gson.fromJson(strResp, new TypeToken<JsonResponseModel<Object>>() {
            }.getType());
        } catch (JsonSyntaxException e) {
            String msg = "json is not a valid representation for an object of type";
            throw new OneAPISDKException(msg, e.getClass().getName());
        }
        if (respObj.getCode() != HTTP_RSP_OK) {
            throw new OneAPISDKException(respObj.getMessage(), String.valueOf(respObj.getCode()));
        }
        return strResp;
    }

    private Response doRequest(String endpointPath, AbstractModel request)
            throws OneAPISDKException, IOException {
        String httpRequestMethod = this.profile.getHttpProfile().getReqMethod();

        HashMap<String, String> params = new HashMap<>();
        request.toMap(params, "");
        String strParam = this.formatRequestData(params);
        String reqMethod = this.profile.getHttpProfile().getReqMethod();
        String url = this.profile.getHttpProfile().getProtocol() + endpointPath;
        String requestPayload = httpRequestMethod.equals(HttpProfile.REQ_POST)
                ? AbstractModel.toJsonString(request) : "";

        String accessKey = this.credential.getAccessKey();
        String secretKey = this.credential.getSecretKey();

        if (accessKey == null || secretKey == null) {
            throw new OneAPISDKException("Invalid credential, accessKey or secretKey is null.");
        }

        // 生成规范化请求字符串
        String canonicalQueryString = this.getCanonicalQueryString(params, httpRequestMethod);
        // 创建时间戳、随机数
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonce = String.valueOf(Math.abs(new SecureRandom().nextInt()));

        // 添加自定义的头部
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("X-AccessKey", this.credential.getAccessKey());
        headerMap.put("X-Nonce", nonce);
        headerMap.put("X-Timestamp", timestamp);

        // 根据自定义的头部、规范化请求字符串、请求体、密钥生成签名
        String signature = SignUtil.signParamsSha256(headerMap, canonicalQueryString + requestPayload + secretKey);

        String authorization = "OneAPI-SHA256 "
                + "Credential="
                + accessKey
                + ", "
                + "Signature="
                + signature;

        // 生成请求头
        Headers.Builder hb = Headers.of(headerMap).newBuilder();
        hb.add("Authorization", authorization);
        Headers headers = hb.build();


        if (reqMethod.equals(HttpProfile.REQ_GET)) {
            return this.httpConnection.getRequest(url + "?" + strParam, headers);
        } else if (reqMethod.equals(HttpProfile.REQ_POST)) {
            return this.httpConnection.postRequest(url, requestPayload, headers);
        } else {
            throw new OneAPISDKException("Method only support (GET, POST)");
        }
    }


    /**
     * 生成规范化请求字符串
     *
     * @param params
     * @param method
     * @return
     */
    private String getCanonicalQueryString(HashMap<String, String> params, String method) {
        if (method != null && method.equals(HttpProfile.REQ_POST)) {
            return "";
        }
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String v = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            queryString.append("&").append(entry.getKey()).append("=").append(v);
        }
        if (queryString.length() == 0) {
            return "";
        } else {
            return queryString.substring(1);
        }
    }


    /**
     * 将请求参数转为规范化的字符串（不考虑 GET 或者 POST）
     *
     * @param param
     * @return
     */
    private String formatRequestData(Map<String, String> param) {
        if (param.size() == 0) {
            return "";
        }
        StringBuilder strParam = new StringBuilder();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            strParam.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                    .append("=")
                    .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                    .append("&");
        }
        if (strParam.charAt(strParam.length() - 1) == '&') {
            strParam.deleteCharAt(strParam.length() - 1);
        }
        return strParam.toString();
    }
}
