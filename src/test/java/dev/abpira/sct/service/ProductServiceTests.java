package dev.abpira.sct.service;

import dev.abpira.sct.dto.ProductRequestDTO;
import dev.abpira.sct.dto.ProductResponseDTO;
import dev.abpira.sct.exception.ProductAlreadyExistsException;
import dev.abpira.sct.exception.ProductNotFoundException;
import dev.abpira.sct.mapper.ProductMapper;
import dev.abpira.sct.model.Product;
import dev.abpira.sct.repository.ProductRepository;
import dev.abpira.sct.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequestDTO requestDTO;
    private Product product;

    @BeforeEach
    void setUp() {

        requestDTO = ProductRequestDTO.builder()
                .type("Phone")
                .manufacturingDate(LocalDateTime.now().minusDays(1))
                .origin("Germany")
                .build();

        product = ProductMapper.mapToProduct(requestDTO);
        product.setId(1L);
    }

    @Test
    void registerProduct_ShouldSaveProduct_WhenNotExists() {
        when(productRepository.existsByTypeAndManufacturingDateAndOrigin(
                requestDTO.getType(),
                requestDTO.getManufacturingDate(),
                requestDTO.getOrigin()))
                .thenReturn(false);

        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponseDTO response = productService.registerProduct(requestDTO);

        assertNotNull(response);
        assertEquals("Phone", response.getType());
        assertEquals("Germany", response.getOrigin());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void registerProduct_ShouldThrowException_WhenAlreadyExists() {
        when(productRepository.existsByTypeAndManufacturingDateAndOrigin(
                requestDTO.getType(),
                requestDTO.getManufacturingDate(),
                requestDTO.getOrigin()))
                .thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class,
                () -> productService.registerProduct(requestDTO));

        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenExists() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponseDTO response = productService.getProductById(1L);

        assertNotNull(response);
        assertEquals("Phone", response.getType());
    }

    @Test
    void getProductById_ShouldThrowException_WhenNotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class,
                () -> productService.getProductById(99L));
    }

    @Test
    void getAllProducts_ShouldReturnListOfProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));

        List<ProductResponseDTO> responses = productService.getAllProducts();

        assertEquals(1, responses.size());
        assertEquals("Phone", responses.get(0).getType());
    }
}
