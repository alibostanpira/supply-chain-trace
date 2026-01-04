package dev.abpira.sct.repository;

import dev.abpira.sct.model.Movement;
import dev.abpira.sct.model.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovementRepository {
    Movement save(Movement movement);

    Optional<Movement> findById(Long id);

    List<Movement> findAll();

    boolean existsByProductAndMovementTimestamp(Product product, LocalDateTime timestamp);

    Optional<Movement> findTopByProductOrderByMovementTimestampDesc(Product product);

    List<Movement> findByProductOrderByMovementTimestampAsc(Product product);
}
