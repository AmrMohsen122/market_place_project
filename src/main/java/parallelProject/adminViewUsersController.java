package parallelProject;

import basic_classes.Customer;
import basic_classes.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import socket.Client;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.Vector;
import static parallelProject.adminMenuController.allUsers;  //TODO uncomment when finishing
//TODO mafrud hnakhud allUsers deh mn login

public class adminViewUsersController implements Initializable {

    public static int indexUser;

    @FXML
    private TreeView<String> usersTree;

    public Vector<String> parse = new Vector<>();
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

    //TODO Not finished yet
    @FXML
    public void gotransaction(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("transaction.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Cart");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void selectUser(){
        //TODO save the index of the selected into indexUser

    }

    private ContextMenu addMenu = new ContextMenu();

    public void TextFieldTreeCellImpl() {
        MenuItem addMenuItem = new MenuItem("Add Employee");
        addMenu.getItems().add(addMenuItem);
        addMenuItem.setOnAction(new EventHandler() {
            public void handle(Event t) {
                TreeItem newEmployee =
                        new TreeItem<String>("New Employee");
               // getTreeItem().getChildren().add(newEmployee);
            }
        });
    }
    public int o;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> Users = new TreeItem<>("All Users:");
        usersTree.setRoot(Users);
        String branchO = "User ";
        for(o=0 ; o<allUsers.size() ; o++) {
            indexUser =o;
            TreeItem<String> branch1 = new TreeItem<>(branchO + (Integer.toString(o + 1)));
            Users.getChildren().addAll(branch1);
            TreeItem<String> branchT = new TreeItem<>("Transaction");
                Button button = new Button("Click to view");
                button.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        parse.add("viewHistory");
                        parse.add(allUsers.get(o).getUsername());
                        Parent root = null;
                        try {
                            root = FXMLLoader.load(getClass().getResource("transaction.fxml"));
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
                        stage.setTitle("My Cart");
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                    }
                });

                branchT.setGraphic(button);

            TreeItem<String> branch11 = new TreeItem<>("- Username: " + (allUsers.get(o).getUsername()).toString());
            TreeItem<String> branch12 = new TreeItem<>("- Email: " + allUsers.get(o).getEmail());
            TreeItem<String> branch13 = new TreeItem<>("- Mobile Number: " + allUsers.get(o).getMobile_number());
            TreeItem<String> branch14 = new TreeItem<>("- Current Balance: " + allUsers.get(o).getCurrent_balance());
            branch1.getChildren().addAll(branchT,branch11,branch12,branch13,branch14);
        }
        /*Users.setExpanded(true);
        usersTree.setCellFactory(
                (TreeView<String> p) -> new EditableItem()
        );

*/

    }

}
