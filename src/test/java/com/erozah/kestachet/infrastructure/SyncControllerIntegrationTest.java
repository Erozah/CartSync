package com.erozah.kestachet.infrastructure;

import com.erozah.kestachet.domain.model.ShoppingList;
import com.erozah.kestachet.domain.port.out.ShoppingListRepositoryPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SyncControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ShoppingListRepositoryPort repositoryPort;

    @Test
    void shouldAcceptSyncBatchAndApplyOperations() throws Exception {
        ShoppingList list = new ShoppingList("list-test-sync", "Courses Soir", "user-123", BigDecimal.valueOf(80.0));
        repositoryPort.save(list);

        String batchJson = """
        {
            "operations": [
                {
                    "operationId": "op-501",
                    "listId": "list-test-sync",
                    "userId": "user-123",
                    "type": "ADD_ITEM",
                    "itemId": "item-501",
                    "gtin": "3017620422003",
                    "itemName": "Nutella",
                    "quantity": 1,
                    "price": 3.99,
                    "timestamp": "2026-09-08T22:00:00Z"
                }
            ]
        }
        """;

        mockMvc.perform(post("/api/sync")
                .contentType(MediaType.APPLICATION_JSON)
                .content(batchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.appliedCount").value(1))
                .andExpect(jsonPath("$.duplicateCount").value(0));
    }
}
