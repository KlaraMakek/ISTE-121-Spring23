import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Client - Server orders
 * 
 * @author Klara Makek
 * @version 2023
 *          ISTE 121 LAB07 HW06
 */
public class MyClientForGUI extends Application implements EventHandler<ActionEvent> {

    // general SOCKET attributes
    public static final int SERVER_PORT = 54321;
    private Socket sSocket = null;

    // IO attributes
    private ObjectInputStream ois = null;
    private ObjectOutputStream oos = null;

    // to height of txtArea

    double height = 5;

    // to create the men bar
    MenuBar mb = new MenuBar();
    Menu menu = new Menu("Order");
    MenuItem connect = new MenuItem("Connect");
    MenuItem placeOrd = new MenuItem("Place Order");
    MenuItem orderCou = new MenuItem("Get Order Count");

    // text areas and labels
    Label nameLbl = new Label("Name:");
    TextArea taName = new TextArea();

    Label adressLab = new Label("Address:");
    Label steetLbl = new Label("Street:");
    TextArea taStreet = new TextArea();

    Label cityLbl = new Label("City:");
    TextArea taCity = new TextArea();

    // the states dropdown
    Label stateLbl = new Label("State:");
    ComboBox<String> taState = new ComboBox<>();

    Label zipLbl = new Label("ZipCode:");
    TextArea taZip = new TextArea();

    Label emailLbl = new Label("EMail:");
    TextArea taEmail = new TextArea();

    Label itemLbl = new Label("Item Number:");
    TextArea taItem = new TextArea();

    Label quantityLbl = new Label("Quantity:");
    TextArea taQuantity = new TextArea();

    /**
     * using grid pane for txt areas and labels
     * don't do that
     */
    GridPane gridPaneTop = new GridPane();
    GridPane gridPaneAdress = new GridPane();
    GridPane gridPaneBot = new GridPane();

    /**
     * the stage - GUI
     * it has 2 parts,
     * one is menu bar with all the buttons
     * the second is gui that is spreed in 3 parts to achieve this look
     * the name part, the address part and the item part
     */
    public void start(Stage _stage) {
        Stage stage = _stage;

        taName.setPrefWidth(250);
        taStreet.setPrefWidth(250);
        taCity.setPrefWidth(150);
        taZip.setPrefWidth(150);
        taEmail.setPrefWidth(250);
        taItem.setPrefWidth(100);
        taQuantity.setPrefWidth(100);

        // to add it in the menu bar
        menu.getItems().addAll(connect, placeOrd, orderCou);
        mb.getMenus().addAll(menu);

        // to make them work
        connect.setOnAction(this);
        placeOrd.setOnAction(this);
        orderCou.setOnAction(this);

        taState.getItems().addAll("AL", "AK", "AZ", "AR", "CA", "CO", "CT", "DE", "FL", "GA", "HI ", "ID", "IL", "IN",
                "IA",
                "KS", "KY", "LA", "ME", "MD", "MA", "MI", "MN", "MS", "MO", "MT", "NE", "NV", "NH", "NJ", "NM", "NY",
                "NC",
                "ND", "OH", "OK ", "OR", "PA", "RI", "SC", "SD", "TN", "TX", "UT", "VT", "VA", "WA", "WV", "WI", "WY");

        gridPaneTop.setPadding(new Insets(10, 0, 0, 10));
        gridPaneTop.setVgap(5);
        gridPaneTop.setHgap(5);

        gridPaneAdress.setPadding(new Insets(0, 0, 0, 35));
        gridPaneAdress.setVgap(5);
        gridPaneAdress.setHgap(5);

        gridPaneBot.setPadding(new Insets(0, 0, 10, 10));
        gridPaneBot.setVgap(5);
        gridPaneBot.setHgap(5);

        // ad the txtar and lbl to grid pane
        gridPaneTop.add(nameLbl, 0, 0);
        gridPaneTop.add(taName, 1, 0);
        gridPaneTop.add(adressLab, 0, 1);

        gridPaneAdress.add(steetLbl, 0, 3);
        gridPaneAdress.add(taStreet, 1, 3);
        gridPaneAdress.add(cityLbl, 2, 3);
        gridPaneAdress.add(taCity, 3, 3);
        gridPaneAdress.add(stateLbl, 0, 4);
        gridPaneAdress.add(taState, 1, 4);
        gridPaneAdress.add(zipLbl, 2, 4);
        gridPaneAdress.add(taZip, 3, 4);

        gridPaneBot.add(emailLbl, 0, 5);
        gridPaneBot.add(taEmail, 1, 5);
        gridPaneBot.add(itemLbl, 0, 6);
        gridPaneBot.add(taItem, 1, 6);
        gridPaneBot.add(quantityLbl, 0, 7);
        gridPaneBot.add(taQuantity, 1, 7);
        VBox root = new VBox();
        root.getChildren().addAll(mb, gridPaneTop, gridPaneAdress, gridPaneBot);

        Scene scene = new Scene(root);
        _stage.setScene(scene);
        _stage.setWidth(800);
        _stage.setHeight(500);
        _stage.show();

        // clint is terminate when closed window
        stage.setOnCloseRequest(
                new EventHandler<WindowEvent>() {
                    public void handle(WindowEvent evt) {
                        terminateClient();
                    }
                });

    }

