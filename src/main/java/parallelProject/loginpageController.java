package parallelProject;

import basic_classes.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import socket.Client;

import java.io.IOException;
import java.text.ParseException;

import java.util.Vector;
import java.sql.Date;
public class loginpageController {
    public static Vector<String> i1 = new Vector<>();
    //TODO CONTAINS CART
     public static  Order cartOrder;
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

    public static User cust;
    public static Customer loggedCustomer;
    public Client client;

    public Customer parseCustomers(String str) throws ParseException {
        String[] itemA = str.split(",");
        Customer c;
//        System.out.println(itemA);
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
    public Admin parseAdmin(String str) throws ParseException {
        String[] itemA = str.split(",");
        Admin c =  null;

        // SENT FORMAT :username+','+password+','+email+','+bdate+','+address+','+mobile_number+','+current_balance_inStr
        if(!itemA[3].equals("null")){
            c = new Admin(itemA[0],itemA[1],itemA[2], Date.valueOf(itemA[3]));
        }
        else {
            c = new Admin(itemA[0],itemA[1],itemA[2], null);
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
        client= new Client("127.0.0.1",2022);
        client.initialize();
        Vector <String>vec = new Vector<>(10);
        vec.add(0,Login);
        vec.add(1,Admin);
        vec.add(2,user);
        vec.add(3,pass);
        i1 = vec;
    }

    public Order initlog() throws IOException {
        String in = client.input.readUTF();
        Vector<String> parsedItems = new Vector<>();
        Order o = null;
        if (in != null) {
            o = new Order(Integer.parseInt(in) ,Date.valueOf(client.input.readUTF()), Double.parseDouble(client.input.readUTF()) , "UNCONFIRMED");
            in = client.input.readUTF();
            while (!in.equals("end")) {
                parsedItems.add(in);
                in = client.input.readUTF();
            }
            if (parsedItems.size() != 0) {
                o.setItems(menuController.parseItems(parsedItems, false));
            }
        }
        return o;

    }

    public Order parseCart (Vector<String> info) {
        //System.out.println(orders);
        Order inf;
        int l=0;
        String[] store;
        String orderDate;
        String price;
        Order or;
              store = info.get(l).split(",");
              //TODO deh el mafrud tb2a cust ?
              loggedCustomer = new Customer(store[0],store[1],store[2],Date.valueOf(store[3]),Double.parseDouble(store[4]),store[5],store[6]);
              l+=2;
              orderDate =info.get(l);
              price = info.get(l + 1);
              l += 2;
              inf = new Order(Date.valueOf(orderDate),Double.parseDouble(price));
              while ((l<info.size()) && !(info.get(l).equals("end"))) {

                store = info.get(l).split(",");
                Item itemNew = new Item(Integer.parseInt(store[0]), Integer.parseInt(store[3]), Double.parseDouble(store[1]), store[2]);
                inf.addItemToOrder(itemNew);
                l++;
            }
              cartOrder=inf;
        return inf;
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
//        validate ="";
        if((validate.equals("Invalid Username")) ||(validate.equals("Invalid Password"))) {
            return false;
        }
        else {
            return true;
        }

    }
    @FXML
    // TODO nsheel 7war en awl m ndos login ykhosh 3la tool
    public void gologin(ActionEvent event) throws IOException, ParseException {
        boolean fcheck,flag;
        getter();
        fillVector();
        fcheck = check();
        flag = validation();
        if(fcheck==false){
            valid.setText("Enter all data!");
        }
        else if (flag == false){
            valid.setText("Invalid username or password!");
        }
        else if( Admin.equals("Customer")){
            cust = parseCustomers(validate);
            cartOrder = initlog();
            Parent root = FXMLLoader.load(getClass().getResource("menu.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setTitle("Home Page");
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        else if(Admin.equals("Admin")){
            cust = parseAdmin(validate);
            //TODO mafrud hna b2a t3mlo haga zy initlog() trga3 ll admin kul el users fi vector of users, w kul el items
            //TODO allUsers da el vector el 3yzako tmluh bl users w da static
            //TODO allItems da el vector el 3yzako tmluh bl items w da static brdo
            // TODO el mfrood dol ytghyro yb2o 7agat el admin
            Parent root = FXMLLoader.load(getClass().getResource("adminMenu.fxml"));
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
     //   client.terminate(); //TODO msh fakra ento ely 3mlnha comment wla ehna
    }


}
