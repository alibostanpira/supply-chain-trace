package dev.abpira.sct.repository;


import dev.abpira.sct.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void existsByTypeAndManufacturingDateAndOrigin_ShouldReturnTrue_WhenProductExists() {
        // Given
        LocalDateTime manufacturingDate = LocalDateTime.of(2024, 1, 15, 10, 0);
        Product product = Product.builder()
                .type("Electronics")
                .manufacturingDate(manufacturingDate)
                .origin("Factory A")
                .build();
        productRepository.save(product);

        // When
        boolean exists = productRepository.existsByTypeAndManufacturingDateAndOrigin(
                "Electronics", manufacturingDate, "Factory A");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByTypeAndManufacturingDateAndOrigin_ShouldReturnFalse_WhenProductDoesNotExist() {
        // Given - no products saved

        // When
        boolean exists = productRepository.existsByTypeAndManufacturingDateAndOrigin(
                "Electronics",
                LocalDateTime.of(2024, 1, 15, 10, 0),
                "Factory A");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByTypeAndManufacturingDateAndOrigin_ShouldReturnFalse_WhenTypeDiffers() {
        // Given
        LocalDateTime manufacturingDate = LocalDateTime.of(2024, 1, 15, 10, 0);
        Product product = Product.builder()
                .type("Electronics")
                .manufacturingDate(manufacturingDate)
                .origin("Factory A")
                .build();
        productRepository.save(product);

        // When - different type
        boolean exists = productRepository.existsByTypeAndManufacturingDateAndOrigin(
                "Clothing", manufacturingDate, "Factory A");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByTypeAndManufacturingDateAndOrigin_ShouldReturnFalse_WhenManufacturingDateDiffers() {
        // Given
        LocalDateTime manufacturingDate = LocalDateTime.of(2024, 1, 15, 10, 0);
        Product product = Product.builder()
                .type("Electronics")
                .manufacturingDate(manufacturingDate)
                .origin("Factory A")
                .build();
        productRepository.save(product);

        // When - different manufacturing date
        boolean exists = productRepository.existsByTypeAndManufacturingDateAndOrigin(
                "Electronics",
                LocalDateTime.of(2024, 1, 16, 10, 0), // Different date
                "Factory A");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByTypeAndManufacturingDateAndOrigin_ShouldReturnFalse_WhenOriginDiffers() {
        // Given
        LocalDateTime manufacturingDate = LocalDateTime.of(2024, 1, 15, 10, 0);
        Product product = Product.builder()
                .type("Electronics")
                .manufacturingDate(manufacturingDate)
                .origin("Factory A")
                .build();
        productRepository.save(product);

        // When - different origin
        boolean exists = productRepository.existsByTypeAndManufacturingDateAndOrigin(
                "Electronics", manufacturingDate, "Factory B");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    void existsByTypeAndManufacturingDateAndOrigin_ShouldReturnTrue_WithExactMillisecondMatch() {
        // Given - test precise timestamp matching
        LocalDateTime manufacturingDate = LocalDateTime
                .of(2024, 1, 15, 10, 30, 45, 123000000);
        Product product = Product.builder()
                .type("Electronics")
                .manufacturingDate(manufacturingDate)
                .origin("Factory A")
                .build();
        productRepository.save(product);

        // When - exact same timestamp including milliseconds
        boolean exists = productRepository.existsByTypeAndManufacturingDateAndOrigin(
                "Electronics", manufacturingDate, "Factory A");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsByTypeAndManufacturingDateAndOrigin_ShouldReturnFalse_WithDifferentMillisecond() {
        // Given
        LocalDateTime manufacturingDate = LocalDateTime
                .of(2024, 1, 15, 10, 30, 45, 123000000);
        Product product = Product.builder()
                .type("Electronics")
                .manufacturingDate(manufacturingDate)
                .origin("Factory A")
                .build();
        productRepository.save(product);

        // When - different milliseconds
        LocalDateTime differentTime = LocalDateTime
                .of(2024, 1, 15, 10, 30, 45, 456000000);
        boolean exists = productRepository.existsByTypeAndManufacturingDateAndOrigin(
                "Electronics", differentTime, "Factory A");

        // Then
        assertThat(exists).isFalse();
    }
}
