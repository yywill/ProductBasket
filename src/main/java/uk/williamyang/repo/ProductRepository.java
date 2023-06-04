package uk.williamyang.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.williamyang.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
