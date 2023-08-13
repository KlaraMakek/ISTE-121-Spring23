import java.net.*;
import java.io.*;
import java.util.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
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
public class MyMThreadServerForRecords extends Application implements EventHandler<ActionEvent> {

   // Socket stuff
   private ObjectInputStream ois = null;
   private ObjectOutputStream oos = null;
   private ServerSocket sSocket = null;
   final String FILE_NAME_SCV = "orders.scv";
   final String FILE_NAME_OBJ = "orders.obj ";
   boolean conti = true;
   int orderCou = 0;
   public static final int SERVER_PORT = 54321;
   int clientCounter = 1;
   ArrayList<Order> arrayRecord = new ArrayList<Order>();
   Stage stage;
   Scene scene;
   // private Scene scene;

   VBox root = new VBox();
   MenuBar mbs = new MenuBar();
   Menu menuS = new Menu("Server");
   MenuItem start = new MenuItem("Start");
   MenuItem convertOrders = new MenuItem("Convert Orders");

   Label serverIP = new Label("Server IP:");
   TextArea serverIPArea = new TextArea();

   Label numOrder = new Label("Number of orders:");
   Label curNumOrd = new Label("");

   FlowPane flowPaneIP = new FlowPane(8, 8);
   FlowPane flowpaneNum = new FlowPane(8, 8);

   /**
    * the stage - GUI has menu bar with 2 "buttons", server address ip and
    * conversion counter
    */
   @Override
   public void start(Stage primaryStage) throws Exception {

      stage = primaryStage;

      stage.setTitle("Server");

      menuS.getItems().addAll(start, convertOrders);
      mbs.getMenus().addAll(menuS);

      start.setOnAction(this);
      convertOrders.setOnAction(this);

      flowPaneIP.getChildren().addAll(serverIP, serverIPArea);
      flowpaneNum.getChildren().addAll(numOrder, curNumOrd);

      serverIPArea.setPrefHeight(15);
      serverIPArea.setPrefWidth(150);
      curNumOrd.setPrefHeight(15);
      curNumOrd.setPrefWidth(150);

      root.getChildren().addAll(mbs, flowPaneIP, flowpaneNum);
      scene = new Scene(root);
      primaryStage.setScene(scene);
      primaryStage.setWidth(550);
      primaryStage.setHeight(200);
      primaryStage.show();
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         public void handle(WindowEvent e) {
            conti = false;
            try {
               if (sSocket != null) {
                  sSocket.close();
               }
            } catch (IOException event) {
               event.printStackTrace();
               System.out.println("IOE in stage.setOnCloseRequest");
            }
         }
      });

   }

   class ServerThread extends Thread {
      public void run() {

         try {
            sSocket = new ServerSocket(SERVER_PORT);

            File file = new File("FILE_NAME_OBJ");
            clientCounter = 0;
            arrayRecord.clear();

            if (file.exists() && file.isFile()) {
               ObjectInputStream fis = new ObjectInputStream(new FileInputStream(file));
               while (true) {
                  try {
                     Order order = (Order) fis.readObject();
                     arrayRecord.add(order);
                     orderCou++;
                  } catch (ClassNotFoundException e) {
                     e.printStackTrace();
                  } catch (EOFException eof) {
                     break;
                  }
               }
               curNumOrd.setText(Integer.toString(orderCou));
            }

         } catch (IOException ioe) {
            System.out.println("IO Exception (1): " + ioe + "\n");
            return;
         }

         while (conti) {
            Socket cSocket = null;
            try {
               System.out.println("Waiting client to connect...");
               // Wait for a connection
               cSocket = sSocket.accept();
            } catch (IOException ioe) {
               System.out.println("IO Exception (2): " + ioe + "\n");
               return;
            }
            // Start client thread
            ClientThread ct = new ClientThread(cSocket, "Client" + clientCounter);
            clientCounter++;
            ct.start();
         }

      }

      public void stopServer() {
         conti = false;
         try {
            sSocket.close();
         } catch (IOException e) {
            e.printStackTrace();
         }
      }
   }// end of ServerThread

   // client thread
   class ClientThread extends Thread {
      private Socket cSocket = null;
      private String cName = "";

      public ClientThread(Socket _cSocket, String _name) {
         this.cSocket = _cSocket;
         this.cName = _name;
      }

      public void run() {
         System.out.println(this.cName + " connected");
         // IO attributes
         ObjectInputStream ois = null;
         ObjectOutputStream oos = null;
         try {
            oos = new ObjectOutputStream(cSocket.getOutputStream());
            ois = new ObjectInputStream(cSocket.getInputStream());

            while (!cSocket.isClosed()) {
               Object object = ois.readObject();
               if (object instanceof Order) {
                  Order order = (Order) object;
                  arrayRecord.add(order);
                  System.out.println("Added " + object.toString());
               } else if (object instanceof String) {
                  String thing = (String) object;

                  switch (thing.toUpperCase()) {
                     case "DISCONNECTING":
                        sSocket.close();
                        System.out.println(this.cName + "disconnected.");
                        break;
                     case "ORDERCOUNT":
                        oos.writeObject(orderCou);
                        break;
                  }

               }
            }
         } catch (Exception e) {
            System.out.println("Exception opening streams: " + e);
            System.out.println(this.cName + " disconnected");
         }

         try {
            oos.close();
            ois.close();
            this.cSocket.close();
         } catch (IOException ie) {
         }

      }
   }
   
   @Override
   public void handle(ActionEvent event) {
      Object source = event.getSource();
      String lbl = "";
      ServerThread server = new ServerThread();

      if (source instanceof MenuItem) {
         lbl = ((MenuItem) event.getSource()).getText();
      } else if (source instanceof Button) {
         lbl = ((Button) event.getSource()).getText();
      }

      switch (lbl) {
         case "Start":
            conti = true;
            server.start();
            start.setText("Stop");
            convertOrders.setDisable(false);

            break;
         case "Stop":
            server.stopServer();
            start.setText("Start");
            convertOrders.setDisable(true);
            break;
         case "Convert Orders":
            doConvertOrders();
            break;
      }
   }

   private void doConvertOrders() {
      try {
         File file = new File(FILE_NAME_SCV);
         file.createNewFile();
         orderCou = 0;

         ObjectOutputStream fos = new ObjectOutputStream(new FileOutputStream(file, false));
         for (int i = 0; i < arrayRecord.size(); i++) {
            fos.writeObject(arrayRecord.get(i));
            orderCou++;
         }
         curNumOrd.setText(Integer.toString(orderCou));
      } catch (Exception e) {
         e.printStackTrace();
         System.out.println("Ex in doConvertOrders()");
      }
   }

   protected void terminateClient() {
      if (oos != null) {
         try {
            oos.close();
            ois.close();
            sSocket.close();
         } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOE in terminateClient()");
         }
      }
   }

   public static void main(String[] args) {
      launch(args);
      new MyMThreadServerForRecords();
   }

} // end class
