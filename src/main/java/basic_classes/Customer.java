package basic_classes;

import database.manager.DatabaseManager;
import java.sql.*;
import java.util.Vector;


//TODO ARE BALANCE UPDATE AND DECREMENT METHODS NEEDED?
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
    @Override
    public void addUser(Connection conn) {
        super.addUser(conn);
        try{
            String query = "insert into customer_acc values( " + current_balance + "," + insertQuotations(user_name) + "," + insertQuotations(address) + "," + insertQuotations(mobile_number) + ")";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(query);

        }catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
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
            String query = "select OID , ODATE, TOTAL_PRICE, IID, ITEM_NAME, PRICE,SELLER_NAME , STOCK , CATEGORY , ITEM_QUANTITY from CUST_ORDER natural join CONTAIN natural join ITEM where USERNAME = " + insertQuotations(user_name) + "ORDER BY OID";
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            int prevOID = 0;
            int currentOID;
            Order o = null;
            if(queryResult.next()) {
                prevOID = queryResult.getInt(queryResult.findColumn("OID"));
                currentOID = prevOID;
                o = new Order(queryResult.getInt("OID"), queryResult.getDate("ODATE"), queryResult.getDouble("TOTAL_PRICE"));
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
                    o = new Order(queryResult.getInt("OID") ,queryResult.getDate("ODATE") , queryResult.getDouble("TOTAL_PRICE"));
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

    //TODO CHECK STOCK BEFORE DECREMENTING
    //TODO CHECK IF THE ORDER ADDS UP TO TOTAL PRICE
    //TODO KHALY MO2MEN Y7OTHA
    /*
        PRE_CONDITIONS: THE USER EXISTS IN DATABASE, ITEMS CONTAINED IN ORDER MUST ALREADY EXIST IN DATABASE
        POST_CONDITIONS: AN ORDER IS PLACED UNDER THE USERNAME
     */
    public void makeOrder(Order o , Connection conn) throws SQLException {
        try {
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            String query = "insert into CUST_ORDER (ODATE , TOTAL_PRICE , USERNAME) values (" + insertQuotations(o.getODate().toString()) + "," + o.getTotalPrice() + "," + insertQuotations(user_name) + ")" ;
            stmt.executeUpdate(query);
            query = "Select last_insert_id()";
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            int currentOID = resultSet.getInt(1);
            query = "update CUSTOMER_ACC SET CURRENT_BALANCE = CURRENT_BALANCE - " + o.getTotalPrice() + " where USERNAME = " + insertQuotations(user_name);
            stmt.executeUpdate(query);
            String insertContain = "insert into CONTAIN (ITEM_QUANTITY , OID , IID) values (?,?,?)";
            String updateStock = "update item set STOCK = STOCK - ? where ITEM_NAME = ?";
            PreparedStatement prepStmt = conn.prepareStatement(insertContain);
            PreparedStatement prepstmt2 = conn.prepareStatement(updateStock);
            for(Item i : o.getItems()){
                prepStmt.setInt(1 , i.getItemQuantity());
                prepStmt.setInt(2 , currentOID);
                prepStmt.setInt(3 , i.getIid());
                prepstmt2.setInt(1 , i.getItemQuantity());
                prepstmt2.setString(2 , i.getItem_name());
                prepStmt.executeUpdate();
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
    }

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
        Order o1 = new Order(Date.valueOf("2022-06-28") , 350);
        Item i1 = Item.getItemInfo("Desert Shoes",conn);
        i1.setItemQuantity(1);
        Item i2 = Item.getItemInfo("Desert white sneakers" , conn);
        i2.setItemQuantity(1);
        o1.addItemToOrder(i1);
        o1.addItemToOrder(i2);
        User u1 = Customer.getUserInfo("Amr Mahmoud" , conn);
        ((Customer)u1).makeOrder(o1 , conn);
        ((Customer)u1).loadOrders(conn);
        for (Order o: ((Customer) u1).orders) {
            System.out.println(o);
            o.printOrderItem();

        }
    }
}
