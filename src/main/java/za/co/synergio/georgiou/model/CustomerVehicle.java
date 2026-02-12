package za.co.synergio.georgiou.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class CustomerVehicle {
	private int index;
	private int customerId;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate date;
	
    private String customerName;
    private String cellphone;
    private String customerAddress;
    private String vehicleRegNumber;
    private String vehicleMakeAnModel;
    private String colour;
    private String vinNumber;
    private int state; //0=active 1=completed 3=deleted
    

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = replaceCommaWithSemicolon(customerName); }

    public String getCellphone() { return cellphone; }
    public void setCellphone(String cellphone) { this.cellphone = cellphone; }

    public String getVehicleRegNumber() { return vehicleRegNumber; }
    public void setVehicleRegNumber(String vehicleRegNumber) { this.vehicleRegNumber = vehicleRegNumber; }  

    public String getColour() { return colour; }
    public void setColour(String colour) { this.colour = colour; }

    public String getVinNumber() { return vinNumber; }
    public void setVinNumber(String vinNumber) { this.vinNumber = replaceCommaWithSemicolon(vinNumber); }
    

    
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

    public int getState() { return state; }
    public void setStatel(int state) { this.state = state; }
    
    public String dropDown() {
    	return customerName  + " " + vehicleRegNumber + " " + vehicleMakeAnModel +" " + colour;
    }
    
    @Override
    public String toString() {
        return "ServiceRecord{" +
                "index=" + index +
                ", state=" + state +
                ", date=" + date +
                ", customerName='" + customerName + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", vehicleRegNumber='" + vehicleRegNumber + '\'' +
                ", vinNumber='" + vinNumber + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", vehicleMakeAnModel='" + vehicleMakeAnModel + '\'' +
                '}';
    }


    public static String replaceCommaWithSemicolon(String input) {
        if (input == null) {
            return null;
        }
        return input.replace(",", ";");
    }

}
