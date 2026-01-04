package dev.abpira.sct.repository;

import dev.abpira.sct.model.Movement;
import dev.abpira.sct.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Profile("!in-memory")
interface JpaMovementRepository extends JpaRepository<Movement, Long> {
    boolean existsByProductAndMovementTimestamp(Product product, LocalDateTime timestamp);

    Optional<Movement> findTopByProductOrderByMovementTimestampDesc(Product product);

    List<Movement> findByProductOrderByMovementTimestampAsc(Product product);
}

@Repository
@Profile("!in-memory")
class DatabaseMovementRepository implements MovementRepository {

    private final JpaMovementRepository jpaRepository;

    public DatabaseMovementRepository(JpaMovementRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Movement save(Movement movement) {
        return jpaRepository.save(movement);
    }

    @Override
    public Optional<Movement> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Movement> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public boolean existsByProductAndMovementTimestamp(Product product, LocalDateTime timestamp) {
        return jpaRepository.existsByProductAndMovementTimestamp(product, timestamp);
    }

    @Override
    public Optional<Movement> findTopByProductOrderByMovementTimestampDesc(Product product) {
        return jpaRepository.findTopByProductOrderByMovementTimestampDesc(product);
    }

    @Override
    public List<Movement> findByProductOrderByMovementTimestampAsc(Product product) {
        return jpaRepository.findByProductOrderByMovementTimestampAsc(product);
    }
}