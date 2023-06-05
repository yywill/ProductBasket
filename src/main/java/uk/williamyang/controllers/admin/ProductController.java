package uk.williamyang.controllers.admin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.williamyang.domain.Product;
import uk.williamyang.repo.ProductRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/api/products")
@Slf4j
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false, defaultValue = "20") int limit,
                                                        @RequestParam(required = false) Long cursor) {
        List<Product> products;
        if (cursor == null) {
            products = productRepository.findAll(PageRequest.of(0, limit)).getContent();
        } else {
            products = productRepository.findAllByIdGreaterThan(cursor, PageRequest.of(0, limit)).getContent();
        }
        return ResponseEntity.ok(products);
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        product.setCode(UUID.randomUUID().toString());
        product.setId(null);
        Product savedProduct = productRepository.save(product);
        log.info("Created product with id {}, code {}", savedProduct.getId(), savedProduct.getCode());
        return ResponseEntity.created(URI.create("/api/products/" + savedProduct.getId())).body(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
