/*
package br.com.localfarm.product.application.services;

import br.com.localfarm.product.domain.models.Product;
import br.com.localfarm.product.domain.models.UnitOfMeasure;
import br.com.localfarm.product.domain.repositories.ProductRepository;
import br.com.localfarm.product.application.exceptions.ProductNotFoundException;
import br.com.localfarm.product.application.exceptions.InvalidProductException;
import br.com.localfarm.product.domain.events.ProductUpdatedEvent;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.ArgumentCaptor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("Tests for ProductService")
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private StreamBridge streamBridge;

    @InjectMocks
    private ProductService productService;

    private @NotNull UnitOfMeasure defaultUnitOfMeasure;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        defaultUnitOfMeasure = UnitOfMeasure.builder()
                .id(Long.valueOf(1L))
                .name("Kilogram")
                .symbol("kg")
                .build();
    }

    @Test
    @DisplayName("Given a valid product, when creating the product, then it should be saved successfully")
    void givenValidProduct_whenCreatingProduct_thenSavesSuccessfully() {
        Product product = Product.builder()
                .id(Long.valueOf(1L))
                .name("ProductName")
                .code("P123")
                .category("Category1")
                .unitOfMeasure(defaultUnitOfMeasure)
                .build();

        when(restTemplate.getForEntity(
                UNIT_OF_MEASURE_SERVICE_URL + defaultUnitOfMeasure.getId(),
                UnitOfMeasure.class
        )).thenReturn(new ResponseEntity<>(defaultUnitOfMeasure, HttpStatus.OK));

        when(productRepository.save(product)).thenReturn(product);

        Product savedProduct = productService.createProduct(product);

        assertEquals(product, savedProduct);
        verify(productRepository, times(1)).save(product);
    }


    @Test
    @DisplayName("Given an invalid product, when creating the product, then it should throw InvalidProductException")
    void givenInvalidProduct_whenCreatingProduct_thenThrowsInvalidProductException() {
        Product product = Product.builder()
                .id(Long.valueOf(1L))
                .name("Valid Name")
                .code("P123")
                .category("Category1")
                .unitOfMeasure(null)
                .build();

        assertThrows(InvalidProductException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any(Product.class));
    }


    @Test
    @DisplayName("Given an existing product, when updating it, then it should be updated and event sent")
    void givenExistingProduct_whenUpdating_thenUpdatesAndSendsEvent() {
        Product updatedProduct = Product.builder()
                .id(Long.valueOf(1L))
                .name("UpdatedName")
                .code("P123")
                .category("Category1")
                .unitOfMeasure(defaultUnitOfMeasure)
                .build();

        Product existingProduct = Product.builder()
                .id(Long.valueOf(1L))
                .name("OldName")
                .code("P123")
                .category("Category1")
                .unitOfMeasure(defaultUnitOfMeasure)
                .build();

        when(productRepository.findById(Long.valueOf(1L))).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(existingProduct)).thenReturn(existingProduct);

        Product result = productService.updateProduct(Long.valueOf(1L), updatedProduct);

        assertEquals("UpdatedName", result.getName());
        verify(productRepository, times(1)).save(existingProduct);
        verify(streamBridge, times(1)).send(eq("productUpdated-out-0"), any(ProductUpdatedEvent.class));
    }

    @Test
    @DisplayName("Given an invalid Unit of Measure, when creating the product, then it should throw InvalidProductException")
    void givenInvalidUnitOfMeasure_whenCreatingProduct_thenThrowsInvalidProductException() {
        Product product = Product.builder()
                .id(Long.valueOf(1L))
                .name("Valid Name")
                .code("P123")
                .category("Category1")
                .unitOfMeasure(defaultUnitOfMeasure)
                .build();

        when(restTemplate.getForEntity(anyString(), eq(UnitOfMeasure.class)))
                .thenThrow(new RuntimeException("Service unavailable"));

        assertThrows(InvalidProductException.class, () -> productService.createProduct(product));
    }


    @Test
    @DisplayName("Given an existing product, when deleting it, then it should be deleted successfully")
    void givenExistingProduct_whenDeleting_thenDeletesSuccessfully() {
        when(productRepository.existsById(Long.valueOf(1L))).thenReturn(Boolean.valueOf(true));

        productService.deleteProduct(Long.valueOf(1L));

        verify(productRepository, times(1)).deleteById(Long.valueOf(1L));
    }

    @Test
    @DisplayName("Given a non-existing product, when deleting it, then it should throw ProductNotFoundException")
    void givenNonExistingProduct_whenDeleting_thenThrowsProductNotFoundException() {
        when(productRepository.existsById(Long.valueOf(1L))).thenReturn(Boolean.valueOf(false));

        assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(Long.valueOf(1L)));
        verify(productRepository, never()).deleteById(Long.valueOf(anyLong()));
    }

    @Test
    @DisplayName("Given pageable request, when getting all products, then it should return a page of products")
    void givenPageableRequest_whenGettingAllProducts_thenReturnsPagedProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Product product1 = Product.builder()
                .id(Long.valueOf(1L))
                .name("Product1")
                .code("P1")
                .category("Category1")
                .unitOfMeasure(defaultUnitOfMeasure)
                .build();

        Product product2 = Product.builder()
                .id(Long.valueOf(2L))
                .name("Product2")
                .code("P2")
                .category("Category2")
                .unitOfMeasure(defaultUnitOfMeasure)
                .build();

        Page<Product> productsPage = new PageImpl<>(Arrays.asList(product1, product2), pageable, 2);

        when(productRepository.findAll(pageable)).thenReturn(productsPage);

        Page<Product> result = productService.getAllProducts(pageable);

        assertEquals(2, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("Given a product, when updating product movements, then it should call RestTemplate with correct URL")
    void givenProduct_whenUpdatingProductMovements_thenCallsRestTemplate() {
        Product product = Product.builder()
                .id(Long.valueOf(1L))
                .name("ProductName")
                .code("P123")
                .category("Category1")
                .unitOfMeasure(defaultUnitOfMeasure)
                .build();

        ArgumentCaptor<ProductUpdatedEvent> eventCaptor = ArgumentCaptor.forClass(ProductUpdatedEvent.class);

        productService.updateProductMovements(product);

        verify(restTemplate, times(1)).postForEntity(
                eq("http://localhost:8084/product-movements/product-updated"),
                eventCaptor.capture(),
                eq(Void.class)
        );

        ProductUpdatedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(product.getId(), capturedEvent.getProductId());
        assertEquals(product.getName(), capturedEvent.getName());
    }
}
*/