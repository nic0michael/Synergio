package za.co.synergio.georgiou.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import za.co.synergio.georgiou.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
}
