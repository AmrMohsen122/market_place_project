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


    @FXML
    public void goViewUsers(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("adminViewUsers.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("All Users");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goSystem(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("adminSystem.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("System Status");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
