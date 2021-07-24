package com.example.agency.unit.controller;

import com.example.agency.controllers.ObjectController;
import com.example.agency.dto.InputObjectDto;
import com.example.agency.entities.User;
import com.example.agency.services.AWSS3ServiceImp;
import com.example.agency.services.ObjectService;
import com.example.agency.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ObjectController.class)
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

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAllObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Mockito.verify(objectService,
                Mockito.times(1)).getObjectWithPaginationAndFilter(Mockito.any());
    }

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
                Mockito.times(1)).allTypeObject();
        Mockito.verify(objectService,
                Mockito.times(1)).allTypeObject();
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAddPostObject() throws Exception{
        MockMultipartFile file = new MockMultipartFile(
                "fileName",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        Mockito.when(awsService.uploadFile(Mockito.any())).thenReturn(Collections.singletonList("Имя файла"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/objects/add")
                .file(file)
                .param("adress","Район Улица Дом")
                .param("square","-1/34/5")
                .param("countRoom","1")
                .param("countFloor","1")
                .param("price","1500000")
                .param("realPrice","1300000")
                .param("description","asdas")
                .param("typeObject","Квартира")
                .param("typeMove","Продажа"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/objects"));

        Mockito.verify(awsService,
                Mockito.times(1)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(objectService,
                Mockito.times(1)).createObject(Mockito.any(),Mockito.any());
    }

}
