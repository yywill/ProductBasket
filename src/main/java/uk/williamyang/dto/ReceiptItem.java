package uk.williamyang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ReceiptItem {
    private String name;
    private int quantity;
    private BigDecimal price;
    private Discount discount;
    private BigDecimal subtotal;
}

