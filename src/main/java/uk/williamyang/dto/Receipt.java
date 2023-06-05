package uk.williamyang.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receipt {
    private String clientName;
    private LocalDate date;
    private int totalItems;
    private int totalQuantity;
    private List<ReceiptItem> items;
    private BigDecimal total;
}