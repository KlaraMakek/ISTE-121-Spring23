import java.io.*;
import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Name: Klara Makek
 * Course/Section: ISTE-121-<section>
 * Practical #2
 * Date: 5.2.2023.
 * 
 * @ASSESSME.INTENSITY:HIGH
 */

public class CharServerStarter extends Application implements EventHandler<ActionEvent> {
   // Window Attributes
   private Stage stage;
   private Scene scene;
   private VBox root = null;

   // GUI components
   private TextArea taList = new TextArea();
   private Button btnClear = new Button("Clear");

   // socket
   private static final int SERVER_PORT = 1234;

   ArrayList<String> names = new ArrayList<>();


   // outputStream array 
   private ArrayList<ObjectOutputStream> outputStream= new ArrayList<>();


   int clientIDCounter = 0;

   /** Main program */
   public static void main(String[] args) {
      launch(args);
   }

   /** start the server */
   public void start(Stage _stage) {
      stage = _stage;
      stage.setTitle("Registration Server (YOUR_NAME)");
      final int WIDTH = 450;
      final int HEIGHT = 400;
      final int X = 550;
      final int Y = 100;
   
      stage.setX(X);
      stage.setY(Y);
      stage.setOnCloseRequest(
         new EventHandler<WindowEvent>() {
            public void handle(WindowEvent evt) {
               System.exit(0);
            }
         });
   
      // Set up root
      root = new VBox();
   
      // Put clear button in North
      HBox hbNorth = new HBox();
      hbNorth.setAlignment(Pos.CENTER);
      hbNorth.getChildren().add(btnClear);
   
      // Set up rootis
      root.getChildren().addAll(hbNorth, taList);
      for (Node n : root.getChildren()) {
         VBox.setMargin(n, new Insets(10));
      }
   
      // Set the scene and show the stage
      scene = new Scene(root, WIDTH, HEIGHT);
      stage.setScene(scene);
      stage.show();
   
      // Adjust size of TextArea
      taList.setPrefHeight(HEIGHT - hbNorth.getPrefHeight());
   
      // do Server Stuff
      doServerStuff();
   }

   /** Server action */
   private void doServerStuff() {
      ServerThread st = new ServerThread();
      st.start();
   }

   // ServerThread
   class ServerThread extends Thread {
      @Override
      public void run() {
         try {
            System.out.println("Openning SOCKET PORT");
            ServerSocket sSocket = new ServerSocket(SERVER_PORT);
         
            while (true) {
               System.out.println("Waiting client to connect...");
               Socket cSocket = sSocket.accept();
            
               ClientThread cT = new ClientThread(cSocket);
               cT.start();
            }
         
         } catch (IOException e) {
            showAlert(AlertType.ERROR, e.getMessage());
         }
      }
   }

   // ClientThread
   class ClientThread extends Thread {
      private Socket cSocket;
      private ObjectOutputStream oos = null;
      private ObjectInputStream ois = null;
   
      public ClientThread(Socket cSocket) {
         this.cSocket = cSocket;
         this.outputStream = outputStream;
      }
   
      @Override
      public void run() {
      
         try {
         
            this.ois = new ObjectInputStream(this.cSocket.getInputStream());
            this.oos = new ObjectOutputStream(this.cSocket.getOutputStream());
         
            //Add oos to the array list
            outputStream.add(oos);

            while (true) {
               Object obj = ois.readObject();
               if (obj instanceof String) {
               
                  // TODO **************************START WRITING HERE***********
                  // Receive the object and cast it to String
               Object receiveObject = ois.receiveObject();
               String message = (String) receiveObject; 
                  // ****Write here the code
               
                  // Split the string message by using the delimiter sign "@". Save result in the
                  // arrayOfMessage array

                  // ****Write here the code
                  String[] arrayOfMassages = message.split("@");
                  /*************************
                   * Check if the arrayOfMessage size is 2, if yes enter the switch/case
                   * structure.
                   * Two messages can be received: "REGISTER@NAME", and "CHAT@NAME:MESSAGE";
                   * Create two cases, "REGISTER" and "CHAT".
                   * If "REGISTER" is received, store the NAME into arrayList (names), and return
                   * "REGISTERED".
                   * If "CHAT" is received, return the content (NAME:MESSAGE) to all clients back
                   */
               
                  // ****Write here the code
               
                  if(arrayOfMessage.lenght==2){
                     switch(arrayOfMessage[0]){
                        case "REGISTER":
                        String name =arrayOfMessage[1];
                        name.add(name);
                        oos.writeObject("REGISTERED");
                        break;

                        case "CHAT";
                        String content = arrayOfMessage[1];
                        for(ObjectOutputStream os : outputStream){
                           os.writeObject(content);
                        }
                        break;
                        default;
                        break;
                     }
                  }else{
                     System.out.println("Error");

                  }
               }
            }
         }catch (ClassNotFoundException e) {
            showAlert(AlertType.ERROR, e.printStackTrace);
            e.printStackTrace();
         } catch (EOFException eof) {
            showAlert(AlertType.WARNING, "Connection lost");
         } catch (IOException ioe) {
            showAlert(AlertType.ERROR, ioe.getMessage());
         }finally{
            try{
               ois.close();
               oos.close();
               cSocket.close();
               outputStream.remove(oos);
            }catch (IOException ioe) {
            showAlert(AlertType.ERROR, ioe.getMessage());
         }
      }
         System.out.println("Client disconnected...");
      }
   
   }

   /** Button handler */
   public void handle(ActionEvent ae) {
   }

   public void showAlert(AlertType type, String message) {
      Platform.runLater(
         new Runnable() {
            public void run() {
               Alert alert = new Alert(type, message);
               alert.showAndWait();
            }
         });
   }
}