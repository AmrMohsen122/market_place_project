package basic_classes;

import java.sql.*;
import java.util.Vector;

public class Order {
    private int OID;
    protected static final boolean debug = true;
    private Date ODate;
    private double totalPrice;
    private String isConfirmed;
    private Vector<Item> items = new Vector<Item>();


    public Vector<Item> getItems() {
        return items;
    }
    public void printOrderItem(){
        for (Item i: items) {
            System.out.println(i);
        }
    }
    public void addItemToOrder(Item item){
        items.add(item);
    }
    public Order(int OID, Date ODate, double totalPrice , String isConfirmed) {
        this.OID = OID;
        this.ODate = ODate;
        this.totalPrice = totalPrice;
        this.isConfirmed = isConfirmed;
    }

    //CONSTRUCTORS
    public Order(Date ODate, double totalPrice , String isConfirmed) {
        this.ODate = ODate;
        this.totalPrice = totalPrice;
        this.isConfirmed = isConfirmed;
    }
    public Order(int OID) {
        this.OID = OID;
        this.ODate = null;
        this.totalPrice = 0;
        this.isConfirmed = "UNCONFIRMED";
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

    public String getIsConfirmed() {
        return isConfirmed;
    }

    public void loadItems(Connection conn){
        try{
            String query = "select IID , PRICE, ITEM_NAME, SELLER_NAME, STOCK, CATEGORY from ITEM natural join CUST_ORDER where OID = " + OID;
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            while(queryResult.next()){
                items.add(new Item(queryResult.getInt(queryResult.findColumn("IID")) , queryResult.getDouble(queryResult.findColumn("PRICE"))
                        ,queryResult.getString(queryResult.findColumn("ITEM_NAME")), queryResult.getString(queryResult.findColumn("SELLER_NAME")), queryResult.getInt(queryResult.findColumn("STOCK")), queryResult.getString(queryResult.findColumn("CATEGORY"))));
            }
        }catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
    }

    public void addItem(int IID , int itemQuantity,double itemPrice ,Connection conn) throws SQLException {
        try{
            conn.setAutoCommit(false);
            String query;
            query  = "insert into CONTAIN values (" + itemQuantity + "," + OID + "," + IID + ")";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            query = "update cust_order set TOTAL_PRICE = TOTAL_PRICE + " + itemPrice * itemQuantity + "where OID = " + OID ;
            stmt.executeUpdate(query);
            conn.commit();
            conn.setAutoCommit(true);
        }
        catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
            conn.rollback();
        }
    }

// TODO function get order adelha el order id tgebly el order object

    @Override
    public String toString(){
        return "OID: " + OID + "\nOrder Date: " + ODate + "\nTotal Price: " + totalPrice;
    }



}
