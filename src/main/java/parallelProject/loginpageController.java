package parallelProject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginpageController {


   @FXML
    public void goSignup(ActionEvent event) throws IOException {

            Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
      // stage.setHeight(420);
      // stage.setWidth(604);
       //     stage.setResizable(false);
            stage.setTitle("SignUp Page");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }

    @FXML
    public void gologin(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Home Page");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

 /*   public void switchToSignUp() throws IOException {
        Stage stage = (Stage) signup.getScene().getWindow();
        stage.close();
        Stage PrimaryStage = new Stage();
        try {
            Parent parent = FXMLLoader.load(getClass().getResource("signup.fxml"));
            Scene scene = new Scene(parent);
            PrimaryStage.setTitle("Sign_Up");
            PrimaryStage.setScene(scene);
            PrimaryStage.show();
        }catch(IOException ex) {
        }
    }*/



   /* @FXML
    public void initialize() {
        imView.setImage(
                new Image("@../../java/parallelProject/image/bag.jpeg")
        );
    }*/
    /*private void switchToSignUp(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setTitle("Sign_Up");
        stage.setScene(scene);
        stage.show();
    }*/

    /*private void switchToLogin(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(parent);
        stage.setTitle("Sign_Up");
        stage.setScene(scene);
        stage.show();
    }*/

}
