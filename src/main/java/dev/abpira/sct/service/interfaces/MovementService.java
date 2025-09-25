package dev.abpira.sct.service.interfaces;

import dev.abpira.sct.dto.MovementRequestDTO;
import dev.abpira.sct.dto.MovementResponseDTO;

import java.util.List;


public interface MovementService {

    MovementResponseDTO logMovement(MovementRequestDTO movementRequestDTO);
    List<MovementResponseDTO> getMovementsByProductId(Long productId);
}
