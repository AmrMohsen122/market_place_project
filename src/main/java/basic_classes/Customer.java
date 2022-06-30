package basic_classes;

import database.manager.DatabaseManager;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Vector;


//TODO CHECK EMAIL EXISTS IN DATABASE
public class Customer extends User{
    double current_balance;
    String address;
    String mobile_number;
    Vector<Order> orders = new Vector<Order>();

    //CONSTRUCTORS
    public Customer(){
        super();
        current_balance = 0;
        address = null;
        mobile_number = null;
    }
    public Customer(String user_name , String password , String email , Date bdate , double current_balance , String address , String mobile_number){
        super(user_name , password , email , bdate);
        this.current_balance = current_balance;
        this.address = address;
        this.mobile_number = mobile_number;
    }

    public Customer(String user_name, String password, String email, double current_balance, String address, String mobile_number) {
        super(user_name, password, email);
        this.current_balance = current_balance;
        this.address = address;
        this.mobile_number = mobile_number;
        this.orders = orders;
    }

    public Customer(String user_name , String password , String email){
        super(user_name , password , email);
        current_balance = 0;
        address = null;
        mobile_number = null;
    }

    //SETTERS AND GETTERS
    public double getCurrent_balance() {
        return current_balance;
    }

    public void setCurrent_balance(double current_balance) {
        this.current_balance = current_balance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
    public Vector<Order> getOrders(){
        return this.orders;
    }

    /*
        PRE_CONDITIONS: USER DOESN'T EXIST IN DATABASE. USERNAME, PASSWORD, EMAIL SHOULDN'T BE NULLS
        POST_CONDITIONS: ADD USER TO CUSTOMER TABLE
     */
    public void addUser(Connection conn) throws SQLException {
        try{
            conn.setAutoCommit(false);
            String query;
            if(bdate != null){
                query  = "insert into USER_ACC values (" + insertQuotations(user_name) + "," + insertQuotations(password)+ "," + insertQuotations(email)+ "," + insertQuotations(bdate.toString()) + ")";

            }
            else{
                query  = "insert into USER_ACC (USERNAME , PASS_WORD , EMAIL)values (" + insertQuotations(user_name) + "," + insertQuotations(password)+ "," + insertQuotations(email)+")";

            }
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);
            query = "insert into customer_acc values( " + current_balance + "," + insertQuotations(user_name) + "," + insertQuotations(address) + "," + insertQuotations(mobile_number) + ")";
            stmt.executeUpdate(query);
            conn.commit();
            conn.setAutoCommit(true);
        }catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());

            }
            conn.rollback();
        }
    }

    /*
        PRE_CONDITIONS: USER EXISTS IN DATABASE
        POST_CONDITIONS: RETURNS THE CUSTOMER OBJECT WITH DETAILS ASSOCIATED WITH PROVIDED USERNAME
     */
    public static Customer getUserInfo(String user_name, Connection conn){
        try {
            String query = "select * from USER_ACC natural join CUSTOMER_ACC where USERNAME = " + insertQuotations(user_name) ;
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            if(queryResult.next()) {
                return new Customer(queryResult.getString("USERNAME"), queryResult.getString("PASS_WORD"),
                        queryResult.getString("EMAIL"), queryResult.getDate("BDATE"),
                        queryResult.getDouble("current_balance") , queryResult.getString("address"),
                        queryResult.getString("mobile_number"));
            }
            else{
                return null;
            }
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
        PRE_CONDITIONS: NONE
        POST_CONDITIONS: THE ORDER LIST OF USER IS FILLED WITH ORDERS HE MADE BEFORE
     */
    public void loadOrders(Connection conn){
        try{
            String query = "select OID , ODATE, TOTAL_PRICE, IID, ITEM_NAME, PRICE,SELLER_NAME , STOCK , CATEGORY , ITEM_QUANTITY from CUST_ORDER natural join CONTAIN natural join ITEM where USERNAME = " + insertQuotations(user_name) + "AND UNCONFIRMED = \"CONFIRMED\"" + "ORDER BY OID";
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            int prevOID = 0;
            int currentOID;
            Order o = null;
            if(queryResult.next()) {
                prevOID = queryResult.getInt(queryResult.findColumn("OID"));
                currentOID = prevOID;
                o = new Order(queryResult.getInt("OID"), queryResult.getDate("ODATE"), queryResult.getDouble("TOTAL_PRICE") , "CONFIRMED");
                orders.add(o);
                o.addItemToOrder(new Item(queryResult.getInt("IID"), queryResult.getDouble("PRICE")
                        , queryResult.getString("ITEM_NAME"), queryResult.getString("SELLER_NAME"),
                        queryResult.getInt("STOCK"), queryResult.getString("CATEGORY") , queryResult.getInt("ITEM_QUANTITY")));
            }
            while(queryResult.next()) {
                currentOID = queryResult.getInt(queryResult.findColumn("OID"));
                if(currentOID == prevOID){
                    o.addItemToOrder(new Item(queryResult.getInt("IID"),queryResult.getDouble("PRICE")
                            ,queryResult.getString("ITEM_NAME"), queryResult.getString("SELLER_NAME"),
                            queryResult.getInt("STOCK"), queryResult.getString("CATEGORY") , queryResult.getInt("ITEM_QUANTITY")));
                }
                else{
                    o = new Order(queryResult.getInt("OID") ,queryResult.getDate("ODATE") , queryResult.getDouble("TOTAL_PRICE") , "CONFIRMED");
                    orders.add(o);
                    o.addItemToOrder(new Item(queryResult.getInt("IID"),queryResult.getDouble("PRICE")
                            ,queryResult.getString("ITEM_NAME"), queryResult.getString("SELLER_NAME"),
                            queryResult.getInt("STOCK"), queryResult.getString("CATEGORY") , queryResult.getInt("ITEM_QUANTITY")));
                }
                prevOID = currentOID;
            }

        }catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
    }

    //TODO WE NEED TO CHECK TABLE CONTAIN IF THE ITEM ALREADY EXIST JUST INCREMENT ITS QUANTITY (PRIMARY KEY CONSTRAINTS)
    //TODO CHECK IF THE ORDER ADDS UP TO TOTAL PRICE
    /*
        PRE_CONDITIONS: THE USER EXISTS IN DATABASE, ITEMS CONTAINED IN ORDER MUST ALREADY EXIST IN DATABASE
        POST_CONDITIONS: AN ORDER IS PLACED UNDER THE USERNAME
     */
    public int makeOrder(Order o , Connection conn) throws SQLException {
        try {
            conn.setAutoCommit(false);
            // get items in current order by IID to query on
            String items = "(";
            int size = o.getItems().size();
            for (int i = 0 ; i <  size; i++){
                items += o.getItems().get(i).getIid();
                if(i != size - 1){
                    items+= ",";
                }
            }
            items += ")";
            System.out.println(items);
            String query = "select IID,STOCK from item where IID in " + items;
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            //Will be used to store the item stock pairs for easier search later
            HashMap<Integer , Integer> itemStock = new HashMap<Integer , Integer>();
            while(queryResult.next()){
                itemStock.put(queryResult.getInt("IID") , queryResult.getInt("STOCK"));
            }
            for (int i = 0 ; i < o.getItems().size() ; i++){
                if(itemStock.get(o.getItems().get(i).getIid()) < o.getItems().get(i).getItemQuantity()){
                    return - 1;
                }
            }
            query = "update CUST_ORDER set UNCONFIRMED  = \"CONFIRMED\" WHERE OID =  " + o.getOID() ;
            stmt.executeUpdate(query);
            query = "update CUSTOMER_ACC SET CURRENT_BALANCE = CURRENT_BALANCE - " + o.getTotalPrice() + " where USERNAME = " + insertQuotations(user_name);
            stmt.executeUpdate(query);
            String updateStock = "update item set STOCK = STOCK - ? where ITEM_NAME = ?";
            PreparedStatement prepstmt2 = conn.prepareStatement(updateStock);
            for(Item i : o.getItems()){
                prepstmt2.setInt(1 , i.getItemQuantity());
                prepstmt2.setString(2 , i.getItem_name());
                prepstmt2.executeUpdate();
            }
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
        return 1;
    }


    /*
        PRE_CONDITIONS: USER EXISTS IN DATABASE
        POST_CONDITIONS: RETURNS AN ORDER OBJECT REPRESENTING CART IF THERE EXISTS AN UNCONFIRMED ORDER, RETURNS AN EMPTY ORDER WITH
        NEXT UNIQUE OID IF NO UNCONFIRMED ORDER EXISTS
        RETURN NULL IN CASE OF SQL ERRS
     */
    public static Order loadCart(String user_name , Connection conn ) throws SQLException {
        try{
            conn.setAutoCommit(false);
            String query = "select OID , ODATE, TOTAL_PRICE, IID, ITEM_NAME, PRICE,SELLER_NAME , STOCK , CATEGORY , ITEM_QUANTITY from CUST_ORDER natural join CONTAIN natural join ITEM where USERNAME = " + insertQuotations(user_name) + "AND UNCONFIRMED = \"UNCONFIRMED\" " + "ORDER BY OID";
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            Order o = null;
            if(queryResult.next()) {
                o = new Order(queryResult.getInt("OID"), queryResult.getDate("ODATE"), queryResult.getDouble("TOTAL_PRICE"), "CONFIRMED");
                o.addItemToOrder(new Item(queryResult.getInt("IID"), queryResult.getDouble("PRICE")
                        , queryResult.getString("ITEM_NAME"), queryResult.getString("SELLER_NAME"),
                        queryResult.getInt("STOCK"), queryResult.getString("CATEGORY"), queryResult.getInt("ITEM_QUANTITY")));
                while(queryResult.next()){
                    o.addItemToOrder(new Item(queryResult.getInt("IID"), queryResult.getDouble("PRICE")
                            , queryResult.getString("ITEM_NAME"), queryResult.getString("SELLER_NAME"),
                            queryResult.getInt("STOCK"), queryResult.getString("CATEGORY") , queryResult.getInt("ITEM_QUANTITY")));
                }
            }
            else{
                query = "select max(OID) from cust_order";
                queryResult = stmt.executeQuery(query);
                queryResult.next();
                int nextOID = queryResult.getInt(1) + 1;
                System.out.println( Date.valueOf(LocalDate.now()));
                query = "insert into cust_order(OID , TOTAL_PRICE , ODATE , USERNAME, UNCONFIRMED) values( " + nextOID + ",0," + insertQuotations(Date.valueOf(LocalDate.now()).toString()) +   "," + insertQuotations(user_name) + "," + "\"UNCONFIRMED\" )";
                stmt.executeUpdate(query);
                o = new Order(nextOID);

            }
            conn.commit();
            conn.setAutoCommit(true);
            return o;
        }catch (SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
            conn.rollback();
        }
        return null;
    }


    /*
        PRE_CONDITIONS: AMOUNT IS A POSITIVE INTEGER AND USERNAME EXISTS IN DATABASE
        POST_CONDITIONS: THE BALANCE ASSOCIATED WITH USERNAME IS INCREMENTED BY SPECIFIED AMOUNT
     */
    public void rechargeBalance(double amount, Connection conn){
        try {
            Statement stmt = conn.createStatement();
            String query = "update CUSTOMER_ACC SET CURRENT_BALANCE = CURRENT_BALANCE + " + amount + " where USERNAME = " + insertQuotations(user_name);
            stmt.executeUpdate(query);
        }
        catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
    }

    @Override
    public String toString(){
       return "***************** CUSTOMER *****************\n" + super.toString() + "\nCurrent balance = " + current_balance + "\nAddress: " + address + "\nMobile number: " + mobile_number;

    }

    public static void main(String[] args) throws SQLException {
        DatabaseManager.initConnection(10);
        Connection conn = DatabaseManager.requestConnection();
//        (String user_name , String password , String email , Date bdate , double current_balance , String address , String mobile_number)
        Order o = Order.getOrderByID(12 , conn);
        System.out.println("**********ORDER*********");
        System.out.println(o);
        o.printOrderItem();
    }
}
