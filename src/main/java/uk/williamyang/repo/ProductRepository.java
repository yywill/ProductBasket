package uk.williamyang.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.williamyang.domain.Discount;
import uk.williamyang.domain.Product;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByCode(String code);
    Page<Product> findAllByIdGreaterThan(Long id, Pageable pageable);

    List<Product> findByDiscount_Code(String code);

    List<Product> findByDiscount_Id(Long id);
}
