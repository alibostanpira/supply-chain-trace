package dev.abpira.sct.repository;

import dev.abpira.sct.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByTypeAndManufacturingDateAndOrigin(
            String type,
            LocalDateTime manufacturingDate,
            String origin);
}
