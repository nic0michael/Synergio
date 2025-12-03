package za.co.synergio.georgiou.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
    private String colour;
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
    private String customerAddress;
    private String vehicleMakeAnModel;
    private List<String> jobsToDo;

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
    public void setCustomerName(String customerName) { this.customerName = replaceCommaWithSemicolon(customerName); }

    public String getCellphone() { return cellphone; }
    public void setCellphone(String cellphone) { this.cellphone = cellphone; }

    public String getVehicleRegNumber() { return vehicleRegNumber; }
    public void setVehicleRegNumber(String vehicleRegNumber) { this.vehicleRegNumber = vehicleRegNumber; }  

    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }

    public String getOdometerReading() { return odometerReading; }
    public void setOdometerReading(String odometerReading) { this.odometerReading = odometerReading; }

    public String getVinNumber() { return vinNumber; }
    public void setVinNumber(String vinNumber) { this.vinNumber = replaceCommaWithSemicolon(vinNumber); }

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
    
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = replaceCommaWithSemicolon(customerAddress);
    }

    public String getVehicleMakeAnModel() {
        return vehicleMakeAnModel;
    }
    
    public void setVehicleMakeAnModel(String vehicleMakeAnModel) {
        this.vehicleMakeAnModel = replaceCommaWithSemicolon(vehicleMakeAnModel);
    }


    public List<String> getJobsToDo() {
        return jobsToDo;
    }

    public void setJobsToDo(List<String> jobsToDo) {
        this.jobsToDo = jobsToDo;
    }
    
    @Override
    public String toString() {
        return "ServiceRecord{" +
                "index=" + index +
                ", state=" + state +
                ", date=" + date +
                ", serviceDate=" + serviceDate +
                ", recurringDate=" + recurringDate +
                ", customerName='" + customerName + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", vehicleRegNumber='" + vehicleRegNumber + '\'' +
                ", odometerReading='" + odometerReading + '\'' +
                ", vinNumber='" + vinNumber + '\'' +
                ", documentType='" + documentType + '\'' +
                ", requirementCategory='" + requirementCategory + '\'' +
                ", interval=" + interval +
                ", daysLeft=" + daysLeft +
                ", materialsRequired='" + materialsRequired + '\'' +
                ", labourHours=" + labourHours +
                ", amount=" + amount +
                ", breakdown='" + breakdown + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", vehicleMakeAnModel='" + vehicleMakeAnModel + '\'' +
                ", jobsToDo=" + (jobsToDo != null ? String.join(", ", jobsToDo) : "[]") +
                '}';
    }


    public static String replaceCommaWithSemicolon(String input) {
        if (input == null) {
            return null;
        }
        return input.replace(",", ";");
    }

}
