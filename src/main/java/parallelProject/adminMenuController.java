package parallelProject;

import basic_classes.Customer;
import basic_classes.Item;
import basic_classes.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import socket.Client;

import java.io.IOException;
import java.util.Vector;

public class adminMenuController {
    public static Vector<Customer> allUsers = null;
    public static Vector<Item> allItems = null;
    Client client = null;


    @FXML
    public void goViewUsers(ActionEvent event) throws IOException {
        client = new Client("127.0.0.1",2022);
        client.initialize();
        allUsers = new Vector<>();
        client.output.writeUTF("sendAllUsers");
        String in = client.input.readUTF();
        String[] parsed;
        while(!in.equals("end")){
            parsed = in.split(",");
            allUsers.add(new Customer(parsed[0] , parsed[1] , parsed[2] , Double.valueOf(parsed[3]) , parsed[4] , parsed[5]));
            in = client.input.readUTF();
        }
        Parent root = FXMLLoader.load(getClass().getResource("adminViewUsers.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("All Users");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goSystem(ActionEvent event) throws IOException {
        client = new Client("127.0.0.1",2022);
        client.initialize();
        allItems = new Vector<>();
        client.output.writeUTF("sendAllItems");
        String in = client.input.readUTF();
        String[] parsed;
        while(!in.equals("end")){
            parsed = in.split(",");
            allItems.add(new Item(Integer.valueOf(parsed[0]) , Double.valueOf(parsed[1]) , parsed[2] , parsed[3],Integer.valueOf(parsed[4]) , parsed[5]));
            in = client.input.readUTF();
        }
        Parent root = FXMLLoader.load(getClass().getResource("adminSystem.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("System Status");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}