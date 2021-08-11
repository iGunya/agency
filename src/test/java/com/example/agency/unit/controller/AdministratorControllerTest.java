package com.example.agency.unit.controller;

import com.example.agency.configs.jwt.AuthEntryPointJwt;
import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.controllers.AdimistratorController;
import com.example.agency.entities.User;
import com.example.agency.services.UserService;
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

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testPostRequestPageAdmin() throws Exception{
        User user = new User();

        mockMvc.perform(post("/only_for_admins")
                .requestAttr("user",user))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/only_for_admins"));

        Mockito.verify(userService,
                Mockito.times(1)).updateRoleAndLogin(user);
    }
}
