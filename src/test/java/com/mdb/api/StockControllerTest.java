package com.mdb.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mdb.api.controller.StockController;
import com.mdb.api.model.StockSymbol;
import com.mdb.api.repository.StockSymbolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StockController.class)
@ExtendWith(MockitoExtension.class)
public class StockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockSymbolRepository stockSymbolRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllStocks() throws Exception {
        List<StockSymbol> stocks = Arrays.asList(new StockSymbol("1", "AAPL", "Apple Inc.", 300.00f));
        given(stockSymbolRepository.findAll()).willReturn(stocks);

        mockMvc.perform(get("/api/stocks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].symbol").value("AAPL"));
    }

    @Test
    public void testGetStockBySymbol() throws Exception {
        StockSymbol stock = new StockSymbol("1", "AAPL", "Apple Inc.", 300.00f);
        given(stockSymbolRepository.findOneBySymbol("AAPL")).willReturn(stock);

        mockMvc.perform(get("/api/stock/AAPL")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value("AAPL"));
    }

    @Test
    public void testAddNewStock() throws Exception {
        StockSymbol newStock = new StockSymbol(null, "GOOGL", "Google LLC", 1500.00f);
        StockSymbol savedStock = new StockSymbol("2", "GOOGL", "Google LLC", 1500.00f);
        given(stockSymbolRepository.save(any(StockSymbol.class))).willReturn(savedStock);

        mockMvc.perform(post("/api/stock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStock)))
                .andExpect(status().isCreated()) // Expect 201 Created status
                .andExpect(jsonPath("$.id").value("2"));
    }


    @Test
    public void testDeleteStock() throws Exception {
        mockMvc.perform(delete("/employees/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
