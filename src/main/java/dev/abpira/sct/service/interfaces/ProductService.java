package dev.abpira.sct.service.interfaces;

import dev.abpira.sct.dto.ProductRequestDTO;
import dev.abpira.sct.dto.ProductResponseDTO;

import java.util.List;

public interface ProductService {

    ProductResponseDTO registerProduct(ProductRequestDTO productRequestDTO);
    ProductResponseDTO getProductById(Long productId);
    List<ProductResponseDTO> getAllProducts();
}
