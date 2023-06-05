package uk.williamyang.domain;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_baskets_items")
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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "basket_id")
    @JsonIgnore
    private Basket basket;

}
