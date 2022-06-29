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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Vector;
import java.sql.Date;
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
    public Client client;

    public Customer parseCustomers(String str) throws ParseException {
        String[] itemA = str.split(",");
        Customer c;

        // SENT FORMAT :username+','+password+','+email+','+bdate+','+address+','+mobile_number+','+current_balance_inStr
        if(!itemA[3].equals("null") && !itemA[4].equals("null")){
            c = new Customer(itemA[0],itemA[1],itemA[2], Date.valueOf(itemA[3]),Double.parseDouble(itemA[4]),itemA[5],itemA[6]);
        }
        else if (itemA[3].equals("null")){
            c = new Customer(itemA[0],itemA[1],itemA[2], null,Double.parseDouble(itemA[4]),itemA[5],itemA[6]);
        }
        else if(itemA[4].equals("null")){
            c = new Customer(itemA[0],itemA[1],itemA[2], Date.valueOf(itemA[3]),0.00,itemA[5],itemA[6]);

        }
        else{
            c = new Customer(itemA[0],itemA[1],itemA[2], null,0.00,itemA[5],itemA[6]);
        }
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

    public void fillVector() throws IOException {
        client = new Client("127.0.0.1", 2022);
        client.socket = new Socket(client.address, client.port);
        client.input = new DataInputStream(new BufferedInputStream(client.socket.getInputStream()));
        client.output = new DataOutputStream(client.socket.getOutputStream());

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
        client.send(i1);
        validate = client.input.readUTF();
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
        fillVector();
        fcheck = check();
        flag = validation();
        System.out.println(flag);
        System.out.println(fcheck);

        if(fcheck==false){
            valid.setText("Enter all data!");
        }
        else if (flag == false){
            valid.setText("Invalid username or password!");
        }
        else if( Admin == "Customer"){
                cust = parseCustomers(validate);
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
        client.terminate();
    }


}
