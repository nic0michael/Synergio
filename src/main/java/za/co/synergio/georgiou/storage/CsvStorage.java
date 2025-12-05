package za.co.synergio.georgiou.storage;

import java.io.IOException;
import java.util.List;

import za.co.synergio.georgiou.model.Customer;
import za.co.synergio.georgiou.model.CustomerVehicle;
import za.co.synergio.georgiou.model.ServiceRecord;

public interface CsvStorage {
	List<ServiceRecord> readAll() throws IOException;
	
	List<Customer> readAllCustomers() throws IOException;
	
	List<CustomerVehicle> readAllVehicles() throws IOException;

	void save(ServiceRecord record) throws IOException;
	
	void update(ServiceRecord record) throws IOException;
	
	List<ServiceRecord> readAllDueIn40Days() throws IOException;

	void publish() throws IOException;
	
	void saveCustomer(Customer c) throws IOException;
	
	void updateCustomer(Customer c) throws IOException;
		
	void saveVehicle(CustomerVehicle v) throws IOException;
	
	void updateVehicle(CustomerVehicle v) throws IOException;
}