    // it terminates the client
    protected void terminateClient() {
        if (oos != null) {
            try {
                oos.close();
                oos.flush();
                ois.close();
                sSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOE in terminateClient()");
            }
        }
    }

    /**
     * menu bar has 3 items, buttons
     * connect button connect client to server
     * and turns to disconnect button
     * 
     * place order button, places order
     * 
     * get order count, gets the orders
     */
    @Override
    public void handle(ActionEvent e) {

        Object source = e.getSource();
        String lbl = "";

        if (source instanceof MenuItem) {
            lbl = ((MenuItem) e.getSource()).getText();
        } else if (source instanceof Button) {
            lbl = ((Button) e.getSource()).getText();
        }

        switch (lbl) {
            case "Connect":
                doConnect();
                break;
            case "Disconnect":
                doDisconnect();
                break;
            case "Place order":
                doPlaceOrder();
                break;

            case "Get Order Count":
                doGetOrdCou();
                break;
        }

    }

    /**
     * When connect buttons is prest, text dialog box shows, when you enter the
     * sever IP("localhost") the server is connected
     */
    private void doConnect() {
        TextInputDialog txt = new TextInputDialog();
        txt.setContentText("Server IP:");
        txt.setHeaderText("Enter the servers IP Adress.");
        txt.showAndWait();

        String ipAddress = txt.getResult();

        try {
            sSocket = new Socket(ipAddress, SERVER_PORT);
            oos = new ObjectOutputStream(sSocket.getOutputStream());
            ois = new ObjectInputStream(sSocket.getInputStream());

            connect.setText("Disconnect");

        } catch (UnknownHostException e) {
            Alert alert = new Alert(AlertType.ERROR, "Connection failed...");
            alert.setHeaderText("Check IP and the server port");
            alert.showAndWait();
            // e.printStackTrace();
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR, "Server not connected.");
            alert.setHeaderText("Unexpected exception");
            alert.showAndWait();
            // e.printStackTrace();
        }
    }

    /**
     * the connect button become the disconnect button, when buttons is prest the
     * clients isn't connected anymore
     */
    private void doDisconnect() {
        if (oos != null) {
            try {
                oos.close();
                oos.flush();
                ois.close();
                sSocket.close();
                System.out.println("Disconnected.");
                connect.setText("Connect");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOE in doDisconnect()");
            }
        }
    }

    /**
     * when the client connects and fills all the 
     * requited fields the order is "sent"
     * if the order passed the alert box shows telling the clint is sent
     */
    private void doPlaceOrder() {

        String name = taName.getText();
        String street = taStreet.getText();
        String city = taCity.getText();
        String state = taState.getPromptText();
        int zip = Integer.parseInt(taZip.getText());
        String email = taEmail.getText();
        int item = Integer.parseInt(taItem.getText());
        int quantity = Integer.parseInt(taQuantity.getText());

        Order order = new Order(name, street, city, state, zip, email, item, quantity);
        try {
            oos.writeObject(order);
            System.out.println("Order sent");

            String feedBack = (String) ois.readObject();
            Alert alert2 = new Alert(AlertType.CONFIRMATION, feedBack);
            alert2.setHeaderText("Order good.");
            alert2.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOE in doPlaceOrder()");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    /**
     * after the orders are sent the client can see how many orders are there
     * they get alert box that says how many orders are  there
     */
    private void doGetOrdCou() {
        try {
            oos.writeObject("OrderCount");
            int orderCou = (int) ois.readObject();

            Alert alert = new Alert(AlertType.INFORMATION, "Order count is: " + orderCou);
            alert.setHeaderText("Order Count");
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOE in doGetOrdCou()");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("ClassCastException in doGetOrdCou()");
        }
    }

    public static void main(String[] args) {
        launch(args);

    }
}
