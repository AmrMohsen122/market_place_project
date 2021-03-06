package socket;
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

    public ServerHandler(Socket socket, DataInputStream input){
        this.socket=socket;
        this.input = input;

    }
    @Override
    public void run(){
        try {

            this.conn = DatabaseManager.requestConnection();
            this.output = new DataOutputStream(this.socket.getOutputStream());
            parse(input,conn,client);
            DatabaseManager.releaseConnection(this.conn);
            terminate();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void terminate() throws IOException {
        socket.close();
        input.close();
        output.close();

    }
    public void parse(DataInputStream input, Connection conn, User client) throws IOException, SQLException {
        Vector<String>orderHistory;
        Vector<Item> items=null;
        Vector <String>itemsFound=null;
        String in = input.readUTF();
        String email = null;
        String password = null;
        Date bdate = null;
        String address = null;
        String mobile_number = null;
        Order order = null;
        switch(in){

// login returns String on the following format:
            /*
            * login
            * Customer OR Admin
            * username
            * password
            * */
            case "sendAllItems":

                Vector<Item> allItems= Item.getAllItems(conn);

                Vector<String> send = loadAllItems(allItems);

                for (int i = 0; i < send.size() ; i++) {
                    output.writeUTF(send.get(i));
                }
                this.output.writeUTF("end");
                /*Output on following format:
                * item1details (iid, price, name, stock)
                * item2deatils
                *
                * "end"
                * */

                break;
            case "sendAllUsers":
                Vector<Customer> customers = Customer.getAllCustomers(conn);
                Vector<String> sendUsers = loadAllUsers(customers);
                for (int i = 0; i < sendUsers.size() ; i++) {
                    output.writeUTF(sendUsers.get(i));
                    orderHistory = loadOrderDetails(customers.get(i));
                    for (int j = 0; j < orderHistory.size(); j++) {
                        this.output.writeUTF(orderHistory.get(j));

                    }
                    this.output.writeUTF("endUser");
                }

                this.output.writeUTF("endAll");


                /*
                * User1deatils (String user_name, String password, String email, double current_balance, String address, String mobile_number
                * user1orders
                * "endUser"
                *
                *
                * "end"
                * */

                break;

            case "login":
                String type = input.readUTF();
                String username = input.readUTF();
                password = input.readUTF();



                client = login(type, username, password, email, bdate,address, mobile_number,conn );

//                    String user_name, String password, String email, double current_balance, String address, String mobile_number
                        /*Output Format is:
                        * client details
                        * startOrder
                        * cartDetails                /*Output on the following format:
                                                     * OrderDate
                                                     * total price
                                                     * item1 details (iid,price, itemname , quantity)
                                                     * item2 details
                                                     * "end
                         * */

                break;
            case "Signup":
                //signUp returns string on the following format:
                //username
                //password
                //email
                //address
                //mobile number
                email=input.readUTF();
                String newUserName= input.readUTF();
                address= input.readUTF();
                mobile_number= input.readUTF();
                bdate = Date.valueOf(input.readUTF());
                String newPassword=input.readUTF();

                signUp(newUserName,newPassword,email,bdate,address,mobile_number,conn);

                break;


            case "viewHistory":
                client = Customer.getUserInfo(input.readUTF(),conn);
                orderHistory = new Vector<String>();
                orderHistory = loadOrderDetails(((Customer) client));
                for (int i = 0; i < orderHistory.size(); i++) {
                    this.output.writeUTF(orderHistory.get(i));
                }

                this.output.writeUTF("end");
                //iid, price, itemname, stock
                /*to be sent to gui
                 * orderdate
                 * totalprice
                 * "startItem"
                 * item1 details  (iid, price, itemname, quantity)
                 * item2 details
                 * "endOrder"  ------> end of first order
                 * orderdate
                 * totalprice
                 * "startItem"
                 * item1 details
                 * item2 details
                 * "endOrder"
                 * "end" -----> end of transaction
                 * */
                break;

            case "rechargeBalance":
            /*Input on following format
            * rechargeBalance
            * amount"rechargeBalance"
            * */
                double amount = Double.parseDouble(input.readUTF());
                client = Customer.getUserInfo(input.readUTF(), conn);
                ((Customer)client).rechargeBalance(amount, conn);

                break;

            case "searchByName":

                //id, price, itemname, stock

                /*Input Format
                 * searchByName
                 * itemName
                 * */
                String itemName = input.readUTF();
                if(Item.itemExists(itemName, conn)!=0)

                    items = Item.search_by_name(itemName, conn);

                itemsFound=loadItems(items,"stock");
                for (int i = 0; i < itemsFound.size(); i++) {
                    this.output.writeUTF(itemsFound.get(i));
                }
                this.output.writeUTF("end");
                break;

            case "searchByCategory":
                /*Input Format
                 * searchByCategory
                 *
                 * */

                //Category already exists since it is chosen from a menu

                Vector<Item>items1 = Item.search_by_3_category("mobile", conn);
                for (int i = items1.size(); i < 3; i++)
                    items1.add(new Item());



                Vector<Item>items2 = Item.search_by_3_category("laptop", conn);
                for (int i = items2.size(); i < 2; i++)
                    items2.add(new Item());

                Vector<Item>items3 = Item.search_by_3_category("accessory", conn);
                for (int i = items3.size(); i < 3; i++)
                    items3.add(new Item());

                itemsFound=loadItems(items1,"stock");
                int size = items1.size()+items2.size()+ items3.size();
                this.output.writeUTF(String.valueOf(size));
                for (int i = 0; i < itemsFound.size(); i++) {
                    this.output.writeUTF(itemsFound.get(i));

                }
                itemsFound=loadItems(items2,"stock");
                for (int i = 0; i < itemsFound.size(); i++) {
                    this.output.writeUTF(itemsFound.get(i));
                }
                itemsFound=loadItems(items3,"stock");
                for (int i = 0; i < itemsFound.size(); i++) {
                    this.output.writeUTF(itemsFound.get(i));
                }


                break;

            case "exit":
                this.terminate();
                break;

            case "loadCart":
                /*Input on the following format
                * loadCart
                * username
                * */

                username = input.readUTF();
                order = Customer.loadCart(username, conn);
                items = order.getItems();

                sendCart(username);

                /*Output on the following format:
                * OrderDate
                * total price
                * item1 details (iid,price, itemname , quantity)
                * item2 details
                * "end
                * */
                break;

            case "addToCart":
                /*Input on the following format
                * addToCart
                * Order ID
                * item ID
                * item quantity
                * item price
                * */
                int oid = Integer.parseInt(this.input.readUTF());
                int iid = Integer.parseInt(this.input.readUTF());
                int itemQty = Integer.parseInt(this.input.readUTF());
                double itemPrice = Double.parseDouble(this.input.readUTF());
                order = Order.getOrderByID(oid,conn);
                order.addItem(iid,itemQty, itemPrice,conn);
                break;

            case "removeFromCart":
                int orderId = Integer.valueOf(input.readUTF());
                int itemId = Integer.valueOf(input.readUTF());
                int itemQuantity = Integer.valueOf(input.readUTF());
                double price = Double.valueOf(input.readUTF());

                order = Order.getOrderByID(orderId,conn);
                order.removeItem(itemId,itemQuantity, price,conn);


                break;
            case "confirmCart":
                /* Input format
                 * confirmCart
                 * client ID
                 * orderID
                 * */

                client = Customer.getUserInfo(input.readUTF(), conn);

                Order o = Order.getOrderByID(Integer.parseInt(input.readUTF()) , conn);
                System.out.println("getOrderById " + o);
                int valid = ((Customer)client).makeOrder(o,conn);
                sendCart(client.getUsername());


                /*
                * Ouptut on the following format
                * Valid Transaction if the transaction is successful
                * Invalid Transaction if the transaction is unsuccessful
                * */
                break;
        }


    }

    public void sendCart(String username) throws IOException, SQLException {
        Order order;
        Vector<Item> items;
        order = Customer.loadCart(username, conn);
        items = order.getItems();
        this.output.writeUTF(String.valueOf(order.getOID()));
        this.output.writeUTF(String.valueOf(order.getODate()));
        this.output.writeUTF(String.valueOf(order.getTotalPrice()));
        Vector<String> itemsFound;
        if(items.size()!=0){
            itemsFound = loadItems(items,"Qty");
            for (int i = 0; i < itemsFound.size(); i++) {
                this.output.writeUTF(itemsFound.get(i));
            }

        }
        this.output.writeUTF("end");
        /*Output on the following format:
         * OrderDate
         * total price
         * item1 details (iid,price, itemname , quantity)
         * item2 details
         * "end"
         * */

    }
    public Item parseItems(String str){
        String[] itemD = str.split(",");
        Item i = new Item(Integer.parseInt(itemD[0]),Double.parseDouble(itemD[1]),itemD[2],itemD[3],Integer.parseInt(itemD[4]),itemD[5],Integer.parseInt(itemD[6]));
        return i;
    }
//    type, username, password, email, bdate,address, mobile_number,conn
    public User login(String type, String username, String password,String email, Date bdate, String address, String mobile_number, Connection c) throws IOException, SQLException {

        double balance = -1;
        if(User.userExists(username, c)!=0) {
            if (type.equals("Admin"))
                client = (Admin)Admin.getUserInfo(username, c);
            else if (type.equals("Customer"))
                client = (Customer)Customer.getUserInfo(username, c);
        }

        if (client!=null) {
            String str = User.getPassword(username, conn);
            if (!str.equals(password)) {

                this.output.writeUTF("Invalid Password");

                return null;
            } else {
                email = client.getEmail();
                bdate = client.getBdate();

                String strToBePassed = "";
                strToBePassed = username + ',' + password + ',' + email + ',' + bdate;
                if (client instanceof Customer) {
                    double current_balance = ((Customer) client).getCurrent_balance();
                    String current_balance_inStr = String.valueOf(current_balance);
                    address = ((Customer) client).getAddress();
                    mobile_number = ((Customer) client).getMobile_number();
                    strToBePassed += ',' + current_balance_inStr + ',' + address + ',' + mobile_number;
                    this.output.writeUTF(strToBePassed);
                    sendCart(username);

                }
                else{
                    this.output.writeUTF(strToBePassed);
                }
                // username,password,email,bdate,current_balance,address, mobile_number
                return client;
            }
        }
        else{

            this.output.writeUTF("Invalid Username");

            return null;

        }


    }
    public void signUp(String newUserName, String newPassword, String email,Date bdate ,String address, String mobileNumber, Connection conn) throws IOException, SQLException {
        Customer newUser= new Customer(newUserName, newPassword, email, bdate, 0,address,mobileNumber);
        if (User.userExists(newUserName,conn)==0) {
            newUser.addUser(conn);
            this.output.writeUTF("Valid");


        }
        else {

            this.output.writeUTF("Invalid");

        }
    }
    public Vector<String> loadAllUsers(Vector <Customer> customers) {
        /*
         * id,price,itemName,stock
         * .
         * .
         * */
        Vector<String> userDet = new Vector<String>();
//        String user_name, String password, String email, double current_balance, String address, String mobile_number
        for (int i = 0; i < customers.size(); i++) {
            String str="";
            str=String.valueOf(customers.get(i).getUsername())+','+
                    String.valueOf(customers.get(i).getPassword())+','+
                    customers.get(i).getEmail()+','+
                    customers.get(i).getCurrent_balance()+','+
                    String.valueOf(customers.get(i).getAddress())+','+customers.get(i).getMobile_number();
            userDet.add(str);

        }
        return userDet;

    }
    public Vector<String> loadAllItems(Vector <Item>items) {
        /*
         * id,price,itemName,stock
         * .
         * .
         * */

        Vector<String> itemDet = new Vector<String>();
//        int iid, double price, String item_name, String seller_name, int stock, String category
        for (int i = 0; i < items.size(); i++) {
            String str="";
            str=String.valueOf(items.get(i).getIid())+','+
                    String.valueOf(items.get(i).getPrice())+','+
                    items.get(i).getItem_name()+','+
                    items.get(i).getSeller_name()+','+
            String.valueOf(items.get(i).getStock())+','+items.get(i).getCategory();

            itemDet.add(str);

        }
        return itemDet;

    }
    /* for value of stockOrQty
    // set to "stock" to return (iid,price,name,stock)
    set to "Qty" to return (iid, price , name , itemQuantity)*/

    public Vector<String> loadItems(Vector <Item>items,String stockOrQty) {
        /*
        * id,price,itemName,stock
        * .
        * .
        * */

        Vector<String> itemDet = new Vector<String>();

        for (int i = 0; i < items.size(); i++) {
            String str="";
            str=String.valueOf(items.get(i).getIid())+','+
                    String.valueOf(items.get(i).getPrice())+','+
                    items.get(i).getItem_name()+',';
            if(stockOrQty.equals("stock"))
                    str+=String.valueOf(items.get(i).getStock());
            else
                str+= String.valueOf(items.get(i).getItemQuantity());
            itemDet.add(str);

        }
        return itemDet;

    }
    public static void addVectorToVector(Vector<String>first , Vector<String>second){
        for (int i = 0; i < second.size(); i++) {
            first.add(second.get(i));
        }
    }
    /*vector of order details
    format is:
    username
    first order date
    first order price
    startItem
    (id,price,itemName,stock)
    startItem
    (id,price,itemName,stock)
    .
    .
    endOrder
    second order date
    second order price
    startItem
    (id,price,itemName,stock)
    startItem
    (id,price,itemName,stock)
    endOrder
    .
    .
    end

    */
    public Vector<String> loadOrderDetails(Customer client) {
        ((Customer)client).loadOrders(conn);
        Vector <String> ordDet = new Vector<String>();
        for (int i = 0; i < client.getOrders().size(); i++) {
            Vector <String> itemDet= loadItems(client.getOrders().get(i).getItems(),"Qty");
            ordDet.add(client.getOrders().get(i).getODate().toString());
            ordDet.add(String.valueOf(client.getOrders().get(i).getTotalPrice()));
//            ordDet.add("startItem"); WE NO LONGER USE START ITEM LINE IN VIEW HISTORY
            addVectorToVector(ordDet,itemDet);
            ordDet.add("endOrder");
        }
        ordDet.add("end");
        return ordDet ;
    }
}
