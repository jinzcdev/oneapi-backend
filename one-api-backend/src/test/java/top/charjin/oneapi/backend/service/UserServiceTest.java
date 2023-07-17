package top.charjin.oneapi.backend.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 用户服务测试
 */
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    void userRegister() {
        String userAccount = "root";
        String userPassword = "root1234";
        String checkPassword = "root1234";
        try {
            long result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
            userAccount = "root";
            result = userService.userRegister(userAccount, userPassword, checkPassword);
            Assertions.assertEquals(-1, result);
        } catch (Exception e) {

        }
    }
}
