import javafx.application.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

//slider
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

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

public class ChatClientStarter extends Application implements EventHandler<ActionEvent> {
   // Window objects
   Stage stage = null;
   Scene scene = null;
   VBox root = null;

   // GUI Components
   private TextField tfServer = new TextField("localhost");
   private TextField tfName = new TextField();
   private Button btnConnect = new Button("Connect");
   private TextArea taForMessages = new TextArea();
   private TextField tfMessageToSend = new TextField("Message to send");

   // socket
   private Socket socket = null;
   private ObjectOutputStream oos = null;
   private ObjectInputStream ois = null;
   private static final int SERVER_PORT = 1234;



   private int currentID = -1;

   /**
    * Main program ...
    * 
    * @args - command line arguments (ignored)
    */
   public static void main(String[] args) {
      launch(args);
   }

   /** constructor */
   public void start(Stage _stage) {
      // Window set up
      stage = _stage;
      stage.setTitle("Registration Client (YOUR_NAME)");
      final int WIDTH = 500;
      final int HEIGHT = 400;
      final int X = 50;
      final int Y = 100;
      stage.setX(X);
      stage.setY(Y);
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         public void handle(WindowEvent evt) {
            System.exit(0);
         }
      });

      // Draw the GUI
      root = new VBox();

      HBox hbTop = new HBox(10);
      hbTop.getChildren().addAll(new Label("Server"), tfServer, new Label("Name"), tfName, btnConnect);

      root.getChildren().addAll(hbTop, taForMessages, tfMessageToSend);
      for (Node n : root.getChildren()) {
         VBox.setMargin(n, new Insets(5));
      }

      btnConnect.setOnAction(this);

      // Set the scene and show the stage
      scene = new Scene(root, WIDTH, HEIGHT);
      stage.setScene(scene);
      stage.show();

      // Adjust sizes
      taForMessages.setPrefHeight(HEIGHT - hbTop.getPrefHeight() - tfMessageToSend.getPrefHeight());
      tfServer.setPrefColumnCount(12);
      tfName.setPrefColumnCount(7);

      // ENTER PRESSED // import javafx.scene.input.KeyEvent;
      tfMessageToSend.setOnKeyPressed(new EventHandler<KeyEvent>() {
         @Override
         public void handle(KeyEvent t) {
            if (t.getCode() == KeyCode.ENTER) {
               sendMessage();
            }
         }
      });
   }

   /** Button handler */
   public void handle(ActionEvent ae) {
      Button btn = (Button) ae.getSource();

      // **********TODO************** */
      // Create two switch cases for "Connect" and "Disconnect"

      // ****Write here the code
         switch(btn.getText()){

            case "Connect":
             doConnect();
            break;

            case "Disconnect":
            doDisconnect();
            break;

            default:
            break;
}

   }

   private void doDisconnect() {
      System.out.println("Disconnect the client");
      try {
         oos.close();
         ois.close();
         socket.close();
         btnConnect.setText("Connect");
         tfName.setDisable(false);
         tfServer.setDisable(false);
      } catch (IOException e) {
         showAlert(AlertType.ERROR, "Exception");
      }
   }

   private void sendMessage() {

      // TODO
      // SEND Message as: "CHAR@NAME:MEssage", where NAME is the client's name, and
      // Message is the message content
      // After sent, clear the message's text field

      // *************************************Write here the code
      String message = tfMessageToSend.getText().trim();
      tfMessageToSend.clear();

         try{
            oos.writeObject("CHAR@" + tfName.getText() +  ":" + message);
            oos.flush();

         }catch(IOException e){
            showAlert(AlertType.ERROR, "Sending message error." + e.getMessage());
         }

      }  

   class ClientThread extends Thread {
      @Override
      public void run() {

         while (true) {
            try {

               // TODO
               // Receive the object, convert in to String and append it to the text area

               // *************************************Write here the code
               Object obj = ois.readObject();

               if(obj instanceof String){
                  String message = (String) obj;
                  Platform.runLater(()->{
                     taForMessages.appendText(message + "\n");
                  });
               }
            } catch (ClassNotFoundException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            } catch (EOFException eof) {
               showAlert(AlertType.WARNING, "Connection lost");
            } catch (IOException ioe) {
               showAlert(AlertType.ERROR, ioe.getMessage());
            }
         }
      }
   }

   private void doConnect() {
      try {
         this.socket = new Socket(tfServer.getText(), SERVER_PORT);

         this.oos = new ObjectOutputStream(this.socket.getOutputStream());
         this.ois = new ObjectInputStream(this.socket.getInputStream());

         // *******************TODO******************************************** */
         // Send string "REGISTER@NAME", where NAME is the client's name

         // *************************************Write here the code
         String name =tfName.getText().trim();
         oos.writeObject("REGISTER@" + name);
         oos.flush();

         // wait from feedback from the server

         // *************************************Write here the code
            String back = (String) ois.readObject();
         // If the feedback is "REGISTERED"
         // * set the btnConnect title to "Disconnect"
         // * disable the tfName text field
         // * disable the tfServer text field
         // * run the ClientThread

         // *************************************Write here the code

         if(back.equals("REGISTERD")){

            btnConnect .setText("Disconnect");

            tfName.setDisable(true);

            tfServer.setDisable(true);

            new ClientThread().start();
         }else{
            showAlert(AlertType.ERROR, back);
         }
      } catch (UnknownHostException e) {
         showAlert(AlertType.ERROR, e.getMessage());
      } catch (IOException e) {
         showAlert(AlertType.ERROR, e.getMessage());
      }
   }

   public void showAlert(AlertType type, String message) {
      Alert alert = new Alert(type, message);
      alert.showAndWait();
   }

}