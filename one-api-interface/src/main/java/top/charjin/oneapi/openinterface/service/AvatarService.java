package top.charjin.oneapi.openinterface.service;

import java.io.IOException;

public interface AvatarService {

    String getUserAvatarUrl(String gender) throws IOException;

    String getCartoonAvatarUrl() throws IOException;
}
