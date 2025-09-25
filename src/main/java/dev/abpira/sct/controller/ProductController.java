package dev.abpira.sct.controller;


import dev.abpira.sct.dto.ErrorResponseDTO;
import dev.abpira.sct.dto.ProductRequestDTO;
import dev.abpira.sct.dto.ProductResponseDTO;
import dev.abpira.sct.service.interfaces.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(
        name = "Product",
        description = "Create and fetch REST APIs for Product"
)
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
public class ProductController {

    private final ProductService productService;

    // Register a new product
    @Operation(
            summary = "register a product"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping
    public ResponseEntity<ProductResponseDTO> registerProduct(
            @Valid @RequestBody ProductRequestDTO productRequestDTO) {
        ProductResponseDTO responseDTO = productService.registerProduct(productRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get product by ID including movement history
    @Operation(
            summary = "fetch a product with ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        ProductResponseDTO responseDTO = productService.getProductById(id);
        return ResponseEntity.ok(responseDTO);
    }

    // Get all products
    @Operation(
            summary = "fetch all products including their movements"
    )
    @ApiResponse(
            responseCode = "200",
            description = "HTTP Status OK"
    )
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }
}
