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

import java.io.IOException;
import java.util.Vector;

public class adminMenuController {

   public static Vector<Customer> allUsers = new Vector<>();
   public static Vector<Item> allItems = new Vector<>();

    public static void getUser() {
        Vector<Customer> setCustomer = new Vector<>();
        Customer user1 = new Customer("Ahmed", "12345", "ahmed@example.com", 2000, "zzzz", "01111");
        Customer user2 = new Customer("Ali", "123", "ali@example.com", 1000, "zzzz", "01111");
         //Customer user3 = new Customer("Muhamad", "1236", "muhamad@example.com", 800, "zzzz", "01111");
        Order testOrder = new Order(1,1000);
        testOrder.addItemToOrder(new Item(1,1,1000,"MOBILE"));
        user1.setOrder(testOrder);


        Order testOrder2 = new Order(2,5000);
        testOrder.addItemToOrder(new Item(2,1,5000,"LAPTOP"));
        user2.setOrder(testOrder2);
        setCustomer.add(0,user1);
        setCustomer.add(1,user2);
        allUsers =setCustomer;
        //allUsers.add(user3);
    }

    public void getItems (){
        Vector<Item> setItems = new Vector<>();
        Item item1 = new Item(1,500,"Iphone 12", 10);
        Item item2 = new Item(2,800,"DELL", 5);
        Item item3 = new Item(3,300,"Power Bank", 20);
        setItems.add(item1);
        setItems.add(item2);
        setItems.add(item3);
        allItems=setItems;
    }
    @FXML
    public void goViewUsers(ActionEvent event) throws IOException {
        getUser();
        Parent root = FXMLLoader.load(getClass().getResource("adminViewUsers.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("All Users");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goSystem(ActionEvent event) throws IOException {
        getItems();
        Parent root = FXMLLoader.load(getClass().getResource("adminSystem.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("System Status");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
