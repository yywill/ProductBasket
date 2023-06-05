package uk.williamyang.domain;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.DETACH)
    private List<Product> product = new ArrayList<>();

    @NonNull
    @Column
    private BigDecimal discountPercentage;
}
