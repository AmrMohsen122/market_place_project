package database.manager;

import java.sql.*;
import java.util.LinkedList;

public class DatabaseManager {
    //the username and password used to access the database server
    private static final String USER_DB = "root";
    private static final String PASSWORD_DB = "123000";
    private static final String URL = "jdbc:mysql://localhost:3306/market_place_db";
    private static LinkedList<Connection> connectionPool = new LinkedList<Connection>();
    //loadDriver() method is used to load driver
    //this method should be executed once at start of program
    public static void loadDriver() throws SQLException{
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("DRIVER LOADED SUCCESSFULLY!");
        }catch(ClassNotFoundException e){
            System.out.println("DRIVER LOADING FAILED!");
            e.printStackTrace();
        }
    }
    public static void initConnection(final int CONNECTION_POOL_SIZE){
        try {
            for(int i = 0 ; i < CONNECTION_POOL_SIZE ; i++){
                connectionPool.add(DriverManager.getConnection(URL , USER_DB , PASSWORD_DB));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    //TODO DON'T RETURN NULL BUT PUT THE THREAD TO SLEEP AND WAKE IT ON A CONDITION
    public static synchronized Connection requestConnection(){
        if(connectionPool.size() > 0){

            return connectionPool.pop();
        }
        else{

            return null;
        }
    }
    //after executing this function the thread should set its connection object to null
    public static synchronized void releaseConnection(Connection releasedConnection){
        connectionPool.add(releasedConnection);

    }
    public static void closeAllConnections(){
        for (int i = 0; i < connectionPool.size(); i++) {
            try {
                connectionPool.peek().close();
                connectionPool.pop();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}


