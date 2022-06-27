package parallelProject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;

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

    public static String parse(BufferedReader input) throws IOException {

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
                Connection c;  //hnassign hena connection ll user
                login(type, username, password,c );
                break;


        }

        return "";
    }
    public void login(String type, String username, String password, Connection c) {
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

    public static void main(String[] args) {
        try {
            ServerHandler server = new ServerHandler();
            while (true) {
                server.listen(server.port);

                input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                parse(input);

            }
        }
        catch(IOException io){
            System.out.println(io);
        }
    }
}
