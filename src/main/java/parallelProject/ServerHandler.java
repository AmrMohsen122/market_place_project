package parallelProject;
import java.io.*;

import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

import java.sql.Date;
import basic_classes.*;

import database.manager.*;


public class ServerHandler implements Runnable{
    private int port = 2022;
    private  Socket socket = null;

    public DataInputStream input = null;
    private DataOutputStream output = null;
    public Connection conn=null;
    public User client = null;

    public ServerHandler(Socket socket){
        this.socket=socket;

    }
    @Override
    public void run(){
        try {

            this.conn = DatabaseManager.requestConnection();
//            System.out.println(conn);
//            System.out.println(this.input);
            parse(input,conn,client);
            DatabaseManager.releaseConnection(this.conn);
            terminate();
            //momken acall el terminate hena badal m a3mlha case lw7dha fe el parse

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void terminate() throws IOException {

        socket.close();
        input.close();
//        output.close();

    }
// TODO aghyar parse akhleha void
    public String parse(DataInputStream input, Connection conn, User client) throws IOException, SQLException {
        Vector<Item> items=null;
        Vector <String>itemsFound=null;
        String in = input.readUTF();

        System.out.println(in);
        switch(in){

// login returns String on the following format:
            /*
            * login
            * Customer OR Admin
            * username
            * password
            * */

            case "login":
                System.out.println("i am here");

                String type = input.readUTF();
                String username = input.readUTF();
                String password = input.readUTF();

                System.out.println("3adet");

                client = login(type, username, password,conn );
                if(client instanceof Customer) {
                    double current_balance = ((Customer)client).getCurrent_balance();
                    String current_balance_inStr=String.valueOf(current_balance);
                    // TODO asend el current balance ll GUI

                    String email =((Customer)client).getEmail() ;
                    Date bdate =((Customer)client).getBdate();
                    String address=((Customer)client).getAddress() ;
                    String mobile_number =((Customer)client).getMobile_number();
                    String strToBePassed="";
                    strToBePassed=username+','+password+','+email+','+bdate+','+address+','+mobile_number+','+current_balance_inStr;
                    this.output.writeUTF(strToBePassed);
                }
                System.out.println("Username: " + username + "\n password: " + password);
                break;
            case "signUp":
                //signUp returns string on the following format:
                //username
                //password
                //email
                //address
                //mobile number
                String newUserName= input.readLine();
                String newPassword=input.readLine();
                String email=input.readLine();
                String address= input.readLine();
                String mobileNumber= input.readLine();
                signUp(newUserName,newPassword,email,address,mobileNumber,conn);
                // TODO check en el password w el confirm password text boxes contain same password

                break;


            case "viewHistory":
                ((Customer)client).loadOrders(conn);

                break;

            case "rechargeBalance":
            /*Input on following format
            * rechargeBalance
            * amount
            * */
                double amount = Double.parseDouble(input.readLine());
                ((Customer)client).rechargeBalance(amount, conn);
                // TODO display message "Successful balance recharge"
                break;

            case "searchByName":
                /*Input Format
                * searchByName
                * itemName
                * */
                // TODO azwd fe el GUI eno ycall searchByName bas lw feh 7aga maktoba gowa el Search Box
                String itemName = input.readLine();
                if(Item.itemExists(itemName, conn)!=0)
                     items = Item.search_by_name(itemName, conn);
                // TODO return item details to GUI
                break;

            case "searchByCategory":
                 /*Input Format
                 * searchByCategory
                 * category name
                 * */

                //Category already exists since it is chosen from a menu

                String categoryName = input.readLine();
                items = Item.search_by_category(categoryName, conn);
                // TODO return item details to GUI
                break;

            case "exit":
                this.terminate();
                //ana khalet el parse non-static hena
                break;
            case "confirmCart":
                /*Input format
                *orderDate
                *totalPrice
                *item1 details (iid,price,item_name,seller_name,stock,category,itemQuantity)
                *item2 details
                *-------
                *end
                * */
             // TODO send "Item Not Found"
            // TODO pass the object contains the order in the make order funcn
                Date oDate = Date.valueOf(input.readLine());
                double tPrice = Double.parseDouble(input.readLine());
                Order o = new Order(oDate, tPrice);
                String nextItem = input.readLine();

                while(!nextItem.equals("end")) {

                    o.addItemToOrder(parseItems(nextItem));
                    nextItem = input.readLine();
                }

                ((Customer)client).makeOrder(o,conn);
                break;


        }

        return "";
    }


    public Item parseItems(String str){
        String[] itemD = str.split(",");
        Item i = new Item(Integer.parseInt(itemD[0]),Double.parseDouble(itemD[1]),itemD[2],itemD[3],Integer.parseInt(itemD[4]),itemD[5],Integer.parseInt(itemD[6]));
        return i;
    }
    public User login(String type, String username, String password, Connection c) {
        User a = null;
        double balance = -1;
        if(User.userExists(username, c)!=0) {
            if (type.equals("Admin"))
                a = (Admin)Admin.getUserInfo(username, c);
            else if (type.equals("Customer"))
                a = (Customer)Customer.getUserInfo(username, c);
        }
        if (a!=null) {
            String str = User.getPassword(username, conn);
            if (!str.equals(password)) {
                //TODO Print error message "Invalid password"
                System.out.println("Invalid Password");
                return null;
            } else {
                // TODO transfer user to new homescreen w lw el user Customer call getBalance "Valid"
                System.out.println("Username: " + username + "\n password: " + password);

                return a;
            }
        }
        else{
            //TODO print Admin or Customer doesn't exist error message "Invalid Username"
            System.out.println("Invalid Username");
            return null;

        }


    }
    public void signUp(String newUserName, String newPassword, String email, String address, String mobileNumber, Connection conn) throws IOException {
        Customer newUser= new Customer();
        if (User.userExists(newUserName,conn)==0) {
            newUser.addUser(conn);
            this.output.writeUTF("Valid");
        }
        else
            this.output.writeUTF("Invalid");
        //  TODO check the password and confirm password are the same
    }
    public Vector<String> loadItems(Vector <Item>items) {

        Vector<String> itemDet = new Vector<String>();
        for (int i = 0; i < items.size(); i++) {
            itemDet.add(String.valueOf(items.get(i).getIid()));
            itemDet.add(String.valueOf(items.get(i).getPrice()));
            itemDet.add(items.get(i).getItem_name());
            itemDet.add(items.get(i).getSeller_name());
            itemDet.add(String.valueOf(items.get(i).getStock()));
            itemDet.add(items.get(i).getCategory());
        }
        return itemDet;
    }
    public static void addVectorToVector(Vector<String>first,Vector<String>second){
        for (int i = 0; i < second.size(); i++) {
            first.add(second.get(i));
        }
    }
    public Vector<String> loadOrderDetails(Customer client)
                        /*vector of order details
                format is: first order date
                first order price
                first item in first order iid;
                    price
                    item_name
                    seller_name
                    stock
                    category
                second order date .....
                */
    {
        Vector<Order> orders =((Customer)client).getOrders();
        Vector <String> ordDet = new Vector<String>();

        for (int i = 0; i < orders.size(); i++) {
            Vector <String> itemDet= loadItems(orders.get(i).getItems());
            ordDet.add(orders.get(i).getODate().toString());
            ordDet.add(String.valueOf(orders.get(i).getTotalPrice()));
            addVectorToVector(ordDet,itemDet);

        }
        return ordDet ;
    }
}
