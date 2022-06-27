package basic_classes;

import database.manager.DatabaseManager;
import java.sql.*;
import java.util.Vector;

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
                return new Customer(queryResult.getString(queryResult.findColumn("USERNAME")), queryResult.getString(queryResult.findColumn("PASS_WORD")),
                        queryResult.getString(queryResult.findColumn("EMAIL")), queryResult.getDate(queryResult.findColumn("BDATE")),
                        queryResult.getDouble(queryResult.findColumn("current_balance")) , queryResult.getString(queryResult.findColumn("address")),
                        queryResult.getString(queryResult.findColumn("mobile_number")));
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
            String query = "select OID , ODATE, TOTAL_PRICE from CUSTOMER_ACC natural join CUST_ORDER where USERNAME = " + insertQuotations(user_name);
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            while(queryResult.next()){
                orders.add(new Order(queryResult.getInt(queryResult.findColumn("OID")) , queryResult.getDate(queryResult.findColumn("ODATE"))
                           ,queryResult.getDouble(queryResult.findColumn("TOTAL_PRICE"))));
            }
        }catch(SQLException e){
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

    public static void main(String[] args) {
        DatabaseManager.initConnection(10);
        Connection conn = DatabaseManager.requestConnection();
        User u1 = Customer.getUserInfo("Ahmed", conn);
        ((Customer)u1).loadOrders(conn);
        for (Order o: ((Customer) u1).orders) {
            System.out.println(o);

        }
    }
}
