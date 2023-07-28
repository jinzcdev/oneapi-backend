package top.charjin.oneapi.clientsdk;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求模型的抽象类
 * 参考腾讯云 SDK 设计，去除了自定义参数
 */
public abstract class AbstractModel {

    public static <O extends AbstractModel> String toJsonString(O obj) {
        return toJsonObject(obj).toString();
    }

    /**
     * Recursively generate obj's JSON object. Even if obj.any() is empty, this recursive progress
     * cannot be skipped because customized additional parameter might be hidden in lower data
     * structure.
     *
     * @param obj
     * @return
     */
    private static <O extends AbstractModel> JsonObject toJsonObject(O obj) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonObject joall = new JsonObject();
        JsonObject jopublic = gson.toJsonTree(obj).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : jopublic.entrySet()) {
            Object fo = null;
            try {
                Field f = obj.getClass().getDeclaredField(entry.getKey());
                f.setAccessible(true);
                fo = f.get(obj);
            } catch (Exception e) {
                // this should never happen
                e.printStackTrace();
            }
            if (fo instanceof AbstractModel) {
                joall.add(entry.getKey(), toJsonObject((AbstractModel) fo));
            } else {
                joall.add(entry.getKey(), entry.getValue());
            }
        }
        return joall;
    }

    /**
     * Deserialize a JSON string into an object of the Class inherited from AbstractModel.
     *
     * @param json JSON formatted string.
     * @param cls  A class which inherited from AbstractModel.
     * @return An object of cls.
     */
    public static <O> O fromJsonString(String json, Class<O> cls) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.fromJson(json, cls);
    }

    protected abstract void toMap(HashMap<String, String> map, String prefix);

    /**
     * Valid only when it's a request object.
     * Some actions can only be posted in multipart format,
     * this method is used to mark which parameters are binary type.
     */
    protected String[] getBinaryParams() {
        return new String[0];
    }

    /**
     * Valid only when it's a multipart request object.
     */
    protected HashMap<String, byte[]> getMultipartRequestParams() {
        return new HashMap<String, byte[]>();
    }

    protected <V> void setParamSimple(HashMap<String, String> map, String key, V value) {
        if (value != null) {
            key = key.replace("_", ".");
            map.put(key, String.valueOf(value));
        }
    }

    protected <V> void setParamArraySimple(HashMap<String, String> map, String prefix, V[] array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                this.setParamSimple(map, prefix + i, array[i]);
            }
        }
    }

    protected <V extends AbstractModel> void setParamObj(
            HashMap<String, String> map, String prefix, V obj) {
        if (obj != null) {
            obj.toMap(map, prefix);
        }
    }

    protected <V extends AbstractModel> void setParamArrayObj(
            HashMap<String, String> map, String prefix, V[] array) {
        if (array != null) {
            for (int i = 0; i < array.length; i++) {
                this.setParamObj(map, prefix + i + ".", array[i]);
            }
        }
    }

    @Override
    public String toString() {
        return toJsonObject(this).toString();
    }
}
