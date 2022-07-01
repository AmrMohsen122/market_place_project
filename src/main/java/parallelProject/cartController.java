package parallelProject;

import basic_classes.Item;
import basic_classes.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import socket.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.Vector;

import static parallelProject.loginpageController.cartOrder;
import static parallelProject.loginpageController.loggedCustomer;
import static parallelProject.menuController.orders;


public class cartController implements Initializable {
    static int ordernum=1;
    @FXML
    private Button confirm;

    @FXML
    private TreeView cartTree;

    @FXML
    private Label notEnough;
    Client client = null;
    static TreeItem<String> cartItems;
    public static Vector<String> confirmVector = new Vector<>();

    public void fillCart() throws IOException {
         client = new Client("127.0.0.1",2022); //TODO uncomment
          client.initialize();                 //TODO uncomment

        Vector <String>vec = new Vector<>(10);
        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);
        if(loggedCustomer.getCurrent_balance() >= cartOrder.getTotalPrice()) {
            Order sendOrder = new Order(ordernum, date, cartOrder.getTotalPrice(), "CONFIRMED");
            for(int i=0; i<cartOrder.getItems().size(); i++)
            {
                sendOrder.addItemToOrder(cartOrder.getItems().get(i));
            }
            ordernum++;
            orders.add(sendOrder);
            vec.add(0, "confirmCart");
            vec.add(1, loggedCustomer.getUsername());
            vec.add(2, Integer.toString(sendOrder.getOID()));
            confirmVector =vec;                                     //TODO confirmVector deh el htakhudha

        }
        else{
            notEnough.setText("Balance is not enough!");
            Order unsendOrder = new Order(ordernum, date, cartOrder.getTotalPrice(), "UNCONFIRMED");
        }

    }


    @FXML
    public void goback(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Home Page");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void confirm(ActionEvent event) throws IOException {

        fillCart();
        Parent root = FXMLLoader.load(getClass().getResource("cart.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Home Page");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
                //TreeItem<String> selected = tree.getSelectionModel().getSelectedItem();
        //            selected.getParent().getChildren().remove(selected)

    }
    public void selectItem(){
      //  TreeItem<String> it = cartItems.getSelectionModel().getSelctedItem();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cartItems = new TreeItem<>("Cart Items: ");
        cartTree.setRoot(cartItems);
        String branchI = "item ";
            TreeItem<String> branch11 = new TreeItem<>("Order Price: " + Double.toString(cartOrder.getTotalPrice()));
            TreeItem<String> branch12 = new TreeItem<>("Items: ");
            cartItems.getChildren().addAll(branch11,branch12);

            for(int m=0 ; m<cartOrder.getItems().size() ; m++){
                TreeItem<String> branch121 = new TreeItem<>(branchI+ (Integer.toString(m+1)));
                branch12.getChildren().addAll(branch121);

                TreeItem<String> branch1211 = new TreeItem<>("Item #: " + Integer.toString(cartOrder.getItems().get(m).getIid()));
                TreeItem<String> branch1212 = new TreeItem<>("Item Price: " + Double.toString(cartOrder.getItems().get(m).getPrice()));
                TreeItem<String> branch1213 = new TreeItem<>("Item Name: " + cartOrder.getItems().get(m).getItem_name());
                TreeItem<String> branch1214 = new TreeItem<>("Item Quantity: " + Integer.toString(cartOrder.getItems().get(m).getItemQuantity()));
                branch121.getChildren().addAll(branch1211,branch1212,branch1213,branch1214);
            }

        }


}
