package dev.abpira.sct.controller;

import dev.abpira.sct.dto.ErrorResponseDTO;
import dev.abpira.sct.dto.MovementRequestDTO;
import dev.abpira.sct.dto.MovementResponseDTO;
import dev.abpira.sct.service.interfaces.MovementService;
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
        name = "Movement",
        description = "log and fetch movements of Products"
)
@RestController
@RequestMapping("/api/movement")
@RequiredArgsConstructor
@Validated
public class MovementController {

    private final MovementService movementService;

    // Log a movement for a product
    @Operation(
            summary = "log the movement of a product"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "HTTP Status NOT FOUND",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "HTTP Status BAD REQUEST",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "HTTP Status CONFLICT",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping
    public ResponseEntity<MovementResponseDTO> logMovement(
            @Valid @RequestBody MovementRequestDTO movementRequestDTO) {
        MovementResponseDTO responseDTO = movementService.logMovement(movementRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Get movement history for a product
    @Operation(
            summary = "fetch all movements of a product"
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
    @GetMapping("/{productId}")
    public ResponseEntity<List<MovementResponseDTO>> getMovements(@PathVariable Long productId) {
        List<MovementResponseDTO> movements = movementService.getMovementsByProductId(productId);
        return ResponseEntity.ok(movements);
    }
}
