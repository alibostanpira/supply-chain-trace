package dev.abpira.sct.service.impl;

import dev.abpira.sct.dto.ProductRequestDTO;
import dev.abpira.sct.dto.ProductResponseDTO;
import dev.abpira.sct.exception.ProductAlreadyExistsException;
import dev.abpira.sct.exception.ProductNotFoundException;
import dev.abpira.sct.mapper.ProductMapper;
import dev.abpira.sct.model.Product;
import dev.abpira.sct.repository.ProductRepository;
import dev.abpira.sct.service.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    @Override
    public ProductResponseDTO registerProduct(ProductRequestDTO dto) {
        boolean exists = productRepository.existsByTypeAndManufacturingDateAndOrigin(
                dto.getType(),
                dto.getManufacturingDate(),
                dto.getOrigin()
        );
        if (exists) {
            throw new ProductAlreadyExistsException("Product already exists");
        }
        Product product = ProductMapper.mapToProduct(dto);
        Product saved = productRepository.save(product);
        return ProductMapper.mapToDTO(saved);
    }

    @Override
    public ProductResponseDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + productId));
        return ProductMapper.mapToDTO(product);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(ProductMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
