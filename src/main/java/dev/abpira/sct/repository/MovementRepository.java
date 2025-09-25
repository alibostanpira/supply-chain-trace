package dev.abpira.sct.repository;

import dev.abpira.sct.model.Movement;
import dev.abpira.sct.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MovementRepository extends JpaRepository<Movement, Long> {
    List<Movement> findByProductOrderByMovementTimestampAsc(Product product);

    // Find the latest movement for a product
    Optional<Movement> findTopByProductOrderByMovementTimestampDesc(Product product);

    // Check if movement exists with same timestamp (for immutability)
    boolean existsByProductAndMovementTimestamp(Product product, LocalDateTime timestamp);
}
