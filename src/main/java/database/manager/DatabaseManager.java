package database.manager;
import basic_classes.User;

import java.sql.*;
import java.util.LinkedList;
import java.util.Vector;

public class DatabaseManager {
    //the username and password used to access the database server
    private static final String USER_DB = "root";
    private static final String PASSWORD_DB = "Nm15986@14";
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

//FOR TESTING THREADS
class DBThreads extends Thread{
    private Connection conn = null;
    private static int threadCount = 0;
    private int TID;
    public DBThreads(){
        incrementID();
        TID = threadCount;
    }
    public void setConn(Connection conn){
        this.conn = conn;
    }
    public void run(){
        incrementID();
        System.out.println("Thread#" + TID);
        conn = DatabaseManager.requestConnection();
        for (int i = 0; i < 10 ; i++) {
            System.out.println("iter: " + i + " " + User.getPassword("Amr Mohsen" ,conn));
        }
        DatabaseManager.releaseConnection(conn);
        conn = null;
    }
    public static synchronized void incrementID(){
        threadCount++;
    }
}

//FOR TESTING, SHOULD BE DELETED LATER
class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseManager.initConnection(10);
        Vector<DBThreads> threads = new Vector<DBThreads>();
        DBThreads t1;

        for(int i = 0 ; i < 5 ; i++) {
            t1 = new DBThreads();
            threads.add(t1);

        }
        long start_time = System.currentTimeMillis();
        for (DBThreads thread:threads) {
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Elapsed Time: " +  (System.currentTimeMillis() - start_time)/1000.0);
        //10k queries ---> 12.326 sec ----> on 1 connection
        //10k queries ---> 11.687 sec ----> on 1 connection

        //10k queries ---> 10.236 sec ----> on 10 connection


    }
}

