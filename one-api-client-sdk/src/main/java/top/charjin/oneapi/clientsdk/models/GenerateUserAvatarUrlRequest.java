package top.charjin.oneapi.clientsdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import top.charjin.oneapi.clientsdk.AbstractModel;

import java.util.HashMap;


/**
 * 生成人物头像请求
 * 无参数
 */
public class GenerateUserAvatarUrlRequest extends AbstractModel {

    public static final String PARAM_MALE = "male";
    public static final String PARAM_FEMALE = "female";
    @Expose
    @SerializedName("gender")
    private String gender;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    protected void toMap(HashMap<String, String> map, String prefix) {
        this.setParamSimple(map, prefix + "gender", this.gender);
    }
}
