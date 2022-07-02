package parallelProject;

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
import java.util.ResourceBundle;

import static parallelProject.adminMenuController.allItems;



public class adminSystemController implements Initializable {
    @FXML
    private TreeView systemTree;

    @FXML
    public void goback(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("adminMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Cart");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> Items = new TreeItem<>("All Items:");
        systemTree.setRoot(Items);

        String branchO = "Item ";
        for(int o=0 ; o<allItems.size() ; o++) {
            TreeItem<String> branch1 = new TreeItem<>(branchO + (Integer.toString(o + 1)));
            Items.getChildren().addAll(branch1);

            TreeItem<String> branch11 = new TreeItem<>("Item #: " + Integer.toString(allItems.get(o).getIid()));
            TreeItem<String> branch12 = new TreeItem<>("Item Price: " + Double.toString(allItems.get(o).getPrice()));
            TreeItem<String> branch13 = new TreeItem<>("Item Name: " + allItems.get(o).getItem_name());
            TreeItem<String> branch14 = new TreeItem<>("Pieces Left in Stock: " + Integer.toString(allItems.get(o).getStock()));
            branch1.getChildren().addAll(branch11,branch12,branch13,branch14);
        }
    }
}