package top.charjin.oneapi.backend.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import top.charjin.oneapi.clientsdk.Credential;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class InterfaceInvokerServiceTest {

    @Autowired
    private InterfaceInvokerService service;

    @Test
    void testInterfaceInvoke() {
        try {
            String result = service.invoke(36L, "gender=123", new Credential("AccessKey", "SecretKey"));
            Assertions.assertNotNull(result);
            System.out.println(result);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
