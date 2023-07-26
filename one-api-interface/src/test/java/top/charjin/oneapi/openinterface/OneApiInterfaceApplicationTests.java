package top.charjin.oneapi.openinterface;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OneApiInterfaceApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCartoonAvatarUrl() throws Exception {
        mockMvc.perform(get("/avatar/cartoon"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }

    @Test
    public void testUserAvatarUrl() throws Exception {
        mockMvc.perform(get("/avatar/user").param("gender", "male"))
                .andExpect(status().isOk())
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()));
    }


}
