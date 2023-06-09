package uk.williamyang.controllers;


import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.williamyang.controllers.admin.DiscountController;
import uk.williamyang.domain.Discount;
import uk.williamyang.repo.DiscountRepository;
import uk.williamyang.repo.ProductRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import uk.williamyang.domain.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class DiscountControllerTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private DiscountController discountController;

    @Test
    public void testGetAllDiscountsWithoutCursor() {
        when(discountRepository.findAll(any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        ResponseEntity<List<Discount>> response = discountController.getAllDiscounts(20, null);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetAllDiscountsWithCursor() {
        when(discountRepository.findAllByIdGreaterThan(anyLong(), any(PageRequest.class))).thenReturn(new PageImpl<>(Collections.emptyList()));
        ResponseEntity<List<Discount>> response = discountController.getAllDiscounts(20, 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetDiscountById() {
        when(discountRepository.findById(anyLong())).thenReturn(Optional.of(new Discount()));
        ResponseEntity<Discount> response = discountController.getDiscountById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetDiscountByIdNotFound() {
        when(discountRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Discount> response = discountController.getDiscountById(1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testCreateDiscount() {
        Product product = new Product();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(discountRepository.save(any(Discount.class))).thenReturn(new Discount());
        ResponseEntity<Discount> response = discountController.createDiscount(new Discount(), 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCreateDiscountProductNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Discount> response = discountController.createDiscount(new Discount(), 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdateDiscount() {
        Product product = new Product();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(discountRepository.findById(anyLong())).thenReturn(Optional.of(new Discount()));
        when(discountRepository.save(any(Discount.class))).thenReturn(new Discount());
        ResponseEntity<Discount> response = discountController.updateDiscount(1L, new Discount("code-1",new BigDecimal(0.2)), 1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testUpdateDiscountNotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(new Product()));
        when(discountRepository.findById(anyLong())).thenReturn(Optional.empty());
        ResponseEntity<Discount> response = discountController.updateDiscount(1L, new Discount(), 1L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testDeleteDiscountById() {
        // Prepare test data
        Long discountId = 1L;
        Discount discount = new Discount();
        discount.setId(discountId);

        Product product1 = new Product();
        product1.setId(1L);
        product1.setDiscount(discount);

        Product product2 = new Product();
        product2.setId(2L);
        product2.setDiscount(discount);

        List<Product> products = Arrays.asList(product1, product2);

        // Configure mock behavior
        when(discountRepository.findById(discountId)).thenReturn(Optional.of(discount));
        when(productRepository.findByDiscount_Id(discountId)).thenReturn(products);

        // Call the method under test
        discountController.deleteDiscountById(discountId);

        // Verify interactions with the mocks
        verify(discountRepository, times(1)).findById(discountId);
        verify(productRepository, times(1)).findByDiscount_Id(discountId);
        verify(productRepository, times(1)).saveAll(products);
        verify(discountRepository, times(1)).deleteById(discountId);
    }

}
