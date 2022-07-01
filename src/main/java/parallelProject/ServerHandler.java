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
        // TODO msh mot2kd hena hcall el output.close walla la
        output.close();

    }
// TODO aghyar parse akhleha void
    public String parse(DataInputStream input, Connection conn, User client) throws IOException, SQLException {

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

            case "login":
                String type = input.readUTF();
                String username = input.readUTF();
                password = input.readUTF();



                client = login(type, username, password, email, bdate,address, mobile_number,conn );

                        // TODO send el cart
                    // TODO asend el current balance ll GUI
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

//                String newUserName, String newPassword, String email,Date bdate ,String address, String mobileNumber, Connection conn
                signUp(newUserName,newPassword,email,bdate,address,mobile_number,conn);
                // TODO check en el password w el confirm password text boxes contain same password

                break;


            case "viewHistory":
                client = Customer.getUserInfo(input.readUTF(),conn);
                ((Customer)client).loadOrders(conn);
                Vector<String>orderHistory=new Vector<String>();
                orderHistory=loadOrderDetails(((Customer) client));
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
                // TODO display message "Successful balance recharge"

                break;

            case "searchByName":

                //id, price, itemname, stock

                /*Input Format
                 * searchByName
                 * itemName
                 * */
                // TODO azwd fe el GUI eno ycall searchByName bas lw feh 7aga maktoba gowa el Search Box
                String itemName = input.readUTF();
                if(Item.itemExists(itemName, conn)!=0)

                    items = Item.search_by_name(itemName, conn);

                itemsFound=loadItems(items,"stock");
                for (int i = 0; i < itemsFound.size(); i++) {
                    this.output.writeUTF(itemsFound.get(i));
                }
                // TODO return item details to GUI
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

                // TODO return item details to GUI
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
                //ana khalet el parse non-static hena
                break;

            case "loadCart":
                /*Input on the following format
                * loadCart
                * username
                * */

                username = input.readUTF();
                order = Customer.loadCart(username, conn);
                items = order.getItems();

//                this.output.writeUTF(String.valueOf(order.getOID()));
                this.output.writeUTF(String.valueOf(order.getODate()));
                this.output.writeUTF(String.valueOf(order.getTotalPrice()));

                if(items.size()!=0){
                    // TODO msh mot2kd mn hean hcall 3la Qty walla stock
                    itemsFound = loadItems(items,"Qty");
                    for (int i = 0; i < itemsFound.size(); i++)
                        this.output.writeUTF(itemsFound.get(i));
                }
                this.output.writeUTF("end");
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


            case "confirmCart":
                /* Input format
                 * confirmCart
                 * client ID
                 * orderID
                 * */
                // TODO send "Item Not Found"
                // TODO pass the object contains the order in the make order funcn

                // TODO function returns order object given its id
                // leh bpass el order lama kdh kdh hwa el unconfirmed ?


                client = Customer.getUserInfo(input.readUTF(), conn);
                // function treturn object mn el order given its order id
                int valid = ((Customer)client).makeOrder(order,conn);
                if (valid!=-1)
                    this.output.writeUTF("Valid Transaction");
                else
                    this.output.writeUTF("Invalid Item Stock");
                // TODO fe el gui msm7losh ykhtar quantity aktar mn el stock ely mwgood

                /*
                * Ouptut on the following format
                * Valid Transaction if the transaction is successful
                * Invalid Transaction if the transaction is unsuccessful
                * */
                break;
        }

        return "";
    }

    public void sendCart(String username) throws IOException, SQLException {

        Order order;
        Vector<Item> items;

        order = Customer.loadCart(username, conn);
        items = order.getItems();

//                this.output.writeUTF(String.valueOf(order.getOID()));
        this.output.writeUTF(String.valueOf(order.getODate()));
        this.output.writeUTF(String.valueOf(order.getTotalPrice()));
        Vector<String> itemsFound;
        if(items.size()!=0){
            // TODO msh mot2kd mn hean hcall 3la Qty walla stock
            itemsFound = loadItems(items,"Qty");
            for (int i = 0; i < itemsFound.size(); i++)
                this.output.writeUTF(itemsFound.get(i));
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
        // TODO case el invalid password btreturn invalid username msh password
        if(User.userExists(username, c)!=0) {
            if (type.equals("Admin"))
                client = (Admin)Admin.getUserInfo(username, c);
            else if (type.equals("Customer"))
                client = (Customer)Customer.getUserInfo(username, c);
        }

        if (client!=null) {
            String str = User.getPassword(username, conn);
            if (!str.equals(password)) {
                //TODO Print error message "Invalid password"

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
                }
                // username,password,email,bdate,current_balance,address, mobile_number

                this.output.writeUTF(strToBePassed);
                this.output.writeUTF("startOrder");
                sendCart(username);

                // TODO transfer user to new homescreen w lw el user Customer call getBalance "Valid"
                return client;
            }
        }
        else{
            //TODO print Admin or Customer doesn't exist error message "Invalid Username"

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

            //  TODO check the password and confirm password are the same
        }
    }
    // for value of stockOrQty
    // set to "stock" to return (iid,price,name,stock)
    // set to "Qty" to return (iid, price , name , itemQuantity)
    public Vector<String> loadItems(Vector <Item>items,String stockOrQty) {
        /*startItem
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
            // TODO shelna startItem mn hena 3ayzenha ta7t fe loadOrderDetails
//            itemDet.add("startItem");
            itemDet.add(str);

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
{

        ((Customer)client).loadOrders(conn);
        Vector <String> ordDet = new Vector<String>();

        for (int i = 0; i < client.getOrders().size(); i++) {
            Vector <String> itemDet= loadItems(client.getOrders().get(i).getItems(),"Qty");
            ordDet.add(client.getOrders().get(i).getODate().toString());
            ordDet.add(String.valueOf(client.getOrders().get(i).getTotalPrice()));
            ordDet.add("startItem");
            addVectorToVector(ordDet,itemDet);
            ordDet.add("endOrder");

        }
        ordDet.add("end");
        return ordDet ;
    }
}
