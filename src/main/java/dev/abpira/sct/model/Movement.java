package dev.abpira.sct.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movements", uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_product_movement_timestamp",
                columnNames = {"product_id", "movementTimestamp"}
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, updatable = false)
    private String fromLocation;

    @NotBlank
    @Column(nullable = false, updatable = false)
    private String toLocation;

    @NotNull
    @Column(nullable = false, updatable = false)
    private LocalDateTime movementTimestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    private Product product;
}
