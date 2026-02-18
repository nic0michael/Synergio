package za.co.synergio.georgiou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import za.co.synergio.georgiou.model.Customer;
import za.co.synergio.georgiou.model.CustomerVehicle;
import za.co.synergio.georgiou.model.ServiceRecord;
import za.co.synergio.georgiou.storage.CsvStorage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class MvcController {
	
	private static final Logger log = LoggerFactory.getLogger(MvcController.class);

    private final CsvStorage csvStorage;

    @Autowired
    public MvcController(CsvStorage csvStorage) {
        this.csvStorage = csvStorage;
    }

@GetMapping("/")
    public String help(Model model) {
    	log.info("help method called");
        return "help";
    }

    @GetMapping("/serviceform")
    public String form(Model model) {
    	log.info("form method called");
        LocalDate utcDate = LocalDate.now(ZoneOffset.UTC);
        ServiceRecord record = new ServiceRecord();
        record.setDate(utcDate);
        model.addAttribute("serviceRecord", record);
        return "form";
    }

    @GetMapping("/servicevehicle")
    public String createServiceByCustomerAndVehicle(
            @RequestParam(value = "customerId", required = false) Integer customerId, 
            @RequestParam(value = "vehicleId", required = false) Integer vehicleId, 
            Model model) throws IOException {

        log.info("createServiceByCustomerAndVehicle called with customerId: " + customerId + ", vehicleId: " + vehicleId);

        if (customerId == null || vehicleId == null) {
            log.error("createServiceByCustomerAndVehicle called with missing parameters");
            return "redirect:/help";
        }
        
        Customer customer = null;
        List<Customer> customers = csvStorage.readAllCustomers();
        for (Customer c : customers) {
            if (c.getIndex() == customerId) {
                customer = c;
                break;
            }
        }
       
        CustomerVehicle vehicle = null;
        List<CustomerVehicle> vehicles = csvStorage.readAllVehicles();
        for (CustomerVehicle v : vehicles) {
            if (v.getIndex() == vehicleId) {
                vehicle =v;
                break;
            }
        }

        ServiceRecord record = new ServiceRecord();
        record.setDate(LocalDate.now(ZoneOffset.UTC));
        
        if (customer != null) {
            record.setCustomerName(customer.getCustomerName());
            record.setCellphone(customer.getCellphone());
            record.setCustomerAddress(customer.getCustomerAddress());
        } else {
            log.error("Customer not found for customerId: " + customerId);
            return "redirect:/help";
        }
        
        if (vehicle != null) {
        	record.setVehicleRegNumber(vehicle.getVehicleRegNumber());
        	record.setVehicleMakeAnModel(vehicle.getVehicleMakeAnModel());
        	record.setColour(vehicle.getColour());
        	record.setVinNumber(vehicle.getVinNumber());
        } else {
            log.error("Vehicle not found for vehicleId: " + vehicleId);
            return "redirect:/help";
        }
        
        model.addAttribute("serviceRecord", record);
        model.addAttribute("customerId", customerId);
        model.addAttribute("vehicleId", vehicleId);

        log.info("Returning form view for createServiceByCustomerAndVehicle");
        return "form";
    }

    @GetMapping("/editRecord")
    public String edit(@RequestParam("index") int index, Model model) throws IOException {
        log.info("form edit called");
        List<ServiceRecord> records = csvStorage.readAll();
        ServiceRecord record = records.stream()
                .filter(r -> r.getIndex() == index)
                .findFirst()
                .orElse(null);

        if (record == null) {
            record = new ServiceRecord();
            record.setDate(LocalDate.now(ZoneOffset.UTC));
        } else {
        	record.setIndex(index);
        }
        log.info("Record:\n"+record.toString());

        model.addAttribute("serviceRecord", record);
        return "edit";
    }
    
    
    @GetMapping("/activateRecord") 
    public String activateRecord(@RequestParam("index") int index)  throws IOException {
    	log.info("deleteRecord called for index: " + index);
        List<ServiceRecord> allRecords = csvStorage.readAll();
        
        for (ServiceRecord serviceRecord : allRecords) {
			if(serviceRecord.getIndex()== index) {
				serviceRecord.setStatel(0);
				log.info("Found and update record "+index + "\n"+serviceRecord);
				csvStorage.update(serviceRecord);
                break;
			}
		}
    	
        return "redirect:/records"; 
    }
    
    
    @GetMapping("/completeRecord") 
    public String completeRecord(@RequestParam("index") int index)  throws IOException {
    	log.info("deleteRecord called for index: " + index);
        List<ServiceRecord> allRecords = csvStorage.readAll();
        
        for (ServiceRecord serviceRecord : allRecords) {
			if(serviceRecord.getIndex()== index) {
				serviceRecord.setStatel(1);
				log.info("Found and update record "+index + "\n"+serviceRecord);
				csvStorage.update(serviceRecord);
                break;
			}
		}
    	
        return "redirect:/records"; 
    }
    
    
    @GetMapping("/deleteRecord") 
    public String deleteRecord(@RequestParam("index") int index)  throws IOException {
    	log.info("deleteRecord called for index: " + index);
        List<ServiceRecord> allRecords = csvStorage.readAll();
        
        for (ServiceRecord serviceRecord : allRecords) {
			if(serviceRecord.getIndex()== index) {
				serviceRecord.setStatel(3);
				log.info("Found and update record "+index + "\n"+serviceRecord);
				csvStorage.update(serviceRecord);
                break;
			}
		}
    	
        return "redirect:/records"; 
    }
    


    
    @PostMapping("/update")
    public String update(@ModelAttribute ServiceRecord serviceRecord, BindingResult bindingResult, Model model) throws IOException {
    	log.info("update method called Record: \n"+serviceRecord.toString());

        if (serviceRecord.getDate() == null)
            bindingResult.rejectValue("date", "date.empty", "Date is required");
        if (serviceRecord.getCustomerName() == null || serviceRecord.getCustomerName().isBlank())
            bindingResult.rejectValue("customerName", "name.empty", "Customer name is required");

        if (bindingResult.hasErrors()) {
            model.addAttribute("serviceRecord", serviceRecord);
            return "form";
        }

        if (serviceRecord.getAmount() == null)
            serviceRecord.setAmount(BigDecimal.ZERO);

        if (serviceRecord.getServiceDate() != null) {
            LocalDate today = LocalDate.now(ZoneOffset.UTC);
            int days = (int) today.until(serviceRecord.getServiceDate()).getDays();
            serviceRecord.setDaysLeft(days);
        }

        if (serviceRecord.getInterval() > 0 && serviceRecord.getDate() != null) {
            LocalDate recurring = serviceRecord.getDate().plusDays(serviceRecord.getInterval());
            serviceRecord.setRecurringDate(recurring);
        }

        csvStorage.update(serviceRecord); // <-- call the update method

        return "redirect:/";
    }


    @GetMapping("/publish") 
    public String publish(Model model) throws IOException {
    	log.info("records method called");
        csvStorage.publish();
        return "redirect:/";
    }
    
    

    @PostMapping("/submit")
    public String submit(@ModelAttribute ServiceRecord serviceRecord, BindingResult bindingResult, Model model)
            throws IOException {
    	log.info("submit method called");

        if (serviceRecord.getDate() == null)
            bindingResult.rejectValue("date", "date.empty", "Date is required");
        if (serviceRecord.getCustomerName() == null || serviceRecord.getCustomerName().isBlank())
            bindingResult.rejectValue("customerName", "name.empty", "Customer name is required");

        if (bindingResult.hasErrors()) {
            model.addAttribute("serviceRecord", serviceRecord);
            return "form";
        }

        if (serviceRecord.getAmount() == null)
            serviceRecord.setAmount(BigDecimal.ZERO);

        if (serviceRecord.getServiceDate() != null) {
            LocalDate today = LocalDate.now(ZoneOffset.UTC);
            int days = (int) today.until(serviceRecord.getServiceDate()).getDays();        	
            serviceRecord.setDaysLeft(days);
        }
        
        
        if (serviceRecord.getInterval() > 0 && serviceRecord.getDate() != null) {
            LocalDate recurring = serviceRecord.getDate().plusDays(serviceRecord.getInterval());
            serviceRecord.setRecurringDate(recurring);
        }

        csvStorage.save(serviceRecord);
        return "redirect:/";
    }

    @GetMapping("/api/records")
    @ResponseBody
    public List<ServiceRecord> apiRecords() throws IOException {
    	log.info("apiRecords method called");
        return csvStorage.readAll();
    }

    @GetMapping("/records")
    public String records(Model model) throws IOException {
    	log.info("records method called");
        List<ServiceRecord> records = csvStorage.readAll();
//        Collections.reverse(records);
        model.addAttribute("records", records);
        return "records";
    }
    
    @GetMapping("/40DAYS")
    public String records40Days(Model model) throws IOException {
    	log.info("40days method called");
        List<ServiceRecord> allRecords = csvStorage.readAll();
        
        // Filter records with daysLeft < 41
        List<ServiceRecord> filtered = new ArrayList<>(
        	    allRecords.stream()
        	        .filter(r -> r.getDaysLeft() < 41 && r.getDaysLeft() > 0)
        	        .toList()
        	);
        // Reverse so newest records appear first
//        Collections.reverse(filtered);

        model.addAttribute("records", filtered);
        return "40days"; 
    }
    

    @GetMapping("/10DAYS")
    public String records10Days(Model model) throws IOException {
    	log.info("10days method called");
        List<ServiceRecord> allRecords = csvStorage.readAll();
        
        // Filter records with daysLeft < 11
        List<ServiceRecord> filtered = new ArrayList<>(
        	    allRecords.stream()
        	        .filter(r -> r.getDaysLeft() < 11 && r.getDaysLeft() > 0)
        	        .toList()
        	);

        // Reverse so newest records appear first
//        Collections.reverse(filtered);

        model.addAttribute("records", filtered);
        return "10days"; 
        
        
        
    }
    
    @GetMapping("/today")
    public String recordsToday(Model model) throws IOException {
    	log.info("today method called");
            List<ServiceRecord> allRecords = csvStorage.readAll();
            
            // Filter records with daysLeft < 2
            List<ServiceRecord> filtered = allRecords.stream()
                    .filter(r -> r.getDaysLeft() < 1 && r.getDaysLeft() >-4)
                    .toList();

            // Reverse so newest records appear first
//            Collections.reverse(filtered);

            model.addAttribute("records", filtered);
            return "today"; 
    }

    @GetMapping("/help")
    public String help() {
    	log.info("help method called");
        return "help";
    }
    
    @GetMapping("/createcustomer")
    public String createCustomer(Model model) {
        log.info("createCustomer method called");
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "createcustomer";
    }
    
    @GetMapping("/editcustomer")
    public String editCustomer(Model model) {
        log.info("editCustomer method called");
        Customer customer = new Customer();
        model.addAttribute("customer", customer);
        return "editcustomer";
    }
    
    @PostMapping("/saveCustomer")
    public String saveCustomer(@ModelAttribute Customer customer) throws IOException {
        log.info("saveCustomer method called");
        csvStorage.saveCustomer(customer);
        log.info("saved Customer");
        return "redirect:/";
    }
    
    @GetMapping("/createvehicle")
    public String createVehicle(Model model) {
        log.info("createVehicle method called");
        CustomerVehicle vehicle = new CustomerVehicle();
        model.addAttribute("customerVehicle", vehicle);
        return "createvehicle";
    }
    
    @GetMapping("/editthevehicle")
    public String editVehicle(@RequestParam(value = "vehicleId", required = true) Integer vehicleId, Model model) throws IOException { // added IOException for csvStorage
        log.info("editVehicle method called for vehicleId: " + vehicleId);
        
        CustomerVehicle vehicle = null;
        List<CustomerVehicle> vehicles = csvStorage.readAllVehicles();
        for (CustomerVehicle v : vehicles) {
            if (v.getIndex() == vehicleId) {
                vehicle = v;
                break;
            }
        }
        
        if (vehicle == null) {
        	log.error("Vehicle not found with id: " + vehicleId);
        	return "redirect:/help";
        }
        
        model.addAttribute("customerVehicle", vehicle);
        return "editvehicle";
    }
    
    @PostMapping("/saveVehicle")
    public String saveVehicle(@ModelAttribute CustomerVehicle vehicle) throws IOException {
        log.info("saveVehicle method called");
        csvStorage.saveVehicle(vehicle);
        log.info("saved Vehicle");
        return "redirect:/";
    }

    @GetMapping("/customerRecords")
    public String customerRecords(Model model) throws IOException {
        log.info("customerRecords method called");
        List<Customer> customers = csvStorage.readAllCustomers();
        model.addAttribute("customers", customers);
        return "recordsOfCustomers";
    }

    @GetMapping("/vehicleRecords")
    public String vehicleRecords(Model model) throws IOException {
        log.info("vehicleRecords method called");
        List<CustomerVehicle> vehicles = csvStorage.readAllVehicles();
        model.addAttribute("vehicles", vehicles);
        return "recordsOfVehicles";
    }

    @GetMapping("/listCustomerOptions")
    @ResponseBody
    public List<Map<String, String>> listCustomerOptions() throws IOException {
        log.info("listCustomerOptions method called");
        List<Customer> customers = csvStorage.readAllCustomers();
        List<Map<String, String>> options = new ArrayList<>();
        
        for (Customer customer : customers) {
            Map<String, String> option = new HashMap<>();
            option.put("option", customer.getCustomerName());
            option.put("value", String.valueOf(customer.getIndex()));
            options.add(option);
        }
        
        return options;
    }

    @GetMapping("/listVehicleOptions")
    @ResponseBody
    public List<Map<String, String>> listVehicleOptions() throws IOException {
        log.info("listVehicleOptions method called");
        List<CustomerVehicle> vehicles = csvStorage.readAllVehicles();
        List<Map<String, String>> options = new ArrayList<>();
        
        for (CustomerVehicle vehicle : vehicles) {
            String vehicleOption = vehicle.getCustomerName() + " " + 
                                   vehicle.getVehicleRegNumber() + " " + 
                                   vehicle.getVehicleMakeAnModel() + " " + 
                                   vehicle.getColour();
            Map<String, String> option = new HashMap<>();
            option.put("option", vehicleOption);
            option.put("value", String.valueOf(vehicle.getIndex()));
            options.add(option);
        }
        
        return options;
    }

    @GetMapping("/searchForCustomer")
    @ResponseBody
    public Customer searchForCustomer(@RequestParam("index") int index) throws IOException {
        log.info("searchForCustomer method called with index: " + index);
        return csvStorage.readAllCustomers().stream()
            .filter(c -> c.getIndex() == index)
            .findFirst()
            .orElse(null);
    }

    @GetMapping("/searchForCustomerVehicle")
    @ResponseBody
    public CustomerVehicle searchForCustomerVehicle(@RequestParam("index") int index) throws IOException {
        log.info("searchForCustomerVehicle method called with index: " + index);
        return csvStorage.readAllVehicles().stream()
            .filter(v -> v.getIndex() == index)
            .findFirst()
            .orElse(null);
    }

    @GetMapping("/profile")
    public String profile() {
        log.info("profile method called");
        return "profile";
    }

    @GetMapping("/customers")
    public String getCustomers(Model model) throws IOException {
        log.info("getCustomers method called");
        List<Customer> customers = csvStorage.readAllCustomers();
        sortCustomersByName(customers);
        model.addAttribute("customers", customers);
        return "selectacustomer";
    }

    @GetMapping("/getcustvehicles")
    public String getCustVehicles(Model model) throws IOException {
    	log.info("getCustVehicles method called");
        List<Customer> customers = csvStorage.readAllCustomers();
        sortCustomersByName(customers);
        model.addAttribute("customers", customers);
        return "getcustvehicles"; 
    }

    private void sortCustomersByName(List<Customer> customers) {
        Collections.sort(customers, (c1, c2) -> {
            String n1 = c1.getCustomerName() != null ? c1.getCustomerName() : "";
            String n2 = c2.getCustomerName() != null ? c2.getCustomerName() : "";
            return n1.compareToIgnoreCase(n2);
        });
    }

    @GetMapping("/getrequestvehicles")
    public String getRequestVehicles(@RequestParam("customerId") int customerId, Model model) throws IOException {
    	log.info("getRequestVehicles method called with customerId: " + customerId);
        List<CustomerVehicle>allVehicles = csvStorage.readAllVehicles();
        List<CustomerVehicle> vehicles = new ArrayList<>();
        model.addAttribute("vehicles", vehicles);
        for (CustomerVehicle vehicle : allVehicles) {
            if (vehicle.getCustomerId() == customerId) {
                vehicles.add(vehicle);
            }
        }
        model.addAttribute("vehicles", vehicles);
        
        Customer customer = null;
        List<Customer> customers = csvStorage.readAllCustomers();
        for (Customer c : customers) {
            if (c.getIndex() == customerId) {
                customer = c;
                break;
            }
        }
        model.addAttribute("customer", customer);
        
        return "displaycustvehicles"; 
    }

    @GetMapping("/customervehiclesbycust")
    public String getCustomerVehiclesByCustomer(@RequestParam("customerId") int customerId, Model model) throws IOException {
        log.info("Processing vehicle list request for customerId: " + customerId);
        List<CustomerVehicle> allVehicles = csvStorage.readAllVehicles();
        List<CustomerVehicle> vehicles = new ArrayList<>();
        
        for (CustomerVehicle vehicle : allVehicles) {
            if (vehicle.getCustomerId() == customerId) {
                vehicles.add(vehicle);
            }
        }

        Customer customer = null;
        List<Customer> customers = csvStorage.readAllCustomers();
        for (Customer c : customers) {
            if (c.getIndex() == customerId) {
                customer = c;
                break;
            }
        }
        
        model.addAttribute("vehicles", vehicles);
        model.addAttribute("customer", customer);

        return "displaycustvehicles"; 
    }

    @GetMapping("/customerbycust")
    public String getCustomerByCustomer(@RequestParam("customerId") int customerId, Model model) throws IOException {
        log.info("Processing customer detail request for customerId: " + customerId);
        Customer customer = null;
        List<Customer> customers = csvStorage.readAllCustomers();
        for (Customer  cust : customers) {
            if(cust.getIndex()==customerId){
               customer = cust;
               break;
            }
        }
        model.addAttribute("customer", customer);
        return "displaycustomerbycustomerId";
    }

}
