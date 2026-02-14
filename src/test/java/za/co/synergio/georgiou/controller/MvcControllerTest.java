package za.co.synergio.georgiou.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;
import za.co.synergio.georgiou.model.Customer;
import za.co.synergio.georgiou.model.CustomerVehicle;
import za.co.synergio.georgiou.model.ServiceRecord;
import za.co.synergio.georgiou.storage.CsvStorage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MvcControllerTest {

    private MvcController controller;
    private CsvStorageStub storageStub;

    @BeforeEach
    public void setUp() {
        storageStub = new CsvStorageStub();
        controller = new MvcController(storageStub);
    }

    @Test
    public void testGetCustVehicles() throws IOException {
        Model model = new ConcurrentModel();
        String viewName = controller.getCustVehicles(model);
        
        assertEquals("getcustvehicles", viewName);
        assertTrue(model.containsAttribute("customers"));
        List<Customer> customers = (List<Customer>) model.getAttribute("customers");
        assertEquals(2, customers.size());
        // Verify sorting (A-Z)
        assertEquals("Alpha", customers.get(0).getCustomerName());
        assertEquals("Beta", customers.get(1).getCustomerName());
    }
/*
    @Test
    public void testGetRequestVehicles() throws IOException {
        Model model = new ConcurrentModel();
        int customerId = 1;
        String viewName = controller.getRequestVehicles(customerId, model);
        
        assertEquals("displaycustvehicles", viewName);
        assertTrue(model.containsAttribute("vehicles"));
        List<CustomerVehicle> vehicles = (List<CustomerVehicle>) model.getAttribute("vehicles");
        assertEquals(1, vehicles.size());
        assertEquals("VW Polo", vehicles.get(0).getVehicleMakeAnModel());
    }
*/
    
    @Test
    public void testGetRequestVehicles_Empty() throws IOException {
        Model model = new ConcurrentModel();
        int customerId = 99; // Non-existent
        String viewName = controller.getRequestVehicles(customerId, model);
        
        assertEquals("displaycustvehicles", viewName);
        assertTrue(model.containsAttribute("vehicles"));
        List<CustomerVehicle> vehicles = (List<CustomerVehicle>) model.getAttribute("vehicles");
        assertTrue(vehicles.isEmpty());
    }

    @Test
    public void testSaveVehicle_BindsCustomerId() throws IOException {
        CustomerVehicle v = new CustomerVehicle();
        v.setCustomerId(123);
        v.setVehicleMakeAnModel("Test Car");
        
        String viewName = controller.saveVehicle(v);
        
        assertEquals("redirect:/", viewName);
        assertEquals(123, ((CsvStorageStub)storageStub).lastSavedVehicle.getCustomerId());
    }

    // Stub implementation
    static class CsvStorageStub implements CsvStorage {
        public CustomerVehicle lastSavedVehicle;

        @Override public void save(ServiceRecord serviceRecord) throws IOException {}
        @Override public void update(ServiceRecord serviceRecord) throws IOException {}
        @Override public List<ServiceRecord> readAll() throws IOException { return new ArrayList<>(); }
        @Override public void publish() throws IOException {}
        
        @Override public void saveCustomer(Customer customer) throws IOException {}
        @Override public void updateCustomer(Customer customer) throws IOException {}
        
        @Override public List<Customer> readAllCustomers() throws IOException {
            List<Customer> list = new ArrayList<>();
            Customer c2 = new Customer(); c2.setIndex(2); c2.setCustomerName("Beta");
            Customer c1 = new Customer(); c1.setIndex(1); c1.setCustomerName("Alpha");
            list.add(c2); // Unsorted
            list.add(c1);
            return list;
        }

        @Override public void saveVehicle(CustomerVehicle vehicle) throws IOException {
            this.lastSavedVehicle = vehicle;
        }
        @Override public void updateVehicle(CustomerVehicle vehicle) throws IOException {}
        @Override public List<CustomerVehicle> readAllVehicles() throws IOException { return new ArrayList<>(); }
        
        @Override public List<ServiceRecord> readAllDueIn40Days() throws IOException { return new ArrayList<>(); }
        
        @Override public List<CustomerVehicle> getCustomerVehicles(int customerId) throws IOException {
             List<CustomerVehicle> list = new ArrayList<>();
            if (customerId == 1) {
                CustomerVehicle v1 = new CustomerVehicle(); 
                v1.setCustomerId(1); 
                v1.setVehicleMakeAnModel("VW Polo");
                list.add(v1);
            }
            return list;
        }
    }
}
