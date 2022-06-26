package basic_classes;


import java.sql.*;
import java.time.LocalDate;

public class User {
    private static final boolean debug = true;
    private String user_name;
    private  String password;
    private String email;
    private LocalDate bdate;

    //CONSTRUCTORS

    //EMPTY CONSTRUCTOR CREATES A TEST OBJECT
    public User(){
        this.user_name = "test";
        this.password = "test";
        this.email = "test@example.com";
        this.bdate = LocalDate.now();
    }
    //CREATES A USER WITH ALL FIELDS SPECIFIED BY ARGUMENTS OF CONSTRUCTOR
    public User(String user_name , String password , String email, LocalDate bdate){
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.bdate = bdate;
    }
    //CREATE A USER WHERE DATE MAY NOT BE SPECIFIED AND IS SET TO DEFAULT VALUE OF CURRENT DATE
    public User(String user_name , String password , String email){
        this.user_name = user_name;
        this.password = password;
        this.email = email;
        this.bdate = LocalDate.now();
    }

    //GETTERS
    public String getUsername() {
        return user_name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBdate() {
        return bdate;
    }

    //TODO THREAD SYNCHRONIZATION
    /*
    * PRE_CONDITIONS: DATABASE EXISTS
    * POST_CONDITIONS: RETURNS 0 IF USER DOESN'T EXIST, 1 IF USER EXISTS, -1 IF A SQL ERROR OCCURRED
    * NOTES: EACH THREAD SHALL HAVE ITS OWN CONNECTION WHICH IS PASSED TO FUNCTIONS TO EXECUTE QUERIES ON
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


    /*
     * PRE_CONDITIONS: CURRENT USERNAME DOESN'T EXISTS IN DATABASE
     * POST_CONDITIONS: ADDS THE CURRENT USER OBJECT TO DATABASE
     */
    public void addUser(Connection conn){
        try{

            String query;
            query  = "insert into USER_ACC values (" + insertQuotations(user_name) + "," + insertQuotations(password)+ "," + insertQuotations(email)+ "," + insertQuotations(bdate.toString()) + ")";
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
     * PRE_CONDITIONS: user_name EXISTS IN DATABASE
     * POST_CONDITIONS: RETRIEVES ALL USER INFO
     */
    public static User getUserInfo(String user_name, Connection conn){
        try {
            String query = "select * from USER_ACC where USERNAME = " + insertQuotations(user_name);
            Statement stmt = conn.createStatement();
            ResultSet queryResult= stmt.executeQuery(query);
            queryResult.next();
            return new User(queryResult.getString(queryResult.findColumn("USERNAME")) , queryResult.getString(queryResult.findColumn("PASS_WORD")) , queryResult.getString(queryResult.findColumn("EMAIL")) , LocalDate.parse(queryResult.getString(queryResult.findColumn("BDATE"))));
        }
        catch(SQLException e){
            if(debug){
                System.out.println("SQLException: " + e.getMessage());
                System.out.println("SQLState: " + e.getSQLState());
            }
        }
        return null;
    }


    //UTILITY FUNCTION USED TO RETURN A QUOTED STRING
    private static String insertQuotations(String str){
        return "\"" + str + "\"" ;
    }

    @Override
    public String toString(){
        return "Username: " + user_name + "\n" +"Password: "+ password + "\n" + email + "\n" + bdate;
    }

}
