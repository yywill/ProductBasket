package uk.williamyang.controllers.customer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uk.williamyang.domain.Basket;
import uk.williamyang.domain.BasketItem;
import uk.williamyang.domain.Customer;
import uk.williamyang.domain.Product;
import uk.williamyang.dto.Discount;
import uk.williamyang.dto.Receipt;
import uk.williamyang.dto.ReceiptItem;
import uk.williamyang.repo.BasketItemRepository;
import uk.williamyang.repo.BasketRepository;
import uk.williamyang.repo.CustomerRepository;
import uk.williamyang.repo.ProductRepository;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/baskets")
@Slf4j
public class BasketController {

    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final CustomerRepository customerRepository;

    public BasketController(BasketRepository basketRepository, BasketItemRepository basketItemRepository, CustomerRepository customerRepository) {
        this.basketRepository = basketRepository;
        this.basketItemRepository = basketItemRepository;
        this.customerRepository = customerRepository;
    }

    @PostMapping("")
    public ResponseEntity<Basket> createBasket(@RequestBody Basket basket) {
        basket.setCode(UUID.randomUUID().toString());
        Basket saved = basketRepository.save(basket);
        log.info("Created basket with id {}, code {}", saved.getId(), saved.getCode());
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{code}")
    public ResponseEntity<Basket> getBasketByCode(@PathVariable String code) {
        Optional<Basket> basketOptional = basketRepository.findByCode(code);
        if (basketOptional.isPresent()) {
            return ResponseEntity.ok(basketOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public List<Basket> getAllBaskets() {
        return basketRepository.findAll();
    }

    @PostMapping("/{basketCode}/items")
    public ResponseEntity<BasketItem> addBasketItem(@PathVariable String basketCode, @RequestBody BasketItem basketItem) {
        Optional<Basket> basketOptional = basketRepository.findByCode(basketCode);
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            basketItem.setBasket(basket);
            basketItem = basketItemRepository.save(basketItem);
            basket.getItems().add(basketItem);
            basket.setTotal(
                    (basket.getTotal() != null ? basket.getTotal() : BigDecimal.ZERO)
                            .add(
                                    (new BigDecimal(basketItem.getQuantity()))
                                            .multiply(basketItem.getProduct().getPrice()))
            );
            basketRepository.save(basket);
            return ResponseEntity.ok(basketItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{basketCode}/items/{itemId}")
    public ResponseEntity<BasketItem> updateBasketItem(@PathVariable String basketCode, @PathVariable Long itemId, @RequestBody BasketItem updatedBasketItem) {
        Optional<Basket> basketOptional = basketRepository.findByCode(basketCode);
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            Optional<BasketItem> basketItemOptional = basket.getItems().stream().filter(item -> item.getId().equals(itemId)).findFirst();
            if (basketItemOptional.isPresent()) {
                BasketItem basketItem = basketItemOptional.get();

                basket.setTotal(
                        (basket.getTotal() != null ? basket.getTotal() : BigDecimal.ZERO)
                                .subtract(
                                        (new BigDecimal(basketItem.getQuantity()))
                                                .multiply(basketItem.getProduct().getPrice()))
                );

                basketItem.setProduct(updatedBasketItem.getProduct());
                basketItem.setQuantity(updatedBasketItem.getQuantity());
                basketItem = basketItemRepository.save(basketItem);

                basket.setTotal(
                        (basket.getTotal() != null ? basket.getTotal() : BigDecimal.ZERO)
                                .add(
                                        (new BigDecimal(basketItem.getQuantity()))
                                                .multiply(basketItem.getProduct().getPrice())
                                )
                );

                basketRepository.save(basket);
                return ResponseEntity.ok(basketItem);
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{basketCode}/items/{itemId}")
    public ResponseEntity<Void> deleteBasketItem(@PathVariable String basketCode, @PathVariable Long itemId) {
        Optional<Basket> basketOptional = basketRepository.findByCode(basketCode);
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            List<BasketItem> items = new ArrayList<>(basket.getItems());
            Optional<BasketItem> basketItemOptional = items.stream().filter(item -> item.getId().equals(itemId)).findFirst();
            if (basketItemOptional.isPresent()) {
                BasketItem basketItem = basketItemOptional.get();
                basket.setTotal(
                        (basket.getTotal() != null ? basket.getTotal() : BigDecimal.ZERO).subtract(
                                (new BigDecimal(basketItem.getQuantity())).multiply(basketItem.getProduct().getPrice())
                        )
                );
                items.remove(basketItem);
                basket.setItems(items);
                basketRepository.save(basket);
                basketItemRepository.delete(basketItem);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{basketCode}/checkout")
    public ResponseEntity<Receipt> checkoutBasket(@PathVariable String basketCode, @RequestHeader("bullish-william-api-key") String apiKey) {
        Optional<Basket> basketOptional = basketRepository.findByCode(basketCode);
        if (basketOptional.isPresent()) {
            Basket basket = basketOptional.get();
            Optional<Customer> customerOptional = customerRepository.findByApiKey(apiKey);
            if (customerOptional.isPresent()) {
                Customer customer = customerOptional.get();
                basket.setCustomer(customer);
                basketRepository.save(basket);

                List<BasketItem> items = basket.getItems();
                List<ReceiptItem> receiptItems = new ArrayList<>();
                BigDecimal total = BigDecimal.ZERO;
                int totalQuantity = 0;

                for (BasketItem item : items) {
                    Product product = item.getProduct();
                    int quantity = item.getQuantity();
                    BigDecimal price = product.getPrice();

                    Discount discount = null;
                    BigDecimal discountValue = BigDecimal.ZERO;

                    if (product.getDiscount() != null) {
                        discountValue = product.getDiscount().getDiscountPercentage();
                        discount = new Discount(product.getDiscount().getName(), discountValue);
                    }

                    BigDecimal subtotal = price.multiply(new BigDecimal(quantity)).subtract(price.multiply(discountValue).multiply(new BigDecimal(quantity)));
                    total = total.add(subtotal);

                    ReceiptItem receiptItem = new ReceiptItem(product.getName(), quantity, price, discount, subtotal);
                    receiptItems.add(receiptItem);

                    totalQuantity += quantity;
                }

                LocalDate date = LocalDate.now();
                String clientName = customer.getClientDisplayName();
                int totalItems = receiptItems.size();
                Receipt receipt = new Receipt(clientName, date, totalItems, totalQuantity, receiptItems, total);

                return ResponseEntity.ok(receipt);

            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid API key");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Basket not found");
        }
    }
}