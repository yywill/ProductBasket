package uk.williamyang.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "t_products")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
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
}
