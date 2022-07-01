package parallelProject;

import basic_classes.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static parallelProject.loginpageController.cartOrder;



public class searcheditemsController implements Initializable {
    static int indexitem;
    

    @FXML
    private Label itemname;

    @FXML
    private Label price;

    @FXML
    private Label quantity;
    
    @FXML
    public void Addtocart(ActionEvent event) throws IOException {
        //IID IS searchController.pars
        //toBeSent.add(Integer.toString(searchController.parseItems(items).get(0).getIid()));

        if(!(quantity.getText().equals("Not Found"))) {
            double addedPrice =0;
            cartOrder.addItemToOrder(new Item(8,1,Double.parseDouble(price.getText()),itemname.getText()));
            addedPrice += cartOrder.getTotalPrice();
            cartOrder.setTotalPrice(addedPrice);
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
    public void gocurrent(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("recharge.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("My Balance");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void goback(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle("Search");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TODO
        for(int j=0; j<menuController.i.size(); j++){
            if (searchController.pars == menuController.i.get(j).getIid())
            {
                itemname.setText(menuController.i.get(j).getItem_name());
                price.setText(Double.toString(menuController.i.get(j).getPrice()));
                quantity.setText(Integer.toString(menuController.i.get(j).getStock()));
                indexitem =j;
                break;
            }
        }
    }
}

