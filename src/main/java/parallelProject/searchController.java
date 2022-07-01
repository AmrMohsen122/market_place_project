package parallelProject;

import basic_classes.Item;
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
import socket.Client;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class searchController implements Initializable {

    @FXML
    private Button cart1;

    @FXML
    private Button cart2;

    @FXML
    private Button cart3;

    @FXML
    private Button cart4;

    @FXML
    private Button cart5;

    @FXML
    private Button cart6;

    @FXML
    private Button cart7;

    @FXML
    private Button cart8;

    @FXML
    private Label i1;

    @FXML
    private Label i2;

    @FXML
    private Label i3;

    @FXML
    private Label i4;

    @FXML
    private Label i5;

    @FXML
    private Label i6;

    @FXML
    private Label i7;

    @FXML
    private Label i8;

    @FXML
    private TextField itemName;

    @FXML
    private Label notfound;

    @FXML
    private Label p1;

    @FXML
    private Label p2;

    @FXML
    private Label p3;

    @FXML
    private Label p4;

    @FXML
    private Label p5;

    @FXML
    private Label p6;

    @FXML
    private Label p7;

    @FXML
    private Label p8;

    @FXML
    private Label q1;

    @FXML
    private Label q2;

    @FXML
    private Label q3;

    @FXML
    private Label q4;

    @FXML
    private Label q5;

    @FXML
    private Label q6;

    @FXML
    private Label q7;

    @FXML
    private Label q8;

    @FXML
    private Button searchByName;

   //For the client
    static Vector<String> search = new Vector<>();
    Client client = null;
    public static int pars;
    public  int index=-1;
    public static Vector<String> items;
    
    public void startSearch() throws IOException {
        client = new Client("127.0.0.1",2022);
        client.initialize();

        Vector <String> startS = new Vector<>(2);
        startS.add(0,"searchByName");
        startS.add(1,itemName.getText());

        search = startS;
        client.send(search);

    }
// TODO
    public int searchh(Vector<Item> v, String name){
        for(int k=0; k<v.size(); k++){
            if( name.equalsIgnoreCase(v.get(k).getItem_name())){
                index=v.get(k).getIid();
                break;
            }
        }
        pars=index;
        return index;
    }

    public static Vector<Item> addcart = new Vector<>();
    @FXML
    public void addToCart1 (ActionEvent event) throws IOException{
        if(!(q1.getText().equals("Not Found"))) {
            String name;
            double price;
            name = i1.getText();
            price = Double.parseDouble(p1.getText());
            Item it = new Item(price, name);
            addcart.add(it);
        }

    }
    @FXML
    public void addToCart2 (ActionEvent event) throws IOException{
        if(!(q2.getText().equals("Not Found"))) {
            String name;
            double price;
            name = i2.getText();
            price = Double.parseDouble(p2.getText());
            Item it = new Item(price, name);
            addcart.add(it);
        }
    }
    @FXML
    public void addToCart3 (ActionEvent event) throws IOException{
        if(!(q3.getText().equals("Not Found"))) {
            String name;
            double price;
            name = i3.getText();
            price = Double.parseDouble(p3.getText());
            Item it = new Item(price,name);
            addcart.add(it);
        }
    }
    @FXML
    public void addToCart4 (ActionEvent event) throws IOException{
        if(!(q4.getText().equals("Not Found"))) {
            String name;
            double price;
            name = i4.getText();
            price = Double.parseDouble(p4.getText());
            Item it = new Item(price, name);
            addcart.add(it);
        }
    }
    @FXML
    public void addToCart5 (ActionEvent event) throws IOException{
        if(!(q5.getText().equals("Not Found"))) {
            String name;
            double price;
            name = i5.getText();
            price = Double.parseDouble(p5.getText());
            Item it = new Item(price, name);
            addcart.add(it);
        }
    }

    @FXML
    public void addToCart6 (ActionEvent event) throws IOException{
        if(!(q6.getText().equals("Not Found"))) {
            String name;
            double price;
            name = i6.getText();
            price = Double.parseDouble(p6.getText());
            Item it = new Item(price, name);
            addcart.add(it);
        }
    }

    @FXML
    public void addToCart7 (ActionEvent event) throws IOException{
        if(!(q7.getText().equals("Not Found"))) {
            String name;
            double price;
            name = i7.getText();
            price = Double.parseDouble(p7.getText());
            Item it = new Item(price, name);
            addcart.add(it);
        }
    }
    @FXML
    public void addToCart8 (ActionEvent event) throws IOException{
        if(!(q1.getText().equals("Not Found"))) {
            String name;
            double price;
            name = i8.getText();
            price = Double.parseDouble(p8.getText());
            Item it = new Item(price, name);
            addcart.add(it);
        }
    }
    public static Vector<Item> parseItems(Vector<String> items){
//        id,price,itemName,stock
        Vector<Item> itemsFound = new Vector<>();
        for (int i = 0; i < items.size(); i++) {

            String [] str = items.get(i).split(",");
            itemsFound.add(new Item(Integer.parseInt(str[0]), Double.parseDouble(str[1]), str[2],Integer.parseInt(str[3])));

        }

        return itemsFound;
    }
    @FXML
    public void goSearchItems(ActionEvent event) throws IOException {
        startSearch();
        for (int j = 0; j < search.size(); j++) {

            client.output.writeUTF(search.get(j));
        }
        String input = client.input.readUTF();
        items = new Vector<>(2);
        while(!input.equals("end")){
            items.add(input);
            input = client.input.readUTF();

        }
        int found = searchh(searchController.parseItems(items), itemName.getText());

        if(found != -1) {
            Parent root = FXMLLoader.load(getClass().getResource("searcheditems.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Searching for an item");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            notfound.setText("Item not found!");
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
        stage.setTitle("My Cart");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void gocurrent(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("recharge.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Balance");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    String i,p,q,xx;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        i="i";
        p="p";
        q="q";
        for(int k=0; k<menuController.i.size(); k++){
            xx = i+ Integer.toString(k+1);
            if(xx.equals("i1")){
                i1.setText(menuController.i.get(k).getItem_name());
                p1.setText(Double.toString(menuController.i.get(k).getPrice()));
                q1.setText(Integer.toString(menuController.i.get(k).getStock()));
            }
           else if(xx.equals("i2")){
               i2.setText(menuController.i.get(k).getItem_name());
               p2.setText(Double.toString(menuController.i.get(k).getPrice()));
               q2.setText(Integer.toString(menuController.i.get(k).getStock()));
           }
            else if(xx.equals("i3")){
                i3.setText(menuController.i.get(k).getItem_name());
                p3.setText(Double.toString(menuController.i.get(k).getPrice()));
                q3.setText(Integer.toString(menuController.i.get(k).getStock()));
            }
            else if(xx.equals("i4")){
                i4.setText(menuController.i.get(k).getItem_name());
                p4.setText(Double.toString(menuController.i.get(k).getPrice()));
                q4.setText(Integer.toString(menuController.i.get(k).getStock()));
            }
            else if(xx.equals("i5")){
                i5.setText(menuController.i.get(k).getItem_name());
                p5.setText(Double.toString(menuController.i.get(k).getPrice()));
                q5.setText(Integer.toString(menuController.i.get(k).getStock()));
            }
            else if(xx.equals("i6")){
                i6.setText(menuController.i.get(k).getItem_name());
                p6.setText(Double.toString(menuController.i.get(k).getPrice()));
                q6.setText(Integer.toString(menuController.i.get(k).getStock()));
            }else if(xx.equals("i7")){
                i7.setText(menuController.i.get(k).getItem_name());
                p7.setText(Double.toString(menuController.i.get(k).getPrice()));
                q7.setText(Integer.toString(menuController.i.get(k).getStock()));
            }
            else if(xx.equals("i8")){
                i8.setText(menuController.i.get(k).getItem_name());
                p8.setText(Double.toString(menuController.i.get(k).getPrice()));
                q8.setText(Integer.toString(menuController.i.get(k).getStock()));
            }

        }
    }
}
