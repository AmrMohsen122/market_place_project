package parallelProject;

import basic_classes.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import socket.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

import static parallelProject.loginpageController.cartOrder;
import static parallelProject.searchController.items;


public class searcheditemsController implements Initializable {
    static int indexitem;

    Client client = null;

    @FXML
    private Label itemname;

    @FXML
    private Label price;

    @FXML
    private Label quantity;
    
    @FXML
    public void Addtocart(ActionEvent event) throws IOException {
        //IID IS searchController.pars
        //toBeSent.add(Integer.toString(searchController.parseItems(items).get(0).getIid()));

        if(!(itemname.getText().equals("Not Found"))) {
            client = new Client("127.0.0.1",2022);
            client.initialize();
            Vector<String> toBeSent = new Vector<>();
            toBeSent.add("addToCart");
            toBeSent.add(Integer.toString(cartOrder.getOID()));
            toBeSent.add(Integer.toString(searchController.pars));
            toBeSent.add("1");
            toBeSent.add(price.getText());
            client.send(toBeSent);
            double addedPrice = Double.parseDouble(price.getText());
            cartOrder.addItemToOrder(new Item(1,1,Double.parseDouble(price.getText()),itemname.getText()));
            addedPrice += cartOrder.getTotalPrice();
            System.out.println(addedPrice);
            cartOrder.setTotalPrice(addedPrice);
        }
    }
    
    @FXML
    public void goviewcart(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("cart.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Cart");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void gocurrent(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("recharge.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Balance");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void goback(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Search");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Vector<Item> v = menuController.parseItems(items , true);
        for(int j=0; j< v.size(); j++){
            if (searchController.pars == v.get(j).getIid())
            {
                itemname.setText(v.get(j).getItem_name());
                price.setText(Double.toString(v.get(j).getPrice()));
                quantity.setText(Integer.toString(v.get(j).getStock()));
                indexitem =j;
                break;
            }
        }
    }
}

