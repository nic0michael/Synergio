package za.co.synergio.georgiou.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class ServiceRecord {
	private int index;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate serviceDate;
	
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate recurringDate;
	
    private String customerName;
    private String cellphone;
    private String vehicleRegNumber;
    private String odometerReading;   
    private String vinNumber;
    private String documentType;
    private String requirementCategory;
    private int interval;
    private int daysLeft;
    private String materialsRequired;
    private double labourHours;
    private BigDecimal amount;
    private String breakdown;
    private int state; //0=active 1=completed 3=deleted

    // --- Getters and Setters ---
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalDate getServiceDate() { return serviceDate; }
    public void setServiceDate(LocalDate serviceDate) { this.serviceDate = serviceDate; }

    public LocalDate getRecurringDate() { return recurringDate; }
    public void setRecurringDate(LocalDate recurringDate) { this.recurringDate = recurringDate; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCellphone() { return cellphone; }
    public void setCellphone(String cellphone) { this.cellphone = cellphone; }

    public String getVehicleRegNumber() { return vehicleRegNumber; }
    public void setVehicleRegNumber(String vehicleRegNumber) { this.vehicleRegNumber = vehicleRegNumber; }

    public String getOdometerReading() { return odometerReading; }
    public void setOdometerReading(String odometerReading) { this.odometerReading = odometerReading; }

    public String getVinNumber() { return vinNumber; }
    public void setVinNumber(String vinNumber) { this.vinNumber = vinNumber; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getRequirementCategory() { return requirementCategory; }
    public void setRequirementCategory(String requirementCategory) { this.requirementCategory = requirementCategory; }

    public int getInterval() { return interval; }
    public void setInterval(int interval) { this.interval = interval; }
    

    public int getDaysLeft() { return daysLeft; }
    public void setDaysLeft(int daysLeft) { this.daysLeft = daysLeft; }


    public String getMaterialsRequired() { return materialsRequired; }
    public void setMaterialsRequired(String materialsRequired) { this.materialsRequired = materialsRequired; }

    public double getLabourHours() { return labourHours; }
    public void setLabourHours(double d) { this.labourHours = d; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getBreakdown() { return breakdown; }
    public void setBreakdown(String breakdown) { this.breakdown = breakdown; }
    

    public int getState() { return state; }
    public void setStatel(int state) { this.state = state; }
}
