package parallelProject;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import parallelProject.FunctionInterface;

public class ServerHandler implements Runnable{
    private int port = 2022;
    private  Socket socket = null;
    private ServerSocket server = null;
    public static BufferedReader input = null;
    private BufferedWriter output = null;
    public Connection conn=null;
    public User client = null;

    public ServerHandler(Socket socket){
        this.socket=socket;

    }
    @Override
    public void run(){
        try {

            this.conn = DataBaseManager.requestConnection();
            parse(input,conn,client);
            DataBaseManager.releaseConnection(this.conn);
            //momken acall el terminate hena badal m a3mlha case lw7dha fe el parse

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void terminate() throws IOException {

        socket.close();
        input.close();
        output.close();

    }

    public String parse(BufferedReader input, Connection conn, User client) throws IOException {

        switch(input.readLine()){
// login returns String on the following format:
            /*
            * login
            * Customer OR Admin
            * username
            * password
            * */
            case "login":

                String type = input.readLine();
                String username = input.readLine();
                String password = input.readLine();

                client = login(type, username, password,conn );
                break;

            case "signUp":
                //signUp returns string on the following format:
                //username
                //password
                //email
                //address
                //mobile number
                String newUserName= input.readLine();
                String newPassword=input.readLine();
                String email=input.readLine();
                String address= input.readLine();
                String mobileNumber= input.readLine();
                signUp(newUserName,newPassword,email,address,mobileNumber,conn);
                break;

            case "search":
                //item name
                String itemName= input.readLine();
                //TODO call the search function
                break;
            case "viewHistory":
                ((Customer)client).loadOrders(conn);
                break;

            case "rechargeBalance":
            /*Input on following format
            * rechargeBalance
            * amount
            * */
                double amount = Double.parseDouble(input.readLine());
                ((Customer)client).rechargeBalance(amount, conn);
                break;

            case "exit":
                this.terminate();
                //ana khalet el parse non-static hena
                break;

        }

        return "";
    }

    public static User login(String type, String username, String password, Connection c) {
        User a = null;
        if(FunctionInterface.userExists(username, c)!=0) {
            if (type.equals("Admin"))
                a = (Admin)Admin.getUserInfo(username, c);
            else if (type.equals("Customer"))
                a = (Customer)Customer.getUserInfo(username, c);
        }
        if (a!=null)
            if(!User.getPassword().equals(password)){
                //TODO Print error message "Invalid password"
                return null;
            }
            else{
                //TODO transfer user to new homescreen
                return a;
            }
        else{
            //TODO print Admin or Customer doesn't exist error message
            return null;

        }


    }
    public static void signUp(String newUserName, String newPassword, String email, String address, String mobileNumber, Connection conn) {
        Customer newUser= new Customer();
        newUser.addUser(conn);
        //  TODO check the password and confirm password are the same
        }

}
