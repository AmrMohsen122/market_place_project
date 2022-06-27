package parallelProject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket serverSocket= null;

    public Server(int port) throws IOException {
    serverSocket=new ServerSocket(port);

    }
    public static void main(String[] args) {
        try {
            Server server = new Server(2022);
            FunctionInterface.loadDriver();
            FunctionInterface.initConnection(100);
            ExecutorService threadPool = Executors.newFixedThreadPool(50);


            while (true) {
                Socket socket=server.serverSocket.accept();
                ServerHandler serverHandler=new ServerHandler(socket);
                serverHandler.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                serverHandler.conn=FunctionInterface.requestConnection();
                threadPool.execute(serverHandler);
            }
        }
        catch (IOException e){
            System.out.println(e);
        }
    }

}
