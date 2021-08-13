package com.example.agency.integration;

import com.amazonaws.services.s3.AmazonS3;
import com.example.agency.repositories.ObjectRepository;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("manager")
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ContextConfiguration(classes = {TestConfig.class})
public class ObjectIntegrityTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectRepository objectRepository;

    @Test
    @Order(1)
    public void testPageAllObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                .string("manager"))
                //в блоке 6 объектов инициализированно
                .andExpect(xpath("/html/body/div/div[2]/div/div/div")
                        .nodeCount(6));
    }

    @Test
    public void testPageAddObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("manager"))
                //значения для селектора из БД
                .andExpect(xpath("//*[@id=\"typeObject\"]/option[1]")
                        .string("Дом"));

    }

    @Test
    @Order(2)
    public void testFilterCountRoomObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects")
                .param("countRoom","2"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("manager"))
                //значения для селектора из БД
                .andExpect(model().attributeExists("objects"))
                .andExpect(xpath("/html/body/div/div[2]/div/div/div")
                        .nodeCount(3));

    }

    @Test
    public void testFilterMaxMinPriceObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects")
                .param("minPrice","500000")
                .param("maxPrice","3000000"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("manager"))
                //значения для селектора из БД
                .andExpect(model().attributeExists("objects"))
                .andExpect(xpath("/html/body/div/div[2]/div/div/div")
                        .nodeCount(3));

    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testDeleteObject() throws Exception{
        long countObjectBeforeDelete = objectRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/objects"));

        long countObjectAfterDelete = objectRepository.count();

        Assertions.assertEquals(1,countObjectBeforeDelete-countObjectAfterDelete);
    }


    @Test
    public void testPageAddObjectPostRequest() throws Exception{
        MockMultipartFile file = new MockMultipartFile(
                "fileName",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
        long countObjectBeforeAdd = objectRepository.count();

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

        long countObjectAfterAdd = objectRepository.count();

        Assertions.assertEquals(1,countObjectAfterAdd-countObjectBeforeAdd);
    }
}
