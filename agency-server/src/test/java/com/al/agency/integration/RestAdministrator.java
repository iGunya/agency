package com.al.agency.integration;

import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.TransportMessage;
import com.al.agency.entities.User;
import com.al.agency.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.matchers.JUnitMatchers.containsString;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithUserDetails("admin")
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
@Transactional
public class RestAdministrator {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testUpdateUserOnAdmin() throws Exception {
        User user = new User(2L, "manager1","","ROLE_USER");
        mockMvc.perform(post("/only_for_admins/grand")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Пользователь обновлен"));

        User updateUserFromDB = userRepository.findByLogin("manager1");

        Assertions.assertNotNull(updateUserFromDB);
        Assertions.assertEquals("ROLE_USER", updateUserFromDB.getRole());
    }

    @Test
    public void testBadUpdateUserOnAdmin() throws Exception {
        User user = new User(2L, "admin","","ROLE_USER");

        mockMvc.perform(post("/only_for_admins/grand")
                .content(objectMapper.writeValueAsString(user))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Пользователь существует"));
    }
}
