package basic_classes;
import database.manager.DatabaseManager;

import java.sql.*;
import java.util.Vector;

public class Item {

    private static final boolean debug = true;

    private int iid;
    private double price;
    private String item_name;
    private String seller_name;
    private int stock;
    private String category;
    //-1 to check if this item belongs to an order or not
    private int itemQuantity = - 1;

    // Constructors

    public int getItemQuantity() {
        return itemQuantity;
    }
    public Item(int iid, double price, String item_name, int stock) {
        this.iid = iid;
        this.price = price;
        this.item_name = item_name;
        this.stock = stock;
    }
    public Item() {
        this.iid = 0;
        this.price = 0.0;
        this.item_name = "Not Found";
        this.stock = 0;
    }
    
    public Item(double price, String item_name) {
        this.price = price;
        this.item_name = item_name;
    }
    
    //Used in History
    public Item(int iid ,int itemQuantity , double price, String item_name) {
        this.iid = iid;
        this.itemQuantity = itemQuantity;
        this.price = price;
        this.item_name = item_name;
    }

    public Item(int iid, double price, String item_name, String seller_name, int stock, String category, int itemQuantity) {
        this.iid = iid;
        this.price = price;
        this.item_name = item_name;
        this.seller_name = seller_name;
        this.stock = stock;
        this.category = category;
        this.itemQuantity = itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Item(int iid, double price, String item_name, String seller_name, int stock, String category){
        this.iid = iid;
        this.price = price;
        this.stock = stock;
        this.item_name = item_name;
        this.seller_name = seller_name;
        this.category = category;
    }

    public int getIid(){
        return iid;
    }
    public double getPrice(){
        return price;
    }
    public int getStock(){
        return stock;
    }
    public String getItem_name(){
        return item_name;
    }
    public String getSeller_name(){
        return seller_name;
    }
    public String getCategory(){
        return category;
    }

    /*
     * PRE_CONDITIONS: DATABASE(item)EXISTS
     * POST_CONDITIONS: RETURNS 0 IF USER DOESN'T EXIST, 1 IF USER EXISTS, -1 IF A SQL ERROR OCCURRED
     * NOTES: EACH THREAD SHALL HAVE ITS OWN CONNECTION WHICH IS PASSED TO FUNCTIONS TO EXECUTE QUERIES ON
     */
    public static int itemExists(String item_name, Connection conn){
        try{
            item_name = "\"" + item_name + "\"";
            String query = "select ITEM_NAME from ITEM where ITEM_NAME = " + item_name;
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            if(!queryResult.next()){
                return 0;
            }
            else{
                return 1;
            }
        }
        catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
            return -1;
        }
    }

    /*
     * PRE_CONDITIONS: Current item doesn't exist in the database
     * POST_CONDITIONS: Adds the current item to the database
     */
    public void addItem(Connection conn){
        try{
            String query;
            query  = "insert into ITEM(iid, price, item_name, seller_name, stock, category) values (" + iid + "," + price + "," + insertQuotations(item_name)+ "," + insertQuotations(seller_name) + "," + stock + "," + insertQuotations(category) + ")";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
        }
        catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
    }

    /*
     * PRE_CONDITIONS: item_name exists in the database
     * POST_CONDITIONS: retrieves all item info
     */
    public static Item getItemInfo(String item_name, Connection conn){
        try {
            String query = "select * from ITEM where ITEM_NAME = " + insertQuotations(item_name);
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            queryResult.next();
            return new Item(queryResult.getInt(queryResult.findColumn("IID")) , queryResult.getFloat(queryResult.findColumn("PRICE")) , queryResult.getString(queryResult.findColumn("ITEM_NAME")) , queryResult.getString(queryResult.findColumn("SELLER_NAME")) , queryResult.getInt(queryResult.findColumn("STOCK")) , queryResult.getString(queryResult.findColumn("CATEGORY")));
        }
        catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
        return null;
    }

