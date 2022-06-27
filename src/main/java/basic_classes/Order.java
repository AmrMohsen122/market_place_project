package basic_classes;

import java.sql.Date;

public class Order {
    private int OID;
    private Date ODate;
    private double totalPrice;

    public Order(int OID, Date ODate, double totalPrice) {
        this.OID = OID;
        this.ODate = ODate;
        this.totalPrice = totalPrice;
    }

    //CONSTRUCTORS
    public Order(Date ODate, double totalPrice) {
        this.ODate = ODate;
        this.totalPrice = totalPrice;
    }

    //GETTERS
    public int getOID() {
        return OID;
    }

    public Date getODate() {
        return ODate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString(){
        return "OID: " + OID + "\nOrder Date: " + ODate + "\nTotal Price: " + totalPrice;
    }



}
