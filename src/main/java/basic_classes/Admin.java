package basic_classes;

import database.manager.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;

public class Admin extends User{

    public Admin(){
        super();
    }
    public Admin(String user_name , String password , String email, LocalDate bdate){
        super(user_name , password , email , bdate);
    }
    public Admin(String user_name , String password , String email){
        super(user_name , password, email);
    }


    private boolean isAdmin(String user_name, Connection conn){
        try{
            String query = "select * from ADMIN_ACC where USERNAME = " + insertQuotations(user_name) ;
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            if(queryResult.next()){
                return true;
            }
        }catch (SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
        return false;
    }
    @Override
    public void getUserInfo(String user_name, Connection conn){
        try {
            if(isAdmin(user_name , conn)) {
                String query = "select * from USER_ACC where USERNAME = " + insertQuotations(user_name) ;
                Statement stmt = conn.createStatement();
                ResultSet queryResult = stmt.executeQuery(query);
                queryResult.next();
                this.user_name = queryResult.getString(queryResult.findColumn("USERNAME"));
                password = queryResult.getString(queryResult.findColumn("PASS_WORD"));
                email = queryResult.getString(queryResult.findColumn("EMAIL"));
                bdate = LocalDate.parse(queryResult.getString(queryResult.findColumn("BDATE")));
            }
        }
        catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
    }

    @Override
    public void addUser(Connection conn){
        super.addUser(conn);
        try{

            String query;
            query  = "insert into ADMIN_ACC values (" + insertQuotations(super.user_name) + ")";
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

    public static void main(String[] args) {
        DatabaseManager.initConnection(10);
        Connection conn = DatabaseManager.requestConnection();
        User a1 = new Admin();
        a1.getUserInfo("amr mohsen" , conn);
        System.out.println(a1);
    }

}
