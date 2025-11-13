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
        LocalDate utcDate = LocalDate.now(ZoneOffset.UTC);
        ServiceRecord record = new ServiceRecord();
        record.setDate(utcDate);
        model.addAttribute("serviceRecord", record);
        return "form";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute ServiceRecord serviceRecord, BindingResult bindingResult, Model model)
            throws IOException {

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

        if (serviceRecord.getServiceDate() == null)
            serviceRecord.setServiceDate(serviceRecord.getDate());

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
        return csvStorage.readAll();
    }

    @GetMapping("/records")
    public String records(Model model) throws IOException {
        List<ServiceRecord> records = csvStorage.readAll();
        Collections.reverse(records);
        model.addAttribute("records", records);
        return "records";
    }

    @GetMapping("/help")
    public String help() {
        return "help";
    }
}
