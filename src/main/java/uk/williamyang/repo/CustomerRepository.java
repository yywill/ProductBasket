package uk.williamyang.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uk.williamyang.domain.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByUserName(String userName);

    Optional<Customer> findByApiKey(String apiKey);
}
