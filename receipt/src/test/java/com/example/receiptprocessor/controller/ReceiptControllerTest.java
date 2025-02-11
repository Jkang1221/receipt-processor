package com.example.receiptprocessor.controller;

import com.example.receiptprocessor.model.Receipt;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReceiptControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String receiptJson;
    private String receiptId;

    @BeforeEach
    public void setUp() throws Exception {
        receiptJson = """
        {
          "retailer": "Target",
          "purchaseDate": "2022-01-01",
          "purchaseTime": "13:01",
          "items": [
            { "shortDescription": "Mountain Dew 12PK", "price": "6.49" },
            { "shortDescription": "Emils Cheese Pizza", "price": "12.25" },
            { "shortDescription": "Knorr Creamy Chicken", "price": "1.26" },
            { "shortDescription": "Doritos Nacho Cheese", "price": "3.35" },
            { "shortDescription": "   Klarbrunn 12-PK 12 FL OZ  ", "price": "12.00" }
          ],
          "total": "35.35"
        }
        """;

        // Process receipt before each test to get a valid ID
        MvcResult result = mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(receiptJson))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        Map<String, String> responseMap = objectMapper.readValue(jsonResponse, Map.class);
        receiptId = responseMap.get("id");
    }

    /**
     * successful receipt processing
     */
    @Test
    public void testProcessReceipt_ShouldReturnId() throws Exception {
        mockMvc.perform(post("/receipts/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(receiptJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());  // Verify 'id' exists
    }

    /**
     * retrieve points using a valid ID
     */
    @Test
    public void testGetPoints_ShouldReturn28Points() throws Exception {
        mockMvc.perform(get("/receipts/" + receiptId + "/points"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.points", is(28)));  // Verify total points = 28
    }

    /**
     * retrieve points with an invalid ID
     */
    @Test
    public void testGetPoints_InvalidId_ShouldReturn404() throws Exception {
        String invalidId = "non-existent-id-123";

        mockMvc.perform(get("/receipts/" + invalidId + "/points"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Receipt not found"));  // Custom error message
    }
}
