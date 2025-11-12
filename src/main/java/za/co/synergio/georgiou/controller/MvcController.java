package za.co.synergio.georgiou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import za.co.synergio.georgiou.model.ServiceRecord;
import za.co.synergio.georgiou.storage.CsvStorage;

//import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;

@Controller
public class MvcController {

	private final CsvStorage csvStorage;

	@Autowired
	public MvcController(CsvStorage csvStorage) {
		this.csvStorage = csvStorage;
	}
	
	@GetMapping("/")
	public String form(Model model) {
	    // Get the current UTC date (same worldwide)
	    LocalDate utcDate = LocalDate.now(ZoneOffset.UTC);
	    
	    ServiceRecord record = new ServiceRecord();
	    record.setDate(utcDate);
	    model.addAttribute("serviceRecord", record);
	    
	    return "form";
	}



	@PostMapping("/submit")
	public String submit(@ModelAttribute ServiceRecord serviceRecord, BindingResult bindingResult, Model model)
			throws IOException {
// Basic validation (can be expanded)
		if (serviceRecord.getDate() == null) {
			bindingResult.rejectValue("date", "date.empty", "Date is required");
		}
		if (serviceRecord.getCustomerName() == null || serviceRecord.getCustomerName().isBlank()) {
			bindingResult.rejectValue("customerName", "name.empty", "Customer name is required");
		}
		if (bindingResult.hasErrors()) {
			model.addAttribute("serviceRecord", serviceRecord);
			return "form";
		}

// Ensure the amount is non-null
		if (serviceRecord.getAmount() == null)
			serviceRecord.setAmount(BigDecimal.ZERO);

		csvStorage.save(serviceRecord);
		return "redirect:/";
	}

	@GetMapping("/api/records")
	@ResponseBody
	public List<ServiceRecord> apiRecords() throws IOException {
		return csvStorage.readAll();
	}
	
	@GetMapping("/records")
	public String records(Model model) throws IOException {
	    List<ServiceRecord> records = csvStorage.readAll();
	    Collections.reverse(records); // last record first
	    model.addAttribute("records", records);
	    return "records";
	}
	
	@GetMapping("/help")
	public String help() {
		return "help"; // help.html
	}

}