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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static parallelProject.loginpageController.cartOrder;

public class cartController implements Initializable {
    @FXML
    private Button confirm;

    @FXML
    private TreeView cartTree;

    ArrayList<Item>  items = new ArrayList<>();
    public void AddtoCart(ActionEvent event){


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

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Home Page");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> cartItems = new TreeItem<>("Cart Items: ");
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
