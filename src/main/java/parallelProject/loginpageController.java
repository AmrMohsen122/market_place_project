package parallelProject;

import basic_classes.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Vector;

public class loginpageController {
    public static Vector<String> i1 = new Vector<>();
    String user,pass,Login,Admin,validate;

    @FXML
    TextField username;
    @FXML
    PasswordField password;
    @FXML
    Button login;
    @FXML
    RadioButton asAdmin;
    @FXML
    Label valid;

    public static Customer cust;
    String cc = "aa,bb,ccc,2000,eee,01111";

    public Customer parseCustomers(String str) throws ParseException {
        String[] itemA = str.split(",");
        //String cc = "aa,bb,ccc,2000,eee,01111";

        //SimpleDateFormat formatter=new SimpleDateFormat("yyyy-mm-dd");
        //Date date1=formatter.parse(itemA[3]);
        Customer c = new Customer(itemA[0],itemA[1],itemA[2], Double.parseDouble(itemA[3]),itemA[4],itemA[5]);
        return c;
    }

    public void getter(){
        boolean fAdmin;
        user = username.getText();
        pass = password.getText();
        Login = login.getId();
        fAdmin = asAdmin.isSelected();
        if(fAdmin == true){
            Admin = "Admin";
        }
        else{
            Admin = "Customer";
        }
    }

    public void fillVector() {
        i1.add(Login);
        i1.add(Admin);
        i1.add(user);
        i1.add(pass);
    }

    public boolean check() {
        if ((user.isEmpty() ) || (pass.isEmpty()))
            return false;
        else
            return true;

    }

    @FXML
    public void goSignup(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("signup.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("SignUp Page");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public boolean validation() throws IOException {
        //Client.send(i1);
        // validate  ;
        if((validate == "Invalid Username") ||(validate == "Invalid Password")) {
            return false;
        }
        else {
            return true;
        }

    }
    @FXML
    public void gologin(ActionEvent event) throws IOException, ParseException {
        boolean fcheck,flag;
        getter();
        //fillVector();

        fcheck = check();
        flag = validation();
        if(fcheck==false){
            valid.setText("Enter all data!");
        }
        else if (flag == false){
            valid.setText("Invalid username or password!");
        }
        else if( Admin == "Customer"){
                cust = new Customer(parseCustomers(cc));
                Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Home Page");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }

        else{
            /*Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Home Page");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();*/
        }
    }
}
