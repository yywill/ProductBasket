package uk.williamyang.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.williamyang.domain.BasketItem;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

}
