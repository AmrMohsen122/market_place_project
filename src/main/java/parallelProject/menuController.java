package parallelProject;

import basic_classes.Item;
import basic_classes.Order;
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
import java.sql.Date;
import java.util.Vector;
import static parallelProject.loginpageController.cust;

// TODO search items lama bkhoshaha marten wara ba3d btdy error 3ayz asl7ha
// TODO 3ayz arbot search by name gui bl backend
public class menuController {

    @FXML
    private Button searchByCategory;

    @FXML
    private Button viewHistory;

    //For Search Page
    Vector<String> testt = new Vector<>(9); // ana khaletha 9 badal 8
    static Vector<String> vSearch = new Vector<>();
     Vector<Item> items = new Vector<>();

     //For History Page
    //orders contain all orders of the customer
    public static Vector<Order> orders = new Vector<>();
    public static Vector<Order> fillingOrders = new Vector<>();
     static Vector<String> vhistory = new Vector<>();
    Vector<String> parsing = new Vector<>();

    //All Items' Data
    public static Vector<Item> i;
    Client client;

    
    
    //Search Functions
    public void fillSearch() throws IOException {
        client= new Client("127.0.0.1",2022);
        client.initialize();

        Vector <String>vec = new Vector<>(2);
        vec.add(0,"searchByCategory");
        vSearch = vec;

        client.send(vSearch);
        initVec();


    }
// TODO
    public void initVec() throws IOException {

        int size = Integer.parseInt(client.input.readUTF());
        Vector<String>it = new Vector<>(size);
        
        //it.add(0,"1,800,Iphone 12,5");
        //it.add(1,"2,900,Iphone 13,1");
        
        for (int j = 0; j < size; j++) {

            it.add(client.input.readUTF());

        }

        testt = it;
    }

// TODO 3ayzeen nn3at el category kman
     public Vector<Item> parseItems (Vector<String>it) {
        int j=0;
        for (int i = 0; i < it.size(); i++) {
              String[] itemobj = it.get(i).split(",");
            Item ii = new Item(Integer.parseInt(itemobj[0]),Double.parseDouble(itemobj[1]),itemobj[2],Integer.parseInt(itemobj[3]));
            items.add(j,ii);
            j++;
        }
        return items;
    }
    
    //History Functions
    public void fillorders() throws IOException {
        //client= new Client("127.0.0.1",2022);
        //client.initialize();

        Vector <String>vec = new Vector<>();
        vec.add(0,"viewHistory");
        vec.add(1,cust.getUsername());
        vhistory = vec;

        //client.send(vhistory);
        initVec2();
    }
    
    public void initVec2() throws IOException {
        int size = Integer.parseInt(client.input.readUTF());
        Vector<String> ord  = new Vector<>(size);
        
        //ord.add(0,"2020-12-3");
        //ord.add(1,"2000");
        //ord.add(2,"startItem");
        //ord.add(3,"1,300,Iphone 12,5");
        //ord.add(4,"endOrder");
       
        //ord.add(5,"2020-12-3");
        //ord.add(6,"2000");
        //ord.add(7,"startItem");
        //ord.add(8,"1,300,Samsung 12,5");
        //ord.add(9,"endOrder");
        //ord.add(10,"end");


        for (int j = 0; j < size; j++) {
            ord.add(client.input.readUTF());
        }
        System.out.println(ord);
        parsing = ord;
    }

    public Vector<Order> parseOrders (Vector<String> ord) {
        //System.out.println(orders);
        Vector<Order> orrr = new Vector<>();
        int l=0;
        int index=0;
        String[] itemobj;
        String orderDate;
        String price;
        Order or;
        while((l<ord.size()) && !(ord.get(l).equals("end"))) {
            orderDate = ord.get(l);
            price = ord.get(l + 1);
            l += 2;
            Order LocalOrder = new Order(Date.valueOf(orderDate),Double.parseDouble(price));
            while ((l<ord.size()) && !(ord.get(l).equals("endOrder"))) {
                if (ord.get(l).equals("startItem")) {
                    l++;
                }

                itemobj = ord.get(l).split(",");
                Item itemNew = new Item(Integer.parseInt(itemobj[0]), Integer.parseInt(itemobj[3]), Double.parseDouble(itemobj[1]), itemobj[2]);
                LocalOrder.addItemToOrder(itemNew);
                l++;

            }
            orrr.add(index,LocalOrder);
            index++;
            l++;
        }
        fillingOrders = orrr;
        return orrr;
    }

    @FXML
    public void gosearch(ActionEvent event) throws IOException {
        fillSearch();
        i = parseItems(testt);
        // TODO el items ely rg3t fe i hya ely hnzahrha fe el screen
        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Search");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        // TODO msh mot2ked h7tag aterminate el connection hena walla la
        client.terminate();
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
        fillorders();
        orders = parseOrders(parsing);
        
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
        // TODO hena lama bdos current balance lw kont 3mlt update fe el databae b recharge balance el update msh byzhar hena
        Parent root = FXMLLoader.load(getClass().getResource("recharge.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Balance");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
