import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

/**
 * GUIStarter - Starter file for GUI examples
 * 
 * @author D. Patric
 * @version 2211
 */

/**
 * @author Klara Makek
 *         ISTE 121 801
 *         Practical 1
 * @version 24.2.2023.
 */
public class GUIStarter extends Application implements EventHandler<ActionEvent> {
   // Attributes are GUI components (buttons, text fields, etc.)
   // are declared here.
   private Stage stage; // The entire window, including title bar and borders
   private Scene scene; // Interior of window

   VBox vBox = new VBox();
   TextArea txt = new TextArea();
   TextArea bosplay = new TextArea();
   ArrayList<String> peopleOnTheTeam = new ArrayList<String>();

   // Choose a pane ... BorderPane used here
   private BorderPane root = new BorderPane();

   // Main just instantiates an instance of this GUI class
   public static void main(String[] args) {
      launch(args);
   }

   // Called automatically after launch sets up javaFX
   public void start(Stage _stage) throws Exception {
      stage = _stage; // save stage as an attribute
      stage.setTitle("FootballlO - Practical 1"); // set the text in the title bar

      scene = new Scene(root, 300, 300); // create scene of specified size
      // with given layout
      root.getChildren().addAll(txt, bosplay);
      stage.setScene(scene); // associate the scene with the stage
      stage.show(); // display the stage (window)
   }

   void doWork(String newFile) throws IOException {
      try {
         double shortest = 0;
         double lightes = 0;
         DataInputStream dis = new DataInputStream(new FileInputStream(newFile));
         try {
            while (dis.available() > 0) {
               String name = dis.readUTF();
               Double lbs = dis.readDouble();
               Double height = dis.readDouble();
               String posison = dis.readUTF();
               int number = dis.readInt();
               String colage = dis.readUTF();

               if (height > shortest) {
                  shortest = height;
               }
               if (lightes < shortest) {
                  lightes = lbs;
               }
               peopleOnTheTeam.add(name);
            }
            txt.appendText("There are " + peopleOnTheTeam.size() + " on the rooster.");
         } catch (IOException e) {
            e.printStackTrace();
         }

      } catch (IOException e) {
         e.printStackTrace();
      }

      DataOutputStream dos = new DataOutputStream(new FileOutputStream("FBRoster2023.bin"));
      dos.writeUTF("There are " + peopleOnTheTeam.size() + " on the rooster.");
   }

   public void handle(ActionEvent evt) {
      // Get the button that was clicked
      Button btn = (Button) evt.getSource();

      // Switch on its name
      switch (btn.getText()) {
         // case "ButtonName":
         // do what the button requires
         // break;
      }
   }
}
