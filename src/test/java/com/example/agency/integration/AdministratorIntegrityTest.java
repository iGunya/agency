package com.example.agency.integration;

import com.example.agency.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin")
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
public class AdministratorIntegrityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPageAdmin() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/only_for_admins"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("admin"))
                .andExpect(xpath("/html/body/div/div[2]/div/div/div/table/tbody/tr")
                        .nodeCount(2));
    }

    @Test
    public void testUpdateUserOnAdmin() throws Exception {
        mockMvc.perform(post("/only_for_admins")
                .param("id_user","2")
                .param("login","manager1")
                .param("password","")
                .param("role","ROLE_USER"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testBadUpdateUserOnAdmin() throws Exception {
        mockMvc.perform(post("/only_for_admins")
                .param("id_user","2")
                .param("login","admin")
                .param("password","")
                .param("role","ROLE_USER"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Пользователь с таким именем существет")))
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("admin"))
                .andExpect(xpath("/html/body/div/div[2]/div/div/div/table/tbody/tr")
                        .nodeCount(2));
    }

}
