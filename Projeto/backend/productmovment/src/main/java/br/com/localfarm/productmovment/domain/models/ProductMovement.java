package br.com.localfarm.productmovment.domain.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import br.com.localfarm.productmovment.domain.models.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Movement date is mandatory")
    private LocalDateTime movementDate;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @NotNull(message = "Quantity is mandatory")
    private Integer quantity;

    @NotBlank(message = "Purpose is mandatory")
    private String purpose;

    @NotNull(message = "Product is mandatory")
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
    private Product product;

    @NotNull(message = "Client is mandatory")
    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private Client client;

    private LocalDateTime updatedAt;
}