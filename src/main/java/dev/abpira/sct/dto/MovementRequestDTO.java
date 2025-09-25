package dev.abpira.sct.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Schema(name = "MovementRequest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementRequestDTO {

    @NotNull(message = "productId is required")
    private Long productId;

    @NotBlank(message = "From location is required")
    private String fromLocation;

    @NotBlank(message = "To location is required")
    private String toLocation;

    @NotNull(message = "Movement timestamp is required")
    private LocalDateTime movementTimestamp;
}
