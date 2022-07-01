package parallelProject;

import basic_classes.Order;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import static parallelProject.menuController.orders;

public class historyController implements Initializable{

    @FXML
    private TreeView tree;

    @FXML
    private Label viewHistory;


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
    public void goback(ActionEvent event) throws IOException {
        orders.clear();
        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> ORDER = new TreeItem<>("Orders:");
        tree.setRoot(ORDER);

        String branchO = "Order ";
        String branchI = "item ";
        Order o1 = orders.get(0);

        for(int o=0 ; o<orders.size() ; o++){
            TreeItem<String> branch1 = new TreeItem<>(branchO+ (Integer.toString(o+1)));
            ORDER.getChildren().addAll(branch1);

                TreeItem<String> branch11 = new TreeItem<>("Order Date: " + (orders.get(o).getODate()).toString());
                TreeItem<String> branch12 = new TreeItem<>("Order Price: " + Double.toString(orders.get(o).getTotalPrice()));
                TreeItem<String> branch13 = new TreeItem<>("Items: ");
                branch1.getChildren().addAll(branch11,branch12,branch13);

                for(int m=0 ; m<orders.get(o).getItems().size() ; m++){
                    TreeItem<String> branch131 = new TreeItem<>(branchI+ (Integer.toString(m+1)));
                    branch13.getChildren().addAll(branch131);

                    TreeItem<String> branch1311 = new TreeItem<>("Item #: " + Integer.toString(orders.get(o).getItems().get(m).getIid()));
                    TreeItem<String> branch1312 = new TreeItem<>("Item Price: " + Double.toString(orders.get(o).getItems().get(m).getPrice()));
                    TreeItem<String> branch1313 = new TreeItem<>("Item Name: " + orders.get(o).getItems().get(m).getItem_name());
                    TreeItem<String> branch1314 = new TreeItem<>("Item Quantity: " + Integer.toString(orders.get(o).getItems().get(m).getItemQuantity()));
                    branch131.getChildren().addAll(branch1311,branch1312,branch1313,branch1314);
                }
            }
    }
}
