package uk.williamyang.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import uk.williamyang.repo.BasketRepository;
import uk.williamyang.repo.DiscountRepository;
import uk.williamyang.repo.ProductRepository;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest
public class RepoTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    DiscountRepository discountRepository;

    // Other repositories

    @Test
    public void testSaveAndFindProduct() {

        // Create a product
        Product product = new Product("Apple-1","Apple", BigDecimal.valueOf(1999),"HKD");

        // Save product
        productRepository.save(product);

        // Find product and assert
        Product found = productRepository.findById(product.getId()).get();
        assertEquals(found.getName(), "Apple");

    }



    @Test
    public void testSaveAndFindBasket() {

        // Create a basket with a single item
        BasketItem item = new BasketItem();
        item.setQuantity(2);
        Basket basket = new Basket();
        basket.setCode("BASKET1");
        basket.setTotal(1);
        basket.setItems(Collections.singletonList(item));

        // Save basket
        Basket savedBasket = basketRepository.save(basket);

        // Find basket and assert
        Basket foundBasket = basketRepository.findById(savedBasket.getId()).get();
        assertNotNull(foundBasket);
        assertEquals(savedBasket.getCode(), foundBasket.getCode());
        assertEquals(savedBasket.getTotal(), foundBasket.getTotal());

    }

    @Test
    public void testSaveAndFindDiscount() {

        // Create a test discount
        Discount discount = new Discount();
        discount.setCode("DISCOUNT1");
        discount.setDiscountPercentage(0.25);
        Product product = new Product("Apple-2","Apple", BigDecimal.valueOf(6999),"HKD");
        discount.setProduct(product);

        // Save the discount
        discountRepository.save(discount);

        // Find the saved discount by ID
        Discount foundDiscount = discountRepository.findById(discount.getId()).get();

        // Assertions
        assertEquals(foundDiscount.getDiscountPercentage(), discount.getDiscountPercentage());
        assertEquals(foundDiscount.getProduct().getId(), discount.getProduct().getId());

    }

}