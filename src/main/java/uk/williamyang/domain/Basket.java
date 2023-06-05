package uk.williamyang.domain;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
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

    @OneToMany(cascade = CascadeType.ALL)
    private List<BasketItem> items = new ArrayList<>();

    @NonNull
    @Column
    private BigDecimal total;

    @ManyToOne(cascade = CascadeType.DETACH)
    private Customer customer;

}
