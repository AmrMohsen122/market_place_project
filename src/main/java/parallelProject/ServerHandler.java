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

    public static String parse(BufferedReader input, Connection conn) throws IOException {

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

                login(type, username, password,conn );
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
        }

        return "";
    }
    public static void login(String type, String username, String password, Connection c) {
        User a;
        if(type.equals("Admin"))
            if(Admin.isAdmin(username, c))
                a = Admin.getUserInfo(username,c);
        else if(type.equals("Customer"))
            if(Customer.isCustomer(username, c))
                a = Customer.getUserInfo(username, c);

        if (a!=null)
            if(!User.getPassword().equals(password)){
                //Print error message to user
            }
            else{
                // transfer user to new homescreen
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
                server.listen(server.port);
                Connection conn = FunctionInterface.requestConnection();
                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                parse(input, conn);

            }
        }
        catch(IOException io){
            System.out.println(io);
        }
    }
}
