import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.scene.shape.Line;

import java.io.*;
import java.util.*;

/**
 * RosterSystemStarter - Class to keep track of a team roster
 * Starter file for Practical 3
 * @author  D. Patric
 * @version 2215
 * @ASSESSME.INTENSITY:HIGH
 */

/**
 * Name: Klara Makek
 * ISTE 121 - 800
 * Practical 3
 * Date: 26.4.2023.
 * 
 * @ASSESME.INTENSTY: HIGH
 */


 public class RosterSystemStarter extends Application implements EventHandler<ActionEvent> {
   // Main window items
   private Stage stage;
   private Scene scene;
   private VBox root = new VBox(8);

   // GUI components
   private TextArea taRoster = new TextArea();
   private TextField tfRosterCount = new TextField();
   private TextField tfSearchFor = new TextField();
   private Button btnSearch = new Button("Search");
   private TextField tfUniform = new TextField();
   private TextField tfName = new TextField();
   private TextField tfPos = new TextField();
   private TextField tfHeight = new TextField();
   private TextField tfWeight = new TextField();
   private TextField tfExp = new TextField();
   private TextField tfCollege = new TextField();
   private Button btnUpdate = new Button("Update");
   private Button btnDelete = new Button("Delete");
   private Button btnSaveExit = new Button("Save/Exit");

   // Data Structures
   TreeMap<String, Roster> map = new TreeMap<>();

   // Constants
   public final static String FILENAME = "FBRoster2021BIN.dat";

   // Misc Attributes
   private int counter = 0;

   /** main */
   public static void main(String[] args) {
      launch(args);
   }

   /** constructor */
   public void start(Stage _stage) {
      stage = _stage;
      stage.setTitle("Roster System");

      // row1 - TextArea
      Font newFont = Font.font("MONOSPACED");
      taRoster.setFont(newFont);
      root.getChildren().add(taRoster);

      // row2 - roster count
      FlowPane fpRow2 = new FlowPane(8, 8);
      tfRosterCount.setPrefColumnCount(3);
      tfRosterCount.setEditable(false);
      fpRow2.getChildren().addAll(new Label("          Players: "), tfRosterCount);
      root.getChildren().add(fpRow2);

      // row3 - line between roster count and search
      Line lineRow3 = new Line(0, 0, 650, 0);
      root.getChildren().add(lineRow3);

      // row4 - search
      FlowPane fpRow4 = new FlowPane(8, 8);
      tfSearchFor.setPrefColumnCount(3);
      fpRow4.getChildren().addAll(new Label("     Uniform #: "), tfSearchFor, btnSearch);
      root.getChildren().add(fpRow4);

      // row5 - line between search and data
      Line lineRow5 = new Line(0, 0, 650, 0);
      root.getChildren().add(lineRow5);

      // row6 - current record 1 of 3
      FlowPane fpRow6 = new FlowPane(8, 8);
      tfUniform.setPrefColumnCount(3);
      tfName.setPrefColumnCount(20);
      tfPos.setPrefColumnCount(4);
      fpRow6.getChildren().addAll(new Label("     Uniform #: "), tfUniform,
            new Label("Name: "), tfName, new Label("Position: "), tfPos);
      root.getChildren().add(fpRow6);

      // row7 - current record 2 of 3
      FlowPane fpRow7 = new FlowPane(8, 8);
      tfHeight.setPrefColumnCount(3);
      tfWeight.setPrefColumnCount(7);
      fpRow7.getChildren().addAll(new Label("            Height: "), tfHeight,
            new Label("Weight: "), tfWeight);
      root.getChildren().add(fpRow7);

      // row8 - current record 3 of 3
      FlowPane fpRow8 = new FlowPane(8, 8);
      tfExp.setPrefColumnCount(3);
      tfCollege.setPrefColumnCount(20);
      fpRow8.getChildren().addAll(new Label("   Experience: "), tfExp,
            new Label("College: "), tfCollege);
      root.getChildren().add(fpRow8);

      // row9 - line between data and controls
      Line lineRow9 = new Line(0, 0, 650, 0);
      root.getChildren().add(lineRow9);

      // row10
      FlowPane fpRow10 = new FlowPane(8, 8);
      fpRow10.setAlignment(Pos.CENTER);
      fpRow10.getChildren().addAll(btnUpdate, btnDelete, btnSaveExit);
      root.getChildren().add(fpRow10);

      // button handlers
      btnSearch.setOnAction(this);
      btnUpdate.setOnAction(this);
      btnDelete.setOnAction(this);
      btnSaveExit.setOnAction(this);

      scene = new Scene(root, 600, 420);
      stage.setX(100);
      stage.setY(100);
      stage.setScene(scene);
      stage.show();

      doProgram();
   }

   /** Read in the data file */
   private void doProgram() {
      // Read in the data file and display it
      readData();
      // dispData();
      dispData();
   }

   /** readData - read in the data from the file */
   private void readData() {
      DataInputStream dis = null;
      try {
         dis = new DataInputStream(new FileInputStream(new File(FILENAME)));
         while (dis.available() > 0) {
            int unNum = dis.readInt();
            String playerName = dis.readUTF();
            String position = dis.readUTF();
            int height = dis.readInt();
            double weight = dis.readDouble();
            String exp = dis.readUTF();
            String college = dis.readUTF();
            Roster rooster = new Roster(unNum, playerName, position, height, weight, exp, college);
            map.put("" + unNum, rooster);
            counter++;

         }

      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         try {
            dis.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }

   }

   /** dispData */
   private void dispData() {
      taRoster.clear();
      for (String key : map.keySet()) {
         String roster = map.get(key).toString();
         taRoster.appendText(roster + "\n");
      }
      tfRosterCount.setText("" + counter);
   }
/* 
   private void dispData2() {
      ArrayList<Roster> list = new ArrayList<>(map.values());
      Collections.sort(list);
      taRoster.clear();
      for (Roster rooster : list) {
         String roster = rooster.toString();
         taRoster.appendText(roster + "\n");
      }
      tfRosterCount.setText("" + counter);
   }
*/
   /**
    * handle
    * button dispatcher
    */
   public void handle(ActionEvent ae) {
      // get the button that was clicked
      Button btn = (Button) ae.getSource();

      // switch on button label
      switch (btn.getText()) {
         case "Search":
            doSearch();
            break;
         case "Update":
            doUpdate();
            break;
         case "Delete":
            doDelete();
            break;
         case "Save/Exit":
            doSaveExit();
            break;
      }
   }

   /** doSearch */
   private void doSearch() {
      String targetKey = tfSearchFor.getText();
      if (map.keySet().contains(targetKey)) {
         Roster rooster = map.get(targetKey);
         tfUniform.setText("" + rooster.getUniformNumber());
         tfName.setText("" + rooster.getPlayerName());
         tfPos.setText("" + rooster.getPosition());
         tfHeight.setText("" + rooster.getHeightInInches());
         tfWeight.setText("" + rooster.getWeightInPounds());
         tfExp.setText("" + rooster.getExperience());
         tfCollege.setText("" + rooster.getCollege());

      } else
         alert(AlertType.WARNING, "Person entry not found", "Roster Search");
   }

   /** doUpdate */
   private void doUpdate() {
      String word = tfSearchFor.getText();
      if (map.keySet().contains(word)) {
         map.remove(word);
         Roster rooster = new Roster(Integer.parseInt(tfUniform.getText()), tfName.getText(), tfPos.getText(),
               Integer.parseInt(tfHeight.getText()), Double.parseDouble(tfWeight.getText()), tfExp.getText(),
               tfCollege.getText());
         map.put(tfUniform.getText(), rooster);
      } else
         alert(AlertType.WARNING, "Person to update not found", "Roster Update");
   //   dispData2();
   }

   /** doDelete */
   private void doDelete() {
      String word = tfSearchFor.getText();
      if (map.keySet().contains(word)) {
         map.remove(word);
         counter--;
      } else
         alert(AlertType.WARNING, "Person to be deleted not found", "Roster Delete");
      dispData();
      tfRosterCount.setText("" + counter);

   }

   /** do SaveExit ... button and window close */
   private void doSaveExit() {
      DataOutputStream dos = null;

      try {
         dos = new DataOutputStream(new FileOutputStream(new File(FILENAME)));
         for (String key : map.keySet()) {
            Roster ros = map.get(key);
            dos.writeInt(ros.getUniformNumber());
            dos.writeUTF(ros.getPlayerName());
            dos.writeUTF(ros.getPosition());
            dos.writeInt(ros.getHeightInInches());
            dos.writeDouble(ros.getWeightInPounds());
            dos.writeUTF(ros.getExperience());
            dos.writeUTF(ros.getCollege());
         }
         stage.close();
      } catch (FileNotFoundException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } catch (IOException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      } finally {
         try {
            dos.close();
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }

   /** alert - utility to provide Alerts */
   private void alert(AlertType type, String message, String header) {
      Alert alert = new Alert(type, message);
      alert.setHeaderText(header);
      alert.showAndWait();
   }
}
