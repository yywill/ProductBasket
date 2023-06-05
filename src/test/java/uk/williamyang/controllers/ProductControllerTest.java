package uk.williamyang.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import uk.williamyang.domain.Product;
import uk.williamyang.repo.ProductRepository;

import org.assertj.core.api.Assertions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class ProductControllerTest {

    private static final String BASE_URL = "/admin/api/products";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductRepository productRepository;

    private List<Product> products;
    private Long cursor;

    @BeforeEach
    void setUp() {
        products = List.of(
                new Product("P1", "Product 1", new BigDecimal("10.00"), "USD"),
                new Product("P2", "Product 2", new BigDecimal("20.00"), "USD"),
                new Product("P3", "Product 3", new BigDecimal("30.00"), "USD")
        );
        cursor = 1L;
    }

    @Test
    void getAllProducts_shouldReturnListOfProducts() throws Exception {
        // Arrange
        when(productRepository.findAll(PageRequest.of(0, 20)))
                .thenReturn(new PageImpl<>(products));
        // Act
        MvcResult result = mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andReturn();

        log.info("page result: {}", result.getResponse().getContentAsString());
        // Assert
        List<Product> resultProducts = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<Product>>() {});
        Assertions.assertThat(resultProducts).isEqualTo(products);
    }

    @Test
    void getAllProducts_shouldReturnPaginatedListOfProducts() throws Exception {
        // Arrange
        List<Product> expectedProducts = products.subList(1, 3);
        Page<Product> expectedPage = new PageImpl<>(expectedProducts);
        when(productRepository.findAllByIdGreaterThan(eq(cursor), any(Pageable.class)))
                .thenReturn(expectedPage);
        // Act
        MvcResult result = mockMvc.perform(get(BASE_URL + "?cursor=" + cursor))
                .andExpect(status().isOk())
                .andReturn();
        // Assert
        List<Product> resultProducts = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<Product>>() {});
        Assertions.assertThat(resultProducts).isEqualTo(expectedProducts);
    }

    @Test
    void createProduct_shouldReturnCreatedProduct() throws Exception {
        // Arrange
        Product newProduct = new Product("P4", "Product 4", new BigDecimal("40.00"), "USD");
        when(productRepository.save(any(Product.class))).thenReturn(newProduct);
        // Act
        MvcResult result = mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andExpect(status().isCreated())
                .andReturn();
        // Assert
        Product responseProduct = objectMapper.readValue(
                result.getResponse().getContentAsString(), Product.class);
        Assertions.assertThat(responseProduct).isEqualTo(newProduct);
    }

    @Test
    void deleteProduct_shouldDeleteProductAndReturnNoContent() throws Exception {
        // Arrange
        Long idToDelete = 1L;
        doNothing().when(productRepository).deleteById(eq(idToDelete));
        // Act
        MvcResult result = mockMvc.perform(delete(BASE_URL + "/" + idToDelete))
                .andExpect(status().isNoContent())
                .andReturn();
        // Assert
        Assertions.assertThat(result.getResponse().getContentAsString()).isEmpty();
        verify(productRepository).deleteById(eq(idToDelete));
    }
}
