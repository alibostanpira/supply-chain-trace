package dev.abpira.sct.repository;

import dev.abpira.sct.model.Movement;
import dev.abpira.sct.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class MovementRepositoryTests {

    @Autowired
    private MovementRepository movementRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product testProduct;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        movementRepository.deleteAll();
        productRepository.deleteAll();

        // Create a test product
        testProduct = Product.builder()
                .type("Electronics")
                .manufacturingDate(LocalDateTime.of(2024, 1, 15, 10, 0))
                .origin("Factory A")
                .build();
        testProduct = productRepository.save(testProduct);
    }

    @Test
    void findByProductOrderByMovementTimestampAsc_ShouldReturnMovementsInAscendingOrder() {
        // Given
        Movement movement1 = createMovement(testProduct, "Factory A", "Warehouse X",
                LocalDateTime.of(2024, 1, 16, 10, 0));
        Movement movement2 = createMovement(testProduct, "Warehouse X", "Store Y",
                LocalDateTime.of(2024, 1, 17, 14, 30));
        Movement movement3 = createMovement(testProduct, "Store Y", "Customer",
                LocalDateTime.of(2024, 1, 18, 9, 15));

        // When
        List<Movement> movements = movementRepository.findByProductOrderByMovementTimestampAsc(testProduct);

        // Then
        assertThat(movements).hasSize(3);
        assertThat(movements.get(0).getMovementTimestamp()).isEqualTo(movement1.getMovementTimestamp());
        assertThat(movements.get(1).getMovementTimestamp()).isEqualTo(movement2.getMovementTimestamp());
        assertThat(movements.get(2).getMovementTimestamp()).isEqualTo(movement3.getMovementTimestamp());
    }

    @Test
    void findByProductOrderByMovementTimestampAsc_ShouldReturnEmptyList_WhenNoMovementsExist() {
        // When
        List<Movement> movements = movementRepository.findByProductOrderByMovementTimestampAsc(testProduct);

        // Then
        assertThat(movements).isEmpty();
    }

    @Test
    void findTopByProductOrderByMovementTimestampDesc_ShouldReturnLatestMovement() {
        // Given
        Movement movement1 = createMovement(testProduct, "Factory A", "Warehouse X",
                LocalDateTime.of(2024, 1, 16, 10, 0));
        Movement movement2 = createMovement(testProduct, "Warehouse X", "Store Y",
                LocalDateTime.of(2024, 1, 17, 14, 30));
        Movement movement3 = createMovement(testProduct, "Store Y", "Customer",
                LocalDateTime.of(2024, 1, 18, 9, 15));

        // When
        Optional<Movement> latestMovement = movementRepository
                .findTopByProductOrderByMovementTimestampDesc(testProduct);

        // Then
        assertThat(latestMovement).isPresent()
                .get()
                .satisfies(movement -> {
                    assertThat(movement.getMovementTimestamp()).isNotEqualTo(movement1.getMovementTimestamp());
                    assertThat(movement.getMovementTimestamp()).isNotEqualTo(movement2.getMovementTimestamp());
                    assertThat(movement.getMovementTimestamp()).isEqualTo(movement3.getMovementTimestamp());
                    assertThat(movement.getToLocation()).isEqualTo("Customer");
                });
    }

    @Test
    void findTopByProductOrderByMovementTimestampDesc_ShouldReturnEmpty_WhenNoMovementsExist() {
        // When
        Optional<Movement> latestMovement = movementRepository
                .findTopByProductOrderByMovementTimestampDesc(testProduct);

        // Then
        assertThat(latestMovement).isEmpty();
    }

    @Test
    void existsByProductAndMovementTimestamp_ShouldReturnTrue_WhenMovementExists() {
        // Given
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 16, 10, 0);
        createMovement(testProduct, "Factory A", "Warehouse X", timestamp);

        // When
        boolean exists = movementRepository.existsByProductAndMovementTimestamp(testProduct, timestamp);

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByProductAndMovementTimestamp_ShouldReturnFalse_WhenMovementDoesNotExist() {
        // Given
        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 16, 10, 0);

        // When
        boolean exists = movementRepository.existsByProductAndMovementTimestamp(testProduct, timestamp);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByProductAndMovementTimestamp_ShouldReturnFalse_ForDifferentProduct() {
        // Given
        Product anotherProduct = Product.builder()
                .type("Clothing")
                .manufacturingDate(LocalDateTime.of(2024, 2, 1, 9, 0))
                .origin("Factory B")
                .build();
        anotherProduct = productRepository.save(anotherProduct);

        LocalDateTime timestamp = LocalDateTime.of(2024, 1, 16, 10, 0);
        createMovement(testProduct, "Factory A", "Warehouse X", timestamp);

        // When
        boolean exists = movementRepository.existsByProductAndMovementTimestamp(anotherProduct, timestamp);

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByProductAndMovementTimestamp_ShouldReturnFalse_ForDifferentTimestamp() {
        // Given
        LocalDateTime timestamp1 = LocalDateTime.of(2024, 1, 16, 10, 0);
        LocalDateTime timestamp2 = LocalDateTime.of(2024, 1, 17, 14, 30);
        createMovement(testProduct, "Factory A", "Warehouse X", timestamp1);

        // When
        boolean exists = movementRepository.existsByProductAndMovementTimestamp(testProduct, timestamp2);

        // Then
        assertThat(exists).isFalse();
    }

    private Movement createMovement(Product product, String fromLocation,
                                    String toLocation, LocalDateTime timestamp) {

        Movement movement = Movement.builder()
                .fromLocation(fromLocation)
                .toLocation(toLocation)
                .movementTimestamp(timestamp)
                .product(product)
                .build();
        return movementRepository.save(movement);
    }
}