    /*
     * PRE_CONDITIONS: item_name exists in the database
     * POST_CONDITIONS: retrieves item price
     */
    public static double getPrice(String item_name, Connection conn) {
        try {
            String query = "select * from ITEM where ITEM_NAME = " + insertQuotations(item_name);
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            queryResult.next();
            return (queryResult.getFloat(queryResult.findColumn("PRICE")));
        } catch (SQLException e) {
            if (debug) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }

        }
        return -1;
    }

    /*
     * PRE_CONDITIONS: item_name exists in the database
     * POST_CONDITIONS: retrieves item stock
     */
    public static int getStock(String item_name, Connection conn) {
        try {
            String query = "select * from ITEM where ITEM_NAME = " + insertQuotations(item_name);
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            queryResult.next();
            return (queryResult.getInt(queryResult.findColumn("STOCK")));
        } catch (SQLException e) {
            if (debug) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }

        }
        return -1;
    }

    /*
     * PRE_CONDITIONS: item_name exists in the database
     * POST_CONDITIONS: retrieves item category
     */

    public static String getCategory(String item_name, Connection conn) {
        try {
            String query = "select * from ITEM where ITEM_NAME = " + insertQuotations(item_name);
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            queryResult.next();
            return (queryResult.getString(queryResult.findColumn("CATEGORY")));
        } catch (SQLException e) {
            if (debug) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }

        }
        return"Not Found";
    }

    /*
     * PRE_CONDITIONS: category exists in the database
     * POST_CONDITIONS: retrieve items by category
     */
    public static Vector<Item> search_by_category(String category, Connection conn){
        try {
            Vector<Item> items = new Vector<Item>();
            String query = "select * from ITEM where CATEGORY = " + insertQuotations(category);
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            while(queryResult.next()) {
                items.add(new Item(queryResult.getInt(queryResult.findColumn("IID")), queryResult.getFloat(queryResult.findColumn("PRICE")), queryResult.getString(queryResult.findColumn("ITEM_NAME")), queryResult.getString(queryResult.findColumn("SELLER_NAME")), queryResult.getInt(queryResult.findColumn("STOCK")), queryResult.getString(queryResult.findColumn("CATEGORY"))));
            }
            return items;
        } catch (SQLException e) {
            if (debug) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
        return null;
    }

    /*
     * PRE_CONDITIONS: category exists in the database
     * POST_CONDITIONS: retrieve 3 items from each category
     */
    public static Vector<Item> search_by_3_category(String category, Connection conn){
        try {
            Vector<Item> items = new Vector<Item>();
            String query = "select * from ITEM where CATEGORY = " + insertQuotations(category)+" limit 3";
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            while(queryResult.next()) {
                items.add(new Item(queryResult.getInt(queryResult.findColumn("IID")), queryResult.getFloat(queryResult.findColumn("PRICE")), queryResult.getString(queryResult.findColumn("ITEM_NAME")), queryResult.getString(queryResult.findColumn("SELLER_NAME")), queryResult.getInt(queryResult.findColumn("STOCK")), queryResult.getString(queryResult.findColumn("CATEGORY"))));
            }
            return items;
        } catch (SQLException e) {
            if (debug) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
        return null;
    }

    /*
     * PRE_CONDITIONS: item_name exists in the database
     * POST_CONDITIONS: retrieve items by name
     */
    public static Vector<Item> search_by_name(String item_name, Connection conn){
        try {
            Vector<Item> items = new Vector<Item>();
            String query = "select * from ITEM where ITEM_NAME = " + insertQuotations(item_name);
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            while(queryResult.next()) {
                items.add(new Item(queryResult.getInt(queryResult.findColumn("IID")), queryResult.getFloat(queryResult.findColumn("PRICE")), queryResult.getString(queryResult.findColumn("ITEM_NAME")), queryResult.getString(queryResult.findColumn("SELLER_NAME")), queryResult.getInt(queryResult.findColumn("STOCK")), queryResult.getString(queryResult.findColumn("CATEGORY"))));
            }
            return items;
        } catch (SQLException e) {
            if (debug) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
        return null;
    }


    /*
        PRE_CONDITIONS: NONE
        POST_CONDITIONS: RETURNS A VECTOR OF ALL ITEMS IN DATABASE, RETURNS NULL IN CASE THERE ARE NO ITEMS IN DATABASE
     */
    public static Vector<Item> getAllItems(Connection conn){
        try {
            Vector<Item> items = new Vector<Item>();
            String query = "select * from ITEM  ";
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            while(queryResult.next()) {
                items.add(new Item(queryResult.getInt(queryResult.findColumn("IID")), queryResult.getFloat(queryResult.findColumn("PRICE")), queryResult.getString(queryResult.findColumn("ITEM_NAME")), queryResult.getString(queryResult.findColumn("SELLER_NAME")), queryResult.getInt(queryResult.findColumn("STOCK")), queryResult.getString(queryResult.findColumn("CATEGORY"))));
            }
            return items;
        } catch (SQLException e) {
            if (debug) {
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
        return null;
    }


    private static String insertQuotations(String str){
        return "\"" + str + "\"" ;
    }

    @Override
    public String toString(){
        return "iid: " + iid + "\nprice: " + price + "\nitem_name: " + item_name + "\nseller_name: " + seller_name + "\nstock: " + stock + "\ncategory: " + category + "\nItem Quantity: " + itemQuantity;
    }
}
