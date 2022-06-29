package parallelProject;

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
    public void send(Vector<String> text ) throws IOException {

        for (int i = 0; i < text.size() ; i++) {
            output.writeUTF(text.get(i));

        }
    }
    public static void main (String [] args){




            Client client = new Client("127.0.0.1", 2022);

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
//                    response = client.serverinput.readLine();
//                while (!response.equals("exit"))
//                    System.out.println("Waiting....");
//                System.out.println(response);
//                System.out.println("Reqeuest processed and finished");
                } catch (IOException i) {

                    System.out.println(i);
                }


            }
            System.out.println("Connection Terminated");

            try {
                client.input.close();
                client.output.close();
                client.socket.close();
            } catch (IOException i) {
                System.out.println(i);
            }

        }

    }

