import java.io.Serializable;
/**
 * Client - Server orders
 * @author Klara Makek
 * @version 2023
 * ISTE 121 LAB07 HW06
 */
public class Order implements Serializable {
    private final long serialVersionUID = 01L;
    private String name;
    private String street;
    private String city;
    private String state;
    private int zip;
    private String email;
    private int item;
    private int quantity;

    

    public Order(String name, String street, String city, String state, int zip, String email, int item, int quantity) {
        this.name = name;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.email = email;
        this.item = item;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return this.name + " " + this.item;
    }

    public String toCSV() {
        return this.name + "," + this.item;
    }
    public long getSerialVersionUID(){
        return serialVersionUID;
    }
}
