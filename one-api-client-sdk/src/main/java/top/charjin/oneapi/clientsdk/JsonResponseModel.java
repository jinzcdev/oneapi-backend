package top.charjin.oneapi.clientsdk;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JsonResponseModel<T> {
    @SerializedName("response")
    @Expose
    public T response;
}
