package parallelProject;

import java.io.*;
import java.net.Socket;

public class Client {

    private Socket socket = null;

    private DataInputStream input = null;
    private DataOutputStream output = null;

    private DataInputStream serverinput = null;

    public Client(String address, int port) {

        try {

            socket = new Socket(address, port);
            System.out.println("Connection established");
            input = new DataInputStream(System.in);
            serverinput = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());


        } catch (IOException i) {

            System.out.println(i);
        }
        String request = "";
        String response = "";
        while (!request.equals("End")) {
            try {

                System.out.println("Please enter request");
                request = input.readLine();
                System.out.println("Input taken from user");

                output.writeUTF(request);


                System.out.println("Request sent to sever " );
                System.out.println("Waiting for Request to be done");
                response = serverinput.readLine();
                while (!response.equals("exit"))
                    System.out.println("Waiting....");
                System.out.println(response);
                System.out.println("Reqeuest processed and finished");
            } catch (IOException i) {

                System.out.println(i);
            }


        }
        System.out.println("Connection Terminated");

        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    public static void main (String [] args){


        Client client = new Client("127.0.0.1", 2022);


    }
}
