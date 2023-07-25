package top.charjin.oneapi.clientsdk;

public class Credential {

    private String accessKey;

    private String secretKey;

    private String token;

    public Credential() {
    }

    public Credential(String accessKey, String secretKey) {
        this(accessKey, secretKey, "");
    }

    public Credential(String accessKey, String secretKey, String token) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.token = token;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return this.secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
