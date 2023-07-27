package top.charjin.oneapi.clientsdk;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonResponseErrModel {

    @SerializedName("error")
    @Expose
    public ErrorInfo error;

    class ErrorInfo {
        @SerializedName("code")
        @Expose
        public String code;

        @Expose
        @SerializedName("message")
        public String message;
    }
}
