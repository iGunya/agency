package com.example.agency.integration;

import com.example.agency.entities.Object;
import com.example.agency.entities.User;
import com.example.agency.repositories.ObjectRepository;
import com.example.agency.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
@WithUserDetails("manager")
@Transactional
public class TestCascadeDeleteObject {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectRepository objectRepository;

    @Test
    @WithMockUser(username = "manager",roles = {"MANAGER"})
    public void testDeleteObjectFromLikeUser() throws Exception{
        final long ID_DELETE_OBJECT = 1L;

        List<User> UsersWithLikeDeleteObject = userRepository
                .findUserByObjects_IdObject(ID_DELETE_OBJECT);

        mockMvc.perform(MockMvcRequestBuilders.get("/managers/objects/delete/1"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Ок"));

        List<User> UsersWithLikeDeleteObjectAfterDelete = userRepository
                .findUserByObjects_IdObject(ID_DELETE_OBJECT);

        Assertions.assertEquals(2,UsersWithLikeDeleteObject.size());
        Assertions.assertEquals(0,UsersWithLikeDeleteObjectAfterDelete.size());
    }
}
