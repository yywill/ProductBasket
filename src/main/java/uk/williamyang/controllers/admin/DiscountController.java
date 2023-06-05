package uk.williamyang.controllers.admin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.williamyang.domain.Discount;
import uk.williamyang.domain.Product;
import uk.williamyang.repo.DiscountRepository;
import uk.williamyang.repo.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/admin/api/discounts")
@Slf4j
public class DiscountController {

    private final DiscountRepository discountRepository;
    private final ProductRepository productRepository;

    public DiscountController(DiscountRepository discountRepository, ProductRepository productRepository) {
        this.discountRepository = discountRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<Discount>> getAllDiscounts(@RequestParam(required = false, defaultValue = "20") int limit,
                                                          @RequestParam(required = false) Long cursor) {
        List<Discount> discounts;
        if (cursor == null) {
            discounts = discountRepository.findAll(PageRequest.of(0, limit)).getContent();
        } else {
            discounts = discountRepository.findAllByIdGreaterThan(cursor, PageRequest.of(0, limit)).getContent();
        }
        return ResponseEntity.ok(discounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Discount> getDiscountById(@PathVariable Long id) {
        return discountRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Discount> createDiscount(@RequestBody Discount discount, @PathVariable Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            discount.setCode(UUID.randomUUID().toString());
            discount.setId(null);
            product.setDiscount(discount);
            Discount savedDiscount = discountRepository.save(discount);
            productRepository.save(product);
            log.info("Created discount with id {}, code {}", savedDiscount.getId(), savedDiscount.getCode());
            return ResponseEntity.ok().body(savedDiscount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/product/{productId}/discount/{id}")
    public ResponseEntity<Discount> updateDiscount(@PathVariable Long id,
                                                   @RequestBody Discount updatedDiscount, @PathVariable Long productId) {
        Optional<Product> productOptional = productRepository.findById(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            return discountRepository.findById(id)
                    .map(discount -> {
                        discount.setName(updatedDiscount.getName());
                        product.setDiscount(discount);
                        discount.setDiscountPercentage(updatedDiscount.getDiscountPercentage());

                        productRepository.save(product);
                        Discount savedDiscount = discountRepository.save(discount);
                        return ResponseEntity.ok().body(savedDiscount);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDiscountById(@PathVariable Long id) {
        discountRepository.findById(id).ifPresent(discount -> {
            List<Product> products = productRepository.findByDiscount_Id(discount.getId());
            products.forEach(product -> {
                product.setDiscount(null);
            });
            productRepository.saveAll(products);
            discountRepository.deleteById(id);
        });
    }
}