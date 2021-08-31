package com.example.agency.integration;

import com.example.agency.entities.User;
import com.example.agency.repositories.UserRepository;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin")
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class AdministratorIntegrityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testPageAdmin() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/only_for_admins"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("admin"))
                .andExpect(xpath("/html/body/div/div[2]/div/div/div/table/tbody/tr")
                        //3 пользователя в БД
                        .nodeCount(3));
    }

}
