package dev.abpira.sct.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;

import java.time.LocalDateTime;

@Schema(name = "ProductRequest")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDTO {

    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Manufacturing date is required")
    @PastOrPresent(message = "Manufacturing date cannot be in the future")
    private LocalDateTime manufacturingDate;

    @NotBlank(message = "Origin is required")
    private String origin;
}
