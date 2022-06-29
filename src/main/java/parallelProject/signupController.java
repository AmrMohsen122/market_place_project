package parallelProject;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
import java.util.Vector;


public class signupController  {
    String Email,Username,Address,Mobile,Bdate, Signup;
    Client client = null;
    String Password = "";
    String Confirm ;
    public static Vector<String> v1 = new Vector<String>();

    @FXML
    TextField email;
    @FXML
    TextField username;
    @FXML
    TextField address;
    @FXML
    TextField mobile;
    @FXML
    PasswordField password;
    @FXML
    PasswordField confirm;
    @FXML
    DatePicker bdate;
    @FXML
    Label match;
    @FXML
    Button signup;


    public void filling(){
        Signup = "Signup";
        Email = email.getText();
        Username = username.getText();
        Address = address.getText();
        Mobile = mobile.getText();
        Password = password.getText();
        Confirm = confirm.getText();
        Bdate = bdate.getValue().toString();

        //Date firstDate1 = new Date(year, month, day);

    }

    public void fillVector() throws IOException {
        client= new Client("127.0.0.1",2022);
        client.initialize();
        Vector <String>vec = new Vector<>(10);
        vec.add(0,Signup);
        vec.add(1,Email);
        vec.add(2,Username);
        vec.add(3,Address);
        vec.add(4,Mobile);
        vec.add(5,Bdate);
        vec.add(6,Password);





        v1 = vec;
        client.send(v1);

    }

    public boolean checking() {
        if ((Email.isEmpty() ) || (Username.isEmpty()) || (Address.isEmpty()) || (Mobile.isEmpty()) || (Password.isEmpty()) || (Confirm.isEmpty()) )
            return false;
        else
            return true;

    }

    public boolean matching(){
        if(Password.matches(Confirm))
        {return  true;}
        else
            return false;
    }

    @FXML
    public void gologin(ActionEvent event) throws IOException {
    // TODO lama el signup yfail ytl3 error message w yebo fe screen el signup
        filling();


        boolean flagfill, flagpass;
        flagpass = matching();
        flagfill = checking();
        if(flagfill == false)
        {
            match.setText("Enter all Data!");
        }
        else if(flagpass == false){
            match.setText("Password mismatch!");
        }
        else
        {
            fillVector();

            Parent root = FXMLLoader.load(getClass().getResource("loginpage.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Login Page");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        client.terminate();
    }


}
