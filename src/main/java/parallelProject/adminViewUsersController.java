package parallelProject;

import basic_classes.Customer;
import basic_classes.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.Vector;

import static parallelProject.adminMenuController.allUsers;
//TODO uncomment when finishing

//TODO mafrud hnakhud allUsers deh mn login


public class adminViewUsersController implements Initializable {

    public static int indexUser;
    @FXML
    private TreeView usersTree;


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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TreeItem<String> Users = new TreeItem<>("All Users:");
        usersTree.setRoot(Users);

        String branchO = "User ";
        for(int o=0 ;  o< allUsers.size() ; o++) {
            TreeItem<String> branch1 = new TreeItem<>(branchO + (Integer.toString(o + 1)));
            Users.getChildren().addAll(branch1);
            TreeItem<String> branchT = new TreeItem<>("View Transaction");
            TreeItem<String> branch11 = new TreeItem<>("- Username: " + (allUsers.get(o).getUsername()).toString());
            TreeItem<String> branch12 = new TreeItem<>("- Email: " + allUsers.get(o).getEmail());
            TreeItem<String> branch13 = new TreeItem<>("- Mobile Number: " + allUsers.get(o).getMobile_number());
            TreeItem<String> branch14 = new TreeItem<>("- Current Balance: " + allUsers.get(o).getCurrent_balance());
            branch1.getChildren().addAll(branchT,branch11,branch12,branch13,branch14);
        }


    }

}
