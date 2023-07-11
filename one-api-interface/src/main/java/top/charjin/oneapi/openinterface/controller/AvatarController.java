package top.charjin.oneapi.openinterface.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 头像接口
 */
@RestController
@RequestMapping("/avatar")
@Slf4j
public class AvatarController {

    @GetMapping("/cartoon")
    public String getCartoonAvatarUrl() {
        String avatarUrl = "https://www.loliapi.com/acg/pp/";
        return getRedirectUrl(avatarUrl);
    }

    /**
     * @param gender male or female
     * @return AvatarUrl
     */
    @GetMapping("/user")
    public String getUserAvatarUrl(@RequestParam String gender) {
        String avatarUrl = "https://xsgames.co/randomusers/avatar.php?g=" + gender;
        return getRedirectUrl(avatarUrl);
    }

    private String getRedirectUrl(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(false); // 禁止自动重定向
            int responseCode = connection.getResponseCode(); // 获取响应码
            if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                    || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
                return connection.getHeaderField("Location");
            } else {
                return url;
            }
        } catch (Exception e) {
            throw new RuntimeException("请求异常!");
        }
    }

    @ExceptionHandler(Exception.class)
    private String exceptionHandler(Exception e) {
        log.error(e.getMessage());
        return e.getMessage();
    }
}
