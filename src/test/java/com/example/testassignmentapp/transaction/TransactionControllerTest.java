package com.example.testassignmentapp.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    void testAddTransaction() throws Exception {
        TransactionDTO dto = new TransactionDTO(LocalDate.now(), "test description", BigDecimal.valueOf(1.3));
        ObjectMapper objectMapper = new JsonMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).addTransaction(any(TransactionDTO.class));
    }

    @Test
    void testGetTransactions() throws Exception {
        TransactionReportRequest request = new TransactionReportRequest(
                LocalDate.of(2022, 2, 2),
                LocalDate.of(2022, 3, 2),
                "USD"
        );
        List<TransactionDTO> expectedTransactions = Arrays.asList(
                new TransactionDTO(LocalDate.of(2022, 2, 4), "tr1", BigDecimal.valueOf(1.1)),
                new TransactionDTO(LocalDate.of(2022, 2, 14), "tr2", BigDecimal.valueOf(1.2))
        );
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(transactionService.getTransactionsInPeriodInCurrency(any(TransactionReportRequest.class)))
                .thenReturn(expectedTransactions);

        MvcResult mvcResult = mockMvc.perform(get("/api/v1/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        List<LinkedHashMap> lst = objectMapper.readValue(actualResponseBody, List.class);
        var actualTransactions = lst.stream().map(ow -> objectMapper.convertValue(ow, TransactionDTO.class)).toList();



        assertThat(actualTransactions).isEqualTo(expectedTransactions);
    }

    List<Map<String, String>> asMaps(List<TransactionDTO> transactions) {
        return transactions.stream().map(tr -> Map.of("date",tr.date().toString(),
                "description", tr.description(),
                "value", tr.value().toString()
                )).toList();
    }
}
