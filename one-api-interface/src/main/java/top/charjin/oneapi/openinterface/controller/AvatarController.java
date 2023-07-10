package top.charjin.oneapi.openinterface.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 头像接口
 */
@RestController
@RequestMapping("/avatar")
public class AvatarController {

    @PostMapping("/random")
    public String getAvatarUrlByPost() throws Exception {
        String avatarUrl = "https://www.loliapi.com/acg/pp/";
        HttpURLConnection connection = (HttpURLConnection) new URL(avatarUrl).openConnection();
        connection.setInstanceFollowRedirects(false); // 禁止自动重定向
        int responseCode = connection.getResponseCode(); // 获取响应码
        if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
            return connection.getHeaderField("Location");
        } else {
            return avatarUrl;
        }
    }


}
