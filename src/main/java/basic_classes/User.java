package basic_classes;



import java.sql.*;
import java.time.LocalDate;


//ABSTRACT CLASS BECAUSE NO USER SHOULD EXIST UNLESS IT IS AN INSTANCE OF ITS SUBCLASSES
public abstract class User {
    protected static final boolean debug = true;
    protected String user_name;
    protected String password;
    protected String email;
    protected Date bdate;

    //CONSTRUCTORS

    //EMPTY CONSTRUCTOR CREATES A TEST OBJECT
    public User(){
        this.user_name = "test";
        this.password = "test";
        this.email = "test@example.com";
        this.bdate = Date.valueOf(LocalDate.now());
    }
    //CREATES A USER WITH ALL FIELDS SPECIFIED BY ARGUMENTS OF CONSTRUCTOR
    public User(String user_name , String password , String email, Date bdate){
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
        this.bdate = null;
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

    public Date getBdate() {
        return bdate;
    }


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
    * POST_CONDITIONS: RETURNS THE PASSWORD OF THAT USER, RETURNS NULL IF AN SQL ERROR OCCURED
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
            return null;
        }
    }


    /*
     * PRE_CONDITIONS: CURRENT USERNAME DOESN'T EXISTS IN DATABASE. USERNAME, PASSWORD, EMAIL SHOULDN'T BE NULLS
     * POST_CONDITIONS: ADDS THE CURRENT USER OBJECT TO DATABASE
     */
    public void addUser(Connection conn){
        try{
            String query  = "insert into USER_ACC values (" + insertQuotations(user_name) + "," + insertQuotations(password)+ "," + insertQuotations(email)+ "," + bdate + ")";
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



    //UTILITY FUNCTION USED TO RETURN A QUOTED STRING
    protected static String insertQuotations(String str){
        return "\"" + str + "\"" ;
    }

    @Override
    public String toString(){
        return "Username: " + user_name + "\n" +"Password: "+ password + "\n" + "Email: "+ email + "\n" + "Birth date: " + bdate;
    }

}
