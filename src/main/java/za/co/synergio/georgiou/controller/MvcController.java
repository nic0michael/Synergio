package za.co.synergio.georgiou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import za.co.synergio.georgiou.model.ServiceRecord;
import za.co.synergio.georgiou.storage.CsvStorage;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
    public String form(Model model) {
    	log.info("form method called");
        LocalDate utcDate = LocalDate.now(ZoneOffset.UTC);
        ServiceRecord record = new ServiceRecord();
        record.setDate(utcDate);
        model.addAttribute("serviceRecord", record);
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
    

}
