package uk.williamyang.domain;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "t_baskets")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column
    private String code;

    @NonNull
    @OneToMany(cascade = CascadeType.PERSIST)
    private List<BasketItem> items;

    @NonNull
    @Column
    private Integer total;

}
