package top.charjin.oneapi.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import top.charjin.oneapi.clientsdk.client.OneApiClient;

/**
 * 主类测试
 */
@SpringBootTest
@ActiveProfiles("test")
class MainApplicationTests {


    @Autowired
    private OneApiClient apiClient;

    @Test
    void contextLoads() {
        System.out.println(apiClient.generateRandomCartoonAvatar());
        System.out.println(apiClient.generateRandomUserAvatar("male"));

    }

}
