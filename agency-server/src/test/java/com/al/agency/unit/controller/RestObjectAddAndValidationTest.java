package com.al.agency.unit.controller;

import com.al.agency.configs.jwt.AuthEntryPointJwt;
import com.al.agency.configs.jwt.JwtUtils;
import com.al.agency.dto.ObjectDto;
import com.al.agency.controllers.RestObjectController;
import com.al.agency.dto.kafka.KafkaMessage;
import com.al.agency.entities.Object;
import com.al.agency.services.AWSS3ServiceImp;
import com.al.agency.services.ObjectService;
import com.al.agency.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestObjectController.class)
public class RestObjectAddAndValidationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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

    @MockBean
    private KafkaTemplate<String, KafkaMessage> kafkaTemplate;

    private static MockMultipartFile file;

    @BeforeAll
    public static void init(){
        file = new MockMultipartFile(
                "fileName[]",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    @BeforeEach
    public void initReturn(){
        Mockito.when(awsService.uploadFile(Mockito.any())).thenReturn(Collections.singletonList("Имя файла"));
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAddPostCorrectObject() throws Exception{
        ObjectDto objectDto = new ObjectDto();
        objectDto.setAdress("Район Улица Дом");
        objectDto.setSquare("-1/34/5");
        objectDto.setCountRoom(1);
        objectDto.setCountFloor(1);
        objectDto.setPrice("1500000");
        objectDto.setRealPrice("1300000");
        objectDto.setTypeObject("Квартира");
        objectDto.setTypeMove("Продажа");

        MockMultipartFile jsonFile = new MockMultipartFile("object", "",
                "application/json", objectMapper.writeValueAsBytes(objectDto));

        Object object = new Object();
        object.setIdObject(1L);
        Mockito.when(objectService.createObjectAndSavePhotos(Mockito.any(), Mockito.any())).thenReturn(object);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/objects/add")
                .file(file)
                .file(jsonFile))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Объект добавлен"));

        Mockito.verify(awsService,
                Mockito.times(1)).uploadFile(Mockito.any());
        Mockito.verify(objectService,
                Mockito.times(1)).createObjectAndSavePhotos(Mockito.any(),Mockito.any());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testValidationInputEmptyObject() throws Exception{
        ObjectDto objectDto = new ObjectDto();
        objectDto.setAdress("");
        objectDto.setPrice("");
        objectDto.setRealPrice("");

        MockMultipartFile jsonFile = new MockMultipartFile("object", "",
                "application/json", objectMapper.writeValueAsBytes(objectDto));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/objects/add")
                    .file(file)
                    .file(jsonFile))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5));

        Mockito.verify(awsService,
                Mockito.times(0)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(objectService,
                Mockito.times(0)).createObjectAndSavePhotos(Mockito.any(),Mockito.any());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testValidationInputBadFieldObject() throws Exception{
        ObjectDto objectDto = new ObjectDto();
        objectDto.setAdress("Район Улица Дом");
        objectDto.setSquare("-1/3b/5");
        objectDto.setCountRoom(-1);
        objectDto.setCountFloor(1);
        objectDto.setPrice("150dfdfdf00");
        objectDto.setRealPrice("1300000");

        MockMultipartFile jsonFile = new MockMultipartFile("object", "",
                "application/json", objectMapper.writeValueAsBytes(objectDto));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/objects/add")
                .file(file)
                //ошибки формата ввода
                .file(jsonFile))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
        Mockito.verify(awsService,
                Mockito.times(0)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(objectService,
                Mockito.times(0)).createObjectAndSavePhotos(Mockito.any(),Mockito.any());
    }
}
