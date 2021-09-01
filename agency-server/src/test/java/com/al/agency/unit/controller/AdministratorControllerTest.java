package com.al.agency.unit.controller;

import com.al.agency.configs.jwt.AuthEntryPointJwt;
import com.al.agency.configs.jwt.JwtUtils;
import com.al.agency.entities.User;
import com.al.agency.controllers.AdimistratorController;
import com.al.agency.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdimistratorController.class)
//@AutoConfigureMockMvc(addFilters = false)
public class AdministratorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void setUp() {
////        mvc = MockMvcBuilders.standaloneSetup(new HandlerController()).build();
//        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//    }

    @MockBean
    private UserService userService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testPageAdmin() throws Exception{
        Mockito.when(userService.getAllUser()).thenReturn(Collections.singletonList(new User()));

        mockMvc.perform(MockMvcRequestBuilders.get("/only_for_admins"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
        .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3").string("admin"));

        Mockito.verify(userService,
                Mockito.times(1)).getAllUser();
    }

    @Test
    @WithMockUser(username = "user",roles = {"USER"})
    public void testForbiddenPageAdmin() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/only_for_admins"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

}
