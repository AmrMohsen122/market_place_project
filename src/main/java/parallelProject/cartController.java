package parallelProject;

import basic_classes.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class cartController implements Initializable {
    @FXML
    private Button confirm;

    @FXML
    private Button cart1;

    @FXML
    private Button cart2;

    @FXML
    private Button cart3;

    @FXML
    private Button cart4;

    @FXML
    private Button cart5;

    @FXML
    private Button cart6;

    @FXML
    private Button cart7;

    @FXML
    private Button cart8;

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

    }
}
