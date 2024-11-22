package br.com.localfarm.productmovment.application.services;

import br.com.localfarm.productmovment.domain.models.Client;
import br.com.localfarm.productmovment.domain.models.Product;
import br.com.localfarm.productmovment.domain.models.ProductMovement;
import br.com.localfarm.productmovment.domain.repositories.ProductMovementRepository;
import br.com.localfarm.productmovment.application.exceptions.ProductMovementNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class ProductMovementService {

    private static final Logger logger = LoggerFactory.getLogger(ProductMovementService.class);

    private final ProductMovementRepository productMovementRepository;
    private final RestTemplate restTemplate;

    private final String PRODUCT_BASE_URL = "https://produto-gaa2a9gfbvenbaaf.brazilsouth-01.azurewebsites.net/p";
    private final String CLIENT_BASE_URL = "https://cliente-localfarm-dpcdeqevd3e7hddg.brazilsouth-01.azurewebsites.net/clients";

    @Autowired
    public ProductMovementService(ProductMovementRepository productMovementRepository, RestTemplate restTemplate) {
        this.productMovementRepository = productMovementRepository;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public ProductMovement createProductMovement(@Valid ProductMovement productMovement) {
        validateProductMovement(productMovement);

        // Validar e obter informações sobre o produto
        Product product = fetchProductById(productMovement.getProduct().getId());
        productMovement.setProduct(product);

        // Validar e obter informações sobre o cliente
        Client client = fetchClientById(productMovement.getClient().getId());
        productMovement.setClient(client);

        return productMovementRepository.save(productMovement);
    }

    @Transactional
    public ProductMovement updateProductMovement(Long id, @Valid ProductMovement productMovement) {
        validateProductMovement(productMovement);

        ProductMovement existingProductMovement = productMovementRepository.findById(id)
                .orElseThrow(() -> new ProductMovementNotFoundException("ProductMovement not found with id: " + id));

        // Validar e obter informações sobre o produto
        Product product = fetchProductById(productMovement.getProduct().getId());
        productMovement.setProduct(product);

        // Validar e obter informações sobre o cliente
        Client client = fetchClientById(productMovement.getClient().getId());
        productMovement.setClient(client);

        productMovement.setId(existingProductMovement.getId());
        return productMovementRepository.save(productMovement);
    }

    private Product fetchProductById(Long productId) {
        String url = PRODUCT_BASE_URL + "/" + productId;

        try {
            ResponseEntity<Product> response = restTemplate.getForEntity(url, Product.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + productId);
            }
        } catch (Exception e) {
            logger.error("Error fetching product with id {}: {}", productId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching product data");
        }
    }

    private Client fetchClientById(Long clientId) {
        String url = CLIENT_BASE_URL + "/" + clientId;

        try {
            ResponseEntity<Client> response = restTemplate.getForEntity(url, Client.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found with id: " + clientId);
            }
        } catch (Exception e) {
            logger.error("Error fetching client with id {}: {}", clientId, e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error fetching client data");
        }
    }

    private void validateProductMovement(ProductMovement productMovement) {
        if (productMovement.getQuantity() == null || productMovement.getQuantity() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }
        if (productMovement.getMovementDate() == null) {
            throw new IllegalArgumentException("Movement date must not be null");
        }
        if (productMovement.getProduct() == null || productMovement.getProduct().getId() == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }
        if (productMovement.getClient() == null || productMovement.getClient().getId() == null) {
            throw new IllegalArgumentException("Client ID must not be null");
        }
        if (productMovement.getType() == null || productMovement.getType().trim().isEmpty()) {
            throw new IllegalArgumentException("Movement type must not be null or empty");
        }
        if (!productMovement.getType().equalsIgnoreCase("Entry") && !productMovement.getType().equalsIgnoreCase("Exit")) {
            throw new IllegalArgumentException("Movement type must be 'Entry' or 'Exit'");
        }
        if (productMovement.getPurpose() == null || productMovement.getPurpose().trim().isEmpty()) {
            throw new IllegalArgumentException("Purpose must not be null or empty");
        }
        if (!productMovement.getPurpose().equalsIgnoreCase("Purchase") && !productMovement.getPurpose().equalsIgnoreCase("Sale") && !productMovement.getPurpose().equalsIgnoreCase("Production")) {
            throw new IllegalArgumentException("Purpose must be 'Purchase', 'Sale', or 'Production'");
        }
    }
}