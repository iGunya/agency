package com.example.agency.integration;

import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WithUserDetails("user")
public class ApiIntegrityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    String TOKEN;

    @PostConstruct
    public void init(){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        "user",
                        "100"));

        TOKEN = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testAllApiObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/objects")
                .header("Authorization", TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
