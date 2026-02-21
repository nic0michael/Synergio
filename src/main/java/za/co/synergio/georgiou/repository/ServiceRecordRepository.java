package za.co.synergio.georgiou.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import za.co.synergio.georgiou.model.ServiceRecord;

public interface ServiceRecordRepository extends JpaRepository<ServiceRecord, Integer> {
    List<ServiceRecord> findByDaysLeftLessThan(int days);
}
