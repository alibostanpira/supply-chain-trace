package dev.abpira.sct.service.impl;


import dev.abpira.sct.dto.MovementRequestDTO;
import dev.abpira.sct.dto.MovementResponseDTO;
import dev.abpira.sct.exception.ImmutableMovementException;
import dev.abpira.sct.exception.InvalidMovementException;
import dev.abpira.sct.exception.ProductNotFoundException;
import dev.abpira.sct.mapper.MovementMapper;
import dev.abpira.sct.model.Movement;
import dev.abpira.sct.model.Product;
import dev.abpira.sct.repository.MovementRepository;
import dev.abpira.sct.repository.ProductRepository;
import dev.abpira.sct.service.interfaces.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final ProductRepository productRepository;


    @Override
    public MovementResponseDTO logMovement(MovementRequestDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + dto.getProductId()));

        // Validate movement chronology and immutability
        validateMovement(product, dto);

        Movement movement = Movement.builder()
                .fromLocation(dto.getFromLocation())
                .toLocation(dto.getToLocation())
                .movementTimestamp(dto.getMovementTimestamp())
                .product(product)
                .build();

        Movement saved = movementRepository.save(movement);
        return MovementMapper.mapToDTO(saved);
    }

    private void validateMovement(Product product, MovementRequestDTO dto) {
        // 1. Check if movement timestamp is after product manufacturing date
        if (dto.getMovementTimestamp().isBefore(product.getManufacturingDate())) {
            throw new InvalidMovementException("Movement timestamp cannot be before product manufacturing date: "
                    + product.getManufacturingDate()
            );
        }

        // 2. Check if movement already exists with same timestamp (immutability)
        if (movementRepository.existsByProductAndMovementTimestamp(product, dto.getMovementTimestamp())) {
            throw new ImmutableMovementException("Movement already exists for this product at timestamp: "
                    + dto.getMovementTimestamp()
            );
        }

        // 3. Check chronological order - get the latest movement
        Optional<Movement> latestMovement = movementRepository.findTopByProductOrderByMovementTimestampDesc(product);

        if (latestMovement.isPresent()) {
            LocalDateTime lastTimestamp = latestMovement.get().getMovementTimestamp();

            if (dto.getMovementTimestamp().isBefore(lastTimestamp)) {
                throw new InvalidMovementException(
                        "New movement must be chronologically after the last movement. " +
                                "Last movement timestamp: " + lastTimestamp +
                                ", New movement timestamp: " + dto.getMovementTimestamp()
                );
            }
        }
    }

    @Override
    public List<MovementResponseDTO> getMovementsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));

        return movementRepository.findByProductOrderByMovementTimestampAsc(product)
                .stream()
                .map(MovementMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
