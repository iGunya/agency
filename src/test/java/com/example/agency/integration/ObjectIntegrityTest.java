package com.example.agency.integration;

import com.amazonaws.services.s3.AmazonS3;
import com.example.agency.dto.InputObjectDto;
import org.junit.jupiter.api.Test;
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

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("manager")
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
public class ObjectIntegrityTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPageAllObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                .string("manager"))
                .andExpect(xpath("/html/body/div/div[2]/div/div/div")
                        .nodeCount(3));

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
    @MockBean
    private AmazonS3 amazonS3;

    @Test
    public void testPageAddObjectPostRequest() throws Exception{
        MockMultipartFile file = new MockMultipartFile(
                "fileName",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/objects/add")
                .file(file)
                .requestAttr("object",new InputObjectDto()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/objects"));
    }
}
