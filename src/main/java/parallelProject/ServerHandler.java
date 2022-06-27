package parallelProject;

import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.function.Function;

import parallelProject.FunctionInterface;

public class ServerHandler {
    private int port = 2022;
    private static Socket socket = null;
    private ServerSocket server = null;
    private static BufferedReader input = null;
    private BufferedWriter output = null;

    public void listen(int port) {

        try {
            server = new ServerSocket(port);
            socket = server.accept();
        }
        //Create thread for user
        catch (IOException io) {
            System.out.println(io);
        }
    }

    public void terminate() throws IOException {

        socket.close();
        input.close();
        output.close();

    }

    public static String parse(BufferedReader input, Connection conn, User client) throws IOException {

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

    public static void main(String[] args) {

        FunctionInterface.loadDriver();
        FunctionInterface.initConnection(100);

        try {
            ServerHandler server = new ServerHandler();
            while (true) {
                User client = null;
                server.listen(server.port);
                Connection conn = FunctionInterface.requestConnection();
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                parse(input, conn, client);

            }
        }
        catch(IOException io){
            System.out.println(io);
        }
    }
}
