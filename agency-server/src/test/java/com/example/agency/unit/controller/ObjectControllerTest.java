package com.example.agency.unit.controller;

import com.example.agency.configs.jwt.AuthEntryPointJwt;
import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.controllers.ObjectController;
import com.example.agency.services.AWSS3ServiceImp;
import com.example.agency.services.ObjectService;
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

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ObjectController.class)
public class ObjectControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AWSS3ServiceImp awsService;

    @MockBean
    private ObjectService objectService;

    //для аунтификации
    @MockBean
    private UserService userService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testForbiddenPageAllObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAddObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk());
        Mockito.verify(objectService,
                Mockito.times(1)).getAllTypeObject();
        Mockito.verify(objectService,
                Mockito.times(1)).getAllTypeMove();
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAllObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Mockito.verify(objectService,
                Mockito.times(1)).getAllTypeObject();
        Mockito.verify(objectService,
                Mockito.times(1)).getAllTypeMove();
    }

}
