package parallelProject;

import basic_classes.Customer;
import basic_classes.Order;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

import static parallelProject.adminMenuController.allUsers;


public class transactionController implements Initializable {

    @FXML
    private TreeView transactionTree;

    @FXML
    public void goback(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("adminViewUsers.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("All Users");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> ORDER = new TreeItem<>("Orders:");
        transactionTree.setRoot(ORDER);
        String branchO = "Order ";
        String branchI = "item ";
        Customer showCustomerTransaction = allUsers.get(0); //TODO indexUser instead of 0
        for(int o=0 ; o<showCustomerTransaction.getOrders().size() ; o++){
            TreeItem<String> branch1 = new TreeItem<>(branchO+ (Integer.toString(o+1)));
            ORDER.getChildren().addAll(branch1);
             String test1 = Double.toString(showCustomerTransaction.getOrders().get(o).getTotalPrice());
           //  String test2 = (showCustomerTransaction.getOrders().get(0).getODate()).toString();
           // TreeItem<String> branch11 = new TreeItem<>("Order Date: " + test2);
            TreeItem<String> branch12 = new TreeItem<>("Order Price: " +test1);
            TreeItem<String> branch13 = new TreeItem<>("Items: ");
            branch1.getChildren().addAll(branch12,branch13);

            for(int m=0 ; m<showCustomerTransaction.getOrders().get(o).getItems().size() ; m++){
                TreeItem<String> branch131 = new TreeItem<>(branchI+ (Integer.toString(m+1)));
                branch13.getChildren().addAll(branch131);

                TreeItem<String> branch1311 = new TreeItem<>("Item #: " + Integer.toString(showCustomerTransaction.getOrders().get(o).getItems().get(m).getIid()));
                TreeItem<String> branch1312 = new TreeItem<>("Item Price: " + Double.toString(showCustomerTransaction.getOrders().get(o).getItems().get(m).getPrice()));
                TreeItem<String> branch1313 = new TreeItem<>("Item Name: " + showCustomerTransaction.getOrders().get(o).getItems().get(m).getItem_name());
                TreeItem<String> branch1314 = new TreeItem<>("Item Quantity: " + Integer.toString(showCustomerTransaction.getOrders().get(o).getItems().get(m).getItemQuantity()));
                branch131.getChildren().addAll(branch1311,branch1312,branch1313,branch1314);
            }
        }
    }
}
