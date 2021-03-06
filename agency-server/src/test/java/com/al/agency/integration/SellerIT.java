package com.al.agency.integration;

import com.al.agency.configs.transport.Transport;
import com.al.agency.dto.kafka.TransportMessage;
import com.al.agency.entities.Seller;
import com.al.agency.repositories.ContractRepository;
import com.al.agency.repositories.SellerRepository;
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
public class SellerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private SellerRepository sellerRepository;
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
    public void testPageAllSellers() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/sellers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("manager"))
                //?????????????????????????????????? 5 ??????????????????
                .andExpect(xpath("//*[@id=\"accordionExample\"]/div")
                        .nodeCount(5));
    }

    @Test
    public void testContractSellers() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/sellers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("manager"))
                //?????????????????????????????????? 6 ????????????????????
                .andExpect(xpath("//*[@class=\"contract\"]")
                        .nodeCount(6));
    }

    @Test
    public void testOneContractForTwoSellers() throws Exception {
        final String FIND_NAME_FILE = "3. 2021-07-16T17:07:00.933521800_da.txt";

        mockMvc.perform(MockMvcRequestBuilders.get("/managers/clients/sellers"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().isOk())
                .andExpect(xpath("/html/body/div/div[1]/div/div[1]/h3")
                        .string("manager"))
                //?????????????????????????????????? 6 ????????????????????
                .andExpect(xpath("//*[@id=\"collapse1\"]/div[@class]/a")
                        .string(FIND_NAME_FILE))
                .andExpect(xpath("//*[@id=\"collapse2\"]/div[@class]/a")
                        .string(FIND_NAME_FILE));
    }

    @Test
    public void testAddNewContractAndSeller() throws Exception{

        long countSellerBeforeAdd = sellerRepository.count();
        long countContractBeforeAdd = contractRepository.count();

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/sellers/add")
                .file(file)
                .param("fio","?????? - ????")
                .param("phone","89425674219")
                .param("passport","4672486764"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        long countSellerAfterAdd = sellerRepository.count();
        long countContractAfterAdd = contractRepository.count();

        Assertions.assertEquals(1,countSellerAfterAdd-countSellerBeforeAdd);
        Assertions.assertEquals(1,countContractAfterAdd-countContractBeforeAdd);
    }

    @Test
    public void testAddContractForSellers() throws Exception{

        Seller sellerBefore = sellerRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(sellerBefore);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/managers/clients/sellers/add")
                .file(file)
                .param("id_seller","1")
                .param("fio","???????????????? ??????????????????"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection());

        Seller sellerAfter = sellerRepository.findById(1L).orElse(null);
        Assertions.assertNotNull(sellerBefore);

        Assertions.assertEquals(1,sellerAfter.getContractsSeller().size()-sellerBefore.getContractsSeller().size());
    }
}
