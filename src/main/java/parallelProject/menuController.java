package parallelProject;

import basic_classes.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Vector;

public class menuController {

    Vector<String> testt = new Vector<>(8);
    String category;
    Client client;
    Vector<String> v2 = new Vector<>();
     Vector<Item> items = new Vector<>();
    public static Vector<Item> i;
    public void initial(){
        testt.add(0,"1");
        testt.add(1,"800");
        testt.add(2,"Iphone 12");
        testt.add(3,"5");
        testt.add(4,"2");
        testt.add(5,"900");
        testt.add(6,"Iphone 13");
        testt.add(7,"1");


    }
    public Vector<Item> parseItems (Vector<String>it) {
        int a=0;
        int b=1;
        int c=2;
        int d=3;
        int j=0;
        for (int i = 0; i < it.size(); i+=4) {
            //  String[] itemobj = it.get(i).split(",");
            Item ii = new Item(Integer.parseInt(it.get(a)),Double.parseDouble(it.get(b)),it.get(c),Integer.parseInt(it.get(d)));
            a+=4;
            b+=4;
            c+=4;
            d+=4;
            items.add(j,ii);
            j++;
        }
        return items;
    }
    @FXML
    private Button searchByCategory;

    public void fillVector() throws IOException {
     //   client= new Client("127.0.0.1",2022);
        Vector <String>vec = new Vector<>(2);
        category=searchByCategory.getId();
        vec.add(0,category);
        v2 = vec;
      //  client.send(v2);

    }

    @FXML
    public void gosearch(ActionEvent event) throws IOException {
        initial();
        i = parseItems(testt);
        fillVector();
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Search");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void goRecharge(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("recharge.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Recharge Balance");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void gohistory(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("history.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My History");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
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
    public void gocurrent(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("recharge.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Balance");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
