package com.al.agency.unit.controller;

import com.al.agency.configs.jwt.AuthEntryPointJwt;
import com.al.agency.configs.jwt.JwtUtils;
import com.al.agency.configs.transport.Transport;
import com.al.agency.controllers.RestObjectController;
import com.al.agency.dto.kafka.TransportMessage;
import com.al.agency.entities.Object;
import com.al.agency.services.AWSS3ServiceImp;
import com.al.agency.services.ObjectService;
import com.al.agency.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestObjectController.class)
public class RestObjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ObjectService objectService;
    @MockBean
    private AWSS3ServiceImp awsService;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;
    @MockBean
    private JwtUtils jwtUtils;
    @MockBean
    private Transport transportSend;

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testDeleteObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Ок"));
        Mockito.verify(objectService,
                Mockito.times(1)).deleteObjectById(Mockito.anyLong());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testEmptyFilterObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/filter"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
        Mockito.verify(objectService,
                Mockito.times(1))
                .createSpecificationForObjects(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testFilterObject() throws Exception{
        Mockito.when(objectService.getObjectsWithFilter(Mockito.any())).thenReturn(Collections.singletonList(new Object()));
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/filter")
                    .param("countRoom", "3"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
        Mockito.verify(objectService,
                Mockito.times(1))
                .createSpecificationForObjects(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());
    }
}
