package za.co.synergio.georgiou.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int index;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date")
    private LocalDate date;
    
    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "cellphone")
    private String cellphone;

    @Column(name = "customer_address")
    private String customerAddress;

    @Column(name = "state")
    private int state; //0=active 1=completed 3=deleted
    

    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }

    
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = replaceCommaWithSemicolon(customerName); }

    public String getCellphone() { return cellphone; }
    public void setCellphone(String cellphone) { this.cellphone = cellphone; }


    
    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = replaceCommaWithSemicolon(customerAddress);
    }


    public int getState() { return state; }
    public void setStatel(int state) { this.state = state; }
    
    public String dropDown() {
    	return customerName;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "index=" + index +
                ", state=" + state +
                ", date=" + date +
                ", customerName='" + customerName + '\'' +
                ", cellphone='" + cellphone + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                '}';
    }


    public static String replaceCommaWithSemicolon(String input) {
        if (input == null) {
            return null;
        }
        return input.replace(",", ";");
    }

}
