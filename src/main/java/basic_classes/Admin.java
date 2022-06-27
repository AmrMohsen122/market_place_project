package basic_classes;

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

    /*
        PRE_CONDITIONS: user_name EXISTS IN DATABASE
        POST_CONDITIONS: RETURNS WHETHER THE USERNAME PASSED IS ASSOCIATED WITH AN ADMIN ACCOUNT
     */
    private static boolean isAdmin(String user_name, Connection conn){
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

    /*
       PRE_CONDITIONS: user_name EXISTS IN DATABASE
       POST_CONDITIONS: RETURN AN ADMIN OBJECT WITH RECORD FOUND, RETURN NULL IF THERE IS NO USER WITH PASSED USERNAME OR IF THE USERNAME DOESN'T HAVE ADMIN PRIVILEGES
     */
    public static Admin getUserInfo(String user_name, Connection conn){
        try {
                String query = "select * from USER_ACC natural join ADMIN_ACC where USERNAME = " + insertQuotations(user_name) ;
                Statement stmt = conn.createStatement();
                ResultSet queryResult = stmt.executeQuery(query);
                if(queryResult.next()) {
                    return new Admin(queryResult.getString(queryResult.findColumn("USERNAME")), queryResult.getString(queryResult.findColumn("PASS_WORD")), queryResult.getString(queryResult.findColumn("EMAIL")), LocalDate.parse(queryResult.getString(queryResult.findColumn("BDATE"))));
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
        PRE_CONDITIONS: THE USERNAME DOESN'T EXIST IN DATABASE
        POST_CONDITIONS: THE USER IS ADDED TO DATABASE WITH ADMIN PRIVILEGES
     */
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

}
