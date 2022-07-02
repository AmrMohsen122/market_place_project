package socket;

import java.io.*;
import java.net.Socket;
import java.util.Vector;
public class Client {

    public Socket socket = null;
    public DataInputStream input = null;
    public DataOutputStream output = null;
    public DataInputStream serverinput = null;
    public String address = null;
    public int port = 0;

    public Client(String address, int port) {
        this.address = address;
        this.port = port;

    }
    public void initialize() throws IOException {


        socket = new Socket(address, port);
        input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        output = new DataOutputStream(socket.getOutputStream());
    }
    public void send(Vector<String> text ) throws IOException {

        for (int i = 0; i < text.size() ; i++) {
            output.writeUTF(text.get(i));

        }
    }
    public void terminate(){

        try {
            this.input.close();
            this.output.close();
            this.socket.close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }
    public static void main (String [] args){




            Client client = new Client("192.168.1.7", 2022);

            try {

                client.socket = new Socket(client.address, client.port);
                System.out.println("Connection established");
                client.input = new DataInputStream(System.in);
//                client.serverinput = new DataInputStream(client.socket.getInputStream());
                client.output = new DataOutputStream(client.socket.getOutputStream());


            } catch (IOException i) {

                System.out.println(i);
            }
            String request = "";
//            String response = "";
            while (!request.equals("end")) {
                try {

                    System.out.println("Please enter request");
                    request = client.input.readLine();
                    System.out.println("Input taken from user");

                    client.output.writeUTF(request);


                    System.out.println("Request sent to sever: " + request);
//                    System.out.println("Waiting for Request to be done");
//                    response = client.serverinput.readUTF();
//                while (!response.equals("exit"))
//                    System.out.println("Waiting....");
//                System.out.println("Response taken from server: " + response);
//                System.out.println("Reqeuest processed and finished");
                } catch (IOException i) {

                    System.out.println(i);
                }


            }
            System.out.println("Connection Terminated");



        }

    }

