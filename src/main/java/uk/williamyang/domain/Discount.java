package uk.williamyang.domain;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "t_discounts")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private String code;

    @Column
    private String name;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Product product;

    @NonNull
    @Column
    private Double discountPercentage;
}
