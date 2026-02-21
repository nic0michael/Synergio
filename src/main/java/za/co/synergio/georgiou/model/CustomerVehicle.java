package za.co.synergio.georgiou.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "CUSTOMER_VEHICLE")
public class CustomerVehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int index; //0=active 1=completed 3=deleted

    @Column(name = "customer_id")
    private int customerId;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "cellphone")
    private String cellphone;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "vehicle_reg_number")
    private String vehicleRegNumber;

    @Column(name = "vehicle_make_model")
    private String vehicleMakeAnModel;

    @Column(name = "colour")
    private String colour;

    @Column(name = "vin_number")
    private String vinNumber;

    @Column(name = "state")
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
        return "CustomerVehicle{" +
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
