package com.al.agency.integration;

import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.TransportMessage;
import com.al.agency.entities.User;
import com.al.agency.repositories.ObjectRepository;
import com.al.agency.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
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
public class CascadeDeleteObject {
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
