package com.al.agency.integration;

import com.al.agency.dto.ObjectDto;
import com.al.agency.repositories.ObjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithUserDetails("manager")
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class RestObjectControllerIntegrityTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectRepository objectRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @Order(1)
    public void testFilterCountRoomObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/filter")
                .param("countRoom","2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    @Order(4)
    public void testDeleteObject() throws Exception{
        long countObjectBeforeDelete = objectRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Ок"));

        long countObjectAfterDelete = objectRepository.count();

        Assertions.assertEquals(1,countObjectBeforeDelete-countObjectAfterDelete);
    }
    @Test
    @Order(2)
    public void testFilterMaxMinPriceObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/filter")
                .param("minPrice","500000")
                .param("maxPrice","3000000"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));

    }


    @Test
    @Order(3)
    public void testPageAddObjectPostRequest() throws Exception{
        MockMultipartFile file = new MockMultipartFile(
                "fileName[]",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

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

        long countObjectBeforeAdd = objectRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/objects/add")
                .file(file)
                .file(jsonFile))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Объект добавлен"));

        long countObjectAfterAdd = objectRepository.count();

        Assertions.assertEquals(1,countObjectAfterAdd-countObjectBeforeAdd);
    }
}
