import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.control.cell.*; // needed to do cell formatting
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.collections.*; // needed for FXCollections

import java.io.*;
import java.util.*;

public class SimpleTableStarter extends Application {

   // Window Attributes
   private Stage stage;
   private Scene scene;
   private VBox root = new VBox(8);

   // GUI components
   private TableView<Customer> tblBank = new TableView<Customer>(); // The Table
   private ObservableList<Customer> data = FXCollections.observableArrayList();

   // The data file
   public static final String BANK_FILE = "BankData.dat";

   public static void main(String[] args) {
      launch(args);
   }

   public void start(Stage _stage) {
      // Setup the window
      stage = _stage;
      stage.setTitle("Simple Table");
      // this.setSize(400, 700);
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         public void handle(WindowEvent evt) {
            System.exit(0);
         }
      });

      // Set up table columns
      TableColumn<Customer, String> tcolName = new TableColumn<Customer, String>("Name");
      TableColumn<Customer, Integer> tcolId = new TableColumn<Customer, Integer>("ID");
      tcolId.setStyle("-fx-alignment: CENTER-RIGHT;");
      TableColumn<Customer, Double> tcolBalance = new TableColumn<Customer, Double>("Balance");
      tcolBalance.setStyle("-fx-alignment: CENTER-RIGHT;");

      // Add columns to the table
      tblBank.setPrefWidth(275);
      tblBank.setPrefHeight(700);
      tblBank.getColumns().add(tcolName);
      tblBank.getColumns().add(tcolId);
      tblBank.getColumns().add(tcolBalance);

      // Add to the root
      root.getChildren().add(tblBank);

      readData(); // Read the data (Vector of Vectors)
      tcolName.setCellValueFactory(new PropertyValueFactory<Customer, String>("name"));
      tcolId.setCellValueFactory(new PropertyValueFactory<Customer, Integer>("id"));
      tcolBalance.setCellValueFactory(new PropertyValueFactory<Customer, Double>("balance"));

      tcolBalance.setCellFactory(tc -> new TableCell<Customer, Double>() {
         @Override
         protected void updateItem(Double balance, boolean empty) {
            super.updateItem(balance, empty);
            if (empty) {
               setText(null);
            } else {
               setText(String.format("%10.2f", balance));
            }
         }
      });
      tblBank.setItems(data);

      // Set scene and show window
      scene = new Scene(root, 275, 700);
      stage.setScene(scene);
      stage.show();
   }

   /**
    * data is reed from the file
    * then added to the constructor for it to show
    */
   private void readData() {
      try {
         DataInputStream dis = new DataInputStream(new FileInputStream(BANK_FILE));
         try {
            while (dis.available() > 0) {
               try {
                  // reed the data
                  String name = dis.readUTF();
                  int id = dis.readInt();
                  double balance = dis.readDouble();

                  // new customer object
                  // adds to the list
                  Customer customer = new Customer(name, id, balance);
                  data.add(customer);
               } catch (IOException e) {
                  e.printStackTrace();
                  return;
               }
            }
         } catch (IOException e) {
            e.printStackTrace();
         }
      } catch (FileNotFoundException fnf) {
         fnf.printStackTrace();
         return;
      }
   }

   
   public class Customer {

      String name;
      int id;
      double balance;

      public Customer(String name, int id, double balance) {
         this.name = name;
         this.id = id;
         this.balance = balance;
      }

      public String getName() {
         return name;
      }

      public void setName(String name) {
         this.name = name;
      }

      public int getId() {
         return id;
      }

      public void setId(int id) {
         this.id = id;
      }

      public double getBalance() {
         return balance;
      }

      public void setBalance(double balance) {
         this.balance = balance;
      }

      @Override
      public String toString() {
         return "Customer [name=" + name + ", id=" + id + ", balance=" + balance + "]";
      }
   }

}
