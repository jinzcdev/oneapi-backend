package top.charjin.oneapi.clientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import top.charjin.oneapi.clientsdk.exception.IllegalUrlException;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class OneApiClient {


    /**
     * 网关地址
     */
    public static final String GATEWAY_HOST = "http://localhost:8090";

    private final String accessKey;
    private final String secretKey;

    public OneApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String, String> getHeader(String body) {
        body = URLEncoder.encode(body, CharsetUtil.CHARSET_UTF_8);
        Map<String, String> header = new HashMap<>();
        String nonce = RandomUtil.randomNumbers(4);
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);

        header.put("AccessKey", accessKey);
        header.put("Nonce", nonce);
        header.put("Body", body);
        header.put("Timestamp", currentTime);
        header.put("Sign", DigestUtil.md5Hex(body + secretKey + nonce + currentTime));

        return header;
    }

    public String generateRandomCartoonAvatar() {
        try (HttpResponse response = httpGet(GATEWAY_HOST + "/avatar/cartoon", getHeader(""))) {
            if (response.getStatus() == HttpStatus.HTTP_OK) {
                return response.body();
            }
        }
        return null;
    }

    public String generateRandomUserAvatar(String gender) {
        try (HttpResponse response = httpGet(GATEWAY_HOST + "/avatar/user?gender=" + gender, getHeader(""))) {
            if (response.getStatus() == HttpStatus.HTTP_OK) {
                return response.body();
            }
        }
        return null;
    }

    /**
     * 通过 POST 方式调用接口
     *
     * @param url
     * @param body
     * @return
     * @throws IllegalUrlException
     */
    public HttpResponse doPostWithEncryptedHeader(String url, String body) throws IllegalUrlException {
        if (!url.startsWith("/") || url.matches(".*//.*")) {
            throw new IllegalUrlException();
        }
        return httpPost(GATEWAY_HOST + url, getHeader(body), body);
    }

    /**
     * 通过 GET 方式调用接口
     *
     * @param url
     * @return
     * @throws IllegalUrlException
     */
    public HttpResponse doGetWithEncryptedHeader(String url) throws IllegalUrlException {
        if (!url.startsWith("/") || url.matches(".*//.*")) {
            throw new IllegalUrlException();
        }
        return httpGet(GATEWAY_HOST + url, getHeader(""));
    }


    private HttpResponse httpGet(String url, Map<String, String> header) {
        try (HttpResponse response = HttpRequest
                .get(url)
                .addHeaders(header)
                .execute()) {
            return response;
        }
    }

    private HttpResponse httpPost(String url, Map<String, String> header, String data) {
        try (HttpResponse response = HttpRequest
                .post(url)
                .addHeaders(header)
                .body(data)
                .execute()) {
            return response;
        }
    }
}
