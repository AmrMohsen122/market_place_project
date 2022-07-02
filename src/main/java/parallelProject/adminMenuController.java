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
import java.sql.Date;
import java.util.Vector;

import static parallelProject.menuController.parseItems;

public class adminMenuController {
    public static Vector<Customer> allUsers = null;
    public static Vector<Item> allItems = null;
    Client client = null;

    /*to be sent to gui
     * orderdate
     * totalprice
     * "startItem"
     * item1 details  (iid, price, itemname, quantity)
     * item2 details
     * "endOrder"  ------> end of first order
     * orderdate
     * totalprice
     * "startItem"
     * item1 details
     * item2 details
     * "endOrder"
     * "end" -----> end of transaction
     * */
    public void loadAllUser() throws IOException {
        client = new Client("127.0.0.1",2022);
        client.initialize();
        Customer cust = null;
        allUsers = new Vector<>();
        client.output.writeUTF("sendAllUsers");
        while(true) {
            String in = client.input.readUTF();
            if(in.equals("endAll"))
                break;

            String[] parsed;
            while (!in.equals("endUser")) {
                parsed = in.split(",");
                cust = new Customer(parsed[0], parsed[1], parsed[2], Double.valueOf(parsed[3]), parsed[4], parsed[5]);
                initVec2(cust.getOrders());
                // read orders
                in = client.input.readUTF();
            }

            allUsers.add(cust);

        }

    }
    public void initVec2(Vector<Order> orders) throws IOException {
        String in = client.input.readUTF();

        Vector<String> parsedItems = new Vector<>();

        if(in != null && !in.equals("end")){
            Order o = new Order(Date.valueOf(in), Double.parseDouble(client.input.readUTF()));
            while(true) {
                in = client.input.readUTF();
                while (!in.equals("endOrder")) {
                    parsedItems.add(in);
                    in = client.input.readUTF();
                }
                o.setItems(parseItems(parsedItems , false) );
                parsedItems.clear();
                orders.add(o);
                in = client.input.readUTF();
                if(in.equals("end")){
                    break;
                }
                o = new Order(Date.valueOf(in), Double.parseDouble(client.input.readUTF()));
            }
        }
    }
    @FXML
    public void goViewUsers(ActionEvent event) throws IOException {
        loadAllUser();
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
    @FXML
    public void goTransaction(ActionEvent event) throws IOException {
        loadAllUser();
        Parent root = FXMLLoader.load(getClass().getResource("transaction.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("All Transactions");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
