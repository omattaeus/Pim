package br.com.localfarm.product.domain.models;

import br.com.localfarm.product.domain.exceptions.InvalidProductException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Unique code is mandatory")
    @Column(unique = true)
    @Size(max = 50, message = "Unique code must not exceed 50 characters")
    private String code;

    @NotNull(message = "Category is mandatory")
    @Size(max = 50, message = "Category must not exceed 50 characters")
    private String category;

    @NotNull(message = "Unit of Measure is mandatory")
    @ManyToOne
    @JoinColumn(name = "unit_of_measure_id", referencedColumnName = "id")
    private UnitOfMeasure unitOfMeasure;

    /**
     * Método de validação manual para o objeto Product.
     */
    public void validate() {
        if (this.getName() == null || this.getName().isEmpty()) {
            throw new InvalidProductException("Product name must not be null or empty");
        }

        if (this.getCode() == null || this.getCode().isEmpty()) {
            throw new InvalidProductException("Product code must not be null or empty");
        }

        if (this.getCategory() == null || this.getCategory().isEmpty()) {
            throw new InvalidProductException("Product category must not be null or empty");
        }

        if (this.getUnitOfMeasure() == null) {
            throw new InvalidProductException("Unit of Measure must not be null");
        }
    }
}