package top.charjin.oneapi.backend.service;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.charjin.oneapi.backend.model.vo.InterfaceInvokeInfoVo;

import java.util.List;

@SpringBootTest
public class UserInterfaceInfoServiceTest {

    @Autowired
    private UserInterfaceInfoService userInterfaceInfoService;

    @Test
    void test() {
        List<InterfaceInvokeInfoVo> list = userInterfaceInfoService.getInterfaceInvokeInfoVoList();
        list.forEach(System.out::println);
        Assertions.assertNotNull(list);
        Assertions.assertNotEquals(list.size(), 0);
    }
}
