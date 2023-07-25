package top.charjin.oneapi.clientsdk.http;


import okhttp3.*;
import top.charjin.oneapi.clientsdk.exception.OneAPISDKException;

import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

;

public class HttpConnection {

    // https://stackoverflow.com/questions/31423154/performance-of-a-singleton-instance-okhttpclient
    // https://github.com/square/okhttp/issues/3372
    // Creating dispatcher and connectionPool is expensive.
    // Share them between OkHttpClients by singleton's Builder.
    private static final OkHttpClient clientSingleton = new OkHttpClient();
    private OkHttpClient client;

    public HttpConnection(Integer connTimeout, Integer readTimeout, Integer writeTimeout) {
        this.client = clientSingleton.newBuilder()
                .connectTimeout(connTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
    }

    public void setProxy(Proxy proxy) {
        this.client = this.client.newBuilder().proxy(proxy).build();
    }

    public void setProxyAuthenticator(Authenticator authenticator) {
        this.client = this.client.newBuilder().proxyAuthenticator(authenticator).build();
    }

    @Deprecated
    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory) {
        this.client = this.client.newBuilder().sslSocketFactory(sslSocketFactory).build();
    }

    public void setSSLSocketFactory(SSLSocketFactory sslSocketFactory, X509TrustManager trustManager) {
        this.client = this.client.newBuilder().sslSocketFactory(sslSocketFactory, trustManager).build();
    }

    public Response doRequest(Request request) throws IOException {
        return this.client.newCall(request).execute();
    }

    public Response getRequest(String url) throws OneAPISDKException, IOException {
        Request request = null;
        try {
            request = new Request.Builder().url(url).get().build();
        } catch (IllegalArgumentException e) {
            throw new OneAPISDKException(e.getClass().getName() + "-" + e.getMessage());
        }

        return this.doRequest(request);
    }

    public Response getRequest(String url, Headers headers) throws OneAPISDKException, IOException {
        Request request = null;
        try {
            request = new Request.Builder().url(url).headers(headers).get().build();
        } catch (IllegalArgumentException e) {
            throw new OneAPISDKException(e.getClass().getName() + "-" + e.getMessage());
        }

        return this.doRequest(request);
    }

    public Response postRequest(String url, String body) throws OneAPISDKException, IOException {
        MediaType contentType = MediaType.parse("application/x-www-form-urlencoded");
        Request request = null;
        try {
            request = new Request.Builder().url(url).post(
                    RequestBody.create(contentType, body)).build();
        } catch (IllegalArgumentException e) {
            throw new OneAPISDKException(e.getClass().getName() + "-" + e.getMessage());
        }

        return this.doRequest(request);
    }

    public Response postRequest(String url, String body, Headers headers)
            throws OneAPISDKException, IOException {
        MediaType contentType = MediaType.parse(headers.get("Content-Type"));
        Request request = null;
        try {
            request =
                    new Request.Builder()
                            .url(url)
                            .post(RequestBody.create(contentType, body))
                            .headers(headers)
                            .build();
        } catch (IllegalArgumentException e) {
            throw new OneAPISDKException(e.getClass().getName() + "-" + e.getMessage());
        }

        return this.doRequest(request);
    }

    public Response postRequest(String url, byte[] body, Headers headers)
            throws OneAPISDKException, IOException {
        MediaType contentType = MediaType.parse(headers.get("Content-Type"));
        Request request = null;
        try {
            request =
                    new Request.Builder()
                            .url(url)
                            .post(RequestBody.create(contentType, body))
                            .headers(headers)
                            .build();
        } catch (IllegalArgumentException e) {
            throw new OneAPISDKException(e.getClass().getName() + "-" + e.getMessage());
        }

        return this.doRequest(request);
    }
}
