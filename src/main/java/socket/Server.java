package socket;

import database.manager.DatabaseManager;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends Thread{
    private ServerSocket serverSocket= null;

    public Server(int port) throws IOException {
    serverSocket=new ServerSocket(port);

    }
    public Server(){


    }

    public static void dbInitialize() throws SQLException {
        DatabaseManager.loadDriver();
        DatabaseManager.initConnection(100);
    }
    public static void handleClient(Server server, ExecutorService threadPool) throws IOException {

        Socket socket=server.serverSocket.accept();
        System.out.println("Connection accepted");
        DataInputStream input = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        ServerHandler serverHandler = new ServerHandler(socket, input);
        threadPool.execute(serverHandler);

    }

    @Override
    public void run() {
        try {
            Server server = new Server(2022);
            Server.dbInitialize();  // initializes a connection from server to the database
            ExecutorService threadPool = Executors.newFixedThreadPool(50);

            while (true) {
                Server.handleClient(server, threadPool);  //initializes a new socket for incoming request and executes a thread for it
            }
        }
        catch (IOException e){
            System.out.println(e);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
