package com.example.agency.unit.controller;

import com.example.agency.configs.jwt.AuthEntryPointJwt;
import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.controllers.ApiController;
import com.example.agency.dto.LoginRequest;
import com.example.agency.dto.ObjectDto;
import com.example.agency.dto.UserDetailsImpl;
import com.example.agency.entities.Object;
import com.example.agency.entities.Photo;
import com.example.agency.services.ObjectService;
import com.example.agency.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiController.class)
public class ApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ObjectService objectService;
    @MockBean
    private UserService userService;
    @MockBean
    AuthenticationManager authenticationManager;
    @MockBean
    JwtUtils jwtUtils;
    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @Test
    @WithMockUser
    public void testGetObjectById() throws Exception {
        ObjectDto objectDto = new ObjectDto();

        Mockito.when(objectService.getObjectDtoById(Mockito.anyLong())).thenReturn(objectDto);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/objects/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testGetObjectByIdPhotos() throws Exception {
        Object object = new Object();
        Photo photo1 = new Photo();
        Photo photo2 = new Photo();
        photo1.setURL_photo("URL_1");
        photo2.setURL_photo("URL_2");
        object.getPhotos().addAll(Arrays.asList(photo1,photo2));

        Mockito.when(objectService.getObjectById(Mockito.anyLong())).thenReturn(object);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/objects/1/photos"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(photo1, photo2))));
    }

    @Test
    @WithMockUser(username = "forTest")
    public void testGetLikeObjectByAuthUser() throws Exception {
        ObjectDto objectDto = new ObjectDto();

        Mockito.when(userService.getLikeObjectDtoByUsername("forTest")).thenReturn(Arrays.asList(objectDto));

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/like"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(objectDto))));
    }

    @Test
    @WithMockUser(username = "forTest")
    public void testSuccessfulAddLikeObjectByAuthUser() throws Exception {
        Mockito.when(userService.addLikeObject(1L,"forTest")).thenReturn(true);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/like/add/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Запись добавленна"));
    }

    @Test
    @WithMockUser(username = "forTest")
    public void testBadAddLikeObjectByAuthUser() throws Exception {
        Mockito.when(userService.addLikeObject(1L,"forTest")).thenReturn(false);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/api/like/add/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$").value("Не найден объект"));
    }

    @Test
    public void testAuthenticationGetJWT() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        UserDetailsImpl userDetails = new UserDetailsImpl(1L,"forTest","100",
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        final String TEST_TOKEN="testJWT";

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn(TEST_TOKEN);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signin")
                        .content(objectMapper.writeValueAsString(new LoginRequest("user","100")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(TEST_TOKEN));
    }

}
