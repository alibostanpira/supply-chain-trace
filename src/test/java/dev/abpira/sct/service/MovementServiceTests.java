package dev.abpira.sct.service;

import dev.abpira.sct.dto.MovementRequestDTO;
import dev.abpira.sct.dto.MovementResponseDTO;
import dev.abpira.sct.exception.ImmutableMovementException;
import dev.abpira.sct.exception.InvalidMovementException;
import dev.abpira.sct.exception.ProductNotFoundException;
import dev.abpira.sct.model.Movement;
import dev.abpira.sct.model.Product;
import dev.abpira.sct.repository.MovementRepository;
import dev.abpira.sct.repository.ProductRepository;
import dev.abpira.sct.service.impl.MovementServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovementServiceTests {

    @Mock
    private MovementRepository movementRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private MovementServiceImpl movementService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .type("Phone")
                .manufacturingDate(LocalDateTime.now().minusDays(10))
                .origin("Factory A")
                .build();
    }

    @Test
    void logMovement_successful() {
        // Given
        MovementRequestDTO dto = MovementRequestDTO.builder()
                .productId(1L)
                .fromLocation("Factory A")
                .toLocation("Warehouse B")
                .movementTimestamp(LocalDateTime.now().minusDays(5))
                .build();

        Movement movement = Movement.builder()
                .id(100L)
                .fromLocation(dto.getFromLocation())
                .toLocation(dto.getToLocation())
                .movementTimestamp(dto.getMovementTimestamp())
                .product(product)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(movementRepository.existsByProductAndMovementTimestamp(product, dto.getMovementTimestamp()))
                .thenReturn(false);
        when(movementRepository.findTopByProductOrderByMovementTimestampDesc(product))
                .thenReturn(Optional.empty());
        when(movementRepository.save(any(Movement.class))).thenReturn(movement);

        // When
        MovementResponseDTO result = movementService.logMovement(dto);

        // Then
        assertNotNull(result);
        assertEquals("Factory A", result.getFromLocation());
        assertEquals("Warehouse B", result.getToLocation());
        verify(movementRepository, times(1)).save(any(Movement.class));
    }

    @Test
    void logMovement_productNotFound() {
        MovementRequestDTO dto = MovementRequestDTO.builder()
                .productId(99L)
                .fromLocation("A")
                .toLocation("B")
                .movementTimestamp(LocalDateTime.now())
                .build();

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> movementService.logMovement(dto));
    }

    @Test
    void logMovement_timestampBeforeManufactureDate_shouldThrow() {
        MovementRequestDTO dto = MovementRequestDTO.builder()
                .productId(1L)
                .fromLocation("A")
                .toLocation("B")
                .movementTimestamp(product.getManufacturingDate().minusDays(1))
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        assertThrows(InvalidMovementException.class, () -> movementService.logMovement(dto));
    }

    @Test
    void logMovement_duplicateTimestamp_shouldThrow() {
        LocalDateTime timestamp = LocalDateTime.now().minusDays(3);

        MovementRequestDTO dto = MovementRequestDTO.builder()
                .productId(1L)
                .fromLocation("A")
                .toLocation("B")
                .movementTimestamp(timestamp)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(movementRepository.existsByProductAndMovementTimestamp(product, timestamp)).thenReturn(true);

        assertThrows(ImmutableMovementException.class, () -> movementService.logMovement(dto));
    }

    @Test
    void logMovement_outOfOrder_shouldThrow() {
        LocalDateTime latestTimestamp = LocalDateTime.now().minusDays(1);

        MovementRequestDTO dto = MovementRequestDTO.builder()
                .productId(1L)
                .fromLocation("A")
                .toLocation("B")
                .movementTimestamp(latestTimestamp.minusDays(2)) // older than last
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(movementRepository.existsByProductAndMovementTimestamp(product, dto.getMovementTimestamp()))
                .thenReturn(false);
        when(movementRepository.findTopByProductOrderByMovementTimestampDesc(product))
                .thenReturn(Optional.of(Movement.builder().movementTimestamp(latestTimestamp).build()));

        assertThrows(InvalidMovementException.class, () -> movementService.logMovement(dto));
    }

    @Test
    void getMovementsByProductId_successful() {
        Movement movement = Movement.builder()
                .id(200L)
                .fromLocation("A")
                .toLocation("B")
                .movementTimestamp(LocalDateTime.now().minusDays(2))
                .product(product)
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(movementRepository.findByProductOrderByMovementTimestampAsc(product))
                .thenReturn(List.of(movement));

        var result = movementService.getMovementsByProductId(1L);

        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getFromLocation());
    }
}
