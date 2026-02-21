package za.co.synergio.georgiou.storage;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import za.co.synergio.georgiou.model.Customer;
import za.co.synergio.georgiou.model.CustomerVehicle;
import za.co.synergio.georgiou.model.ServiceRecord;
import za.co.synergio.georgiou.repository.CustomerRepository;
import za.co.synergio.georgiou.repository.CustomerVehicleRepository;
import za.co.synergio.georgiou.repository.ServiceRecordRepository;

@Service("h2Storage")
@Primary
//@Profile("h2") // Optional: Use profiles to switch between implementations if needed
public class H2Storage implements CsvStorage {

    private final CustomerRepository customerRepository;
    private final CustomerVehicleRepository customerVehicleRepository;
    private final ServiceRecordRepository serviceRecordRepository;

    public H2Storage(CustomerRepository customerRepository, 
                     CustomerVehicleRepository customerVehicleRepository, 
                     ServiceRecordRepository serviceRecordRepository) {
        this.customerRepository = customerRepository;
        this.customerVehicleRepository = customerVehicleRepository;
        this.serviceRecordRepository = serviceRecordRepository;
    }

    @Override
    public List<ServiceRecord> readAll() throws IOException {
        return serviceRecordRepository.findAll();
    }

    @Override
    public List<Customer> readAllCustomers() throws IOException {
        return customerRepository.findAll();
    }

    @Override
    public List<CustomerVehicle> readAllVehicles() throws IOException {
        return customerVehicleRepository.findAll();
    }

    @Override
    public void save(ServiceRecord record) throws IOException {
        serviceRecordRepository.save(record);
    }

    @Override
    public void update(ServiceRecord record) throws IOException {
        // JPA save performs an update if the ID exists
        serviceRecordRepository.save(record);
    }

    @Override
    public List<ServiceRecord> readAllDueIn10Days() throws IOException {
        // Option 1: Fetch all and filter in memory (matching CsvStorageImpl logic)
        // return readAll().stream()
        //         .filter(r -> r.getDaysLeft() < 11)
        //         .sorted((a, b) -> Integer.compare(a.getDaysLeft(), b.getDaysLeft()))
        //         .collect(Collectors.toList());
        
        // Option 2: Use custom repository method if available (more efficient for DB)
         return serviceRecordRepository.findByDaysLeftLessThan(11);
    }

    @Override
    public List<ServiceRecord> readAllDueIn40Days() throws IOException {
        // Option 1: Fetch all and filter in memory (matching CsvStorageImpl logic)
        // return readAll().stream()
        //         .filter(r -> r.getDaysLeft() < 41)
        //         .sorted((a, b) -> Integer.compare(a.getDaysLeft(), b.getDaysLeft()))
        //         .collect(Collectors.toList());
        
        // Option 2: Use custom repository method if available (more efficient for DB)
         return serviceRecordRepository.findByDaysLeftLessThan(41);
    }

    @Override
    public void publish() throws IOException {
        // Publish logic might be specific to CSV export or some other business process.
        // If it was just dumping to a file "Published.csv", H2 implementation might:
        // 1. Do nothing (if it's just a file backup concept)
        // 2. Or actually write to a file if the requirement insists on generating a report.
        
        // For now, mirroring CsvStorageImpl intent but adapting to DB context.
        // If the requirement is strictly "generate a file", we might need to implement file writing here too.
        // However, usually moving to DB means we query the DB instead of reading "Published.csv".
        // Leaving empty or logging as typically this is a legacy artifact function.
        // System.out.println("Publish functionality moved to Database or Reporting Service");
    }

    @Override
    public void saveCustomer(Customer c) throws IOException {
        customerRepository.save(c);
    }

    @Override
    public void updateCustomer(Customer c) throws IOException {
        customerRepository.save(c);
    }

    @Override
    public void saveVehicle(CustomerVehicle v) throws IOException {
        customerVehicleRepository.save(v);
    }

    @Override
    public void updateVehicle(CustomerVehicle v) throws IOException {
        customerVehicleRepository.save(v);
    }

    @Override
    public List<CustomerVehicle> getCustomerVehicles(int customerId) throws IOException {
        // Assuming repository has this method, or we filter in memory
         return customerVehicleRepository.findByCustomerId(customerId);
		// If repository method missing, fallback:
        // return customerVehicleRepository.findAll().stream()
        //        .filter(v -> v.getCustomerId() == customerId)
        //        .collect(Collectors.toList());
    }
}
