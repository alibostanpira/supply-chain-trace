package dev.abpira.sct.mapper;

import dev.abpira.sct.dto.MovementResponseDTO;
import dev.abpira.sct.dto.ProductRequestDTO;
import dev.abpira.sct.dto.ProductResponseDTO;
import dev.abpira.sct.model.Product;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductMapper {

    public static ProductResponseDTO mapToDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .type(product.getType())
                .manufacturingDate(product.getManufacturingDate())
                .origin(product.getOrigin())
                .movements(Optional.ofNullable(product.getMovements())
                        .orElseGet(ArrayList::new)
                        .stream()
                        .map(m -> MovementResponseDTO.builder()
                                .fromLocation(m.getFromLocation())
                                .toLocation(m.getToLocation())
                                .movementTimestamp(m.getMovementTimestamp())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }

    public static Product mapToProduct(ProductRequestDTO dto) {
        return Product.builder()
                .type(dto.getType())
                .manufacturingDate(dto.getManufacturingDate())
                .origin(dto.getOrigin())
                .movements(new ArrayList<>())
                .build();
    }
}
