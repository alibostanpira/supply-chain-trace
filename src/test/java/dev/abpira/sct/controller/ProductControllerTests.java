package dev.abpira.sct.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.abpira.sct.dto.ProductRequestDTO;
import dev.abpira.sct.dto.ProductResponseDTO;
import dev.abpira.sct.service.interfaces.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "manufacturer", roles = {"MANUFACTURER"})
    void registerProduct_ShouldReturnCreated() throws Exception {
        ProductRequestDTO request = ProductRequestDTO.builder()
                .type("Phone")
                .manufacturingDate(LocalDateTime.now().minusDays(1))
                .origin("Germany")
                .build();

        ProductResponseDTO response = ProductResponseDTO.builder()
                .id(1L)
                .type("Phone")
                .manufacturingDate(request.getManufacturingDate())
                .origin("Germany")
                .build();

        when(productService.registerProduct(any(ProductRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/products")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.type", is("Phone")))
                .andExpect(jsonPath("$.origin", is("Germany")));
    }

    @Test
    @WithMockUser(username = "auditor", roles = {"AUDITOR"})
    void getProductById_ShouldReturnProduct() throws Exception {
        ProductResponseDTO response = ProductResponseDTO.builder()
                .id(1L)
                .type("Phone")
                .manufacturingDate(LocalDateTime.now().minusDays(1))
                .origin("Germany")
                .build();

        when(productService.getProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.type", is("Phone")));
    }

    @Test
    @WithMockUser(username = "auditor", roles = {"AUDITOR"})
    void getAllProducts_ShouldReturnList() throws Exception {
        ProductResponseDTO p1 = ProductResponseDTO.builder()
                .id(1L)
                .type("Phone")
                .manufacturingDate(LocalDateTime.now().minusDays(1))
                .origin("Germany")
                .build();

        ProductResponseDTO p2 = ProductResponseDTO.builder()
                .id(2L)
                .type("Laptop")
                .manufacturingDate(LocalDateTime.now().minusDays(2))
                .origin("USA")
                .build();

        when(productService.getAllProducts()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].type", is("Phone")))
                .andExpect(jsonPath("$[1].type", is("Laptop")));
    }

    @Test
    void unauthorizedAccess_ShouldReturnUnauthorized() throws Exception {
        // No @WithMockUser -> should fail security
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isUnauthorized());
    }
}
