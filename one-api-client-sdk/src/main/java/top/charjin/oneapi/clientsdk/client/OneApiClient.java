package top.charjin.oneapi.clientsdk.client;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class OneApiClient {

    public static final String BASE_URL = "https://charjin.top:7089/oneapi";

    private String accessKey;
    private String secretKey;

    public OneApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }



    private Map<String, String> getHeader(String body) {
        String encode;
        encode = URLEncoder.encode(body, CharsetUtil.CHARSET_UTF_8);
        Map<String, String> header = new HashMap<>();
        String nonce = RandomUtil.randomNumbers(4);
        String currentTime = String.valueOf(System.currentTimeMillis() / 1000);

        header.put("AccessKey", accessKey);
        header.put("Nonce", nonce);
        header.put("Body", encode);
        header.put("Timestamp", currentTime);
        header.put("Sign", DigestUtil.md5Hex(encode + secretKey + nonce + currentTime));

        return header;
    }

    private String httpGet(String url, Map<String, String> header) {
        HttpResponse response = HttpRequest
                .get(url)
                .addHeaders(header)
                .execute();
        String content = response.body();
        response.close();
        return content;
    }

    private String httpPost(String url, Map<String, String> header, String data) {
        HttpResponse response = HttpRequest
                .post(url)
                .addHeaders(header)
                .body(data)
                .execute();
        String content = response.body();
        response.close();
        return content;
    }
}
