package com.al.agency.integration;

import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.TransportMessage;
import com.al.agency.entities.Buyer;
import com.al.agency.repositories.BuyerRepository;
import com.al.agency.repositories.ContractRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@ContextConfiguration(classes = {TestConfig.class})
@WithUserDetails("manager")
public class BuyerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private ContractRepository contractRepository;


    private static MockMultipartFile file;
    @BeforeAll
    private static void initFile(){
        file = new MockMultipartFile(
                "fileName",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello, World!".getBytes()
        );
    }

    @Test
    public void testPageAllBuyers() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/buyers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("manager"))
                //в блоке 6 объектов инициализированно
                .andExpect(xpath("//*[@id=\"accordionExample\"]/div")
                        .nodeCount(4));
    }

    @Test
    public void testContractBuyers() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/buyers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("manager"))
                //в каждом class="card-body" ссыка на контракт
                .andExpect(xpath("//*[@class=\"card-body\"]")
                        .nodeCount(5));
    }

    @Test
    public void testAddNewContractAndBuyer() throws Exception{

        long countBuyerBeforeAdd = buyerRepository.count();
        long countContractBeforeAdd = contractRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/buyers/add")
                .file(file)
                .param("fio","Кто - То")
                .param("phone","89425674219")
                .param("passport","4672486764")
                .param("description","Хочу"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        long countBuyerAfterAdd = buyerRepository.count();
        long countContractAfterAdd = contractRepository.count();

        Assertions.assertEquals(1,countBuyerAfterAdd-countBuyerBeforeAdd);
        Assertions.assertEquals(1,countContractAfterAdd-countContractBeforeAdd);
    }

    @Test
    public void testAddContractForBuyers() throws Exception{

        Buyer buyerBefore = buyerRepository.findById(2L).orElse(null);
        Assertions.assertNotNull(buyerBefore);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/buyers/add")
                .file(file)
                .param("id_buyer","2")
                .param("fio","Матвей Николаевич"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        Buyer buyerAfter = buyerRepository.findById(2L).orElse(null);
        Assertions.assertNotNull(buyerBefore);

        Assertions.assertEquals(1,buyerAfter.getContracts().size()-buyerBefore.getContracts().size());
    }
}
