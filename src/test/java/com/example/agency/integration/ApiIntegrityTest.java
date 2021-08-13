package com.example.agency.integration;

import com.example.agency.configs.jwt.JwtUtils;
import com.example.agency.dto.LoginRequest;
import com.example.agency.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(classes = {TestConfig.class})
@TestPropertySource(locations = "classpath:application-test.properties")
@WithUserDetails("user")
@Transactional
public class ApiIntegrityTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    String TOKEN;

    @PostConstruct
    public void init(){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        "user",
                        "100"));

        TOKEN = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    public void testAllApiObject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/objects")
                .header("Authorization", TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(6));
    }

    @Test
    public void testApiObjectById() throws Exception{
        final Long idObject = 2L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/objects/{id}", idObject)
                .header("Authorization", TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.adress").value("Где-то"))
                .andExpect(jsonPath("$.typeMove").value("Продажа"))
                .andExpect(jsonPath("$.typeObject").value("Участок"));
    }

    @Test
    public void testApiGetPhotosObjectById() throws Exception{
        final Long idObject = 1L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/objects/{id}/photos", idObject)
                .header("Authorization", TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @WithUserDetails("admin")
    public void testApiGetLikeObjectByPrincipalNameAdmin() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/like")
                .header("Authorization", TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @WithUserDetails("user")
    public void testApiGetLikeObjectByPrincipalNameUser() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/like")
                .header("Authorization", TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    @WithUserDetails("user")
    public void testSuccessApiAddLikeObjectByPrincipalNameUser() throws Exception{
        final Long idAddObject = 6L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/like/add/{id_object}", idAddObject)
                .header("Authorization", TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Запись добавленна"));;
    }

    @Test
    @WithUserDetails("user")
    public void testBadApiAddLikeObjectByPrincipalNameUser() throws Exception{
        final Long idAddObject = 100L;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/like/add/{id_object}", idAddObject)
                .header("Authorization", TOKEN))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value("Не найден объект"));;
    }

    @Test
    public void testAuthorization() throws Exception{
        final LoginRequest dataAuthorization = new LoginRequest("user","100");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signin")
                        .content(objectMapper.writeValueAsString(dataAuthorization))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString());
    }

    @Test
    public void testBadAuthorization() throws Exception{
        final LoginRequest dataAuthorization = new LoginRequest("user123","100");

        mockMvc.perform(
                MockMvcRequestBuilders.post("/api/auth/signin")
                        .content(objectMapper.writeValueAsString(dataAuthorization))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").value("Bad credentials"));
    }
}
