package parallelProject;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import socket.Client;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static parallelProject.adminMenuController.allUsers;

public class adminViewUsersController implements Initializable {



    @FXML
    private TreeView<String> usersTree;

    Client client;



    @FXML
    public void goback(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("adminMenu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Cart");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void gotransaction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("transaction.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Cart");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    private ContextMenu addMenu = new ContextMenu();

    public void TextFieldTreeCellImpl() {
        MenuItem addMenuItem = new MenuItem("Add Employee");
        addMenu.getItems().add(addMenuItem);
        addMenuItem.setOnAction(new EventHandler() {
            public void handle(Event t) {
                TreeItem newEmployee =
                        new TreeItem<String>("New Employee");
            }
        });
    }
  
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> Users = new TreeItem<>("All Users:");
        usersTree.setRoot(Users);
        String branchO = "User ";
        for (int o = 0; o < allUsers.size(); o++) {
            TreeItem<String> branch1 = new TreeItem<>(branchO + (Integer.toString(o + 1)));
            Users.getChildren().addAll(branch1);
            TreeItem<String> branch11 = new TreeItem<>("- Username: " + (allUsers.get(o).getUsername()).toString());
            TreeItem<String> branch12 = new TreeItem<>("- Email: " + allUsers.get(o).getEmail());
            TreeItem<String> branch13 = new TreeItem<>("- Mobile Number: " + allUsers.get(o).getMobile_number());
            TreeItem<String> branch14 = new TreeItem<>("- Current Balance: " + allUsers.get(o).getCurrent_balance());
            branch1.getChildren().addAll(branch11, branch12, branch13, branch14);
        }

    }
}
