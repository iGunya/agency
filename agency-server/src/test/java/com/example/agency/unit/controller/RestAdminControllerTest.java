package com.example.agency.unit.controller;

import com.example.agency.configs.jwt.AuthEntryPointJwt;
import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.controllers.RestAdminController;
import com.example.agency.entities.User;
import com.example.agency.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestAdminController.class)
public class RestAdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testUpdateRoleAndLoginRequestPageAdmin() throws Exception{
        User user = new User();

        Mockito.when(userService.chekUserByUsername(Mockito.any())).thenReturn(null);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/only_for_admins/grand")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Пользователь обновлен"));

        Mockito.verify(userService,
                Mockito.times(1)).updateRoleAndLogin(Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testUpdateRoleRequestPageAdmin() throws Exception{
        User user = new User();
        user.setId_user(2L);

        Mockito.when(userService.chekUserByUsername(Mockito.any())).thenReturn(user);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/only_for_admins/grand")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Пользователь обновлен"));

        Mockito.verify(userService,
                Mockito.times(1)).updateRoleAndLogin(Mockito.any());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testUpdateRoleAndNotUniqueLoginRequestPageAdmin() throws Exception{
        User user = new User();
        user.setId_user(2L);

        User userFromDB = new User();
        userFromDB.setId_user(1L);

        Mockito.when(userService.chekUserByUsername(Mockito.any())).thenReturn(userFromDB);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/only_for_admins/grand")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Пользователь существует"));

        Mockito.verify(userService,
                Mockito.times(0)).updateRoleAndLogin(Mockito.any());
    }
}
