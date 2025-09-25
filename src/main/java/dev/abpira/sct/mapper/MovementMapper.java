package dev.abpira.sct.mapper;

import dev.abpira.sct.dto.MovementResponseDTO;
import dev.abpira.sct.model.Movement;

public class MovementMapper {

    public static MovementResponseDTO mapToDTO(Movement movement) {
        return MovementResponseDTO.builder()
                .fromLocation(movement.getFromLocation())
                .toLocation(movement.getToLocation())
                .movementTimestamp(movement.getMovementTimestamp())
                .build();
    }
}
