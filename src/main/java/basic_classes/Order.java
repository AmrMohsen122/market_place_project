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
    
    public Order( Date ODate, double totalPrice) {
        this.ODate = ODate;
        this.totalPrice = totalPrice;
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

    public void setItems(Vector<Item> items) {
        this.items = items;
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
            Statement stmt = conn.createStatement();
            query = "select * from CONTAIN where OID = " + OID + " AND IID = " + IID;
            ResultSet queryResult = stmt.executeQuery(query);
            if(queryResult.next()){
                query = "update CONTAIN set ITEM_QUANTITY = ITEM_QUANTITY + " + itemQuantity + " where OID = " + OID +" AND IID = " + IID;
                stmt.executeUpdate(query);
            }
            else {
                query = "insert into CONTAIN values (" + itemQuantity + "," + OID + "," + IID + ")";
                stmt.executeUpdate(query);
            }
            query = "update cust_order set TOTAL_PRICE = TOTAL_PRICE + " + itemPrice * itemQuantity + "where OID = " + OID;
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
    /*
        PRE_CONDITIONS: ITEM ALREADY EXISTS IN DATABASE, ITEM_QUANTITY SHOULD BE MORE THAN ITEM_QUANTITY IN ORDER
        POST_CONDITIONS: ITEM QUANTITY IS DECREMENTED FROM DATABASE
        itemQuantity SHOULD BE ONE
     */
    public void removeItem(int IID , int itemQuantity, double itemPrice ,Connection conn) throws SQLException{
        try{
            conn.setAutoCommit(false);
            String query;
            Statement stmt = conn.createStatement();
            query = "select * from CONTAIN where OID = " + OID + " AND IID = " + IID;
            ResultSet queryResult = stmt.executeQuery(query);
            if(queryResult.next()){
                if(queryResult.getInt("ITEM_QUANTITY") <= itemQuantity){
                    query = "delete from CONTAIN where OID = " + OID + " AND IID = " + IID;
                }
                else {
                    query = "update CONTAIN set ITEM_QUANTITY = ITEM_QUANTITY - " + itemQuantity + " where OID = " + OID + " AND IID = " + IID;
                }
                stmt.executeUpdate(query);
            }
            query = "update cust_order set TOTAL_PRICE = TOTAL_PRICE - " + itemPrice * itemQuantity + "where OID = " + OID;
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
    @Override
    public String toString(){
        return "OID: " + OID + "\nOrder Date: " + ODate + "\nTotal Price: " + totalPrice;
    }


    /*
        PRE_CONDITIONS: THE OID EXISTS IN DATABASE
        POST_CONDITIONS: RETURN AN OBJECT REPRESENTING ORDER WITH THAT OID, RETURN NULL IN CASE THE OID PASSED DOESN'T EXIST OR ORDER IS DOESN'T CONTAIN ANY ITEMS
     */
    public static Order getOrderByID(int OID , Connection conn){
        try{
            String query = "select OID , ODATE, TOTAL_PRICE, IID, ITEM_NAME, PRICE,SELLER_NAME , STOCK , CATEGORY , ITEM_QUANTITY, UNCONFIRMED from CUST_ORDER natural join CONTAIN natural join ITEM where OID = " +  OID;
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            Order o = null;
            if(queryResult.next()) {
                o = new Order(queryResult.getInt("OID"), queryResult.getDate("ODATE"), queryResult.getDouble("TOTAL_PRICE"), queryResult.getString("UNCONFIRMED"));
                o.addItemToOrder(new Item(queryResult.getInt("IID"), queryResult.getDouble("PRICE")
                        , queryResult.getString("ITEM_NAME"), queryResult.getString("SELLER_NAME"),
                        queryResult.getInt("STOCK"), queryResult.getString("CATEGORY"), queryResult.getInt("ITEM_QUANTITY")));
                while(queryResult.next()){
                    o.addItemToOrder(new Item(queryResult.getInt("IID"), queryResult.getDouble("PRICE")
                            , queryResult.getString("ITEM_NAME"), queryResult.getString("SELLER_NAME"),
                            queryResult.getInt("STOCK"), queryResult.getString("CATEGORY") , queryResult.getInt("ITEM_QUANTITY")));
                }
            }
            return o;
        }catch (SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
        return null;
    }

}
