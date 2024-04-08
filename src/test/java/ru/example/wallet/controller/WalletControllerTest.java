package ru.example.wallet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.example.wallet.controller.request.OperationType;
import ru.example.wallet.controller.request.WalletRequest;
import ru.example.wallet.repository.WalletRepository;
import ru.example.wallet.service.WalletService;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class WalletControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean(name = "walletServiceMock")
    private WalletService walletServiceMock;

    private final String UUID_OK = "aaaaaaaa-bbbb-cccc-dddd-111111111111";
    private final String UUID_BAD = "aaaaaaaa-bbbb-cccc-dddd-000000000000";

    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        entityManager.createQuery("DELETE FROM Wallet").executeUpdate();
        entityManager.createNativeQuery("INSERT INTO wallet (id, amount) VALUES ('aaaaaaaa-bbbb-cccc-dddd-111111111111', 1000)").executeUpdate();
    }

    @Test
    void getWalletBalance_Ok() throws Exception {
        mockMvc.perform(get("/wallet/")
                    .param("walletId", UUID_OK))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(UUID_OK));
    }

    @Test
    void getWalletBalance_badRequest() throws Exception {
        mockMvc.perform(get("/wallet/")
                        .param("walletId", UUID_BAD))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Wallet not found")));
    }

    @Test
    void editWalletBalance_Ok() throws Exception {
        Double mySum = 10.0;
        ObjectMapper objectMapper = new ObjectMapper();
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setValletId(UUID.fromString(UUID_OK));
        walletRequest.setOperationType(OperationType.DEPOSIT);
        walletRequest.setAmount(mySum);

        String walletRequestJson = objectMapper.writeValueAsString(walletRequest);

        mockMvc.perform(post("/wallet/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletRequestJson))
                .andExpect(content().string(containsString("success")))
                .andExpect(status().isOk());
    }

    @Test
    void editWalletBalance_badRequest() throws Exception {
        Double mySum = 999999999999.0;
        ObjectMapper objectMapper = new ObjectMapper();
        WalletRequest walletRequest = new WalletRequest();
        walletRequest.setValletId(UUID.fromString(UUID_OK));
        walletRequest.setOperationType(OperationType.WITHDRAW);
        walletRequest.setAmount(mySum);

        String walletRequestJson = objectMapper.writeValueAsString(walletRequest);

        mockMvc.perform(post("/wallet/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Not enough funds in your wallet")));

        walletRequest.setAmount(-10.0);
        walletRequestJson = objectMapper.writeValueAsString(walletRequest);

        mockMvc.perform(post("/wallet/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(walletRequestJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("The amount must be greater than zero")));
    }
}