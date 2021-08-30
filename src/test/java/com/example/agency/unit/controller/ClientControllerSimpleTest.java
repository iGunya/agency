package com.example.agency.unit.controller;

import com.example.agency.configs.jwt.AuthEntryPointJwt;
import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.controllers.ClientController;
import com.example.agency.services.AWSS3ServiceImp;
import com.example.agency.services.BuyerService;
import com.example.agency.services.SellerService;
import com.example.agency.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerSimpleTest {
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

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAllSeller() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/sellers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Mockito.verify(sellerService,
                Mockito.times(1)).getSellerWithPaginationAndFilter(Mockito.any());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAllBuyer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/buyers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk());

        Mockito.verify(buyerService,
                Mockito.times(1)).getBuyersWithFilter(Mockito.any());
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAddSeller() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/sellers/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("add-seller"))
                .andExpect(model().size(2));
    }

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testPageAddBuyer() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/buyers/add"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(view().name("add-buyer"))
                .andExpect(model().size(2));
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testForbiddenNestingBuyerForAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/buyers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user",roles = {"USER"})
    public void testForbiddenNestingBuyerForUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/buyers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin",roles = {"ADMIN"})
    public void testForbiddenNestingSellerForAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/sellers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "user",roles = {"USER"})
    public void testForbiddenNestingSellerForUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/sellers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isForbidden());
    }
}
