package com.al.agency.integration;

import com.al.agency.entities.User;
import com.al.agency.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
public class Authorization {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSuccessfulLogin() throws Exception{
        mockMvc.perform(formLogin().user("admin").password("100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/after_login"));
    }

    @Test
    public void testBadLogin() throws Exception{
        mockMvc.perform(formLogin().user("user1").password("100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login?error=true"));
    }

    @Test
    public void testRegistration() throws Exception{
        mockMvc.perform(post("/registration")
                .param("login","user1")
                .param("password","100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Вы успешно зарегистрированны")));

        User user = userRepository.findByLogin("user");
        Assertions.assertNotNull(user);

        mockMvc.perform(formLogin().user("user").password("100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/after_login"));
    }
}
