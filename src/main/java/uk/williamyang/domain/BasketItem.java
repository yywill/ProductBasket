package uk.williamyang.domain;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_basket_items")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class BasketItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private String code;

    @NonNull
    @ManyToOne
    private Product product;

    @NonNull
    @Column
    private Integer quantity;

    @NonNull
    @ManyToOne
    private Basket basket;

}
