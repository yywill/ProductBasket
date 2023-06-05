package uk.williamyang.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.williamyang.domain.Product;
import uk.williamyang.repo.ProductRepository;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin/api/products")
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
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.created(URI.create("/api/products/" + savedProduct.getId())).body(savedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
