package top.charjin.oneapi.gateway.http;

import top.charjin.oneapi.gateway.exception.AuthorizationFormatErrorException;

import java.util.HashMap;
import java.util.Map;

public class AuthorizationParser {

    public static Map<String, String> parseByMap(String auth) throws AuthorizationFormatErrorException {
        Map<String, String> map = new HashMap<>();

        int firstBlankPosition = auth.indexOf(' ');
        if (firstBlankPosition == -1) {
            throw new AuthorizationFormatErrorException("Authorization 格式错误");
        }
        map.put("Schema", auth.substring(0, firstBlankPosition));
        String[] items = auth.substring(firstBlankPosition + 1).split(", ");
        if (items.length == 0) {
            throw new AuthorizationFormatErrorException("Authorization 格式错误");
        }
        for (String item : items) {
            String[] kv = item.split("=");
            if (kv.length != 2) {
                throw new AuthorizationFormatErrorException("Authorization 格式错误");
            }
            map.put(kv[0], kv[1]);
        }
        return map;
    }

}
