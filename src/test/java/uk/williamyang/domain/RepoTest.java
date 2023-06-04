package uk.williamyang.domain;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.williamyang.repo.BasketRepository;
import uk.williamyang.repo.ProductRepository;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


@SpringBootTest
@RunWith(SpringRunner.class)
public class RepoTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    BasketRepository basketRepository;

    // Other repositories

    @Test
    public void testSaveAndFindProduct() {

        // Create a product
        Product product = new Product("","Apple", BigDecimal.valueOf(1.99));

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

    // Other tests here

}