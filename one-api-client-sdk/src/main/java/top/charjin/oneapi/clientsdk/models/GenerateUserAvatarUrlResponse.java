package top.charjin.oneapi.clientsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import top.charjin.oneapi.clientsdk.AbstractModel;

import java.util.HashMap;


/**
 * 生成人物头像响应
 * AvatarUrl: 头像地址
 */
public class GenerateUserAvatarUrlResponse extends AbstractModel {

    @SerializedName("avatarUrl")
    @Expose
    private String avatarUrl;

    @Override
    protected void toMap(HashMap<String, String> map, String prefix) {
        this.setParamSimple(map, prefix + "avatarUrl", this.avatarUrl);
    }
}
