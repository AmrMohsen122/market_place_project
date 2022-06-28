package parallelProject;



import java.sql.*;
import java.time.LocalDate;
class DataBaseManager{
        public static Connection requestConnection(){

                return null;
                }
         public static void releaseConnection(Connection connect){};
        }
class User {
        public static String getPassword() {
        return null;
}

        public String getEmail() {
                return null;
        }

        public Date getBdate() {
                return null;
        }

}
class Admin extends User{
        public static Admin getUserInfo(String user_name, Connection conn){
                return null;

        }
        public void addUser(Connection conn) {

        }
        public static boolean isAdmin(String user_name, Connection conn){
                return false;
        }
}
class Customer extends User{
        public static Customer getUserInfo(String user_name, Connection conn){
                Customer c = new Customer();
                return c;
        }
        public void addUser(Connection conn) {

        }
        public void loadOrders(Connection conn){

        }
        public static boolean isCustomer(String user_name, Connection conn){
                return false;
        }
}
public class FunctionInterface {
         /*
            PRE_CONDITIONS: user_name EXISTS IN DATABASE
            POST_CONDITIONS: RETURNS WHETHER THE USERNAME PASSED IS ASSOCIATED WITH AN ADMIN ACCOUNT
         */


        /*
           PRE_CONDITIONS: user_name EXISTS IN DATABASE
           POST_CONDITIONS: RETURN AN ADMIN OBJECT WITH RECORD FOUND, RETURN NULL IF THERE IS NO USER WITH PASSED USERNAME OR IF THE USERNAME DOESN'T HAVE ADMIN PRIVILEGES
         */


        /*
            PRE_CONDITIONS: THE USERNAME DOESN'T EXIST IN DATABASE. USERNAME, PASSWORD, EMAIL SHOULDN'T BE NULLS
            POST_CONDITIONS: THE USER IS ADDED TO DATABASE WITH ADMIN PRIVILEGES
         */

        //SETTERS AND GETTERS
        public double getCurrent_balance() {
                return 0.00;
        }

        public void setCurrent_balance(double current_balance) {

        }

        public String getAddress() {
                return "ah yana";
        }

        public void setAddress(String address) {

        }

        public String getMobile_number() {
                return null;
        }

        public void setMobile_number(String mobile_number) {

        }

        /*
            PRE_CONDITIONS: USER DOESN'T EXIST IN DATABASE. USERNAME, PASSWORD, EMAIL SHOULDN'T BE NULLS
            POST_CONDITIONS: ADD USER TO CUSTOMER TABLE
         */



        /*
            PRE_CONDITIONS: USER EXISTS IN DATABASE
            POST_CONDITIONS: RETURNS THE CUSTOMER OBJECT WITH DETAILS ASSOCIATED WITH PROVIDED USERNAME
         */



    /*
        PRE_CONDITIONS: USER EXISTS
        POST_CONDITIONS: RETURN THE USER CURRENT BALANCE
     */

        //GETTERS
        public static String getUsername() {
                return null;

        }



        //TODO THREAD SYNCHRONIZATION

        /*
         * PRE_CONDITIONS: DATABASE EXISTS
         * POST_CONDITIONS: RETURNS 0 IF USER DOESN'T EXIST, 1 IF USER EXISTS, -1 IF A SQL ERROR OCCURRED
         * NOTES: EACH THREAD SHALL HAVE ITS OWN CONNECTION WHICH IS PASSED TO FUNCTIONS TO EXECUTE QUERIES ON
         */
        public static int userExists(String user_name , Connection conn){
                return 0;
        }

        /*
         * PRE_CONDITIONS: USER EXISTS IN DATABASE
         * POST_CONDITIONS: RETURNS THE PASSWORD OF THAT USER, RETURNS NULL IF AN SQL ERROR OCCURED
         */
        public static String getPassword(String user_name, Connection conn){
                return null;
        }


        /*
         * PRE_CONDITIONS: CURRENT USERNAME DOESN'T EXISTS IN DATABASE. USERNAME, PASSWORD, EMAIL SHOULDN'T BE NULLS
         * POST_CONDITIONS: ADDS THE CURRENT USER OBJECT TO DATABASE
         */




        //UTILITY FUNCTION USED TO RETURN A QUOTED STRING
        protected static String insertQuotations(String str){
                return null;

        }


        public static void initConnection(final int connectionNo){};
        public static void loadDriver(){};


    }




