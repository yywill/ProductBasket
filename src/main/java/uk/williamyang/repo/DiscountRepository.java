package uk.williamyang.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.williamyang.domain.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {

}
