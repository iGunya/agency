package com.example.agency.unit.controller;

import com.example.agency.controllers.AuthenticationController;
import com.example.agency.entities.User;
import com.example.agency.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;

    @MockBean
    private UserService userService;

    @Test
    public void testPageLogin() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Вход")))
                .andExpect(content().string(containsString("Username")))
                .andExpect(content().string(containsString("Password")))
                .andExpect(content().string(containsString("Регистрация")));
    }

    @Test
    public void testPageRegistration() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/registration"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Регистрация")))
                .andExpect(content().string(containsString("Username")))
                .andExpect(content().string(containsString("Password")))
                .andExpect(content().string(containsString("Зарегистрировать")));
    }

    @Test
    public void testLogin() throws Exception{
        mockMvc.perform(formLogin().user("admin").password("100"))
                .andDo(MockMvcResultHandlers.print())
                //тк нет БД ошибка авторизации
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testPostRegistration() throws Exception{
        mockMvc.perform(post("/registration")
                .param("username","user")
                .param("password","100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Вы успешно зарегистрированны")));
    }

    @Test
    public void testNotUniqueUserPostRegistration() throws Exception{
        when(userService.findByLogin(Mockito.any())).thenReturn(new User());

        mockMvc.perform(post("/registration")
                .param("username","user")
                .param("password","100"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Такой пользователь существует")));
    }
}
