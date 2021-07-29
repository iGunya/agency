package com.example.agency.unit.controller;

import com.example.agency.configs.jwt.AuthEntryPointJwt;
import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.controllers.ClientController;
import com.example.agency.entities.Seller;
import com.example.agency.services.AWSS3ServiceImp;
import com.example.agency.services.BuyerService;
import com.example.agency.services.SellerService;
import com.example.agency.services.UserService;
import org.junit.jupiter.api.BeforeAll;
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

import java.util.Collections;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@WebMvcTest(ClientController.class)
public class ClientControllerSellerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private SellerService sellerService;
    @MockBean
    private BuyerService buyerService;
    @MockBean
    private AWSS3ServiceImp awss3Service;
    //для аунтификации
    @MockBean
    private UserService userService;

    @MockBean
    private AuthEntryPointJwt authEntryPointJwt;

    @MockBean
    private JwtUtils jwtUtils;

    private static MockMultipartFile file;
    @BeforeAll
    public static void init(){
        file = new MockMultipartFile(
                "fileName",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAddPostSeller() throws Exception {
        Mockito.when(awss3Service.uploadFile(Mockito.any())).thenReturn(Collections.singletonList("Имя файла"));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/sellers/add")
                .file(file)
                .param("fio", "Иван Иванович")
                .param("phone","89005034876")
                .param("passport","1234567890"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/clients/sellers"));

        Mockito.verify(awss3Service,
                Mockito.times(1)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(sellerService,
                Mockito.times(0)).findById(Mockito.anyLong());
        Mockito.verify(sellerService,
                Mockito.times(1)).saveContractAndSeller(Mockito.any(Seller.class),Mockito.anyString());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageUpdatePostSeller() throws Exception {
        Mockito.when(awss3Service.uploadFile(Mockito.any())).thenReturn(Collections.singletonList("Имя файла"));
        Mockito.when(sellerService.findById(Mockito.anyLong())).thenReturn(new Seller());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/sellers/add")
                .file(file)
                .param("id_seller", "1")
                .param("fio", "Иван Иванович"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/clients/sellers"));

        Mockito.verify(awss3Service,
                Mockito.times(1)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(sellerService,
                Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(sellerService,
                Mockito.times(1)).saveContractAndSeller(Mockito.any(Seller.class),Mockito.anyString());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testValidationSeller() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/sellers/add")
                .file(file)
                .param("fio", "Иван Иванович")
                .param("passport", "1547")
                .param("phone","asd"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("add-seller"))
                //ошибки формат ввода паспорта и телефона
                .andExpect(model().errorCount(2));

        Mockito.verify(awss3Service,
                Mockito.times(0)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(sellerService,
                Mockito.times(0)).findById(Mockito.anyLong());
        Mockito.verify(sellerService,
                Mockito.times(0)).saveContractAndSeller(Mockito.any(Seller.class),Mockito.anyString());
    }
}
