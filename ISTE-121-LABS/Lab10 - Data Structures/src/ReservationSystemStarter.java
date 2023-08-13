import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.geometry.*;

import java.io.*;
import java.util.*;

/**
 * ReservationSystemStarter - Class to keep track of a reservation database
 * Starter file for Lab10
 * 
 * @author D. Patric
 * @version 2205
 */

/**
 * Reservation System
 * @author Klara Makek
 * @version 2023
 *          ISTE 121 LAB09
 */
public class ReservationSystemStarter extends Application implements EventHandler<ActionEvent> {
   // Main window items
   private Stage stage;
   private Scene scene;
   private VBox root = new VBox(8);

   // GUI components
   private TextArea taReservations = new TextArea();
   private Button btnSearch = new Button("Search");
   private TextField tfSearchFor = new TextField();
   private TextField tfConfirmation = new TextField();
   private TextField tfName = new TextField();
   private TextField tfWidget = new TextField();
   private TextField tfStartDateTime = new TextField();
   private Button btnCancel = new Button("Cancel");
   private Button btnSave = new Button("Save");
   private Button btnUpdate = new Button("Update");

   // *Data st structure
   private TreeMap<String, Reservation> map = new TreeMap<>();
   private final static String FILENAME = "RES_DATA.dat";

   /** main */
   public static void main(String[] args) {
      launch(args);
   }

