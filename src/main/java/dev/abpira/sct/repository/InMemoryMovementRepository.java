package dev.abpira.sct.repository;

import dev.abpira.sct.model.Movement;
import dev.abpira.sct.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Profile("in-memory")
public class InMemoryMovementRepository implements MovementRepository {

    private final Map<Long, Movement> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Movement save(Movement movement) {
        if (movement.getId() == null) {
            movement.setId(idGenerator.getAndIncrement());
        }
        storage.put(movement.getId(), movement);
        return movement;
    }

    @Override
    public Optional<Movement> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Movement> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsByProductAndMovementTimestamp(Product product, LocalDateTime timestamp) {
        return storage.values().stream()
                .anyMatch(m -> m.getProduct().getId().equals(product.getId())
                        && m.getMovementTimestamp().equals(timestamp));
    }

    @Override
    public Optional<Movement> findTopByProductOrderByMovementTimestampDesc(Product product) {
        return storage.values().stream()
                .filter(m -> m.getProduct().getId().equals(product.getId()))
                .max(Comparator.comparing(Movement::getMovementTimestamp));
    }

    @Override
    public List<Movement> findByProductOrderByMovementTimestampAsc(Product product) {
        return storage.values().stream()
                .filter(m -> m.getProduct().getId().equals(product.getId()))
                .sorted(Comparator.comparing(Movement::getMovementTimestamp))
                .collect(Collectors.toList());
    }
}