package br.com.localfarm.app.domain.repositories;

import br.com.localfarm.app.domain.models.ProductMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductMovementRepository extends JpaRepository<ProductMovement, Long> {
    Page<ProductMovement> findAll(Pageable pageable);
}