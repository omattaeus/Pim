package br.com.localfarm.product.interfaces.controllers;

import br.com.localfarm.product.domain.models.Product;
import br.com.localfarm.product.application.exceptions.ProductNotFoundException;
import br.com.localfarm.product.application.exceptions.InvalidProductException;
import br.com.localfarm.product.application.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/p")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Acesso permitido para "Administrador Geral" e "Gerencial Fazenda"
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENCIAL')")
    @PostMapping("/criar")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, @RequestParam Long clientId) {
        Product createdProduct = productService.createProduct(product, clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    // Acesso permitido para "Administrador Geral" e "Gerencial Fazenda"
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENCIAL')")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @Valid @RequestBody Product product, @RequestParam Long clientId) {
        try {
            Product updatedProduct = productService.updateProduct(id, product, clientId);
            return ResponseEntity.ok(updatedProduct);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (InvalidProductException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    // Acesso permitido apenas para "Administrador Geral"
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    @DeleteMapping("/excluir/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Acesso permitido para "Administrador Geral", "Gerencial Fazenda" e "Operacional Fazenda"
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENCIAL') or hasRole('ROLE_OPERACIONAL')")
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    // Acesso permitido para "Administrador Geral", "Gerencial Fazenda" e "Operacional Fazenda"
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR') or hasRole('ROLE_GERENCIAL') or hasRole('ROLE_OPERACIONAL')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}