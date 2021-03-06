package com.al.agency.unit.controller;

import com.al.agency.configs.jwt.AuthEntryPointJwt;
import com.al.agency.configs.jwt.JwtUtils;
import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.TransportMessage;
import com.al.agency.entities.Buyer;
import com.al.agency.controllers.ClientController;
import com.al.agency.services.AWSS3ServiceImp;
import com.al.agency.services.BuyerService;
import com.al.agency.services.SellerService;
import com.al.agency.services.UserService;
import org.junit.Ignore;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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

@WebMvcTest(ClientController.class)
public class ClientControllerBuyerTest {
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

    @MockBean
    private Transport transportSend;

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
    @Disabled
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAddPostBuyer() throws Exception {
        Mockito.when(awss3Service.uploadFile(Mockito.any())).thenReturn(Collections.singletonList("Имя файла"));

        Buyer buyer = new Buyer();
        buyer.setId_buyer(1L);
        Mockito.when(buyerService.saveContractAndBuyer(Mockito.any(), Mockito.any())).thenReturn(buyer);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/buyers/add")
                .file(file)
                .param("fio", "Иван Иванович")
                .param("phone","89005034876")
                .param("passport","1234567890"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/clients/buyers"));

        Mockito.verify(awss3Service,
                Mockito.times(1)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(buyerService,
                Mockito.times(0)).getBuyerById(Mockito.anyLong());
        Mockito.verify(buyerService,
                Mockito.times(1)).saveContractAndBuyer(Mockito.any(Buyer.class),Mockito.anyString());
    }

    @Test
    @Disabled //без AWS
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAddContractForBuyer() throws Exception {
        Mockito.when(awss3Service.uploadFile(Mockito.any())).thenReturn(Collections.singletonList("Имя файла"));
        Mockito.when(buyerService.getBuyerById(Mockito.anyLong())).thenReturn(new Buyer());

        Buyer buyer = new Buyer();
        buyer.setId_buyer(1L);
        Mockito.when(buyerService.saveContractAndBuyer(Mockito.any(), Mockito.any())).thenReturn(buyer);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/buyers/add")
                .file(file)
                .param("id_buyer", "1")
                .param("fio", "Иван Иванович"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/managers/clients/buyers"));

        Mockito.verify(awss3Service,
                Mockito.times(1)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(buyerService,
                Mockito.times(1)).saveContractAndBuyer(Mockito.any(Buyer.class),Mockito.anyString());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testValidationBuyer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/buyers/add")
                .file(file)
                .param("fio", "Иван Иванович")
                .param("passport", "1547")
                .param("phone","asd"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("add-buyer"))
                //ошибки формат ввода паспорта и телефона
                .andExpect(model().errorCount(2));

        Mockito.verify(awss3Service,
                Mockito.times(0)).uploadFile(new MockMultipartFile[]{file});
        Mockito.verify(buyerService,
                Mockito.times(0)).getBuyerById(Mockito.anyLong());
        Mockito.verify(buyerService,
                Mockito.times(0)).saveContractAndBuyer(Mockito.any(Buyer.class),Mockito.anyString());
    }
}
