package za.co.synergio.georgiou.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class ServiceRecord {
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
    private String customerName;
    private String cellphone;
    private String vehicleRegNumber;
    private String audometerReading;
    private String vinNumber;
    private String documentType;              // Quotation / Invoice
    private String requirementCategory;       // Oil Change / Service / etc.
    private String interval;                  // Days / Months / KMs combined
    private String materialsRequired;
    private double labourHours;
    private BigDecimal amount;
    private String breakdown;

    public ServiceRecord() {}

    // --- Getters and Setters ---
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCellphone() { return cellphone; }
    public void setCellphone(String cellphone) { this.cellphone = cellphone; }

    public String getVehicleRegNumber() { return vehicleRegNumber; }
    public void setVehicleRegNumber(String vehicleRegNumber) { this.vehicleRegNumber = vehicleRegNumber; }

    public String getAudometerReading() { return audometerReading; }
    public void setAudometerReading(String audometerReading) { this.audometerReading = audometerReading; }

    public String getVinNumber() { return vinNumber; }
    public void setVinNumber(String vinNumber) { this.vinNumber = vinNumber; }

    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }

    public String getRequirementCategory() { return requirementCategory; }
    public void setRequirementCategory(String requirementCategory) { this.requirementCategory = requirementCategory; }

    public String getInterval() { return interval; }
    public void setInterval(String interval) { this.interval = interval; }

    public String getMaterialsRequired() { return materialsRequired; }
    public void setMaterialsRequired(String materialsRequired) { this.materialsRequired = materialsRequired; }

    public double getLabourHours() { return labourHours; }
    public void setLabourHours(double labourHours) { this.labourHours = labourHours; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getBreakdown() { return breakdown; }
    public void setBreakdown(String breakdown) { this.breakdown = breakdown; }

    @Override
    public String toString() {
        return "ServiceRecord{" +
                "date=" + date +
                ", customerName='" + customerName + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", vehicleRegNumber='" + vehicleRegNumber + '\'' +
                ", audometerReading='" + audometerReading + '\'' +
                ", vinNumber='" + vinNumber + '\'' +
                ", documentType='" + documentType + '\'' +
                ", requirementCategory='" + requirementCategory + '\'' +
                ", interval='" + interval + '\'' +
                ", materialsRequired='" + materialsRequired + '\'' +
                ", labourHours=" + labourHours +
                ", amount=" + amount +
                ", breakdown='" + breakdown + '\'' +
                '}';
    }
}
