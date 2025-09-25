package dev.abpira.sct.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(name = "MovementResponse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovementResponseDTO {

    private String fromLocation;
    private String toLocation;
    private LocalDateTime movementTimestamp;
}
