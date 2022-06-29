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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;


public class rechargeController implements  Initializable{

    @FXML
    private TextField amount;

    @FXML
    private TextField currentBalance;

    @FXML
    private Label lll;

    @FXML
    private Label wrong;

    @FXML
    private Label test;

    @FXML
    private Button rechargeBalance;

    String Amount ="";
    String Recharge, Current;
    double curr;
    Vector<String> c1 = new Vector<String>();

    public void getAmount () {
        Recharge = rechargeBalance.getId();
        Amount = amount.getText();

        Current = String.valueOf(((Customer)loginpageController.cust).getCurrent_balance());
        //currentBalance.setText(Current);
    }

    public void filling(){
        c1.add(Recharge);
        c1.add(Amount);
    }


    @FXML
    public void Ok(ActionEvent event) throws IOException {
        getAmount();
        if(!Amount.isEmpty()) {
            double amountt = Double.parseDouble(Amount);
            double currentt = Double.parseDouble(Current);
            currentt = currentt + amountt;
            Current = Double.toString(currentt);
            currentBalance.setText(Current);

            amount.setText("");
            lll.setText("The Amount is added to your balance successfully!");
        }

        else{
            wrong.setText("Please enter amount!");
        }
    }

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

        Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Main Menu");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) throws NullPointerException{
        /*Amount = amount.getText();
        if(Amount == ""){
            rechargeBalance.setDisable(true);

        }*/

        Current = String.valueOf(((Customer)loginpageController.cust).getCurrent_balance());
        currentBalance.setText(Current);
    }
}