   /** constructor */
   public void start(Stage _stage) {
      stage = _stage;
      stage.setTitle("Reservation System");

      // row1 - TextArea
      Font newFont = Font.font("MONOSPACED");
      taReservations.setFont(newFont);
      root.getChildren().add(taReservations);

      // row2 - search
      FlowPane fpRow2 = new FlowPane(8, 8);
      fpRow2.setAlignment(Pos.CENTER);
      tfSearchFor.setPrefColumnCount(10);
      fpRow2.getChildren().addAll(new Label("Confirmation #: "), tfSearchFor, btnSearch);
      root.getChildren().add(fpRow2);

      // row3 - current record 1 of 2
      FlowPane fpRow3 = new FlowPane(8, 8);
      fpRow3.setAlignment(Pos.CENTER);
      tfConfirmation.setPrefColumnCount(5);
      tfConfirmation.setEditable(true);
      tfName.setPrefColumnCount(10);
      tfName.setEditable(true);
      tfWidget.setPrefColumnCount(10);
      tfWidget.setEditable(true);
      fpRow3.getChildren().addAll(new Label("Conf #: "), tfConfirmation,
            new Label("Name #: "), tfName, new Label("Widget: "), tfWidget);
      root.getChildren().add(fpRow3);

      // row4 - current record 2 of 2
      FlowPane fpRow4 = new FlowPane(8, 8);
      fpRow4.setAlignment(Pos.CENTER);
      tfStartDateTime.setPrefColumnCount(15);
      fpRow4.getChildren().addAll(new Label("Start Date & Time: "), tfStartDateTime);
      root.getChildren().add(fpRow4);

      // row5
      FlowPane fpRow5 = new FlowPane(8, 8);
      fpRow5.setAlignment(Pos.CENTER);
      fpRow5.getChildren().addAll(btnCancel, btnSave, btnUpdate);
      root.getChildren().add(fpRow5);

      // button handlers
      btnUpdate.setOnAction(this);
      btnSearch.setOnAction(this);
      btnCancel.setOnAction(this);
      btnSave.setOnAction(this);

      scene = new Scene(root, 600, 325);
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
      dispData();
   }

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
         case "Cancel":
            doCancel();
            break;
         case "Save":
            doSave();
            break;
         case "Update":
            doUpdate();
            break;
      }
   }

   /**
    * we need do add it to the map
    */

   private void doSearch() {
      String targetKey = tfSearchFor.getText();
      // map.keySet().contains(targetKey)
      if (map.containsKey(targetKey)) {
         Reservation founRes = map.get(targetKey);

         // put data in the textArea
         tfConfirmation.setText(founRes.getConfirmation());
         tfName.setText(founRes.getName());
         tfWidget.setText(founRes.getWidget());
         tfStartDateTime.setText(founRes.getStartDateTime());
      } else {
         alert(AlertType.WARNING, "Reservation not found", "Reservation searched");
      }
   }

   /**
    * dispData
    * Display all reservations (a la toString) in the jta
    */
   private void dispData() {
      // * clear the list
      taReservations.setText(null);

      // create array list for name sorting
      ArrayList<Reservation> resArray = new ArrayList<>(map.values());
      Collections.sort(resArray, Collections.reverseOrder());

      for (Reservation res : resArray) {
         taReservations.appendText(res.toString() + "\n");
      }
      // !VERY IMPORTAnT
      /*
       * // for every key give me reservation
       * for (String key : map.keySet()) {
       * taReservations.appendText(map.get(key).toString() + "\n");
       * 
       * // * clear the list
       * 
       * }
       */
   }

   /** do Save ... button and window close */
   private void doSave() {

      DataOutputStream dos = null;
      try {

         dos = new DataOutputStream(new FileOutputStream(new File(FILENAME)));

         for (String conf : map.keySet()) {
            Reservation res = map.get(conf);
            dos.writeUTF(res.getConfirmation());
            dos.writeUTF(res.getName());
            dos.writeUTF(res.getWidget());
            dos.writeUTF(res.getStartDateTime());
         }
         try {
            dos.close();
         } catch (IOException ioe) {
            alert(AlertType.ERROR, "IOException closing output file: " + ioe,
                  "I/O Error - output file");
         }

         alert(AlertType.INFORMATION, "File written.", "File Save");

      } catch (FileNotFoundException e) {
         alert(AlertType.ERROR, "File not found exception: " + e,
               "File not found");
      } catch (IOException ioe) {
         alert(AlertType.ERROR, "IOException opening output file: " + ioe,
               "I/O Error - output file");
      } finally {
         if (dos != null) {
            try {
               dos.close();
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
      }
   }

   /** doSearch */
   private void doUpdate() {

      String targetKey = tfSearchFor.getText();
      // map.keySet().contains(targetKey)
      if (map.containsKey(targetKey)) {
         Reservation founRes = map.get(targetKey);

         founRes.setConfirmation(tfConfirmation.getText());
         founRes.setName(tfName.getText());
         founRes.setWidget(tfWidget.getText());
         founRes.setStartDateTime(tfStartDateTime.getText());

         // removes old target key
         map.remove(targetKey);
         // puts in the map
         map.put(tfConfirmation.getText().trim(), founRes);
         // displays
         dispData();
      } else {
         alert(AlertType.WARNING, "Reservation not found", "Reservation searched");
      }
   }

   /** doCancel */
   private void doCancel() {

      String targetKey = tfSearchFor.getText();
      // map.keySet().contains(targetKey)
      if (map.containsKey(targetKey)) {
         // * remove
         map.remove(targetKey);

         // put data in the textArea
         tfConfirmation.setText("");
         tfName.setText("");
         tfWidget.setText("");
         tfStartDateTime.setText("");
         dispData();
      } else {
         alert(AlertType.WARNING, "Reservation cant be canceld", "Reservation canceld");
      }

   }

   /** Read data from the file */
   private void readData() {
      DataInputStream dis = null;
      try {
         dis = new DataInputStream(new FileInputStream(new File(FILENAME)));
         while (dis.available() > 0) {
            String confNum = dis.readUTF();
            String name = dis.readUTF();
            String widget = dis.readUTF();
            String startDateTime = dis.readUTF();

            // * crate a new reservation
            Reservation res = new Reservation(confNum, name, widget, startDateTime);
            System.out.print(res);
            // * put in side of map
            map.put(confNum, res);
         }
      } catch (FileNotFoundException e) {
         e.printStackTrace();
      } catch (IOException oe) {
         oe.printStackTrace();
      }
   }

   private void alert(AlertType type, String message, String header) {
      Alert alert = new Alert(type, message);
      alert.setHeaderText(header);
      alert.showAndWait();
   }
}
