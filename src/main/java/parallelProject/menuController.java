package parallelProject;

import basic_classes.Item;
import javafx.beans.binding.IntegerBinding;
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

    Vector<String> testt = new Vector<>(9); // ana khaletha 9 badal 8
    String category;
    Client client;
    static Vector<String> v2 = new Vector<>();
     Vector<Item> items = new Vector<>();
    public static Vector<Item> i;
//    public void initial(){
//        testt.add(0,"1");
//        testt.add(1,"800");
//        testt.add(2,"Iphone 12");
//        testt.add(3,"5");
//        testt.add(4,"2");
//        testt.add(5,"900");
//        testt.add(6,"Iphone 13");
//        testt.add(7,"1");
//
//
//    }
    public void initVec() throws IOException {
        int size = Integer.parseInt(client.input.readUTF());
        Vector<String>it = new Vector<>(size);

        for (int j = 0; j < size; j++) {
            it.add(client.input.readUTF());
        }
        testt = it;
    }

     public Vector<Item> parseItems (Vector<String>it) {
         System.out.println(it);
        int j=0;
        for (int i = 0; i < it.size(); i+=4) {
              String[] itemobj = it.get(i).split(",");
            Item ii = new Item(Integer.parseInt(itemobj[0]),Double.parseDouble(itemobj[1]),itemobj[2],Integer.parseInt(itemobj[3]));

            items.add(j,ii);
            j++;
        }
        return items;
    }
    @FXML
    private Button searchByCategory;

    public void fillVector() throws IOException {
        client= new Client("127.0.0.1",2022);
        client.initialize();

        Vector <String>vec = new Vector<>(2);
//        category=searchByCategory.getId();
        vec.add(0,"searchByCategory");
        v2 = vec;
        client.send(v2);
        initVec();

    }

    @FXML
    public void gosearch(ActionEvent event) throws IOException {


        fillVector();
        i = parseItems(testt);
        // TODO el items ely rg3t fe i hya ely hnzahrha fe el screen
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
