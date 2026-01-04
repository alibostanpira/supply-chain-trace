package dev.abpira.sct.repository;

import dev.abpira.sct.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Profile("in-memory")
public class InMemoryProductRepository implements ProductRepository {

    private final Map<Long, Product> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
        }
        storage.put(product.getId(), product);
        return product;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public boolean existsByTypeAndManufacturingDateAndOrigin(
            String type, LocalDateTime manufacturingDate, String origin) {
        return storage.values().stream()
                .anyMatch(p -> p.getType().equals(type)
                        && p.getManufacturingDate().equals(manufacturingDate)
                        && p.getOrigin().equals(origin));
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }
}