package uk.williamyang.controllers;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.williamyang.controllers.admin.DiscountController;
import uk.williamyang.controllers.customer.BasketController;
import uk.williamyang.domain.*;
import uk.williamyang.dto.Receipt;
import uk.williamyang.repo.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class BasketControllerTest {

    @InjectMocks
    private BasketController basketController;

    @Mock
    private BasketRepository basketRepository;

    @Mock
    private BasketItemRepository basketItemRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
        // Given
        Product product = new Product("Product-1", "Product 1", new BigDecimal("10.00"), "HKD");
        product.setId(1L);

        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
    }

    @Test
    void testCreateBasket() {
        // Given
        Basket basket = new Basket();
        basket.setId(1L);

        given(basketRepository.save(basket)).willReturn(basket);

        // When
        ResponseEntity<Basket> response = basketController.createBasket(basket);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(basket);
    }

    @Test
    void testGetBasketByCode() {
        // Given
        Basket basket = new Basket();
        basket.setId(1L);
        String code = "abc";
        basket.setCode(code);

        given(basketRepository.findByCode(code)).willReturn(Optional.of(basket));

        // When
        ResponseEntity<Basket> response = basketController.getBasketByCode(code);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(basket);
    }

    @Test
    void testGetAllBaskets() {
        // Given
        List<Basket> baskets = Arrays.asList(
                new Basket("code-1", new BigDecimal(0)),
                new Basket("code-2", new BigDecimal(0))
        );

        given(basketRepository.findAll()).willReturn(baskets);

        // When
        List<Basket> allBaskets = basketController.getAllBaskets();

        // Then
        assertThat(allBaskets).isEqualTo(baskets);
    }

    @Test
    void testAddBasketItem() {
        // Given
        Basket basket = new Basket("code-1", new BigDecimal(0));

        BasketItem basketItem = new BasketItem();
        basketItem.setId(2L);
        Product product = new Product("Product-1","Product 1", new BigDecimal("10.00"), "HKD");
        product.setId(1L);
        basketItem.setProduct(product);
        basketItem.setQuantity(2);

        given(basketRepository.findByCode("abc")).willReturn(Optional.of(basket));
        given(basketItemRepository.save(basketItem)).willReturn(basketItem);

        given(productRepository.findById(1L)).willReturn(Optional.of(basketItem.getProduct()));
        // When
        ResponseEntity<BasketItem> response = basketController.addBasketItem("abc", basketItem);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(basketItem);
    }

    @Test
    void testUpdateBasketItem() {
        // Given
        Basket basket = new Basket("code-1", new BigDecimal(20));


        BasketItem basketItem = new BasketItem();
        basketItem.setId(2L);
        Product product = new Product("Product-1","Product 1", new BigDecimal("10.00"), "HKD");
        product.setId(1L);

        basketItem.setQuantity(2);
        basketItem.setBasket(basket);
        basketItem.setProduct(product);

        basket.setItems(Collections.singletonList(basketItem));

        given(basketRepository.save(basket)).willReturn(basket);
        given(basketRepository.findByCode("code-1")).willReturn(Optional.of(basket));
        given(basketItemRepository.save(basketItem)).willReturn(basketItem);
        given(basketItemRepository.findById(2L)).willReturn(Optional.of(basketItem));

        BasketItem updatedBasketItem = new BasketItem();
        updatedBasketItem.setId(2L);

        Product productUpdated = new Product("Product-1","Product 1", new BigDecimal("10.00"), "HKD");
        productUpdated.setId(2L);

        updatedBasketItem.setProduct(productUpdated);
        updatedBasketItem.setQuantity(3);

        given(productRepository.findById(1L)).willReturn(Optional.of(product));
        given(productRepository.findById(2L)).willReturn(Optional.of(productUpdated));

        // When
        ResponseEntity<BasketItem> response = basketController.updateBasketItem("code-1", 2L, updatedBasketItem);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(basketItem);
        assertThat(basket.getTotal()).isEqualTo(new BigDecimal("30.00"));
    }

    @Test
    void testDeleteBasketItem() {
        // Given
        BasketItem basketItem = new BasketItem();
        basketItem.setId(2L);
        basketItem.setProduct(new Product("Product-1","Product 1", new BigDecimal("10.00"), "HKD"));
        basketItem.setQuantity(2);

        Product product = new Product("Product-1", "Product 1", new BigDecimal("10.00"), "HKD");
        product.setId(1L);
        basketItem.setProduct(product);

        Basket basket = new Basket("code-1", new BigDecimal(20));
        basket.setItems(Collections.singletonList(basketItem));

        given(basketRepository.findByCode("abc")).willReturn(Optional.of(basket));
        given(basketItemRepository.findById(2L)).willReturn(Optional.of(basketItem));

        given(productRepository.findById(1L)).willReturn(Optional.of(product));



        // When
        ResponseEntity<Void> response = basketController.deleteBasketItem("abc", 2L);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(basket.getTotal()).isLessThanOrEqualTo(BigDecimal.ZERO);
        assertThat(basket.getTotal()).isGreaterThanOrEqualTo(BigDecimal.ZERO);
    }

    @Test
    void testCheckoutBasket() {
        // Given
        BasketItem basketItem = new BasketItem();
        basketItem.setId(2L);
        basketItem.setProduct(new Product("Product-1","Product 1", new BigDecimal("10.00"), "HKD"));
        basketItem.setQuantity(2);

        Basket basket = new Basket("code-1", new BigDecimal(0));
        basket.setItems(Collections.singletonList(basketItem));

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setClientDisplayName("Test Customer");
        customer.setApiKey("abc");

        given(basketRepository.findByCode("abc")).willReturn(Optional.of(basket));
        given(customerRepository.findByApiKey("abc")).willReturn(Optional.of(customer));

        // When
        ResponseEntity<Receipt> response = basketController.checkoutBasket("abc", "abc");

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTotal()).isEqualTo(new BigDecimal("20.00"));
        assertThat(response.getBody().getItems().get(0).getSubtotal()).isEqualTo(new BigDecimal("20.00"));
    }
}