package dev.abpira.sct.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "ProductResponse")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDTO {

    private Long id;
    private String type;
    private LocalDateTime manufacturingDate;
    private String origin;
    private List<MovementResponseDTO> movements;
}
