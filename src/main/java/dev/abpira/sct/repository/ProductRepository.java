package dev.abpira.sct.repository;

import dev.abpira.sct.model.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    Optional<Product> findById(Long id);

    List<Product> findAll();

    boolean existsByTypeAndManufacturingDateAndOrigin(
            String type,
            LocalDateTime manufacturingDate,
            String origin);

    void deleteById(Long id);

    boolean existsById(Long id);
}