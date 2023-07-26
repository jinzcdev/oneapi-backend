package top.charjin.oneapi.openinterface.service.Impl;

import org.springframework.stereotype.Service;
import top.charjin.oneapi.openinterface.service.AvatarService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AvatarServiceImpl implements AvatarService {

    @Override
    public String getUserAvatarUrl(String gender) throws IOException {
        String avatarUrl = "https://xsgames.co/randomusers/avatar.php?g=" + gender;
        return getRedirectUrl(avatarUrl);
    }

    @Override
    public String getCartoonAvatarUrl() throws IOException {
        String avatarUrl = "https://www.loliapi.com/acg/pp/";
        return getRedirectUrl(avatarUrl);
    }

    private String getRedirectUrl(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setInstanceFollowRedirects(false); // 禁止自动重定向
        int responseCode = connection.getResponseCode(); // 获取响应码
        if (responseCode == HttpURLConnection.HTTP_MOVED_TEMP
                || responseCode == HttpURLConnection.HTTP_MOVED_PERM) {
            return connection.getHeaderField("Location");
        } else {
            return url;
        }

    }
}
