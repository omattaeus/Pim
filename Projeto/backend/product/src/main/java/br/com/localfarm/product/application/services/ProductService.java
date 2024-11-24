package br.com.localfarm.product.application.services;

import br.com.localfarm.product.domain.models.Product;
import br.com.localfarm.product.domain.models.UnitOfMeasure;
import br.com.localfarm.product.domain.repositories.ProductRepository;
import br.com.localfarm.product.application.exceptions.ProductNotFoundException;
import br.com.localfarm.product.application.exceptions.InvalidProductException;
import br.com.localfarm.product.domain.events.ProductUpdatedEvent;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final RestTemplate restTemplate;
    private final StreamBridge streamBridge;

    private static final String UNIT_OF_MEASURE_SERVICE_URL = "https://unidade-medida-localfarm-gsatghf5bjfaa3gx.brazilsouth-01.azurewebsites.net/units-of-measure/";

    @Autowired
    public ProductService(ProductRepository productRepository, RestTemplate restTemplate, StreamBridge streamBridge) {
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
        this.streamBridge = streamBridge;
    }

    @Transactional
    public Product createProduct(@Valid Product product) {
        validateAndSetUnitOfMeasure(product);
        validateProduct(product);
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, @Valid Product product) {
        validateAndSetUnitOfMeasure(product);
        validateProduct(product);

        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        existingProduct.setName(product.getName());
        existingProduct.setCode(product.getCode());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setUnitOfMeasure(product.getUnitOfMeasure());

        Product updatedProduct = productRepository.save(existingProduct);

        // Envia o evento de atualização do produto
        ProductUpdatedEvent event = new ProductUpdatedEvent(
                updatedProduct.getId(),
                updatedProduct.getName(),
                updatedProduct.getCode(),
                updatedProduct.getCategory()
        );
        streamBridge.send("productUpdated-out-0", event);

        updateProductMovements(updatedProduct);

        return updatedProduct;
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id)
                .or(() -> {
                    throw new ProductNotFoundException("Product not found with id: " + id);
                });
    }

    public void updateProductMovements(Product product) {
        String url = "https://produto-gaa2a9gfbvenbaaf.brazilsouth-01.azurewebsites.net/product-movements/product-updated";
        ProductUpdatedEvent event = new ProductUpdatedEvent(
                product.getId(),
                product.getName(),
                product.getCode(),
                product.getCategory()
        );
        restTemplate.postForEntity(url, event, Void.class);
    }

    private void validateAndSetUnitOfMeasure(Product product) {
        if (product.getUnitOfMeasure() == null || product.getUnitOfMeasure().getId() == null) {
            throw new InvalidProductException("Unit of Measure must not be null");
        }

        String url = UNIT_OF_MEASURE_SERVICE_URL + product.getUnitOfMeasure().getId();
        ResponseEntity<UnitOfMeasure> response;

        try {
            response = restTemplate.getForEntity(url, UnitOfMeasure.class);
        } catch (Exception e) {
            throw new InvalidProductException("Failed to fetch Unit of Measure: " + e.getMessage());
        }

        if (response == null || !response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new InvalidProductException("Invalid Unit of Measure ID: " + product.getUnitOfMeasure().getId());
        }

        product.setUnitOfMeasure(response.getBody());
    }

    public void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new InvalidProductException("Product name must not be null or empty");
        }
        if (product.getName().length() > 100) {
            throw new InvalidProductException("Product name must not exceed 100 characters");
        }

        if (product.getCode() == null || product.getCode().trim().isEmpty()) {
            throw new InvalidProductException("Product code must not be null or empty");
        }
        if (product.getCode().length() > 50) {
            throw new InvalidProductException("Product code must not exceed 50 characters");
        }

        if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
            throw new InvalidProductException("Product category must not be null or empty");
        }
        if (product.getCategory().length() > 50) {
            throw new InvalidProductException("Product category must not exceed 50 characters");
        }
        if (product.getUnitOfMeasure() == null || product.getUnitOfMeasure().getId() == null) {
            throw new InvalidProductException("Unit of Measure is mandatory");
        }
    }
}