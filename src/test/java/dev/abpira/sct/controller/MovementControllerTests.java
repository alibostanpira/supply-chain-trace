package dev.abpira.sct.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abpira.sct.dto.MovementRequestDTO;
import dev.abpira.sct.dto.MovementResponseDTO;
import dev.abpira.sct.service.interfaces.MovementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovementControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private MovementService movementService;

    @Test
    @WithMockUser(roles = "LOGISTICS")
    void testLogMovement() throws Exception {
        MovementRequestDTO request = new MovementRequestDTO(
                1L, "Warehouse", "Store", LocalDateTime.now()
        );

        MovementResponseDTO response = new MovementResponseDTO(
                "Warehouse", "Store", request.getMovementTimestamp()
        );

        given(movementService.logMovement(any(MovementRequestDTO.class)))
                .willReturn(response);

        mockMvc.perform(post("/api/movement")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fromLocation").value("Warehouse"))
                .andExpect(jsonPath("$.toLocation").value("Store"))
                .andExpect(jsonPath("$.movementTimestamp").exists());
    }

    @Test
    @WithMockUser(roles = "AUDITOR")
    void testGetMovements() throws Exception {
        Long productId = 1L;
        MovementResponseDTO response = new MovementResponseDTO(
                "Warehouse", "Store", LocalDateTime.now()
        );

        given(movementService.getMovementsByProductId(productId))
                .willReturn(List.of(response));

        mockMvc.perform(get("/api/movement/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fromLocation").value("Warehouse"))
                .andExpect(jsonPath("$[0].toLocation").value("Store"))
                .andExpect(jsonPath("$[0].movementTimestamp").exists());
    }
}
