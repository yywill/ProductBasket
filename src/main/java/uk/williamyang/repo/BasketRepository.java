package uk.williamyang.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.williamyang.domain.Basket;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

}
