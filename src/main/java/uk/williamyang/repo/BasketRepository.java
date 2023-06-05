package uk.williamyang.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.williamyang.domain.Basket;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {

    Optional<Basket> findByCustomerId(Long customerId);

    Optional<Basket> findByCustomer_ApiKey(String apiKey);

    Optional<Basket> findByCode(String code);
}
