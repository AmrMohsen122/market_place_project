package basic_classes;

import database.manager.DatabaseManager;

import java.sql.*;
import java.util.Date;

public class User {
    private static final boolean debug = true;
    private String user_name;
    private String address;
    private Date bdate;
    private float current_balance;

    /*
    * PRE_CONDITIONS: DATABASE EXISTS
    * POST_CONDITIONS: RETURNS 0 IF USER DOESN'T EXIST, 1 IF USER EXISTS, -1 IF A SQL ERROR OCCURRED
     */
    public static int userExists(String user_name , Connection conn){
        try{
            user_name = "\"" + user_name + "\"";
            String query = "select USERNAME from USER_ACC where USERNAME = " + user_name;
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
    * PRE_CONDITIONS: USER EXISTS IN DATABASE
    * POST_CONDITIONS: RETURNS THE PASSWORD OF THAT USER
    */
    public static String getPassword(String user_name, Connection conn){
        try {
            user_name = "\"" + user_name + "\"";
            String query = "select PASS_WORD from USER_ACC where USERNAME = " + user_name;
            Statement stmt = conn.createStatement();
            ResultSet queryResult = stmt.executeQuery(query);
            queryResult.next();
            return queryResult.getString(queryResult.findColumn("PASS_WORD"));
        }
        catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
            return "SQL ERR";
        }
    }



}
