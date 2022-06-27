package parallelProject;

import basic_classes.Admin;
import basic_classes.Customer;
import basic_classes.User;
import database.manager.DatabaseManager;

import java.sql.*;
import java.time.LocalDate;

public class FunctionInterface {
         /*
            PRE_CONDITIONS: user_name EXISTS IN DATABASE
            POST_CONDITIONS: RETURNS WHETHER THE USERNAME PASSED IS ASSOCIATED WITH AN ADMIN ACCOUNT
         */
        private static boolean isAdmin(String user_name, Connection conn){
            }

        /*
           PRE_CONDITIONS: user_name EXISTS IN DATABASE
           POST_CONDITIONS: RETURN AN ADMIN OBJECT WITH RECORD FOUND, RETURN NULL IF THERE IS NO USER WITH PASSED USERNAME OR IF THE USERNAME DOESN'T HAVE ADMIN PRIVILEGES
         */
        public static basic_classes.Admin getUserInfo(String user_name, Connection conn){
            }

        /*
            PRE_CONDITIONS: THE USERNAME DOESN'T EXIST IN DATABASE. USERNAME, PASSWORD, EMAIL SHOULDN'T BE NULLS
            POST_CONDITIONS: THE USER IS ADDED TO DATABASE WITH ADMIN PRIVILEGES
         */
        @Override
        public void addUser(Connection conn){

        }
        //SETTERS AND GETTERS
        public double getCurrent_balance() {

        }

        public void setCurrent_balance(double current_balance) {

        }

        public String getAddress() {

        }

        public void setAddress(String address) {

        }

        public String getMobile_number() {

        }

        public void setMobile_number(String mobile_number) {

        }

        /*
            PRE_CONDITIONS: USER DOESN'T EXIST IN DATABASE. USERNAME, PASSWORD, EMAIL SHOULDN'T BE NULLS
            POST_CONDITIONS: ADD USER TO CUSTOMER TABLE
         */
        @Override
        public void addUser(Connection conn) {

        }

        /*
            PRE_CONDITIONS: USER EXISTS IN DATABASE
            POST_CONDITIONS: RETURNS THE CUSTOMER OBJECT WITH DETAILS ASSOCIATED WITH PROVIDED USERNAME
         */
        public static basic_classes.Customer getUserInfo(String user_name, Connection conn){
            }

    /*
        PRE_CONDITIONS: USER EXISTS
        POST_CONDITIONS: RETURN THE USER CURRENT BALANCE
     */

        //GETTERS
        public String getUsername() {

        }

        public String getPassword() {

        }

        public String getEmail() {

        }

        public Date getBdate() {

        }

        //TODO THREAD SYNCHRONIZATION

        /*
         * PRE_CONDITIONS: DATABASE EXISTS
         * POST_CONDITIONS: RETURNS 0 IF USER DOESN'T EXIST, 1 IF USER EXISTS, -1 IF A SQL ERROR OCCURRED
         * NOTES: EACH THREAD SHALL HAVE ITS OWN CONNECTION WHICH IS PASSED TO FUNCTIONS TO EXECUTE QUERIES ON
         */
        public static int userExists(String user_name , Connection conn){

        }

        /*
         * PRE_CONDITIONS: USER EXISTS IN DATABASE
         * POST_CONDITIONS: RETURNS THE PASSWORD OF THAT USER, RETURNS NULL IF AN SQL ERROR OCCURED
         */
        public static String getPassword(String user_name, Connection conn){

        }


        /*
         * PRE_CONDITIONS: CURRENT USERNAME DOESN'T EXISTS IN DATABASE. USERNAME, PASSWORD, EMAIL SHOULDN'T BE NULLS
         * POST_CONDITIONS: ADDS THE CURRENT USER OBJECT TO DATABASE
         */
        public void addUser(Connection conn){
                 }



        //UTILITY FUNCTION USED TO RETURN A QUOTED STRING
        protected static String insertQuotations(String str){

        }


    }




