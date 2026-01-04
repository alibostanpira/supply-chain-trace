package dev.abpira.sct.repository;

import dev.abpira.sct.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Profile("!in-memory")
interface JpaProductRepository extends JpaRepository<Product, Long> {
    boolean existsByTypeAndManufacturingDateAndOrigin(
            String type,
            LocalDateTime manufacturingDate,
            String origin);
}

@Repository
@Profile("!in-memory")
class DatabaseProductRepository implements ProductRepository {

    private final JpaProductRepository jpaRepository;

    public DatabaseProductRepository(JpaProductRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Product save(Product product) {
        return jpaRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public boolean existsByTypeAndManufacturingDateAndOrigin(
            String type, LocalDateTime manufacturingDate, String origin) {
        return jpaRepository.existsByTypeAndManufacturingDateAndOrigin(
                type, manufacturingDate, origin);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}