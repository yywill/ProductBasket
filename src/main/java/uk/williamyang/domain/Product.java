package uk.williamyang.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "t_products")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    String code;

    @NonNull
    @Column
    String name;

    @NonNull
    @Column
    BigDecimal price;

    @NonNull
    @Column
    String currency;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "discount_id")
    private Discount discount;
}
