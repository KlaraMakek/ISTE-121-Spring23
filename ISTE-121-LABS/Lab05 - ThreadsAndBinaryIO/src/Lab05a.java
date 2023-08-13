import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOError;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javafx.application.Application;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.*;

/**
 * Lab05 - Threads and Binary IO
 * 
 * @author Pete Lutz
 * @version 7-18-2018
 */

/**
 * @author KLara Makek
 * @version 21.2.2023. try 5
 *          ISTE121 - LAB05
 */
public class Lab05a extends Application implements EventHandler<ActionEvent> {
   // Window attributes
   Stage stage;
   Scene scene;

   // GUI components
   private TextField tfCities = new TextField();
   private TextField tfRecords = new TextField();
   private TextField tfObjSize = new TextField();
   private TextArea taCompleted = new TextArea();
   private TextArea taFound = new TextArea();
   private Button btnFind = new Button("Find");

   private final String ZIPCITYSTATE_PATH = "./ZipCityState.dat";
   private final String CITYARRAYLIST_PATH = "./CityArrayList.dat";

   private List<String> city;
   private ArrayList<FileThread> thredF;
   private ObjectOutputStream OOS;
   private DataOutputStream DOS;
   private File fileO;
   private int redCout;

   private int threadDone;
   private int threadCoun;

   /** main program */
   public static void main(String[] args) {
      launch(args);
   }

   /** constructor */
   public void start(Stage _stage) {
      // setup window
      stage = _stage;
      stage.setTitle("Data Accumulator");
      VBox root = new VBox(8);

      // Textfields in the Top
      FlowPane fpTop = new FlowPane(8, 8);
      fpTop.setAlignment(Pos.CENTER);
      fpTop.getChildren().addAll(new Label("Cities: "), tfCities,
            new Label("Records: "), tfRecords,
            new Label("Object Size: "), tfObjSize);

      tfCities.setEditable(false);
      tfRecords.setEditable(false);
      tfObjSize.setEditable(false);
      root.getChildren().add(fpTop);

      // Two text areas in the Center
      FlowPane fpCenter = new FlowPane(8, 8);
      fpCenter.setAlignment(Pos.CENTER);
      fpCenter.getChildren().addAll(taCompleted, taFound);

      // Use monospaced font
      Font newFont = Font.font("MONOSPACED");
      taCompleted.setFont(newFont);
      taFound.setFont(newFont);
      taCompleted.setPrefHeight(250);
      taCompleted.setPrefWidth(400);
      taFound.setPrefHeight(250);
      taFound.setPrefWidth(400);
      root.getChildren().add(fpCenter);

      // Find button in the Bottom
      FlowPane fpBot = new FlowPane(8, 8);
      fpBot.setAlignment(Pos.CENTER);
      fpBot.getChildren().add(btnFind);
      btnFind.setDisable(true);
      btnFind.setOnAction(this);
      root.getChildren().add(fpBot);

      city = Collection.synchronizedList(new ArrayList<String>());
      thredF = new ArrayList<FileThread>();
      try {
         fileO = new File(CITYARRAYLIST_PATH);
         DOS = new DataOutputStream(new FileOutputStream(new File(ZIPCITYSTATE_PATH)));
      } catch (FileNotFoundException e) {
         System.out.println("Output file not found!");
      }
      try {
         OOS = new ObjectOutputStream(new FileOutputStream(fileO));
      } catch (IOException e) {
         System.out.println("Initializing error!");
      }
      redCout = 0;
      threadDone = 0;
      threadCoun = 0;

      readFiles();

      scene = new Scene(root, 900, 325);
      stage.setScene(scene);
      stage.show();
   }

   // initialize and reed files
   private void readFiles() {
      int cou = 1;
      while (true) {
         try {
            FileThread fileTh = new FileThread(this, cou);
            cou++;
            thredF.add(fileTh);
            fileTh.start();
         } catch (FileNotFoundException e) {
            break;
         }
      }
      threadCoun = cou - 1;
   }

   // when all threads done, the boxes are full
   // writes in array
   // closes the steam
   public void threadDone() {
      System.out.println("All threads are done.");
      btnFind.setDisable(false);
      tfRecords.setText("" + redCout);
      tfCities.setText("" + city.size());

      try {
         OOS.writeObject(new ArrayList<String>());
      } catch (IOException e) {
         System.out.println("Faild path to: " + CITYARRAYLIST_PATH);
      }
      tfObjSize.setText("" + fileO.length());
      try {
         DOS.close();
         OOS.close();
      } catch (IOException e) {
         System.out.println("IOE");
      }

   }

   /** Button handler */
   public void handle(ActionEvent ae) {
      TextInputDialog tid = new TextInputDialog();
      tid.setTitle("Find city");
      tid.setHeaderText("");
      tid.setGraphic(null);
      tid.showAndWait();

      String cityToFInd = tid.getResult();
      int index = city.indexOf(cityToFInd);
      if (index >= 0) {
         taFound.appendText(String.format("%-20s was found at %7d\n", cityToFInd, index));
      } else {
         taFound.appendText(String.format("%-20s was found at %7d\n", cityToFInd, index));
      }

   }

   // adds city to array
   public void putInArray(String cityName) {
      city.add(cityName);
   }

   //writes in ZipCityState.dat
   public void writeFile(int zip, String city, String state) {
      try {
         DOS.writeInt(zip);
         DOS.writeUTF(city);
         DOS.writeUTF(state);
      } catch (IOException e) {
         System.out.println("IOE");
      }
      redCout++;
   }

   public void finishThred(FileThread thread, int recordsFound) {
      taCompleted.appendText(String.format("File %s done, bigest count is %d!\n", thread.getName(), recordsFound));
      threadDone++;
      if (threadDone == threadCoun) {
         threadDone();
      }
   }
}