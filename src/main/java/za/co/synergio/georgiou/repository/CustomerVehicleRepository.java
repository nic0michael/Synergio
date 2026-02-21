package za.co.synergio.georgiou.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.synergio.georgiou.model.CustomerVehicle;

public interface CustomerVehicleRepository extends JpaRepository<CustomerVehicle, Integer> {
    List<CustomerVehicle> findByCustomerId(int customerId);
}
